package com.hanre.fakeinsta.config;

import com.cloudinary.Cloudinary;
import com.cloudinary.utils.ObjectUtils;
import org.modelmapper.ModelMapper;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class MainConfig {

//    @Autowired
//    private Environment environment;

//    @Primary
//    @Bean
//    public DataSource getDataSource() {
//        DataSourceBuilder dataSourceBuilder = DataSourceBuilder.create();
//        dataSourceBuilder.driverClassName(environment.getProperty("spring.datasource.driverClassName"));
//        dataSourceBuilder.url(Crypto.performDecrypt(environment.getProperty("spring.datasource.url")));
//        dataSourceBuilder.username(Crypto.performDecrypt(environment.getProperty("spring.datasource.username")));//environment.getProperty("spring.datasource.user")
//        dataSourceBuilder.password(Crypto.performDecrypt(environment.getProperty("spring.datasource.password")));//environment.getProperty("spring.datasource.password")
//        return dataSourceBuilder.build();
//    }

    @Bean
    public ModelMapper getModelMapper() {
        return new ModelMapper();
    }

    @Bean
    public Cloudinary cloudinary(){
        return new Cloudinary(ObjectUtils.asMap(
                "cloud_name",CloudinaryConfig.getCloudName(),
                "api_key",CloudinaryConfig.getApiKey(),
                "api_secret",CloudinaryConfig.getApiSecret()));
    }

}