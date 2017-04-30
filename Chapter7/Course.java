package com.qinggniq.homework.Course;

import java.awt.List;
import java.util.ArrayList;
import java.util.Set;

public class Course {

	String courseId = null;
	String courseName = null;
	int stuScroce = 0;
	Set<String> preCourseIdTable = null;
	
	public Course(String courseId,String courseName,int stuScore,Set<String> preCourseTable) {
		this.courseId = courseId;
		this.courseName = courseName;
		this.stuScroce = stuScore;
		this.preCourseIdTable = preCourseTable;
	}
	public static void main(String[] args) {
		
	}

}
