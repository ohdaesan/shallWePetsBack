package com.ohdaesan.shallwepets.images.service;

import java.util.HashMap;
import java.util.Map;
import java.util.UUID;
import com.amazonaws.AmazonServiceException;
import com.amazonaws.SdkClientException;
import com.amazonaws.services.s3.AmazonS3;
import com.amazonaws.services.s3.model.*;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;

@Service
@RequiredArgsConstructor
public class S3Service {
    @Value("${cloud.aws.s3.bucket}")
    private String bucket;

    private final AmazonS3 amazonS3;

    public Map<String, String> uploadFile(MultipartFile multipartFile) throws IOException {
        String originalFileName = multipartFile.getOriginalFilename();

        String uniqueFileName = generateUniqueFileName(originalFileName);

        String ext = uniqueFileName.split("\\.")[1];
        String contentType = getContentType(ext);

        try {
            ObjectMetadata metadata = new ObjectMetadata();
            metadata.setContentType(contentType);

            amazonS3.putObject(new PutObjectRequest(bucket, uniqueFileName, multipartFile.getInputStream(), metadata)
                    .withCannedAcl(CannedAccessControlList.PublicRead));
        } catch (AmazonServiceException e) {
            e.printStackTrace();
        } catch (SdkClientException e) {
            e.printStackTrace();
        } catch (IOException e) {
            throw new RuntimeException(e);
        }

        Map<String, String> map = new HashMap<>();
        map.put("imageUrl", amazonS3.getUrl(bucket, uniqueFileName).toString());
        map.put("imageOrigName", originalFileName);
        map.put("imageSavedName", uniqueFileName);

        return map;
    }

    private String generateUniqueFileName(String originalFileName) {
        String fileExtension = originalFileName.substring(originalFileName.lastIndexOf("."));
        String uniqueFileName = System.currentTimeMillis() + "_" + UUID.randomUUID() + fileExtension;
        return uniqueFileName;
    }

    private String getContentType(String ext) {
        switch (ext.toLowerCase()) {
            case "jpeg":
                return "image/jpeg";
            case "png":
                return "image/png";
            case "txt":
                return "text/plain";
            case "csv":
                return "text/csv";
            default:
                return "application/octet-stream";
        }
    }
}