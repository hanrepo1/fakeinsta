package com.hanre.fakeinsta.config;

import com.hanre.fakeinsta.security.Crypto;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.PropertySource;

@Configuration
@PropertySource("classpath:security.properties")
public class CloudinaryConfig {

    private static String cloudName;
    private static String apiKey;
    private static String apiSecret;

    public static String getCloudName() {
        return cloudName;
    }

    @Value("${cloudinary.cloud.name}")
    private void setCloudName(String cloudName) {
        this.cloudName = Crypto.performDecrypt(cloudName);
    }

    public static String getApiKey() {
        return apiKey;
    }

    @Value("${cloudinary.api.key}")
    private void setApiKey(String apiKey) {
        this.apiKey = Crypto.performDecrypt(apiKey);
    }

    public static String getApiSecret() {
        return apiSecret;
    }

    @Value("${cloudinary.api.secret}")
    public void setApiSecret(String apiSecret) {
        this.apiSecret = Crypto.performDecrypt(apiSecret);
    }
}