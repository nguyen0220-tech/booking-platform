package com.catholic.ac.kr.booking_platform.resolver;

import com.catholic.ac.kr.booking_platform.dto.RoleForBatchDTO;
import com.catholic.ac.kr.booking_platform.dto.UserDTO;
import com.catholic.ac.kr.booking_platform.dto.UserInfoDetailsDTO;
import com.catholic.ac.kr.booking_platform.dto.response.ListResponse;
import com.catholic.ac.kr.booking_platform.enumdef.FilterUser;
import com.catholic.ac.kr.booking_platform.enumdef.SearchType;
import com.catholic.ac.kr.booking_platform.mapper.ConverterForBatchMapping;
import com.catholic.ac.kr.booking_platform.model.User;
import com.catholic.ac.kr.booking_platform.service.RoleService;
import com.catholic.ac.kr.booking_platform.service.UserManageService;
import lombok.RequiredArgsConstructor;
import org.springframework.graphql.data.method.annotation.Argument;
import org.springframework.graphql.data.method.annotation.BatchMapping;
import org.springframework.graphql.data.method.annotation.QueryMapping;
import org.springframework.stereotype.Controller;

import java.util.Collections;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.stream.Collectors;

@Controller
@RequiredArgsConstructor
public class UserManageResolver {
    private final UserManageService userManageService;
    private final RoleService roleService;

    @QueryMapping
    public ListResponse<UserDTO> users(
            @Argument int page,
            @Argument int size) {

        return userManageService.getUsers(page, size);
    }

    @QueryMapping
    public UserDTO user(
            @Argument SearchType searchType,
            @Argument String keyword) {
        return userManageService.getUserWithType(searchType, keyword);
    }

    @QueryMapping
    public ListResponse<UserDTO> usersFilter(
            @Argument int page,
            @Argument int size,
            @Argument FilterUser filter,
            @Argument boolean is
    ) {
        return userManageService.filterUser(page, size, filter, is);
    }

    @BatchMapping(typeName = "User", field = "infoDetails")
    public Map<UserDTO, UserInfoDetailsDTO> infoDetails(List<UserDTO> users) {
        List<Long> userIds = getUserIds(users);

        List<User> userList = userManageService.getAllByIds(userIds);

        Map<Long, UserInfoDetailsDTO> map = userList.stream()
                .collect(Collectors.toMap(
                        User::getId,
                        ConverterForBatchMapping::convertToUserInFfo
                ));

        return users.stream()
                .collect(Collectors.toMap(
                        u -> u,
                        u -> map.get(u.getId())
                ));
    }

    @BatchMapping(typeName = "User", field = "roles")
    public Map<UserDTO, Set<String>> roles(List<UserDTO> users) {
        List<Long> userIds = getUserIds(users);

        List<RoleForBatchDTO> roles = roleService.getAllByUserId(userIds);

        Map<Long, Set<String>> map = roles.stream()
                .collect(Collectors.groupingBy(
                        RoleForBatchDTO::getUserId,
                        Collectors.mapping(ConverterForBatchMapping::convertToRoleName, Collectors.toSet())
                ));

        return users.stream()
                .collect(Collectors.toMap(
                        u -> u,
                        u -> map.getOrDefault(u.getId(), Collections.emptySet())
                ));
    }

    private List<Long> getUserIds(List<UserDTO> users) {
        return users.stream()
                .map(UserDTO::getId)
                .distinct()
                .toList();
    }
}
