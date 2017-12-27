import javax.persistence.Embeddable;
import java.util.Objects;

/**
 * Created by Kanes on 26.12.2017.
 */
@Embeddable
public class Address {

    private String street;
    private String city;
    private String zipCode;

    public Address(String street, String city, String zipCode){
        this.street = street;
        this.city = city;
        this.zipCode = zipCode;
    }

    public Address(){}


    public String getStreet() {
        return street;
    }

    public void setStreet(String street) {
        this.street = street;
    }

    public String getCity() {
        return city;
    }

    public void setCity(String city) {
        this.city = city;
    }

    public String getZipCode() {
        return zipCode;
    }

    public void setZipCode(String zipCode) {
        this.zipCode = zipCode;
    }

    @Override
    public boolean equals(Object o){
        if(o.getClass() != getClass())
            return false;
        Address a = (Address) o;
        return Objects.equals(a.city, city)
                && Objects.equals(a.street, street)
                && Objects.equals(a.zipCode, zipCode);
    }
}
