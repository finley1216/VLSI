import java.awt.Graphics;
import java.awt.Color;

public class Block {
    private int X = 4, Y = 0;
    private int block_x = 30, block_y = 30;
    private int deltax = 0;
    private int normal = 600;
    private int fast = 50;
    private int delaytimeforMovent = normal;
    private long begintime;
    private boolean collision = false;
    private int STATEBOARD[][];
    private MyPanel board;
    private Color color;
    public int score = 0;
    public int reg = 0;

    // 取得Mypanel 的物件
    public Block(int[][] STATEBOARD, MyPanel board, Color color, int score) {
        this.STATEBOARD = STATEBOARD;
        this.board = board;
        this.color = color;
        this.score = score;
    }

    // 消除一行 +50分
    public void update() {
        if (collision) {
            for (int row = 0; row < STATEBOARD.length; row++) {
                for (int col = 0; col < STATEBOARD[0].length; col++) {
                    if (STATEBOARD[row][col] != 0) {
                        board.getboard()[Y + row][X + col] = color;
                    }
                }
            }
            clearline();
            board.setcurrentshape();
            return;
        }
        // 水平移動
        boolean moveLR = true;
        if (X + deltax + STATEBOARD[0].length <= 10 && (X + deltax >= 0)) { // 避免移動到格子外面
            for (int row = 0; row < STATEBOARD.length; row++) {
                for (int col = 0; col < STATEBOARD[row].length; col++) {
                    if (STATEBOARD[row][col] != 0) {
                        if (board.getboard()[Y + row][X + deltax + col] != null) {
                            moveLR = false;
                        }
                    }
                }
            }
            if (moveLR) {
                X += deltax;
            }
        }
        deltax = 0;
        // 垂直移動
        if (System.currentTimeMillis() - begintime > delaytimeforMovent) {
            if (!(Y + 1 + STATEBOARD.length > 16)) { // 避免掉落到地面下面
                for (int row = 0; row < STATEBOARD.length; row++) {
                    for (int col = 0; col < STATEBOARD[row].length; col++) {
                        if (STATEBOARD[row][col] != 0) {
                            if (board.getboard()[Y + 1 + row][X + deltax + col] != null) {
                                collision = true;
                            }
                        }
                    }
                }
                if (!collision) {
                    Y++;
                }
            } else {
                collision = true;
            }
            begintime = System.currentTimeMillis();
        }
    }

    // 消除
    private void clearline() {
        int buttonLine = board.getboard().length - 1;
        for (int topline = board.getboard().length - 1; topline > 0; topline--) {
            int count = 0;
            for (int col = 0; col < board.getboard()[0].length; col++) {
                if (board.getboard()[topline][col] != null) {
                    count++;
                }

                board.getboard()[buttonLine][col] = board.getboard()[topline][col];
            }
            if (count < board.getboard()[0].length) {
                buttonLine--;
            }
            if (count == 10) {
                score += 1000;
            }
        }
    }

    public void render(Graphics g) {
        for (int w = 0; w < STATEBOARD.length; w++) {
            for (int s = 0; s < STATEBOARD[0].length; s++) {
                if (STATEBOARD[w][s] != 0) {
                    g.setColor(color);
                    g.fillRect(118 + s * block_x + 30 * X, 12 + block_y * w + 30 * Y, block_x, block_y);
                }
            }
        }
    }

    // 旋轉
    public void rotate() {
        int[][] rotatedShape = null;
        rotatedShape = transpose(STATEBOARD);
        rotatedShape = reverseRows(rotatedShape);
        // 檢查旋轉時會不會超出格子
        if ((X + rotatedShape[0].length > 10) || (Y + rotatedShape.length > 16)) {
            return;
        }
        // 檢查旋轉時會不會碰到其他方塊
        for (int row = 0; row < rotatedShape.length; row++) {
            for (int col = 0; col < rotatedShape[row].length; col++) {
                if (rotatedShape[row][col] != 0) {
                    if (board.getboard()[Y + row][X + col] != null) {
                        return;
                    }
                }
            }
        }
        STATEBOARD = rotatedShape;
    }

    private int[][] transpose(int[][] matrix) {
        int[][] temp = new int[matrix[0].length][matrix.length];
        for (int i = 0; i < matrix.length; i++) {
            for (int j = 0; j < matrix[0].length; j++) {
                temp[j][i] = matrix[i][j];
            }
        }
        return temp;
    }

    private int[][] reverseRows(int[][] matrix) {
        int middle = matrix.length / 2;
        for (int i = 0; i < middle; i++) {
            int[] temp = matrix[i];
            matrix[i] = matrix[matrix.length - i - 1];
            matrix[matrix.length - i - 1] = temp;
        }
        return matrix;
    }

    public void setx(int X) {
        this.X = X;
    }

    public void sety(int Y) {
        this.Y = Y;
    }

    public void reset() {
        this.X = 4;
        this.Y = 0;
        collision = false;
    }

    public void speedup() {
        delaytimeforMovent = fast;
    }

    public void speeddown() {
        delaytimeforMovent = normal;
    }

    public void movefastdown() {
        delaytimeforMovent = 1;
    }

    public void moveRight() {
        deltax = 1;
    }

    public void moveLeft() {
        deltax = -1;
    }

    public int[][] getCoords() {
        return STATEBOARD;
    }

    public int getX() {
        return X;
    }

    public int getY() {
        return Y;
    }

    public int getscore() {
        return score;
    }
}