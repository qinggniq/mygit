package com.qinggniq.homework.Course;

import java.io.File;
import java.io.FileNotFoundException;
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
		System.out.println(courseMarkMap);
		for(int i=0;i < courseTable.size(); i++) {
			result.add(new ArrayList<Node>());
			result.get(i).add(new Node(i,this.courseTable.get(i).preCourseIdTable.size()));
		}
		for(int i=0;i < courseTable.size();i++) {
			System.out.println(i + ":");
			for(Iterator<String> it = this.courseTable.get(i).preCourseIdTable.iterator();it.hasNext();){
				String preCourse = it.next();
				result.get(courseMarkMap.get(preCourse)).add(result.get(i).get(0));
				System.out.println("---" + preCourse);
			}
		}
		System.out.println(result);
		return result;
	}
	//打印课程表
	public void showCourse() {
		for(int i=0;i < this.courseTable.size(); i++) {
			//打印课程信息
			System.out.println("***" + this.courseTable.get(i).preCourseIdTable);
		}
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
				System.out.println("---"+result[i][j].courseId+"---");
			}
		}
		
		return result;
	}
	//最快完成课程
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
		for(int i=0;i < semNum-1; ++i) {
			result[i] = new Course[peerSemMaxCourseNum];
			for(int j=0;j < peerSemMaxCourseNum; ++j,++cnt) {
				result[i][j] = this.courseTable.get(topSort[cnt]);
			}
		}
		result[semNum-1] = new Course[leftCourseNum];
		for(int i=0;i<leftCourseNum;i++,++cnt){
			result[semNum-1][i] = this.courseTable.get(cnt);
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
			return "无法完成安排，请检查课程中的先修关系是否正确";
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
	//删除课程
  	//删除课程（根据课程编号）
	public boolean deletCourse(String courseId) {
		boolean result = false;
		Course curCourse = null;
		for(int i=0;i < this.courseTable.size();i++) {
			curCourse = this.courseTable.get(i);
			if(curCourse.courseId.equals(courseId)){
				this.courseTable.remove(i);
				result = true;
				continue;
			}
			if(curCourse.preCourseIdTable.contains(courseId)) {
				curCourse.preCourseIdTable.remove(courseId);
			}
		}
		return result;
	}
	//修改每学期课程最大数
	
	//改变每学期最大课程数
	public void changePeerMaxCourseNum(int courseNum) {
		this.peerSemMaxCourseNum = courseNum;
	}
	//改变每学期最多学分
	//修改一学期最大学分
	public void changepeerSemMaxSore(int peerSemMaxSore) {
		this.peerSemMaxSore = peerSemMaxSore;
	}
	//改变总共学期数
	//修改最大学期数
	public void changeTotalSemester(int totalSemester) {
		this.totalSemester = totalSemester;
	}
	//修改课程编号
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
	//修改课程名称
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
	//修改课程学分
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
	//输入课程表
	
	//输入课程信息
	public void inputCourseTable(Scanner sc,String endMark ) {
		for(;;){
			String courseId = null;
			String courseName = null;
			int courseStuScore = 0;
			Set<String> preCourseTable = new HashSet<String>();
			System.out.print("输入课程编号:");
			if((courseId = sc.next()).equals(endMark)) {
				break;
			}
			System.out.print("请输入课程名称:");
			if((courseName = sc.next()).equals(endMark)) {
				break;
			}
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
	public void input(InputStream is,String endMark) {
		Scanner sc = new Scanner(is);
		System.out.println("--------------------------教学计划编制------------------------------------------");
		System.out.println("\t功能：");
		//一些输出
		System.out.println("请输入最大学期数(默认为10):");
		this.totalSemester = inputMisc(sc);
		System.out.print("请输入每学期可修最大课程数：");
		this.peerSemMaxCourseNum = inputMisc(sc);
		System.out.print("请输入一学期学分上限：");
		this.peerSemMaxSore = inputMisc(sc);
		System.out.println("请逐个输入课程信息(包括编号，名称，学分，先修课程):");
		this.inputCourseTable(sc, endMark);
		System.out.println("课程信息输入完毕。");
		///功能
		boolean inputGoOn = true;
		int mode = 0;
		for(;inputGoOn;) {
			try {
				mode = sc.nextInt();
			} catch (InputMismatchException e) {
				// TODO: handle exception
				System.out.println("输入错误，请重新输入。");
				continue;
			}
			switch (mode) {
			case 0://添加课程
				System.out.println("输入" + endMark + "停止添加课程.");
				this.inputCourseTable(sc, endMark);
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
				System.out.println("请选择要修改的信息：");
				System.out.println("\t1.课程编号 2.课程名称 3.课程学分 4.最大学期数 \n\t" +
						"5.一学期学分上限 6.一学期最大课程数");
				int mod = 0;
			
			case 3://导入课程
				System.out.print("请输入导入课程的文件名称：");
				String filePath = sc.next();
				File inCouFile = new File(filePath);
				try {
					Scanner fsc = new Scanner(inCouFile);
					this.inputCourseTable(fsc, endMark);
					fsc.close();
				} catch (FileNotFoundException e) {
					// TODO Auto-generated catch block
					System.out.println("文件打开失败，请检查输入路径是否正确。");
				}
				break;
			default:
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
				System.out.println("输入错误，请重新输入");
				continue;
			}
			break;
		}
		return result;
	}
	public static void main(String[] args) throws FileNotFoundException {
		CourseManager cm = new CourseManager(5,7);
		Scanner sc = new Scanner(new File("/home/wc/HrbeuCourseTable.ct"));
		cm.inputCourseTable(sc, "!");
		sc.close();
		//cm.showCourse();
		//cm.showGraph();
		System.out.print(cm.showDistributionByMinTime());
	}	
}


