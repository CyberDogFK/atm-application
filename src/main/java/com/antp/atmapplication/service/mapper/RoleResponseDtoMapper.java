package com.antp.atmapplication.service.mapper;

import com.antp.atmapplication.dto.RoleResponseDto;
import com.antp.atmapplication.model.Role;
import org.springframework.stereotype.Service;

@Service
public class RoleResponseDtoMapper implements ResponseDtoMapper<RoleResponseDto, Role> {
    @Override
    public RoleResponseDto mapToDto(Role model) {
        RoleResponseDto roleResponseDto = new RoleResponseDto();
        roleResponseDto.setId(model.getId());
        roleResponseDto.setName(model.getName().name());
        return roleResponseDto;
    }
}
