package com.qinggniq.cal.view;
import com.qinggniq.cal.process.*;
import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.ComponentOrientation;
import java.awt.Dimension;
import java.awt.FlowLayout;
import java.awt.Font;
import java.awt.GraphicsConfiguration;
import java.awt.HeadlessException;
import java.awt.TextField;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.BorderFactory;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JTextArea;
import javax.swing.JTextField;

public class CalView extends JFrame {
	JTextArea jta1 = new JTextArea(),
			jta2 = new JTextArea();
	
	Expression exp = null;
	public CalView() throws HeadlessException {
		// TODO Auto-generated constructor stub
		this.setTitle("计算器");
		this.setResizable(false); //不改变大小
		this.setLocationRelativeTo(null);
		this.setSize(228, 360);
		this.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		
		Font font_lable = new Font("楷体",Font.PLAIN,12);
		this.setLayout(new FlowLayout(1,0,0));
		jta1.setPreferredSize(new Dimension(220,60));
		jta2.setPreferredSize(new Dimension(220,60));
		jta1.setBackground(Color.yellow);
		jta2.setBackground(Color.green);
		jta1.setBounds(14, 15, 190, 50);
		jta2.setBounds(14, 15, 190, 50);
		
		//jl1.setLayout(new BorderLayout());
		jta1.setBorder(BorderFactory.createLineBorder(new Color(142,156,173)));
		jta2.setBorder(BorderFactory.createLineBorder(new Color(142,156,173)));
		jta1.setFont(new Font("楷体",Font.PLAIN,20));
		jta2.setFont(new Font("楷体",Font.PLAIN,20));
		jta1.setAlignmentX(RIGHT_ALIGNMENT);
		jta1.setLineWrap(true);
		jta1.requestFocus();
		this.add(jta1);
		this.add(jta2);
		//改
		final String buttonText[] = { 
				"CA","CW", "/", "*", 
				"7", "8", "9", "-", 
				"4", "5", "6", "+",
				"1", "2", "3", "%"
				,"0","(", ")", "="
				}; 
		 JButton jb[] = new JButton[20];

		for (int i = 0; i < jb.length; i++) {
			jb[i] = new JButton(buttonText[i]);
			jb[i].setFont(font_lable);
			jb[i].setPreferredSize(new Dimension(55,40));
			jb[i].setBackground(Color.white);
			jb[i].addActionListener(new MyActionListener(jb[i], jta1, jta2,exp));
			this.add(jb[i]);
		}
		
		JButton jbToSuffix = new JButton("转后缀")
			,jbToPrefix = new JButton("转前缀"),
			jbSpace = new JButton("Space");  
		jbToPrefix.setPreferredSize(new Dimension(73, 40));  
		jbToPrefix.setBackground(Color.orange);  
		jbToSuffix.setPreferredSize(new Dimension(73, 40));  
		jbToSuffix.setBackground(Color.orange); 
		jbSpace.setPreferredSize(new Dimension(73, 40));  
		jbSpace.setBackground(Color.orange);
		jbToPrefix.addActionListener(new MyActionListener(jbToPrefix, jta1, jta2, exp));
		jbToSuffix.addActionListener(new MyActionListener(jbToSuffix, jta1, jta2, exp));
		jbSpace.addActionListener(new MyActionListener(jbSpace, jta1, jta2, exp));
		this.add(jbToPrefix);
		this.add(jbSpace);
		this.add(jbToSuffix);
	}
	
	
	public static void main(String[] args) throws ClassNotFoundException {
		new CalView().setVisible(true);
	}
}

class MyActionListener implements ActionListener{
	JButton jb;
	JTextArea jta1,jta2;
	Expression exp = null;
	public MyActionListener(JButton jb,JTextArea jta1,JTextArea jta2,Expression exp) {
		// TODO Auto-generated constructor stub
		this.jb = jb;
		this.jta1 = jta1;
		this.jta2 = jta2;
		this.exp = exp;
	}
	
	@Override
	public void actionPerformed(ActionEvent arg0) {
		// TODO Auto-generated method stub
		String buttonText = jb.getText();
		String curText = jta1.getText();
		int len = curText.length();
		if(buttonText.equals("CA")){
			jta1.setText("");
			jta2.setText("");
			exp = null;
		}else if(buttonText.equals("CW")){
			if(len<=1)
				jta1.setText("");
			else {
				jta1.replaceRange(null,len-1, len);
			}
		}else if(buttonText.equals("Space")){
			jta1.append(" ");
		}else if(!buttonText.equals("=") &&!buttonText.equals("转后缀") 
				&&!buttonText.equals("转前缀")){
			jta1.append(jb.getText());
		} else if(buttonText.equals("=") ){
			exp = new Expression(curText);
			jta2.setText("结果： " + exp.calNum());
		}else if (buttonText.equals("转后缀")){
			exp = new Expression(curText);
			jta2.setText("后缀：\n" + exp.toSuffix());
		}else if (buttonText.equals("转前缀")){
			exp = new Expression(curText);
			jta2.setText("前缀：\n" + exp.toPrefix());
		}
	}	
}
