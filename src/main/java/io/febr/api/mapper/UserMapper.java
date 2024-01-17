package io.febr.api.mapper;

import io.febr.api.config.MapperConfiguration;
import io.febr.api.domain.User;
import io.febr.api.dto.AuthDTO;
import org.mapstruct.Mapper;

@Mapper(config = MapperConfiguration.class)
public interface UserMapper extends
        BaseMapper<AuthDTO.UserResponse, User> {
}
