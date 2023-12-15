package .Model;

public class FoodBoard {

    private String foodName;
    private int foodBoardId, foodId, boardId, foodCount, boardNumber, foodBoardStatus;
    private double foodPrice;
    private byte[] foodBoardImg;

    public FoodBoard(String foodName, int foodBoardId, int foodId, int foodCount, double foodPrice, byte[] foodBoardImg) {
        this.foodName = foodName;
        this.foodBoardId = foodBoardId;
        this.foodId = foodId;
        this.foodCount = foodCount;
        this.foodPrice = foodPrice;
        this.foodBoardImg = foodBoardImg;
    }

    public FoodBoard(int foodBoardId, String foodName, int boardNumber, int foodBoardStatus) {
        this.foodName = foodName;
        this.foodBoardId = foodBoardId;
        this.boardNumber = boardNumber;
        this.foodBoardStatus = foodBoardStatus;
    }

    public FoodBoard(int foodId, int boardId) {
        this.foodId = foodId;
        this.boardId = boardId;
    }

    public int getBoardNumber() {
        return boardNumber;
    }

    public void setBoardNumber(int boardNumber) {
        this.boardNumber = boardNumber;
    }

    public String getFoodName() {
        return foodName;
    }

    public void setFoodName(String foodName) {
        this.foodName = foodName;
    }

    public int getFoodCount() {
        return foodCount;
    }

    public void setFoodCount(int foodCount) {
        this.foodCount = foodCount;
    }

    public double getFoodPrice() {
        return foodPrice;
    }

    public void setFoodPrice(double foodPrice) {
        this.foodPrice = foodPrice;
    }

    public int getFoodBoardId() {
        return foodBoardId;
    }

    public void setFoodBoardId(int foodBoardId) {
        this.foodBoardId = foodBoardId;
    }

    public int getFoodId() {
        return foodId;
    }

    public void setFoodId(int foodId) {
        this.foodId = foodId;
    }

    public int getBoardId() {
        return boardId;
    }

    public void setBoardId(int boardId) {
        this.boardId = boardId;
    }

    public byte[] getFoodBoardImg() {
        return foodBoardImg;
    }

    public void setFoodBoardImg(byte[] foodBoardImg) {
        this.foodBoardImg = foodBoardImg;
    }

    public int getFoodBoardStatus() {
        return foodBoardStatus;
    }

    public void setFoodBoardStatus(int foodBoardStatus) {
        this.foodBoardStatus = foodBoardStatus;
    }
}
