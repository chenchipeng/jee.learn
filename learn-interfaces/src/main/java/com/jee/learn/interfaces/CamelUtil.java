package com.jee.learn.interfaces;

import org.junit.Test;

import com.google.common.base.CaseFormat;

public class CamelUtil {

	@Test
	public void batchTest() {
		String str = "TEST_DATA";
		System.out.println(str + " -> " + upperUnderscoreToUpperCamel(str));

		str = "TEST_DATA";
		System.out.println(str + " -> " + upperUnderscoreToLowerCamel(str));

		str = "test_data";
		System.out.println(str + " -> " + lowerUnderscoreToUpperCamel(str));

		str = "test_data";
		System.out.println(str + " -> " + lowerUnderscoreToLowerCamel(str));

		str = "test_data";
		System.out.println(str + " -> " + toClassName(str));
		str = "TEST_DATA";
		System.out.println(str + " -> " + toClassName(str));

		str = "test_data";
		System.out.println(str + " -> " + toFieldName(str));
		str = "TEST_DATA";
		System.out.println(str + " -> " + toFieldName(str));
	}

	/**
	 * 根据驼峰命名规则生成类名
	 * 
	 * @param str
	 * @return
	 */
	public static String toClassName(String str) {
		return lowerUnderscoreToUpperCamel(str.toLowerCase());
	}

	/**
	 * 根据驼峰命名规则生成字段
	 * 
	 * @param str
	 * @return
	 */
	public static String toFieldName(String str) {
		return lowerUnderscoreToLowerCamel(str.toLowerCase());
	}

	/**
	 * TEST_DATA to TestData
	 * 
	 * @param str
	 * @return
	 */
	public static String upperUnderscoreToUpperCamel(String str) {
		return CaseFormat.UPPER_UNDERSCORE.to(CaseFormat.UPPER_CAMEL, str);
	}

	/**
	 * TEST_DATA to testData
	 * 
	 * @param str
	 * @return
	 */
	public static String upperUnderscoreToLowerCamel(String str) {
		return CaseFormat.UPPER_UNDERSCORE.to(CaseFormat.LOWER_CAMEL, str);
	}

	/**
	 * test_data to TestData
	 * 
	 * @param str
	 * @return
	 */
	public static String lowerUnderscoreToUpperCamel(String str) {
		return CaseFormat.LOWER_UNDERSCORE.to(CaseFormat.UPPER_CAMEL, str);
	}

	/**
	 * test_data to testData
	 * 
	 * @param str
	 * @return
	 */
	public static String lowerUnderscoreToLowerCamel(String str) {
		return CaseFormat.LOWER_UNDERSCORE.to(CaseFormat.LOWER_CAMEL, str);
	}

}
