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
    private int numFilesSaved = 0;

    private final String FILE_LINE_SEPARATOR = "\r\n";
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
        btn_Save.addActionListener(e -> saveDataToFile());

        // Listen for text entered into the TextArea and record the time the key was last pressed
        txt_Data.addKeyListener(new KeyAdapter() {
            @Override
            public void keyReleased(KeyEvent e) {
                if (!box_AutoMode.isSelected()) {
                    super.keyReleased(e);
                    return;
                }

                int index = txt_Data.getText().indexOf(FILE_LINE_SEPARATOR + end_of_file);
                if (index < 0) index = txt_Data.getText().indexOf("\n" + end_of_file);

                if (index < 0) {
                    super.keyReleased(e);
                    return;
                }

                if (!saveDataToFile()) {
                    box_AutoMode.setSelected(false);
                }
            }
        });
    }

    public static void main(String[] args) {
        new Main();
    }

    public boolean saveDataToFile() {
        String in_string;
        String filename;
        String data;

        txt_Data.setText(txt_Data.getText().trim());
        if (!txt_Data.getText().contains("\r")) txt_Data.setText(txt_Data.getText().replace("\n", FILE_LINE_SEPARATOR));
        int index = txt_Data.getText().indexOf(FILE_LINE_SEPARATOR + end_of_file);

        // Process data in the textbox - keep looping if until there's no EOF
        while (index > 0) {
            filename = txt_Data.getText().substring(0, index).split(FILE_LINE_SEPARATOR, 2)[0].trim();
            data =txt_Data.getText().substring(0, index).split(FILE_LINE_SEPARATOR, 2)[1];
            txt_Data.setText(txt_Data.getText().substring(index + FILE_LINE_SEPARATOR.length() + end_of_file.length()).trim());

            if (!saveFile(filename, data)) return false;

            index = txt_Data.getText().indexOf(FILE_LINE_SEPARATOR + end_of_file);
        }

        return true;
    }

    public boolean saveFile(String in_filename, String in_data) {
        String filepath = txt_FilePath.getText() + "\\" + in_filename;

        File file = new File(filepath);

        // Error Checking (minimal)
        // If the file already exists, display a message and stop.
        if (file.exists()) {
            lbl_Status.setText("File exists (" + filepath + ")");
            JOptionPane.showMessageDialog(Main.this, "File already exists");
            return false;
        }

        // Output the data to the file
        try {
            PrintStream ps = new PrintStream(new FileOutputStream(file));
            ps.print(in_data);
            ps.close();
        } catch (FileNotFoundException e) {
            throw new RuntimeException(e);
        } finally {
            numFilesSaved++;
            lbl_Status.setText("Status: " + numFilesSaved + " files saved (Last: " + filepath + ")");
        }

        return true;
    }
}