package .Model;

public class Food {
    private Integer food_id, food_type, food_status;
    private String food_name;
    private Double food_price;
    private byte[] food_image;

    public Food(Integer food_id, Integer food_type, Integer food_status, String food_name, Double food_price, byte[] food_image) {
        this.food_id = food_id;
        this.food_type = food_type;
        this.food_status = food_status;
        this.food_name = food_name;
        this.food_price = food_price;
        this.food_image = food_image;
    }

    public Food(Integer food_type, Integer food_status, String food_name, Double food_price, byte[] food_image) {
        this.food_type = food_type;
        this.food_status = food_status;
        this.food_name = food_name;
        this.food_price = food_price;
        this.food_image = food_image;
    }

    public Food() {
    }

    public Integer getFood_id() {
        return food_id;
    }

    public void setFood_id(Integer food_id) {
        this.food_id = food_id;
    }

    public Integer getFood_type() {
        return food_type;
    }

    public void setFood_type(Integer food_type) {
        this.food_type = food_type;
    }

    public Integer getFood_status() {
        return food_status;
    }

    public void setFood_status(Integer food_status) {
        this.food_status = food_status;
    }

    public String getFood_name() {
        return food_name;
    }

    public void setFood_name(String food_name) {
        this.food_name = food_name;
    }

    public Double getFood_price() {
        return food_price;
    }

    public void setFood_price(Double food_price) {
        this.food_price = food_price;
    }

    public byte[] getFood_image() {
        return food_image;
    }

    public void setFood_image(byte[] food_image) {
        this.food_image = food_image;
    }
}
