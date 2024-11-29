package com.shopingcart.dream_shops.service.image;

import com.shopingcart.dream_shops.dto.ImageDto;
import com.shopingcart.dream_shops.model.Image;
import com.shopingcart.dream_shops.model.Product;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;

public interface IImageService {
    Image getImageById(Long id);
    void deleteImageById(Long id);
    List<ImageDto> saveImages(Long productId, List<MultipartFile> files);
    void updateImage(MultipartFile file, Long imageId);
}
