
import static java.nio.file.StandardWatchEventKinds.ENTRY_MODIFY;

import java.io.File;
import java.io.IOException;
import java.nio.file.FileSystems;
import java.nio.file.Files;
import java.nio.file.LinkOption;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.WatchEvent;
import java.nio.file.WatchKey;
import java.nio.file.WatchService;
import java.nio.file.attribute.BasicFileAttributes;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Scanner;
import java.util.Timer;
import java.util.TimerTask;

import javax.imageio.ImageIO;

import com.mongodb.BasicDBObject;
import com.mongodb.DB;
import com.mongodb.DBCollection;
import com.mongodb.DBCursor;
import com.mongodb.DBObject;
import com.mongodb.MongoClient;
import com.mongodb.gridfs.GridFS;
import com.mongodb.gridfs.GridFSDBFile;
import com.mongodb.gridfs.GridFSInputFile;

import javafx.application.Application;
import javafx.application.Platform;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.concurrent.Task;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Group;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.ContentDisplay;
import javafx.scene.control.ContextMenu;
import javafx.scene.control.Label;
import javafx.scene.control.ListView;
import javafx.scene.control.MenuItem;
import javafx.scene.control.PasswordField;
import javafx.scene.control.TextField;
import javafx.scene.control.cell.TextFieldListCell;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.input.DragEvent;
import javafx.scene.input.Dragboard;
import javafx.scene.input.MouseButton;
import javafx.scene.input.MouseEvent;
import javafx.scene.input.TransferMode;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Pane;
import javafx.stage.Stage;
import javafx.stage.WindowEvent;
import javafx.util.StringConverter;
import javafx.util.converter.DefaultStringConverter;





public class FYP1 extends Application {
	/*Field Declartion Over here ===========================================================================================
	 * ===================================================================================================================
	 * ==================================================================================================================*/
	 static int flag = 0 ;
	 static String status;
	 private Stage stage;
	 static GridFS photo  = null;
	 static java.awt.Image system_tray_image;
	 private Timer notificationTimer = new Timer();
	 private DateFormat timeFormat = SimpleDateFormat.getTimeInstance();
	 static int level_file = 0; 
	 Button back_button;
	 Button foward_button;
	 
	 //
	 static String parent_folder_name= "root";
	 static int final_length;;
	 
	 static List<String> myList = new ArrayList<String>();
	 static List<String> forward_list = new ArrayList<String>();
		static List<String> list = new ArrayList<String>();
		static String current_Path = " " ;
		static HashMap<String, Integer> hmap = new HashMap<String,Integer>();
	 //
	 
	 
	/* Method Decalartion over here =========================================================================================
	 * ======================================================================================================================
	 * ======================================================================================================================
	 * ======================================================================================================================*/
	 private void showStage() {
		    stage.show();		       
		    }
	 
	 private void hideSTage() {
		    stage.hide();		       
		    }
	 /*Display Directory=================================================================================================
	  * =============================================================================================================*/
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
			
			}
		
		}
	 
	 /*Fet file Extension method ======================================================================================
	  * ==================================================================================================================*/
	 public static String getFileExtension(String  fileName) {
	        if(fileName.lastIndexOf(".") != -1 && fileName.lastIndexOf(".") != 0)
	        return fileName.substring(fileName.lastIndexOf(".")+1);
	        else return "";
	    }
	 /**/
	 public  void  file_show_with_hiearchy(ListView<String> list_view_to_show_file,int final_length ,ObservableList<String> items_to_show_file,GridFS photo ,javafx.scene.image.Image text_file,javafx.scene.image.Image pdf, javafx.scene.image.Image folder,javafx.scene.image.Image arrow_left,javafx.scene.image.Image arrow_right,String status,Scene scene){
		 
		
		 
		 back_button.setOnMouseClicked(new EventHandler<MouseEvent>() {

			@Override
			public void handle(MouseEvent arg0) {

				//System.out.println("Back");
					String temp_folderString = null;
					if(myList.size() == 1){
						back_button.setDisable(true);
					}
					else {
						temp_folderString = myList.get(myList.size()-2);
						forward_list.add(myList.get(myList.size()-1));
						myList.remove(myList.size()-1);
						System.out.println(myList);
					}
				level_file--;
				list_view_to_show_file.getItems().clear();
				//hbox.getChildren().clear();
				
				file_show_with_hiearchy(list_view_to_show_file, final_length, items_to_show_file, photo, text_file, pdf, folder, arrow_left, arrow_right,temp_folderString,scene);
				
				
				
			}
		});
		 
		  foward_button.setOnMouseClicked(new EventHandler<MouseEvent>() {

				@Override
				public void handle(MouseEvent arg0) {
					// TODO Auto-generated method stub
					//System.out.println("Forward");
					String temp_folderString = null;
					
					if(!(forward_list.isEmpty()) ){
						System.out.println(forward_list);
						temp_folderString = forward_list.get(forward_list.size()-1);
						myList.add(temp_folderString);
						System.out.println(forward_list.size());
						//System.out.println(" zero index data "  + forward_list.get(0) + "last index" + forward_list.get(forward_list.size()));
						forward_list.remove(forward_list.size()-1);
						System.out.println("temp value " + temp_folderString);
						
						
						
					}
					level_file++;
					list_view_to_show_file.getItems().clear();
					//hbox.getChildren().clear();
					file_show_with_hiearchy(list_view_to_show_file, final_length, items_to_show_file, photo, text_file, pdf, folder, arrow_left, arrow_right,temp_folderString,scene);
					
				}
			});
		  
		  
		  
		  
		  
		  //
		  /* 5/14/15 when Someone Dropped Files herer=====================================================================*/
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
		    
		    
		    /*Dropping over surface of theclipboard only file is dropped ===================================================================================
	        ==========================================================================================================
	        		===================================================================================*/
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
							
								
								System.out.println(dateFormat.format(date));
								
		                         
		                         if(attr.isRegularFile())
		                         {
		                        	// System.out.println("File extension is: "+getFileExtension(path_of_file.getFileName().toString()));
		                        	 String extension_of_file = getFileExtension(path_of_file.getFileName().toString());
		                        	 list_view_to_show_file.getItems().add(path_of_file.getFileName().toString());		                        	 
		                        	 DBObject obj = new BasicDBObject();
		                     		obj.put( "path", current_Path+ "/"+path_of_file.getFileName().toString() );
		                     		obj.put("current_Path", current_Path);      
		                     		obj.put("created_date", dateFormat.format(date));
		                     		obj.put("modified_date", dateFormat.format(date));
		                     		obj.put("type", "file");
		                     		obj.put("level", level_file);
		                     		obj.put("level_path", "/"+path_of_file.getFileName().toString());
		                     		obj.put("extension" , extension_of_file);
		                     		obj.put("parent_folder_name", parent_folder_name);
		                     		 //Creates a GridFS instance for the specified bucket in the given database.
		                     		GridFSInputFile input;
		                     				                     		
		                        	    input = photo.createFile(imageFile);	
		                        	    input.setMetaData(obj);
			                     		//input.setContentType("png");
										input.setFilename(path_of_file.getFileName().toString()); //set filename 
										input.setContentType(extension_of_file);
				                     	input.save();
		                        	 System.out.println("This is the regular file");
		                         }
		                         else if (attr.isDirectory()){
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
	                //event.setDropCompleted(success);
	              //  event.consume();
	            }
	        });
	        
	        /*Drop Event is ended here =========================================================================
	         * ===================================================================================================
	         * ====================================================================================================
	         * =====================================================================================================*/
		    /*5/14/15End When Someone Dropped Files=======================================================================*/
		    
		    
		    /*When New FOlder is created*/
	        
	        
	        /*Creating a new folder on the clipboard =========================================================
	         * ===============================================================================================
	         * ===================================================================================================
	         * =====================================================================================================
	         * ==================================================================================================*/ 
	        list_view_to_show_file.setOnMouseClicked(new EventHandler<MouseEvent>() {

				@Override
				public void handle(MouseEvent e) {
					if (e.getButton() == MouseButton.SECONDARY)
		            {
						DateFormat dateFormat = new SimpleDateFormat("yyyy/MM/dd HH:mm:ss");
						Date date = new Date();
						String folder_name = "new folder";
					
						ContextMenu menu = new ContextMenu();
						MenuItem folder_create = new MenuItem("Create New Folder");
						folder_create.setGraphic(new ImageView(folder));
						folder_create.setOnAction(new EventHandler<ActionEvent>() {

							@Override
							public void handle(ActionEvent arg0) {
								list_view_to_show_file.getItems().add(folder_name);												
		                        	 DBObject obj = new BasicDBObject();
			                     		obj.put( "path", current_Path+ "//" + folder_name);
			                     		
			                     		obj.put("current_Path", current_Path);      
			                     		obj.put("created_date", dateFormat.format(date));
			                     		obj.put("modified_date", dateFormat.format(date));
			                     		obj.put("type", "folder");
			                     		obj.put("level", level_file);
			                     		obj.put("level_path", "//"+folder_name);
			                     		obj.put("parent_folder_name", parent_folder_name);                     		
			                     	    obj.put("extension" ,"no");
			                     		
			                     		
			                     		
			                     		 //Creates a GridFS instance for the specified bucket in the given database.
			                     		GridFSInputFile input;
			                     		File image_file = new File("c:\\ali.txt");	
			                     		
			                   	try {
									input = photo.createFile(image_file);
									input.setFilename(folder_name);
									input.setMetaData(obj);
				                   	input.save();
								} catch (IOException e) {
									// TODO Auto-generated catch block
									e.printStackTrace();
								}						
							}
						});
						MenuItem remove_file = new MenuItem("remove file");
						remove_file.setOnAction(new EventHandler<ActionEvent>() {

							@Override
							public void handle(ActionEvent arg0) {
								
								System.out.println("remove file");
							}
						});
						menu.getItems().addAll(folder_create,remove_file);
						
						list_view_to_show_file.setContextMenu(menu);
			
		                System.out.println("Desired Click");
		            }
		            else
		            {
		                System.out.println("No right click");
		            }
				//	items_of_labels.remove(labels.getSelectionModel().getSelectedIndex());
					
					
				}
			});
	        
	        /*end of folder is created*/
		    
		  /*In future If we want to display the path in label =======================================================
		   * ========================================================================================================
		   * ========================================================================================================*/  
		    Label show_path = new Label();
		    show_path.setPadding(new Insets(0, 0, 0, 20));
		  //  show_path.setMaxWidth(350.0);
		    show_path.setMinWidth(350.0);
		    show_path.setMinHeight(25.0);
		    show_path.setLineSpacing(25);
		   
		    show_path.setStyle("-fx-background-color: 	#DCDCDC;");
		    /*End Label Code==========================================================================================
		     * ======================================================================================================*/
		   
		    if(forward_list.isEmpty()){
		    	foward_button.setDisable(true);
		    }
		    else{
		    	foward_button.setDisable(false);
		    }
		    
			if(myList.size() == 1){
				back_button.setDisable(true);
			}
			
			else if (myList.size() > 1){
				back_button.setDisable(false);
			}
		       	
		   
		//    hbox.getChildren().addAll(buttonCurrent, forward, show_path);
		 /*End f HBox=============================================================================================
		  * ============================================================================================================
		  * ===========================================================================================================
		  * */
		    
		  
		    
		    /*Display Files if status = normal means partucular folder files will show =================================
		    =====================================================================================================*/
		    DBObject query;
		 if(status == "normal"){
			 query = new BasicDBObject().append("metadata.level",level_file).append("metadata.parent_folder_name", parent_folder_name);
		 }
		 else{
			 System.out.println("ststus "+ status);
			 query = new BasicDBObject().append("metadata.level",level_file).append("metadata.parent_folder_name", status);
			 //query = new BasicDBObject().append("metadata.level",level_file);
		 }
		    List<GridFSDBFile> files = photo.find(query);
		    if(level_file < 1)
		    {
		    	//System.out.println(level_file);
		    }
		    if(level_file > 0){
		    	if(files.isEmpty()){
		    		foward_button.setDisable(true);
		    	}		    	
		    }
	        for (GridFSDBFile file : files) {        	        	
	        	String type = file.getMetaData().get("type").toString();
	        	list_view_to_show_file.getItems().add(file.getFilename());
	        	
	        }//end of for loop
	        
	        
	        /*End of File Display ====================================================================================
	         * =======================================================================================================*/
	        
	        
	        /*Add  cell Factory for making a custom list =================================================================
		     * ==========================================================================================================
		     * ==========================================================================================================*/
		    
		    StringConverter<String> identityStringConverter = new DefaultStringConverter();
		    list_view_to_show_file.setCellFactory(lv -> new TextFieldListCell<String>(identityStringConverter) {
	            private javafx.scene.image.ImageView folder1 = new javafx.scene.image.ImageView(folder);
	            private javafx.scene.image.ImageView pdf1 = new javafx.scene.image.ImageView(pdf);
	            private javafx.scene.image.ImageView text_file1 = new javafx.scene.image.ImageView(text_file);
               //Run to customize the string 
	            @Override
	            public void updateItem(String item, boolean empty) {
	                super.updateItem(item, empty);
	                if (! empty && ! isEditing()) {
	                	String extension  = getFileExtension(item);
	                    setStaticGraphic(item,extension);
	                	 setOnMouseClicked(new EventHandler<MouseEvent>() {
	             			@Override
	             			public void handle(MouseEvent e) {
	             				if (e.getButton() == MouseButton.SECONDARY)
	             	            {
	             					DateFormat dateFormat = new SimpleDateFormat("yyyy/MM/dd HH:mm:ss");
	             					Date date = new Date();	             				
	             					ContextMenu menu = new ContextMenu();
	             					MenuItem folder_create = new MenuItem("Open");
	             					folder_create.setGraphic(new javafx.scene.image.ImageView(folder));
	             					folder_create.setOnAction(new EventHandler<ActionEvent>() {
	             						@Override
	             						public void handle(ActionEvent arg0) {	             						             					
	             							if(getFileExtension(item) == ""){
	             								myList.add(item);
	             								back_button.setDisable(false);
	             								list_view_to_show_file.getItems().clear();
	        									//hbox.getChildren().clear();
	        									
	        									level_file = level_file + 1;
	        									parent_folder_name = item;
	        									if(level_file < final_length){
	        										System.out.println(level_file);	        			
	        										file_show_with_hiearchy(list_view_to_show_file,final_length,items_to_show_file,photo,text_file,pdf,folder,arrow_left,arrow_right,"normal",scene);
	        									}
	        									
	        									else{
	        							file_show_with_hiearchy(list_view_to_show_file,final_length,items_to_show_file,photo,text_file,pdf,folder,arrow_left,arrow_right,"normal",scene);
	        										System.out.println("End of File Hiearcgu");
	        									} 		
	             							}
	             							else{
	             								DBObject update_file = new BasicDBObject().append("filename", item).append("metadata.level", level_file);
	        									GridFSDBFile imageForOutput = photo.findOne(update_file);
	             								System.out.println("extension is " + getFileExtension(item));
	             								
	             								if(getFileExtension(item).equals("txt")){
	             									try {

	             										
	             										Date temp_UploadDate=imageForOutput.getUploadDate();
	            										System.out.println("Upload date  = " + imageForOutput.getUploadDate());
	            										DateFormat dateFormat = new SimpleDateFormat("yyyy/MM/dd HH:mm:ss");
	            										Date date = new Date();
	            										DBObject update_metadata = new BasicDBObject().append("modified_date", dateFormat.format(date));
	            										//System.out.println("File Name= "+ file.getFilename() + "  File Meta Data= " +file.getMetaData() + " File upload Date = " + file.getUploadDate() );
	            										String temp_file = imageForOutput.getFilename();
	            										DBObject temp_metadata = imageForOutput.getMetaData();
	            										System.out.println("Temp Meta data before update " + temp_metadata);
	            										
	            									//	file.setMetaData((update_metadata));
	            										 imageForOutput.getMetaData().putAll(update_metadata);
	            										 temp_metadata = imageForOutput.getMetaData();
	            										
	            											imageForOutput.writeTo("C:\\ali1.txt");
	            										System.out.println("Temp Meta data before update " + imageForOutput.getMetaData());
	            										//System.out.println("Temp Upload Date " + temp_UploadDate);
	            										//photo.remove(photo.findOne(update_file));
	            										photo.remove(update_file);
	            										System.out.println("photo =  " + photo);
	            										System.out.println("image for output " + imageForOutput);
	            										
	            										
	            									
	            										Process p = Runtime.getRuntime().exec("notepad C:\\ali1.txt");
	            										try {
															if( p.waitFor() == 0){																		
																System.out.println(" exit" );
																 File imageFile = new File("C:\\ali1.txt");
															      GridFSInputFile input;
															      
																		input = photo.createFile(imageFile);
																		input.setFilename(temp_file);
																		input.setMetaData(temp_metadata);				
															      	    input.save();
																//File imageFile = new File("C:\\ali1.txt");
																imageFile.delete();}
														} catch (InterruptedException e) {
															// TODO Auto-generated catch block
															e.printStackTrace();
														}
	             										
	             										
	             										
													} catch (IOException e) {
														// TODO Auto-generated catch block
														e.printStackTrace();
													}
	             									
	             								}
	             								else if(getFileExtension(item).equals("pdf")){
	             									try {
														imageForOutput.writeTo("C:\\ali1.pdf");
														if ((new File("c:\\ali1.pdf")).exists()) {
															 
															Process p = Runtime
															   .getRuntime()
															   .exec("rundll32 url.dll,FileProtocolHandler c:\\ali1.pdf");
															try {
																p.waitFor();
															} catch (InterruptedException e) {
																// TODO Auto-generated catch block
																e.printStackTrace();
															}
												 
														} else {
												 
															System.out.println("File is not exists");
												 
														}
													} catch (IOException e) {
														// TODO Auto-generated catch block
														e.printStackTrace();
													}
           										System.out.println("Its a pdf file");
           									}
	             									
	             							}
	             						}
	             					});	             					
	             					menu.getItems().addAll(folder_create);	             					
	             					setContextMenu(menu);
	             	            }
	             	            else
	             	            {
	             	                //System.out.println("No right click");
	             	            }	    
	             			}
	             		});                  
	                }
	            }
	            
	            //when you cancel to change the name of folder and file 
	            @Override
	            public void cancelEdit() {
	            	if(list_view_to_show_file.getEditingIndex() == -1 ){
	            		System.out.println(list_view_to_show_file.getSelectionModel().getSelectedIndex());
	            		String extension =  getFileExtension(list_view_to_show_file.getSelectionModel().getSelectedItem());
	                	String item = list_view_to_show_file.getItems().get(list_view_to_show_file.getSelectionModel().getSelectedIndex());
	                	super.cancelEdit();
	                	setStaticGraphic(item, extension);          		
	            	}
	            	else{	           		
	                	String item = list_view_to_show_file.getItems().get(list_view_to_show_file.getEditingIndex());
	                	DBObject update_file = new BasicDBObject().append("filename", item).append("metadata.level", level_file);
						GridFSDBFile file = photo.findOne(update_file);
						String extension = file.getMetaData().get("extension").toString();
	                	super.cancelEdit();
	                	setStaticGraphic(item, extension);
	            	}         
	            }
	         
	            //When you change name of any folder and file it first change name is 
	            @Override
	            public void commitEdit(String newValue) {
	            	//here save filename and 
	            	
	            	String old_value = list_view_to_show_file.getItems().get(list_view_to_show_file.getEditingIndex());
	            	DBObject update_file = new BasicDBObject().append("filename", old_value).append("metadata.level", level_file);
					GridFSDBFile file = photo.findOne(update_file);
					String extension = file.getMetaData().get("extension").toString();
	            	if(newValue.equals("")){       		            		
	            		cancelEdit();
	            	}
	            	else{
	            		file.put("filename" , newValue);
	            		file.save();
	            		int parent = level_file + 1;
	            		DBObject update_file1 = new BasicDBObject().append("metadata.parent_folder_name", old_value).append("metadata.level", parent);
	            		 List<GridFSDBFile> files = photo.find(update_file1);		 
	            		 for (GridFSDBFile file1 : files) {   
	 	    	        	file1.getMetaData().put("parent_folder_name", newValue);
	 	    	        	file1.save();
	 	    	        }	            			            		 
	            			super.commitEdit(newValue);                                	
	            			 setStaticGraphic(newValue,extension);            		
	            	}            		               
	            }
	            
	            //show images with folder pdf and text file=========================================
	            private void setStaticGraphic(String item , String extension) {
	            	if(extension.equals("txt")) {
	            		setGraphic(text_file1);
	            		
	            	}
	            	else if(extension.equals("pdf")){
	            		setGraphic(pdf1);
	            	}
	            	
	              	else if (!(getFileExtension(item).contains("."))){
	              
	        	     setGraphic(folder1);
	            	}
	                
	                setContentDisplay(ContentDisplay.LEFT);
	                setGraphicTextGap(10.2);                
	            }
	        });
		    /*==============================================================================================================
		     * ===========================================================================================================*/
	        
	 
		  //
		  
	 }
	 /*System Tray Will Create=============================================================================================
	  * ==================================================================================================================
	  * =================================================================================================================
	  * ===============================================================================================================*/
	 private void addAppToTray() {
     try {
         // ensure awt toolkit is initialized.
         java.awt.Toolkit.getDefaultToolkit();
         if (!java.awt.SystemTray.isSupported()) {
             System.out.println("No system tray support, application exiting.");
             Platform.exit();
         }
         java.awt.SystemTray tray = java.awt.SystemTray.getSystemTray();
         system_tray_image= ImageIO.read(getClass().getResource("folder.png"));
         java.awt.TrayIcon trayIcon = new java.awt.TrayIcon(system_tray_image);
         java.awt.MenuItem openItem = new java.awt.MenuItem("No SQL FileSystem");
         openItem.addActionListener(event -> Platform.runLater(this::showStage));
         java.awt.MenuItem exitItem = new java.awt.MenuItem("Exit");
         exitItem.addActionListener(event -> {
             notificationTimer.cancel();
             Platform.exit();
             tray.remove(trayIcon);
             System.exit(0);
         });
         final java.awt.PopupMenu popup = new java.awt.PopupMenu();
         popup.add(openItem);
         popup.addSeparator();
         popup.add(exitItem);
         trayIcon.setPopupMenu(popup);
         notificationTimer.schedule(
                 new TimerTask() {
                     @Override
                     public void run() {
                         javax.swing.SwingUtilities.invokeLater(() ->
                             trayIcon.displayMessage(
                                     "hello",
                                     "The time is now " + timeFormat.format(new Date()),
                                     java.awt.TrayIcon.MessageType.INFO
                             )
                         );
                     }
                 },
                 5_000,
                 60_000
         );
         tray.add(trayIcon);
     } catch (java.awt.AWTException | IOException e) {
         System.out.println("Unable to init system tray");
         e.printStackTrace();
     }
 }
	 
	 
	 
	 /*Main Method  Over Here That will launch javaFX application Thread  ====================================================
	  * =====================================================================================================================
	 * ======================================================================================================================
	 * ======================================================================================================================*/

	public static void main(String[] args) {
		launch(args);
	}
	

	 /*JavaFX Starting Point ====================================================
	  * =====================================================================================================================
	 * ======================================================================================================================
	 * ======================================================================================================================*/
	@Override
	public void start(final Stage stage) throws Exception {
		//
		
		 javafx.scene.image.Image text_file = new javafx.scene.image.Image(getClass().getResourceAsStream("text_file.png"));
         javafx.scene.image.Image pdf = new javafx.scene.image.Image(getClass().getResourceAsStream("pdf.gif"));
         javafx.scene.image.Image folder = new javafx.scene.image.Image(getClass().getResourceAsStream("folder.png"));
         javafx.scene.image.Image arrow_left = new javafx.scene.image.Image(getClass().getResourceAsStream("arrow_left.png"));
         javafx.scene.image.Image arrow_right = new javafx.scene.image.Image(getClass().getResourceAsStream("arrow_right.png"));
       //  
		 this.stage = stage;
		 Platform.setImplicitExit(false); 		 
		 javax.swing.SwingUtilities.invokeLater(this::addAppToTray);	//add system tray 	 
		 MongoClient mongoClient;
		 mongoClient = new MongoClient( "localhost" , 27017 );
		 DB db1 = mongoClient.getDB( "nosql" );
		 
		 myList.add("root");
		 Pane root = new Pane();
		 Scene scene = new Scene(root,1000,600);
		 root.setPrefHeight(600);
		 root.setPrefWidth(1000);
			 Image root_image = new Image("black_background.jpg");
			 ImageView root_image_view = new ImageView(root_image);
			 root_image_view.setFitHeight(600);
			 root_image_view.setFitWidth(1000);
			 
			 Pane register_pane = new Pane();
			 register_pane.setLayoutX(299.0);
			 register_pane.setLayoutY(107.0);
			 register_pane.setPrefHeight(350.0);
			 register_pane.setPrefWidth(400.0);
				 Image register_pane_image = new Image("Register_page.jpg");
				 ImageView register_pane_image_view = new ImageView(register_pane_image);
				 register_pane_image_view.setFitHeight(350);
				 register_pane_image_view.setFitWidth(400);
				 Button login = new Button();
				 login.setLayoutX(234);
				 login.setLayoutY(175);
				 login.setPrefWidth(108);
				 login.setPrefHeight(40);
				 Image login_btn_image = new Image("login-button.png");
				 ImageView login_btn_image_view = new ImageView(login_btn_image);
				 login.setGraphic(login_btn_image_view);
				 PasswordField pass_field = new PasswordField();
				 pass_field.setLayoutX(53);
				 pass_field.setLayoutY(129);
				 pass_field.setPrefWidth(300);
				 pass_field.setPrefHeight(40);
				 pass_field.setPromptText("Password");
				 TextField user_name = new TextField();
				 user_name.setPromptText("Username");
				 user_name.setLayoutX(53);
				 user_name.setLayoutY(82);
				 user_name.setPrefWidth(300);
				 user_name.setPrefHeight(40);
				 Button sign_up = new Button();
				 sign_up.setLayoutX(103);
				 sign_up.setLayoutY(175);
				 sign_up.setPrefWidth(108);
				 sign_up.setPrefHeight(40);
				 Image sign_up_btn_image = new Image("sign_up_btn.png");
				 ImageView sign_up_btn_image_view = new ImageView(sign_up_btn_image);
				 sign_up_btn_image_view.setFitHeight(40);
				 sign_up_btn_image_view.setFitWidth(108);
				 sign_up.setGraphic(sign_up_btn_image_view);				 				 
			 register_pane.getChildren().addAll(register_pane_image_view,login,pass_field,user_name,sign_up);
		 
		 
		 
		 
		 
		 
		 
//		 Pane root = new Pane();
//		 root.setPrefHeight(600);
//		 root.setPrefWidth(1000);
			 Pane top_bar_pane = new Pane();
			     Image top_bar_pane_image = new Image("file_syatem_background_image.jpg");
				 ImageView top_bar_pane_image_view = new ImageView(top_bar_pane_image);
				 top_bar_pane_image_view.setFitHeight(163);
				 top_bar_pane_image_view.setFitWidth(1050);
				 
				 TextField path_display_text_field = new TextField();
				 path_display_text_field.setLayoutX(108);
				 path_display_text_field.setLayoutY(104);
				 path_display_text_field.setPrefHeight(33);
				 path_display_text_field.setPrefWidth(624);
				 path_display_text_field.setPromptText("File Path");
				 path_display_text_field.setEditable(false);
				 path_display_text_field.setStyle("-fx-background-radius: 15;" + "-fx-font-size: 18px;" +  "-fx-font-weight: bold;");
				 
				 TextField search_text_field = new TextField();
				 search_text_field.setLayoutX(741);
				 search_text_field.setLayoutY(105);
				 search_text_field.setPrefHeight(36);
				 search_text_field.setPrefWidth(212);
				 search_text_field.setPromptText("Search");
				 search_text_field.setStyle("-fx-font-size: 18px;" +  "-fx-font-weight: bold;");
				 
				 Button search_button = new Button();
				 search_button.setPrefWidth(39);
				 search_button.setPrefHeight(38);
				 search_button.setMinHeight(38);
				 search_button.setMinWidth(39);
				 search_button.setMaxHeight(38);
				 search_button.setMaxWidth(39);				 
				 search_button.setLayoutX(20);
				 search_button.setLayoutY(103);
				 search_button.setLayoutX(953);
				 search_button.setLayoutY(105);
				 search_button.setMnemonicParsing(false);
				 Image search_button_image = new Image("search_image.png");
				 ImageView search_button_image_view = new ImageView(search_button_image);
				 search_button_image_view.setPickOnBounds(true);
				 search_button_image_view.setPreserveRatio(true);
				 search_button_image_view.setFitHeight(30);
				 search_button_image_view.setFitWidth(39);
				 search_button.setGraphic(search_button_image_view);
				 
				 
				 foward_button = new Button();
				 foward_button.setPrefWidth(37);
				 foward_button.setPrefHeight(35);
				 foward_button.setMinHeight(35);
				 foward_button.setMinWidth(37);
				 foward_button.setMaxHeight(35);
				 foward_button.setMaxWidth(37);				 
				 foward_button.setLayoutX(64);
				 foward_button.setLayoutY(103);
				 foward_button.setMnemonicParsing(false);
				 Image foward_button_image = new Image("arrow_right.png");
				 ImageView foward_button_image_view = new ImageView(foward_button_image);
				 foward_button_image_view.setFitHeight(30);
				 foward_button_image_view.setFitWidth(37);
				 foward_button.setGraphic(foward_button_image_view);
				 foward_button.setStyle("-fx-background-radius: 300px;");
				 
				 
				 back_button = new Button();
				 back_button.setPrefWidth(37);
				 back_button.setPrefHeight(35);
				 back_button.setMinHeight(35);
				 back_button.setMinWidth(37);
				 back_button.setMaxHeight(35);
				 back_button.setMaxWidth(37);				 
				 back_button.setLayoutX(20);
				 back_button.setLayoutY(103);
				 back_button.setMnemonicParsing(false);
				 Image back_button_image = new Image("arrow_left.png");
				 ImageView back_button_image_view = new ImageView(back_button_image);
				 back_button_image_view.setFitHeight(30);
				 back_button_image_view.setFitWidth(37);
				 back_button.setGraphic(back_button_image_view);
				 back_button.setStyle("-fx-background-radius: 300px;");
				 
				 Image nosql_logo_image = new Image("nosql-matters-logo.png");
				 ImageView nosql_logo_image_view = new ImageView(nosql_logo_image);
				 nosql_logo_image_view.setFitHeight(104);
				 nosql_logo_image_view.setFitWidth(95);
				 nosql_logo_image_view.setLayoutX(14);
				 nosql_logo_image_view.setLayoutY(1);
				 
				 
			 top_bar_pane.getChildren().addAll(top_bar_pane_image_view,path_display_text_field,search_text_field,search_button,back_button,foward_button,nosql_logo_image_view);
			 
			 
			 
			 Pane list_view_pane = new Pane();
			 list_view_pane.setPrefSize(436, 436);
			 list_view_pane.setLayoutY(166);
			     Label name_of_file = new Label();
			     name_of_file.setAlignment(Pos.CENTER);
			     name_of_file.setPrefSize(350, 27);
			     name_of_file.setText("Name");
			     name_of_file.setContentDisplay(ContentDisplay.CENTER);
			     name_of_file.setStyle("-fx-font-size: 18px;" +  "-fx-font-weight: bold;");
			     name_of_file.setLayoutX(37);
			     name_of_file.setLayoutY(5);
			     ListView<String> list_view_to_show_file = new ListView<String>();
			     ObservableList<String> items_to_show_file = FXCollections.observableArrayList();
			     list_view_to_show_file.setPrefSize(439, 391);
			     list_view_to_show_file.setLayoutY(40);
			     //
			     list_view_to_show_file.setEditable(true);
			     
			     
			     	
					   			
			     //
			     
			     
		 
		 
		   list_view_pane.getChildren().addAll(name_of_file,list_view_to_show_file);
		 			 
		 root.getChildren().addAll(root_image_view,register_pane);
//		 root.getChildren().addAll(top_bar_pane,list_view_pane);
		 stage.setScene(scene);
		 stage.setResizable(false);
//		 stage.show();
		 
		 stage.setOnCloseRequest(new EventHandler<WindowEvent>() {

			@Override
			public void handle(WindowEvent arg0) {
				System.exit(0);
				
			}
		});
		/*Click On Sign Up  Button to register in user profile file system================================================
		 * ===============================================================================================================*/
		 sign_up.setOnMouseClicked(new EventHandler<MouseEvent>() {
			@Override
			public void handle(MouseEvent arg0) {
				DBCollection collection = db1.getCollection("account");
				
				
				
		         
		         BasicDBObject query = new BasicDBObject("username", user_name.getText()).append("password", pass_field.getText());
		         DBCursor cursor = collection.find(query);
		         int initial_object=1;
		         while (cursor.hasNext()) { 
		        	 initial_object++;
		         }		         
		         if(initial_object==1){
		        	 BasicDBObject document = new BasicDBObject();
				        document.put("username", user_name.getText());
				        document.put("password", pass_field.getText());
				        collection.insert(document);
		        	 
		         }
		         else{
		        	 System.out.println("ALready Exist");
		         }		        		        		        
			}
		});
		 
		  
		  
		 
		 /*Click on the login Button ====================================================================================
		  * ===========================================================================================================
		  * ==========================================================================================================*/
		 login.setOnMouseClicked(new EventHandler<MouseEvent>() {

			@Override
			public void handle(MouseEvent arg0) {
				DBCollection collection = db1.getCollection("account");	         
		         BasicDBObject query_for_login = new BasicDBObject("username", user_name.getText()).append("password", pass_field.getText());
		         DBCursor cursor = collection.find(query_for_login);
		         int initial_object=1;
		         BasicDBObject obj = null;
		         while (cursor.hasNext()) {
		        	 obj = (BasicDBObject) cursor.next();
		        	 initial_object++;
		         }		         
		         if(initial_object==1){
		        	 System.out.println("No Record Exist");
		         }
		         else{
		        	 photo = new GridFS(db1, obj.getString("username"));
		        	 root.getChildren().removeAll(root_image_view,register_pane);
		        	 root.getChildren().addAll(top_bar_pane,list_view_pane);
				     
				   	    String newFileName = ".^<>/";
				   		GridFSDBFile imageForOutput = null;
					   			 imageForOutput =photo.findOne(newFileName);					   								   							   	
					   			if(imageForOutput == null){
					   				System.out.println("First Time App No Hidden File Is Created");
					   				final_length=1;
					   			}
					   			else{
					   				String type = imageForOutput.getMetaData().get("level_level").toString();
					   				int a = Integer.parseInt(type);
					   				final_length = a +1;					   			
					   			}	
					   			
					   		//Initial Level Find FIle
					   			DBObject query = new BasicDBObject().append("metadata.level",level_file);
							    List<GridFSDBFile> files = photo.find(query);
							    status = "normal";	   	
							    
							    file_show_with_hiearchy(list_view_to_show_file,final_length,items_to_show_file,photo,text_file,pdf,folder,arrow_left,arrow_right,status,scene);
							    
		         }	//else end if successful login				
			}
		});
		 
		
		 
		 
		 
		 /*Initially will check the weather virtual drive is mounted or unmounted =======================================
		  * ============================================================================================================
		  * ===========================================================================================================*/
		File file = new File("C:\\Users\\dell\\Desktop\\ali\\this.txt");
		Scanner scanner = new Scanner(file);
		 while (scanner.hasNextLine()) {
		        String line = scanner.nextLine();
		        if (line.indexOf("/home/ali/stab") != -1) {
		        	 System.out.println("Initially Mounted");
		        	 flag = 1;		        	 
		        	}
		        else{
		        	//System.out.println("Not found");
		        	flag = 0;
		        }
		      
		    }
		
		 //Task Will Use To Run Java Thread On Javafx .JavaFX hace only one thread to start new thread we use Task for
		 // concurrent working. Now we will work on two thread .If we want to run jaavfx thread task we will use 
		 //Platform.runlater to run jaavFX thread inside Task 
		 Task task = new Task<Void>() {
			    @Override public Void call() throws IOException, InterruptedException {
			    	 WatchService watcher = FileSystems.getDefault().newWatchService();
					 Path dir = Paths.get("C:\\Users\\dell\\Desktop\\ali");
			         dir.register(watcher, ENTRY_MODIFY);
			         while(true){
			         WatchKey key;			         
			         key = watcher.take();
			         for (WatchEvent<?> event : key.pollEvents()) {
		                 WatchEvent.Kind<?> kind = event.kind();		                  
		                 @SuppressWarnings("unchecked")
		                 WatchEvent<Path> ev = (WatchEvent<Path>) event;
		                 Path fileName = ev.context();		                  
		                 System.out.println(kind.name() + ": " + fileName);		                  
		                 if (kind == ENTRY_MODIFY &&
		                         fileName.toString().equals("this.txt")) {
		                     System.out.println("My source file has changed!!!");
		                     Thread.sleep(15000);
		                 	File file1 = new File("C:\\Users\\dell\\Desktop\\ali\\this.txt");
		            		Scanner scanner1 = new Scanner(file1);
		            		 while (scanner1.hasNextLine()) {
		            		        String line = scanner1.nextLine();
		            		        if (line.indexOf("/home/ali/stab") != -1) {		            		        	 
		            		        	 System.out.println("Mount");
		            		        	 Platform.runLater(new Runnable() {											
											@Override
											public void run() {
												if(stage.isShowing()){													
												}
												else{
													showStage();
												}
																					
											}
										});		            		        	
		            		        	 flag = 1;
		            		        	}
		            		        else{		            		        	
		            		      flag=0;
		            		        }
		            		      
		            		    }
								if(flag==0){
									 System.out.println("UnMount");
									 Platform.runLater(new Runnable() {										
											@Override
											public void run() {
												if(stage.isShowing()){
													hideSTage();
												}
											}
										});		 
								}		            		 
		                 }		                 
		             }
			         boolean valid = key.reset();
		             if (!valid) {
		                 break;
		             }
		         }
			        return null;
			    }
			};		//Task class end Over Here		         
			new Thread(task).start();        			
	}
}
