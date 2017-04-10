package com.qinggniq.cal.process;

import java.awt.List;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;
import java.util.Stack;
import java.util.regex.Pattern;

public class Expression {

	/**
	 * @param args
	 */
	String expression;							//表达式
	public ArrayList<String> expList = new ArrayList<String>();
	enum ExpType {Prefix,Infix,Suffix};			//表达式类型
	ExpType expType = ExpType.Infix;			//默认中缀		
 	
	/*运算符优先级*/
	static Map<String, Integer> priority = new HashMap<>();  
	static {
		priority.put(")",-1);
		priority.put("(",-1);
		priority.put("+", 0);
		priority.put("-", 0);
		priority.put("*", 1);
		priority.put("/", 1);
		priority.put("%", 1);
	}
	static boolean isNum(String str) {
		Pattern pattern = Pattern.compile("^[-\\+]?[\\d]*$");    
		return pattern.matcher(str).matches(); 
	}
	public Expression(String ss) {
		this.expression = ss;
		expType = judgeType();
		expList = this.ToList();
	}
	private ExpType judgeType() {
		int len = expression.length();
		ExpType resultExpType = ExpType.Infix;
		boolean isInOrSuf = false;
		for(int i=0;i<len;i++) {
			char curChar = expression.charAt(i);
			if(Character.isWhitespace(curChar))
				continue;
			else if(Character.isDigit(curChar)){
				isInOrSuf = true;
				break;
			}else{
				resultExpType = ExpType.Prefix;
				break;
			}
		}
		if(isInOrSuf){
			for(int i=len-1;i>=0;i--) {
				char curChar = expression.charAt(i);
				if(Character.isWhitespace(curChar))
					continue;
				else if(!Character.isDigit(curChar) && curChar != ')'){
					resultExpType = ExpType.Suffix;
					break;
				}else{
					resultExpType = ExpType.Infix;
					break;
				}
			}
			return resultExpType;
		}else
			return resultExpType;
	}
	/*将运算数与运算符分离到一个链表*/
 	public ArrayList<String> ToList() {
 		ArrayList<String> result = new ArrayList<String>();
 		int len = expression.length();
 		int prePos = 0;String save = "";
 		for(int i=0;i < len;i++) {
 			char curChar = expression.charAt(i);
 			if( Character.isDigit(curChar)) {
 				if(i+1 == len)
 					result.add(save + curChar);
 				else if(!Character.isDigit(expression.charAt(i+1))) {
 					save += String.valueOf(curChar);
 					result.add(save);
 					save = "";
 				}else {
					save += String.valueOf(curChar);
				}
 			}else if( !Character.isWhitespace(curChar)){
 				if(expType == ExpType.Suffix || expType == ExpType.Prefix){
 					if(curChar == '-' && i!=len - 1&& !Character.isWhitespace(expression.charAt(i+1))){
 						save += "-";
						continue;
 					}
				}else if(expType == ExpType.Infix){
					if(curChar == '-' && i!=0 &&result.get(result.size()-1).equals("(")){
						save += "-";
						continue;
 	 				}
				}/*
				*******************************************************************
				*****************Prefix
				**/
 				
				result.add(String.valueOf(curChar));
				//prePos = i+1;
			}
 		}
 		return result;
 	}
	/*比价运算符优先级*/
	static boolean lowerPrior(String a,String b){
		return priority.get(a.trim()) < priority.get(b.trim());
	}
	
 	/*中缀转后缀*/
 	public String toSuffix() {
		if(this.expType == ExpType.Suffix)
			return this.expression;
		if(this.expType == ExpType.Prefix)
			return null;
		//this.expType = ExpType.Suffix;
		String suffixExp = "";
		
		int len = expList.size();
		Stack<String> operatorStack = new Stack<String>();
		
		for(int i=0; i<len; i++) {
			String curOp = expList.get(i);
			String topOp = null;
			if(curOp.equals("+")||curOp.equals("*")||
					curOp.equals("/")||curOp.equals("%")||
					curOp.equals("-")){
				if(operatorStack.empty())
					operatorStack.push(curOp);
				else {
						topOp = operatorStack.peek();
						if(Expression.lowerPrior(topOp,curOp)){
							operatorStack.push(curOp);
						} else {
							while ( !Expression.lowerPrior(topOp, curOp) ) {
							suffixExp += operatorStack.pop() + " ";
							if(!operatorStack.empty()){
								topOp = operatorStack.peek();
							}else
								break;
							}
							operatorStack.push(curOp);
					}
				}
			}else if(curOp.equals("(")){
 				operatorStack.push(curOp);
 			}else if(curOp.equals(")")){
 				while (!operatorStack.peek().equals("(")) {
					suffixExp += operatorStack.pop() + " ";
				}
 				operatorStack.pop();
 			}else {
 				suffixExp += curOp + " ";
			}
		}
		while (!operatorStack.empty()) {
			suffixExp += operatorStack.pop() + " ";
		}
		//this.expression = suffixExp;
		//this.expList = ToList();
		return suffixExp;
	}
 	/*中缀转前缀*/
 	public String toPrefix() {
		if(this.expType == ExpType.Prefix)
			return expression;
		else if(this.expType == ExpType.Suffix){
			return null;
		}
		//this.expType = ExpType.Prefix;
		
		String prefixExp = "";
		int len  = this.expList.size();
		Stack<String> operatorStack = new Stack<String>();
		Stack<String> saveStack = new Stack<String>();
		for(int i=len-1;i>=0;i-- ) {
			String curOp = expList.get(i);
			String topOp = null;
			if(curOp.equals("+")||curOp.equals("*")||
					curOp.equals("/")||curOp.equals("%")||
					curOp.equals("-")){
				if(operatorStack.empty())
					operatorStack.push(curOp);
				else {
						topOp = operatorStack.peek();
						if(!Expression.lowerPrior(curOp,topOp)){
							operatorStack.push(curOp);
						} else {
							while ( Expression.lowerPrior(curOp,topOp) ) {
								//prefixExp += operatorStack.pop() + " ";
								saveStack.push(operatorStack.pop());
								if(!operatorStack.empty()){
									topOp = operatorStack.peek();
								}else
									break;
							}
							operatorStack.push(curOp);
					}
				}
			}else if(curOp.equals(")")){
 				operatorStack.push(curOp);
 			}else if(curOp.equals("(")){
 				while (!operatorStack.peek().equals(")")) {
					//prefixExp += operatorStack.pop() + " ";
					saveStack.push(operatorStack.pop());
				}
 				operatorStack.pop();
 			}else {
 				saveStack.push(curOp);
 				//prefixExp += curOp + " ";
			}
		}
		while (!operatorStack.empty()) {
			//prefixExp += operatorStack.pop() + " ";
			saveStack.push(operatorStack.pop());
		}
		//反转String使之成为前缀表达式
		//StringBuffer sb = new StringBuffer(prefixExp);
		//sb.reverse();
		//prefixExp = sb.toString();
		//ArrayList<String> reverArrayList = new ArrayList<String>();
		while (!saveStack.empty()) {
			//reverArrayList.add( saveStack.peek() );
			prefixExp += ( saveStack.pop() + " ");
		}
		//this.expression = prefixExp;
		//this.expList = ToList();
		//********************代码*********************//
		return prefixExp;
 	}
 	
 	private void calculate(Stack<Long> opStack,String op) {
		if(op.equals("+")){
			long rightNum = opStack.pop();
			long leftNum = opStack.pop();
			opStack.push(rightNum+leftNum);
			System.out.println("操作符 ：" + op + " 操作数 ：" + leftNum + " "+rightNum);
		}else if(op.equals("-")){
			long rightNum = opStack.pop();
			long leftNum = opStack.pop();
			if(expType == ExpType.Infix || expType == ExpType.Suffix)
				opStack.push(leftNum-rightNum);
			else 
				opStack.push(rightNum-leftNum);;
			System.out.println("操作符 ：" + op + " 操作数 ：" + leftNum + " "+rightNum);
		}else if(op.equals("*")){
			long rightNum = opStack.pop();
			long leftNum = opStack.pop();
			opStack.push(rightNum*leftNum);
			System.out.println("操作符 ：" + op + " 操作数 ：" + leftNum + " "+rightNum);
		}else if(op.equals("/")){
			long rightNum = opStack.pop();
			long leftNum = opStack.pop();
			if(expType == ExpType.Infix || expType == ExpType.Suffix)
				opStack.push(leftNum/rightNum);
			else 
				opStack.push(rightNum/leftNum);
			System.out.println("操作符 ：" + op + " 操作数 ：" + leftNum + " "+rightNum);
		}else if(op.equals("%")){
			long rightNum = opStack.pop();
			long leftNum = opStack.pop();
			if(expType == ExpType.Infix || expType == ExpType.Suffix)
				opStack.push(leftNum%rightNum);
			else 
				opStack.push(rightNum%leftNum);
			System.out.println("操作符 ：" + op + " 操作数 ：" + leftNum + " "+rightNum);
		}
	}
 	/*各类表达式求值*/
 	public long calNum() {
 		long result = 0;
 		if(this.expType == ExpType.Prefix) {
 			result = this.calNumOfPrefix();
 		} else if (this.expType == ExpType.Infix) {
 			result = this.calNumOfInfix();
 		} else {
 			result = this.calNumOfSuffix();
		}
 		return result;
 	}
 	private long calNumOfSuffix() {
		// TODO Auto-generated method stub
		long result = 0;
		Stack<Long> opNumStack = new Stack<Long>();
		int len = expList.size();
		
		for(int i=0; i<len; i++) {
			String curOp = expList.get(i);
			if(curOp.equals("+")||curOp.equals("*")||
					curOp.equals("/")||curOp.equals("%")||
					curOp.equals("-")){
				calculate(opNumStack, curOp);
			}else
				opNumStack.push(Long.parseLong(curOp));
		}
		result = opNumStack.peek();
		return result;
	}
 	
	private long calNumOfPrefix() {
		long result = 0;
		Stack<Long> opNumStack = new Stack<Long>();
		int len = expList.size();
		
		for(int i=len-1; i>= 0; i--) {
			String curOp = expList.get(i);
			if(curOp.equals("+")||curOp.equals("*")||
					curOp.equals("/")||curOp.equals("%")||
					curOp.equals("-")){
				calculate(opNumStack, curOp);
			}else
				opNumStack.push(Long.parseLong(curOp));
		}
		result = opNumStack.peek();
 		return result;
 	}
 	private long calNumOfInfix() {
 		long result = 0;
 		int expLen = expList.size();
		Stack<Long> opNumStack = new Stack<Long>();
		Stack<String> opratorStack = new Stack<String>();
		//opratorStack.push('#');
		
		for(int i=0;i < expLen; i++) {
			String curOp = expList.get(i);
			if(curOp.equals("+")||curOp.equals("*")||
					curOp.equals("/")||curOp.equals("%")||
					curOp.equals("-")){
						if(opratorStack.empty())
							opratorStack.push(curOp);
						else {	
							String topOp = opratorStack.peek();
							//System.out.println(topOp);
							if(Expression.lowerPrior(topOp,curOp)){
								opratorStack.push(curOp);
						} else {
								while ( !Expression.lowerPrior(topOp, curOp) ) {
									calculate(opNumStack,topOp);
									opratorStack.pop();
									if(!opratorStack.empty()){
										topOp = opratorStack.peek();
									}else
								break;
								}
								opratorStack.push(curOp);
							}
						}
			}else if(curOp.equals("(")){
				opratorStack.push(curOp);
			}else if(curOp.equals(")")){
				while (!opratorStack.peek().equals("(")) {
					String topOp = opratorStack.pop();
					//System.out.println("---" + topOp + "---");
					calculate(opNumStack, topOp);
				}
				//System.out.println("---" + opratorStack.peek()+ "---");
				opratorStack.pop();
			}else {
				opNumStack.push(Long.parseLong(curOp));
			}
		}
		while (!opratorStack.empty()) {
			String topOp = opratorStack.pop();
			//System.out.println("***" + topOp + "***");
			calculate(opNumStack, topOp);
		}
		result = opNumStack.peek();
		return result;
 	}
	/*public static void main(String[] args) {
		// TODO Auto-generated method stub
		Expression exp = new Expression("+ * 3  2 9");
		System.out.println(exp.calNum());
		//Stack<Integer> tes = new Stack<Integer>();
		//tes.pop();
		System.out.println(exp.expList);
		System.out.println(exp.toPrefix());
		System.out.println(exp.expList);
		System.out.println(exp.calNum());
		//System.out.println(exp.toSuffix());
		
		//System.out.println(exp.calNum());
		
		for (String string : exp.expList) {
			System.out.println(priority.get(string));
		}
}	*/
}
