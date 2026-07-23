package com.assignment.asm.service.impl;

import com.assignment.asm.service.ImageService;
import com.cloudinary.Cloudinary;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.util.Map;

@Service
public class ImageServiceImpl implements ImageService {

    @Autowired
    private Cloudinary cloudinary;
    @Override
    public String upload(MultipartFile file) {
        try {

            Map result = cloudinary.uploader()
                    .upload(
                            file.getBytes(),
                            Map.of()
                    );


            return result
                    .get("secure_url")
                    .toString();


        } catch(Exception e){

            throw new RuntimeException(
                    "Upload image failed"
            );

        }
    }
}
