package com.catholic.ac.kr.booking_platform.components;

import com.catholic.ac.kr.booking_platform.repository.UserRepository;
import com.cloudinary.Cloudinary;
import com.cloudinary.utils.ObjectUtils;
import org.springframework.stereotype.Component;
import org.springframework.web.multipart.MultipartFile;

import java.time.LocalDateTime;
import java.util.Map;

@Component
public class UploadHandler {
    public final UserRepository userRepository;
    private final Cloudinary cloudinary;

    public UploadHandler(UserRepository userRepository, Cloudinary cloudinary) {
        this.userRepository = userRepository;
        this.cloudinary = cloudinary;
    }

    public String uploadFile(Long userId, MultipartFile file) {
        try {
            Map<?, ?> result = cloudinary.uploader().upload(
                    file.getBytes(), ObjectUtils.asMap(
                            "folder", "media_" + userId,
                            "public_id", "user_id" + "_" + LocalDateTime.now(), //when delete file
                            "overwrite", true));

            return result.get("secure_url").toString();
        } catch (Exception e) {
            throw new RuntimeException("Upload fail: ", e);
        }
    }
}
