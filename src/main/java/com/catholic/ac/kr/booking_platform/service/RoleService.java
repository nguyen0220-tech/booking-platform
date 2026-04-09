package com.catholic.ac.kr.booking_platform.service;

import com.catholic.ac.kr.booking_platform.dto.RoleForBatchDTO;
import com.catholic.ac.kr.booking_platform.repository.RoleRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class RoleService {
    private final RoleRepository roleRepository;

    public List<RoleForBatchDTO> getAllByUserId(List<Long> userId) {
        return  roleRepository.findAllByUserIds(userId);
    }

}
