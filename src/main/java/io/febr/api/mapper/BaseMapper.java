package io.febr.api.mapper;

import org.mapstruct.MappingTarget;

import java.util.List;

public interface BaseMapper<T, V> {
    T toDto(V entity);

    V toEntity(T dto);

    List<T> toDtoList(List<V> entities);

    List<V> toEntitiesList(List<T> dtos);

    V toEntity(T dto, @MappingTarget V entity);
}
