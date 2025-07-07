import javax.swing.JButton;
import java.awt.Color;
import java.awt.Font;

public class Tile extends JButton {
    private String value;
    private boolean isFlipped = false;
    private boolean isMatched = false;

    public Tile(String value) {
        this.value = value;
        setText(""); // initially hidden
        setFocusPainted(false);
        setFont(new Font("Arial", Font.BOLD, 22));
        setBackground(new Color(0xeeeeee));
        setForeground(Color.DARK_GRAY);
    }

    public void flip() {
        if (!isMatched) {
            isFlipped = !isFlipped;
            setText(isFlipped ? value : "");
        }
    }

    public String getValue() {
        return value;
    }

    public boolean isFlipped() {
        return isFlipped;
    }

    public void setMatched(boolean matched) {
        isMatched = matched;
        setEnabled(false);
        setBackground(new Color(0xa0e7e5)); // light blue for matched
    }

    public boolean isMatched() {
        return isMatched;
    }
}
