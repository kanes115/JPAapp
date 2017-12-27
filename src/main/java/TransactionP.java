import javax.persistence.*;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;

/**
 * Created by Kanes on 25.12.2017.
 */
@Entity
public class TransactionP {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private int id;

    @ManyToMany
    private List<Product> products = new LinkedList<Product>();

    @ManyToOne
    private Customer customer;

    @ElementCollection
    private Map<Product, Integer> quantity = new HashMap<Product, Integer>();

    public TransactionP(){}


    public List<Product> getProducts() {
        return products;
    }

    public Map<Product, Integer> getQuantity() {
        return quantity;
    }

    public void addProduct(Product product, int quantity) throws IllegalArgumentException {
        if(quantity <= product.getUnitsInStock()) {
            products.add(product);
            this.quantity.put(product, quantity);
            product.getTransactions().add(this);
            product.decreaseUnits(quantity);
        }else
            throw new IllegalArgumentException("Too big quantity");
    }

    public int getId() {
        return id;
    }

    public Customer getCustomer() {
        return customer;
    }

    public void setCustomer(Customer customer) {
        this.customer = customer;
    }
}
