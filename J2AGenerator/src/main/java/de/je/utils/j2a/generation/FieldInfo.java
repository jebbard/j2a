package de.je.utils.j2a.generation;

import java.util.List;
import java.util.Map;

public class FieldInfo {

	public FieldInfo(String fieldName, Class<?> fieldType, boolean isPkey, boolean isFkey, boolean isPrimitive,
		boolean isVersion, boolean isUnique, boolean isNullable, List<Class<?>> actualTypeArguments) {
		this.fieldName = fieldName;
		this.fieldType = fieldType;
		this.isPkey = isPkey;
		this.isFkey = isFkey;
		this.isPrimitive = isPrimitive;
		this.isVersion = isVersion;
		this.isUnique = isUnique;
		this.isNullable = isNullable;
		this.actualTypeArguments = actualTypeArguments;

		this.getterName = (FieldInfo.isBool(getFieldType()) ? "is" : "get") + getFieldNameFirstUppercase() + "()";
	}

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result
			+ ((actualTypeArgumentSurrogates == null) ? 0 : actualTypeArgumentSurrogates.hashCode());
		result = prime * result + ((actualTypeArguments == null) ? 0 : actualTypeArguments.hashCode());
		result = prime * result + ((fieldName == null) ? 0 : fieldName.hashCode());
		result = prime * result + ((fieldType == null) ? 0 : fieldType.hashCode());
		result = prime * result + ((getterName == null) ? 0 : getterName.hashCode());
		result = prime * result + (isFkey ? 1231 : 1237);
		result = prime * result + (isNullable ? 1231 : 1237);
		result = prime * result + (isPkey ? 1231 : 1237);
		result = prime * result + (isPrimitive ? 1231 : 1237);
		result = prime * result + (isUnique ? 1231 : 1237);
		result = prime * result + (isVersion ? 1231 : 1237);
		result = prime * result + ((typeSurrogate == null) ? 0 : typeSurrogate.hashCode());
		return result;
	}

	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		FieldInfo other = (FieldInfo) obj;
		if (actualTypeArgumentSurrogates == null) {
			if (other.actualTypeArgumentSurrogates != null)
				return false;
		} else if (!actualTypeArgumentSurrogates.equals(other.actualTypeArgumentSurrogates))
			return false;
		if (actualTypeArguments == null) {
			if (other.actualTypeArguments != null)
				return false;
		} else if (!actualTypeArguments.equals(other.actualTypeArguments))
			return false;
		if (fieldName == null) {
			if (other.fieldName != null)
				return false;
		} else if (!fieldName.equals(other.fieldName))
			return false;
		if (fieldType == null) {
			if (other.fieldType != null)
				return false;
		} else if (!fieldType.equals(other.fieldType))
			return false;
		if (getterName == null) {
			if (other.getterName != null)
				return false;
		} else if (!getterName.equals(other.getterName))
			return false;
		if (isFkey != other.isFkey)
			return false;
		if (isNullable != other.isNullable)
			return false;
		if (isPkey != other.isPkey)
			return false;
		if (isPrimitive != other.isPrimitive)
			return false;
		if (isUnique != other.isUnique)
			return false;
		if (isVersion != other.isVersion)
			return false;
		if (typeSurrogate == null) {
			if (other.typeSurrogate != null)
				return false;
		} else if (!typeSurrogate.equals(other.typeSurrogate))
			return false;
		return true;
	}

	@Override
	public String toString() {
		return "FieldInfo [fieldName=" + fieldName + ", fieldType=" + fieldType + ", isPkey=" + isPkey + ", isFkey="
			+ isFkey + ", isPrimitive=" + isPrimitive + ", isVersion=" + isVersion + ", isUnique=" + isUnique
			+ ", isNullable=" + isNullable + ", getterName=" + getterName + ", actualTypeArguments="
			+ actualTypeArguments + ", actualTypeArgumentSurrogates=" + actualTypeArgumentSurrogates
			+ ", typeSurrogate=" + typeSurrogate + "]";
	}

	/**
	 * Returns the value of the attribute actualTypeArguments.
	 *
	 * @return actualTypeArguments The value of the attribute actualTypeArguments.
	 */
	public List<Class<?>> getActualTypeArguments() {
		return actualTypeArguments;
	}

	/**
	 * Returns the value of the attribute getterName.
	 *
	 * @return getterName The value of the attribute getterName.
	 */
	public String getGetterName() {
		return getterName;
	}

	public String getFieldName() {
		return fieldName;
	}

	public String getFieldNameFirstUppercase() {
		return fieldName.substring(0, 1).toUpperCase() + fieldName.substring(1);
	}

	/**
	 * Returns the value of the attribute isVersion.
	 *
	 * @return isVersion The value of the attribute isVersion.
	 */
	public boolean isVersion() {
		return isVersion;
	}

	/**
	 * Returns the value of the attribute isUnique.
	 *
	 * @return isUnique The value of the attribute isUnique.
	 */
	public boolean isUnique() {
		return isUnique;
	}

	public Class<?> getFieldType() {
		return fieldType;
	}

	public boolean isPkey() {
		return isPkey;
	}

	public boolean isFkey() {
		return isFkey;
	}

	public boolean isPrimitive() {
		return isPrimitive;
	}

	/**
	 * Returns the value of the attribute isNullable.
	 *
	 * @return isNullable The value of the attribute isNullable.
	 */
	public boolean isNullable() {
		return isNullable;
	}

	public void setActualTypeArgumentSurrogates(Map<Class<?>, String> actualTypeArgumentSurrogates) {
		this.actualTypeArgumentSurrogates = actualTypeArgumentSurrogates;
	}

	public Map<Class<?>, String> getActualTypeArgumentSurrogates() {
		return actualTypeArgumentSurrogates;
	}

	/**
	 * Returns the value of the attribute typeSurrogate.
	 *
	 * @return typeSurrogate The value of the attribute typeSurrogate.
	 */
	public String getTypeSurrogate() {
		return typeSurrogate;
	}

	/**
	 * Sets the attribute typeSurrogate.
	 *
	 * @param typeSurrogate typeSurrogate The new value to set.
	 */
	public void setTypeSurrogate(String typeSurrogate) {
		this.typeSurrogate = typeSurrogate;
	}

	public static boolean isBool(Class<?> object) {
		return object.equals(Boolean.class) || object.equals(Boolean.TYPE);
	}

	public static boolean isFloat(Class<?> object) {
		return object.equals(Float.class) || object.equals(Float.TYPE);
	}

	public static boolean isDouble(Class<?> object) {
		return object.equals(Double.class) || object.equals(Double.TYPE);
	}

	public static boolean isLong(Class<?> object) {
		return object.equals(Long.class) || object.equals(Long.TYPE);
	}

	private final String fieldName;
	private final Class<?> fieldType;
	private final boolean isPkey;
	private final boolean isFkey;
	private final boolean isPrimitive;
	private final boolean isVersion;
	private final boolean isUnique;
	private final boolean isNullable;
	private final String getterName;

	private final List<Class<?>> actualTypeArguments;
	private Map<Class<?>, String> actualTypeArgumentSurrogates;

	private String typeSurrogate;
}
