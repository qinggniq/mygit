package com.qinggniq.homework.STManager;

public class GeneralTable {
	Node headNode = null;
	
	public GeneralTable(String info) {
		
	}
	 
	public static void main(String[] args) {
		// TODO Auto-generated method stub

	}

}

class Node{
	enum NodeType {TableNode,NormalNode};
	NodeType nodeType = NodeType.TableNode;	
	String Name = null;
	Node ptrHead = null;
	Node ptrTail = null;
	
	public Node(String name) {
		nodeType = NodeType.NormalNode;
		this.Name = name;
	}
	public Node(Node ptrHead,Node ptrTail) {
		this.nodeType = NodeType.TableNode;
		this.ptrHead = ptrHead;
		this.ptrTail = ptrTail;
	}
	public Node getHead() {
		if(isTable())
			return this.ptrHead;
		else {
			return null;
		}
	}
	public Node getTail() {
		if(isTable()){
			return this.ptrTail;
		}else{
			return null;
		}
	}
	public String getinfo() {
		if(!isTable()){
			return Name;
		}else{
			return null;
		}
	}
	public boolean isTable() {
		return this.nodeType == NodeType.TableNode ? true:false;
	}
}