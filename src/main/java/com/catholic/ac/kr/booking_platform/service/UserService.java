package com.catholic.ac.kr.booking_platform.service;

import com.catholic.ac.kr.booking_platform.dto.response.PageInfo;
import com.catholic.ac.kr.booking_platform.dto.UserDTO;
import com.catholic.ac.kr.booking_platform.dto.response.ApiResponse;
import com.catholic.ac.kr.booking_platform.dto.response.ListResponse;
import com.catholic.ac.kr.booking_platform.enumdef.AdminActive;
import com.catholic.ac.kr.booking_platform.enumdef.FilterUser;
import com.catholic.ac.kr.booking_platform.enumdef.SearchType;
import com.catholic.ac.kr.booking_platform.exception.BadRequestException;
import com.catholic.ac.kr.booking_platform.exception.ResourceNotFoundException;
import com.catholic.ac.kr.booking_platform.mapper.UserMapper;
import com.catholic.ac.kr.booking_platform.model.User;
import com.catholic.ac.kr.booking_platform.projection.UserProjection;
import com.catholic.ac.kr.booking_platform.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.http.HttpStatus;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class UserService {
    private final UserRepository userRepository;

    @PreAuthorize("hasRole('ADMIN')")
    @Cacheable(value = "userPage", key = "{#page, #size}")
    public ListResponse<UserDTO> getUsers(int page, int size) {
        Pageable pageable = PageRequest.of(page, size, Sort.by("id").ascending());

        Page<UserProjection> userProjections = userRepository.findAllUser(pageable);

        Page<UserDTO> userDTOS = userProjections.map(UserMapper::userDTO);

        List<UserDTO> rs = userDTOS.getContent();

        return new ListResponse<>(
                rs,
                new PageInfo(page, size, userProjections.hasNext()));
    }

    @Cacheable(value = "userInfos", key = "#ids")
    public List<User> getAllByIds(List<Long> ids) {
        return userRepository.findAllById(ids);
    }

    @PreAuthorize("hasRole('ADMIN')")
    public UserDTO getUserWithType(SearchType type, String keyword) {
        UserProjection projection = null;
        switch (type) {
            case USERNAME -> projection = userRepository.findUserByUsername(keyword);

            case EMAIL -> projection = userRepository.findUserByEmail(keyword);
        }

        if (projection == null) {
            return null;
        }

        return UserMapper.userDTO(projection);
    }

    @PreAuthorize("hasRole('ADMIN')")
    public ListResponse<UserDTO> filterUser(int page, int size, FilterUser filter, boolean is) {
        Pageable pageable = PageRequest.of(page, size, Sort.by("id").ascending());
        Page<UserProjection> userProjections = null;
        switch (filter) {
            case ENABLED -> userProjections = userRepository.filterUserEnabled(pageable, is);
            case BLOCKED -> userProjections = userRepository.filterUserBlocked(pageable, is);
        }

        Page<UserDTO> userDTOS = userProjections.map(UserMapper::userDTO);

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
