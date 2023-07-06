import java.awt.*;
import java.awt.event.*;
import javax.swing.*;

public class TicTacToeGame extends JFrame {
    private JButton[] buttons;
    private boolean playerX;
    private boolean gameOver;
    
    public TicTacToeGame() {
        setTitle("Tic-Tac-Toe");
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setSize(300, 300);
        setLayout(new GridLayout(3, 3));
        
        buttons = new JButton[9];
        playerX = true;
        gameOver = false;
        
        initializeButtons();
    }
    
    private void initializeButtons() {
        for (int i = 0; i < 9; i++) {
            JButton button = new JButton();
            button.setFont(new Font(Font.SANS_SERIF, Font.BOLD, 40));
            buttons[i] = button;
            button.addActionListener(new ButtonClickListener());
            add(button);
        }
    }
    
    private class ButtonClickListener implements ActionListener {
        @Override
        public void actionPerformed(ActionEvent e) {
            JButton clickedButton = (JButton) e.getSource();
            
            if (clickedButton.getText().equals("") && !gameOver) {
                if (playerX) {
                    clickedButton.setText("X");
                } else {
                    clickedButton.setText("O");
                }
                
                if (checkForWin()) {
                    JOptionPane.showMessageDialog(null, "Player " + (playerX ? "X" : "O") + " wins!");
                    gameOver = true;
                } else if (checkForDraw()) {
                    JOptionPane.showMessageDialog(null, "It's a draw!");
                    gameOver = true;
                } else {
                    playerX = !playerX;
                }
            }
        }
    }
    
    private boolean checkForWin() {
        String[] board = new String[9];
        
        for (int i = 0; i < 9; i++) {
            board[i] = buttons[i].getText();
        }
        
        // Check rows
        for (int i = 0; i <= 6; i += 3) {
            if (!board[i].equals("") && board[i].equals(board[i + 1]) && board[i].equals(board[i + 2])) {
                return true;
            }
        }
        
        // Check columns
        for (int i = 0; i <= 2; i++) {
            if (!board[i].equals("") && board[i].equals(board[i + 3]) && board[i].equals(board[i + 6])) {
                return true;
            }
        }
        
        // Check diagonals
        if (!board[0].equals("") && board[0].equals(board[4]) && board[0].equals(board[8])) {
            return true;
        }
        
        if (!board[2].equals("") && board[2].equals(board[4]) && board[2].equals(board[6])) {
            return true;
        }
        
        return false;
    }
    
    private boolean checkForDraw() {
        for (int i = 0; i < 9; i++) {
            if (buttons[i].getText().equals("")) {
                return false;
            }
        }
        
        return true;
    }
    
    public static void main(String[] args) {
        SwingUtilities.invokeLater(new Runnable() {
            public void run() {
                TicTacToeGame game = new TicTacToeGame();
                game.setVisible(true);
            }
        });
    }
}
