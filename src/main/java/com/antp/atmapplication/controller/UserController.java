package com.antp.atmapplication.controller;

import com.antp.atmapplication.dto.AccountResponseDto;
import com.antp.atmapplication.dto.UserRequestDto;
import com.antp.atmapplication.dto.UserResponseDto;
import com.antp.atmapplication.model.Account;
import com.antp.atmapplication.model.User;
import com.antp.atmapplication.service.AccountService;
import com.antp.atmapplication.service.UserService;
import com.antp.atmapplication.service.mapper.RequestDtoMapper;
import com.antp.atmapplication.service.mapper.ResponseDtoMapper;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.parameters.P;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import java.util.List;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/user")
@Tag(name = "User controller")
public class UserController {
    private final static Logger logger = LoggerFactory.getLogger(UserController.class);
    private final UserService userService;
    private final AccountService accountService;
    private final ResponseDtoMapper<UserResponseDto, User> responseDtoMapper;
    private final RequestDtoMapper<UserRequestDto, User> requestDtoMapper;
    private final ResponseDtoMapper<AccountResponseDto, Account> accountResponseDtoMapper;

    public UserController(UserService userService,
                          AccountService accountService, ResponseDtoMapper<UserResponseDto, User> responseDtoMapper,
                          RequestDtoMapper<UserRequestDto, User> requestDtoMapper,
                          ResponseDtoMapper<AccountResponseDto, Account> accountResponseDtoMapper) {
        this.userService = userService;
        this.accountService = accountService;
        this.responseDtoMapper = responseDtoMapper;
        this.requestDtoMapper = requestDtoMapper;
        this.accountResponseDtoMapper = accountResponseDtoMapper;
    }

    @GetMapping
    @Operation(summary = "Get all users")
    public List<UserResponseDto> getAll() {
        return userService.getAll().stream()
                .map(responseDtoMapper::mapToDto)
                .collect(Collectors.toList());
    }

    @GetMapping("/{id}")
    @Operation(summary = "Get user with specific id")
    public UserResponseDto getById(@PathVariable
                                       @Parameter(name = "User id")
                                       Long id) {
        return responseDtoMapper.mapToDto(userService.getById(id));
    }

    @PutMapping("/{id}")
    @Operation(summary = "Change specific user")
    public UserResponseDto update(@PathVariable
                                      @Parameter(name = "User id") Long id,
                                  @RequestBody UserRequestDto userDto) {
        User user = requestDtoMapper.mapToModel(userDto);
        user.setId(id);
        return responseDtoMapper.mapToDto(userService.save(user));
    }

    @PutMapping("/{id}/account/{accountId}")
    @Operation(summary = "Add account to existing user")
    public UserResponseDto addAccountToUser(@PathVariable
                                                @Parameter(name = "User id")
                                                Long id,
                                            @PathVariable
                                            @Parameter(name = "Account id")
                                            Long accountId) {
        User user = userService.getById(id);
        Account account = accountService.findById(accountId);
        if (!account.getUser().equals(user)) {
            throw new RuntimeException("You can't add this account to some account");
        }
        user.getAccounts().add(account);
        return responseDtoMapper.mapToDto(userService.save(user));
    }

    @GetMapping("/my-accounts")
    @Operation(summary = "When you authenticated return your accounts")
    public List<AccountResponseDto> getCurrentUserAccounts(Authentication authentication) {
        return userService.findByName(authentication.getName()).orElseThrow(() ->
                        new RuntimeException("Can't find user with name "
                                + authentication.getName()))
                .getAccounts().stream()
                .map(accountResponseDtoMapper::mapToDto)
                .toList();
    }
}
