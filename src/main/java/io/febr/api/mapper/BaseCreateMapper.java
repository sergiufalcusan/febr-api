package io.febr.api.mapper;

import java.util.List;

public interface BaseCreateMapper<T, V> {
    V createDtoToEntity(T dto);

    List<V> createDtosToEntitiesList(List<T> dtos);
}
