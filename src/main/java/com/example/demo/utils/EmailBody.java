package com.example.demo.utils;



import lombok.Builder;

@Builder
public record EmailBody(String to, String subject, String text) {

}
