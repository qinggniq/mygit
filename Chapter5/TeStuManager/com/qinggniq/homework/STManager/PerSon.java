package com.qinggniq.homework.STManager;
import java.awt.List;
import java.util.ArrayList;


public class PerSon {
	String name = null;
	
	public PerSon(String name) {
		this.name = name;
	}
	public String getName() {
		return this.name;
	}
	public static void main(String[] args) {
		// TODO Auto-generated method stub
		Teacher ssStudent = new Teacher("mm", "tt");
		Student ssStudent2 = new Student("Eril","mm","sfs",true);
		ssStudent.addStu(ssStudent2);
		System.out.println(ssStudent.show());
	}

}

class Teacher extends PerSon {
	String level = null;
	ArrayList<Student> stuTable = null;
	int masNum = 0;
	int bacNum = 0;
	public Teacher(String name,String level) {
		super(name);
		this.level = level;
		this.stuTable = new ArrayList<Student>();
	}
	public void addStu(Student stu) {
		this.stuTable.add(stu);
	}
	public String getlevel() {
		return this.level;
	}
	public boolean deleteStu(String name,String classNum) {
		Student student = null;
		String stuName = null;
		String stuClass = null;
		boolean hasStu = false;
		for(int i=0;i<this.stuTable.size();i++){
			student = this.stuTable.get(i);
			stuName = student.getName();
			stuClass = student.getClassNum();
			if(student.canTeach()){
				if(stuName.equals(name) && stuClass.equals(stuClass)){
					this.stuTable.remove(i);
					hasStu = true;
					break;
				}
				if(student.deleteStu(stuName,classNum)){
					hasStu = true;
					break;
				}
			}else{
				if(stuName.equals(name) && stuClass.equals(stuClass)){
					this.stuTable.remove(i);
					hasStu = true;
					break;
				}
			}
		}
		if(hasStu){
			return true;
		}else{
			return false;
		}
	}
	//导师所带学生情况
	public String show() {
		String result = "导师: ";
		result += this.getName() + " 职称：" + this.getlevel() + "\n";
		for(int i=0;i<this.stuTable.size();i++){
			result += this.stuTable.get(i).show();
		}
		return result;
	}
	public boolean changeStu(String name,String classNum,String toStuName) {
		Student toStu = null;
		Student tmpStu = null;
		Student changeStu = null;
		for(int i=0;i<this.stuTable.size();i++){
			tmpStu = this.stuTable.get(i);
			if(tmpStu.getName().equals(toStuName)){
				toStu = tmpStu;
			}
		}
		
		if(toStu==null)
			return false;
		System.out.println(toStu.getName());
		for(int i=0;i<this.stuTable.size();i++){
			tmpStu = this.stuTable.get(i);
			changeStu = tmpStu.getStudent(name, classNum);
			if(changeStu!=null){
				if(changeStu.canTeach()){
					return false;
				}
				break;
			}
		}
		
		if(changeStu!=null){
			System.out.println(changeStu.getName());
			toStu.stuTable.add(changeStu);
			return true;
		}else{
			return false;
		}
	}
	public String query(String name){
		if(name.equals(this.getName())){
			return this.show();
		}else{
			for(int i=0;i<this.stuTable.size();i++){
				String queryRes = this.stuTable.get(i).query(name);
				if(queryRes!=null)
					return queryRes;
			}
		}
		return null;
	}
	private void countNum () {
		Student stu = null;
		for(int i=0;i < stuTable.size();i++) {
			stu = this.stuTable.get(i);
			if(stu.canTeach()){
				this.masNum++;
				this.bacNum+=stu.getStuNum();
			}else {
				this.bacNum++;
			}
		}
	}
	public Student getStudent(String name,String classNum) {
		Student tmpStu = null;
		Student targetStu = null;
		for(int i=0;i<this.stuTable.size();i++){
			tmpStu = this.stuTable.get(i);
			if(tmpStu.getStudent(name, classNum)!=null){
				targetStu = tmpStu.getStudent(name, classNum);
				if(!targetStu.canTeach())
					return targetStu;
				else {
					this.stuTable.remove(i);
					return targetStu;
				}
			}
		}
		return null;
	}
	//导师学生个数情况
	public String showStuNum() {
		this.countNum();
		return	"研究生: " + this.masNum + " 本科生：" + this.bacNum;
	}
}
class Student extends PerSon {
	public enum StuType {Master,Bacher};
	String classNum = null;
	ArrayList<Student> stuTable = null;
	String techName = null;
	StuType stuType = StuType.Bacher;
	boolean canTeach = false;
	
	public Student(String name,String classNum,String techName,boolean canTeach) {
		super(name);
		this.classNum = classNum;
		this.techName = techName;
		this.canTeach = canTeach;
		if(canTeach())
			this.stuTable = new ArrayList<>();
	}
	/*public Student(String name,String teachName,boolean canTeach) {
		super(name);
		this.techName = teachName;
		this.canTeach = canTeach;
		if(canTeach())
			this.stuTable = new ArrayList<>();
	}*/
	public String getClassNum() {
		return this.classNum;
	}
	public String getTeachName() {
		return this.techName;
	}
	boolean canTeach(){
		return canTeach;
	}
	//研究生带本科生情况
	public String show() {
		String result = "";
		
		if(canTeach()) {
			result += "\t研究生：" + this.getName()+"  班级："+this.getClassNum()+"\n";
			for(int i=0;i < stuTable.size();i++){
				result+="\t\t本科生: " + stuTable.get(i).getName()+"  班级： " + stuTable.get(i).getClassNum()+"\n";
			} 
		}else{
			result += "\t本科生：" + this.getName()+"  班级："+this.getClassNum()+"\n";
		}
		return result;
	}
	public int getStuNum() {
		return this.stuTable.size();
	}
	public Student getStudent(String name,String classNum) {
		Student tmp = null;
		if(this.getClassNum().equals(classNum)&&this.getName().equals(name)){
			return this;
		}
		for(int i =0;i<this.stuTable.size();i++){
			tmp = this.stuTable.get(i);
			if(tmp.getName().equals(name)&&tmp.getClassNum().equals(classNum)){
				tmp = this.stuTable.remove(i);
				break;
			}
		}
		return tmp;
	}
	public boolean deleteStu(String name,String classNum) {
		if(!canTeach())
			return false;
		Student student = null;
		String stuName = null;
		String stuClassNum = null;
		boolean hasStu = false;
		for(int i=0;i<this.stuTable.size();i++){
			student = this.stuTable.get(i);
			stuName = student.getName();
			stuClassNum = student.getClassNum();
			if(stuName.equals(name)&&stuClassNum.equals(classNum)){
				this.stuTable.remove(i);
				hasStu = true;
				break;
			}
		}
		if(hasStu){
			return true;
		}else{
			return false;
		}
	}
	public String query(String name) {
		if(name.equals(this.getName())){
			return this.show();
		}
		if(canTeach){
			Student tmpStu = null;
			for(int i=0;i<this.stuTable.size();i++){
				tmpStu = this.stuTable.get(i);
				if(tmpStu.getName().equals(name)){
					return tmpStu.show();
				}
			}
		}else{
			return null;
		}
		return null;
	}
}


