package nyan.ecommerce.spreeze.product;

import java.util.List;
import java.util.Optional;

import org.bson.types.ObjectId;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("api/v1/products")
public class ProductController {

    @Autowired
    private ProductService productService;

    @GetMapping
    public ResponseEntity<List<Product>> getAllProducts() {
        return new ResponseEntity<List<Product>>(productService.getAllProducts(), HttpStatus.OK);
    }

    @PreAuthorize("hasAuthority('ROLE_ADMIN')")
    @PostMapping
    public ResponseEntity<Product> createProduct(@RequestBody Product productToCreate) {
        return new ResponseEntity<Product>(productService.createProduct(productToCreate), HttpStatus.OK);
    }

    @GetMapping("{id}")
    public ResponseEntity<Optional<Product>> createProduct(@PathVariable("id") ObjectId id) {
        return new ResponseEntity<Optional<Product>>(productService.getProduct(id), HttpStatus.OK);
    }

    @PreAuthorize("hasAuthority('ROLE_ADMIN')")
    @PatchMapping("{id}")
    public ResponseEntity<Product> updateProduct(@PathVariable("id") ObjectId id,
            @RequestBody Product productToUpdate) {
        return new ResponseEntity<Product>(productService.updateProduct(id, productToUpdate), HttpStatus.OK);
    }

    @PreAuthorize("hasAuthority('ROLE_ADMIN')")
    @DeleteMapping("{id}")
    public ResponseEntity<String> deleteProduct(@PathVariable("id") ObjectId id) {
        return new ResponseEntity<String>(productService.deleteProduct(id), HttpStatus.OK);
    }

}
