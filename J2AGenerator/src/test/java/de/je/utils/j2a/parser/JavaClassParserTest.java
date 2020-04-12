/**
 *
 * {@link JavaClassParserTest}.java
 *
 * @author Jens Ebert
 *
 * @date 10.02.2020
 *
 */
package de.je.utils.j2a.parser;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.Set;
import java.util.concurrent.atomic.AtomicLong;
import java.util.stream.Collectors;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Disabled;
import org.junit.jupiter.api.Test;

import com.github.j2a.core.config.J2AConfiguration;
import com.github.j2a.core.parser.ReflectionJavaClassParser;
import com.github.j2a.core.parser.declaration.AbstractJavaElementDeclaration;
import com.github.j2a.core.parser.declaration.JavaClassDeclaration;
import com.github.j2a.core.parser.declaration.JavaClassType;
import com.github.j2a.core.parser.declaration.JavaElementVisibility;
import com.github.j2a.core.parser.declaration.JavaFieldDeclaration;
import com.github.j2a.core.parser.declaration.JavaMethodDeclaration;
import com.github.j2a.core.parser.declaration.JavaParameterDeclaration;

import de.je.utils.j2a.parser.TestClass.InnerTestClass;
import de.je.utils.j2a.parser.TestClass.TestEnum;

/**
 * {@link JavaClassParserTest}
 *
 */
class JavaClassParserTest {

	@Test
	public void parseClassDeclaration_forMostComplexClass_returnsAllClassProperties() {
		JavaClassDeclaration classDeclaration = new ReflectionJavaClassParser(
			new J2AConfiguration(Set.of(TestClass.class.getPackageName()))).parse(TestClass.class);

		assertClassDeclarationIsValid(classDeclaration, JavaClassType.CLASS, TestClass.class,
			JavaElementVisibility.PUBLIC, Object.class, false, false, Disabled.class);

		Assertions.assertEquals(2, classDeclaration.getImplementedInterfaces().size());
		assertHasReferencedClassWithType(TestInterface1.class, classDeclaration.getImplementedInterfaces());
		assertHasReferencedClassWithType(TestInterface2.class, classDeclaration.getImplementedInterfaces());
		Assertions.assertEquals(true, classDeclaration.isAbstract());
		Assertions.assertEquals(false, classDeclaration.isInnerClass());
		Assertions.assertEquals(false, classDeclaration.isStrictFp());
		Assertions.assertEquals(true, classDeclaration.isApplicationClass());
		Assertions.assertEquals(2, classDeclaration.getNestedClasses().size());
		Assertions.assertEquals(4, classDeclaration.getFields().size());
		Assertions.assertEquals(6, classDeclaration.getMethods().size());

		JavaClassDeclaration nestedEnumDeclaration = assertHasReferencedClassWithType(TestEnum.class,
			classDeclaration.getNestedClasses());

		assertClassDeclarationIsValid(nestedEnumDeclaration, JavaClassType.ENUM, TestEnum.class,
			JavaElementVisibility.PUBLIC, Enum.class, true, true);
		Assertions.assertEquals(0, nestedEnumDeclaration.getImplementedInterfaces().size());
		Assertions.assertEquals(false, nestedEnumDeclaration.isAbstract());
		Assertions.assertEquals(true, nestedEnumDeclaration.isInnerClass());
		Assertions.assertEquals(false, nestedEnumDeclaration.isStrictFp());
		Assertions.assertEquals(true, nestedEnumDeclaration.isApplicationClass());
		Assertions.assertEquals(0, nestedEnumDeclaration.getNestedClasses().size());
		Assertions.assertEquals(4, nestedEnumDeclaration.getFields().size());
		Assertions.assertEquals(3, nestedEnumDeclaration.getMethods().size());

		assertHasField(nestedEnumDeclaration, JavaElementVisibility.PUBLIC, TestEnum.class, "X", true, true);
		assertHasField(nestedEnumDeclaration, JavaElementVisibility.PUBLIC, TestEnum.class, "Y", true, true);
		assertHasField(nestedEnumDeclaration, JavaElementVisibility.PUBLIC, TestEnum.class, "Z", true, true);
		assertHasField(nestedEnumDeclaration, JavaElementVisibility.PRIVATE, TestEnum[].class, "ENUM$VALUES", true,
			true);

		// TODO assert enum constants (X,Y,Z + ENUM$VALUES
		// TODO assert enum generated methods (valueOf, values and Std Constructor)

		JavaClassDeclaration secondNestedClassDeclaration = assertHasReferencedClassWithType(InnerTestClass.class,
			classDeclaration.getNestedClasses());

		assertClassDeclarationIsValid(secondNestedClassDeclaration, JavaClassType.CLASS, InnerTestClass.class,
			JavaElementVisibility.PACKAGE_PRIVATE, ArrayList.class, true, true);
		Assertions.assertEquals(0, secondNestedClassDeclaration.getImplementedInterfaces().size());
		Assertions.assertEquals(false, secondNestedClassDeclaration.isAbstract());
		Assertions.assertEquals(true, secondNestedClassDeclaration.isInnerClass());
		Assertions.assertEquals(false, secondNestedClassDeclaration.isStrictFp());
		Assertions.assertEquals(true, secondNestedClassDeclaration.isApplicationClass());
		Assertions.assertEquals(0, secondNestedClassDeclaration.getNestedClasses().size());
		Assertions.assertEquals(2, secondNestedClassDeclaration.getFields().size());
		Assertions.assertEquals(2, secondNestedClassDeclaration.getMethods().size());

		JavaMethodDeclaration sncdFirstMethod = assertHasMethod(secondNestedClassDeclaration, "myInnerMethod", 0,
			JavaElementVisibility.PACKAGE_PRIVATE, void.class, false, false);
		Assertions.assertEquals(false, sncdFirstMethod.isAbstract());
		Assertions.assertEquals(false, sncdFirstMethod.isBridge());
		Assertions.assertEquals(true, sncdFirstMethod.isStrictFp());
		Assertions.assertEquals(false, sncdFirstMethod.isConstructor());
		Assertions.assertEquals(false, sncdFirstMethod.isDefault());
		Assertions.assertEquals(false, sncdFirstMethod.isNative());
		Assertions.assertEquals(false, sncdFirstMethod.isSynchronized());
		Assertions.assertEquals(false, sncdFirstMethod.isVarArgs());

		JavaFieldDeclaration sncdFirstField = assertHasField(secondNestedClassDeclaration,
			JavaElementVisibility.PRIVATE, long.class, "serialVersionUID", true, true);
		Assertions.assertEquals(false, sncdFirstField.isTransient());
		Assertions.assertEquals(false, sncdFirstField.isVolatile());

		JavaFieldDeclaration sncdSecondField = assertHasField(secondNestedClassDeclaration,
			JavaElementVisibility.PACKAGE_PRIVATE, int.class, "myInnerProp", false, false);
		Assertions.assertEquals(false, sncdSecondField.isTransient());
		Assertions.assertEquals(false, sncdSecondField.isVolatile());

		// Outer classes' fields
		JavaFieldDeclaration firstField = assertHasField(classDeclaration, JavaElementVisibility.PACKAGE_PRIVATE,
			Boolean.class, "TEST", false, true);
		Assertions.assertEquals(false, firstField.isTransient());
		Assertions.assertEquals(false, firstField.isVolatile());

		JavaFieldDeclaration secondField = assertHasField(classDeclaration, JavaElementVisibility.PUBLIC, String.class,
			"TEST_2", true, true);
		Assertions.assertEquals(false, secondField.isTransient());
		Assertions.assertEquals(false, secondField.isVolatile());

		JavaFieldDeclaration thirdField = assertHasField(classDeclaration, JavaElementVisibility.PRIVATE, int.class,
			"myInt", true, false);
		Assertions.assertEquals(true, thirdField.isTransient());
		Assertions.assertEquals(false, thirdField.isVolatile());

		JavaFieldDeclaration fourthField = assertHasField(classDeclaration, JavaElementVisibility.PROTECTED,
			AtomicLong.class, "x", false, false);
		Assertions.assertEquals(false, fourthField.isTransient());
		Assertions.assertEquals(true, fourthField.isVolatile());

		JavaMethodDeclaration firstMethod = assertHasMethod(classDeclaration, "myGenericMethod", 3,
			JavaElementVisibility.PUBLIC, List.class, false, true, SafeVarargs.class);
		Assertions.assertEquals(false, firstMethod.isAbstract());
		Assertions.assertEquals(false, firstMethod.isBridge());
		Assertions.assertEquals(false, firstMethod.isStrictFp());
		Assertions.assertEquals(false, firstMethod.isConstructor());
		Assertions.assertEquals(false, firstMethod.isDefault());
		Assertions.assertEquals(false, firstMethod.isNative());
		Assertions.assertEquals(false, firstMethod.isSynchronized());
		Assertions.assertEquals(true, firstMethod.isVarArgs());
		assertHasMethodParam(firstMethod, 0, "arg0", String.class, false);
		assertHasMethodParam(firstMethod, 1, "arg1", int.class, false);
		assertHasMethodParam(firstMethod, 2, "arg2", long[].class, false);

		JavaMethodDeclaration secondMethod = assertHasMethod(classDeclaration, TestClass.class.getSimpleName(), 2,
			JavaElementVisibility.PUBLIC, TestClass.class, false, false);

		Assertions.assertEquals(false, secondMethod.isAbstract());
		Assertions.assertEquals(false, secondMethod.isBridge());
		Assertions.assertEquals(false, secondMethod.isStrictFp());
		Assertions.assertEquals(true, secondMethod.isConstructor());
		Assertions.assertEquals(false, secondMethod.isDefault());
		Assertions.assertEquals(false, secondMethod.isNative());
		Assertions.assertEquals(false, secondMethod.isSynchronized());
		Assertions.assertEquals(false, secondMethod.isVarArgs());

		assertHasMethodParam(secondMethod, 0, "arg0", int.class, false);
		assertHasMethodParam(secondMethod, 1, "arg1", AtomicLong.class, false);

		JavaMethodDeclaration thirdMethod = assertHasMethod(classDeclaration, TestClass.class.getSimpleName(), 0,
			JavaElementVisibility.PACKAGE_PRIVATE, TestClass.class, false, false);
		Assertions.assertEquals(false, thirdMethod.isAbstract());
		Assertions.assertEquals(false, thirdMethod.isBridge());
		Assertions.assertEquals(false, thirdMethod.isStrictFp());
		Assertions.assertEquals(true, thirdMethod.isConstructor());
		Assertions.assertEquals(false, thirdMethod.isDefault());
		Assertions.assertEquals(false, thirdMethod.isNative());
		Assertions.assertEquals(false, thirdMethod.isSynchronized());
		Assertions.assertEquals(false, thirdMethod.isVarArgs());

		JavaMethodDeclaration fourthMethod = assertHasMethod(classDeclaration, "doStuff", 1,
			JavaElementVisibility.PUBLIC, void.class, false, false);
		Assertions.assertEquals(false, fourthMethod.isAbstract());
		Assertions.assertEquals(false, fourthMethod.isBridge());
		Assertions.assertEquals(false, fourthMethod.isStrictFp());
		Assertions.assertEquals(false, fourthMethod.isConstructor());
		Assertions.assertEquals(false, fourthMethod.isDefault());
		Assertions.assertEquals(false, fourthMethod.isNative());
		Assertions.assertEquals(true, fourthMethod.isSynchronized());
		Assertions.assertEquals(true, fourthMethod.isVarArgs());

		assertHasMethodParam(fourthMethod, 0, "arg0", String[].class, false);

		JavaMethodDeclaration fifthMethod = assertHasMethod(classDeclaration, "myStupidNativeMethod", 1,
			JavaElementVisibility.PROTECTED, Long.class, false, false);
		Assertions.assertEquals(false, fifthMethod.isAbstract());
		Assertions.assertEquals(false, fifthMethod.isBridge());
		Assertions.assertEquals(false, fifthMethod.isStrictFp());
		Assertions.assertEquals(false, fifthMethod.isConstructor());
		Assertions.assertEquals(false, fifthMethod.isDefault());
		Assertions.assertEquals(true, fifthMethod.isNative());
		Assertions.assertEquals(false, fifthMethod.isSynchronized());
		Assertions.assertEquals(false, fifthMethod.isVarArgs());

		assertHasMethodParam(fifthMethod, 0, "arg0", int.class, false);

		JavaMethodDeclaration sixthMethod = assertHasMethod(classDeclaration, "getMyInt", 0,
			JavaElementVisibility.PACKAGE_PRIVATE, int.class, false, false);
		Assertions.assertEquals(false, sixthMethod.isAbstract());
		Assertions.assertEquals(false, sixthMethod.isBridge());
		Assertions.assertEquals(false, sixthMethod.isStrictFp());
		Assertions.assertEquals(false, sixthMethod.isConstructor());
		Assertions.assertEquals(false, sixthMethod.isDefault());
		Assertions.assertEquals(false, sixthMethod.isNative());
		Assertions.assertEquals(false, sixthMethod.isSynchronized());
		Assertions.assertEquals(false, sixthMethod.isVarArgs());
		Assertions.assertEquals(0, sixthMethod.getParameters().size());
	}

	@Test
	public void parseClassDeclaration_forSimpleInterface_returnsAllInterfaceProperties() {
		JavaClassDeclaration classDeclaration = new ReflectionJavaClassParser(
			new J2AConfiguration(Set.of(TestInterface1.class.getPackageName()))).parse(TestInterface1.class);

		assertClassDeclarationIsValid(classDeclaration, JavaClassType.INTERFACE, TestInterface1.class,
			JavaElementVisibility.PUBLIC, null, false, false);
		Assertions.assertEquals(0, classDeclaration.getImplementedInterfaces().size());
		Assertions.assertEquals(true, classDeclaration.isAbstract());
		Assertions.assertEquals(false, classDeclaration.isInnerClass());
		Assertions.assertEquals(false, classDeclaration.isStrictFp());
		Assertions.assertEquals(true, classDeclaration.isApplicationClass());
		Assertions.assertEquals(0, classDeclaration.getNestedClasses().size());
		Assertions.assertEquals(0, classDeclaration.getFields().size());
		Assertions.assertEquals(1, classDeclaration.getMethods().size());

		JavaMethodDeclaration firstMethodDeclaration = assertHasMethod(classDeclaration, "doStuff", 1,
			JavaElementVisibility.PUBLIC, void.class, false, false);
		Assertions.assertEquals(false, firstMethodDeclaration.isAbstract());
		Assertions.assertEquals(false, firstMethodDeclaration.isBridge());
		Assertions.assertEquals(false, firstMethodDeclaration.isStrictFp());
		Assertions.assertEquals(false, firstMethodDeclaration.isConstructor());
		Assertions.assertEquals(true, firstMethodDeclaration.isDefault());
		Assertions.assertEquals(false, firstMethodDeclaration.isNative());
		Assertions.assertEquals(false, firstMethodDeclaration.isSynchronized());
		Assertions.assertEquals(true, firstMethodDeclaration.isVarArgs());
		Assertions.assertEquals(1, firstMethodDeclaration.getParameters().size());

		assertHasMethodParam(firstMethodDeclaration, 0, "arg0", String[].class, false);
	}

	private void assertClassDeclarationIsValid(JavaClassDeclaration classDeclaration, JavaClassType classType,
		Class<?> expectedType, JavaElementVisibility expectedVisibility, Class<?> expectedBaseClass,
		boolean expectedFinal, boolean expectedStatic, Class<?>... expectedClassAnnotations) {
		assertIsType(expectedType, classDeclaration);
		Assertions.assertEquals(classType, classDeclaration.getClassType());
		Assertions.assertEquals(expectedVisibility, classDeclaration.getVisibility());
		if (expectedBaseClass == null) {
			Assertions.assertNull(classDeclaration.getBaseClassOrInterface());
		} else {
			assertIsType(expectedBaseClass, classDeclaration.getBaseClassOrInterface());
		}

		assertHasAnnotations(classDeclaration, expectedClassAnnotations);
		Assertions.assertEquals(expectedFinal, classDeclaration.isFinal());
		Assertions.assertEquals(expectedStatic, classDeclaration.isStatic());
	}

	private void assertHasAnnotations(AbstractJavaElementDeclaration elementToCheck,
		Class<?>... expectedElementAnnotations) {
		Assertions.assertEquals(expectedElementAnnotations.length, elementToCheck.getAnnotations().size());

		for (Class<?> annotation : expectedElementAnnotations) {
			assertHasReferencedClassWithType(annotation, elementToCheck.getAnnotations());
		}
	}

	private JavaFieldDeclaration assertHasField(JavaClassDeclaration secondNestedClassDeclaration,
		JavaElementVisibility expectedVisibility, Class<?> expectedFieldType, String expectedFieldName,
		boolean expectedFinal, boolean expectedStatic, Class<?>... expectedFieldAnnotations) {
		JavaFieldDeclaration fieldToCheck = assertHasFieldWithName(expectedFieldName, secondNestedClassDeclaration);
		Assertions.assertEquals(expectedVisibility, fieldToCheck.getVisibility());
		assertIsType(expectedFieldType, fieldToCheck.getType());
		assertHasAnnotations(fieldToCheck, expectedFieldAnnotations);

		Assertions.assertEquals(expectedFinal, fieldToCheck.isFinal());
		Assertions.assertEquals(expectedStatic, fieldToCheck.isStatic());
		return fieldToCheck;
	}

	private JavaFieldDeclaration assertHasFieldWithName(String name, JavaClassDeclaration classDeclaration) {
		Optional<JavaFieldDeclaration> firstElementWithName = classDeclaration.getFields().stream()
			.filter(c -> c.getName().equals(name)).findFirst();

		Assertions.assertTrue(firstElementWithName.isPresent());

		return firstElementWithName.get();
	}

	private JavaMethodDeclaration assertHasMethod(JavaClassDeclaration classDeclaration, String expectedMethodName,
		int expectedParamCount, JavaElementVisibility expectedVisibility, Class<?> expectedReturnType,
		boolean expectedFinal, boolean expectedStatic, Class<?>... expectedAnnotations) {
		JavaMethodDeclaration method = assertHasMethodWithNameAndParamCount(expectedMethodName, classDeclaration,
			expectedParamCount);
		Assertions.assertEquals(expectedVisibility, method.getVisibility());
		assertIsType(expectedReturnType, method.getReturnType());
		Assertions.assertEquals(expectedFinal, method.isFinal());
		Assertions.assertEquals(expectedStatic, method.isStatic());
		assertHasAnnotations(method, expectedAnnotations);
		return method;
	}

	private void assertHasMethodParam(JavaMethodDeclaration fifthMethod, int paramIndex, String expectedParamName,
		Class<?> expectedParamType, boolean expectedFinal, Class<?>... expectedParamAnnotations) {
		JavaParameterDeclaration fifFirstParam = fifthMethod.getParameters().get(paramIndex);

		Assertions.assertEquals(expectedParamName, fifFirstParam.getName());
		assertIsType(expectedParamType, fifFirstParam.getType());
		Assertions.assertEquals(expectedFinal, fifFirstParam.isFinal());
		assertHasAnnotations(fifFirstParam, expectedParamAnnotations);
	}

	private JavaMethodDeclaration assertHasMethodWithNameAndParamCount(String name,
		JavaClassDeclaration classDeclaration, int paramCount) {
		Set<JavaMethodDeclaration> elementsWithName = classDeclaration.getMethods().stream()
			.filter(c -> c.getName().equals(name)).collect(Collectors.toSet());
		Optional<JavaMethodDeclaration> firstElementWithNameAndParamCount = elementsWithName.stream()
			.filter(c -> c.getParameters().size() == paramCount).findFirst();

		Assertions.assertTrue(firstElementWithNameAndParamCount.isPresent());

		return firstElementWithNameAndParamCount.get();
	}

	private JavaClassDeclaration assertHasReferencedClassWithType(Class<?> type,
		List<JavaClassDeclaration> classDeclarations) {
		Optional<JavaClassDeclaration> firstElementWithName = classDeclarations.stream().filter(c -> isType(c, type))
			.findFirst();

		Assertions.assertTrue(firstElementWithName.isPresent());

		return firstElementWithName.get();
	}

	private void assertIsType(Class<?> expectedType, JavaClassDeclaration classDeclaration) {
		Assertions.assertNotNull(classDeclaration);
		Assertions.assertEquals(expectedType.getSimpleName(), classDeclaration.getName());
		Assertions.assertEquals(expectedType.getPackageName(), classDeclaration.getPackageName());
		Assertions.assertEquals(expectedType.isPrimitive(), classDeclaration.isPrimitive());
	}

	private boolean isType(JavaClassDeclaration classDeclaration, Class<?> expectedType) {
		return expectedType.getSimpleName().equals(classDeclaration.getName())
			&& expectedType.getPackageName().equals(classDeclaration.getPackageName())
			&& expectedType.isPrimitive() == classDeclaration.isPrimitive();
	}
}
