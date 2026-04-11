package com.catholic.ac.kr.booking_platform.service;

import com.catholic.ac.kr.booking_platform.dto.PageInfo;
import com.catholic.ac.kr.booking_platform.dto.UserDTO;
import com.catholic.ac.kr.booking_platform.dto.response.ApiResponse;
import com.catholic.ac.kr.booking_platform.dto.response.ListResponse;
import com.catholic.ac.kr.booking_platform.mapper.UserMapper;
import com.catholic.ac.kr.booking_platform.model.User;
import com.catholic.ac.kr.booking_platform.projection.UserProjection;
import com.catholic.ac.kr.booking_platform.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.http.HttpStatus;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class UserService {
    private final UserRepository userRepository;

    @PreAuthorize("hasRole('ADMIN')")
    public ListResponse<UserDTO> getUsers(int page, int size) {
        Pageable pageable = PageRequest.of(page, size, Sort.by("id").ascending());

        Page<UserProjection> userProjections = userRepository.findAllUser(pageable);

        Page<UserDTO> userDTOS = userProjections.map(UserMapper::userDTO);

        List<UserDTO> rs = userDTOS.getContent();

        return new ListResponse<>(
                rs,
                new PageInfo(page, size, userProjections.hasNext()));
    }

    public List<User> getAllByIds(List<Long> ids) {
        return userRepository.findAllById(ids);
    }

    @PreAuthorize("hasRole('ADMIN')")
    public ApiResponse<String> blockUser(Long userId) {
        User user = userRepository.findById(userId)
                .orElseThrow(()-> new UsernameNotFoundException("User not found"));

        user.setBlocked(true);

        userRepository.save(user);

        return ApiResponse.success(HttpStatus.OK.value(), HttpStatus.OK.getReasonPhrase(), "User blocked");
    }
}
