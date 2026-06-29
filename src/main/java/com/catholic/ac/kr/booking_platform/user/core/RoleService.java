package com.catholic.ac.kr.booking_platform.user.core;

import com.catholic.ac.kr.booking_platform.user.data.RoleRepository;
import com.catholic.ac.kr.booking_platform.user.dto.RoleForBatchDTO;
import lombok.RequiredArgsConstructor;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class RoleService {
    private final RoleRepository roleRepository;

    @PreAuthorize("hasRole('ADMIN')")
    @Cacheable(value = "userRoles", key = "#userIds")
    public List<RoleForBatchDTO> getAllByUserId(List<Long> userIds) {
        return roleRepository.findAllByUserIds(userIds);
    }

}
