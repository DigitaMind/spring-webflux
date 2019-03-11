package com.interswitch.reactivepro.api.controller;

import com.interswitch.reactivepro.api.dao.ProductDao;
import com.interswitch.reactivepro.api.model.Product;
import com.interswitch.reactivepro.api.model.ProductEvent;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

import java.time.Duration;

@RestController
@RequestMapping("/products")
public class ProductController {
    @Autowired
    private ProductDao productDao;

    @GetMapping
    public Flux<Product> getAllProducts(){
        return productDao.findAll();
    }

    @GetMapping("{id}")
    public Mono<ResponseEntity<Product>> getProductById(@PathVariable("id") String id){
        return productDao
                .findById(id)
                .map(product -> ResponseEntity.ok(product))
                .defaultIfEmpty(ResponseEntity.notFound().build());
    }

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public Mono<Product> saveProduct(@RequestBody Product product){
        return productDao.save(product);
    }

    @PutMapping("{id}")
    @ResponseStatus(HttpStatus.OK)
    public Mono<ResponseEntity<Product>> updateProduct(@PathVariable("id") String id, @RequestBody Product product){
        return productDao
                .findById(id)
                .flatMap(existingProduct -> {existingProduct.setName(product.getName());
                existingProduct.setPrice(product.getPrice());
                return productDao.save(existingProduct);
                })
                .map(updateProduct -> ResponseEntity.ok(updateProduct))
                .defaultIfEmpty(ResponseEntity.notFound().build());
    }

    @DeleteMapping("{id}")
    @ResponseStatus(HttpStatus.OK)
    public Mono<ResponseEntity<Void>> deleteProduct(@PathVariable("id") String id){
        return productDao.findById(id)
                .flatMap(existingProduct -> productDao.delete(existingProduct)
                                                .then(Mono.just(ResponseEntity.ok().<Void>build())))
                .defaultIfEmpty(ResponseEntity.notFound().build());
    }

    @DeleteMapping
    @ResponseStatus(HttpStatus.OK)
    public Mono<Void> deleteProduct(){
        return productDao.deleteAll();
    }

    @GetMapping(value = "/events", produces = MediaType.TEXT_EVENT_STREAM_VALUE)
    public Flux<ProductEvent> getProductEvents(){
        return Flux.interval(Duration.ofSeconds(1))
                .map(val -> new ProductEvent(val, "Product Event"));
    }

}
