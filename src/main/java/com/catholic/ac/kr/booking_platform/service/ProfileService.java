package com.catholic.ac.kr.booking_platform.service;

import com.catholic.ac.kr.booking_platform.components.UploadHandler;
import com.catholic.ac.kr.booking_platform.dto.ProfileDTO;
import com.catholic.ac.kr.booking_platform.dto.response.ApiResponse;
import com.catholic.ac.kr.booking_platform.exception.ResourceNotFoundException;
import com.catholic.ac.kr.booking_platform.mapper.UserMapper;
import com.catholic.ac.kr.booking_platform.model.User;
import com.catholic.ac.kr.booking_platform.repository.UserRepository;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

@Service
public class ProfileService {
    private final UserRepository userRepository;
    private final UploadHandler uploadHandler;

    public ProfileService(UserRepository userRepository, UploadHandler uploadHandler) {
        this.userRepository = userRepository;
        this.uploadHandler = uploadHandler;
    }

    @Cacheable(value = "profile", key = "{#userId}")
    public ApiResponse<ProfileDTO> getProfile(Long userId) {
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new ResourceNotFoundException("User not found"));

        ProfileDTO profile = UserMapper.userToUserDTO(user);

        return ApiResponse.success(HttpStatus.OK.value(), HttpStatus.OK.getReasonPhrase(),
                "Get profile successfully", profile);
    }

    @CacheEvict(value = "profile", allEntries = true)
    public ApiResponse<String> uploadAvatar(Long userId, MultipartFile file) {
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new ResourceNotFoundException("User not found"));

        String avatarUrl = uploadHandler.uploadFile(userId, file);

        user.setAvatarUrl(avatarUrl);

        userRepository.save(user);

        return ApiResponse.success(HttpStatus.OK.value(), HttpStatus.OK.getReasonPhrase(), avatarUrl);
    }
}
