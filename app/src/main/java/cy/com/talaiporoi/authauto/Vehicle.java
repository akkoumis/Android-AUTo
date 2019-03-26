package cy.com.talaiporoi.authauto;

public class Vehicle {
    private String manufacturer, model;
    private int photo;

    public Vehicle(String manufacturer, String model, int photo) {
        this.manufacturer = manufacturer;
        this.model = model;
        this.photo = photo;
    }

    public String getManufacturer() {
        return manufacturer;
    }

    public void setManufacturer(String manufacturer) {
        this.manufacturer = manufacturer;
    }

    public String getModel() {
        return model;
    }

    public void setModel(String model) {
        this.model = model;
    }

    public int getPhoto() {
        return photo;
    }

    public void setPhoto(int photo) {
        this.photo = photo;
    }
}
