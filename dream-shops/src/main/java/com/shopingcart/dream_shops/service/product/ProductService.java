package com.shopingcart.dream_shops.service.product;

import com.shopingcart.dream_shops.dto.ImageDto;
import com.shopingcart.dream_shops.dto.ProductDto;
import com.shopingcart.dream_shops.exception.AlreadyExistException;
import com.shopingcart.dream_shops.exception.ProductNotFoundException;
import com.shopingcart.dream_shops.exception.ResourceNotFoundException;
import com.shopingcart.dream_shops.model.Category;
import com.shopingcart.dream_shops.model.Image;
import com.shopingcart.dream_shops.model.Product;
import com.shopingcart.dream_shops.repository.CategoryRepository;
import com.shopingcart.dream_shops.repository.ImageRepository;
import com.shopingcart.dream_shops.repository.ProductRepository;
import com.shopingcart.dream_shops.request.AddProductRequest;
import com.shopingcart.dream_shops.request.ProductUpdateRequest;
import org.modelmapper.ModelMapper;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class ProductService implements IProductService{

    private final ProductRepository productRepository;
    private final CategoryRepository categoryRepository;
    private final ImageRepository imageRepository;
    private final ModelMapper modelMapper;

    public ProductService(ProductRepository productRepository, CategoryRepository categoryRepository, ImageRepository imageRepository, ModelMapper modelMapper) {
        this.productRepository = productRepository;
        this.categoryRepository = categoryRepository;
        this.imageRepository = imageRepository;
        this.modelMapper = modelMapper;
    }

    @Override
    public Product addProduct(AddProductRequest request) {
        //Check id the category is found in DB
        //If YES, set it as new product category
        //if NOT, then save it as new CATEGORY
//        Category category = Optional.ofNullable(categoryRepository.findByName(String.valueOf(request.getCategory())))
//                .orElseGet(()->{
//                    Category newCategory = new Category(request.getCategory());
//                    return categoryRepository.save(newCategory);
//                });

        if(productExists(request.getName(), request.getBrand())){
            throw new AlreadyExistException(request.getBrand() + " " + request.getName() + " already exists you may update this product");
        }
        Category category = categoryRepository.findByName(request.getCategory());
        if (category == null) {
            // If category doesn't exist, create a new one
            category = new Category(request.getCategory());
            categoryRepository.save(category);
        }
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
    public Product getProductById(Long id) {

        return productRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Product not found"));
    }

    @Override
    public void deleteProductById(Long id) {
        Product product = productRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Product not found with id: " + id));
        product.setCategory(null);
        productRepository.delete(product);
    }

    @Override
    public Product updateProduct(ProductUpdateRequest request, Long productId) {
        return productRepository.findById(productId)
                .map(existingProduct -> updateExistingProduct(existingProduct,request))
                .map(productRepository :: save)
                .orElseThrow(() -> new ProductNotFoundException("Product not found"));
    }

    private Product updateExistingProduct(Product existingProduct, ProductUpdateRequest request){
        existingProduct.setName(request.getName());
        existingProduct.setBrand(request.getBrand());
        existingProduct.setPrice(request.getPrice());
        existingProduct.setInventory(request.getInventory());
        existingProduct.setDescription(request.getDescription());

        if (request.getCategory() != null) {
            Category category = categoryRepository.findByName(request.getCategory().getName());
            existingProduct.setCategory(category);
        }
        return existingProduct;
    }

    @Override
    public List<Product> getAllProducts() {

        return productRepository.findAll();
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
    public List<Product> getProductsByName(String name) {
        return productRepository.findByName(name);
    }

    @Override
    public List<Product> getProductsByBrandAndName(String brand, String name) {
        return productRepository.findByBrandAndName(brand, name);
    }

    @Override
    public Long countProductsByBrandAndName(String brand, String name) {
        return productRepository.countByBrandAndName(brand, name);
    }

    @Override
    public List<ProductDto> getConvertedProducts(List<Product> products){
        return products.stream().map(this::convertToDto).toList();
    }

    @Override
    public ProductDto convertToDto(Product product){
        ProductDto productDto = modelMapper.map(product, ProductDto.class);
            List<Image> images = imageRepository.findByProductId(product.getId());
            List<ImageDto> imageDtos = images.stream()
                    .map(image -> modelMapper.map(image, ImageDto.class))
                    .toList();
            productDto.setImages(imageDtos);
            return productDto;

    }
}
