package com.example.fakeestore.messages;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public class ErrorMessage {

    @JsonProperty(value = "error")
    private String error;
    private String message;
    @JsonProperty(value = "object")
    private Object errorObject;


}
