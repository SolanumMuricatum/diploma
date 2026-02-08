package com.pepino.albumservice.service.impl;

import com.pepino.albumservice.config.props.S3Props;
import com.pepino.albumservice.entity.Photo;
import com.pepino.albumservice.repository.PhotoRepository;
import com.pepino.albumservice.service.PhotoService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import software.amazon.awssdk.core.sync.RequestBody;
import software.amazon.awssdk.services.s3.S3Client;
import software.amazon.awssdk.services.s3.model.DeleteObjectRequest;
import software.amazon.awssdk.services.s3.model.PutObjectRequest;

import java.util.Base64;
import java.util.List;
import java.util.UUID;

@Service
@RequiredArgsConstructor
public class PhotoServiceImpl implements PhotoService {

    private final S3Client s3Client;
    private final PhotoRepository photoRepository;
    private final S3Props s3Props;

    @Override
    public Photo savePhoto(Photo photoDto) {
        String pureBase64 = photoDto.getImage().substring(photoDto.getImage().indexOf(",") + 1);
        byte[] imageBytes = Base64.getDecoder().decode(pureBase64);

        String fileName = UUID.randomUUID() + ".jpg";

        s3Client.putObject(
                PutObjectRequest.builder().bucket(s3Props.getBucketName()).key(fileName).build(),
                RequestBody.fromBytes(imageBytes)
        );

        String fileUrl = String.format("http://localhost:8888/buckets/%s/%s", s3Props.getBucketName(), fileName);

        Photo photo = new Photo();
        photo.setId(UUID.randomUUID());
        photo.setAlbumId(photoDto.getAlbumId());
        photo.setUserId(photoDto.getUserId());
        photo.setImage(fileUrl);

        photoRepository.save(photo);
        return photo;
    }

    @Override
    public List<Photo> getAllPhotos(UUID albumId, UUID userId) {
        return photoRepository.findAllByAlbumIdAndUserId(albumId, userId);
    }

    @Override
    public void deletePhoto(UUID photoId) {
        Photo photo = photoRepository.findById(photoId)
                .orElseThrow(() -> new RuntimeException("Фото не найдено"));

        String fileKey = photo.getImage().substring(photo.getImage().lastIndexOf("/") + 1);

        try {
            DeleteObjectRequest deleteRequest = DeleteObjectRequest.builder()
                    .bucket(s3Props.getBucketName())
                    .key(fileKey)
                    .build();

            s3Client.deleteObject(deleteRequest);

            photoRepository.delete(photo);

        } catch (Exception e) {
            throw new RuntimeException("Ошибка при удалении файла из хранилища: " + e.getMessage());
        }
    }
}
