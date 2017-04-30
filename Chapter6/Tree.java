package com.qinggniq.homework.FileCompression;

import java.nio.file.attribute.AclEntry.Builder;
import java.security.KeyStore.Entry;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;
import java.util.PriorityQueue;
import java.util.Set;

import com.qinggniq.homework.FileCompression.*;


public class Tree {
	Node rootNode;
	
	public Tree() {
		this.rootNode = new Node((byte) 0, 0L);
	}
	
	public static void main(String[] args) {
		HfTree ht = new HfTree("/home/wc/mygit/README.md");
		//HfTree ht1 = new HfTree(ht.getCodeDic());
		FileInput fi = new FileInput("/home/wc/mygit/README.md");
		//System.out.println(ht1.getByteByCode("110"));
		System.out.println(ht.getCodeDic());
		System.out.println(ht.getByteByCode("110"));
	}
}

class HfTree extends Tree{
	FileInput fileInput = null; 
	Map<Byte, String> codeDic = null;
	PriorityQueue<Node> queue = null;
	
	public HfTree(String path) {
		super();
		this.buildTree(path);
		codeDic = new HashMap<Byte, String>();
		this.codeTree(this.rootNode,"");
	}
	
	/*public HfTree(Map<Byte, String> dic) {
		super();
		codeDic = dic;
		this.buildTree(this.rootNode,dic);
	}*/
	public HfTree(Map<Byte, Long> dic) {
		super();
		buildTree(dic);
	}
	public void buildTree(String path) {
		fileInput = new FileInput(path);
		Map<Byte,Long> frequeDic = fileInput.getDic();
		if(frequeDic == null)
			return;
		buildTree(frequeDic);
	}
	public void buildTree(Map<Byte, Long> frequeDic) {
		queue = new PriorityQueue<Node>();
		Set<java.util.Map.Entry<Byte,Long>> mapSet = frequeDic.entrySet();
		for(Iterator<java.util.Map.Entry<Byte,Long>> it = mapSet.iterator();it.hasNext();) {
			@SuppressWarnings("unchecked")
			Map.Entry<Byte,Long> tmp = (java.util.Map.Entry<Byte,Long>) it.next();
			queue.add(new Node(tmp.getKey(),tmp.getValue()));
		}
		
		for(int i=0;i < frequeDic.size()-1;i++) {
			Node firstNode = queue.remove();
			Node secondNode = queue.remove();
			queue.add(new Node(firstNode,secondNode));
		}
		this.rootNode = queue.remove();
	}
	
	public void buildTree(Node root,Map<Byte, String> dic) {
		Set<java.util.Map.Entry<Byte,String>> mapSet = dic.entrySet();
		for(Iterator<java.util.Map.Entry<Byte,String>> it = mapSet.iterator();it.hasNext();) {
			@SuppressWarnings("unchecked")
			Map.Entry<Byte,String> tmp = (java.util.Map.Entry<Byte,String>) it.next();
			String code = tmp.getValue();
			Node tmpNode = this.rootNode;
			
			for(int i=0;i < code.length() - 1 ; i++) {
				if(code.charAt(i) == '0') {
					tmpNode.setLChild(new Node());
					tmpNode = tmpNode.getLChild();
				} else {
					tmpNode.setRChild(new Node());
					tmpNode = tmpNode.getRChild();
				}
			}
			if(code.charAt(code.length()-1) == '0'){
				tmpNode.setLChild(new Node(tmp.getKey(),0L));
			} else {
				tmpNode.setRChild(new Node(tmp.getKey(),0L));
			}	
		}
	}
	
	public void codeTree(Node root,String code) {
		if( root == null ) {
			return;
		}
		else if(root.isLeave() ){
			this.codeDic.put(root.getbyteCode(), code);
		}else{
			codeTree(root.leftNode, code + "0");
			codeTree(root.rightNode,code + "1");
		}
	}
	public void getAllNode(Node root ) {
		if(root == null)
			return;
		if(root.isLeave()	)
			System.out.println(root.getbyteCode() + ":" +root.getWeight());
		getAllNode(root.leftNode);
		getAllNode(root.rightNode);
	}
	public Node getRoot() {
		return this.rootNode;
	}
	public Map<Byte, String> getCodeDic() {
		
		return this.codeDic;
	}
	
	public byte getByteByCode(String code) {
		Node tmpNode = this.rootNode;
		for(int i=0;i < code.length();i++) {
			if(code.charAt(i) == '0') {
				tmpNode = tmpNode.leftNode;
			}else{
				tmpNode = tmpNode.rightNode;
			}
		}
		return tmpNode.getbyteCode();
	}
}




class Node implements Comparable<Node>{
	Byte byteCode = '\0' ;
	Long weight;
	Node leftNode = null,rightNode=null;
	boolean isLeave = true;
	public Node() {
		this.byteCode = '\0';
		this.weight = 0L;
		this.isLeave = false;
	}
	public Node(Byte byteCode,Long weight) {
		this.byteCode = byteCode;
		this.weight = weight;
		this.isLeave = true;
	}
	
	public Node(Node lchild,Node rchild) {
		this.byteCode = '\0';
		this.weight = lchild.weight + rchild.weight;
		this.leftNode = lchild;
		this.rightNode = rchild;
		this.isLeave = false;
	}
	public Long getWeight() {
		return this.weight;
	}
	public byte getbyteCode() {
		return this.byteCode;
	}
	public void setLChild(Node lchild) {
		if(this.leftNode == null)
			this.leftNode = lchild;
	}
	public Node getLChild() {
		return this.leftNode;
	}
	public void setRChild(Node rchild) {
		if(this.rightNode == null)
			this.rightNode = rchild;
	}
	public Node getRChild() {
		return this.rightNode;
	}
	@Override
	public int compareTo(Node arg0) {
		// TODO Auto-generated method stub
		
		return this.weight > arg0.getWeight() ? 1 :
			(this.weight == arg0.getWeight() ? 0 : -1);
	}

	public boolean isLeave() {
		return this.isLeave;
	}
}
