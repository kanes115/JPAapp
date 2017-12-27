import javax.persistence.*;
import java.util.HashSet;
import java.util.Set;

/**
 * Created by Kanes on 25.12.2017.
 */
@Entity
public class Supplier extends Company {

    @OneToMany
    private transient Set<Product> products;
    private String bankAccountNo;

    public Supplier(){}

    public Supplier(String companyName, Address address, String bankAccountNo){
        super(companyName, address);
        this.products = new HashSet<Product>();
        this.bankAccountNo = bankAccountNo;
    }

    public void addProduct(Product p){
        this.products.add(p);
        p.setSupplier(this);
    }


    public Set<Product> getProducts() {
        return products;
    }

    public void setProducts(Set<Product> products) {
        this.products = products;
    }


    public String getBankAccountNo() {
        return bankAccountNo;
    }

    public void setBankAccountNo(String bankAccountNo) {
        this.bankAccountNo = bankAccountNo;
    }
}
