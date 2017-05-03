package com.qinggniq.homework.Course;
import java.util.ArrayList;
import java.util.Stack;

import com.qinggniq.homework.Course.*;

public class Graph {
	ArrayList<ArrayList<Node>> graph = null;
	int SIZE = 0;
	public Graph(ArrayList<ArrayList<Node>> ingraph) {
		// TODO Auto-generated constructor stub
		this.graph = ingraph;
		this.SIZE = ingraph.size();
	}
	
	//public Graph() 
	//public void buildNode(ArrayList<E>)
	public boolean getTopoSort(int[] result) {
		
		int curPos = 0;
		int cnt = 0;
		Stack<Node> zeroNodes = new Stack<Node>();
		
		for(int i=0;i < this.SIZE;i++) {
			if(graph.get(i).get(0).indegree == 0){
				zeroNodes.push(graph.get(i).get(0));
				//System.out.println("000--"+graph.get(i).get(0).mark +"--000");
			}
		}
		while(!zeroNodes.isEmpty()) {
			result[curPos++] = zeroNodes.peek().getMark();
			int curMark = zeroNodes.pop().getMark();
			//System.out.println("*** " + curMark + " ***");
			cnt++;
			for(int i=1 ; i < this.graph.get(curMark).size(); ++i) {
				
				if(--this.graph.get(curMark).get(i).indegree == 0){
					zeroNodes.push(this.graph.get(curMark).get(i));
				}
			}
		}
		if(cnt < this.SIZE){
			return false;
		}else{
			return true;
		}
	}
	public static void main(String[] args) {
		
	}

}

class Node {
	int mark = 0;
	int indegree = 0;
 	
	public Node(int mark,int indegree) {
		this.mark = mark;
		this.indegree = indegree;
	}
	public int getMark() {
		return mark;
	}
	public void incIndegree() {
		this.indegree++;
	}
	public void decIndegree() {
		this.indegree--;
	}
}
