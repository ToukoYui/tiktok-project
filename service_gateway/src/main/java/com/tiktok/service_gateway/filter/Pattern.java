package com.tiktok.service_gateway.filter;

import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Component;

import java.util.List;

@Data
@Component
@ConfigurationProperties(prefix = "gateway")
public class Pattern {
    private List<String> whitelist;
}
