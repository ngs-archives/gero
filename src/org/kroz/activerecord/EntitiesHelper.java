package org.kroz.activerecord;

import java.lang.reflect.Field;
import java.util.Hashtable;

import org.kroz.activerecord.utils.ARConst;
import org.kroz.activerecord.utils.Logg;


/**
 * Provides convenience methods to handle entites and corresponding classes
 * 
 * @author Vladimir Kroz
 */
public class EntitiesHelper {
	private static final String AR_BASE_CLASS_NAME = ActiveRecordBase.class
			.getSimpleName();
	private static final String AR_PK_FIELD_JAVA_NAME = "id";

	/**
	 * Copies all fields from src to dst which have the same name and type
	 * 
	 * @param dst
	 *            destination object
	 * @param src
	 *            source object
	 * @return populated destination object on success, null otherwise
	 */
	static public <T1, T2> T1 copyFields(T1 dst, T2 src) {
		if (null == src) {
			Logg.w(ARConst.TAG, "(%t) %s error: null source object",
					EntitiesHelper.class.getName());
			return null;
		}
		if (null == dst) {
			Logg.w(ARConst.TAG, "(%t) %s error: null destination object",
					EntitiesHelper.class.getName());
			return null;
		}

		// Build list of fields in target object
		Hashtable<String, Field> dstFields = new Hashtable<String, Field>();
		for (Field field : dst.getClass().getFields()) {
			dstFields.put(field.getName(), field);
		}

		// Build list of fields in source object
		Hashtable<String, Field> srcFields = new Hashtable<String, Field>();
		for (Field field : src.getClass().getFields()) {
			srcFields.put(field.getName(), field);
		}

		try {
			copyPkFields(dst, src, dstFields, srcFields);
			copyDataFields(dst, src, dstFields, srcFields);
		} catch (SecurityException e) {
			Logg.w(ARConst.TAG, e, "(%t) %s error: %s",
					EntitiesHelper.class.getName(), e.getLocalizedMessage());
			return null;
		} catch (IllegalArgumentException e) {
			Logg.w(ARConst.TAG, e, "(%t) %s error: %s",
					EntitiesHelper.class.getName(), e.getLocalizedMessage());
			return null;
		} catch (NoSuchFieldException e) {
			Logg.w(ARConst.TAG, e, "(%t) %s error: %s",
					EntitiesHelper.class.getName(), e.getLocalizedMessage());
			return null;
		} catch (IllegalAccessException e) {
			Logg.w(ARConst.TAG, e, "(%t) %s error: %s",
					EntitiesHelper.class.getName(), e.getLocalizedMessage());
			return null;
		}

		return dst;
	}

	/**
	 * Copies all fields from src to dst which have the same name and type,
	 * except id or _id field
	 * 
	 * @param dst
	 *            destination object
	 * @param src
	 *            source object
	 * @return populated destination object on success, null otherwise
	 */
	static public <T1, T2> T1 copyFieldsWithoutID(T1 dst, T2 src) {
		if (null == src) {
			Logg.w(ARConst.TAG, "(%t) %s error: null source object",
					EntitiesHelper.class.getName());
			return null;
		}
		if (null == dst) {
			Logg.w(ARConst.TAG, "(%t) %s error: null source object",
					EntitiesHelper.class.getName());
			return null;
		}

		// Build list of fields in target object
		Hashtable<String, Field> dstFields = new Hashtable<String, Field>();
		for (Field field : dst.getClass().getFields()) {
			dstFields.put(field.getName(), field);
		}

		// Build list of fields in source object
		Hashtable<String, Field> srcFields = new Hashtable<String, Field>();
		for (Field field : src.getClass().getFields()) {
			srcFields.put(field.getName(), field);
		}

		try {
			copyDataFields(dst, src, dstFields, srcFields);
		} catch (IllegalArgumentException e) {
			e.printStackTrace();
			dst = null;
		} catch (IllegalAccessException e) {
			e.printStackTrace();
			dst = null;
		}

		return dst;
	}

	/**
	 * Copies ID field. If dst or src are subclasses of ActiveRecordBase, then
	 * copies id fields of parent class
	 * 
	 * @param dst
	 * @param src
	 * @param dstFields
	 * @throws NoSuchFieldException
	 * @throws SecurityException
	 * @throws IllegalAccessException
	 * @throws IllegalArgumentException
	 */
	private static <T2, T1> void copyPkFields(T1 dst, T2 src,
			Hashtable<String, Field> dstFields,
			Hashtable<String, Field> srcFields) throws SecurityException,
			NoSuchFieldException, IllegalArgumentException,
			IllegalAccessException {
		boolean srcIsAR = (src.getClass().getSuperclass().getSimpleName()
				.equals(AR_BASE_CLASS_NAME)) ? true : false;
		boolean dstIsAR = (dst.getClass().getSuperclass().getSimpleName()
				.equals(AR_BASE_CLASS_NAME)) ? true : false;
		boolean dstHasId = (dstFields.containsKey(AR_PK_FIELD_JAVA_NAME)) ? true
				: false;
		boolean srcHasId = (srcFields.containsKey(AR_PK_FIELD_JAVA_NAME)) ? true
				: false;

		if (srcIsAR && dstIsAR) {
			((ActiveRecordBase) dst)._id = ((ActiveRecordBase) src)._id;
		} else if (srcIsAR && dstHasId) {
			Field dstId = dst.getClass().getField("id");
			if (dstId.getType().getSimpleName().equals("long")) {
				dstId.setLong(dst, ((ActiveRecordBase) src)._id);
			} else {
				Logg.w(ARConst.TAG,
						"(%t) %s.copyPkFields(): field '%s.%s' must have type 'long'. Copy operation skipped",
						EntitiesHelper.class.getName(), dst.getClass()
								.getSimpleName(), dstId.getName());
				throw new IllegalArgumentException(
						String.format(
								"Field '%s.%s' must have type 'long'. Copy operation skipped",
								dst.getClass().getSimpleName(), dstId.getName()));
			}
		} else if (srcHasId && dstIsAR) {
			Field srcId = src.getClass().getField("id");
			if (srcId.getType().getSimpleName().equals("long")) {
				((ActiveRecordBase) dst)._id = srcId.getLong(src);
			} else if (srcId.getType().getSimpleName().equals("int")) {
				((ActiveRecordBase) dst)._id = srcId.getInt(src);
			} else {
				Logg.w(ARConst.TAG,
						"%s.copyPkFields(): field '%s.%s' must have type 'long' or 'int'. Copy operation skipped",
						EntitiesHelper.class.getName(), src.getClass()
								.getSimpleName(), srcId.getName());
				throw new IllegalArgumentException(
						String.format(
								"Field '%s.%s' must have type 'long' or 'int'. Copy operation skipped",
								src.getClass().getSimpleName(), srcId.getName()));
			}
		} else if (srcHasId && dstHasId) {
			Field srcId = src.getClass().getField("id");
			Field dstId = dst.getClass().getField("id");
			dstId.setLong(dst, srcId.getLong(src));
		}
	}

	private static <T2, T1> void copyDataFields(T1 dst, T2 src,
			Hashtable<String, Field> dstFields,
			Hashtable<String, Field> srcFields)
			throws IllegalArgumentException, IllegalAccessException {
		// Iterate through fields of source object
		for (Field srcField : srcFields.values()) {

			String srcFieldName = srcField.getName();
			// Skip ID field - it's copied by special method
			if (srcFieldName.equalsIgnoreCase("id"))
				continue;

			// If destination object has field corresponding with the field
			// in source object
			// Then copy value from source field to destination
			if (dstFields.containsKey(srcFieldName)) {

				// Get destination field from list
				Field dstField = dstFields.get(srcFieldName);

				// Additional check - fields should have similar type
				String srcFldTypeName = srcField.getType().getSimpleName();
				String dstFldTypeName = dstField.getType().getSimpleName();
				if (!srcFldTypeName.equals(dstFldTypeName))
					continue;

				// Assign values
				// Need this long constructto handle various types via
				// reflection mechanism
				if (dstFldTypeName.equals("long")) {
					long srcValue;
					srcValue = srcField.getLong(src);
					dstField.setLong(dst, srcValue);
				}
				if (dstFldTypeName.equals("int")) {
					int srcValue = srcField.getInt(src);
					dstField.setInt(dst, srcValue);
				} else if (dstFldTypeName.equals("short")) {
					short srcValue = srcField.getShort(src);
					dstField.setShort(dst, srcValue);
				} else if (dstFldTypeName.equals("float")) {
					float srcValue = srcField.getFloat(src);
					dstField.setFloat(dst, srcValue);
				} else if (dstFldTypeName.equals("double")) {
					double srcValue = srcField.getDouble(src);
					dstField.setDouble(dst, srcValue);
				} else if (dstFldTypeName.equals("boolean")) {
					boolean srcValue = srcField.getBoolean(src);
					dstField.setBoolean(dst, srcValue);
				} else {
					Object srcValue = srcField.get(src);
					dstField.set(dst, srcValue);
				}
			}
		}

	}
}
