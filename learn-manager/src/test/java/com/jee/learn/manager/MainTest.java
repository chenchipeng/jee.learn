package com.jee.learn.manager;

import org.apache.commons.lang3.StringUtils;

import com.jee.learn.manager.util.base.Platforms;
import com.jee.learn.manager.util.base.annotation.Nullable;

public class MainTest {

    public static void main(String[] args) {
        System.out.println(getParentPath(Platforms.WORKING_DIR));
    }

    /**
     * 获得上层目录的路径
     */
    public static String getParentPath(String path) {
        String parentPath = path;

        if (Platforms.FILE_PATH_SEPARATOR.equals(parentPath)) {
            return parentPath;
        }

        parentPath = removeEnd(parentPath, Platforms.FILE_PATH_SEPARATOR_CHAR);

        int idx = parentPath.lastIndexOf(Platforms.FILE_PATH_SEPARATOR_CHAR);
        if (idx >= 0) {
            parentPath = parentPath.substring(0, idx + 1);
        } else {
            parentPath = Platforms.FILE_PATH_SEPARATOR;
        }

        return parentPath;
    }

    /**
     * 如果结尾字符为c, 去除掉该字符.
     */
    public static String removeEnd(final String s, final char c) {
        if (endWith(s, c)) {
            return s.substring(0, s.length() - 1);
        }
        return s;
    }

    /**
     * 判断字符串是否以字母结尾
     * 
     * 如果字符串为Null或空，返回false
     */
    public static boolean endWith(@Nullable CharSequence s, char c) {
        if (StringUtils.isEmpty(s)) {
            return false;
        }
        return s.charAt(s.length() - 1) == c;
    }

}
