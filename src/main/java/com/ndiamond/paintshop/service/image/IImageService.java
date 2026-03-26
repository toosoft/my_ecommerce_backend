package com.ndiamond.paintshop.service.image;

import com.ndiamond.paintshop.dto.ImageDto;
import com.ndiamond.paintshop.model.Image;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;

public interface IImageService {
    Image getImageById(Long id);
    void deleteImageById(Long id);
    List<ImageDto> saveImages(List<MultipartFile> files, Long productId);

    void updateImage(MultipartFile file, Long imageId);

}
