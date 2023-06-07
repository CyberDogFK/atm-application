package com.antp.atmapplication.dto;

import com.antp.atmapplication.model.Role;
import lombok.Getter;
import lombok.Setter;

import java.util.List;

@Getter
@Setter
public class UserResponseDto {
    private Long id;
    private String name;
    private String password;
    private Role role;
    private List<Long> accountIds;
}
