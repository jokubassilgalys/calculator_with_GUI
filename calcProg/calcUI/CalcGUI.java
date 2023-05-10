package calcUI;
import java.awt.event.*;
import java.math.BigDecimal;
import java.math.BigInteger;

import javax.swing.*;

import calcException.ExceptionDivideByZero;
import calcModes.BaseCalculator;

import java.awt.*;

public class CalcGUI extends JFrame implements ActionListener{
    JButton numButton[] = new JButton[10];
    JButton addButton, subButton, mulButton, divButton, powButton,
            clearButton, modButton, logButton, lgButton, sinButton,
            cosButton, tanButton, equalsButton, decButton, delButton;
    JTextArea calcTextArea = new JTextArea();
    String arg1 = "0", arg2 = "", operator = "", answer = "";

    public CalcGUI(){
        JFrame calcFrame = new JFrame("Calculator");
        Container calcPane = calcFrame.getContentPane();
        GridBagConstraints grid = new GridBagConstraints();
        calcPane.setBackground(new Color(200, 200, 200));

        calcFrame.setLocationRelativeTo(null);
        calcFrame.setResizable(false);
        //calcFrame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        calcPane.setLayout(new GridBagLayout());
        calcTextArea.setText("0");
        calcTextArea.setEditable(false);
        calcTextArea.setLineWrap(true);
        calcTextArea.setFont(new Font("Serif Plain",Font.BOLD,20));

        grid.fill = GridBagConstraints.HORIZONTAL;
        //grid.ipadx = grid.ipady = 30;
        grid.ipady = 30;
        grid.insets = new Insets(2,2,2,2);

        powButton = new JButton("^");
        grid.gridx = 0;
        grid.gridy = 1;
        calcPane.add(powButton, grid);
        modButton = new JButton("mod");
        grid.gridx = 0;
        grid.gridy = 2;
        calcPane.add(modButton, grid);
        logButton = new JButton("log");
        grid.gridx = 0;
        grid.gridy = 3;
        calcPane.add(logButton, grid);
        lgButton = new JButton("lg");
        grid.gridx = 0;
        grid.gridy = 4;
        calcPane.add(lgButton, grid);
        clearButton = new JButton("C");
        grid.gridx = 0;
        grid.gridy = 5;
        grid.gridwidth = 2;
        calcPane.add(clearButton, grid);

        grid.gridwidth = 1;
        sinButton = new JButton("sin(x)");
        grid.gridx = 1;
        grid.gridy = 1;
        calcPane.add(sinButton, grid);
        cosButton = new JButton("cos(x)");
        grid.gridx = 2;
        grid.gridy = 1;
        calcPane.add(cosButton, grid);
        tanButton = new JButton("tan(x)");
        grid.gridx = 3;
        grid.gridy = 1;
        calcPane.add(tanButton, grid);

        int x = 1, y = 2;
        for(int i = 1; i <= 9; ++i){
            numButton[i] = new JButton(i + "");
            grid.gridx = x;
            grid.gridy = y;
            numButton[i].addActionListener(this);

            if(x == 3){ x = 1; y++; }
            else { x++; }

            calcPane.add(numButton[i], grid);
        }
        numButton[0] = new JButton("0");
        grid.gridx = 2;
        grid.gridy = 5;
        numButton[0].addActionListener(this);
        calcPane.add(numButton[0], grid);

        addButton = new JButton("+");
        grid.gridx = 4;
        grid.gridy = 1;
        addButton.addActionListener(this);
        calcPane.add(addButton, grid);

        subButton = new JButton("-");
        grid.gridx = 4;
        grid.gridy = 2;
        subButton.addActionListener(this);
        calcPane.add(subButton, grid);

        mulButton = new JButton("*");
        grid.gridx = 4;
        grid.gridy = 3;
        mulButton.addActionListener(this);
        calcPane.add(mulButton, grid);

        divButton = new JButton("/");
        grid.gridx = 4;
        grid.gridy = 4;
        divButton.addActionListener(this);
        calcPane.add(divButton, grid);
        
        // delButton = new JButton("del");
        // grid.gridx = 1;
        // grid.gridy = 5;
        // calcPane.add(delButton, grid);
        decButton = new JButton(".");
        grid.gridx = 3;
        grid.gridy = 5;
        calcPane.add(decButton, grid);

        equalsButton = new JButton("=");
        grid.gridx = 4;
        grid.gridy = 5;
        equalsButton.addActionListener(this);
        calcPane.add(equalsButton, grid);

        grid.gridx = 0;
        grid.gridy = 0;
        grid.gridwidth = 5;
        calcPane.add(calcTextArea, grid);

        calcFrame.pack();
        calcFrame.setSize(350,400);
        calcFrame.setVisible(true);
    }


    public void actionPerformed(ActionEvent e){
        JButton callerButton = (JButton)e.getSource();
        String currentText = calcTextArea.getText();
        if(!(answer.isEmpty())){
            currentText = arg1 = answer;
            answer = arg2 = operator = "";
        }

        // Boolean lastButtonWasOperator = false;

        // if(!(currentText.substring(currentText.length() - 1).equals(" "))){
        //     lastButtonWasOperator = true;
        // }
        // else { 
        //     lastButtonWasOperator = false;
        // }

        if(callerButton == addButton){
            currentText += " + ";
            operator = "+";
        }
        else if(callerButton == subButton){
            currentText += " - ";
            operator = "-";
        }
        else if(callerButton == mulButton){
            currentText += " * ";
            operator = "*";
        }
        else if(callerButton == divButton){
            currentText += " / ";
            operator = "/";
        }
        else if(callerButton == equalsButton){
            answer = "" + evaluate();
            currentText += "\n= " + answer;
        }
        else {
            for(int i = 0; i <= 9; ++i){
                if(callerButton == numButton[i]){
                    if(!(currentText.substring(currentText.length() - 1).equals("0"))){
                        currentText += i;
                    }          
                    else{
                        if(currentText.length() > 1){
                            if((currentText.substring(currentText.length() - 2, currentText.length() - 1)).equals(" ")){
                                currentText = currentText.substring(0, currentText.length() - 1) + i;
                            }
                            else{
                                currentText += i;
                            }
                        }
                        else{
                            currentText = currentText.substring(0, currentText.length() - 1) + i;
                        }
                    }

                    if(operator.isEmpty()){ arg1 += i; }
                    else { arg2 += i; }
                }
            }
        }        
        calcTextArea.setText(currentText);
        
        //String s = e.getActionCommand();
    } 

    public BigDecimal evaluate(){
        BaseCalculator inputNumbers;
        inputNumbers = new BaseCalculator(new BigInteger(arg1), new BigInteger(arg2));
        
        switch(operator){
            case "+":
                inputNumbers.addition();
                break;
            case "-":
                inputNumbers.subtraction();
                break;
            case "*":
                inputNumbers.multiplication();
                break;
            case "/":
                try {
                    inputNumbers.division();
                }
                catch(ExceptionDivideByZero e){
                    e.printStackTrace();
                }
                break;
            // case "^":
            //     ((SimpleCalculator)inputNumbers).power();
            //     break;
            // case "%":
            //     try {
            //         ((SimpleCalculator)inputNumbers).percent();
            //     }
            //     catch(ExceptionDivideByZero e){
            //         e.printStackTrace();
            //     }
            //     break;
            // case "mod":
            //     try {
            //         ((SimpleCalculator)inputNumbers).mod();
            //     }
            //     catch(ExceptionDivideByZero e){
            //         e.printStackTrace();
            //     }
            //     break;
            // case "log":
            //     ((ScientificCalculator)inputNumbers).log();
            //     break;
            // case "lg":
            //     ((ScientificCalculator)inputNumbers).lg();
            //     break;
            // case "sin":
            //     ((ScientificCalculator)inputNumbers).sin();
            //     break;
            // case "cos":
            //     ((ScientificCalculator)inputNumbers).cos();
            //     break;
            // case "tan":
            //     ((ScientificCalculator)inputNumbers).tan();
            //     break;
            default:
               System.out.println("Invalid operator");
        }
        return inputNumbers.getAnswer();
    }
}
