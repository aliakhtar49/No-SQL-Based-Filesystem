package Images;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.RandomAccessFile;
import java.io.Reader;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.nio.file.StandardOpenOption;

import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;
import org.json.simple.parser.ParseException;

public class file_read_withjava {
	

	public static void main(String[] args) throws IOException, ParseException {
//after import filr to json then make a single file 
//		JSONParser parser = new JSONParser();
//		Object obj = parser.parse(new FileReader("C:\\a\\domain-bk.json"));
//		JSONObject jsonObject = (JSONObject) obj;		
//		Object obj1 = parser.parse(new FileReader("C:\\a\\domain-bk1.json"));
//		JSONObject jsonObject1 = (JSONObject) obj1;
//		JSONObject final_create = new JSONObject();
//		final_create.put("import_chunks", jsonObject);
//		final_create.put("import_type", "file");
//		final_create.put("import_files", jsonObject1);
//		 FileWriter file = new FileWriter("C:\\a\\test.json");
//		 file.write(final_create.toJSONString());
//		 file.flush();
//         file.close();
         
         
         JSONParser parser1 = new JSONParser();
 		Object test_file = parser1.parse(new FileReader("C:\\a\\test.json"));
 		JSONObject test_file_json = (JSONObject) test_file;
 		//System.out.println(test_file_json);
 		String filename = test_file_json.get("import_type").toString();
 		if(filename.equals("file")){
 			Object import_files = new JSONObject();
 			import_files = test_file_json.get("import_files");
 			Object import_chunks = new JSONObject();
 			import_chunks = test_file_json.get("import_chunks");
 			 FileWriter file = new FileWriter("C:\\a\\import_files.json");
 			 file.write(import_files.toString());
 			 file.flush();
 	         file.close();
 	         
 	        FileWriter file1 = new FileWriter("C:\\a\\import_chunks.json");
			 file1.write(import_chunks.toString());
			 file1.flush();
	         file1.close();
 			
 			System.out.println(import_chunks);
 		}
 		
		
//		JSONObject obj3=new JSONObject();
//		  obj3.put("name","foo");
//		  obj3.put("num",new Integer(100));
//		  obj3.put("balance",new Double(1000.21));
//		  obj3.put("is_vip",new Boolean(true));
//		  obj3.put("nickname",null);
//		  obj3.put("new", obj1);
//		  System.out.print(obj3.get("new"));
		  
	}

}
