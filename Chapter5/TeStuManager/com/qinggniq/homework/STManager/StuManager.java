package com.qinggniq.homework.STManager;

import java.awt.event.ActionListener;
import java.util.ArrayList;
import java.util.InputMismatchException;
import java.util.Scanner;
import java.util.jar.JarException;

import com.qinggniq.homework.STManager.*;

public class StuManager {
	ArrayList<Teacher> tecList = null;
	
	public StuManager() {
		// TODO Auto-generated constructor stub
		this.tecList = new ArrayList<Teacher>();
	}
	//加入老师
	public void addTecher(String name,String level) {
		this.tecList.add(new Teacher(name, level));
	}
	//插入学生（研究生）
	public boolean insertStu(String name,String classNum,String tecName,boolean canTeach) {
		boolean status = false;
		if(canTeach){
			Teacher tmpTec = null;
			for(int i=0;i<this.tecList.size();i++) {
				tmpTec = this.tecList.get(i);
				if(tmpTec.getName().equals(tecName)){
					tmpTec.stuTable.add(new Student(name,classNum,tecName,canTeach));
					status = true;
					break;
				}
			}
		}else {
			Teacher tmpTec = null;
			Student tmpStu = null;
			for(int i=0;i<this.tecList.size();i++) {
				tmpTec = this.tecList.get(i);
				if(tmpTec.getName().equals(tecName)){
					tmpTec.stuTable.add(new Student(name,classNum,tecName,canTeach));
					status = true;
					break;
				}else {
					for(int j=0;j<tmpTec.stuTable.size();j++){
						tmpStu = tmpTec.stuTable.get(j);
						if(tmpStu.getName().equals(tecName)){
							tmpStu.stuTable.add(new Student(name,classNum,tecName,canTeach));
							status = true;
							break;
						}
					}
				}
			}
		}
			return status;	
	}
	//查询学生情况
	public String query(String name) {
		
		for(int i=0;i<this.tecList.size();i++){
			String queryRes = this.tecList.get(i).query(name);
			if(queryRes!=null)
				return queryRes;
		}
		return null;
	}
	//删除学生
	public boolean deleteStu(String name,String classNum) {
		Teacher tmpTec = null;
		for(int i=0;i<this.tecList.size();i++){
			tmpTec = this.tecList.get(i);
			if(tmpTec.deleteStu(name, classNum)){
				return true;
			}
		}
		return false;
	}
	//导师带的学生的数量表示
	public String countNum(String name) {
		Teacher tmpTec = null;
		for(int i=0;i<this.tecList.size();i++){
			tmpTec = this.tecList.get(i);
			if(tmpTec.getName().equals(name))
				return tmpTec.showStuNum();
		}
		return null;
	}
	//导师带的学生的全部状况
	public String showStu(String name) {
		Teacher tmpTec = null;
		for(int i=0;i<this.tecList.size();i++){
			tmpTec = this.tecList.get(i);
			if(tmpTec.getName().equals(name))
				return tmpTec.show();
		}
		return null;
	}

	public boolean changeStuTec(String name,String classNum,String toStuName,boolean inTeach) {
		Student changStu = null;
		Teacher toTeacher = null;
		if(inTeach){
			for(int i=0;i<this.tecList.size();i++) {
				if(this.tecList.get(i).changeStu(name, classNum, toStuName)){
					//this.tecList.get(i).show();
					return true;
				}
			}
		}else{
			
			for(int i=0;i<this.tecList.size();i++){
				if(this.tecList.get(i).getName().equals(toStuName)){
					toTeacher = this.tecList.get(i);
				}
			}
			if(toTeacher == null){
				return false;
			}
			for(int i=0;i<this.tecList.size();i++) {
				changStu = this.tecList.get(i).getStudent(name, classNum);
				if(changStu != null){
					break;
				}
			}
		}
		if(changStu != null){
			toTeacher.stuTable.add(changStu);
			return true;
		}else{
			return false;
		}	
	}
	public boolean deleteTec(String name) {
		for(int i=0;i<this.tecList.size();i++){
			if(this.tecList.get(i).getName().equals(name)){
				this.tecList.remove(i);
				return true;
			}
		}
		return false;
	}
	public void input() {
		int mode;
		Scanner scanner = new Scanner(System.in);
		System.out.println("导师学生管理");
		for(;;){
			System.out.println("1.添加导师   2.添加学生  3.删除学生  4.查询导师（学生）信息  5.统计导师所带学生 " +
					"6.输出导师所带所有学生信息  7.删除导师 8.更改学生负责人 9.退出");
			try{
				mode = scanner.nextInt();
			}catch(InputMismatchException e){
				System.out.println("输入错误请重新输入");
				scanner.next();
				continue;
			}
			String level = null;
			String name = null;
			String classNum = null;
			String techName = null;
			int canTech = 0;
			int inTeacher = 0;
			boolean inputFlag = true;
			switch (mode) {
			case 1:
				System.out.print("请输入老师的姓名与职称：");
				name = scanner.next();
				level = scanner.next();
				this.tecList.add(new Teacher(name, level));
				System.out.println("导师 " + name + " : "+level+"已被加入到表中。");
				break;
			case 2:
				if(isEmpty()){
					System.out.println("表中还没有导师，请先添加导师。");
				}else{
					
					for(;inputFlag;){
						System.out.println("请选择学生类型：1.本科生  2.研究生 3.返回上一级");
						try{
							canTech = scanner.nextInt();
						}catch(InputMismatchException e){
							System.out.println("输入格式错误，请重新输入。");
							scanner.next();
							continue;
						}
						switch (canTech) {
						case 1:
							System.out.print("请输入学生的姓名,班级和导师或上级研究生名字：");
							name = scanner.next();
							classNum = scanner.next();
							techName = scanner.next();
							if(this.insertStu(name, classNum, techName, false)){
								System.out.println("本科生：" + name + " " + classNum + " 已被成功插入。");
								inputFlag = false;
							}else{
								System.out.println("插入失败，请检查老师或研究生是否存在。");
								break;
							}
							break;
						case 2:
							System.out.print("请输入学生的姓名,班级和导师的名字：");
							name = scanner.next();
							classNum = scanner.next();
							techName = scanner.next();
							if(this.insertStu(name, classNum, techName, true)){
								System.out.println("本科生：" + name + " " + classNum + " 已被成功插入。");
								inputFlag = false;
							}else{
								System.out.println("插入失败，请检查老师是否存在。");
								break;
							}
							break;
						case 3:
							inputFlag = false;
							break;
						default:
							System.out.println("插入失败，请检查老师或研究生是否存在。");
							break;
						}
					}
				}
				break;
			case 3:
				System.out.print("请输入学生的姓名与班级：");
				name = scanner.next();
				classNum = scanner.next();
				if(this.deleteStu(name, classNum)){
					System.out.println("学生 "+ name + " " + classNum + " 已被成功删除。");
				}else{
					System.out.println("删除失败，请检查是否输入错误。");
				}
				break;
			case 4:
				if(isEmpty()){
					System.out.println("表中还没有导师，请先添加导师。");
				}else{
					System.out.print("请输入要查寻的导师或学生的名字：");
					String qurRes = null;
					name = scanner.next();
					qurRes = this.query(name);
					if(qurRes != null){
						System.out.println(name + " 的基本信息为：");
						System.out.print(qurRes);
					}else{
						System.out.println("查询失败，请检查是否输入错误。");
					}
				}
				break;
			case 5:
				if(isEmpty()){
					System.out.println("表中还没有导师，请先添加导师。");
				}else{
					System.out.print("请输入要统计的导师的姓名：");
					name = scanner.next();
					String cntNum = this.countNum(name);
					if(cntNum != null){
						System.out.println("统计结果如下：");
						System.out.println(cntNum);
					}else{
						System.out.println("统计失败，请检查输入是否正确。");
					}
				}
				break;
			case 6:
				if(isEmpty()){
					System.out.println("表中还没有导师，请先添加导师。");
				}else{
					System.out.print("请输入要输出的导师的姓名：");
					name = scanner.next();
					String stuIfo = this.showStu(name);
					if(stuIfo != null){
						System.out.println("输出结果如下：");
						System.out.println(stuIfo);
					}else{
						System.out.println("输出失败，请检查输入是否正确。");
					}
				}
				break;
			case 7:
				if(isEmpty()){
					System.out.println("表中还没有导师，请先添加导师。");
				}else{
					System.out.print("请输入要删除的导师的姓名：");
					name = scanner.next();
					if(this.deleteTec(name)){
						System.out.println("导师 " + name + " 已被成功删除。");
					}else{
						System.out.println("删除失败，请检查输入。");
					}
				}
				break;
				
			case 8:
				if(isEmpty()){
					System.out.println("表中还没有导师，请先添加导师。");
				}else{
					
					for(;inputFlag;){
						System.out.println("请选择修改类型：1.导师内修改  2.导师间修改 3.返回上一级");
						try{
							inTeacher = scanner.nextInt();
						}catch(InputMismatchException e){
							System.out.println("输入格式错误，请重新输入。");
							scanner.next();
							continue;
						}
						switch (inTeacher) {
						case 1:
							System.out.print("请输入学生的姓名,班级和要转移到的研究生的名字：");
							name = scanner.next();
							classNum = scanner.next();
							techName = scanner.next();
							if(this.changeStuTec(techName, classNum, techName,true)){
								System.out.println("本科生：" + name + " " + classNum + " 已被成功转移。");
								inputFlag = false;
							}else{
								System.out.println("插入失败，请检查研究生是否存在。");
								break;
							}
							break;
						case 2:
							System.out.print("请输入学生的姓名,班级和要转入的导师的名字：");
							name = scanner.next();
							classNum = scanner.next();
							techName = scanner.next();
							if(this.changeStuTec(name, classNum, techName,false)){
								System.out.println("本科生：" + name + " " + classNum + " 已被成功转移。");
								inputFlag = false;
							}else{
								System.out.println("修改失败，请检查老师是否存在。");
								break;
							}
							break;
						case 3:
							inputFlag = false;
							break;
						default:
							System.out.println("插入失败，请检查老师或研究生是否存在。");
							break;
						}
					}
				}
				break;
			case 9:
				System.out.println("谢谢使用！");
				scanner.close();
				return;
			default:
				System.out.println("没有(" + mode +")这个选项，请检查输入。");
				break;
			}
		System.out.println("------------------------------------------------------------" +
				"---------------------------------------------------------------");
		}
	}
	public boolean isEmpty() {
		return tecList.isEmpty();
	}
	public static void main(String[] args) {	
		// TODO Auto-generated method stub
		StuManager stm = new StuManager();
		/*stm.addTecher("wc", "1");
		stm.insertStu("qing", "2015051213", "wc",true);
		stm.insertStu("niu", "20150612", "qing", false);
		stm.addTecher("gang", "2");
		stm.insertStu("gniq","20150613", "wc", true);
		System.out.println(stm.changeStuTec("niu","20150612", "gniq"));
		System.out.println(stm.showStu("wc"));
		System.out.println(stm.countNum("wc"));
		System.out.println(stm.query("wc")+stm.query("qing"));*/
		stm.input();
	}

}
