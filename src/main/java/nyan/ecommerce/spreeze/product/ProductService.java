package nyan.ecommerce.spreeze.product;

import java.util.List;
import java.util.Optional;

import org.bson.types.ObjectId;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.mongodb.core.FindAndModifyOptions;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.data.mongodb.core.query.Query;
import org.springframework.data.mongodb.core.query.Update;
import org.springframework.stereotype.Service;

@Service
public class ProductService {

    @Autowired
    private ProductRepository productRepository;

    @Autowired
    private MongoTemplate mongoTemplate;

    public List<Product> getAllProducts() {
        return productRepository.findAll();
    }

    public Product createProduct(Product productToCreate) {
        return productRepository.insert(productToCreate);
    }

    public Optional<Product> getProduct(ObjectId id) {
        return productRepository.findById(id);
    }

    public Product updateProduct(ObjectId id, Product productToUpdate) {
        Query productQuery = new Query();

        productQuery.addCriteria(Criteria.where("id").is(id));

        Update productUpdate = new Update();

        if (productToUpdate.getTitle() != null) {
            productUpdate.set("title", productToUpdate.getTitle());
        }

        if (productToUpdate.getDescription() != null) {
            productUpdate.set("description", productToUpdate.getDescription());
        }

        if (productToUpdate.getPrice() != null) {
            productUpdate.set("price", productToUpdate.getPrice());
        }

        if (productToUpdate.getDiscountPercentage() != null) {
            productUpdate.set("discountPercentage", productToUpdate.getDiscountPercentage());
        }

        if (productToUpdate.getStock() != null) {
            productUpdate.set("stock", productToUpdate.getStock());
        }

        if (productToUpdate.getBrand() != null) {
            productUpdate.set("brand", productToUpdate.getBrand());
        }

        if (productToUpdate.getCategory() != null) {
            productUpdate.set("category", productToUpdate.getCategory());
        }

        return mongoTemplate.findAndModify(
                productQuery,
                productUpdate, new FindAndModifyOptions().returnNew(true), Product.class);
    }

    public String deleteProduct(ObjectId id) {
        productRepository.deleteById(id);

        return "Product Deleted";
    }
}
