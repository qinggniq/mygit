package com.qinggniq.homework.Course;

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
	int maxScore = 0;
	
	public CourseManager(int coursNum,int maxScore) {
		this.courseTable = new ArrayList<Course>();
		this.peerSemMaxCourseNum = coursNum;
		this.maxScore = maxScore;
		this.curCourseNum = 0;
	}
	public void addCourse(Course c) {
		this.courseTable.add(c);
		this.curCourseNum++;
	}
	public Map<String, Integer> getCourseMark() {
		Map<String, Integer> result = new HashMap<String, Integer>();
		for(int i=0;i < courseTable.size(); i++){
			result.put(this.courseTable.get(i).courseId, i);
		}
		return result;
	}
	public ArrayList<ArrayList<Node>> buildCourseGraph() {
		ArrayList<ArrayList<Node>> result =new ArrayList<ArrayList<Node>>();
		Map<String, Integer> courseMarkMap = this.getCourseMark();
		
		for(int i=0;i < courseTable.size(); i++) {
			result.add(new ArrayList<Node>());
			result.get(i).add(new Node(i));
		}
		for(int i=0;i < courseTable.size();i++) {
			System.out.println(i + ":");
			for(Iterator<String> it = this.courseTable.get(i).preCourseIdTable.iterator();it.hasNext();){
				String preCourse = it.next();
				result.get(courseMarkMap.get(preCourse)).add(result.get(i).get(0));
				System.out.println("---" + preCourse);
			}
		}
		return result;
	}
	public void showCourse() {
		for(int i=0;i < this.courseTable.size(); i++) {
			
			System.out.println("***" + this.courseTable.get(i).preCourseIdTable);
		}
	}
	public void showGraph() {
		ArrayList<ArrayList<Node>> result = this.buildCourseGraph();		
		for(int i=0;i < result.size(); i++) {
			for(int j=0;j < result.get(i).size(); j++) {
				System.out.print(result.get(i).get(j).mark + " ");
			}
			System.out.println();
		}
	}
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
	public void changeCourseNum(int courseNum) {
		this.peerSemMaxCourseNum = courseNum;
	}
	public void changeMaxScore(int maxScore) {
		this.maxScore = maxScore;
	}
	public void changeTotalSemester(int totalSemester) {
		this.totalSemester = totalSemester;
	}
	public void changemaxScore(int maxScore) {
		this.maxScore = maxScore;
	}
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
		}
	}
	
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
		this.maxScore = inputMisc(sc);
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
				
			default:
				break;
			}
		}
	}
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
	public static void main(String[] args) {
		CourseManager cm = new CourseManager(12,30);
		Scanner sc = new Scanner(System.in);
		cm.inputCourseTable(sc, "!");
		sc.close();
		cm.showCourse();
		cm.showGraph();
	}

}
