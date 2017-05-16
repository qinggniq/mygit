package com.qinggniq.homework.finalClassManager;

import java.awt.CardLayout;
import java.io.BufferedOutputStream;
import java.io.BufferedWriter;
import java.io.DataOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStreamWriter;
import java.nio.file.Path;
import java.text.Format;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.HashSet;
import java.util.InputMismatchException;
import java.util.Iterator;
import java.util.Map;
import java.util.Random;
import java.util.Scanner;
import java.util.Set;

import org.ietf.jgss.Oid;

public class ClassExamManager {
	ArrayList<Class> classTable = new ArrayList<Class>();
	Integer classNum = 0;
	Set<String> courseSet = new HashSet<String>();
	Map<String, Boolean> tmpMap = new HashMap<String, Boolean>();
	int maxExamInOneDay = 0;
	static String[] firstName = {
		"王","李","董","赵","徐","刘","鲁","高","付","崔","黄","马","张","苏","胡","杨","欧","敦"
	};
	static String[] secondName = ("访天、静柏、凌丝、小翠、雁卉、访文、凌文、芷云、思柔、巧凡、慕山、依云、千柳、从凝、安梦、香旋、凡巧、映天、安柏、平萱、以筠、忆曼、新竹、绮露、觅儿、" +
			"碧蓉、白竹、飞兰、曼雁、雁露、凝冬、含" +
			"灵、初阳、海秋、香天、夏容、傲冬、谷翠、冰双、绿兰、盼易、思松、梦山、友灵、绿竹、灵安、凌柏、秋柔、又蓝、尔竹、香天、天蓝、青枫、问芙、" +
			"语海、灵珊、凝丹、小蕾、迎夏、水之、飞珍、冰夏、亦竹、飞莲、海白、元蝶、春蕾、芷天、怀绿、尔容、元芹、若云、寒烟、听筠、采梦、凝莲、元彤、" +
			"觅山、痴瑶、代桃、冷之、盼秋、秋寒、慕蕊、巧夏、海亦、初晴、巧蕊、听安、芷雪、以松、梦槐、寒梅、香岚、寄柔、映冬、孤容、晓蕾、安萱、听枫、夜绿、" +
			"雪莲、从丹、碧蓉、绮琴、雨文、幼荷、青柏、痴凝、初蓝、忆安、盼晴、寻冬、雪珊、梦寒、迎南、巧香、采南、如彤、春竹、采枫、若雁、翠阳、沛容、幻翠、山兰、" +
			"芷波、雪瑶、代巧、寄云、慕卉、冷松、涵梅、书白、乐天、雁卉、宛秋、傲旋、新之、凡儿、夏真、静枫、痴柏、恨蕊、乐双、白玉、问玉、寄松、丹蝶、元瑶、冰蝶、访曼、" +
			"代灵、芷烟、白易、尔阳、怜烟、平卉、丹寒、访梦、绿凝、冰菱、语蕊、痴梅、思烟、忆枫、映菱、访儿、凌兰、曼岚、若枫、傲薇、凡灵、乐蕊、秋灵、谷槐、觅云 、以寒、" +
			"寒香、小凡、代亦、梦露、映波、友蕊、寄凡、怜蕾、雁枫、水绿、曼荷、笑珊、寒珊、谷南、慕儿、夏岚、友儿、小萱、紫青、妙菱、冬寒、曼柔、语蝶、青筠、夜安、觅海、问安、" +
			"晓槐、雅山、访云、翠容、寒凡、晓绿、以菱、冬云、含玉、访枫、含卉、夜白、冷安、灵竹、醉薇、元珊、幻波、盼夏、元瑶、迎曼、水云、访琴、谷波、乐之、笑白、之山、妙海、" +
			"紫霜、平夏、凌旋、孤丝、怜寒、向萍、凡松、青丝、翠安、如天、凌雪、绮菱、代云、南莲、寻南、春文、香薇、冬灵、凌珍、采绿、天春、沛文、紫槐、幻柏、采文、春梅、雪旋、" +
			"盼海、映梦、安雁、映容、凝阳、访风、天亦、平绿、盼香、觅风、小霜、雪萍、半雪、山柳、谷雪、靖易、白薇、梦菡、飞绿、如波、又晴、友易、香菱、冬亦、问雁、妙春、海冬、半安、" +
			"平春、幼柏、秋灵、凝芙、念烟、白山、从灵、尔芙、迎蓉、念寒、翠绿、翠芙、靖儿、妙柏、千凝、小珍、天巧。妙旋、雪枫、夏菡、元绿、痴灵、绮琴、雨双、听枫、觅荷、凡之、晓凡、雅彤、" +
			"香薇、孤风、从安、绮彤、之玉、雨珍、幻丝、代梅、香波、青亦、元菱、海瑶、飞槐、听露、梦岚、幻竹、新冬、盼翠、谷云、忆霜、水瑶、慕晴、秋双、雨真、觅珍、丹雪、从阳、元枫、痴香、思天、" +
			"如松、妙晴、谷秋、妙松、晓夏、香柏、巧绿、宛筠、碧琴、盼兰、小夏、安容、青曼、千儿、香春、寻双、涵瑶、冷梅、秋柔、思菱、醉波、醉柳、以寒、迎夏、向雪、香莲、以丹、依凝、如柏、"+
			"雁菱、凝竹、宛白、初柔、南蕾、书萱、梦槐、香芹、南琴、绿海、沛儿、晓瑶、听春、凝蝶、紫雪、念双、念真、曼寒、凡霜、飞雪、雪兰、雅霜、从蓉、冷雪、靖巧、翠丝、觅翠、凡白、乐蓉、迎波、" +
					"丹烟、梦旋、书双、念桃、夜天、海桃、青香、恨风、安筠、觅柔、初南、秋蝶、千易、安露、诗蕊、山雁、友菱、香露、晓兰、白卉、语山、冷珍、秋翠、夏柳").split("、");
	static String[] className = {"A","B","C","D","E","F","G","H","I","J"}; 
	static String[] courseName = {"B1","B2","B3","B4","B5","B6","B7","B8","B9","B10"};
	public ClassExamManager() {
		//this.classNum = classNum;
		
	}
	public ClassExamManager(String path) {
		this.loadClassInfo(path);
	}
	public void show(){
		for(int i=0;i<secondName.length;i++){
			System.out.print(secondName[i]+" ");
		}
	}
	private ArrayList<ArrayList<String>> generateManagement(){
		ArrayList<ArrayList<String>> result = new ArrayList<ArrayList<String>>();
		Set<String> tmpSet = new HashSet<String>();
		ArrayList<Set<Class>> courseToClass = new ArrayList<Set<Class>>();
		Map<String,Integer> coursePos = new HashMap<String,Integer>();
		int cnt = 0;
		//System.out.println(this.classTable.size());
		for(Iterator<String> it = this.courseSet.iterator();it.hasNext();) {
			String course = it.next();
			courseToClass.add(new HashSet<Class>());
			for(int i=0;i<this.classTable.size();i++) {
				if(this.classTable.get(i).courseSet.contains(course)){
					courseToClass.get(cnt).add(this.classTable.get(i));
				}
			}
			coursePos.put(course, cnt);
			cnt++;
		}
		cnt = -1;
		for(Iterator<String> it = this.courseSet.iterator();it.hasNext();) {
			Set<String> canNot = new HashSet<String>();
			String tmpCourse = it.next();
			
			if(!tmpSet.contains(tmpCourse)){
				tmpSet.add(tmpCourse);
				//System.out.print(tmpCourse+"---");
				result.add(new ArrayList<String>());
				//System.out.println(tmpSet);
				result.get(++cnt).add(tmpCourse);
			}else{
				continue;
			}
			//System.out.print(tmpCourse+"***");
			canNot.add(tmpCourse);
			for(Iterator<Class> iterator = courseToClass.get(coursePos.get(tmpCourse)).iterator();iterator.hasNext();){
				canNot.addAll(iterator.next().courseSet);
			}
			//System.out.print(cnt+"+++");
			for(Iterator<String> itn = this.courseSet.iterator();itn.hasNext();){
				String addCourse = itn.next();
				if((!canNot.contains(addCourse))&&(!tmpSet.contains(addCourse))){
					for(Iterator<Class> iterator = courseToClass.get(coursePos.get(addCourse)).iterator();iterator.hasNext();){
						canNot.addAll(iterator.next().courseSet);
					}
					//System.out.print(addCourse+" ");
					tmpSet.add(addCourse);
					result.get(cnt).add(addCourse);
				}
			}
			//System.out.print("\n");
			
		}
		return result;
	}
	public void showManageMent(){
		ArrayList<ArrayList<String>> result = this.generateManagement();
	//	System.out.println(result.size());
		for(int i=0;i<result.size();i++){
			for(int j=0;j<result.get(i).size();j++){
				System.out.print(result.get(i).get(j)+" ");
			}
			System.out.print("\n");
		}
	}
	public void generateStuInfo(String path) {
		try{
			OutputStreamWriter bos = new OutputStreamWriter(new FileOutputStream(new File(path)),"utf-8");
			BufferedWriter dos = new BufferedWriter(bos);
		Random genrator = new Random();
		for(int i=0;i<10;++i){
			dos.write(this.className[i]);
			dos.write(" ");
			int classStuNum = genrator.nextInt(30)%(60-30+1)+30;
			dos.write(Integer.toString(classStuNum));
			dos.write(" ");
			Set<Integer> saveSet = new HashSet<Integer>();
			for(int j=0;j<3;){
				int nextCoursePos = genrator.nextInt(10);
				if(saveSet.contains(nextCoursePos)){ 
					continue;
				}else{
					j++;
					saveSet.add(nextCoursePos);
				}
				dos.write(this.courseName[nextCoursePos]+" ");
			}
			dos.write("\n");
			for(int j=0;j<classStuNum;j++) {
				dos.write("201506"+i+j+" ");
				dos.write(firstName[genrator.nextInt(firstName.length)]+secondName[genrator.nextInt(secondName.length)]+" ");
				for(Iterator<Integer> it=saveSet.iterator();it.hasNext();) {
					dos.write(courseName[it.next()]+" ");
					dos.write(Integer.toString(genrator.nextInt(101)));
					dos.write(" ");
				}
				dos.write('\n');
			}
		}
		dos.close();
		}catch(IOException e){
			e.printStackTrace();
		}
	}
	public void loadClassInfo(String path){
		try{
			Scanner sc = new Scanner(new File(path));
		for(;sc.hasNext();++this.classNum) {
			this.classTable.add(new Class(sc));
		}
		Collections.sort(classTable);
		for(int i=0;i<classNum;i++) {
			this.courseSet.addAll(this.classTable.get(i).courseSet);
		}
		sc.close();
		}catch (IOException e){
			System.out.println("没有那个文件或目录");
		}
	}
	public String showAllInfoByCourseScore(String course) {
		ArrayList<Student> tmpArrayList = new ArrayList<Student>();
		String result = "";
		for(int i=0;i<classNum;i++) {
			Class tmpClass = this.classTable.get(i);
			if(course == null){
				tmpArrayList.addAll(tmpClass.stuTable);
			}else{
				if(tmpClass.identCourseMap.containsKey(course)){
					tmpArrayList.addAll(tmpClass.stuTable);
				}
			}
		}
		if(course == null) {
			Collections.sort(tmpArrayList);
		}else{
			Collections.sort(tmpArrayList,new MyComparetor(course));
		}
		for(int i=0;i<tmpArrayList.size();i++){
			Student tmpsStudent = tmpArrayList.get(i);
			result += tmpsStudent.getSpeceficInfo(course);
		}
		return result;
	}
	public String showAllInfo() {
		String result = "";
		for(int i=0;i<this.classNum;i++) {
			result+=(this.classTable.get(i).classId+"班\n");
			result+=this.classTable.get(i).showTableBySpecificScore(null);
			result += "合计"+this.classTable.get(i).classStuNum+"人 均分:"+this.classTable.get(i).classTotalScore.toString().substring(0,4)+"\n";
		}
		return result;
	}
	public String showSpecificInfoByClass(String classId) {
		String result = "";
		for(int i=0;i<this.classNum;++i) {
			Class tmpClass = this.classTable.get(i);
			if(tmpClass.classId.equals(classId)){
				result += tmpClass.showTableBySpecificScore(null);
				return result;
			}
		}
		return null;
	}
	
	private ArrayList<ArrayList<Student>> getIntevalScoreInfo(String courseId,String classId) {
		ArrayList<ArrayList<Student>> tmpArrayLists = new ArrayList<ArrayList<Student>>();
		for(int i=0;i<5;++i) {
			tmpArrayLists.add(new ArrayList<Student>());
		}
		
		if(courseId == null){
			for(int i=0;i<this.classNum;i++) {
				Class tmpClass = this.classTable.get(i);
				if(classId == null || tmpClass.classId.equals(classId)){
					ArrayList<ArrayList<Student>> curInterval = tmpClass.sumIntervalScoreStudent(null);
					for(int j=0;j<5;j++) {
						tmpArrayLists.get(j).addAll(curInterval.get(j));
					}
				}
			}
			for(int i=0;i<5;i++) {
				Collections.sort(tmpArrayLists.get(i));
			}
		}else{
			for(int i=0;i<this.classNum;i++) {
				Class tmpClass = this.classTable.get(i);
				if(classId == null || tmpClass.classId.equals(classId)){
				if(tmpClass.identCourseMap.containsKey(courseId)){
					ArrayList<ArrayList<Student>> curInterval = tmpClass.sumIntervalScoreStudent(courseId);
					for(int j=0;j<5;j++) {
						tmpArrayLists.get(j).addAll(curInterval.get(j));
					}
				}
			}
			}
			for(int i=0;i<5;i++) {
				Collections.sort(tmpArrayLists.get(i),new MyComparetor(courseId));
			}
		}
		return tmpArrayLists;
	}
	public String showIntervalInfo(String courseId,String classId) {
		ArrayList<ArrayList<Student>> tmpArrayList = this.getIntevalScoreInfo(courseId,classId);
		String result = "";
		if(courseId == null){
			result+="总分分数区间为：\n";
		}else{
			result+=courseId +"科目分数区间为：\n";
		}
		for(int i=0;i<5;++i) {
			switch (i) {
			case 0:
				result+=">90分段 总计："+tmpArrayList.get(i).size()+"人\n";
				break;
			case 1:
				result+="80-89分段 总计："+tmpArrayList.get(i).size()+"人\n";
				break;
			case 2:
				result+="70-79分段 总计："+tmpArrayList.get(i).size()+"人\n";
				break;
			case 3:
				result+="60-69分段 总计："+tmpArrayList.get(i).size()+"人\n";
				break;
			case 4:
				result+="<60分段 总计："+tmpArrayList.get(i).size()+"人\n";
				break;
			default :
					break;
			}
			for(int j=0;j<tmpArrayList.get(i).size();j++) {
				result+=tmpArrayList.get(i).get(j).getSpeceficInfo(courseId);
			}
		}
		return result;
	}
	public String showIntervalInfoBySpecificClass(String classId,String courseId,int mode) {
		String result = "";
		Class tmpClass = null;
		ArrayList<ArrayList<Student>> tmpArrayLists = getIntevalScoreInfo(courseId, classId);
		/*if(classId == null){
			for(int i=0;i<classNum;i++) {
					tmpClass = this.classTable.get(i);
			}
		}else{
			for(int i=0;i<classNum;i++) {
				if(this.classTable.get(i).classId.equals(classId)){
					tmpClass = this.classTable.get(i);
				}
			}
		}
		if(tmpClass != null){
			tmpArrayLists = tmpClass.sumIntervalScoreStudent(courseId);
		}else{
			return null;
		}*/
		if(courseId == null){
			if(classId != null)
				result+=classId;
			result += "班总分分数区间：\n";
		} else{
			if(classId != null)
				result+=classId;
			result += "班" + courseId + "科目分数区间：\n";
		}
		for(int i=0;i<5;++i) {
			if(mode < 0){
				switch (i) {
				case 0:
					result+=">90分段 总计："+tmpArrayLists.get(i).size()+"人\n";
					break;
				case 1:
					result+="80-89分段 总计："+tmpArrayLists.get(i).size()+"人\n";
					break;
				case 2:
					result+="70-79分段 总计："+tmpArrayLists.get(i).size()+"人\n";
					break;
				case 3:
					result+="60-69分段 总计："+tmpArrayLists.get(i).size()+"人\n";
					break;
				case 4:
					result+="<60分段 总计："+tmpArrayLists.get(i).size()+"人\n";
					break;
				default :
						break;
				}
			}else{
				switch (mode) {
				case 0:
					result+=">90分段 总计："+tmpArrayLists.get(mode).size()+"人\n";
					break;
				case 1:
					result+="80-89分段 总计："+tmpArrayLists.get(mode).size()+"人\n";
					break;
				case 2:
					result+="70-79分段 总计："+tmpArrayLists.get(mode).size()+"人\n";
					break;
				case 3:
					result+="60-69分段 总计："+tmpArrayLists.get(mode).size()+"人\n";
					break;
				case 4:
					result+="<60分段 总计："+tmpArrayLists.get(mode).size()+"人\n";
					break;
				default :
						break;
				}
				for(int j=0;j<tmpArrayLists.get(mode).size();j++) {
					result+=tmpArrayLists.get(mode).get(j).getSpeceficInfo(courseId);
				}
				break;
			}
			for(int j=0;j<tmpArrayLists.get(i).size();j++) {
				result+=tmpArrayLists.get(i).get(j).getSpeceficInfo(courseId);
			}
		}
		return result;
	}
	public String searchStudent(String Id) {
		for(int i=0;i < this.classTable.size();i++) {
			Class tmpClass = this.classTable.get(i);
			for(int j=0;j<tmpClass.stuTable.size();j++) {
				if(tmpClass.stuTable.get(j).Id.equals(Id)) {
					return tmpClass.stuTable.get(j).getSpeceficInfo(null);
				}
			}
		}
		return null;
	}
	
	
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
	public void input(Scanner sc) {    
		System.out.println("-----------------------------学生信息查询系统--------------------------------");
		System.out.println("功能：");
		System.out.println("0.生成学生信息 1. 生成考试安排 2.查看学生信息 3.统计学生信息 4.查找学生信息 5.退出");
		boolean inputGo = true;
		int mode = 0;
		
		for(;inputGo;){
			System.out.print("请选择功能：");
			mode = inputMisc(sc);
			switch (mode) {
			case 0:
				System.out.println("1.随机生成 2.文件导入 3.返回");
				System.out.print("请输入：");
				mode = inputMisc(sc);
				switch (mode) {
				case 1:
					this.generateStuInfo("/home/wc/out.txt");
					this.loadClassInfo("/home/wc/TEST/out.txt");
					System.out.println("随机生成成功,信息保存在/home/wc/TEST/out.txt里");
					break;
				case 2:
					System.out.print("请输入导入文件路径:");
					String pathString = sc.next();
					this.loadClassInfo(pathString);
					System.out.println("课程信息导入成功。");
					break;
				case 3:
					break;
				default:
					System.out.println("输入错误，已返回到上一级。");
					break;
				}
				break;
			case 1:
				this.showManageMent();
				break;
			case 2:
				System.out.println("1.输出全体学生信息 2.分班级输出学生信息 3.指定班级输出信息 4.返回上一级");
				mode = inputMisc(sc);
				switch (mode) {
				case 1:
					System.out.println(this.showAllInfoByCourseScore(null));
					break;
				case 2:
					System.out.println(this.showAllInfo());
					break;
				case 3:
					System.out.print("输入班级名：");
					String className = sc.next();
					String classInfo = this.showSpecificInfoByClass(className);
					if(classInfo == null){
						System.out.println("班级名不存在。");
					}else{
						System.out.print(classInfo);
					}
					break;
				case 4:
					break;
				default:
					System.out.print("输入错误，已返回上一级。");
					break;
				}
				break;
			case 3:
				String courseName = null;
				System.out.println("1.按总分统计分数段信息 2.按某课程统计分数段信息 3.按班级和课程统计分数段信息 4.返回上一级");
				System.out.println("输入选择：");
				mode = inputMisc(sc);
				switch (mode) {
				case 1:
					System.out.print(this.showIntervalInfo(null,null));
					break;
				case 2:
					System.out.print("输入课程名称：");
					courseName = sc.next();
					if(this.courseSet.contains(courseName)){
						System.out.print(this.showIntervalInfo(courseName,null));
					}else{
						System.out.println("课程不存在。");
					}
					break;
				case 3:
					System.out.print("输入课程名称：");
					courseName = sc.next();
					System.out.print("输入班级名：");
					String classId = sc.next();
					if(this.courseSet.contains(courseName)){
						String courseInfo = this.showIntervalInfoBySpecificClass(classId, courseName,-1);
						if(courseInfo == null || courseInfo.isEmpty()){
							System.out.print("班级不存在。");
							break;
						}else{
							System.out.print(courseInfo);
						}
					}else{
						System.out.print("课程不存在。");
					}
					break;
				case 4:
					break;
				default:
					System.out.println("输入错误，已返回主菜单。");
					break;
				}
				break;
			case 4:
				System.out.println("1.总分或某门课程某分数段的信息 2.指定班级总分或某门课程某分数段信息 3.指定学号学生信息 4.返回主菜单");
				System.out.print("请选择：");
				mode = inputMisc(sc);
				String classId = null;
				String course = null;
				switch (mode) {
				case 1:
					System.out.print("输入课程名(如查询总分输入#):");
					course 	=sc.next();
					System.out.print("分数段：0.>90 1.80-89 2.70-79 3.60-69 4.<60");
					System.out.print("输入选择：");
					mode =  inputMisc(sc);
					if(course.equals("#")){
						System.out.print(this.showIntervalInfoBySpecificClass(null, null, mode));
					}else{
						System.out.print(this.showIntervalInfoBySpecificClass(null, course, mode));
					}
					break;
				case 2:
					System.out.print("输入班级名：");
					classId = sc.next();
					System.out.print("输入课程名(如查询总分输入#):");
					course 	=sc.next();
					System.out.print("分数段：0.>90 1.80-89 2.70-79 3.60-69 4.<60");
					System.out.print("输入选择：");
					mode =  inputMisc(sc);
					if(course.equals("#")){
						System.out.print(this.showIntervalInfoBySpecificClass(classId, null, mode));
					}else{
						System.out.print(this.showIntervalInfoBySpecificClass(classId, course, mode));
					}
					break;
				case 3:
					System.out.print("输入学号：");
					String Id = sc.next();
					System.out.println(this.searchStudent(Id));
					break;
				default:
					break;
				}	
				break;
			case 5:
				inputGo = false;
				break;
			default:
				break;
			}
			if(inputGo){
			System.out.println("--------------------------------------------------------------------------" +
					"--------------------------------------------------------------------");
			System.out.println("0.生成学生信息 1. 生成考试安排 2.查看学生信息 3.统计学生信息 4.查找学生信息 5.退出");
		}	
		}
	}
	public static void main(String[] args) {
		Scanner scanner = new Scanner(System.in);
		new ClassExamManager().input(scanner);
		scanner.close();
		//new ClassExamManager("/home/wc/TEST/out.txt").showManageMent();
	}

}
