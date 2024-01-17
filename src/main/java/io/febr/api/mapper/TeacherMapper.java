package io.febr.api.mapper;

import io.febr.api.config.MapperConfiguration;
import io.febr.api.domain.Teacher;
import io.febr.api.dto.TeacherDTO;
import org.mapstruct.Mapper;

@Mapper(config = MapperConfiguration.class)
public interface TeacherMapper extends
        BaseMapper<TeacherDTO.RegisterResponse, Teacher>,
        BaseCreateMapper<TeacherDTO.RegisterRequest, Teacher> {
}
