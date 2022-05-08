package com.example.fakeebay.messages;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.AllArgsConstructor;
import lombok.Getter;

@AllArgsConstructor
@Getter
public class ResponseMessage {

    @JsonProperty(value = "message")
    private String message;

}
