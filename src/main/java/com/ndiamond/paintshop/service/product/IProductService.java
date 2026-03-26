package com.ndiamond.paintshop.service.product;

import com.ndiamond.paintshop.dto.ProductDto;
import com.ndiamond.paintshop.model.Product;
import com.ndiamond.paintshop.request.AddProductRequest;
import com.ndiamond.paintshop.request.ProductUpdateRequest;

import java.util.List;
import java.util.Map;

public interface IProductService {
    Product addProduct(AddProductRequest product);

    List<Product> addProducts(List<AddProductRequest> requests);

    Product getProductById(Long id);
    void deleteProductById(Long id);
    Product updateProduct(ProductUpdateRequest product, Long productId);

    List<Product> getAllProducts();

    List<Product> getProductsByName(String name);
    List<Product> getProductsByBrandAndName(String name, String category);

    List<Product> getProductsByCategory(String category);
    List<Product> getProductsByBrand(String brand);
    List<Product> getProductsByCategoryAndBrand(String category, String brand);

    Long countProductsByBrandAndName(String brand, String name);


    List<ProductDto> getConvertedProducts(List<Product> products);

    ProductDto convertToDto(Product product);

    Map<Long, Product> getProductsByIds(List<Long> productIds);

//    List<ProductDto> convertToDtoList(List<Product> products);
}
