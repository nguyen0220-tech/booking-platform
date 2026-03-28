package booking_platform.service;

import booking_platform.dto.ApiResponse;
import booking_platform.model.User;
import booking_platform.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class UserService {
    private final UserRepository userRepository;

//    @PreAuthorize("hasRole('USER')")
    public ApiResponse<List<User>> getUsers() {
        return ApiResponse.success(HttpStatus.OK.value(), HttpStatus.OK.getReasonPhrase(),
                "All users found", userRepository.findAll());
    }
}
