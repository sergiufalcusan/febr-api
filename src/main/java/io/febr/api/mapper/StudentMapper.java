package io.febr.api.mapper;

import io.febr.api.config.MapperConfiguration;
import io.febr.api.domain.Student;
import io.febr.api.dto.StudentDTO;
import org.mapstruct.Mapper;

@Mapper(config = MapperConfiguration.class)
public interface StudentMapper extends
        BaseMapper<StudentDTO.RegisterResponse, Student>,
        BaseCreateMapper<StudentDTO.RegisterRequest, Student> {
}
