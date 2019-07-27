package com.hongz.uneed.service.mapper;

import com.hongz.uneed.domain.*;
import com.hongz.uneed.service.dto.UserJobDTO;

import org.mapstruct.*;

/**
 * Mapper for the entity {@link UserJob} and its DTO {@link UserJobDTO}.
 */
@Mapper(componentModel = "spring", uses = {UserMapper.class})
public interface UserJobMapper extends EntityMapper<UserJobDTO, UserJob> {

    @Override
    @Mapping(source = "category.id", target = "categoryId")
    @Mapping(source = "category.name", target = "categoryName")
    @Mapping(source = "user.id", target = "userId")
    @Mapping(source = "user.login", target = "userLogin")
    UserJobDTO toDto(UserJob userJob);

    @Override
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

    default Category categoryFromId(Long id) {
        if (id == null) {
            return null;
        }
        Category obj = new Category();
        obj.setId(id);
        return obj;
    }
}
