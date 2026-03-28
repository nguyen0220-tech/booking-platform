package booking_platform.service;

import booking_platform.dto.PageInfo;
import booking_platform.dto.UserDTO;
import booking_platform.dto.response.ListResponse;
import booking_platform.mapper.UserMapper;
import booking_platform.model.User;
import booking_platform.projection.UserProjection;
import booking_platform.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class UserService {
    private final UserRepository userRepository;

//    @PreAuthorize("hasRole('ADMIN')")
    public ListResponse<UserDTO> getUsers(Long me, int page, int size) {
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
}
