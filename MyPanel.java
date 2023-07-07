import javax.swing.JPanel;
import java.awt.*;
import javax.swing.*;
import java.awt.geom.RoundRectangle2D;

import java.awt.event.*;
import java.awt.Color;

public class MyPanel extends JPanel implements ActionListener, KeyListener {
    private int COL = 10;
    private int ROW = 16;
    private final int fps = 60;
    private final int delay = fps / 1000;
    private int BLOCK_SIZE = 30;
    private Timer time;
    // state狀態
    public int startplay = 0;
    public int startplause = 1;
    public int gameover = 2;
    private int state = startplay;
    // 分數
    public int score = 0;
    public int r;
    // T,S,S,7,7,l,田
    private Color[][] board = new Color[16][10];
    private Color[] colors = { Color.decode("#00FFFF"), Color.decode("#800080"), Color.decode("#FFA500"),
            Color.decode("#0000FF"), Color.decode("#00FF00"), Color.decode("#FF0000"), Color.decode("#FFFF00") };
    private Block[] shapes = new Block[7];
    private int[] regblock = new int[1000];
    private int[] reg4block = new int[4];
    public int i = 0;
    private Block currentShape;
    private int[][][] s = {
            { { 1, 1, 1, 1 } },
            { { 1, 1, 1 }, { 0, 1, 0 } },
            { { 1, 1, 1 }, { 0, 0, 1 } },
            { { 1, 1, 1 }, { 1, 0, 0 } },
            { { 0, 1, 1 }, { 1, 1, 0 } },
            { { 1, 1, 0 }, { 0, 1, 1 } },
            { { 1, 1 }, { 1, 1 } } };

    MyPanel() {
        // 0~6 所有方塊類型
        shapes[0] = new Block(new int[][] {
                { 1, 1, 1, 1 } // I shape;
        }, this, colors[0], score);
        shapes[1] = new Block(new int[][] {
                { 1, 1, 1 }, { 0, 1, 0 }, // T shape;
        }, this, colors[1], score);
        shapes[2] = new Block(new int[][] {
                { 1, 1, 1 }, { 1, 0, 0 }, // L shape; 橘
        }, this, colors[2], score);

        shapes[3] = new Block(new int[][] {
                { 1, 1, 1 }, { 0, 0, 1 }, // J shape; 藍
        }, this, colors[3], score);

        shapes[4] = new Block(new int[][] {
                { 0, 1, 1 }, { 1, 1, 0 }, // S shape;
        }, this, colors[4], score);

        shapes[5] = new Block(new int[][] {
                { 1, 1, 0 }, { 0, 1, 1 }, // Z shape;
        }, this, colors[5], score);
        shapes[6] = new Block(new int[][] {
                { 1, 1 }, { 1, 1 }, // O shape;
        }, this, colors[6], score);

        for (int R = 0; R < 1000; R++) {
            int reg = (int) (Math.random() * 7);
            regblock[R] = reg;
        }
        // 第一個方塊
        currentShape = shapes[regblock[0]];
        reg4block[0] = regblock[0];
        reg4block[1] = regblock[1];
        reg4block[2] = regblock[2];
        reg4block[3] = regblock[3];
        this.setPreferredSize(new Dimension(550, 500));
        time = new Timer(delay, new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                update();
                repaint();
            }
        });
        time.start();
    }

    private void update() {
        if (state == startplay) {
            currentShape.update();
        }
    }

    // 取得方塊
    public void setcurrentshape() {
        score += 50;
        score += currentShape.getscore();
        currentShape = shapes[reg4block[0]];
        i++;
        currentShape.reset();
        checkgameover();
    }

    // 檢查是否遊戲結束
    private void checkgameover() {
        int[][] coords = currentShape.getCoords();
        for (int row = 0; row < coords.length; row++) {
            for (int col = 0; col < coords[0].length; col++) {
                if (coords[row][col] != 0) {
                    if (board[row + currentShape.getY()][col + currentShape.getX()] != null) {
                        state = gameover;
                    }
                }
            }
        }
    }

    public void actionPerformed(ActionEvent c) {
    }

    public void paint(Graphics g) {
        super.paint(g);
        paintmid(g);
        paintright(g);
        paintleft(g);
        drawText(g);
        paintplayway(g);
        paintregblock(g);
        startgame(g);
        drawline(g);
        paintscore(g);
        if (state == startplause) {
            g.setColor(Color.BLACK);
            g.setFont(new Font("Georgia", Font.BOLD, 70));
            g.drawString("PAUSE", 138, 270);
        }
        for (int l = i, m = 0; l < i + 4; l++, m++) {
            reg4block[m] = regblock[l];
        }
    }

    // 遊戲開始
    public void startgame(Graphics g) {
        currentShape.render(g);
        stack(g);
    }

    // 堆疊
    private void stack(Graphics g) {
        for (int w = 0; w < board.length; w++) {
            for (int s = 0; s < board[w].length; s++) {
                if (board[w][s] != null) {
                    g.setColor(board[w][s]);
                    g.fillRect(118 + s * BLOCK_SIZE, 12 + BLOCK_SIZE * w, BLOCK_SIZE, BLOCK_SIZE);
                }
            }
        }
    }

    // 印出分數
    public void paintscore(Graphics g) {
        g.setColor(Color.BLACK);
        g.drawString(Integer.toString(score), 17, 50);
    }

    // 印出遊戲玩法
    private void paintplayway(Graphics g) {
        g.setColor(Color.BLACK);
        g.setFont(new Font("Monospaced", Font.BOLD, 20));
        g.drawString("玩法:", 5, 150);
        g.drawString("⇨:Right", 5, 180);
        g.drawString("⇦:Left", 5, 210);
        g.drawString("⇧:Rotate", 5, 240);
        g.drawString("⇩:Down", 5, 270);
        g.drawString("[Shift]:", 5, 300);
        g.drawString("Pause", 6, 330);
        g.drawString("[Space]:", 5, 360);
        g.drawString("Movedown", 6, 390);

    }

    // 印出下一個方塊
    private void paintregblock(Graphics g) {
        for (int r = 0; r < s[reg4block[0]].length; r++) {
            for (int w = 0; w < s[reg4block[0]][0].length; w++) {
                if (s[reg4block[0]][r][w] != 0) {
                    g.setColor(colors[reg4block[0]]);
                    g.fillRect(460 + r * 30, 45 + 30 * w, 29, 29);
                    repaint();
                }

            }
        }
        for (int r = 0; r < s[reg4block[1]].length; r++) {
            for (int w = 0; w < s[reg4block[1]][0].length; w++) {
                if (s[reg4block[1]][r][w] != 0) {
                    g.setColor(colors[reg4block[1]]);
                    g.fillRect(460 + r * 30, 185 + 30 * w, 29, 29);
                    repaint();
                }

            }
        }
    }

    // 中間框框
    private void paintmid(Graphics g) {
        BasicStroke bs = new BasicStroke(8L, BasicStroke.CAP_ROUND, BasicStroke.JOIN_MITER);
        Graphics2D gd = (Graphics2D) g;
        gd.setColor(new Color(128, 128, 128));
        gd.setStroke(bs);
        RoundRectangle2D.Double rect = new RoundRectangle2D.Double(114, 8, 308, 488, 2, 2);
        gd.draw(rect);
    }

    // 右邊框框
    private void paintright(Graphics g) {
        BasicStroke bs = new BasicStroke(8L, BasicStroke.CAP_ROUND, BasicStroke.JOIN_MITER);
        Graphics2D gd = (Graphics2D) g;
        gd.setColor(new Color(128, 128, 128));
        gd.setStroke(bs);
        RoundRectangle2D.Double rect = new RoundRectangle2D.Double(435, 6, 110, 310, 1, 1);
        gd.draw(rect);
        RoundRectangle2D.Double rect1 = new RoundRectangle2D.Double(435, 6, 110, 170, 1, 1);
        gd.draw(rect1);
    }

    // 左邊框框
    private void paintleft(Graphics g) {
        BasicStroke bs2 = new BasicStroke(8L, BasicStroke.CAP_ROUND,
                BasicStroke.JOIN_MITER);
        Graphics2D g3d = (Graphics2D) g;
        g3d.setColor(new Color(128, 128, 128));
        g3d.setStroke(bs2);
        RoundRectangle2D.Double rect = new RoundRectangle2D.Double(6, 6, 95, 120, 1, 1);
        g3d.draw(rect);
    }

    // 文字
    private void drawText(Graphics g) {
        Graphics2D g_2d = (Graphics2D) g;
        g_2d.setColor(new Color(0, 0, 0));
        // 得分
        g.setFont(new Font("Monospaced", Font.BOLD, 20));
        g.drawString("Next：", 455, 30);
        g.setFont(new Font("Monospaced", Font.BOLD, 20));
        g.drawString("SCORE：", 15, 30);
    }

    // 格子
    private void drawline(Graphics g) {
        BasicStroke bs2 = new BasicStroke(3L, BasicStroke.CAP_ROUND, BasicStroke.JOIN_MITER);
        Graphics2D g_2d = (Graphics2D) g;
        g_2d.setColor(new Color(0, 0, 0, 50));
        g_2d.setStroke(bs2);
        int x1 = 118;
        int y1 = 20;
        int x2 = 420;
        int y2 = 20;
        for (int i = 1; i <= ROW; i++) {
            y1 = 12 + 30 * i;
            y2 = 12 + 30 * i;
            g_2d.drawLine(x1, y1, x2, y2);
        }
        y1 = 10;
        y2 = 490;
        for (int i = 1; i < COL; i++) {
            x1 = 118 + 30 * i;
            x2 = 118 + 30 * i;
            g_2d.drawLine(x1, y1, x2, y2);
        }
    }

    public void keyTyped(KeyEvent e) {
    }

    public Color[][] getboard() {
        return board;
    }

    // 按鈕按下
    public void keyPressed(KeyEvent e) {

        if (e.getKeyCode() == KeyEvent.VK_DOWN) {
            currentShape.speedup();
        } else if (e.getKeyCode() == KeyEvent.VK_LEFT) {
            currentShape.moveLeft();
        } else if (e.getKeyCode() == KeyEvent.VK_RIGHT) {
            currentShape.moveRight();
        } else if (e.getKeyCode() == KeyEvent.VK_UP) {
            currentShape.rotate();
        } else if (e.getKeyCode() == KeyEvent.VK_SPACE) {
            currentShape.movefastdown();
        }
        // 暫停
        if (state != gameover) {
            if (e.getKeyCode() == KeyEvent.VK_SHIFT) {
                if (state == startplay) {
                    state = startplause;
                } else if (state == startplause) {
                    state = startplay;
                }
            }
        }
        // 遊戲結束跳出提示
        if (state == gameover) {
            int result = JOptionPane.showConfirmDialog(null, "需要結束嗎??", "遊戲結束", JOptionPane.YES_NO_OPTION,
                    JOptionPane.QUESTION_MESSAGE);
            if (result == JOptionPane.YES_OPTION) {
                System.exit(0);
            }
        }
    }

    // 當向下鍵放開 回復原來速度
    public void keyReleased(KeyEvent e) {
        if (e.getKeyCode() == KeyEvent.VK_DOWN) {
            currentShape.speeddown();
        }
        if (e.getKeyCode() == KeyEvent.VK_SPACE) {
            currentShape.speeddown();
        }
    }
}
