package com.jangburich.config;

import com.baroservice.api.BarobillApiProfile;
import com.baroservice.api.BarobillApiService;
import java.net.MalformedURLException;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class BarobillConfig {

    @Bean
    public BarobillApiService barobillApiService() throws MalformedURLException {
        return new BarobillApiService(BarobillApiProfile.TESTBED);
    }
}
