/**
 *
 * {@link ReflectionJavaClassParser}.java
 *
 * @author Jens Ebert
 *
 * @date 10.02.2020
 *
 */
package com.github.j2a.core.parser;

import java.lang.annotation.Documented;
import java.lang.annotation.Inherited;
import java.lang.annotation.Retention;
import java.lang.annotation.Target;
import java.lang.reflect.AnnotatedElement;
import java.lang.reflect.Executable;
import java.lang.reflect.Field;
import java.lang.reflect.Modifier;
import java.lang.reflect.Parameter;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import com.github.j2a.core.config.J2AConfiguration;
import com.github.j2a.core.parser.declaration.JavaClassDeclaration;
import com.github.j2a.core.parser.declaration.JavaClassType;
import com.github.j2a.core.parser.declaration.JavaElementVisibility;
import com.github.j2a.core.parser.declaration.JavaFieldDeclaration;
import com.github.j2a.core.parser.declaration.JavaMethodDeclaration;
import com.github.j2a.core.parser.declaration.JavaParameterDeclaration;

/**
 * {@link ReflectionJavaClassParser} parses Java classes based on a {@link Class} instance.
 *
 * The following limitations apply to this kind of {@link JavaClassParser}:
 * <ul>
 * <li>Clearly, a reflection-based parser cannot extract method bodies or comments, neither Javadoc or standard Java
 * comments</li>
 * <li>Furthermore, we cannot get the imports defined in a compilation unit</li>
 * <li>Parameter names are only obtainable if the corresponding class was compiled with the -parameters flag since Java
 * 8, otherwise we get just numbered parameter names such as arg0, arg1 and so on</li>
 * <li>Of course, due to type erasure, we cannot get as much details about generic template arguments and concrete types
 * used as we could hope for</li>
 * <li>The order of nested classes, methods, fields or annotation of an element cannot be guaranteed, i.e. these
 * elements might not appear in the same order as defined in the source (or even class) file. Method and constructor
 * parameters are always guaranteed to be returned in their declaration order, though.</li>
 * <li>Any annotations with Retention SOURCE are eliminated by the compiler and do not appear in the class file, thus
 * they cannot be returned</li>
 * <li>The keyword final in front of parameters does not make it into the class file</li>
 * </ul>
 */
public class ReflectionJavaClassParser implements JavaClassParser<Class<?>> {

	private final J2AConfiguration configuration;

	private final Map<Class<?>, JavaClassDeclaration> parsedClassCache = new HashMap<>();

	public ReflectionJavaClassParser(J2AConfiguration configuration) {
		if (configuration == null) {
			throw new IllegalArgumentException("Configuration must not be null");
		}

		this.configuration = configuration;
	}

	@Override
	public JavaClassDeclaration parse(Class<?> classToParse) {
		return parseClass(classToParse);
	}

	private List<JavaClassDeclaration> parseAnnotations(AnnotatedElement annotatedElement) {
		if (annotatedElement == Target.class || annotatedElement == Retention.class
			|| annotatedElement == Documented.class || annotatedElement == Inherited.class) {
			return new ArrayList<>();
		}

		return Arrays.stream(annotatedElement.getDeclaredAnnotations()).map(a -> parseClass(a.annotationType()))
			.collect(Collectors.toList());
	}

	private JavaClassDeclaration parseClass(Class<?> classToParse) {
		if (classToParse == null) {
			return null;
		}

		if (parsedClassCache.containsKey(classToParse)) {
			return parsedClassCache.get(classToParse);
		}

		String packageName = classToParse.getPackageName();

		boolean isApplicationClass = configuration.isApplicationPackage(packageName);

		boolean isFinal = Modifier.isFinal(classToParse.getModifiers());
		String name = classToParse.getSimpleName();
		boolean isStatic = Modifier.isStatic(classToParse.getModifiers());
		JavaElementVisibility visibility = JavaElementVisibility.fromModifier(classToParse.getModifiers());
		boolean isAbstract = (classToParse.getModifiers() & Modifier.ABSTRACT) != 0;
		boolean isStrictFp = (classToParse.getModifiers() & Modifier.STRICT) != 0;
		JavaClassType classType = classToParse.isInterface() ? JavaClassType.INTERFACE
			: classToParse.isAnnotation() ? JavaClassType.ANNOTATION
				: classToParse.isEnum() ? JavaClassType.ENUM : JavaClassType.CLASS;

		boolean isInnerClass = classToParse.isMemberClass();

		JavaClassDeclaration javaClassDeclaration = new JavaClassDeclaration(classToParse, isFinal, name, visibility,
			isStatic, classType, isInnerClass, packageName, isAbstract, isStrictFp, isApplicationClass,
			classToParse.isPrimitive());

		parsedClassCache.put(classToParse, javaClassDeclaration);

		// The following properties are only determined if the class is an application
		// class - i.e. we do need to recurse deeper into the dependencies
		if (isApplicationClass) {
			javaClassDeclaration.setAnnotations(parseAnnotations(classToParse));
			javaClassDeclaration.setBaseClass(parseClass(classToParse.getSuperclass()));
			javaClassDeclaration.setImplementedInterfaces(
				Arrays.stream(classToParse.getInterfaces()).map(this::parseClass).collect(Collectors.toList()));
			List<JavaMethodDeclaration> methods = Arrays.stream(classToParse.getDeclaredMethods())
				.map(m -> parseExecutable(m, false, parseClass(m.getReturnType()), m.isDefault(), m.isBridge()))
				.collect(Collectors.toList());
			methods.addAll(Arrays.stream(classToParse.getDeclaredConstructors())
				.map(m -> parseExecutable(m, true, javaClassDeclaration, false, false)).collect(Collectors.toList()));

			javaClassDeclaration.setMethods(methods);
			javaClassDeclaration.setFields(
				Arrays.stream(classToParse.getDeclaredFields()).map(this::parseField).collect(Collectors.toList()));
			javaClassDeclaration.setNestedClasses(
				Arrays.stream(classToParse.getDeclaredClasses()).map(this::parseClass).collect(Collectors.toList()));
		}

		return javaClassDeclaration;
	}

	private JavaMethodDeclaration parseExecutable(Executable executableToParse, boolean isConstructor,
		JavaClassDeclaration returnType, boolean isDefault, boolean isBridge) {
		if (executableToParse == null) {
			throw new IllegalArgumentException("Constructor/method to parse must not be null");
		}

		boolean isFinal = Modifier.isFinal(executableToParse.getModifiers());
		String name = isConstructor ? executableToParse.getDeclaringClass().getSimpleName()
			: executableToParse.getName();
		List<JavaClassDeclaration> annotations = parseAnnotations(executableToParse);
		boolean isStatic = Modifier.isStatic(executableToParse.getModifiers());
		JavaElementVisibility visibility = JavaElementVisibility.fromModifier(executableToParse.getModifiers());

		boolean isAbstract = Modifier.isAbstract(executableToParse.getModifiers());
		boolean isStrictFp = Modifier.isStrict(executableToParse.getModifiers());
		boolean isSynchronized = Modifier.isSynchronized(executableToParse.getModifiers());
		boolean isNative = Modifier.isNative(executableToParse.getModifiers());
		boolean isSynthetic = executableToParse.isSynthetic();
		boolean isVarArgs = executableToParse.isVarArgs();

		List<JavaParameterDeclaration> parameterDeclarations = Arrays.stream(executableToParse.getParameters())
			.map(this::parseParameter).collect(Collectors.toList());

		return new JavaMethodDeclaration(isFinal, name, annotations, visibility, isStatic, isConstructor,
			isSynchronized, isNative, isAbstract, isStrictFp, isBridge, isSynthetic, isDefault, isVarArgs, returnType,
			parameterDeclarations);
	}

	private JavaFieldDeclaration parseField(Field fieldToParse) {
		if (fieldToParse == null) {
			throw new IllegalArgumentException("Field to parse must not be null");
		}

		boolean isFinal = Modifier.isFinal(fieldToParse.getModifiers());
		String name = fieldToParse.getName();
		List<JavaClassDeclaration> annotations = parseAnnotations(fieldToParse);
		boolean isStatic = Modifier.isStatic(fieldToParse.getModifiers());
		JavaElementVisibility visibility = JavaElementVisibility.fromModifier(fieldToParse.getModifiers());

		boolean isSynthetic = fieldToParse.isSynthetic();
		boolean isVolatile = Modifier.isVolatile(fieldToParse.getModifiers());
		boolean isTransient = Modifier.isTransient(fieldToParse.getModifiers());

		JavaClassDeclaration fieldType = parseClass(fieldToParse.getType());

		return new JavaFieldDeclaration(isFinal, name, annotations, visibility, isStatic, isVolatile, isTransient,
			fieldType, isSynthetic);
	}

	private JavaParameterDeclaration parseParameter(Parameter parameterToParse) {
		if (parameterToParse == null) {
			throw new IllegalArgumentException("Parameter to parse must not be null");
		}

		boolean isFinal = Modifier.isFinal(parameterToParse.getModifiers());
		String name = parameterToParse.getName();
		List<JavaClassDeclaration> annotations = parseAnnotations(parameterToParse);

		JavaClassDeclaration parameterType = parseClass(parameterToParse.getType());

		return new JavaParameterDeclaration(isFinal, name, annotations, parameterType);
	}
}
