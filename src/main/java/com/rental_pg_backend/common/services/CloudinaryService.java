package com.rental_pg_backend.common.services;

import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import com.cloudinary.Cloudinary;

import jakarta.annotation.Resource;
import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class CloudinaryService {

    @Resource
    private Cloudinary cloudinary;

    private static final String FOLDER_NAME = "public";

    public String uploadFile(MultipartFile file) {
        try {
            HashMap<Object, Object> options = new HashMap<>();

            options.put("folder", FOLDER_NAME);

            var uploadedFile = cloudinary.uploader().upload(file.getBytes(), options);

            String publicId = (String) uploadedFile.get("public_id");

            return cloudinary.url().secure(true).generate(publicId);
        } catch (IOException e) {
            e.printStackTrace();
            return null;
        }
    }

    public List<String> uploadMultipleFiles(List<MultipartFile> files) {
        List<String> urls = new ArrayList<>();
        for (MultipartFile file : files) {
            String url = uploadFile(file);
            if (url != null) {
                urls.add(url);
            }
        }
        return urls;
    }

}
