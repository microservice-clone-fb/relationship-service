package com.tam.relationship.repository.httpclient;

import org.springframework.cloud.openfeign.FeignClient;

import com.tam.relationship.configuration.AuthenticationRequestInterceptor;

@FeignClient(
        name = "file-service",
        url = "http://localhost:8084",
        configuration = {AuthenticationRequestInterceptor.class})
public interface FileClient {}
