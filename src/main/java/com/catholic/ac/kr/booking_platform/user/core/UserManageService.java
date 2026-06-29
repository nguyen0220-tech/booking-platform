package com.catholic.ac.kr.booking_platform.user.core;

import com.catholic.ac.kr.booking_platform.helper.response.ApiResponse;
import com.catholic.ac.kr.booking_platform.helper.response.ListResponse;
import com.catholic.ac.kr.booking_platform.helper.response.PageInfo;
import com.catholic.ac.kr.booking_platform.infrastructure.exception.BadRequestException;
import com.catholic.ac.kr.booking_platform.infrastructure.exception.ResourceNotFoundException;
import com.catholic.ac.kr.booking_platform.infrastructure.security.userdetails.SecurityUtils;
import com.catholic.ac.kr.booking_platform.infrastructure.security.userdetails.UserDetailsImpl;
import com.catholic.ac.kr.booking_platform.user.constant.AdminActive;
import com.catholic.ac.kr.booking_platform.user.constant.FilterUser;
import com.catholic.ac.kr.booking_platform.user.constant.RoleName;
import com.catholic.ac.kr.booking_platform.user.constant.SearchType;
import com.catholic.ac.kr.booking_platform.user.core.event.UserBlockedEvent;
import com.catholic.ac.kr.booking_platform.user.data.User;
import com.catholic.ac.kr.booking_platform.user.data.UserRepository;
import com.catholic.ac.kr.booking_platform.user.dto.UserDTO;
import com.catholic.ac.kr.booking_platform.user.dto.UserMapper;
import lombok.RequiredArgsConstructor;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.http.HttpStatus;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Service;

import java.security.Principal;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class UserManageService {
    private final UserRepository userRepository;
    private final ApplicationEventPublisher eventPublisher;

    public Map<Long, UserDTO> batchLoaderUsers(List<Long> userIds, Principal principal) {
        List<User> users = getAllByUserIds(userIds, principal);
        return users.stream()
                .collect(Collectors.toMap(
                        User::getId,
                        UserMapper::toUserDTO
                ));
    }

    private List<User> getAllByUserIds(List<Long> userIds, Principal principal) {
        List<User> users = userRepository.findAllById(userIds);

        UserDetailsImpl userDetails = SecurityUtils.getUserDetails(principal);
        boolean isAdmin = SecurityUtils.isAdmin(principal);
        if (userDetails == null || !isAdmin) {

            return users.stream()
                    .peek(u -> {
                        u.setEmail(null);
                        u.setPhone(null);
                    })
                    .toList();
        }

        return users;
    }

    @PreAuthorize("hasRole('ADMIN')")
    @Cacheable(value = "userPage", key = "{#page, #size}")
    public ListResponse<UserDTO> getUsers(int page, int size) {
        Pageable pageable = PageRequest.of(page, size, Sort.by("id").ascending());

        Page<User> userPage = userRepository.findAll(pageable);

        Page<UserDTO> userDTOS = userPage.map(UserMapper::toUserDTO);

        List<UserDTO> rs = userDTOS.getContent();

        return new ListResponse<>(
                rs,
                new PageInfo(page, size, userPage.hasNext()));
    }

    @PreAuthorize("hasRole('ADMIN')")
    public UserDTO getUserWithType(SearchType type, String keyword) {
        User user = new User();
        switch (type) {
            case USERNAME -> user = userRepository.findUserByUsername(keyword);

            case EMAIL -> user = userRepository.findUserByEmail(keyword);
        }

        if (user == null) {
            return null;
        }

        return UserMapper.toUserDTO(user);
    }

    @PreAuthorize("hasRole('ADMIN')")
    public ListResponse<UserDTO> filterUser(int page, int size, FilterUser filter, boolean is) {
        Pageable pageable = PageRequest.of(page, size, Sort.by("id").ascending());
        Page<User> userPage = null;
        switch (filter) {
            case ENABLED -> userPage = userRepository.filterUserEnabled(pageable, is);
            case BLOCKED -> userPage = userRepository.filterUserBlocked(pageable, is);
        }

        Page<UserDTO> userDTOS = userPage.map(UserMapper::toUserDTO);

        List<UserDTO> rs = userDTOS.getContent();

        return new ListResponse<>(rs, new PageInfo(page, size, userDTOS.hasNext()));
    }

    @PreAuthorize("hasRole('ADMIN')")
    public ListResponse<UserDTO> filterUserByRole(int page, int size, RoleName name) {
        Pageable pageable = PageRequest.of(page, size, Sort.by("id").ascending());

        Page<User> userProjections = userRepository.findByRoleName(name, pageable);

        Page<UserDTO> userDTOS = userProjections.map(UserMapper::toUserDTO);

        List<UserDTO> rs = userDTOS.getContent();

        return new ListResponse<>(rs, new PageInfo(page, size, userProjections.hasNext()));
    }

    @PreAuthorize("hasRole('ADMIN')")
    @CacheEvict(value = "userInfos", allEntries = true)
    public ApiResponse<String> blockUser(Long currentUserId, Long userId, AdminActive active) {
        if (currentUserId.equals(userId)) {
            throw new BadRequestException("You cannot active yourself.");
        }

        User user = userRepository.findById(userId)
                .orElseThrow(() -> new ResourceNotFoundException("User not found"));

        switch (active) {
            case BLOCK -> {
                if (user.isBlocked()) {
                    return ApiResponse.success(HttpStatus.OK.value(), "ALREADY_BLOCKED", "User is already blocked");
                }
                user.setBlocked(true);
                eventPublisher.publishEvent(new UserBlockedEvent(userId));
            }

            case UNBLOCK -> {
                if (!user.isBlocked()) {
                    return ApiResponse.success(HttpStatus.OK.value(), "ALREADY_UNBLOCKED", "User is already unblocked");
                }
                user.setBlocked(false);

            }
        }

        userRepository.save(user);

        return ApiResponse.success(
                HttpStatus.OK.value(),
                HttpStatus.OK.getReasonPhrase(),
                active + " 성공");
    }
}
