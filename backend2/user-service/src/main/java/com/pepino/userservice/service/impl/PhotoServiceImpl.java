package com.pepino.userservice.service.impl;

import com.pepino.userservice.config.props.S3Props;
import com.pepino.userservice.entity.User;
import com.pepino.userservice.repository.UserRepository;
import com.pepino.userservice.service.PhotoService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import software.amazon.awssdk.core.sync.RequestBody;
import software.amazon.awssdk.services.s3.S3Client;
import software.amazon.awssdk.services.s3.model.DeleteObjectRequest;
import software.amazon.awssdk.services.s3.model.PutObjectRequest;

import java.util.Base64;
import java.util.UUID;

@Service
@RequiredArgsConstructor
public class PhotoServiceImpl implements PhotoService {
    private final S3Client s3Client;
    private final UserRepository userRepository;
    private final S3Props s3Props;

    @Override
    public User saveAccountPhoto(String photoBase64, UUID userId) {
        String pureBase64 = photoBase64.substring(photoBase64.indexOf(",") + 1);
        byte[] imageBytes = Base64.getDecoder().decode(pureBase64);

        String fileName = UUID.randomUUID() + ".jpg";

        s3Client.putObject(
                PutObjectRequest.builder().bucket(s3Props.getBucketName()).key(fileName).build(),
                RequestBody.fromBytes(imageBytes)
        );

        String fileUrl = String.format("http://localhost:8888/buckets/%s/%s", s3Props.getBucketName(), fileName);

        User user = userRepository.findById(userId)
                .orElseThrow(() -> new RuntimeException("Пользователь не найден"));

        user.setPhoto(fileUrl);

        return userRepository.save(user);
    }

    @Override
    public void deleteAccountPhoto(UUID userId) {
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new RuntimeException("Пользователь не найден"));

        String fileKey = user.getPhoto().substring(user.getPhoto().lastIndexOf("/") + 1);

        try {
            DeleteObjectRequest deleteRequest = DeleteObjectRequest.builder()
                    .bucket(s3Props.getBucketName())
                    .key(fileKey)
                    .build();

            s3Client.deleteObject(deleteRequest);

            user.setPhoto(null);
            userRepository.save(user);

        } catch (Exception e) {
            throw new RuntimeException("Ошибка при удалении файла из хранилища: " + e.getMessage());
        }
    }
}
