package com.jee.learn.thymeleaf.support;

import java.io.File;
import java.util.ArrayList;
import java.util.List;
import java.util.jar.JarFile;

/**
 * jar 检测工具
 * 
 * @author 1002360
 * @version 1.0<br/>
 *          修改记录:<br/>
 *          1.2018年1月22日 下午2:56:33 1002360 新建
 */
public class JarTools {

    public static void main(String[] args) {
        List<String> paths = new ArrayList<String>();
        paths = getAllFilePaths(new File("E:\\dev\\mavenHub"), paths);
        for (String path : paths) {
            System.out.println(path);
        }
    }

    private static List<String> getAllFilePaths(File filePath, List<String> filePaths) {
        File[] files = filePath.listFiles();
        if (files == null) {
            return filePaths;
        }
        for (File f : files) {
            if (!f.exists()) {
                continue;
            }
            if (f.isDirectory()) {
                // 打印子目录路径
                // filePaths.add(f.getPath());
                getAllFilePaths(f, filePaths);
            } else {

                if (ckJar(f)) {
                    // 打印文件路径
                    filePaths.add("异常: " + f.getPath());
                    continue;
                }
                if (ckCache(f)) {
                    // 打印文件路径
                    filePaths.add("清除缓存: " + f.getPath());
                    continue;
                }

            }
        }
        return filePaths;
    }

    // return ture is error
    private static boolean ckJar(File file) {

        // 跳过非jar文件
        if (!file.getPath().endsWith(".jar")) {
            return false;
        }

        try {
            JarFile jar = new JarFile(file);
            jar.close();
        } catch (Exception e) {
            e.printStackTrace();
            return true;
        }
        return false;
    }

    // 清理缓存
    private static boolean ckCache(File file) {
        if (!file.getPath().endsWith("-in-progress")) {
            return false;
        }
        
        
        File f = new File(file.getParent());
        
        if(!deleteDir(f)){
            // 清除失败可能是由于文件被占用导致的,需要退出EC手工清除
            System.out.println("清除失败: "+f.getPath());
        }
        
        return true;
    }

    /**
     * 递归删除目录下的所有文件及子目录下所有文件
     * 
     * @param dir
     *            将要删除的文件目录
     * @return boolean Returns "true" if all deletions were successful. If a
     *         deletion fails, the method stops attempting to delete and returns
     *         "false".
     */
    private static boolean deleteDir(File dir) {
        if (dir.isDirectory()) {
            String[] children = dir.list();
            // 递归删除目录中的子目录下
            for (int i = 0; i < children.length; i++) {
                boolean success = deleteDir(new File(dir, children[i]));
                if (!success) {
                    return false;
                }
            }
        }
        // 目录此时为空，可以删除
        return dir.delete();
    }

}
