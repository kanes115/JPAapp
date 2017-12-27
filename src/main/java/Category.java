import javax.persistence.*;
import java.util.LinkedList;
import java.util.List;

/**
 * Created by Kanes on 25.12.2017.
 */

@Entity
public class Category {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private int id;

    private String name;
    private String description;

    @OneToMany
    private transient List<Product> products = new LinkedList<>();

    public Category(String name, String description){
        this.name = name;
    }

    public Category(){

    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public void addProduct(Product p){
        this.products.add(p);
        p.setCategory(this);
    }


    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }
}
