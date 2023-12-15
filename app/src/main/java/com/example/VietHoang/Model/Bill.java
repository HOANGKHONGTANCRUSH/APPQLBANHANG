package .Model;

public class Bill {
    Integer bill_id, board_id, board_number;
    String bill_date;
    Double bill_price;

    public Bill() {

    }

    public Bill(Integer bill_id, Integer board_number, String bill_date, Double bill_price, Integer board_id) {
        this.bill_id = bill_id;
        this.board_number = board_number;
        this.board_id = board_id;
        this.bill_date = bill_date;
        this.bill_price = bill_price;
    }

    public Bill(Integer board_id, String bill_date, Double bill_price) {
        this.board_id = board_id;
        this.bill_date = bill_date;
        this.bill_price = bill_price;
    }

    public Integer getBill_id() {
        return bill_id;
    }

    public void setBill_id(Integer bill_id) {
        this.bill_id = bill_id;
    }

    public Integer getBoard_id() {
        return board_id;
    }

    public void setBoard_id(Integer board_id) {
        this.board_id = board_id;
    }

    public String getBill_date() {
        return bill_date;
    }

    public void setBill_date(String bill_date) {
        this.bill_date = bill_date;
    }

    public Double getBill_price() {
        return bill_price;
    }

    public void setBill_price(Double bill_price) {
        this.bill_price = bill_price;
    }

    public Integer getBoard_number() {
        return board_number;
    }

    public void setBoard_number(Integer board_number) {
        this.board_number = board_number;
    }
}
