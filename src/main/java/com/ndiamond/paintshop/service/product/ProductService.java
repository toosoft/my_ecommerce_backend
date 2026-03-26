package com.ndiamond.paintshop.service.product;

import com.ndiamond.paintshop.dto.ImageDto;
import com.ndiamond.paintshop.dto.ProductDto;
import com.ndiamond.paintshop.exceptions.AlreadyExistsException;
import com.ndiamond.paintshop.exceptions.ResourceNotFoundException;
import com.ndiamond.paintshop.model.Category;
import com.ndiamond.paintshop.model.Image;
import com.ndiamond.paintshop.model.Product;
import com.ndiamond.paintshop.repository.CategoryRepository;
import com.ndiamond.paintshop.repository.ImageRepository;
import com.ndiamond.paintshop.repository.ProductRepository;
import com.ndiamond.paintshop.request.AddProductRequest;
import com.ndiamond.paintshop.request.ProductUpdateRequest;
import lombok.RequiredArgsConstructor;
import org.modelmapper.ModelMapper;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class ProductService implements IProductService {

    private final ImageRepository imageRepository;
    private final ProductRepository productRepository;
    private final CategoryRepository categoryRepository;
    private final ModelMapper modelMapper;

    @Override
    public Product addProduct(AddProductRequest request) {
        // check if the category is found in the DB
        //if yes, set it as the new product category
        //if no, then save it as a new category
        // Then set it as the new product category

        if (productExists(request.getName(), request.getBrand())){
            throw new AlreadyExistsException(request.getBrand()+" "
                    +request.getName()+" already exists, you may update this product instead");
        }

        Category category = Optional.ofNullable(categoryRepository.
                findByName(request.getCategory().getName())).orElseGet(()-> {
                    Category newCategory = new Category(request.getCategory().getName());
                    return categoryRepository.save(newCategory);
        });
        request.setCategory(category);
        return productRepository.save(createProduct(request, category));
    }

    private boolean productExists(String name, String brand){
        return productRepository.existsByNameAndBrand(name, brand);
    }


    private Product createProduct(AddProductRequest request, Category category){
        return new Product(
                request.getName(),
                request.getBrand(),
                request.getPrice(),
                request.getInventory(),
                request.getDescription(),
                category
        );
    }

    @Override
    public List<Product> addProducts(List<AddProductRequest> requests) {

        return requests.stream()
                .map(request -> {
                    try {
                        return addProduct(request);
                    } catch (AlreadyExistsException e) {
                        return null; // skip
                    }
                })
                .filter(Objects::nonNull)
                .toList();
    }

    @Override
    public Product getProductById(Long id) {
        return productRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Product not Found"));
    }

    @Override
    public void deleteProductById(Long id) {
        productRepository.findById(id).ifPresentOrElse(productRepository::delete, ()-> {
            throw new ResourceNotFoundException("Product not Found");
        });
    }

    @Override
    public Product updateProduct(ProductUpdateRequest request, Long productId) {
        return productRepository.findById(productId)
                .map(existingProduct -> updateExistingProduct(existingProduct, request))
                .map(productRepository::save)
                .orElseThrow(()-> new ResourceNotFoundException("Product not found"));
    }

    private Product updateExistingProduct(Product existingProduct, ProductUpdateRequest request) {

        existingProduct.setName(request.getName());
        existingProduct.setBrand(request.getBrand());
        existingProduct.setPrice(request.getPrice());
        existingProduct.setInventory(request.getInventory());
        existingProduct.setDescription(request.getDescription());

        Category category = categoryRepository.findByName(request.getCategory().getName());
        existingProduct.setCategory(category);
        return existingProduct;
    }



    @Override
    public List<Product> getAllProducts() {
        return productRepository.findAll();
    }

    @Override
    public List<Product> getProductsByName(String name) {
        return productRepository.findByName(name);
    }

    @Override
    public List<Product> getProductsByBrandAndName( String brand, String name) {
        return productRepository.findByBrandAndName(brand, name);
    }

    @Override
    public List<Product> getProductsByCategory(String category) {
        return productRepository.findByCategoryName(category);
    }

    @Override
    public List<Product> getProductsByBrand(String brand) {
        return productRepository.findByBrand(brand);
    }

    @Override
    public List<Product> getProductsByCategoryAndBrand(String category, String brand) {
        return productRepository.findByCategoryNameAndBrand(category, brand);
    }

    @Override
    public Long countProductsByBrandAndName(String brand, String name) {
        return productRepository.countByBrandAndName(brand, name);
    }

    @Override
    public List<ProductDto> getConvertedProducts(List<Product> products) {
        return products.stream().map(this::convertToDto).toList();
    }

    @Override
    public ProductDto convertToDto(Product product) {
        ProductDto productDto = modelMapper.map(product, ProductDto.class);
        List<Image> images = imageRepository.findByProductId(product.getId());
        List<ImageDto> imagesDtos = images.stream()
                .map(image -> modelMapper.map(image, ImageDto.class))
                .toList();

        productDto.setImages(imagesDtos);
        return productDto;
    }

    @Override
    public Map<Long, Product> getProductsByIds(List<Long> productIds) {

        List<Product> products = productRepository.findAllById(productIds);

        return products.stream()
                .collect(Collectors.toMap(
                        Product::getId,
                        product -> product
                ));
    }
}
