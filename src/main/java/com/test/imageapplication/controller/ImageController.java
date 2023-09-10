package com.test.imageapplication.controller;

import com.test.imageapplication.entity.DBImage;
import com.test.imageapplication.service.ImageService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.List;

@RestController
@RequiredArgsConstructor
@CrossOrigin(origins = "*")
@RequestMapping("/images")
public class ImageController {

    private final ImageService imageService;

    @PostMapping
    public void uploadImage(@RequestParam("image") MultipartFile file) throws IOException {
        imageService.uploadImage(file);
    }

    @GetMapping("/{key}")
    public List<DBImage> getImagesByKey(@PathVariable String key) {
        return imageService.getAll(key);
    }
}
