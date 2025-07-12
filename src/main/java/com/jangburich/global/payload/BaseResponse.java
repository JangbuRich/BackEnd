package com.jangburich.global.payload;

import com.fasterxml.jackson.annotation.JsonProperty;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Builder;
import lombok.Data;

import java.time.LocalDateTime;

@Schema(description = "API 기본 응답")
@Data
@Builder
public class BaseResponse <T>{
    @JsonProperty("data")
    private T data;

    @JsonProperty("transaction_time")
    private LocalDateTime transactionTime;

    @JsonProperty("description")
    private String description;

    public BaseResponse() {}

    public BaseResponse(T data, LocalDateTime transactionTime, String description){
        this.data = data;
        this.transactionTime = transactionTime;
        this.description=description;
    }
}