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
import java.util.Map;

import javax.print.attribute.standard.Compression;

import com.qinggniq.homework.FileCompression.*;

public class FileCompression {
	private Map<Byte, Long> getDicFromFile(FileInputStream fi) {
		Map<Byte, Long> resMap = new HashMap<Byte,Long>();
		try{
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
		}catch (IOException e) {
			System.out.println("译码本读取错误。");
		}
		return resMap;
	}
	
	public void compression(String inputFilePath,String outputFilePath) {
		FileInput fin = new FileInput(inputFilePath);
		HfTree hfTree  = new HfTree(inputFilePath);
		Map<Byte, String> codeDicMap = hfTree.getCodeDic();
		try {
			File inputFile = new File(inputFilePath);
			File outFile = new File(outputFilePath);
			BufferedOutputStream bof = new BufferedOutputStream(new FileOutputStream(outFile));
			BufferedInputStream bif = new BufferedInputStream(new FileInputStream(inputFile));
			//DataOutputStream dos = new DataOutputStream(bof);
			System.out.println(codeDicMap);
			bof.write(fin.getDic().toString().getBytes());
			bof.write('\n');
			long fileLenght = inputFile.length();
			DataOutputStream dos = new DataOutputStream(bof);
			dos.writeLong(fileLenght);
			
			Byte inByte = 0;
			Byte outByte = 0;
			int ipos = 0;
			int opos = 0;
			String code = "";
			int count = 0;
			for(;;) {
				//System.out.println("555555555");
				if(ipos == 0){
					inByte = (byte)bif.read();
					if(count == fileLenght){
						bof.write(outByte);
						break;
					} else {
						code = codeDicMap.get(inByte);
					}
					count++;
					System.out.print(code);
					System.out.println("***"+inByte);
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
			System.out.println("++ " + count);
			System.out.println("jieshu");
			bif.close();
			bof.close();
		} catch (Exception e) {
			System.out.println("压缩发生错误。");
		}
		
	}
	public void depression(String inputFilePath,String outputFilePath) {
		//deal with the head of file
		try {
			File inputFile = new File(inputFilePath);
			FileInputStream bis = new FileInputStream(inputFile);
			FileOutputStream bos = new FileOutputStream(new File(outputFilePath));
			Map<Byte, Long> dic = this.getDicFromFile(bis);
			HfTree tree = new HfTree(dic);
			
			DataInputStream dis = new DataInputStream(bis);
			long fileLength = dis.readLong();
			System.out.println(dic);
			
			byte inByte = 0;
			Node tmpNode = tree.getRoot();
			int count = 0;
			for(;;) {
				inByte = (byte) bis.read();
				if(fileLength -1 == count) {
					break;
				}else {
					for(int i=0;i< 8;i++){
						boolean right = (inByte & (1 << (7-i) )) == 0 ? false:true;
						if(right) {
							tmpNode = tmpNode.getRChild();
							System.out.print(1);
							if(tmpNode.isLeave() ){
								count++;
								System.out.println("--*" + tmpNode.getbyteCode());
								bos.write(tmpNode.getbyteCode());
								tmpNode = tree.getRoot();
							}
						}else{
							System.out.print(0);
							tmpNode = tmpNode.getLChild();
							if(tmpNode.isLeave() ){
								count++;
								System.out.println("--*" + tmpNode.getbyteCode());
								bos.write(tmpNode.getbyteCode());
								tmpNode = tree.getRoot();
							}
						}
					}
				}
			}
			System.out.println("*  " + count);
			bis.close();
			bos.close();
		} catch (IOException e) {
			System.out.println("?????????");
		}
	}
	public static void main(String[] args) {
		FileCompression fc = new FileCompression();
		fc.compression("/home/wc/org-notes/notes.org", "/home/wc/OJ/test");
		fc.depression( "/home/wc/OJ/test","/home/wc/OJ/out");
	}

}
