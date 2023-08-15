import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

public class ScientificCalculator extends JFrame {
    private JTextField displayField;
    private JButton[] numberButtons;
    private JButton[] functionButtons;
    private JButton equalsButton;
    private JButton clearButton;
    private JButton dotButton;
    private JButton backButton;
    private JButton sqrtButton;
    private JButton sinButton;
    private JButton cosButton;
    private JButton tanButton;
    
    private String input;
    
    public ScientificCalculator() {
        setTitle("Scientific Calculator");
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setSize(300, 400);
        setLayout(new BorderLayout());
        
        input = "";
        
        displayField = new JTextField();
        displayField.setHorizontalAlignment(JTextField.RIGHT);
        displayField.setEditable(false);
        add(displayField, BorderLayout.NORTH);
        
        JPanel buttonPanel = new JPanel();
        buttonPanel.setLayout(new GridLayout(5, 4));
        
        numberButtons = new JButton[10];
        for (int i = 0; i < 10; i++) {
            numberButtons[i] = new JButton(Integer.toString(i));
            numberButtons[i].addActionListener(new NumberButtonClickListener());
            buttonPanel.add(numberButtons[i]);
        }
        
        functionButtons = new JButton[6];
        equalsButton = new JButton("=");
        clearButton = new JButton("C");
        dotButton = new JButton(".");
        backButton = new JButton("←");
        sqrtButton = new JButton("√");
        sinButton = new JButton("sin");
        cosButton = new JButton("cos");
        tanButton = new JButton("tan");
        
        functionButtons[0] = equalsButton;
        functionButtons[1] = clearButton;
        functionButtons[2] = dotButton;
        functionButtons[3] = backButton;
        functionButtons[4] = sqrtButton;
        functionButtons[5] = sinButton;
        
        for (int i = 0; i < 6; i++) {
            functionButtons[i].addActionListener(new FunctionButtonClickListener());
            buttonPanel.add(functionButtons[i]);
        }
        
        buttonPanel.add(cosButton);
        buttonPanel.add(tanButton);
        
        add(buttonPanel, BorderLayout.CENTER);
    }
    
    private class NumberButtonClickListener implements ActionListener {
        @Override
        public void actionPerformed(ActionEvent e) {
            JButton clickedButton = (JButton) e.getSource();
            input += clickedButton.getText();
            displayField.setText(input);
        }
    }
    
    private class FunctionButtonClickListener implements ActionListener {
        @Override
        public void actionPerformed(ActionEvent e) {
            JButton clickedButton = (JButton) e.getSource();
            String buttonText = clickedButton.getText();
            
            if (buttonText.equals("=")) {
                double result = evaluateExpression(input);
                displayField.setText(Double.toString(result));
                input = "";
            } else if (buttonText.equals("C")) {
                input = "";
                displayField.setText("");
            } else if (buttonText.equals("←")) {
                if (!input.isEmpty()) {
                    input = input.substring(0, input.length() - 1);
                    displayField.setText(input);
                }
            } else if (buttonText.equals("√")) {
                double result = Math.sqrt(Double.parseDouble(input));
                displayField.setText(Double.toString(result));
                input = "";
            } else if (buttonText.equals("sin")) {
                double result = Math.sin(Math.toRadians(Double.parseDouble(input)));
                displayField.setText(Double.toString(result));
                input = "";
            } else if (buttonText.equals("cos")) {
                double result = Math.cos(Math.toRadians(Double.parseDouble(input)));
                displayField.setText(Double.toString(result));
                input = "";
            } else if (buttonText.equals("tan")) {
                double result = Math.tan(Math.toRadians(Double.parseDouble(input)));
                displayField.setText(Double.toString(result));
                input = "";
            }
        }
    }
    
    private double evaluateExpression(String expression) {
        try {
            return new ExpressionEvaluator().evaluate(expression);
        } catch (Exception e) {
            JOptionPane.showMessageDialog(null, "Invalid expression", "Error", JOptionPane.ERROR_MESSAGE);
            return 0;
        }
    }
    
    public static void main(String[] args) {
        SwingUtilities.invokeLater(new Runnable() {
            public void run() {
                ScientificCalculator calculator = new ScientificCalculator();
                calculator.setVisible(true);
            }
        });
    }
    
    private class ExpressionEvaluator {
        private double evaluate(String expression) throws Exception {
            return new Object() {
                int pos = -1, ch;
    
                void nextChar() {
                    ch = (++pos < expression.length()) ? expression.charAt(pos) : -1;
                }
    
                boolean eat(int charToEat) {
                    while (ch == ' ') nextChar();
                    if (ch == charToEat) {
                        nextChar();
                        return true;
                    }
                    return false;
                }
    
                double parse() throws Exception {
                    nextChar();
                    double x = parseExpression();
                    if (pos < expression.length()) throw new Exception("Unexpected: " + (char)ch);
                    return x;
                }
    
                double parseExpression() throws Exception {
                    double x = parseTerm();
                    for (;;) {
                        if      (eat('+')) x += parseTerm();
                        else if (eat('-')) x -= parseTerm();
                        else return x;
                    }
                }
    
                double parseTerm() throws Exception {
                    double x = parseFactor();
                    for (;;) {
                        if      (eat('*')) x *= parseFactor();
                        else if (eat('/')) x /= parseFactor();
                        else return x;
                    }
                }
    
                double parseFactor() throws Exception {
                    if (eat('+')) return parseFactor();
                    if (eat('-')) return -parseFactor();
    
                    double x;
                    int startPos = this.pos;
                    if (eat('(')) {
                        x = parseExpression();
                        eat(')');
                    } else if ((ch >= '0' && ch <= '9') || ch == '.') {
                        while ((ch >= '0' && ch <= '9') || ch == '.') nextChar();
                        x = Double.parseDouble(expression.substring(startPos, this.pos));
                    } else {
                        throw new Exception("Unexpected: " + (char)ch);
                    }
    
                    if (eat('^')) x = Math.pow(x, parseFactor());
    
                    return x;
                }
            }.parse();
        }
    }
}
