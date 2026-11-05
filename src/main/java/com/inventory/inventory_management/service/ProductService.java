package com.inventory.inventory_management.service;

import com.inventory.inventory_management.entity.Product;
import com.inventory.inventory_management.repository.ProductRepository;
import org.springframework.stereotype.Service;

import java.util.List;


@Service
public class ProductService {

    private final ProductRepository productRepository;

    public ProductService(ProductRepository productRepository) {
        this.productRepository = productRepository;
    }

    public Product addProduct(Product product) {

        if (productRepository.existsBySku(product.getSku())) {
            throw new IllegalArgumentException(
                    "Product with this SKU already exists"
            );
        }

        return productRepository.save(product);
    }

    public List<Product> getAllProducts(){
        return productRepository.findAll();
    }

    public Product getProductById(Long id){
        return productRepository.findById(id)
                .orElseThrow(()-> new RuntimeException("Product not found with ID: " + id)
                );
    }

    public Product updateProduct(Long id, Product updatingProduct){

        // Find the existing product
        Product existingProduct = getProductById(id);

        // Check whether the new SKU belongs to another product
        if(!existingProduct.getSku().equals(updatingProduct.getSku())
                && productRepository.existsBySku(updatingProduct.getSku())){

            throw new IllegalArgumentException("Product with this SKU already exists!");
        }

        // Update the existing products fields
        existingProduct.setName(updatingProduct.getName());
        existingProduct.setSku(updatingProduct.getSku());
        existingProduct.setPrice(updatingProduct.getPrice());
        existingProduct.setQuantity(updatingProduct.getQuantity());

        // Save the updated product
        return productRepository.save(existingProduct);
    }
}
