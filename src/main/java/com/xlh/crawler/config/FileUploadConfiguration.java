package com.xlh.crawler.config;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.web.servlet.MultipartConfigFactory;
import org.springframework.context.annotation.Bean;

import javax.servlet.MultipartConfigElement;

/**
 * 文件上传配置
 *
 * @author 单红宇(CSDN CATOOP)
 * @create 2017年3月11日
 */
public class FileUploadConfiguration {


    @Value("${file.save.path}")
    private String savePath;
    @Bean
    public MultipartConfigElement multipartConfigElement() {
        MultipartConfigFactory factory = new MultipartConfigFactory();
        // 设置文件大小限制 ,超出设置页面会抛出异常信息，
        // 这样在文件上传的地方就需要进行异常信息的处理了;
        factory.setMaxFileSize("256KB"); // KB,MB
        /// 设置总上传数据总大小
        factory.setMaxRequestSize("512KB");
        // Sets the directory location where files will be stored.
         factory.setLocation("d:/");
        return factory.createMultipartConfig();
    }
}