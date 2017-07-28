package cn.sfpy.field;

import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;


/**
 * 表类的基础类，增加自动生成 Field[] ALL_FIELDS 方法
 * 
 * @author sunkn
 *
 */
public class AbstractTable {

	/**
	 * 该方法只返回在使用该方法之前已经初始化的 {@link kl.common.field.Field}变量
	 * 
	 * @return
	 */
	public List<Field> getListTableFields() {
		return getListTableFields(new String[]{});
	}
	
	/**
	 * 获取当前类定义的Field数组
	 * 
	 * @param excludes
	 *            排除不需要添加的
	 * @return
	 */
	public List<Field> getListTableFields(String... excludes) {
		List<Field> listFields = new ArrayList<Field>();
		try {
			java.lang.reflect.Field[] declaredFields = this.getClass()
					.getDeclaredFields();
			for (java.lang.reflect.Field field : declaredFields) {

				if (field.getType().equals(Field.class)) {
					Field fieldInstance = (Field) field.get(this);
					if (fieldInstance == null) {
						// Field还未初始化， 不放入List
					} else {
						if (excludes != null
								&& Arrays.binarySearch(excludes,
										fieldInstance.name) > -1) {
							continue;
						}
						listFields.add(fieldInstance);
					}
				}
			}

		} catch (Exception e) {
			throw new IllegalStateException(e);
		}
		return listFields;
	}

	/**
	 * 
	 * 该方法只返回在使用该方法之前已经初始化的 {@link kl.common.field.Field}变量
	 * 
	 * @return
	 */
	public Field[] getArrayTableFields() {
		List<Field> listTableFields = getListTableFields();
		return listTableFields.toArray(new Field[listTableFields.size()]);
	}
	
	public Field[] getArrayTableFields(String... excludes) {
		List<Field> listTableFields = getListTableFields(excludes);
		return listTableFields.toArray(new Field[listTableFields.size()]);
	}
}
