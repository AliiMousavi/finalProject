package com.example.phase2.dto;

public record ResponseDto<T>(String message, T info) {

}