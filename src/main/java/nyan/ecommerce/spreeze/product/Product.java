package nyan.ecommerce.spreeze.product;

import java.math.BigDecimal;
import java.util.List;

import org.bson.types.ObjectId;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;
import org.springframework.data.mongodb.core.mapping.DocumentReference;

import com.mongodb.lang.NonNull;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import nyan.ecommerce.spreeze.category.Category;

@Document(collection = "products")
@Data
@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
public class Product {

    @Id
    private ObjectId id;

    @NonNull
    private String title;

    private String description;

    @NonNull
    private BigDecimal price;

    private Float discountPercentage;

    private Float rating;

    @NonNull
    private Integer stock;
    private String brand;

    @DocumentReference
    private Category category;

    private String thumbnail;

    private List<String> images;
}
