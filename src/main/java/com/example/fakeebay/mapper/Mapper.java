package com.example.fakeebay.mapper;

public interface Mapper<E, D> {

    D convertEntityToDto(E e);
    E convertDtoToEntity(D d);
}
