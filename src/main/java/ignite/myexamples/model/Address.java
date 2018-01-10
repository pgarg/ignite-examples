package ignite.myexamples.model;

import org.apache.ignite.cache.query.annotations.QuerySqlField;

public class Address {

    @QuerySqlField (index = true)
    private String street;

    @QuerySqlField(index = true)
    private int zip;


    public Address(String street, int zip) {
        this.street = street;
        this.zip = zip;
    }

    public String getStreet() {
        return street;
    }

    public void setStreet(String street) {
        this.street = street;
    }

    public int getZip() {
        return zip;
    }

    public void setZip(int zip) {
        this.zip = zip;
    }

    @Override
    public String toString() {
        return "Address{" +
                "street='" + street + '\'' +
                ", zip=" + zip +
                '}';
    }
}
