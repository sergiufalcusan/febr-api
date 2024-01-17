package io.febr.api.mapper;

import io.febr.api.config.MapperConfiguration;
import io.febr.api.domain.Course;
import io.febr.api.dto.CourseDTO;
import org.mapstruct.Mapper;

@Mapper(config = MapperConfiguration.class)
public interface CourseMapper extends BaseMapper<CourseDTO.CreateResponse, Course>, BaseCreateMapper<CourseDTO.CreateRequest, Course> {
}
