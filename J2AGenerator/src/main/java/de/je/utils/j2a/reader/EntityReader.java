package de.je.utils.j2a.reader;

import java.lang.reflect.Field;
import java.lang.reflect.Modifier;
import java.lang.reflect.ParameterizedType;
import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToMany;
import javax.persistence.ManyToOne;
import javax.persistence.OneToMany;
import javax.persistence.OneToOne;
import javax.persistence.Table;
import javax.persistence.UniqueConstraint;
import javax.persistence.Version;

import de.je.utils.j2a.generation.FieldInfo;

public class EntityReader {

	private final static Class<Id> ANNOTATION_PKEY = Id.class;
	private final static Class<Version> VERSION = Version.class;
	private final static Class<OneToOne> ANNOTATION_ONE_TO_ONE = OneToOne.class;
	private final static Class<OneToMany> ANNOTATION_ONE_TO_MANY = OneToMany.class;
	private final static Class<ManyToOne> ANNOTATION_MANY_TO_ONE = ManyToOne.class;
	private final static Class<ManyToMany> ANNOTATION_MANY_TO_MANY = ManyToMany.class;
	private final static Class<JoinColumn> ANNOTATION_FKEY = JoinColumn.class;
	private final static Class<Entity> ENTITY = Entity.class;
	private final static Class<Table> TABLE = Table.class;
	private final static Class<Column> COLUMN = Column.class;

	public List<FieldInfo> getFieldInfos(Class<?> entityClass, String idClassName, String versionClassName,
		boolean defaultNullable) {
		if (entityClass == null)
			throw new IllegalArgumentException("Parameter 'entity' must not be null");

		if (entityClass.getAnnotation(ENTITY) == null)
			System.out
				.println("[WARNING] Class '" + entityClass.getName() + "' is NOT annotated with " + ENTITY.getName());

		List<FieldInfo> fieldInfosReturned = new ArrayList<FieldInfo>();

		try {
			Class<?> idClass = Class.forName(idClassName);
			Class<?> versionClass = Class.forName(versionClassName);

			Set<String> uniqueConstraintFields = determineUniqueConstraintFields(entityClass);

			Class<?> currentClass = entityClass;

			// Add fields of base classes
			do {
				fieldInfosReturned.addAll(
					getFieldInfosOfClass(currentClass, uniqueConstraintFields, idClass, versionClass, defaultNullable));

				currentClass = currentClass.getSuperclass();
			} while ((currentClass != Object.class));
		} catch (ClassNotFoundException e) {
			throw new IllegalArgumentException("Id class with name '" + idClassName + "' could not be found.");
		}

		return fieldInfosReturned;
	}

	private Set<String> determineUniqueConstraintFields(Class<?> entityClass) {
		Set<String> uniqueConstraintFields = new HashSet<String>();

		Table tableAnnotation = entityClass.getAnnotation(TABLE);

		if (tableAnnotation != null) {
			UniqueConstraint[] uniqueConstraints = tableAnnotation.uniqueConstraints();

			for (int i = 0; i < uniqueConstraints.length; i++) {
				UniqueConstraint constraint = uniqueConstraints[i];

				for (int j = 0; j < constraint.columnNames().length; j++)
					uniqueConstraintFields.add(constraint.columnNames()[j]);
			}
		}
		return uniqueConstraintFields;
	}

	private List<FieldInfo> getFieldInfosOfClass(Class<?> entityClass, Set<String> uniqueConstraintFields,
		Class<?> idClass, Class<?> versionClass, boolean defaultNullable) {
		List<FieldInfo> fieldInfosReturned = new ArrayList<FieldInfo>();
		Field[] fields = entityClass.getDeclaredFields();

		for (int i = 0; i < fields.length; i++) {
			Field field = fields[i];

			// Ignore static fields
			if (!Modifier.isStatic(field.getModifiers())) {
				Id pkeyAnnotation = field.getAnnotation(ANNOTATION_PKEY);
				OneToOne oneToOne = field.getAnnotation(ANNOTATION_ONE_TO_ONE);
				ManyToOne manyToOne = field.getAnnotation(ANNOTATION_MANY_TO_ONE);
				OneToMany oneToMany = field.getAnnotation(ANNOTATION_ONE_TO_MANY);
				ManyToMany manyToMany = field.getAnnotation(ANNOTATION_MANY_TO_MANY);
				JoinColumn fkeyAnnotation = field.getAnnotation(ANNOTATION_FKEY);
				Version versionAnnotation = field.getAnnotation(VERSION);
				Column columnAnnotation = field.getAnnotation(COLUMN);

				Class<?> fieldType = field.getType();

				if (pkeyAnnotation != null)
					fieldType = idClass;

				if (versionAnnotation != null)
					fieldType = versionClass;

				boolean isPkey = pkeyAnnotation != null;
				boolean isFkey = oneToOne != null || oneToMany != null || manyToMany != null || manyToOne != null;
				boolean isVersion = versionAnnotation != null;
				boolean isUnique = uniqueConstraintFields.contains(field.getName());
				boolean isNullable = defaultNullable;

				if (columnAnnotation != null)
					isNullable = columnAnnotation.nullable();

				else if (fkeyAnnotation != null)
					isNullable = fkeyAnnotation.nullable();

				Type genericType = field.getGenericType();

				List<Class<?>> genericActualArguments = new ArrayList<Class<?>>();
				if (genericType instanceof ParameterizedType) {
					Type[] typeArguments = ((ParameterizedType) genericType).getActualTypeArguments();
					for (int j = 0; j < typeArguments.length; j++)
						genericActualArguments.add((Class<?>) typeArguments[j]);
				}
				FieldInfo newFieldInfo = new FieldInfo(field.getName(), fieldType, isPkey, isFkey,
					fieldType.isPrimitive(), isVersion, isUnique, isNullable, genericActualArguments);

				fieldInfosReturned.add(newFieldInfo);
			}
		}
		return fieldInfosReturned;
	}
}
