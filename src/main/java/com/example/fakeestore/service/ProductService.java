package com.example.fakeestore.service;

import com.example.fakeestore.entity.Category;
import com.example.fakeestore.entity.Product;
import com.example.fakeestore.entity.User;
import com.example.fakeestore.exceptions.CategoryAllreadyExistsException;
import com.example.fakeestore.exceptions.CategoryNotFoundException;
import com.example.fakeestore.exceptions.ProductNotFoundException;
import com.example.fakeestore.repository.CategoryRepository;
import com.example.fakeestore.repository.ProductRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.Random;

@Service
public class ProductService {

    @Autowired
    ProductRepository productRepository;

    @Autowired
    CategoryRepository categoryRepository;


    @Transactional
    public Product createProduct(Product product)
    {
        return productRepository.save(product);
    }

    @Transactional
    public void deleteProduct(Long id) throws ProductNotFoundException {
        Product product = getProductById(id);
        productRepository.delete(product);
    }

    @Transactional(readOnly = true)
    public Product getProductById(Long id) throws ProductNotFoundException
    {
        Optional<Product> product = productRepository.findById(id);

        if(!product.isPresent())
            throw new ProductNotFoundException(id);

        return product.get();
    }


    @Transactional(readOnly = true)
    public Page<Product> getProductsByUser(User user,int page, int pageSize, String sortBy)
    {
        Pageable pageable = PageRequest.of(page,pageSize, Sort.by(sortBy));
        Page<Product> pagedResult = productRepository.findProductByUser(user, pageable);

        return pagedResult;
    }


    @Transactional(readOnly = true)
    public Page<Product> getFilteredProducts(String name,String barcode, Integer qty, Float minPrice, Float maxPrice, String[] category, int page,int pageSize, String sortBy)
    {
        Pageable pageable = PageRequest.of(page,pageSize, Sort.by(sortBy));
        Page<Product> pagedResult = productRepository.advancedFilter(name,barcode, qty, minPrice, maxPrice, category, pageable);
        return pagedResult;

    }

    @Transactional(readOnly = true)
    public Page<Product> getNewProducts()
    {
        Pageable pageable = PageRequest.of(0,5, Sort.by("id"));
        Page<Product> pagedResult = productRepository.findByOrderByIdDesc(pageable);
        return pagedResult;
    }

    @Transactional(readOnly = true)
    public Page<Product> getRandomProducts()
    {
        long productCount = productRepository.count();
        Long[] ids = new Long[5];
        for(int i = 0;i<ids.length;i++)
        {
            ids[i] = new Random().nextLong(productCount);
        }
        Pageable pageable = PageRequest.of(0,5, Sort.by("id"));
        Page<Product> pagedResult = productRepository.getRandomProducts(ids,pageable);
        return pagedResult;
    }

    @Transactional
    public Product updateProduct(Product product) throws ProductNotFoundException
    {
        Product productToUpdate = getProductById(product.getId());
        productToUpdate.setName(product.getName());
        productToUpdate.setBarcode(product.getBarcode());
        productToUpdate.setDescription(product.getDescription());
        productToUpdate.setPrice(product.getPrice());
        productToUpdate.setQuantity(product.getQuantity());
        productToUpdate.setCategory(product.getCategory());
        return productRepository.save(productToUpdate);
    }

    @Transactional
    public Category createCategory(Category category) throws CategoryAllreadyExistsException {
        if(categoryRepository.existsById(category.getName()))
        {
            throw new CategoryAllreadyExistsException(category.getName());
        }

        return categoryRepository.save(category);
    }

    @Transactional(readOnly = true)
    public List<Category> getCategories() {
        return categoryRepository.findAll();
    }
    @Transactional(readOnly = true)
    public Category getCategory(String name) throws CategoryNotFoundException
    {
        Optional<Category> category = categoryRepository.findById(name);
        if(!category.isPresent())
            throw new CategoryNotFoundException(name);
        return category.get();
    }
}
