package com.jangburich.config.s3;

import java.io.IOException;
import java.util.UUID;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import com.amazonaws.services.s3.AmazonS3;
import com.amazonaws.services.s3.model.DeleteObjectRequest;
import com.amazonaws.services.s3.model.ObjectMetadata;
import com.amazonaws.services.s3.model.PutObjectRequest;
import com.amazonaws.services.s3.model.PutObjectResult;

import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
@Service
public class S3Service {
    private final AmazonS3 amazonS3;
    @Value("${cloud.aws.s3.bucket}")
    private String bucket;

    private String changedImageName(String originName) {
        String random = UUID.randomUUID().toString();
        return random +'-' + originName;
    }

    public String uploadImageToS3(MultipartFile image) {
        String originName = image.getOriginalFilename();
        String ext = originName.substring(originName.lastIndexOf("."));
        String changedName = changedImageName(originName);
        ObjectMetadata metadata = new ObjectMetadata();
        metadata.setContentLength(image.getSize());
        metadata.setContentType("image/" + ext);

        try {
            PutObjectResult putObjectResult = amazonS3.putObject(new PutObjectRequest(
                    bucket, changedName, image.getInputStream(), metadata
            ));
        } catch (IOException e) {
            throw new RuntimeException("ImageUploadException");
        }
        return amazonS3.getUrl(bucket, changedName).toString();
    }

    public void deleteImageOnS3(String imgURL) {
        String fileName = imgURL.substring(imgURL.lastIndexOf("-"));;

        amazonS3.deleteObject(new DeleteObjectRequest(bucket, fileName));
    }
}