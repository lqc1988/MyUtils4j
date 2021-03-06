package com.lqc.freemarker.hello;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileOutputStream;
import java.io.OutputStreamWriter;
import java.io.Writer;
import java.util.*;

import freemarker.template.Configuration;
import freemarker.template.Template;

/**
 * @ClassName Hello
 * @Description FreeMarker 测试
 * @Author liqinchao
 * @Date 2021/3/6 14:29
 * @Version 1.0
 **/
public class FreeMarkerDemo {

    /**
     * 最常见的问题：
     * java.io.FileNotFoundException: xxx does not exist. 解决方法：要有耐心
     * FreeMarker jar 最新的版本（2.3.23）提示 Configuration 方法被弃用
     * 代码自动生产基本原理：
     * 数据填充 freeMarker 占位符
     */

    private static final String TEMPLATE_PATH = "src/main/java/com/lqc/freemarker/hello/templates";
    private static final String CLASS_PATH = "src/main/java/com/lqc/freemarker/hello";

    public static void main(String[] args) {
//        hello();
        stringFreeMarker();
    }

    /**
     * 根据stringFreeMarker.ftl生成文件
     */
    static void stringFreeMarker() {
        // step1 创建freeMarker配置实例
        Configuration configuration = new Configuration();
        Writer out = null;
        try {
            // step2 获取模版路径
            configuration.setDirectoryForTemplateLoading(new File(TEMPLATE_PATH));
            // step3 创建数据模型
            Map<String, Object> dataMap = new HashMap<>();
            dataMap.put("name", "itdragon博客");
            dataMap.put("dateTime", new Date());

            List<User> users = new ArrayList<>();
            users.add(new User(1, "ITDragon 博客"));
            users.add(new User(2, "欢迎"));
            users.add(new User(3, "You！"));
            dataMap.put("users", users);
            // step4 加载模版文件
            Template template = configuration.getTemplate("stringFreeMarker.ftl");
            // step5 生成数据
            out = new OutputStreamWriter(System.out);
            // step6 输出文件
            template.process(dataMap, out);
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            try {
                if (null != out) {
                    out.flush();
                }
            } catch (Exception e2) {
                e2.printStackTrace();
            }
        }
    }

    /**
     * 根据hello.ftl生成文件
     */
    static void hello() {
        // step1 创建freeMarker配置实例
        Configuration configuration = new Configuration();
        Writer out = null;
        try {
            // step2 获取模版路径
            configuration.setDirectoryForTemplateLoading(new File(TEMPLATE_PATH));
            // step3 创建数据模型
            Map<String, Object> dataMap = new HashMap<String, Object>();
            dataMap.put("classPath", "com.lqc.freemarker.hello");
            dataMap.put("className", "AutoCodeDemo");
            dataMap.put("helloWorld", "通过简单的 <代码自动生产程序> 演示 FreeMarker的HelloWorld！");
            // step4 加载模版文件
            Template template = configuration.getTemplate("hello.ftl");
            // step5 生成数据
            File docFile = new File(CLASS_PATH + "\\" + "AutoCodeDemo.java");
            out = new BufferedWriter(new OutputStreamWriter(new FileOutputStream(docFile)));
            // step6 输出文件
            template.process(dataMap, out);
            System.out.println("^^^^^^^^^^^^^^^^^^^^^^^^AutoCodeDemo.java 文件创建成功 !");
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            try {
                if (null != out) {
                    out.flush();
                }
            } catch (Exception e2) {
                e2.printStackTrace();
            }
        }
    }

}