import javax.swing.*;

public class Main {
    public static void main(String[] args) {
        new Main().launch();
    }

    public void launch() {
        SwingUtilities.invokeLater(() -> {
            String[] options = {"Easy (3x4)", "Medium (4x4)", "Hard (6x6)"};
            int choice = JOptionPane.showOptionDialog(null, "Select Difficulty Level", "Memory Game",
                    JOptionPane.DEFAULT_OPTION, JOptionPane.QUESTION_MESSAGE, null, options, options[1]);

            int rows = 4;
            int cols = 4;

            switch (choice) {
                case 0: rows = 3; cols = 4; break;
                case 1: rows = 4; cols = 4; break;
                case 2: rows = 6; cols = 6; break;
                default: System.exit(0);
            }

            new GameBoard(rows, cols);
        });
    }
}
