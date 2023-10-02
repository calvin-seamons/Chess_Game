package chess;

public class Chess_Position implements ChessPosition{
    private int row;
    private int col;

    public Chess_Position(int row, int col) {
        if (row < 1 || row > 8 || col < 1 || col > 8) {
            throw new IllegalArgumentException("Invalid position");
        }
        this.row = row;
        this.col = col;
    }

    /**
     * @return which row this position is in
     * 1 codes for the bottom row
     */
    @Override
    public int getRow() {
        return row;
    }

    /**
     * @return which column this position is in
     * 1 codes for the left row
     */
    @Override
    public int getColumn() {
        return col;
    }
}
