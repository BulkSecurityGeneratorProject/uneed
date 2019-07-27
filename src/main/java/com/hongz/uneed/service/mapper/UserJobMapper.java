package com.hongz.uneed.service.mapper;

import com.hongz.uneed.domain.*;
import com.hongz.uneed.service.dto.UserJobDTO;

import org.mapstruct.*;

/**
 * Mapper for the entity {@link UserJob} and its DTO {@link UserJobDTO}.
 */
@Mapper(componentModel = "spring", uses = {CategoryMapper.class, TagMapper.class, UserMapper.class})
public interface UserJobMapper extends EntityMapper<UserJobDTO, UserJob> {

    @Mapping(source = "category.id", target = "categoryId")
    @Mapping(source = "category.name", target = "categoryName")
    @Mapping(source = "user.id", target = "userId")
    @Mapping(source = "user.login", target = "userLogin")
    UserJobDTO toDto(UserJob userJob);

    @Mapping(source = "categoryId", target = "category")
    @Mapping(target = "removeTag", ignore = true)
    @Mapping(source = "userId", target = "user")
    UserJob toEntity(UserJobDTO userJobDTO);

    default UserJob fromId(Long id) {
        if (id == null) {
            return null;
        }
        UserJob userJob = new UserJob();
        userJob.setId(id);
        return userJob;
    }
}
