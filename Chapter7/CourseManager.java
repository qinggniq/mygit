package com.qinggniq.homework.Course;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.InputMismatchException;
import java.util.Iterator;
import java.util.Map;
import java.util.MissingFormatArgumentException;
import java.util.Scanner;
import java.util.Set;

import javax.net.ssl.SSLContext;

import com.qinggniq.homework.Course.*;

public class CourseManager {
	ArrayList<Course> courseTable = null;
	int totalSemester = 0;
	int peerSemMaxCourseNum = 0;
	int curCourseNum = 0;
	int peerSemMaxSore = 0;
	
	//通过每学期最大分数，最大学分初始化
	public CourseManager(int peerSemMaxCourseNum,int totalSemester) {
		this.courseTable = new ArrayList<Course>();
		this.peerSemMaxCourseNum = peerSemMaxCourseNum;
		this.totalSemester = totalSemester;
		this.curCourseNum = 0;
	}
	//添加课程
	public void addCourse(Course c) {
		this.courseTable.add(c);
		this.curCourseNum++;
	}
	//得到课程ID和位置的映射关系
	public Map<String, Integer> getCourseMark() {
		Map<String, Integer> result = new HashMap<String, Integer>();
		for(int i=0;i < courseTable.size(); i++){
			result.put(this.courseTable.get(i).courseId, i);
		}
		return result;
	}
	//根据课程的先修关系同过临界链表建图
	public ArrayList<ArrayList<Node>> buildCourseGraph() {
		ArrayList<ArrayList<Node>> result =new ArrayList<ArrayList<Node>>();
		Map<String, Integer> courseMarkMap = this.getCourseMark();
		//System.out.println(courseMarkMap);
		for(int i=0;i < courseTable.size(); i++) {
			result.add(new ArrayList<Node>());
			result.get(i).add(new Node(i,this.courseTable.get(i).preCourseIdTable.size()));
		}
		for(int i=0;i < courseTable.size();i++) {
			//System.out.println(i + ":");
			for(Iterator<String> it = this.courseTable.get(i).preCourseIdTable.iterator();it.hasNext();){
				String preCourse = it.next();
				result.get(courseMarkMap.get(preCourse)).add(result.get(i).get(0));
				//System.out.println("---" + preCourse);
			}
		}
		//System.out.println(result);
		return result;
	}
	//打印课程表
	public String showCourse() {
		String result = "";
		Course curCourse = null;
		System.out.println("课程编号\t课程名称\t课程学分\t先修课程");
		for(int i=0;i < this.courseTable.size(); i++) {
			curCourse = this.courseTable.get(i);
			result += " " + curCourse.courseId + " \t " +curCourse.courseName +" \t " +
					curCourse.stuScroce + " \t " + curCourse.preCourseIdTable+"\n";
		}
		return result;
	}
	//打印临界链表
	public void showGraph() {
		ArrayList<ArrayList<Node>> result = this.buildCourseGraph();		
		for(int i=0;i < result.size(); i++) {
			for(int j=0;j < result.get(i).size(); j++) {
				System.out.print(result.get(i).get(j).mark + " ");
			}
			System.out.println();
		}
	}
	//得到课程的拓扑排序数组
	private int[] getCourseSortByorder(){
		int[] result = new int[this.courseTable.size()];
		Graph graph = new Graph(this.buildCourseGraph());
		if(graph.getTopoSort(result)){
			return result;
		}else{
			return null;
		}
	}
	//得到课程编号的拓扑排序数组
	public String[] getCourseSortById(){
		String[] result = new String[this.courseTable.size()];
		int[] topSort = this.getCourseSortByorder();
		for(int i=0;i<this.courseTable.size();i++) {
			result[i] = this.courseTable.get(topSort[i]).courseId;
		}
		return result;
	}
	//均匀完成课程
	private Course[][] getBestDistrubtionByGeneTask(){
		if(curCourseNum > totalSemester*peerSemMaxCourseNum){
			return null;
		}
	
		int[] topSort = this.getCourseSortByorder();
		if(topSort == null){
			return null;
		}
		Course[][] result = new Course[this.totalSemester][];
		int peerCourseNum = this.curCourseNum/this.totalSemester;
		int leftCourse = this.curCourseNum%this.totalSemester;
		System.out.println(topSort.length);
		int cnt = 0;
		for(int i=0;i < this.totalSemester; i++) {
			int inc = 0;
			if(leftCourse-- > 0){
				result[i] = new Course[peerCourseNum + 1];
				inc = 1;
			}else{
				result[i] = new Course[peerCourseNum];
				inc = 0;
			}
			for(int j=0;j < peerCourseNum+inc;j++,cnt++){
				result[i][j] = this.courseTable.get(topSort[cnt]);
				//System.out.println("---"+result[i][j].courseId+"---");
			}
		}
		
		return result;
	}
	
	//最快完成课程
	private Course[][] getBestDistrubtionByMinTime() {
		if(curCourseNum > totalSemester*peerSemMaxCourseNum){
			return null;
		}
		int[] topSort = this.getCourseSortByorder();
		if(topSort == null){
			return null;
		}
		int leftCourseNum = curCourseNum%peerSemMaxCourseNum ;
		int inc = curCourseNum%peerSemMaxCourseNum != 0? 1:0; 
		int semNum = curCourseNum/peerSemMaxCourseNum + inc;
		
		Course[][] result = new Course[semNum][];
		
		int cnt = 0;
		//System.out.println("SemNum = "+semNum);
		if(inc == 1){
			for(int i=0;i < semNum-1; ++i) {
				result[i] = new Course[peerSemMaxCourseNum];
				for(int j=0;j < peerSemMaxCourseNum; ++j,++cnt) {
					result[i][j] = this.courseTable.get(topSort[cnt]);
				}
			}
			result[semNum-1] = new Course[leftCourseNum];
			for(int i=0;i<leftCourseNum;i++,++cnt){
				result[semNum-1][i] = this.courseTable.get(cnt);
				//System.out.println(result[semNum-1][i]);
			}
		}else{
			for(int i=0;i < semNum; ++i) {
				result[i] = new Course[peerSemMaxCourseNum];
				for(int j=0;j < peerSemMaxCourseNum; ++j,++cnt) {
					result[i][j] = this.courseTable.get(topSort[cnt]);
				}
			}
		}
		return result;
	}
	
	//显示均匀完成的安排
	public String showDistributionByGeneTask() {
		Course[][] resultDis = this.getBestDistrubtionByGeneTask();
		String saveRes = "";
		if(resultDis == null){
			return "无法完成安排，请检查课程中的先修关系是否正确";
		}else{
			for(int i=0;i < resultDis.length;++i) {
				saveRes += "第" +(i+1)+"学期:  ";
				for(int j=0;j < resultDis[i].length;++j) {
					if(j == resultDis[i].length -1)
						saveRes += "[ " + resultDis[i][j].courseId + " : " +resultDis[i][j].courseName+" ]"; 
					else{
						saveRes += "[ " + resultDis[i][j].courseId + " : " +resultDis[i][j].courseName + " ] -> ";
					}
				}
				saveRes += "\n";
			}
		}
		return saveRes;
	}
	//显示最快完成的安排
	public String showDistributionByMinTime() {
		Course[][] resultDis = this.getBestDistrubtionByMinTime();
		String saveRes = "";
		if(resultDis == null){
			if(curCourseNum > peerSemMaxCourseNum*totalSemester){
				return "输入课程过多";
			}else{
				return "无法完成安排，请检查课程中的先修关系是否正确";
			}
		}else{
			for(int i=0;i < resultDis.length;++i) {
				saveRes += "第" +(i+1)+"学期:  ";
				for(int j=0;j < resultDis[i].length;++j) {
					if(j == resultDis[i].length -1)
						saveRes += "[ " + resultDis[i][j].courseId + " : " +resultDis[i][j].courseName + " ]" ; 
					else{
						saveRes += "[ " + resultDis[i][j].courseId + " : " +resultDis[i][j].courseName + " ] -> ";
					}
				}
				saveRes += "\n";
			}
		}
		return saveRes;
	}
  	//删除课程（根据课程编号）
	public boolean deletCourse(String courseId) {
		boolean result = false;
		Course curCourse = null;
		int pos = 0;
		for(int i=0;i < this.courseTable.size();i++) {
			curCourse = this.courseTable.get(i);
			if(curCourse.courseId.equals(courseId)){
				//this.courseTable.remove(i);
				pos = i;
				result = true;
				continue;
			}
			if(curCourse.preCourseIdTable.contains(courseId)) {
				curCourse.preCourseIdTable.remove(courseId);
			}
		}
		if(result){
			this.curCourseNum--;
			this.courseTable.remove(pos);
		}
		return result;
	}
	//修改每学期课程最大数
	
	public boolean delePreCourse(String course,String preCourse) {
		Course curCourse = null;
		for(int i=0;i < this.courseTable.size();i++){
			curCourse = this.courseTable.get(i);
			if(curCourse.courseId.equals(course)){
				if(curCourse.preCourseIdTable.contains(preCourse)){
					curCourse.preCourseIdTable.remove(preCourse);
					return true;
				}else{
					return false;
				}
			}
		}
		return false;
	}
	public boolean addPreCourse(String course,String preCourse){
		Course curCourse = null;
		for(int i=0;i < this.courseTable.size();i++){
			curCourse = this.courseTable.get(i);
			if(curCourse.courseId.equals(course)){
				curCourse.preCourseIdTable.add(preCourse);
				return true;
			}
		}
		return false;
	}
	//改变每学期最大课程数
	public void changePeerMaxCourseNum(int courseNum) {
		this.peerSemMaxCourseNum = courseNum;
	}

	//修改一学期最大学分
	public void changepeerSemMaxSore(int peerSemMaxSore) {
		this.peerSemMaxSore = peerSemMaxSore;
	}
	
	//修改最大学期数
	public void changeTotalSemester(int totalSemester) {
		this.totalSemester = totalSemester;
	}
	
	//改变课程编号
	public void changeCourseId(String nowcourseId,String tocourseId) {
		Course curCourse = null;
		for(int i=0;i < this.courseTable.size();i++) {
			curCourse = this.courseTable.get(i);
			if(curCourse.courseId.equals(nowcourseId)){
				curCourse.courseId = tocourseId;
				continue;
			}
			if(curCourse.preCourseIdTable.contains(nowcourseId)) {
				curCourse.preCourseIdTable.remove(nowcourseId);
				curCourse.preCourseIdTable.add(tocourseId);
				break;
			}
		}
	}

	//改变课程名称
	public void changeCourseName(String nowcourseName,String tocourseName) {
		Course curCourse = null;
		for(int i=0;i < this.courseTable.size();i++) {
			curCourse = this.courseTable.get(i);
			if(curCourse.courseName.equals(nowcourseName)){
				curCourse.courseId = tocourseName;
				break;
			}	
		}
	}

	//改变课程学分
	public void changeCourseScore(String nowcourseId,int toScore) {
		Course curCourse = null;
		for(int i=0;i < this.courseTable.size();i++) {
			curCourse = this.courseTable.get(i);
			if(curCourse.courseId.equals(nowcourseId) ){
				curCourse.stuScroce = toScore;
				break;
			}	
		}
	}
	//导出课程表
	
	public boolean exportCourseTable(String filePath,String endMark) {
		String result = this.showCourse();
		
		String[] line = result.split("\n");
		File exportFile = new File(filePath);
		try{
			FileOutputStream fo = new FileOutputStream(exportFile);
			if(result.isEmpty()){
				fo.write('\0');
				fo.close();
				return true;
			}
			for(int i=0;i<line.length;i++) {
				String[] tmp = line[i].split("\t");
				for(int j=0;j<tmp.length;j++) {
					if(j != tmp.length - 1){
						fo.write((tmp[j].trim()+" ").getBytes());
					}else{
						String purSet = tmp[j].trim();
						String[] preC = purSet.substring(1,purSet.length()-1).split(",");
						for(int k =0 ;k < preC.length;k++) {
							fo.write((preC[k].trim()+" ").getBytes());
						}
						fo.write(("#\n").getBytes());
					}
				}
			}
			fo.write(endMark.getBytes());
			fo.close();
		}catch(IOException e){
			return false;
		}
		return true;
	}
	//输入课程信息
	public void inputCourseTable(Scanner sc,String endMark ,boolean isFile) {
		for(;;){
			String courseId = null;
			String courseName = null;
			int courseStuScore = 0;
			Set<String> preCourseTable = new HashSet<String>();
			if(!isFile)
				System.out.print("输入课程编号:");
			if((courseId = sc.next()).equals(endMark)) {
				break;
			}
			if(!isFile)
				System.out.print("请输入课程名称:");
			if((courseName = sc.next()).equals(endMark)) {
				break;
			}
			if(!isFile)
				System.out.print("请输入课程学分:");
			String curString = null;
			for(;;){
				if((curString = sc.next()).equals(endMark)) {
					return;
				}
				try{
					courseStuScore = Integer.parseInt(curString);
				} catch (Exception e){
					System.out.println("输入错误，请重新输入。");
					continue;
				}
				break;
			}
			if(!isFile)
				System.out.print("请输入此课程的先修课程(输入'#'结束输入)");
			for(;;) {
				if((curString = sc.next()).equals(endMark)) {
					return;
				} else if(curString.equals("#")){
					break;
				}
				preCourseTable.add(curString);
			}
			this.courseTable.add(new Course(courseId, courseName, courseStuScore, preCourseTable));
			this.curCourseNum++;
		}
	}
	//各种功能的选择输入
	public void input(Scanner sc,String endMark) {
		
		System.out.println("-------------------------------------教学计划编制---------------------------------------------------\n");
		System.out.println("输入必要信息：");
		//一些输出
		System.out.println("请输入最大学期数:");
		this.totalSemester = inputMisc(sc);
		System.out.print("请输入每学期可修最大课程数：");
		this.peerSemMaxCourseNum = inputMisc(sc);
		//System.out.print("请输入一学期学分上限：");
		//this.peerSemMaxSore = inputMisc(sc);
		//System.out.println("请逐个输入课程信息(包括编号，名称，学分，先修课程):");
		//this.inputCourseTable(sc, endMark,true);
	//	System.out.println("课程信息输入完毕。");
		///功能
		
		boolean inputGoOn = true;
		int mode = 0;
		for(;inputGoOn;) {
			System.out.println("\n------------------------------------------我是分割线-------------------------------------------------\n");
			System.out.println("0.添加课程  1.删除课程  2.信息修改  3.导入课程  4.导出课程 5.打印当前课程信息  " +
					"6.按照减轻学生负担原则安排课程  7.安装尽快学完原则安排课程  8.退出");
			System.out.print("请选择功能：");
			try {
				mode = sc.nextInt();
			} catch (InputMismatchException e) {
				// TODO: handle exception
				System.out.println("输入错误，请重新输入。");
				sc.next();
				continue;
			}
			switch (mode) {
				case 0://添加课程
					System.out.println("输入 " + endMark + " 停止添加课程.");
					this.inputCourseTable(sc, endMark,false);
					break;
				case 1://删除课程
					System.out.println("请输入要删除的课程编号:");
					String courseId = sc.next();
					if(this.deletCourse(courseId)){
						System.out.println("课程已从课程列表删除，与之相应的先修关系也已删除。");
					}else{
						System.out.println("课程删除失败，课程列表中无此课程。");
					}
					break;
				case 2://信息修改
					System.out.println("请选择要修改的信息类型：");
					System.out.println("\t1.课程编号 2.课程名称 3.课程学分 4.学期数 " +
							"5.一学期最大课程数 6.先修课程 7.取消");
					System.out.println("请选择要修改的信息：");
					int mod = 0;
					boolean goon = true;
					for(;goon;){
					
						mod = inputMisc(sc);
						switch (mod) {
						case 1:
							System.out.print("请输入要修改的课程编号：");
							String nowCourseId = sc.next();
							System.out.print("请输入修改后的课程编号：");
							String toCourseId = sc.next();
							this.changeCourseId(nowCourseId, toCourseId);
							System.out.println("修改成功。");
							goon = false;
							break;
						case 2:
							System.out.print("请输入要修改的课程名称：");
							String nowCourseName = sc.next();
							System.out.print("请输入修改后的课程名称：");
							String toCourseName = sc.next();
							this.changeCourseName(nowCourseName, toCourseName);
							goon = false;
							break;
						case 3:
							System.out.print("请输入要修改的课程编号：");
							String CourseId = sc.next();
							System.out.print("请输入修改后的课程编号：");
							int courseScore = inputMisc(sc);
							this.changeCourseScore(CourseId,courseScore);
							goon = false;
							break;
						case 4:
							System.out.print("请输入修改后的学期数：");
							int totalSemester = inputMisc(sc);
							this.changeTotalSemester(totalSemester);
							goon = false;
							break;
						case 5:
							System.out.print("请输入每学期课程数上限：");
							int peerSemeMaxCouNum = this.inputMisc(sc);
							this.changePeerMaxCourseNum(peerSemeMaxCouNum);
							goon = false;
							break;
						case 6:
							System.out.println("1.删除先修课程 2.添加先修课程");
							boolean cando = true;
							for(;cando;){
								System.out.print("请选" +
										"择功能：");
								int f = inputMisc(sc);
								switch (f) {
									case 1:
										System.out.print("请输入要修改先修课程的课程：");
										String cou = sc.next();
										System.out.print("请输入要删除的先修课程编号");
										String pre = sc.next();
										if(this.delePreCourse(cou, pre)){
											System.out.println("删除成功！");
										}else{
											System.out.print("删除失败!");
										}
										cando = false;
										break;
									case 2:
										System.out.print("请输入要修改先修课程的课程：");
										String co = sc.next();
										System.out.print("请输入要添加的先修课程编号");
										String pr = sc.next();
										if(this.addPreCourse(co, pr)){
											System.out.println("添加成功！");
										}else{
											System.out.print("添加失败!");
										}
										cando = false;
										break;
									default:
										System.out.print("输如错误。");
										break;
									}
								}
							case 7:
								goon = false;
								break;
							default:
								System.out.println("输入错误请重新输入：");
								break;
						}
					}
					break;
				case 3://导入课程
					System.out.print("请输入导入课程的文件名称：");
					String filePath = sc.next();
					File inCouFile = new File(filePath);
					try {
						Scanner fsc = new Scanner(inCouFile);
						this.inputCourseTable(fsc, endMark,true);
						fsc.close();
						System.out.println("文件导入成功");
					} catch (FileNotFoundException e) {
						// TODO Auto-generated catch block
						System.out.println("文件打开失败，请检查输入路径是否正确。");
					}
					break;
				case 4://导出课程
					System.out.print("请输入要导出到的文件名称及路径：");
					String path = sc.next();
					if(!this.exportCourseTable(path, endMark)){
						System.out.println("文件导出失败，请检查路径是否存在。");
					}else{
						System.out.println("文件导出成功！");
					}
					break;
				case 5://打印目前课程信息
					System.out.print(this.showCourse());
					break;
				case 6:
					System.out.println("按照减轻学生负担的原则，每学期的安排为：");
					System.out.print(this.showDistributionByGeneTask());
					break;
				case 7:
					System.out.println("按照尽快学完的原则，每学期的安排为：");
					System.out.print(this.showDistributionByMinTime());
					break;
				case 8://
					System.out.println("谢谢使用");
					inputGoOn = false;
					break;
				default:
					System.out.print("输入错误，请重新选择：");
					break;
			}

		}
	}
	//一些数值的输入错误处理
	private int inputMisc(Scanner sc) {
		int result = 0;
		for(;;) {
			try{
				result = sc.nextInt();
			}catch(InputMismatchException e){
				System.out.print("输入错误，请重新输入:");
				sc.next();
				continue;
			}
			break;
		}
		return result;
	}
	public static void main(String[] args) throws FileNotFoundException {
		CourseManager cm = new CourseManager(5,8);
		Scanner sc = new Scanner(System.in/*new File("/home/wc/HrbeuCourseTable.ct")*/);
		cm.input(sc, "!");
		sc.close();
		//cm.exportCourseTable("/home/wc/FFF.ct", "!");
		//System.out.print(cm.showCourse());
		//cm.showGraph();
		//System.out.print(cm.showDistributionByMinTime());
	}	
}


