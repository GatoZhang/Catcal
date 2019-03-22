package main;
import java.awt.*;

import javax.imageio.ImageIO;
import javax.swing.*;
import java.awt.event.ActionListener;
import java.util.ArrayList;
import java.awt.event.ActionEvent;
import java.lang.Math;
import java.io.IOException;

public class Main {
	static JFrame fra = new JFrame();
	static boolean newCount = true;
	private static JTextField textField;
	public static final Double E = 2.7182818284590452354;
	public static final Double PI = 3.14159265358979323846;
	
	static ArrayList<String> toList(String rawString){
		String num = new String("");
		String operator = new String("");
		ArrayList<String> rawList = new ArrayList<String>();
		String label = "";
		
		for (int i = 0; i < rawString.length(); i++){
			char next = rawString.charAt(i);
			if ( next == '+' || next == '-' || next == '*' || next == '/' || next == '^' || next == '(' || next == ')' ){
				if ( !num.equals("") ){
					if ( num.length() - num.replace(".", "").length() > 1 || num.length() > 1 && num.charAt(0) == '0' && num.charAt(1) != '.' ) {
						label = "Error";
						break;
					}
					rawList.add(num);
					num = "";
				}
				operator = operator + rawString.charAt(i);
				rawList.add(operator);
				operator = "";
			} else if ( next == 's' || next == 't' || next == 'c' ){
				operator = operator + rawString.charAt(i) + rawString.charAt(i+1) + rawString.charAt(i+2);
				rawList.add(operator);
				operator = "";
				i += 2;
			} else if ( next == 'e' ){
				rawList.add("e");
			} else if ( next == '\u03C0' ){
				rawList.add("pi");
			} else {
				num = num + next;
			}
		}
		if ( !num.equals("") ){
			if ( num.length() - num.replace(".", "").length() > 1 || num.length() > 1 && num.charAt(0) == '0' && num.charAt(1) != '.' ) {
				label = "Error";
			} else {
				rawList.add(num);
				num = "";
			}
		}
		
		if ( label.equals("Error") ) {
			rawList.clear();
			return rawList;
		} else {
			return rawList;
		}
		
	}
	
	static ArrayList<String> toPost(ArrayList<String> rawList) {
		ArrayList<String> postList = new ArrayList<String>();
		
		Stack postStack = new Stack();
		//if ( postList.size() > 0 ) System.out.println("Not empty before toPost");
		//if ( !postStack.top().equals("Error") ) System.out.println("Not empty before toPost");
		for (int i = 0; i < rawList.size(); i++ ) {
			String item = rawList.get(i);
			if ( item.equals("(") ) {
				postStack.input(item);
				if ( rawList.get(i+1).equals("-") ) {
					postStack.input("m");
					i++;
				}
			} else if ( item.equals(")") ) {
				while ( !postStack.top().equals("Error") ) {
					if ( postStack.top().equals("(") ) {
						postStack.output();
						break;
					} else {
						postList.add(postStack.output());
					}
				}
			} else if ( item.equals("+") || item.equals("-") ) {
				while ( !postStack.top().equals("Error") && !postStack.top().equals("(")) {
					postList.add(postStack.output());
				}
				postStack.input(item);
			} else if ( item.equals("*") || item.equals("/") ) {
				while ( !postStack.top().equals("+") && !postStack.top().equals("-") && !postStack.top().equals("Error") && !postStack.top().equals("(") ) {
					postList.add(postStack.output());
				}
				postStack.input(item);
			} else if ( item.equals("^") || item.equals("sin") || item.equals("cos") || item.equals("tan") ) {
				postStack.input(item);
			} else {
				postList.add(item);
			}
		}
		while ( !postStack.top().equals("Error") ) {
			postList.add(postStack.output());
		}
		return postList;
	}
	
	static String toCal(ArrayList<String> postList) throws IOException {
		String answer = "";
		Stack numList = new Stack();
		
		for (int i = 0; i < postList.size(); i++ ) {
			String item = postList.get(i);
			if ( item.equals("+") ) {
				double a = Double.valueOf(numList.output());
				if ( numList.top().equals("Error") ) {
					numList.input(Double.toString(a));
				} else {
					double b = Double.valueOf(numList.output());
					numList.input(Double.toString(a + b));
				}
			} else if ( item.equals("-") ) {
				double a = Double.valueOf(numList.output());
				if ( numList.top().equals("Error") ) {
					numList.input(Double.toString(-a));
				} else {
					double b = Double.valueOf(numList.output());
					numList.input(Double.toString(b - a));
				}
			} else if ( item.equals("m") ) {
				double a = Double.valueOf(numList.output());
				numList.input(Double.toString(-a));
			} else if ( item.equals("*") ) {
				double a = Double.valueOf(numList.output());
				double b = Double.valueOf(numList.output());
				numList.input(Double.toString(a * b));
			} else if ( item.equals("/") ) {
				double a = Double.valueOf(numList.output());
				double b = Double.valueOf(numList.output());
				if ( a == 0 ) {
					answer = "Error";
					break;
				} 
				numList.input(Double.toString(b / a));
			} else if ( item.equals("^") ) {
				double a = Double.valueOf(numList.output());
				double b = Double.valueOf(numList.output());
				numList.input(Double.toString(Math.pow(b,a)));
			} else if ( item.equals("sin") ) {
				double a = Double.valueOf(numList.output());
				numList.input(Double.toString(Math.sin(a)));
			} else if ( item.equals("cos") ) {
				double a = Double.valueOf(numList.output());
				numList.input(Double.toString(Math.cos(a)));
			} else if ( item.equals("tan") ) {
				double a = Double.valueOf(numList.output());
				numList.input(Double.toString(Math.tan(a)));
			} else if ( item.equals("e") ){
				numList.input(Double.toString(E));
			} else if ( item.equals("pi") ) {
				numList.input(Double.toString(PI));
			} else {
				numList.input(item);
			}
		}
		
		if ( !answer.equals("Error") ){
			answer = numList.output();
		}
		
		return answer;
	}
	
	public static void main (String args[]) throws ClassNotFoundException, InstantiationException, IllegalAccessException, UnsupportedLookAndFeelException {
		fra.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		fra.setIconImage(Toolkit.getDefaultToolkit().getImage(Main.class.getResource("/main/icon.png")));
		fra.setFont(new Font("Arial", Font.PLAIN, 12));
		fra.setTitle("CatCal");
		fra.setSize(378,466);
		fra.setResizable(false);
		fra.getContentPane().setLayout(null);
		
		UIManager.setLookAndFeel("com.sun.java.swing.plaf.nimbus.NimbusLookAndFeel");
		
		Toolkit kit = Toolkit.getDefaultToolkit();
		Dimension screenSize = kit.getScreenSize();
		int screenWidth = screenSize.width / 2;
		int screenHeight = screenSize.height / 2;
		int height = fra.getHeight() / 2;
		int width = fra.getWidth() / 2;
		fra.setLocation(screenWidth - width, screenHeight - height);
		
		JButton btnNewButton = new JButton("1");
		btnNewButton.setFont(new Font("Arial", Font.PLAIN, 15));
		btnNewButton.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent arg0) {
				char last = textField.getText().charAt(textField.getText().length() - 1);
				if (textField.getText().equals("0") || newCount ) {
					textField.setText("1");
					newCount = false;
				} else if (last != ')' && last != 'e' && last != '\u03C0'){
					textField.setText(textField.getText() + "1");
				}
				
			}
		});
		btnNewButton.setBounds(27, 228, 93, 39);
		fra.getContentPane().add(btnNewButton);
		
		JButton btnNewButton_1 = new JButton("4");
		btnNewButton_1.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				char last = textField.getText().charAt(textField.getText().length() - 1);
				if (textField.getText().equals("0") || newCount ) {
					textField.setText("4");
					newCount = false;
				} else if (last != ')' && last != 'e' && last != '\u03C0'){
					textField.setText(textField.getText() + "4");
				}
				
			}
		});
		btnNewButton_1.setFont(new Font("Arial", Font.PLAIN, 15));
		btnNewButton_1.setBounds(27, 277, 93, 39);
		fra.getContentPane().add(btnNewButton_1);
		
		JButton btnNewButton_2 = new JButton("7");
		btnNewButton_2.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				char last = textField.getText().charAt(textField.getText().length() - 1);
				if (textField.getText().equals("0") || newCount ) {
					textField.setText("7");
					newCount = false;
				} else if (last != ')' && last != 'e' && last != '\u03C0') {
					textField.setText(textField.getText() + "7");
				}
				
			}
		});
		btnNewButton_2.setFont(new Font("Arial", Font.PLAIN, 15));
		btnNewButton_2.setBounds(27, 328, 93, 39);
		fra.getContentPane().add(btnNewButton_2);
		
		JButton btnNewButton_3 = new JButton("2");
		btnNewButton_3.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				char last = textField.getText().charAt(textField.getText().length() - 1);
				if (textField.getText().equals("0") || newCount ) {
					textField.setText("2");
					newCount = false;
				} else if (last != ')' && last != 'e' && last != '\u03C0') {
					textField.setText(textField.getText() + "2");
				}
				
			}
		});
		btnNewButton_3.setFont(new Font("Arial", Font.PLAIN, 15));
		btnNewButton_3.setBounds(140, 228, 93, 39);
		fra.getContentPane().add(btnNewButton_3);
		
		JButton btnNewButton_4 = new JButton("5");
		btnNewButton_4.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				char last = textField.getText().charAt(textField.getText().length() - 1);
				if (textField.getText().equals("0") || newCount ) {
					textField.setText("5");
					newCount = false;
				} else if (last != ')' && last != 'e' && last != '\u03C0'){
					textField.setText(textField.getText() + "5");
				}
				
			}
		});
		btnNewButton_4.setFont(new Font("Arial", Font.PLAIN, 15));
		btnNewButton_4.setBounds(140, 277, 93, 39);
		fra.getContentPane().add(btnNewButton_4);
		
		JButton button = new JButton("8");
		button.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				char last = textField.getText().charAt(textField.getText().length() - 1);
				if (textField.getText().equals("0") || newCount ) {
					textField.setText("8");
					newCount = false;
				} else if (last != ')' && last != 'e' && last != '\u03C0') {
					textField.setText(textField.getText() + "8");
				}
				
			}
		});
		button.setFont(new Font("Arial", Font.PLAIN, 15));
		button.setBounds(140, 328, 93, 39);
		fra.getContentPane().add(button);
		
		JButton button_1 = new JButton("3");
		button_1.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				char last = textField.getText().charAt(textField.getText().length() - 1);
				if (textField.getText().equals("0") || newCount ) {
					textField.setText("3");
					newCount = false;
				} else if (last != ')' && last != 'e' && last != '\u03C0'){
					textField.setText(textField.getText() + "3");
				}
			}
		});
		button_1.setFont(new Font("Arial", Font.PLAIN, 15));
		button_1.setBounds(254, 228, 93, 39);
		fra.getContentPane().add(button_1);
		
		JButton button_2 = new JButton("6");
		button_2.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				char last = textField.getText().charAt(textField.getText().length() - 1);
				if (textField.getText().equals("0") || newCount ) {
					textField.setText("6");
					newCount = false;
				} else if (last != ')' && last != 'e' && last != '\u03C0') {
					textField.setText(textField.getText() + "6");
				}
				
			}
		});
		button_2.setFont(new Font("Arial", Font.PLAIN, 15));
		button_2.setBounds(254, 277, 93, 39);
		fra.getContentPane().add(button_2);
		
		JButton button_3 = new JButton("9");
		button_3.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				char last = textField.getText().charAt(textField.getText().length() - 1);
				if (textField.getText().equals("0") || newCount ) {
					textField.setText("9");
					newCount = false;
				} else if (last != ')' && last != 'e' && last != '\u03C0') {
					textField.setText(textField.getText() + "9");
				}
				
			}
		});
		button_3.setFont(new Font("Arial", Font.PLAIN, 15));
		button_3.setBounds(254, 328, 93, 39);
		fra.getContentPane().add(button_3);
		
		textField = new JTextField();
		textField.setToolTipText("haha");
		textField.setBackground(Color.WHITE);
		textField.setEditable(false);
		textField.setText("0");
		textField.setHorizontalAlignment(SwingConstants.RIGHT);
		textField.setFont(new Font("Arial", Font.PLAIN, 24));
		textField.setBounds(10, 10, 353, 49);
		fra.getContentPane().add(textField);
		textField.setColumns(10);
		
		JButton btnNewButton_5 = new JButton("+");
		btnNewButton_5.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent arg0) {
				char last = textField.getText().charAt(textField.getText().length() - 1);
				if ( textField.getText().equals("0") ) {
					textField.setText("+");
					newCount = false;
				}
				else if ( last != '.' && last != '+' && last != '-' && last != '*' && last != '/' && last != '(' && last != '^' ) {
					textField.setText(textField.getText() + "+");
					newCount = false;
				}
			}
		});
		btnNewButton_5.setFont(new Font("Arial", Font.PLAIN, 15));
		btnNewButton_5.setBounds(10, 167, 60, 39);
		fra.getContentPane().add(btnNewButton_5);
		
		JButton button_4 = new JButton("-");
		button_4.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				char last = textField.getText().charAt(textField.getText().length() - 1);
				if ( textField.getText().equals("0") ) {
					textField.setText("-");
					newCount = false;
				}
				else if ( last != '.' && last != '+' && last != '-' && last != '*' && last != '/' && last != '^' ) {
					textField.setText(textField.getText() + "-");
					newCount = false;
				}
			}
		});
		button_4.setFont(new Font("Arial", Font.PLAIN, 15));
		button_4.setBounds(75, 167, 60, 39);
		fra.getContentPane().add(button_4);
		
		JButton button_5 = new JButton("*");
		button_5.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				char last = textField.getText().charAt(textField.getText().length() - 1);
				if ( last != '.' && last != '+' && last != '-' && last != '*' && last != '/' && last != '(' && last != '^' ) {
					textField.setText(textField.getText() + "*");
					newCount = false;
				}
			}
		});
		button_5.setFont(new Font("Arial", Font.PLAIN, 15));
		button_5.setBounds(140, 167, 60, 39);
		fra.getContentPane().add(button_5);
		
		JButton button_6 = new JButton("/");
		button_6.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				char last = textField.getText().charAt(textField.getText().length() - 1);
				if ( last != '.' && last != '+' && last != '-' && last != '*' && last != '/' && last != '(' && last != '^' ) {
					textField.setText(textField.getText() + "/");
					newCount = false;
				}
			}
		});
		button_6.setFont(new Font("Arial", Font.PLAIN, 15));
		button_6.setBounds(205, 167, 60, 39);
		fra.getContentPane().add(button_6);
		
		JButton btnNewButton_6 = new JButton("0");
		btnNewButton_6.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				char last = textField.getText().charAt(textField.getText().length() - 1);
				if (textField.getText().equals("0") || newCount ) {
					textField.setText("0");
					newCount = false;
				} else if ( last != ')' && last != 'e' && last != '\u03C0' ) {
					textField.setText(textField.getText() + "0");
				}
				
			}
		});
		btnNewButton_6.setFont(new Font("Arial", Font.PLAIN, 15));
		btnNewButton_6.setBounds(140, 377, 93, 39);
		fra.getContentPane().add(btnNewButton_6);
		
		JButton btnGo = new JButton("=");
		btnGo.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent arg0) {
				String rawString = new String();
				rawString = textField.getText();
				String answer;
				ArrayList rawList = new ArrayList();
				ArrayList postList = new ArrayList();
				
				int left = 0, right = 0;
				left = rawString.length() - rawString.replace("(", "").length();
				right = rawString.length() - rawString.replace(")", "").length();
				for (int i = 0; i < rawString.length(); i++) {
					if ( rawString.charAt(i) == '(' ) left++;
					else if ( rawString.charAt(i) == ')' ) right++;
					if ( right > left ) break;
				}
				if ( left != right ) {
					JOptionPane.showMessageDialog(null, "Error on brackets!", "Error", JOptionPane.ERROR_MESSAGE); 
					textField.setText("0");
					newCount = true;
				} else if ( rawString.charAt(rawString.length() - 1) == '.' || rawString.charAt(rawString.length() - 1) == '+' || rawString.charAt(rawString.length() - 1) == '-' || rawString.charAt(rawString.length() - 1) == '*' || rawString.charAt(rawString.length() - 1) == '/' || rawString.charAt(rawString.length() - 1) == '^' ) {
					JOptionPane.showMessageDialog(null, "Error form!", "Error", JOptionPane.ERROR_MESSAGE); 
					textField.setText("0");
					newCount = true;
				} else {
					rawList = toList(rawString);
					if (rawList.size() == 0){
						JOptionPane.showMessageDialog(null, "Error for dot!", "Error", JOptionPane.ERROR_MESSAGE); 
						textField.setText("0");
					} else {
						postList = toPost(rawList);
						try {
							answer = toCal(postList);
							if ( answer.equals("Error") ){
								JOptionPane.showMessageDialog(null, "Denominator is 0!", "Error", JOptionPane.ERROR_MESSAGE); 
								textField.setText("0");
								newCount = true;
							} else if ( answer.equals("NaN") ){
								JOptionPane.showMessageDialog(null, "Only real number...", "Error", JOptionPane.ERROR_MESSAGE); 
								textField.setText("0");
								newCount = true;
							}
							else {
								textField.setText(answer);
								newCount = true;
							}
							
						} catch (Exception e) {
							System.out.println(e);
						}
					}
				}
				
			}
		});
		btnGo.setFont(new Font("Arial", Font.PLAIN, 15));
		btnGo.setBounds(254, 377, 93, 39);
		fra.getContentPane().add(btnGo);
		
		JButton button_7 = new JButton(".");
		button_7.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				char last = textField.getText().charAt(textField.getText().length() - 1);
				if( last == '.' || last == '+' || last == '-' || last == '*' || last == '/' || last == '\u03C0' || last == 'e' || last == '^' || last == '(' || last == ')' ) {
				} else {
					textField.setText(textField.getText() + ".");
				}
				
			}
		});
		button_7.setFont(new Font("Arial", Font.BOLD, 15));
		button_7.setBounds(27, 377, 93, 39);
		fra.getContentPane().add(button_7);
		
		JButton button_8 = new JButton("(");
		button_8.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				char last = textField.getText().charAt(textField.getText().length() - 1);
				int i;
				for (i = 0; i < 10; i++){
					if ( last - '0' == i ) {
						break;
					}
				}
				if ( i >= 10 && last != '.' && last != ')' ) {
					textField.setText(textField.getText() + "(");
				} else if ( textField.getText().equals("0") || newCount ) {
					textField.setText("(");
					newCount = false;
				}
			}
		});
		button_8.setFont(new Font("Arial", Font.PLAIN, 15));
		button_8.setBounds(140, 118, 60, 39);
		fra.getContentPane().add(button_8);
		
		JButton button_9 = new JButton(")");
		button_9.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				char last = textField.getText().charAt(textField.getText().length() - 1);
				int numBrac = textField.getText().indexOf('(');
				if ( last != '.' && last != '+' && last != '-' && last != '*' && last != '/' && last != '(' && last != '^' && numBrac != -1) {
					textField.setText(textField.getText() + ")");
				}
			}
		});
		button_9.setFont(new Font("Arial", Font.PLAIN, 15));
		button_9.setBounds(205, 118, 60, 39);
		fra.getContentPane().add(button_9);
		
		JButton button_10 = new JButton("^");
		button_10.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				char last = textField.getText().charAt(textField.getText().length() - 1);
				if ( last != '.' && last != '+' && last != '-' && last != '*' && last != '/' && last != '(' && last != '^' ) {
					textField.setText(textField.getText() + "^");
					newCount = false;
				}
			}
		});
		button_10.setFont(new Font("Arial", Font.PLAIN, 15));
		button_10.setBounds(10, 69, 60, 39);
		fra.getContentPane().add(button_10);
		
		JButton btnSin = new JButton("sin");
		btnSin.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				char last = textField.getText().charAt(textField.getText().length() - 1);
				int i;
				for (i = 0; i < 10; i++){
					if ( last - '0' == i ) {
						break;
					}
				}
				if ( i >= 10 && last != '.' && last != 'e' && last != ')' && last != '\u03C0' ) {
					textField.setText(textField.getText() + "sin(");
					newCount = false;
				} else if ( textField.getText().equals("0") ) {
					textField.setText("sin(");
					newCount = false;
				} else if ( newCount ) {
					textField.setText( "sin(" + textField.getText() );
					newCount = false;
				}
			}
		});
		btnSin.setFont(new Font("Arial", Font.PLAIN, 15));
		btnSin.setBounds(75, 69, 60, 39);
		fra.getContentPane().add(btnSin);
		
		JButton btnCos = new JButton("cos");
		btnCos.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				char last = textField.getText().charAt(textField.getText().length() - 1);
				int i;
				for (i = 0; i < 10; i++){
					if ( last - '0' == i ) {
						break;
					}
				}
				if ( i >= 10 && last != '.' && last != 'e' && last != ')' && last != '\u03C0' ) {
					textField.setText(textField.getText() + "cos(");
					newCount = false;
				} else if ( textField.getText().equals("0") ) {
					textField.setText("cos(");
					newCount = false;
				} else if ( newCount ) {
					textField.setText( "cos(" + textField.getText() );
					newCount = false;
				}
			}
		});
		btnCos.setFont(new Font("Arial", Font.PLAIN, 15));
		btnCos.setBounds(140, 69, 60, 39);
		fra.getContentPane().add(btnCos);
		
		JButton btnTan = new JButton("tan");
		btnTan.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				char last = textField.getText().charAt(textField.getText().length() - 1);
				int i;
				for (i = 0; i < 10; i++){
					if ( last - '0' == i ) {
						break;
					}
				}
				if ( i >= 10 && last != '.' && last != 'e' && last != ')' && last != '\u03C0' ) {
					textField.setText(textField.getText() + "tan(");
					newCount = false;
				} else if ( textField.getText().equals("0") ) {
					textField.setText("tan(");
					newCount = false;
				} else if ( newCount ) {
					textField.setText( "tan(" + textField.getText() );
					newCount = false;
				}
			}
		});
		btnTan.setFont(new Font("Arial", Font.PLAIN, 15));
		btnTan.setBounds(205, 69, 60, 39);
		fra.getContentPane().add(btnTan);
		
		JButton btnE = new JButton("e");
		btnE.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				char last = textField.getText().charAt(textField.getText().length() - 1);
				int i;
				for (i = 0; i < 10; i++){
					if ( last - '0' == i ) {
						break;
					}
				}
				if ( textField.getText().equals("0") || newCount ) {
					textField.setText("e");
					newCount = false;
				}
				if ( i >= 10 && last != '.' && last != ')' && last != 'e' && last != '\u03C0' ) {
					textField.setText(textField.getText() + "e");
				}
			}
		});
		btnE.setFont(new Font("Arial", Font.PLAIN, 15));
		btnE.setBounds(10, 118, 60, 39);
		fra.getContentPane().add(btnE);
		
		JButton button_12 = new JButton("\u03C0");
		button_12.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				char last = textField.getText().charAt(textField.getText().length() - 1);
				int i;
				for (i = 0; i < 10; i++){
					if ( last - '0' == i ) {
						break;
					}
				}
				if ( textField.getText().equals("0") || newCount ) {
					textField.setText("\u03C0");
					newCount = false;
				}
				if ( i >= 10 && last != '.' && last != ')' && last != 'e' && last != '\u03C0' ) {
					textField.setText(textField.getText() + "\u03C0");
				}
			}
		});
		button_12.setFont(new Font("Arial", Font.PLAIN, 15));
		button_12.setBounds(75, 118, 60, 39);
		fra.getContentPane().add(button_12);
		
		JButton btnNewButton_7 = new JButton("clean");
		btnNewButton_7.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				textField.setText("0");
			}
		});
		btnNewButton_7.setFont(new Font("Arial", Font.PLAIN, 15));
		btnNewButton_7.setBounds(276, 69, 87, 86);
		fra.getContentPane().add(btnNewButton_7);
		
		JButton btnNewButton_8 = new JButton("Del");
		btnNewButton_8.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent arg0) {
				String text = textField.getText();
				if ( text.length() == 1 || text.equals("sin(") || text.equals("cos(") || text.equals("tan(") ) {
					textField.setText("0");
				} else if ( text.length() > 4 && (text.substring(text.length() - 4, text.length()).equals("sin(") || text.substring(text.length() - 4, text.length()).equals("cos(") || text.substring(text.length() - 4, text.length()).equals("tan(") ) ) {
					textField.setText(text.substring(0, text.length() - 4));
				} else {
					textField.setText(text.substring(0, text.length() - 1));
				}
			}
		});
		btnNewButton_8.setFont(new Font("Arial", Font.PLAIN, 15));
		btnNewButton_8.setBounds(275, 167, 88, 39);
		fra.getContentPane().add(btnNewButton_8);
		
		/*fra.setLocation(400, 150);
		String src = "/img/icon.png";
		
		try{
			Image image = ImageIO.read(fra.getClass().getResource(src));
			fra.setIconImage(image);
		} catch (Exception e) {
			System.out.println(e);
		}*/
		 

		
		fra.setVisible(true);
	}
}
