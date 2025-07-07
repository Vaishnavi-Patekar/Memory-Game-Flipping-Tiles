import javax.swing.*;                     
import java.awt.*;                        
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;    
import java.util.ArrayList;
import java.util.Collections;            
import java.util.List;

public class GameBoard extends JFrame {
    private ArrayList<Tile> tiles = new ArrayList<>();
    private Tile firstSelected = null;
    private Tile secondSelected = null;
    private Timer flipBackTimer;

    private int moves = 0;
    private int seconds = 0;
    private JLabel moveLabel;
    private JLabel timerLabel;
    private Timer gameTimer;

    private int rows, cols;
    private boolean darkMode = false;

    public GameBoard(int rows, int cols) {
        this.rows = rows;
        this.cols = cols;

        setTitle("🧠 Memory Game - Flipping Tiles");
        setSize(600, 700);
        setDefaultCloseOperation(EXIT_ON_CLOSE);
        setLayout(new BorderLayout());
        setLocationRelativeTo(null);

        // Top panel
        JPanel topPanel = new JPanel(new FlowLayout(FlowLayout.CENTER, 20, 10));
        moveLabel = new JLabel("Moves: 0");
        timerLabel = new JLabel("Time: 0s");

        JButton restartButton = new JButton("🔁 Restart");
        restartButton.setFocusable(false);
        restartButton.addActionListener(e -> restartGame());

        JButton themeToggle = new JButton("🌙 Dark Mode");
        themeToggle.setFocusable(false);
        themeToggle.addActionListener(e -> {
            darkMode = !darkMode;
            themeToggle.setText(darkMode ? "☀ Light Mode" : "🌙 Dark Mode");
            applyTheme();
        });

        topPanel.add(moveLabel);
        topPanel.add(timerLabel);
        topPanel.add(restartButton);
        topPanel.add(themeToggle);

        // Main grid panel
        JPanel gridPanel = new JPanel(new GridLayout(rows, cols, 10, 10));
        gridPanel.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));

        int pairCount = (rows * cols) / 2;
        List<String> values = generateValues(pairCount);
        Collections.shuffle(values);

        for (String value : values) {
            Tile tile = new Tile(value);
            tile.addActionListener(new TileFlipper());
            tiles.add(tile);
            gridPanel.add(tile);
        }

        add(topPanel, BorderLayout.NORTH);
        add(gridPanel, BorderLayout.CENTER);

        startTimer();
        applyTheme();
        setVisible(true);
    }

    private List<String> generateValues(int pairs) {
        List<String> values = new ArrayList<>();
        for (int i = 1; i <= pairs; i++) {
            values.add(String.valueOf(i));
            values.add(String.valueOf(i));
        }
        return values;
    }

    private void restartGame() {
        gameTimer.stop();
        dispose();
        new Main().launch(); // Restart through Main
    }

    private void startTimer() {
        seconds = 0;
        gameTimer = new Timer(1000, e -> {
            seconds++;
            timerLabel.setText("Time: " + seconds + "s");
        });
        gameTimer.start();
    }

    private void applyTheme() {
        Color bgColor = darkMode ? new Color(40, 44, 52) : Color.WHITE;
        Color tileColor = darkMode ? new Color(70, 80, 90) : new Color(0xeeeeee);
        Color matchedColor = darkMode ? new Color(60, 130, 120) : new Color(0xa0e7e5);
        Color textColor = darkMode ? Color.WHITE : Color.DARK_GRAY;

        getContentPane().setBackground(bgColor);
        for (Tile tile : tiles) {
            if (tile.isMatched()) {
                tile.setBackground(matchedColor);
            } else {
                tile.setBackground(tileColor);
            }
            tile.setForeground(textColor);
        }
    }

    private class TileFlipper implements ActionListener {
        public void actionPerformed(ActionEvent e) {
            Tile clickedTile = (Tile) e.getSource();

            if (clickedTile.isFlipped() || clickedTile.isMatched())
                return;

            clickedTile.flip();

            if (firstSelected == null) {
                firstSelected = clickedTile;
            } else if (secondSelected == null) {
                secondSelected = clickedTile;
                moves++;
                moveLabel.setText("Moves: " + moves);
                checkMatch();
            }
        }

        private void checkMatch() {
            if (firstSelected.getValue().equals(secondSelected.getValue())) {
                firstSelected.setMatched(true);
                secondSelected.setMatched(true);
                firstSelected = null;
                secondSelected = null;
                applyTheme(); // update matched colors
                checkWin();
            } else {
                flipBackTimer = new Timer(1000, e -> {
                    firstSelected.flip();
                    secondSelected.flip();
                    firstSelected = null;
                    secondSelected = null;
                    flipBackTimer.stop();
                });
                flipBackTimer.setRepeats(false);
                flipBackTimer.start();
            }
        }

        private void checkWin() {
            boolean allMatched = tiles.stream().allMatch(Tile::isMatched);
            if (allMatched) {
                gameTimer.stop();
                JOptionPane.showMessageDialog(null,
                    "🎉 You won in " + moves + " moves and " + seconds + " seconds!");
            }
        }
    }
}
