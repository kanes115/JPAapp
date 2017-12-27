import java.util.List;
import java.util.stream.Collector;
import java.util.stream.Collectors;

import org.hibernate.SessionFactory;
//import org.hibernate.boot.registry.StandardServiceRegistryBuilder;
import org.hibernate.cfg.Configuration;


import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;
import javax.persistence.EntityTransaction;
import javax.persistence.Persistence;

/**
 * Created by Kanes on 26.12.2017.
 */
@SuppressWarnings("unchecked")
public class DBFetcher {

    private EntityManagerFactory emf = Persistence.createEntityManagerFactory("IDominikStanaszekJPAPractice");
    private EntityManager em = emf.createEntityManager();


    public List<Product> getAllProducts(){
        EntityTransaction etx = em.getTransaction();
        etx.begin();

        List<Product> tmp = (List<Product>) em.createQuery("from Product")
                .getResultList();

        etx.commit();

        return tmp;
    }

    public List<Category> getAllCategories(){
        EntityTransaction etx = em.getTransaction();
        etx.begin();

        List<Category> tmp = (List<Category>) em.createQuery("from Category")
                .getResultList();

        etx.commit();

        return tmp;

    }

    public List<Supplier> getAllSuppliers(){
        EntityTransaction etx = em.getTransaction();
        etx.begin();

        List<Supplier> tmp = (List<Supplier>) em.createQuery("from Supplier")
                .getResultList();

        etx.commit();

        return tmp;
    }

    public List<Customer> getAllCustomers(){
        EntityTransaction etx = em.getTransaction();
        etx.begin();

        List<Customer> tmp = (List<Customer>) em.createQuery("from Customer")
                .getResultList();

        etx.commit();

        return tmp;
    }

    public Category getCategory(int id){
        EntityTransaction etx = em.getTransaction();
        etx.begin();

        List<Category> tmp = (List<Category>) em.createQuery("select c from Category c where c.id = :id")
                .setParameter("id", id)
                .getResultList();

        etx.commit();

        return tmp.get(0);
    }

    public List<Product> getProducts(int catId){
        EntityTransaction etx = em.getTransaction();
        etx.begin();

        List<Product> tmp = (List<Product>) em.createQuery("select c from Product c where c.category.id = :id")
                .setParameter("id", catId)
                .getResultList();

        etx.commit();

        return tmp;
    }

    public List<Product> getProductsOfSupplier(int id){
        EntityTransaction etx = em.getTransaction();
        etx.begin();

        List<Product> tmp = (List<Product>) em.createQuery("select c from Product c where c.supplier.id = :id")
                .setParameter("id", id)
                .getResultList();

        etx.commit();

        return tmp;
    }

    public List<TransactionP> getTransactionsOfCustomer(int id) {
        EntityTransaction etx = em.getTransaction();
        etx.begin();

        List<TransactionP> tmp = (List<TransactionP>) em.createQuery("select c from TransactionP c where c.customer.id = :id")
                .setParameter("id", id)
                .getResultList();

        etx.commit();

        return tmp;
    }

    public List<Product> getProductsOfTransaction(int id) {
        EntityTransaction etx = em.getTransaction();
        etx.begin();

        List<Product> tmp = (List<Product>) em.createQuery("select a from TransactionP t join t.products a where t.id = :id")
                .setParameter("id", id)
                .getResultList();

        etx.commit();

        return tmp;
    }

    public Customer getCustomer(int id){
        EntityTransaction etx = em.getTransaction();
        etx.begin();

        Customer tmp = (Customer) em.createQuery("select a from Customer a where a.id = :id")
                .setParameter("id", id)
                .getResultList().get(0);

        etx.commit();
        return tmp;
    }

    public Customer addClient(String name, String street, String city, String zipcode){
        EntityTransaction etx = em.getTransaction();
        etx.begin();

        Customer c = new Customer(name, new Address(street, city, zipcode), 0.0);

        if(doesCustomerExist(c)){
            etx.commit();
            return null;
        }

        em.persist(c);
        etx.commit();
        return c;
    }

    public void addTransaction(TransactionP t, Customer c){
        EntityTransaction etx = em.getTransaction();
        etx.begin();

        c.addTransaction(t);

        em.persist(c);
        em.persist(t);
        etx.commit();
    }

    public boolean doesCustomerExist(Customer c){
        List<Customer> tmp = (List<Customer>) em.createQuery("select c from Customer c where c.address = :address and c.name = :name")
                .setParameter("address", c.getAddress())
                .setParameter("name", c.getName())
                .getResultList();
        return !tmp.isEmpty();
    }

    public void addCategory(){
        EntityManagerFactory emf = Persistence.createEntityManagerFactory("IDominikStanaszekJPAPractice");
        EntityManager em = emf.createEntityManager();
        EntityTransaction etx = em.getTransaction();
        etx.begin();

        Category c = new Category("RTV", "This category is devoted for digital devices");
        Category c2 = new Category("Bakery", "This category is devoted bread and rolls");

        Supplier zaczyn = new Supplier("Zaczyn", new Address("Mickiewicza", "Kraków", "12-432"), "284792374823764832");
        Supplier neonet = new Supplier("Neonet", new Address("Reymonta", "Sucha Beskidzka", "34-200"), "485789236594326");

        Product p1 = new Product("Bread", 213, zaczyn, c2);
        Product p2 = new Product("rolls", 2130, zaczyn, c2);
        Product p3 = new Product("TV", 21, neonet, c);
        Product p4 = new Product("xbox one x", 12, neonet, c);
        Product p5 = new Product("Macbook pro 13", 15, neonet, c);

        Customer dom = new Customer("Dominik Stanaszek", new Address("29 Stycznia", "Sucha Beskidzka", "34-200"), 0.2);
        Customer aga = new Customer("Agnieszka Pierzchala", new Address("Hrubieszowska", "Zamosc", "22-400"), 0.3);
        Customer franczak = new Customer("Przemyslaw Franczak", new Address("nieznana", "Krasnystaw", "21-300"), 0.1);

        TransactionP t1 = new TransactionP();
        t1.addProduct(p1, 1);
        t1.addProduct(p2, 3);
        TransactionP t2 = new TransactionP();
        t2.addProduct(p1, 1);
        t2.addProduct(p4, 3);
        TransactionP t3 = new TransactionP();
        t3.addProduct(p3, 1);
        t3.addProduct(p5, 3);

        dom.addTransaction(t1);
        aga.addTransaction(t2);
        franczak.addTransaction(t3);

        em.persist(c);
        em.persist(c2);

        em.persist(zaczyn);
        em.persist(neonet);

        em.persist(p1);
        em.persist(p2);
        em.persist(p3);
        em.persist(p4);
        em.persist(p5);

        em.persist(dom);
        em.persist(aga);
        em.persist(franczak);

        em.persist(t1);
        em.persist(t2);
        em.persist(t3);

        etx.commit();
        em.close();
    }

    public Product getProduct(int id) {
        EntityTransaction etx = em.getTransaction();
        etx.begin();

        Product tmp = (Product) em.createQuery("select a from Product a where a.prodId = :id")
                .setParameter("id", id)
                .getResultList().get(0);
        etx.commit();
        return tmp;
    }

    public void addProductToTransaction(Product p, TransactionP t, int quantity) {
        System.out.println("before: " + p.getUnitsInStock());
        EntityTransaction etx = em.getTransaction();
        etx.begin();

        t.addProduct(p, quantity);

        em.persist(t);
        em.persist(p);
        etx.commit();
        System.out.println("Added p: " + p.getProductName() + " to transaction of: " + t.getCustomer().getName());
        System.out.println("after: " + p.getUnitsInStock());

    }
}
