package com.portalpolinema.backend.config; // <--- PENTING: sesuaikan dengan folder

import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.ResourceHandlerRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

@Configuration
public class StaticResourceConfig implements WebMvcConfigurer {

        @Value("${dir-uploads}")
        private String uploadDirUploads;

        @Override
        public void addResourceHandlers(ResourceHandlerRegistry registry) {
                // ubah backslash ke slash, lalu tambahkan prefix "file:"
                String locationUpload = "file:" + uploadDirUploads.replace("\\", "/") + "/";

                registry.addResourceHandler("/uploads/**")
                                .addResourceLocations(locationUpload);

        }
}
