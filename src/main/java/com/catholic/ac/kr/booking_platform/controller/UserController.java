//package booking_platform.controller;
//
//import booking_platform.dto.response.ApiResponse;
//import booking_platform.dto.UserDTO;
//import booking_platform.security.userdetails.UserDetailsImpl;
//import booking_platform.service.UserService;
//import lombok.RequiredArgsConstructor;
//import org.springframework.data.web.PagedModel;
//import org.springframework.security.core.annotation.AuthenticationPrincipal;
//import org.springframework.web.bind.annotation.GetMapping;
//import org.springframework.web.bind.annotation.RequestMapping;
//import org.springframework.web.bind.annotation.RequestParam;
//import org.springframework.web.bind.annotation.RestController;
//
//
//@RestController
//@RequestMapping("user")
//@RequiredArgsConstructor
//public class UserController {
//    private final UserService userService;
//
////    @GetMapping("all")
////    public ApiResponse<PagedModel<UserDTO>> getUsers(
////            @AuthenticationPrincipal UserDetailsImpl userDetails,
////            @RequestParam int page,
////            @RequestParam int size) {
////        return userService.getUsers(userDetails.getId(), page, size);
////    }
//}
