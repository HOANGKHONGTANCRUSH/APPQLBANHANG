package .Model;

public class Board {
    Integer board_id,board_number,board_status;

    Double board_price;

    public Board() {
    }

    public Board(Integer board_id, Integer board_number, Integer board_status) {
        this.board_id = board_id;
        this.board_number = board_number;
        this.board_status = board_status;
    }

    public Board(Integer board_id, Integer board_number, Double board_price) {
        this.board_id = board_id;
        this.board_number = board_number;
        this.board_price = board_price;
    }

    public Board(Integer board_number, Integer board_status) {
        this.board_number = board_number;
        this.board_status = board_status;
    }

    public Integer getBoard_id() {
        return board_id;
    }

    public void setBoard_id(Integer board_id) {
        this.board_id = board_id;
    }

    public Integer getBoard_number() {
        return board_number;
    }

    public void setBoard_number(Integer board_number) {
        this.board_number = board_number;
    }

    public Integer getBoard_status() {
        return board_status;
    }

    public void setBoard_status(Integer board_status) {
        this.board_status = board_status;
    }

    public Double getBoard_price() {
        return board_price;
    }

    public void setBoard_price(Double board_price) {
        this.board_price = board_price;
    }
}

