package com.qinggniq.homework.FileCompression;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.HashMap;
import java.util.Map;

public class FileInput {

	Map<Byte,Long> dic = null;
	String path = null;
	public FileInput(String path) {
		this.path = path;
		this.dic = new HashMap<Byte,Long>();
	}
	
	public Map<Byte,Long> getDic() {
		if(this.processFrequent()){
			return this.dic;
		}else{
			return null;
		}
	}
	
	private boolean processFrequent() {
		File file = new File(this.path);
		InputStream is = null;
		try{
			is = new FileInputStream(file);
			int tmpByte = '\0';
			FileOutputStream fos = new FileOutputStream(new File("/home/wc/OJ/test.txt"));
			while ((tmpByte = is.read()) != -1) {
				this.dic.put((byte) tmpByte, this.dic.get((byte)tmpByte) == null ? 1:this.dic.get((byte)tmpByte)+1);
				fos.write(tmpByte);
			}
			is.close();
			fos.close();

		}catch(IOException e){
			e.printStackTrace();
			return false;
		}
		return true;
	}
	
	public static void main(String[] args) {
		FileInput input = new FileInput("/home/wc/mygit/README.md");
		System.out.println(input.getDic());
	}

}
