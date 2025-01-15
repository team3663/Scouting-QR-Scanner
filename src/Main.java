import javax.swing.*;
import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;
import java.io.*;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.Timer;
import java.util.TimerTask;

public class Main extends JFrame {
    private JPanel MainPanel;
    private JButton btn_Save;
    private JLabel lbl_Status;
    private JTextArea txt_Data;
    private JTextField txt_FilePath;
    private JLabel lbl_FilePath;

    // Maximum time we wait (after text starts to flow in) after text stops to flow in before we trigger an auto-save
    private final long MAX_DATA_ENTRY_PAUSE = 1_000;
    // System time the last key was pressed (or flow in from scanner)
    private long lastKeyPressTime = 0;
    // Current system time
    private long current_time = 0;

    public Main () {
        // Set FORM values
        setContentPane(MainPanel);
        setTitle("CPR 3663 - Scouting QR Scanner");
        setDefaultCloseOperation(EXIT_ON_CLOSE);
        setSize(600,900);
        setLocationRelativeTo(null);
        setVisible(true);

        // Timer to check periodically if text stopped coming in
        Timer timer;

        // Set up a button to FORCE an auto-save
        btn_Save.addActionListener(e -> {
            saveDataToFile();
        });

        // Listen for text entered into the TextArea and record the time the key was last pressed
        txt_Data.addKeyListener(new KeyAdapter() {
            @Override
            public void keyTyped(KeyEvent e) {
                super.keyTyped(e);
                lastKeyPressTime = System.currentTimeMillis();
            }
        });

        // Set up the timer to check the conditions and take action
        timer = new Timer();
        TimerTask timer_task = new TimerTask() {
            @Override
            public void run() {
                // If no text has been entered, do nothing.
                if (lastKeyPressTime == 0)
                    return;

                // Check how long it's been since text was entered into the TextArea
                current_time = System.currentTimeMillis();
                long diff_time = current_time - lastKeyPressTime;
                String message = String.valueOf(diff_time); // TODO: REMOVE

                // If we've waited beyond the threshold, auto-save the text to a file
                if (diff_time > MAX_DATA_ENTRY_PAUSE) {
                    message += " **";  // TODO: REMOVE
                    // Reset the time the last key was pressed since we're now waiting for another QR code
                    lastKeyPressTime = 0;
                    // call the function to save off the file here
                }

                lbl_Status.setText(message); // TODO: REMOVE
            }
        };

        timer.schedule(timer_task, 0, 500);
    }

    public static void main(String[] args) {
        new Main();
    }

    public void saveDataToFile() {
        String filename = txt_Data.getText().split("\\n", 2)[0];
        String data = txt_Data.getText().split("\\n",2)[1];
        String filepath = txt_FilePath.getText() + "\\" + filename;

        File file = new File(filepath);

        // Error Checking (minimal)
        // If the file already exists, display a message and stop.
        if (file.exists()) {
            JOptionPane.showMessageDialog(Main.this, "File already exists");
            return;
        }

        // Output the data to the file
        try {
            PrintStream ps = new PrintStream(new FileOutputStream(file));
            ps.print(data);
            ps.close();
        } catch (FileNotFoundException e) {
            throw new RuntimeException(e);
        } finally {
            // If we were successful, clear out the text box for the next incoming file.
            txt_Data.setText("");
        }
    }
}