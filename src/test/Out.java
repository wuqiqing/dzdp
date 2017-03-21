package test;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;

public class Out {
	public static File file=null;
    public  static void WriteFileExample(String content,String path) {
    	if(file==null){
    	     file = new File(path);
    	}
		try {
		    FileOutputStream fop = new FileOutputStream(file,true);
			byte[] contentInBytes = content.getBytes();
			fop.write(contentInBytes);
			fop.flush();
			fop.close();
		} catch (IOException e) {
			e.printStackTrace();
		}
}
}
