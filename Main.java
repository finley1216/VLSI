
//package game;
import javax.swing.*;
import java.awt.*;
import java.net.URL;
import java.awt.event.*;

class Gameframe extends JFrame implements ActionListener {
    private int W = 480;
    private int H = 613;
    private JFrame obj = new JFrame("俄羅斯方塊");
    MyPanel panel;

    public Gameframe() {
        Setbackground();
    }

    private void Setbackground() {
        JButton BUTTON = new JButton("START");
        Font f = new Font("Monospaced", Font.BOLD, 20);
        JLabel picture = new JLabel();
        // 剛凱始遊戲畫面
        obj.setBounds(100, 200, W, H);
        obj.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        obj.getContentPane().setLayout(null);
        // 開始按鈕
        BUTTON.setBounds(180, 450, 100, 50);
        BUTTON.setFont(f);
        BUTTON.setFocusPainted(false);
        BUTTON.addActionListener(this);
        obj.getContentPane().add(BUTTON);
        // background picture
        URL pic = this.getClass().getResource("picture4.jpg");
        ImageIcon icon = new ImageIcon(pic);
        picture.setIcon(icon);
        picture.setBounds(-45, 5, icon.getIconWidth(), icon.getIconWidth());
        obj.getContentPane().add(picture);
        obj.setLocationByPlatform(true);
        obj.setResizable(false);
        obj.setVisible(true);
    }

    public void actionPerformed(ActionEvent e) {
        obj.dispose();
        setnextbackground();

    }

    // 下一個遊戲視窗
    private void setnextbackground() {
        panel = new MyPanel();
        this.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        this.add(panel);
        this.pack();
        this.addKeyListener(panel);
        this.setVisible(true);

    }

}

public class Main {
    public static void main(String[] argv) {
        Gameframe game = new Gameframe();
    }
}