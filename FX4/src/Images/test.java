package Images;


import java.awt.Desktop;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.io.RandomAccessFile;
import java.net.URL;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;

import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;
import org.json.simple.parser.ParseException;

import com.mongodb.BasicDBObject;
import com.mongodb.DB;
import com.mongodb.DBCollection;
import com.mongodb.DBCursor;
import com.mongodb.DBObject;
import com.mongodb.Mongo;
import com.mongodb.gridfs.GridFS;
import com.mongodb.gridfs.GridFSDBFile;
import com.mongodb.gridfs.GridFSInputFile;

import javafx.application.Application;
import javafx.event.EventHandler;
import javafx.scene.Group;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.input.MouseEvent;
import javafx.scene.media.Media;
import javafx.scene.media.MediaPlayer;
import javafx.stage.FileChooser;
import javafx.stage.Stage;

public class test extends Application{

	public static void main(String[] args) throws IOException {
		launch(args);
		
		
	}

	@Override
	public void start(Stage stage) throws Exception {
		Group root = new Group();
		Scene  scene = new Scene(root,500,500);
		Button btn = new Button("export");
		root.getChildren().addAll(btn);
		
		stage.setScene(scene);
		stage.show();
		btn.setOnMouseClicked(new EventHandler<MouseEvent>() {

			@Override
			public void handle(MouseEvent arg0) {
				
				
//				JSONParser parser = new JSONParser();
//				Object obj_of_files;
//				try {
//					obj_of_files = parser.parse(new FileReader("C:\\a\\myRecords.json"));
//					JSONObject jsonObject = (JSONObject) obj_of_files;
//					Object obj_of_chunks = parser.parse(new FileReader("C:\\a\\myRecords1.json"));
//					JSONObject jsonObject1 = (JSONObject) obj_of_chunks;
//					JSONObject final_create = new JSONObject();
//					final_create.put("import_chunks", jsonObject);
//					final_create.put("import_type", "file");
//					final_create.put("import_files", jsonObject1);
//					FileChooser fileChooser = new FileChooser();
//					 FileChooser.ExtensionFilter extFilter = new FileChooser.ExtensionFilter("JSON files (*.json)", "*.json");
//			         fileChooser.getExtensionFilters().add(extFilter);
//			         
//			         
//			         File file = fileChooser.showSaveDialog(stage);
//			         if(file != null){
//			        	 FileWriter fileWriter = null;
//			             
//				            try {
//								fileWriter = new FileWriter(file);
//								 fileWriter.write(final_create.toString());
//						            fileWriter.close();
//							} catch (IOException e) {
//							
//								e.printStackTrace();
//							}
//				           
//			            
//			         }
//				} catch (FileNotFoundException e1) {
//					
//					e1.printStackTrace();
//				} catch (IOException e1) {
//					// TODO Auto-generated catch block
//					e1.printStackTrace();
//				} catch (ParseException e1) {
//					// TODO Auto-generated catch block
//					e1.printStackTrace();
//				}
//						
				
				
				
		         
		         
//				 FileWriter file = new FileWriter("C:\\a\\test.json");
//				 file.write(final_create.toJSONString());
//				 file.flush();
//		         file.close();
//				
			}
		});
	//	C:\a>mongoexport -d imagedb -c photo.files -q {'filename':'first','metadata.level':'2'} --out myRecords.json
			
//		DateFormat dateFormat = new SimpleDateFormat("yyyy/MM/dd HH:mm:ss");
//		Date date = new Date();
//		Mongo mongo = new Mongo("localhost", 27017);
//		DB db = mongo.getDB("imagedb");
//		DBCollection collection = db.getCollection("photo.chunks");
//		GridFS gfsPhoto = new GridFS(db, "photo");
//		Runtime runtimeObject = Runtime.getRuntime();
//		String a = "photo.files";
//		String first = "\'first\'";
//		String level = "\'2\'"; 
//		String command = "mongoexport.exe -d imagedb -c photo.files -q {'filename':"+ first +",'metadata.level':" + level+ "} --out C:\\a\\myRecords.json";
//		Process p;
//				p = Runtime.getRuntime().exec(command);
		//runtimeObject.exec("C:\\mongodb\\bin\\mongoimport.exe -d imagedb -c photo.files -q {'filename':'first','metadata.level':'2'} --out C:\\a\\myRecords.json");
		
//		DBObject update_file = new BasicDBObject().append("filename", "first").append("metadata.level", "2");
//		
//		GridFSDBFile imageForOutput = gfsPhoto.findOne(update_file);
//		System.out.println(imageForOutput.getId());
//		String command = "mongoexport.exe -d imagedb -c photo.chunks -q {'files_id':ObjectId('"+ imageForOutput.getId() +"')} --out C:\\a\\myRecords1.json";
//		Process p;
//				p = Runtime.getRuntime().exec(command);
		
		
		
		
				// db.photo.chunks.find({"files_id" : ObjectId("55c5906bdaee7c5874f7a4c6")});
//		DBObject update_file1 = new BasicDBObject().append("files_id",imageForOutput.getId());
		
		
		

//		DBObject doc = collection.findOne(update_file1);				
//		System.out.println(doc.get("data"));
		
//		String newFileName = "mkyong-java-image";
//
//		File imageFile = new File("C:\\Users\\dell\\Desktop\\Design JavaFX\\search_image.png");
//		DBObject obj = new BasicDBObject();
// 		obj.put( "path", "\\" );
// 		obj.put("current_Path", "\\");      
// 		obj.put("created_date", dateFormat.format(date));
// 		obj.put("modified_date", dateFormat.format(date));
// 		obj.put("type", "file");
// 		obj.put("level", "2");
// 		obj.put("level_path", "\\ali\\a");
// 		obj.put("extension" , "png");
// 		obj.put("parent_folder_name", "\\a\\b");
// 		GridFSInputFile input;		                     				                     		
//    	input = gfsPhoto.createFile(imageFile);	
//    	input.setMetaData(obj);
//	    input.setFilename("second"); //set filename 
//		input.setContentType("png");
//        input.save();
//
//		// get image file from local drive
//		GridFSInputFile gfsFile = gfsPhoto.createFile(imageFile);
//
//		// set a new filename for identify purpose
//		gfsFile.setFilename(newFileName);
//
//		// save the image file into mongoDB
//		gfsFile.save();
		
	
		
//		DBCursor cursor = gfsPhoto.getFileList();
//		while (cursor.hasNext()) {
//			System.out.println(cursor.next());
//		}
//		String newFileName = "mkyong-java-image";
//	
//		GridFSDBFile imageForOutput = gfsPhoto.findOne(newFileName);
//		imageForOutput.writeTo("c:\\a\\JavaWebHostingNew.png"); 
		
		
		
	}

}
