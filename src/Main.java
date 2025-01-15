import javax.swing.*;

public class Main extends JFrame {
    private JTextField txtFileData;
    private JPanel MainPanel;
    private JButton btn_Save;

    public Main () {
        setContentPane(MainPanel);
        setTitle("CPR 3663 - Scouting QR Scanner");
        setDefaultCloseOperation(EXIT_ON_CLOSE);
        setSize(600,900);
        setLocationRelativeTo(null);
        setVisible(true);

        btn_Save.addActionListener(e -> {
            String data = txtFileData.getText();
            JOptionPane.showMessageDialog(Main.this, "Clicked the button! " + data);
        });
    }

    public static void main(String[] args) {
        new Main();
    }
}
