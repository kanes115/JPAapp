import javax.persistence.*;
import java.util.LinkedList;
import java.util.List;


@Entity
public class Product {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private int prodId;

    private String productName;
    private int UnitsInStock;
    @ManyToOne
    private Supplier supplier = null;

    @ManyToOne
    @JoinColumn
    private Category category;

    @ManyToMany
    private transient List<TransactionP> transactions = new LinkedList<TransactionP>();

    public Product(){}

    public Product(String name, int stock, Supplier supplier, Category category){
        this.productName = name;
        this.UnitsInStock = stock;
        supplier.addProduct(this);
        this.category = category;
        this.category.addProduct(this);
    }

    public void addTransactioin(TransactionP transaction){
        transactions.add(transaction);
        transaction.getProducts().add(this);
    }

    public String getProductName() {
        return productName;
    }

    public void setProductName(String productName) {
        this.productName = productName;
    }

    public int getUnitsInStock() {
        return UnitsInStock;
    }

    public void setUnitsInStock(int unitsInStock) {
        UnitsInStock = unitsInStock;
    }

    public int getProdId() {
        return prodId;
    }

    public void setProdId(int id) {
        this.prodId = id;
    }

    public Supplier getSupplier() {
        return supplier;
    }

    public void setSupplier(Supplier supplier) {
        this.supplier = supplier;
    }

    public List<TransactionP> getTransactions() {
        return transactions;
    }

    public Category getCategory() {
        return category;
    }

    public void setCategory(Category category) {
        this.category = category;
    }

    public void decreaseUnits(int delta){
        if(delta <= UnitsInStock)
            this.UnitsInStock -= delta;
    }
}

