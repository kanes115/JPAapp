import javax.persistence.Entity;
import javax.persistence.OneToMany;
import java.util.LinkedList;
import java.util.List;

/**
 * Created by Kanes on 26.12.2017.
 */
@Entity
public class Customer extends Company {

    private double discount;

    @OneToMany
    private transient List<TransactionP> transactions = new LinkedList<TransactionP>();

    public Customer(){}

    public Customer(String name, Address address, double discount){
        super(name, address);
        this.discount = discount;
    }

    public double getDiscount() {
        return discount;
    }

    public void setDiscount(double discount) {
        this.discount = discount;
    }

    public List<TransactionP> getTransactions() {
        return transactions;
    }

    public void addTransaction(TransactionP tr){
        this.transactions.add(tr);
        tr.setCustomer(this);
    }

    @Override
    public boolean equals(Object o){
        if(o.getClass() != getClass())
            return false;
        Customer c = (Customer) o;
        return super.getName().equals(c.getName()) &&
                super.getAddress().equals(c.getAddress());

    }
}
