package com.jayden.dto;

public record ResponseDto<T>(String success, String error, T entity) {

}
