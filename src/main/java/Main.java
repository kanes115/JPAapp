import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.Transaction;
//import org.hibernate.boot.registry.StandardServiceRegistryBuilder;
import org.hibernate.cfg.Configuration;
import org.hibernate.engine.spi.ExceptionConverter;


import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;
import javax.persistence.EntityTransaction;
import javax.persistence.Persistence;
import java.io.UnsupportedEncodingException;
import java.net.URLDecoder;
import java.util.*;
import java.util.stream.Collectors;

import static spark.Spark.*;


public class Main {

    public static void main(String[] args) {
        DBFetcher s = new DBFetcher();
        //s.addCategory();


        //Gson gson = new Gson();
        staticFileLocation("/web");

        GsonBuilder g = new GsonBuilder();
        g.registerTypeAdapter(TransactionP.class, new TransactionSerializer());
        Gson gson = g.create();

        //System.out.println("asdf: " + gson.toJson(s.getProductsOfTransaction()));

        get("/products", (req, res) ->
                gson.toJson(s.getAllProducts()));
        get("/categories", (req, res) ->
                gson.toJson(s.getAllCategories()));
        get("/categories/:id/products", (req, res) ->
                gson.toJson(s.getProducts(Integer.parseInt(req.params(":id")))));
        get("/suppliers/:id/products", (req, res) ->
                gson.toJson(s.getProductsOfSupplier(Integer.parseInt(req.params(":id")))));
        get("/suppliers", (req, res) ->
                gson.toJson(s.getAllSuppliers()));
        get("/customers", (req, res) ->
                gson.toJson(s.getAllCustomers()));
        get("/customers/:id/transactions", (req, res) ->
                gson.toJson(s.getTransactionsOfCustomer(Integer.parseInt(req.params(":id")))));
        get("/transactions/:id/products", (req, res) ->
                gson.toJson(s.getProductsOfTransaction(Integer.parseInt(req.params(":id")))));
        post("/createtransaction", (req, res) -> {
            try {
                System.out.println("args: " + req.body());
                Map<String, String[]> data = parseUrlQueryString(req.body());

                Customer c;
                if (Objects.equals(data.get("ctype")[0], "new")) {
                    c = s.addClient(data.get("name")[0],
                            data.get("street")[0],
                            data.get("city")[0],
                            data.get("zipcode")[0]);
                    if (c == null)
                        return "Customer already exists";
                } else
                    c = s.getCustomer(Integer.parseInt(data.get("clients")[0]));

                if (c == null)
                    throw new Exception("Error: got wrong client id");

                TransactionP t = new TransactionP();
                c.addTransaction(t);

                data.keySet().stream().filter(Main::isProduct).forEach(k -> {
                    int quantity = Integer.parseInt(data.get(k)[0]);
                    if(quantity != 0) {
                        Product p = s.getProduct(Integer.parseInt(k.split("_")[1]));
                        s.addProductToTransaction(p, t, quantity);
                    }
                });
            }catch(Exception e){
                e.printStackTrace();
                return "Error:\n" + e.getMessage();
            }
            return "ok";
        });
    }


    public static boolean isProduct(String p){
        return p.startsWith("prod_");
    }


    public static Map<String, String[]> parseUrlQueryString(String s) {
        if (s == null) return new HashMap<String, String[]>(0);
        // In map1 we use strings and ArrayLists to collect the parameter values.
        HashMap<String, Object> map1 = new HashMap<String, Object>();
        int p = 0;
        while (p < s.length()) {
            int p0 = p;
            while (p < s.length() && s.charAt(p) != '=' && s.charAt(p) != '&') p++;
            String name = urlDecode(s.substring(p0, p));
            if (p < s.length() && s.charAt(p) == '=') p++;
            p0 = p;
            while (p < s.length() && s.charAt(p) != '&') p++;
            String value = urlDecode(s.substring(p0, p));
            if (p < s.length() && s.charAt(p) == '&') p++;
            Object x = map1.get(name);
            if (x == null) {
                // The first value of each name is added directly as a string to the map.
                map1.put(name, value);
            } else if (x instanceof String) {
                // For multiple values, we use an ArrayList.
                ArrayList<String> a = new ArrayList<String>();
                a.add((String) x);
                a.add(value);
                map1.put(name, a);
            } else {
                @SuppressWarnings("unchecked")
                ArrayList<String> a = (ArrayList<String>) x;
                a.add(value);
            }
        }
        // Copy map1 to map2. Map2 uses string arrays to store the parameter values.
        HashMap<String, String[]> map2 = new HashMap<String, String[]>(map1.size());
        for (Map.Entry<String, Object> e : map1.entrySet()) {
            String name = e.getKey();
            Object x = e.getValue();
            String[] v;
            if (x instanceof String) {
                v = new String[]{(String) x};
            } else {
                @SuppressWarnings("unchecked")
                ArrayList<String> a = (ArrayList<String>) x;
                v = new String[a.size()];
                v = a.toArray(v);
            }
            map2.put(name, v);
        }
        return map2;
    }

    private static String urlDecode(String s) {
        try {
            return URLDecoder.decode(s, "UTF-8");
        } catch (UnsupportedEncodingException e) {
            throw new RuntimeException("Error in urlDecode.", e);
        }
    }
}