import javax.swing.*;

public class Main extends JFrame {
    public Main () {
        setTitle("CPR 3663 - Scouting QR Scanner");
        setDefaultCloseOperation(EXIT_ON_CLOSE);
        setSize(600,900);
        setLocationRelativeTo(null);
        setVisible(true);
    }

    public static void main(String[] args) {
        new Main();
    }
}
