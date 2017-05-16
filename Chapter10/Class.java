package com.qinggniq.homework.finalClassManager;

import java.io.File;
import java.io.IOException;
import java.text.Format;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.Comparator;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Iterator;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Scanner;
import java.util.Set;
import java.util.function.Function;
import java.util.function.ToDoubleFunction;
import java.util.function.ToIntFunction;
import java.util.function.ToLongFunction;

import javax.swing.text.StyledEditorKit.ForegroundAction;

public class Class implements Comparable<Class>{
	Double classTotalScore = 0.0;
	Integer classStuNum = 0;
	String classId = null;
	ArrayList<Student> stuTable = null;
	Map<String, ArrayList<Student>> identCourseMap = null;
	Set<String> courseSet = new HashSet<String>();
	public Class(Scanner sc) {
		this.classId = sc.next();
		this.classStuNum = sc.nextInt();
		this.stuTable = new ArrayList<Student>();
		this.identCourseMap = new HashMap<String,ArrayList<Student>>();
		String[] courseNames = new String[3];
		for(int i=0;i<3;++i){
			courseNames[i] = sc.next();
			this.courseSet.add(courseNames[i]);
		}
		for(int i=0;i<this.classStuNum;++i) {
			this.stuTable.add(new Student(sc,classId));
		}
		
		for(int i=0;i<this.stuTable.size();++i) {
			this.classTotalScore += this.stuTable.get(i).totalScore;
		}
		this.classTotalScore/=this.stuTable.size();
		for(int i=0;i<3;++i) {
			String tmpString = courseNames[i];
			this.identCourseMap.put(tmpString, this.stuTable);
			Collections.sort(this.identCourseMap.get(tmpString), new MyComparetor(tmpString));
		}
		Collections.sort(this.stuTable);
	}
	

	public String showTableBySpecificScore(String course) {
		String result = "";
		ArrayList<Student> tmpArrayList = null;
		if(course == null){
			tmpArrayList = this.stuTable;
		}else{
			tmpArrayList = this.identCourseMap.get(course);
		}
		for(int i=0;i<stuTable.size();i++) {
			result += tmpArrayList.get(i).getSpeceficInfo(course);
		}
		return result;
	}
	public String showSpecificScoreInterval(int mode,String courseId) {
		String result = "";
		if(courseId == null){
			result += (this.classId + "班总分在 ");
		}else{
			result += (this.classId + "班 " +courseId + "科目分数在 ");
		}
		switch (mode) {
		case 1://>=90
			result += "大于90分段的学生详细信息为:\n";
			result += this.getSpecificScoreInfo(100, 90,courseId);
			break;
		case 2:
			result += "80-89分段的学生详细信息为:\n";
			result += this.getSpecificScoreInfo(89, 80,courseId);
			break;
		case 3:
			result += "70-79分段的学生详细信息为:\n";
			result += this.getSpecificScoreInfo(79, 70,courseId);
			break;
		case 4:
			result += "60-69分段的学生详细信息为:\n";
			result += this.getSpecificScoreInfo(69, 60,courseId);
			break;
		case 5:
			result += "<60分段的学生详细信息为:\n";
			result += this.getSpecificScoreInfo(60,0,courseId);
			break;
		default:
			break;
		}
 		return result;
	}
	
	public ArrayList<ArrayList<Student>> sumIntervalScoreStudent(String courseId) {
		ArrayList<ArrayList<Student>> result = new ArrayList<ArrayList<Student>>();
		for(int i=0;i<5;++i){
			result.add(new ArrayList<Student>());
		}
		this.sumSpecificScoreNum(100, 90, courseId, result.get(0));
		this.sumSpecificScoreNum(89, 80, courseId, result.get(1));
		this.sumSpecificScoreNum(79, 70, courseId, result.get(2));
		this.sumSpecificScoreNum(69, 60, courseId, result.get(3));
		this.sumSpecificScoreNum(60, 0, courseId,	result.get(4));
		return result;
	}
	public String getSpecificScoreInfo(int up,int low,String course){
		String result = "";
		int num = 0;
		if(course==null){
			for(int i=0;i<this.stuTable.size();i++) {
				Student tmpStudent = this.stuTable.get(i);
				if(tmpStudent.totalScore <= up) {
					if(tmpStudent.totalScore >= low){
						result += tmpStudent.getSpeceficInfo(course);
						num++;
					}else{
						break;
					}
				}
			}
			result+=("总计："+num+"人");
		}else{
			for(int i=0;i<this.stuTable.size();i++) {
				Student tmpStudent = this.stuTable.get(i);
				if(tmpStudent.scoreMap.get(course) <= up) {
					if(tmpStudent.scoreMap.get(course) >= low){
						result += tmpStudent.getSpeceficInfo(course);
						num++;
					}else{
						break;
					}
				}
			}
			result+=("总计："+num+"人");
		}
		return result;
	}
	
	public void sumSpecificScoreNum(int up,int low,String courseId,ArrayList<Student> students) {	
		
		if(courseId==null){
			for(int i=0;i<this.stuTable.size();i++) {
				Student tmpStudent = this.stuTable.get(i);
				if(tmpStudent.totalScore<= up) {
					if(tmpStudent.totalScore >= low){
						students.add(tmpStudent);
					}else{
						break;
					}
				}
			}
		}else{
			for(int i=0;i<this.stuTable.size();i++) {
				Student tmpStudent = this.stuTable.get(i);
				if(tmpStudent.scoreMap.get(courseId) <= up) {
					if(tmpStudent.scoreMap.get(courseId) >= low){
						students.add(tmpStudent);
					}else{
						break;
					}
				}
			}
		}
	}
	@Override
	public int compareTo(Class args) {
		return -this.classTotalScore.compareTo(args.classTotalScore);
	}
	public static void main(String[] args) {
		try{
			Scanner sc = new Scanner(new File("/home/wc/TEST/input.txt"));
			System.out.print(new Class(sc).sumIntervalScoreStudent(null));	
			sc.close();	
		}catch(IOException e){
			e.printStackTrace();
		}
	}

}
class MyComparetor implements Comparator<Student> {
	String courseName = null;
	public MyComparetor(String courseName) {
		this.courseName = courseName;
	}
	public int compare(Student args1,Student args2){
		return args1.compareToIdenCourseScore(args2, courseName);
	}
}
class Student implements Comparable<Student>{
	String classId = null; 
	 String name = null;
	 String Id = null;
	 Double totalScore = 0.0;
	 Map<String, Integer> scoreMap = null ;
	
	public Student(String name,Map<String, Integer> scoreMap) {
		// TODO Auto-generated constructor stub
		this.name = name;
		this.scoreMap = scoreMap;
		Set<Entry<String, Integer>> tmpSet = this.scoreMap.entrySet(); 
		for(Iterator<Entry<String, Integer>> Iterator = tmpSet.iterator();Iterator.hasNext();) {
			this.totalScore += Iterator.next().getValue();
		}
	}
	
	public Student(Scanner sc,String classId) {
		this.scoreMap = new HashMap<String, Integer>();
		this.Id = sc.next();
		this.name = sc.next();
		this.classId = classId;
		for(int i=0;i<3;++i) {
			String courseId = sc.next();
			int courseScore = Integer.parseInt(sc.next());
			this.totalScore += courseScore;
			this.scoreMap.put(courseId, courseScore);
		}
		this.totalScore/=3;
	}
	public String getSpeceficInfo(String course) {
		String result = "";
		result += ("学号 "+this.Id+" ");
		result += ("姓名 "+this.name+" ");
		result += ("班级 "+this.classId+" ");
		Set<Entry<String, Integer>> tmpSet = this.scoreMap.entrySet();
		for(Iterator<Entry<String, Integer>> Iterator = tmpSet.iterator();Iterator.hasNext();) {
			Entry<String, Integer> tmpEntry = Iterator.next();
			if(course == null || course.equals(tmpEntry.getKey())){
				result += (tmpEntry.getKey() + " : " + tmpEntry.getValue()+" ");
			}
		}
		if(course == null)
			result+=("总分: "+this.totalScore.toString().substring(0, 4)+"\n");
		else{
			result += "\n";
		}
		return result;
	}
	public int compareToIdenCourseScore(Student args,String courseName) {
		return -this.scoreMap.get(courseName).compareTo(args.scoreMap.get(courseName));
	}
	@Override
	public int compareTo(Student arg) {
		// TODO Auto-generated method stub
		return this.totalScore > arg.totalScore ? -1 :
			(this.totalScore < arg.totalScore ? 1 : 0);
	}
	
}