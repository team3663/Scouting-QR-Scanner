import javax.swing.*;
import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.PrintStream;

public class Main extends JFrame {
    private JPanel MainPanel;
    private JButton btn_Save;
    private JLabel lbl_Status;
    private JLabel lbl_FilePath;
    private JTextArea txt_Data;
    private JTextField txt_FilePath;
    private JCheckBox box_AutoMode;

    private final String default_status = "Status: ";
    private final String end_of_file = "EOF";

    public Main() {
        // Set FORM values
        setContentPane(MainPanel);
        setTitle("CPR 3663 - Scouting QR Scanner");
        setDefaultCloseOperation(EXIT_ON_CLOSE);
        setSize(600, 900);
        setLocationRelativeTo(null);
        setVisible(true);

        // Set up a button to FORCE an auto-save
        btn_Save.addActionListener(e -> saveDataToFile(txt_Data.getText()));

        // Listen for text entered into the TextArea and record the time the key was last pressed
        txt_Data.addKeyListener(new KeyAdapter() {
            @Override
            public void keyReleased(KeyEvent e) {
                if (!box_AutoMode.isSelected()) {
                    super.keyReleased(e);
                    return;
                }

                lbl_Status.setText(default_status);
                int index = txt_Data.getText().indexOf("\n" + end_of_file);

                if (index < 0) {
                    super.keyReleased(e);
                    return;
                }

                while (index >= 0) {
                    String str_data = txt_Data.getText().substring(0, index);
                    if (saveDataToFile(str_data)) {
                        if ((index + end_of_file.length() + 2) > txt_Data.getText().length())
                            txt_Data.setText("");
                        else
                            txt_Data.setText(txt_Data.getText().substring(index + end_of_file.length() + 2));
                    } else {
                        box_AutoMode.setSelected(false);
                        return;
                    }
                    index = txt_Data.getText().indexOf("\n" + end_of_file);
                }
            }
        });
    }

    public static void main(String[] args) {
        new Main();
    }

    public boolean saveDataToFile(String in_string) {
        in_string = in_string.trim();

        String filename = in_string.split("\\n", 2)[0].trim();
        String data = in_string.split("\\n", 2)[1];
        String filepath = txt_FilePath.getText() + "\\" + filename;

        File file = new File(filepath);

        // Error Checking (minimal)
        // If the file already exists, display a message and stop.
        if (file.exists()) {
            JOptionPane.showMessageDialog(Main.this, "File already exists");
            return false;
        }

        // Output the data to the file
        try {
            PrintStream ps = new PrintStream(new FileOutputStream(file));
            ps.print(data);
            ps.close();
        } catch (FileNotFoundException e) {
            throw new RuntimeException(e);
        } finally {
            lbl_Status.setText(default_status + " File " + filepath + " saved!");
        }

        return true;
    }
}