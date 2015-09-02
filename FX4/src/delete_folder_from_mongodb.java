import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.LinkOption;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.attribute.BasicFileAttributes;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;

import com.mongodb.BasicDBObject;
import com.mongodb.DB;
import com.mongodb.DBObject;
import com.mongodb.Mongo;
import com.mongodb.MongoClient;
import com.mongodb.gridfs.GridFS;
import com.mongodb.gridfs.GridFSDBFile;
import com.mongodb.gridfs.GridFSInputFile;

import javafx.application.Application;
import javafx.event.EventHandler;
import javafx.scene.Group;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.ListView;
import javafx.scene.input.DragEvent;
import javafx.scene.input.Dragboard;
import javafx.scene.input.MouseEvent;
import javafx.scene.input.TransferMode;
import javafx.stage.Stage;


public class delete_folder_from_mongodb extends Application {
	
	 static int level_file = 0; 
	 static String current_Path = " " ;
	 static String parent_folder_name= "root";
	 static String status;
	 /*Bacward and forward option*/
	 static List<String> myList = new ArrayList<String>();
	 static List<String> forward_list = new ArrayList<String>();
	 static HashMap<String, Integer> hmap_to_store_folder_with_level_when_delet_folder = new HashMap<String,Integer>();
	 static List<String> list_all_folder_in_a_folder_when_delete_folder =new ArrayList<String>();
	 /*For Folder copy */
		static List<String> list = new ArrayList<String>();
		static HashMap<String, Integer> hmap = new HashMap<String,Integer>();
		
		 public static String getFileExtension(String  fileName) {
		        if(fileName.lastIndexOf(".") != -1 && fileName.lastIndexOf(".") != 0)
		        return fileName.substring(fileName.lastIndexOf(".")+1);
		        else return "";
		    }
		 public static void delete_folder_hiearchy( GridFS photo , int temp_level ,String folder_name){
			  temp_level = temp_level + 1;
				DBObject query;
				query = new BasicDBObject().append("metadata.level",temp_level).append("metadata.parent_folder_name", folder_name);
				List<GridFSDBFile> files = photo.find(query);
				for (GridFSDBFile file : files) { 
					String type = file.getMetaData().get("type").toString();
					if(type.equals("file")){
						System.out.println("File Name is  "+ file.getFilename());
					}
					else{
						list_all_folder_in_a_folder_when_delete_folder.add(file.getFilename());
						hmap_to_store_folder_with_level_when_delet_folder.put(file.getFilename(), temp_level);
						//System.out.println("Folder Name is  "+ file.getFilename()  + "Hmap contentent is "  + hmap_to_store_folder_with_level_when_delet_folder);
						//delete_folder_hiearchy(photo,temp_level,file.getFilename());
					}
					
		        		        	
		        }
				if(hmap_to_store_folder_with_level_when_delet_folder.isEmpty()){								
					System.out.println("Its main no folder is left to save when saving a folder ");
				}
				else{
					String next_temp_folder_to_save_when_delete_folder_hieachy = list_all_folder_in_a_folder_when_delete_folder.get(list_all_folder_in_a_folder_when_delete_folder.size()-1);
					//System.out.println("next folder to delete name " + next_temp_folder_to_save_when_delete_folder_hieachy);
					File temp_folder  = new File(next_temp_folder_to_save_when_delete_folder_hieachy);				
					list_all_folder_in_a_folder_when_delete_folder.remove(list_all_folder_in_a_folder_when_delete_folder.size()-1);
					int next_temp_folder_level= hmap_to_store_folder_with_level_when_delet_folder.get(next_temp_folder_to_save_when_delete_folder_hieachy);
					hmap_to_store_folder_with_level_when_delet_folder.remove(next_temp_folder_to_save_when_delete_folder_hieachy);
					System.out.println("temp folder =  " + temp_folder  +  " next temp folder level = " + next_temp_folder_level + " hmap" + hmap_to_store_folder_with_level_when_delet_folder); 
					delete_folder_hiearchy(photo, next_temp_folder_level,next_temp_folder_to_save_when_delete_folder_hieachy);
					
//					saving_directory_hieachy(temp_folder, list_view_to_show_file, photo, date, dateFormat, next_temp_folder_level, temp_folder.getName().toString());			
				}
				
		 }
		 public static void displayDirectoryContents(File dir , ListView<String> list_view_to_show_file , GridFS photo, Date date,DateFormat dateFormat ,int temp_level,String temp_parent) {
			 File[] files = dir.listFiles();
				System.out.println(files.length);
				for(int i =0;i<files.length;i++){
					
					if(files[i].isDirectory()){
						//save all directory and add on list item
						 DBObject obj = new BasicDBObject();
	               		obj.put( "path", current_Path+ "//" + files[i].getName());
	               		
	               		obj.put("current_Path", current_Path);      
	               		obj.put("created_date", dateFormat.format(date));
	               		obj.put("modified_date", dateFormat.format(date));
	               		obj.put("type", "folder");
	               		obj.put("level", temp_level);
	               		obj.put("level_path", "//"+files[i].getName());
	               		obj.put("parent_folder_name", temp_parent);                     		
	               	    obj.put("extension" ,"no");
						System.out.println(" folder name " + files[i].getName());
						GridFSInputFile input;
	              		File image_file = new File("c:\\ali.txt");
	              		try {
							input = photo.createFile(image_file);
							input.setFilename(files[i].getName());
							input.setMetaData(obj);
		                   	input.save();
						} catch (IOException e) {
							// TODO Auto-generated catch block
							e.printStackTrace();
						}
						list.add(files[i].getAbsolutePath());
						hmap.put(files[i].getAbsolutePath(), temp_level);
					}
					else if(files[i].isFile()){
						//Save files only with level this 
						String extension_of_file = getFileExtension(files[i].getName());
		                   //	 list_view_to_show_file.getItems().add(file.getName());		                        	 
		                   	 DBObject obj = new BasicDBObject();
		                		obj.put( "path", current_Path+ "/"+files[i].getName());
		                		obj.put("current_Path", current_Path);      
		                		obj.put("created_date", dateFormat.format(date));
		                		obj.put("modified_date", dateFormat.format(date));
		                		obj.put("type", "file");
		                		obj.put("level", temp_level);
		                		obj.put("level_path", "/"+ files[i].getName());
		                		obj.put("extension" , extension_of_file);
		                		obj.put("parent_folder_name", temp_parent);
		                		 //Creates a GridFS instance for the specified bucket in the given database.
		                		GridFSInputFile input;
		                				                     		
		                   	    try {
									input = photo.createFile(files[i]);
									//input.setContentType("png");
									input.setFilename(files[i].getName()); //set filename 
									input.setContentType(extension_of_file);
									 input.setMetaData(obj);
			                     	input.save();
								System.out.println("     file:" + files[i].getCanonicalPath());
								} catch (IOException e) {
									// TODO Auto-generated catch block
									e.printStackTrace();
								}	
		                   	   
		                    	
						System.out.println(" File name is "  + files[i].getName());
					}
					
				}
				
				
				if(list.isEmpty()){
					
					System.out.println("ENd");
				}
				else{
					String temp_folder1 = list.get(list.size()-1);
					File temp_folder  = new File(temp_folder1);
					//System.out.println("Temp folder value "  + temp_folder1);
					
					list.remove(list.size()-1);
					//System.out.println("List value " + list);
					//System.out.println("Hmap value is " +hmap.get(temp_folder1));
					int level1 = hmap.get(temp_folder1) + 1;
					
					//System.out.println(level1);
					hmap.remove(temp_folder1);
					displayDirectoryContents(temp_folder, list_view_to_show_file, photo, date, dateFormat, level1, temp_folder.getName().toString());
					
					//System.out.println("AFter remove Hmap we get " + hmap);
				//displayDirectoryContents(temp_folder,level1);
				}
			
			}

	public static void main(String[] args) {
		launch(args);

	}

	@Override
	public void start(Stage stage) throws Exception {
		Button btn = new Button("Delete FOlder");
		
		ListView<String> list_view_to_show_file = new ListView<String>();
		list_view_to_show_file.setEditable(true);
		 Mongo mongo = new MongoClient("localhost",27017);	
			DB db1 = mongo.getDB("fyp1");
		    GridFS photo = new GridFS(db1,"photo"); 
		Group root = new Group();
		Scene scene = new Scene(root,500,500);
		root.getChildren().addAll(list_view_to_show_file);
		stage.setScene(scene);
		stage.show();
		btn.setOnMouseClicked(new EventHandler<MouseEvent>() {

			@Override
			public void handle(MouseEvent arg0) {
				String folder_name= "temp";
				delete_folder_hiearchy(photo,level_file,folder_name);
				
			}

			
		});
		 scene.setOnDragOver(new EventHandler<DragEvent>() {
	            public void handle(DragEvent event) {
	            	//System.out.println("On drag over ");
	                Dragboard db = event.getDragboard();
	                if (db.hasFiles()) {
	                    event.acceptTransferModes(TransferMode.COPY);
	                } else {
	                    event.consume();
	                }
	            }
	        });
		 
		 
		  scene.setOnDragDropped(new EventHandler<DragEvent>() {
	            @Override
	            public void handle(DragEvent event) {
	         //   	System.out.println("Drpped");
	            	 Dragboard db = event.getDragboard();
	                 boolean success = false;
	                 if (db.hasFiles()) {
	                     success = true;
	                     String filePath = null;
	                     	                     
	                     for (File file:db.getFiles()) {
	                         filePath = file.getAbsolutePath();
	                        
	                         System.out.println(filePath);
	                       //Creates a GridFS instance for the specified bucket in the given database.
	                         File imageFile = new File(filePath);
	                         Path path_of_file = Paths.get(filePath);
	                         BasicFileAttributes attr;
							try {
								attr = Files.readAttributes(path_of_file, BasicFileAttributes.class, LinkOption.NOFOLLOW_LINKS);
								DateFormat dateFormat = new SimpleDateFormat("yyyy/MM/dd HH:mm:ss");
								Date date = new Date();
							
								
							
		                          if (attr.isDirectory()){
		                        	 list_view_to_show_file.getItems().add(imageFile.getName());
		                        	 DBObject obj = new BasicDBObject();
			                     		obj.put( "path", current_Path+ "//" + imageFile.getName());
			                     		
			                     		obj.put("current_Path", current_Path);      
			                     		obj.put("created_date", dateFormat.format(date));
			                     		obj.put("modified_date", dateFormat.format(date));
			                     		obj.put("type", "folder");
			                     		obj.put("level", level_file);
			                     		obj.put("level_path", "//"+ imageFile.getName());
			                     		obj.put("parent_folder_name", parent_folder_name);                     		
			                     	    obj.put("extension" ,"no");
			                     	   GridFSInputFile input;
			                     		File image_file = new File("c:\\ali.txt");	
			                     		try{
			                     		input = photo.createFile(image_file);
										input.setFilename(imageFile.getName());
										input.setMetaData(obj);
					                   	input.save();
			                     		}
			                     		 catch (IOException e) {
												// TODO Auto-generated catch block
												e.printStackTrace();
											}	
			                     		int a = level_file + 1;
		                        	 displayDirectoryContents(imageFile,list_view_to_show_file,photo,date,dateFormat,a,imageFile.getName().toString());
		                        	 
		                         }
		                       
		                         
							} catch (IOException e) {
								// TODO Auto-generated catch block
								e.printStackTrace();
							}
	                       
             	
	                     }
	                                            
	                     
	                 }
	                
	            }
	        });
		
	}

}
