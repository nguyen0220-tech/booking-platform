package booking_platform.resolver;

import booking_platform.dto.RoleForBatchDTO;
import booking_platform.dto.UserDTO;
import booking_platform.dto.UserInfoDetailsDTO;
import booking_platform.dto.response.ListResponse;
import booking_platform.helper.ConverterForBatchMapping;
import booking_platform.model.User;
import booking_platform.service.RoleService;
import booking_platform.service.UserService;
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
public class UserResolver {
    private final UserService userService;
    private final RoleService roleService;

    @QueryMapping
    public ListResponse<UserDTO> users(
            @Argument Long me,
            @Argument int page,
            @Argument int size) {

        return userService.getUsers(me, page, size);
    }

    @BatchMapping(typeName = "User", field = "infoDetails")
    public Map<UserDTO, UserInfoDetailsDTO> infoDetails(List<UserDTO> users) {
        List<Long> userIds = getUserIds(users);

        List<User> userList = userService.getAllByIds(userIds);

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
