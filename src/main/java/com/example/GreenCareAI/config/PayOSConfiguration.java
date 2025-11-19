package com.example.GreenCareAI.config;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import vn.payos.PayOS;
import vn.payos.core.ClientOptions;
@Configuration
public class PayOSConfiguration {

    @Value("${payos.client.id}")
    private String clientId;

    @Value("${payos.api.key}")
    private String apiKey;

    @Value("${payos.checksum.key}")
    private String checksumKey;

    @Value("${payos.api.baseurl}")
    private String baseUrl;

    @Bean
    public PayOS payOS() {
        // Khởi tạo ClientOptions bằng Builder (chuẩn cho V2.0.1)
        ClientOptions clientOptions = ClientOptions.builder()
                .clientId(clientId)
                .apiKey(apiKey)
                .checksumKey(checksumKey)
                .baseURL(baseUrl)
                .build();

        return new PayOS(clientOptions);
    }
}