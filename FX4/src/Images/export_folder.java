package Images;

import java.io.DataInputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;

import org.json.simple.JSONArray;
import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;
import org.json.simple.parser.ParseException;

import com.mongodb.BasicDBObject;
import com.mongodb.DB;
import com.mongodb.DBObject;
import com.mongodb.MongoClient;
import com.mongodb.gridfs.GridFS;
import com.mongodb.gridfs.GridFSDBFile;

import javafx.application.Application;
import javafx.event.EventHandler;
import javafx.scene.Group;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.input.MouseEvent;
import javafx.stage.FileChooser;
import javafx.stage.Stage;

public class export_folder extends Application{
	
	static int level_file = 0;
	private Stage stage;
	static String parent_folder_name= "root";
	static String current_user_login = "ali";
	static JSONObject final_one = new JSONObject();
	static String path_of_folder_hieachy_when_export = "";
	static HashMap<String, Integer> hmap_to_store_folder_with_level_when_export_folder = new HashMap<String,Integer>();
	 static List<String> list_all_folder_in_a_folder_when_export_folder =new ArrayList<String>();
	 static List<Integer> when_export_folder_contain_total_level = new ArrayList<Integer>();
	
	public void export_folder(GridFS photo , String file_name,int temp_parent_level,Stage stage) throws IOException, ParseException, InterruptedException {
		when_export_folder_contain_total_level.add(temp_parent_level);
		Collections.sort(when_export_folder_contain_total_level); // Sort the arraylist	
		final_one.put("total_level", when_export_folder_contain_total_level.get(when_export_folder_contain_total_level.size() - 1));
		path_of_folder_hieachy_when_export =path_of_folder_hieachy_when_export + "\\" +  file_name;		
		DBObject query = new BasicDBObject().append("metadata.parent_folder_name", file_name).append("metadata.level", temp_parent_level);
		List<GridFSDBFile> files = photo.find(query);		    	
		    if(final_one.containsKey(temp_parent_level)){		    	
		    }
		    else{
		    	
		    	JSONArray create_new_level_chunks_json_array = new JSONArray();		
				JSONArray create_new_level_files_json_array = new JSONArray();
				JSONObject create_new_level_json_object = new JSONObject();
				create_new_level_json_object.put("chunks", create_new_level_chunks_json_array);
				create_new_level_json_object.put("files", create_new_level_files_json_array);
				final_one.put(temp_parent_level, create_new_level_json_object);
		    }
			
			for (GridFSDBFile file : files) { 
				
				String type = file.getMetaData().get("type").toString();
				if(type.equals("file")){	
					String a = file.getFilename().toString().replaceAll("\\s", "").toLowerCase();
					String command_to_make_gridfs_files_export_json_file = "mongoexport.exe -d nosql -c "+ current_user_login+".files -q {'metadata.export_file_name':'"+ a +"','metadata.level':" + temp_parent_level + "} --out C:\\a\\files.json";
					Process process_of_command_to_make_gridfs_files_export_json_file;
					process_of_command_to_make_gridfs_files_export_json_file = Runtime.getRuntime().exec(command_to_make_gridfs_files_export_json_file);
					DBObject query_for_getting_id_to_make_chunks_export_file = new BasicDBObject().append("filename", file.getFilename()).append("metadata.level", temp_parent_level);
					
					GridFSDBFile files_id_to_make_chunks_export_file = photo.findOne(query_for_getting_id_to_make_chunks_export_file);
					String command_to_make_gridfs_chunks_export_json_file = "mongoexport.exe -d nosql -c "+ current_user_login+".chunks -q {'files_id':ObjectId('"+ files_id_to_make_chunks_export_file.getId() +"')} --out C:\\a\\chunks.json";
					Process process_of_command_to_make_gridfs_chunks_export_json_file;
					process_of_command_to_make_gridfs_chunks_export_json_file = Runtime.getRuntime().exec(command_to_make_gridfs_chunks_export_json_file);
					if( process_of_command_to_make_gridfs_chunks_export_json_file.waitFor() == 0){	
						JSONParser parser = new JSONParser();	
						
						DataInputStream dis = 
							    new DataInputStream (
							    	 new FileInputStream ("C:\\a\\files.json"));
							       
							 byte[] datainBytes = new byte[dis.available()];
							 dis.readFully(datainBytes);
							 dis.close();
							       
							 String content = new String(datainBytes, 0, datainBytes.length);
							 
							 DataInputStream dis1 = 
									    new DataInputStream (
									    	 new FileInputStream ("C:\\a\\chunks.json"));
									       
									 byte[] datainBytes1 = new byte[dis1.available()];
									 dis1.readFully(datainBytes1);
									 dis1.close();
									       
									 String content1 = new String(datainBytes1, 0, datainBytes1.length);
							     
							// System.out.println(content);
						
						
//						Object obj_of_files;
//						obj_of_files = parser.parse(new FileReader("C:\\a\\files.json"));
//						JSONObject json_object_of_files = (JSONObject) obj_of_files;
//						Object obj_of_chunks = parser.parse(new FileReader("C:\\a\\chunks.json"));
//						JSONObject json_object_of_chunks = (JSONObject) obj_of_chunks;
									 
									
						JSONObject v = (JSONObject) final_one;				
						JSONObject get_the_level_json = (JSONObject) final_one.get(temp_parent_level);
						JSONArray get_the_chunks_array_to_add_new_chunks = (JSONArray) get_the_level_json.get("chunks");
						//get_the_chunks_array_to_add_new_chunks.add("json_object_of_chunks");
						get_the_chunks_array_to_add_new_chunks.add(content1);
						JSONArray get_the_files_array_to_add_new_files = (JSONArray) get_the_level_json.get("files");	
						//get_the_files_array_to_add_new_files.add(json_object_of_files);
						get_the_files_array_to_add_new_files.add(content);
					}
//					
					//System.out.println("final"+ final_one);
				}
				else{
	               // JSONObject v = (JSONObject) final_one;
					String a = file.getFilename().toString().replaceAll("\\s", "").toLowerCase();
					String command_to_make_gridfs_files_export_json_file = "mongoexport.exe -d nosql -c "+ current_user_login+".files -q {'metadata.export_file_name':'"+ a +"','metadata.level':" + temp_parent_level + "} --out C:\\a\\files.json";
					Process process_of_command_to_make_gridfs_files_export_json_file;
					process_of_command_to_make_gridfs_files_export_json_file = Runtime.getRuntime().exec(command_to_make_gridfs_files_export_json_file);
					//DBObject query_for_getting_id_to_make_chunks_export_file = new BasicDBObject().append("filename", file_name).append("metadata.level", level_file);
//					JSONParser parser = new JSONParser();	
//					Object obj_of_files;
//					obj_of_files = parser.parse(new FileReader("C:\\a\\files.json"));
//					JSONObject export_value_of_json_object_of_files = (JSONObject) obj_of_files;
					 DataInputStream dis2 = 
							    new DataInputStream (
							    	 new FileInputStream ("C:\\a\\files.json"));
							       
							 byte[] datainBytes2 = new byte[dis2.available()];
							 dis2.readFully(datainBytes2);
							 dis2.close();
							       
							 String content2 = new String(datainBytes2, 0, datainBytes2.length);
					JSONObject get_the_level_json_for_folder = (JSONObject) final_one.get(temp_parent_level);
					JSONArray get_the_chunks_array_for_folder = (JSONArray) get_the_level_json_for_folder.get("chunks");
					get_the_chunks_array_for_folder.add("null");	
					JSONArray get_the_files_array_for_folder = (JSONArray) get_the_level_json_for_folder.get("files");	
					get_the_files_array_for_folder.add(content2);
					String save_temp_path_in_list_to_make_it_unique  = path_of_folder_hieachy_when_export + "\\" +file.getFilename();
					list_all_folder_in_a_folder_when_export_folder.add(save_temp_path_in_list_to_make_it_unique);	
					int b = temp_parent_level + 1;
					hmap_to_store_folder_with_level_when_export_folder.put(save_temp_path_in_list_to_make_it_unique, b);
					
				}
			}


		if(hmap_to_store_folder_with_level_when_export_folder.isEmpty()){								
			System.out.println("Its main no folder is left to save when saving a folder ");
			path_of_folder_hieachy_when_export = "";
			
			System.out.println("third"+ final_one);
			FileChooser fileChooser = new FileChooser();
			 FileChooser.ExtensionFilter extFilter = new FileChooser.ExtensionFilter("JSON files (*.json)", "*.json");
	         fileChooser.getExtensionFilters().add(extFilter);
	         
	         //Show save file dialog
	         File file = fileChooser.showSaveDialog(stage);
	         if(file != null){
	        	 FileWriter fileWriter = null;
	             
		            try {
						fileWriter = new FileWriter(file);
						 fileWriter.write(final_one.toString());
				            fileWriter.close();
					} catch (IOException e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
					}
		           
	             
	         }
		}
		else{
			String next_temp_path_to_save_when_export_folder_hieachy = list_all_folder_in_a_folder_when_export_folder.get(list_all_folder_in_a_folder_when_export_folder.size()-1);			
			list_all_folder_in_a_folder_when_export_folder.remove(list_all_folder_in_a_folder_when_export_folder.size()-1);
			int next_temp_folder_level= hmap_to_store_folder_with_level_when_export_folder.get(next_temp_path_to_save_when_export_folder_hieachy);
			hmap_to_store_folder_with_level_when_export_folder.remove(next_temp_path_to_save_when_export_folder_hieachy); 
			String folder_name_from_next_temp_path_to_save_when_delete_folder_hieachy =next_temp_path_to_save_when_export_folder_hieachy.substring(next_temp_path_to_save_when_export_folder_hieachy.lastIndexOf("\\")+1);
			export_folder(photo, folder_name_from_next_temp_path_to_save_when_delete_folder_hieachy, next_temp_folder_level,stage);
		}
	}

	public static void main(String[] args) {
		launch(args);

	}

	@Override
	public void start(final Stage stage) throws Exception {
		this.stage = stage;
		MongoClient mongoClient;
		 mongoClient = new MongoClient( "localhost" , 27017 );
		 DB db1 = mongoClient.getDB( "nosql" );
		 GridFS photo = new GridFS(db1, "ali");
		Group root = new Group();
		Scene scne = new Scene(root,500,500);
		Button btn  = new Button("click");
		Button import_fol  = new Button("Import....");
		btn.setOnMouseClicked(new EventHandler<MouseEvent>() {

			@Override
			public void handle(MouseEvent arg0) {
			//	int temp_level_make_json = l ;
				String item = "temp1";
				try {
					try {
						int temp_parent_level  = level_file;
						String a = item.toString().replaceAll("\\s", "").toLowerCase();
						String command_to_make_gridfs_files_export_json_file = "mongoexport.exe -d nosql -c "+ current_user_login+".files -q {'metadata.export_file_name':'"+ a +"','metadata.level':" + level_file + "} --out C:\\a\\files.json";
						Process process_of_command_to_make_gridfs_files_export_json_file;
						process_of_command_to_make_gridfs_files_export_json_file = Runtime.getRuntime().exec(command_to_make_gridfs_files_export_json_file);
						DBObject query_for_getting_id_to_make_chunks_export_file = new BasicDBObject().append("filename", item).append("metadata.level", level_file);				
						JSONParser parser = new JSONParser();	
						if(new File("C:\\a\\files.json").exists()){
							System.out.println("exist");
						}
						else{
							System.out.println("Not exist");
						}
						try {
							if( process_of_command_to_make_gridfs_files_export_json_file.waitFor() == 0){	
								Object obj_of_files;
								 DataInputStream dis2 = 
										    new DataInputStream (
										    	 new FileInputStream ("C:\\a\\files.json"));
										       
										 byte[] datainBytes2 = new byte[dis2.available()];
										 dis2.readFully(datainBytes2);
										 dis2.close();
										       
										 String content2 = new String(datainBytes2, 0, datainBytes2.length);
							//	obj_of_files=	(JSONObject) parser.parse(new InputStreamReader(new FileInputStream("items.json")));
//								obj_of_files = parser.parse(new FileReader("C:\\a\\files.json"));
//								JSONObject export_value_of_json_object_of_files = (JSONObject) obj_of_files;						
									final_one.put("file_type", "folder");
									final_one.put("start_level", temp_parent_level);
									final_one.put("total_level", temp_parent_level);							
									JSONArray create_chunks_array = new JSONArray();
									create_chunks_array.add("null");
									JSONObject create_chunks_object_with_array_its_value= new JSONObject();
									create_chunks_object_with_array_its_value.put("chunks", create_chunks_array);							
									JSONObject create_files_object_with_array_its_value= new JSONObject();
									JSONArray create_files_array = new JSONArray();
									create_files_array.add(content2);							
									create_files_object_with_array_its_value.put("files", create_files_array);	
									
									JSONObject cobmined_two_object_with_array_value = new JSONObject();
									cobmined_two_object_with_array_value.put("chunks", create_chunks_array);
									cobmined_two_object_with_array_value.put("files", create_files_array);
									final_one.put(temp_parent_level, cobmined_two_object_with_array_value);
									temp_parent_level = temp_parent_level  + 1;	
									try {
										export_folder(photo,item,temp_parent_level,stage);
									} catch (InterruptedException e) {
										// TODO Auto-generated catch block
										e.printStackTrace();
									}
							}
						} catch (InterruptedException e1) {
							// TODO Auto-generated catch block
							e1.printStackTrace();
						}
					
						
					} catch (ParseException e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
					}
				} catch (IOException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
			}
		});
		
		
		import_fol.setLayoutX(100);
		import_fol.setOnMouseClicked(new EventHandler<MouseEvent>() {

			@Override
			public void handle(MouseEvent arg0) {
				JSONParser parser1 = new JSONParser();
		 		Object test_file;
				
					//String parent = "aa";
					//int level = 2;
					try {
						test_file = parser1.parse(new FileReader("C:\\Users\\dell\\Desktop\\sdd.json"));
						JSONObject test_file_json = (JSONObject) test_file;
						System.out.println(test_file_json);
					} catch (FileNotFoundException e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
					} catch (IOException e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
					} catch (ParseException e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
					}
					
				
			}
		});
		root.getChildren().addAll(btn,import_fol);
		stage.setScene(scne);
		stage.show();
		
		
	}

}
