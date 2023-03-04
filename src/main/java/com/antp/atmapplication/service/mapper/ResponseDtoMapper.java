package com.antp.atmapplication.service.mapper;

public interface ResponseDtoMapper<D, T> {
    D mapToDto(T model);
}
