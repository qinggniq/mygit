package com.qinggniq.homework.FileCompression;

import java.io.BufferedInputStream;
import java.io.BufferedOutputStream;
import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.nio.file.attribute.DosFileAttributes;
import java.util.HashMap;
import java.util.InputMismatchException;
import java.util.Map;
import java.util.Scanner;

import javax.print.attribute.standard.Compression;

import com.qinggniq.homework.FileCompression.*;

public class FileCompression {
	private Map<Byte, Long> getDicFromFile(String ss) {
		
		Map<Byte, Long> resMap = new HashMap<Byte,Long>();
		try{
			File freCodeFile = new File(ss);
			@SuppressWarnings("resource")
			FileInputStream fi = new FileInputStream(freCodeFile);
			String mapString = "";
			int tmpChar = 0;
			while ((tmpChar = fi.read()) != '\n' && tmpChar != -1) {
				mapString+=(char)tmpChar;
			}
			
			mapString = mapString.substring(1, mapString.length()-1);
			String[] tmpsStrings = mapString.split(",");
			for(int i=0;i<tmpsStrings.length;i++){
				//System.out.println(tmpsStrings[i]);
				String[] partElem = tmpsStrings[i].split("=");
				resMap.put(Byte.parseByte(partElem[0].trim()),Long.parseLong(partElem[1].trim()));
			}
			fi.close();
		}catch (IOException e) {
			e.printStackTrace();
			System.out.println("译码本读取错误。");
		}
		return resMap;
	}
	
	public void compression(String inputFilePath,String outputFilePath) {
			
			FileInput fin = new FileInput(inputFilePath+".txt");
			File outputCodeFile = new File(outputFilePath+"--HufCode.txt");
			File outputFreFile = new File(outputFilePath+"--HufTree.dat");
			HfTree hfTree  = new HfTree(inputFilePath+".txt");
			Map<Byte, String> codeDicMap = hfTree.getCodeDic();
		try {
			File inputFile = new File(inputFilePath+".txt");
			File outFile = new File(outputFilePath+".dat");
			BufferedOutputStream bof = new BufferedOutputStream(new FileOutputStream(outFile));
		
			BufferedOutputStream bcof = new BufferedOutputStream(new FileOutputStream(outputCodeFile));
			BufferedOutputStream bfof = new BufferedOutputStream(new FileOutputStream(outputFreFile));
			BufferedInputStream bif = new BufferedInputStream(new FileInputStream(inputFile));
			//DataOutputStream dos = new DataOutputStream(bof);
			//System.out.println(codeDicMap);
			bfof.write(fin.getDic().toString().getBytes());
			bfof.write('\n');
			
			bfof.close();
			bcof.write(hfTree.getCodeDic().toString().getBytes());
			bcof.close();
			
			long fileLenght = inputFile.length();
			System.out.println(fileLenght);
			DataOutputStream dos = new DataOutputStream(bof);
			dos.writeLong(fileLenght);
			Byte inByte = 0;
			Byte outByte = 0;
			Byte initByte = 0;
			int ipos = 0;
			int opos = 0;
			String code = "";
			int count = 0;
			for(;;) {
				//System.out.println("555555555");
				
				if(ipos == 0){
					inByte = (byte)bif.read();
					if(count == 0){
						initByte = inByte;
						code = codeDicMap.get(inByte);
					}else if(count == fileLenght){
						code = codeDicMap.get(initByte);
					} else if(count == fileLenght+1){
						bof.write(outByte);
						break;
					} else{
						code = codeDicMap.get(inByte);
					}
					count++;
					
				}
				
				int i = 0 ;
				for(i=ipos;i < code.length() && opos < 8;i++) {
					if(code.charAt(i) == '1') {
						outByte = (byte) ( (1 << (7 - opos)) | outByte);		
					}
					ipos++;
					opos++;
				}
				//System.out.println(outByte);
				if(i == code.length()) {
					ipos = 0;
				}			
				if(opos == 8)  {
					bof.write(outByte);
					outByte = 0;
					opos = 0;
				}		
			}
			//System.out.println("++ " + count);
			bof.write('\n');	
			bif.close();
			bof.close();
			System.out.println("文件压缩成功，压缩率为:"+(float)outFile.length()/fileLenght*100+"%");
		} catch (IOException e) {
			//e.printStackTrace();
			System.out.println("压缩发生错误。");
		}
		
	}
	public void depression(String inputFilePath) {
		try {
			File inputFile = new File(inputFilePath+".dat");
			File outputFile = new File(inputFilePath + ".txt");
			FileInputStream bis = new FileInputStream(inputFile);
			FileOutputStream bos = new FileOutputStream(outputFile);
			
			Map<Byte, Long> dic = this.getDicFromFile(inputFilePath+"--HufTree.dat");
			HfTree tree = new HfTree(dic);
			
			DataInputStream dis = new DataInputStream(bis);
			long fileLength = dis.readLong();
			
			byte inByte = 0;
			Node tmpNode = tree.getRoot();
			int count = 0;
			for(;;) {
				inByte = (byte) bis.read();
				if(fileLength == count) {
					break;
				}else {
					for(int i=0;i< 8;i++){
						boolean right = (inByte & (1 << (7-i) )) == 0 ? false:true;
						if(right) {
							tmpNode = tmpNode.getRChild();
							//System.out.print(1);
							if(tmpNode.isLeave() ){
								count++;
								//System.out.println("--*" + tmpNode.getbyteCode());
								bos.write(tmpNode.getbyteCode());
								tmpNode = tree.getRoot();
							}
						}else{
							//System.out.print(0);
							tmpNode = tmpNode.getLChild();
							if(tmpNode.isLeave() ){
								count++;
								//System.out.println("--*" + tmpNode.getbyteCode());
								bos.write(tmpNode.getbyteCode());
								tmpNode = tree.getRoot();
							}
						}
					}
				}
			}
			System.out.println("解压成功");
			bis.close();
			bos.close();
		} catch (IOException e) {
			//e.printStackTrace();
			System.out.println("文件打开失败！");
		}
	}
	
	public void input() {
		System.out.println("------------------------------------------哈夫曼文本压缩----------------------------------");
		System.out.println("--说明--");
		System.out.println("1.只能压缩文本文件(压缩视频图片效果很差),请保证文本文档后缀为txt。");
		System.out.println("2.由于本程序的局限，请保证要压缩的文档大小大于1个字节。");
		System.out.println("3.压缩时默认为'txt'后缀’，输入时请不要加txt后缀，否则会导致文件打开失败。");
		System.out.println("4.解压时默认为‘dat’后缀‘，输入解压文件明时不要加’txt‘后缀。");
		System.out.println("\n\n--功能--：");
		System.out.println("\t1.压缩\t\t2.解压\t\t3.离开");
		
		boolean goo = true;
		for(;goo;) {
			int mode;
			//System.out.print("\r");
			System.out.println("------------------------------------------我是分割线----------------------------------");
			System.out.println("\n请选择功能：");
			Scanner sac = new Scanner(System.in);
			try{
				mode = sac.nextInt();
			}catch (InputMismatchException e){
				sac.next();
				System.out.print("输入错误，请重新输入");
				continue;
			}
			String inpath = null;
			String opath = null;
			switch (mode) {
			case 1:
				System.out.print("请输入文件路径:");
				inpath = sac.next();
				System.out.print("请输入要保存的压缩文件名:");
				opath = sac.next();
				this.compression(inpath.trim(), opath.trim());
				break;
			case 2:
				System.out.print("请输入文件路径:");
				inpath = sac.next();
				this.depression(inpath.trim());
				break;
			case 3:
				goo = false;
				sac.close();
				System.out.println("谢谢使用！！");
				break;
			default:
				System.out.println("请输入正确选项");
				break;
			}
			
		}
		
}
	public static void main(String[] args) {
		FileCompression fc = new FileCompression();
		fc.input();
		//fc.compression("/home/wc/TEST/source", "/home/wc/TEST/output");
		//fc.depression( "/home/wc/TEST/output");
	}

}
