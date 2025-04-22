package com.pcrt.Pcrt.config;

import com.cloudinary.Cloudinary;
import com.cloudinary.utils.ObjectUtils;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class CloudinaryConfig {
    @Bean
    public Cloudinary cloudinary() {
        return new Cloudinary(ObjectUtils.asMap(
        "cloud_name", "dwdvnztnn",
        "api_key", "571438538929217",
        "api_secret", "ZdK569SSxGMgAcDKPwatx2Lores",
        "secure", true));
    }
}
