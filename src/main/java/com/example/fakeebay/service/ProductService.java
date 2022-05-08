package com.example.fakeebay.service;

import com.example.fakeebay.entity.Product;
import com.example.fakeebay.entity.User;
import com.example.fakeebay.exceptions.ProductNotFoundException;
import com.example.fakeebay.repository.ProductRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
public class ProductService {

    @Autowired
    ProductRepository productRepository;


    @Transactional
    public Product createProduct(Product product)
    {
        return productRepository.save(product);
    }

    @Transactional
    public void deleteProduct(Long id)
    {
        Product product = getProductById(id);
        productRepository.delete(product);
    }

    @Transactional(readOnly = true)
    public Product getProductById(Long id) throws ProductNotFoundException
    {
        return productRepository.findById(id).orElseThrow(() -> {throw new ProductNotFoundException(id);});
    }

    @Transactional(readOnly = true)
    public List<Product> getProductsByUser(User user)
    {
        return productRepository.findProductByUser(user);
    }


    @Transactional(readOnly = true)
    public List<Product> getProductsByText(String text)
    {
        return productRepository.findProductByNameContainingOrBarcodeContaining(text);
    }

    @Transactional(readOnly = true)
    public List<Product> getAllAvailableProducts()
    {
        return productRepository.findAllAvailableProducts();
    }

    @Transactional(readOnly = true)
    public List<Product> getAllProducts()
    {
        return productRepository.findAll();
    }

    @Transactional(readOnly = true)
    public List<Product> getFilteredProducts(String name,String barcode, Integer qty, Float minPrice, Float maxPrice)
    {
        return productRepository.advancedFilter(name,barcode, qty, minPrice, maxPrice);
    }


    @Transactional
    public Product updateProduct(Product product) throws ProductNotFoundException
    {
        Product productToUpdate = productRepository.findById(product.getId()).orElseThrow(() -> {throw new ProductNotFoundException(product.getId());});
        productToUpdate.setName(product.getName());
        productToUpdate.setBarcode(product.getBarcode());
        productToUpdate.setPrice(product.getPrice());
        productToUpdate.setQuantity(product.getQuantity());
        return productRepository.save(productToUpdate);
    }

}
