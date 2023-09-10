package com.test.imageapplication.service;

import com.test.imageapplication.entity.DBImage;
import com.test.imageapplication.repositiry.ImageRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;
import software.amazon.awssdk.core.SdkBytes;
import software.amazon.awssdk.core.sync.RequestBody;
import software.amazon.awssdk.services.rekognition.model.DetectLabelsRequest;
import software.amazon.awssdk.services.rekognition.model.Image;
import software.amazon.awssdk.services.s3.model.ObjectCannedACL;
import software.amazon.awssdk.services.s3.model.PutObjectRequest;

import java.io.IOException;
import java.io.InputStream;
import java.util.List;

@Service
@RequiredArgsConstructor
public class ImageService {

    private final ImageRepository imageRepository;

    public void uploadImage(MultipartFile file) throws IOException {
        imageRepository.save(DBImage.builder()
                .name(file.getOriginalFilename())
                .url(putImageToS3(file))
                .content(getContent(file.getInputStream()))
                .build());
    }

    public List<DBImage> getAll(String key) {
        return imageRepository.findAllByKey(key.toLowerCase());
    }

    private String putImageToS3(MultipartFile file) throws IOException {
        var request = PutObjectRequest.builder()
                .bucket(AWSUtil.BUCKET_NAME)
                .key(file.getSize() + file.getOriginalFilename())
                .acl(ObjectCannedACL.PUBLIC_READ)
                .build();

        AWSUtil.getS3Client().putObject(request, RequestBody.fromBytes(file.getBytes()));
        return "https://" + AWSUtil.BUCKET_NAME + ".s3." + AWSUtil.AWS_REGION + ".amazonaws.com/" + request.key();
    }

    private List<String> getContent(InputStream inputStream) {
        var sourceBytes = SdkBytes.fromInputStream(inputStream);
        var image = Image.builder()
                .bytes(sourceBytes)
                .build();

        var detectLabelsResponse = AWSUtil.getRekognitionClient()
                .detectLabels(DetectLabelsRequest
                        .builder()
                        .image(image)
                        .build());

        return detectLabelsResponse
                .labels()
                .stream()
                .map(label -> label.name().toLowerCase())
                .toList();
    }
}
