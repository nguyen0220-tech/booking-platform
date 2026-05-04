package com.catholic.ac.kr.booking_platform.profile.core.strategy;

import com.catholic.ac.kr.booking_platform.helper.response.ApiResponse;
import com.catholic.ac.kr.booking_platform.profile.core.UpdateProfileType;
import com.catholic.ac.kr.booking_platform.profile.data.UpdateProfileRequest;
import com.catholic.ac.kr.booking_platform.user.data.User;

public interface ProfileStrategy {
    UpdateProfileType getUpdateProfileType();
    ApiResponse<String> updateProfile(User currentUser, UpdateProfileRequest request);
}
