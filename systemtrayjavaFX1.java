/*Latest app in which system tray work fine commenting not done yet over heree   5/31/2015 date*/
import javafx.application.*;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.*;
import javafx.scene.control.Button;
import javafx.scene.control.ContentDisplay;
import javafx.scene.control.ContextMenu;
import javafx.scene.control.Label;
import javafx.scene.control.ListView;
import javafx.scene.control.MenuItem;
import javafx.scene.control.cell.TextFieldListCell;
import javafx.scene.image.ImageView;
import javafx.scene.input.DragEvent;
import javafx.scene.input.Dragboard;
import javafx.scene.input.MouseButton;
import javafx.scene.input.MouseEvent;
import javafx.scene.input.TransferMode;
import javafx.scene.layout.*;
import javafx.scene.paint.Color;
import javafx.stage.*;
import javafx.util.StringConverter;
import javafx.util.converter.DefaultStringConverter;

import javax.imageio.ImageIO;

import com.mongodb.BasicDBObject;
import com.mongodb.DB;
import com.mongodb.DBObject;
import com.mongodb.Mongo;
import com.mongodb.MongoClient;
import com.mongodb.gridfs.GridFS;
import com.mongodb.gridfs.GridFSDBFile;
import com.mongodb.gridfs.GridFSInputFile;

import java.awt.Image;
import java.io.File;
import java.io.IOException;
import java.net.URL;
import java.net.UnknownHostException;
import java.nio.file.Files;
import java.nio.file.LinkOption;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.attribute.BasicFileAttributes;
import java.text.*;
import java.util.*;
 
// Java 8 code
public class systemtrayjavaFX1 extends Application {
	
	 static int level_file = 0; 
	 static String current_Path = " " ;
	 static String parent_folder_name= "root";
	 static String status;
	 static List<String> myList = new ArrayList<String>();
	 static List<String> forward_list = new ArrayList<String>();
		static List<String> list = new ArrayList<String>();
		static HashMap<String, Integer> hmap = new HashMap<String,Integer>();
	 
		
			
	
	 public static String getFileExtension(String  fileName) {
	        if(fileName.lastIndexOf(".") != -1 && fileName.lastIndexOf(".") != 0)
	        return fileName.substring(fileName.lastIndexOf(".")+1);
	        else return "";
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
		 /*
		 
		 
		 int temp_level_file = temp_level + 1;
		 String temp_level_file_parent = temp_parent;
			try {
				File[] files = dir.listFiles();
				for (File file : files) {
					if (file.isDirectory()) {
						 DBObject obj = new BasicDBObject();
                  		obj.put( "path", current_Path+ "//" + file.getName());
                  		
                  		obj.put("current_Path", current_Path);      
                  		obj.put("created_date", dateFormat.format(date));
                  		obj.put("modified_date", dateFormat.format(date));
                  		obj.put("type", "folder");
                  		obj.put("level", temp_level_file);
                  		obj.put("level_path", "//"+file.getName());
                  		obj.put("parent_folder_name", temp_level_file_parent);                     		
                  	    obj.put("extension" ,"no");
                  		
                  		
                  		
                  		 //Creates a GridFS instance for the specified bucket in the given database.
                  		GridFSInputFile input;
                  		File image_file = new File("c:\\ali.txt");
                  		try {
							input = photo.createFile(image_file);
							input.setFilename(file.getName());
							input.setMetaData(obj);
		                   	input.save();
						} catch (IOException e) {
							// TODO Auto-generated catch block
							e.printStackTrace();
						}
						
						temp_level_file_parent = file.getName();
						System.out.println("directory:" + file.getCanonicalPath());
						displayDirectoryContents(file,list_view_to_show_file,photo, date ,dateFormat , temp_level_file,file.getName());
					} else {
						String extension_of_file = getFileExtension(file.getName());
                   //	 list_view_to_show_file.getItems().add(file.getName());		                        	 
                   	 DBObject obj = new BasicDBObject();
                		obj.put( "path", current_Path+ "/"+file.getName());
                		obj.put("current_Path", current_Path);      
                		obj.put("created_date", dateFormat.format(date));
                		obj.put("modified_date", dateFormat.format(date));
                		obj.put("type", "file");
                		obj.put("level", temp_level_file);
                		obj.put("level_path", "/"+ file.getName());
                		obj.put("extension" , extension_of_file);
                		obj.put("parent_folder_name", parent_folder_name);
                		 //Creates a GridFS instance for the specified bucket in the given database.
                		GridFSInputFile input;
                				                     		
                   	    input = photo.createFile(file);	
                   	    input.setMetaData(obj);
                    		//input.setContentType("png");
							input.setFilename(file.getName()); //set filename 
							input.setContentType(extension_of_file);
	                     	input.save();
						System.out.println("     file:" + file.getCanonicalPath());
					}
				}
			} catch (IOException e) {
				e.printStackTrace();
			}*/
		}
	 /*=====================================================================================================================
	  * ======================================================================================================================
	  * ======================================================================================================================
	  * =====================================================================================================================
	  * ======================================================================================================================
	  * ====================================================================================================================*/
	 //method call when  click on folder to open  next level of hiearchy will show 
	 public  void  file_show_with_hiearchy(ListView<String> list_view_to_show_file,int i ,ObservableList<String> items_to_show_file,GridFS photo ,javafx.scene.image.Image text_file,javafx.scene.image.Image pdf, javafx.scene.image.Image folder,HBox hbox,javafx.scene.image.Image arrow_left,javafx.scene.image.Image arrow_right,String status,Scene scene){
		
		 
		 /*Hbox */
		 /*Back Button code============================================================================================
		  * =====================================================================================================*/
		 hbox.setPadding(new Insets(15, 15, 15, 12));
		    hbox.setSpacing(15);
		    hbox.setStyle("-fx-background-color: #336699;");	    
		    Button buttonCurrent = new Button();
		    buttonCurrent.setGraphic(new javafx.scene.image.ImageView(arrow_left));
		    buttonCurrent.setStyle(
	                "-fx-background-radius: 15; " +
	                "-fx-min-width: 10px; " +
	                "-fx-min-height: 20px; " +
	                "-fx-max-width: 10px; " +
	                "-fx-max-height: 20px;"
	        );	    
		    buttonCurrent.setOnMouseClicked(new EventHandler<MouseEvent>() {

				@Override
				public void handle(MouseEvent arg0) {
				//System.out.println("Back");
					String temp_folderString = null;
					if(myList.size() == 1){
						buttonCurrent.setDisable(true);
					}
					else {
						temp_folderString = myList.get(myList.size()-2);
						forward_list.add(myList.get(myList.size()-1));
						myList.remove(myList.size()-1);
						System.out.println(myList);
					}
				level_file--;
				list_view_to_show_file.getItems().clear();
				hbox.getChildren().clear();
				
				file_show_with_hiearchy(list_view_to_show_file, i, items_to_show_file, photo, text_file, pdf, folder, hbox, arrow_left, arrow_right,temp_folderString,scene);
				}
			});
		    /*Back Button End==========================================================================================
		     * ========================================================================================================
		     * =======================================================================================================*/
		
		    
		    
		    /*Forward Button Code=====================================================================================
		     * ======================================================================================================
		     * =====================================================================================================*/
		    Button forward = new Button();
		    forward.setGraphic(new ImageView(arrow_right));
		    forward.setStyle(
	                "-fx-background-radius: 15; " +
	                "-fx-min-width: 10px; " +
	                "-fx-min-height: 20px; " +
	                "-fx-max-width: 10px; " +
	                "-fx-max-height: 20px;" +
	                "-fx-padding-left:25px"
	        );
		    forward.setOnMouseClicked(new EventHandler<MouseEvent>() {

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
					hbox.getChildren().clear();
					file_show_with_hiearchy(list_view_to_show_file, i, items_to_show_file, photo, text_file, pdf, folder, hbox, arrow_left, arrow_right,temp_folderString,scene);
					
				}
			});
		    /*==========================================================================================================
		     * ========================================================================================================
		     * =====================================================================================================*/
		    
		    
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
		    	forward.setDisable(true);
		    }
		    else{
		    	forward.setDisable(false);
		    }
		    
			if(myList.size() == 1){
				buttonCurrent.setDisable(true);
			}
			
			else if (myList.size() > 1){
				buttonCurrent.setDisable(false);
			}
		       	
		   
		    hbox.getChildren().addAll(buttonCurrent, forward, show_path);
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
		    		forward.setDisable(true);
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
	             								buttonCurrent.setDisable(false);
	             								list_view_to_show_file.getItems().clear();
	        									hbox.getChildren().clear();
	        									
	        									level_file = level_file + 1;
	        									parent_folder_name = item;
	        									if(level_file < i){
	        										System.out.println(level_file);	        			
	        										file_show_with_hiearchy(list_view_to_show_file,i,items_to_show_file,photo,text_file,pdf,folder,hbox,arrow_left,arrow_right,"normal",scene);
	        									}
	        									
	        									else{
	        							file_show_with_hiearchy(list_view_to_show_file,i,items_to_show_file,photo,text_file,pdf,folder,hbox,arrow_left,arrow_right,"normal",scene);
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
	        
	 }
	static Image image;
	
    // one icon location is shared between the application tray icon and task bar icon.
    // you could also use multiple icons to allow for clean display of tray icons on hi-dpi devices.
  //  private static final String iconImageLoc =  "";
	
    //image = ImageIO.read(getClass().getResource("folder.png"));
    // application stage is stored so that it can be shown and hidden based on system tray icon operations.
    private Stage stage;
 
    // a timer allowing the tray icon to provide a periodic notification event.
    private Timer notificationTimer = new Timer();
 
    // format used to display the current time in a tray icon notification.
    private DateFormat timeFormat = SimpleDateFormat.getTimeInstance();
 
    // sets up the javafx application.
    // a tray icon is setup for the icon, but the main stage remains invisible until the user
    // interacts with the tray icon.
    @Override public void start(final Stage stage) {
        // stores a reference to the stage.
        this.stage = stage;
 
        // instructs the javafx system not to exit implicitly when the last application window is shut.
        Platform.setImplicitExit(false);
 
        // sets up the tray icon (using awt code run on the swing thread).
        javax.swing.SwingUtilities.invokeLater(this::addAppToTray);
        javafx.scene.image.Image text_file = new javafx.scene.image.Image(getClass().getResourceAsStream("text_file.png"));
        javafx.scene.image.Image pdf = new javafx.scene.image.Image(getClass().getResourceAsStream("pdf.gif"));
        javafx.scene.image.Image folder = new javafx.scene.image.Image(getClass().getResourceAsStream("folder.png"));
        javafx.scene.image.Image arrow_left = new javafx.scene.image.Image(getClass().getResourceAsStream("arrow_left.png"));
        javafx.scene.image.Image arrow_right = new javafx.scene.image.Image(getClass().getResourceAsStream("arrow_right.png"));
		ListView<String> list_view_to_show_file = new ListView<String>();
		ObservableList<String> items_to_show_file = FXCollections.observableArrayList();
		list_view_to_show_file.setEditable(true);
		 Mongo mongo = null;
	
			try {
				mongo = new MongoClient("localhost",27017);
			} catch (UnknownHostException e1) {
				// TODO Auto-generated catch block
				e1.printStackTrace();
			}
	
			DB db1 = mongo.getDB("fyp1");
		    GridFS photo = new GridFS(db1,"photo"); 
	    String current_path = "/";	   	 
	    myList.add("root");
	    int i;
	    //get level from database
	   	    
	   	    String newFileName = ".^<>/";
	   		GridFSDBFile imageForOutput = null;
		   			 imageForOutput =photo.findOne(newFileName);
		   			
		   		
		   	
		   			if(imageForOutput == null){
		   				System.out.println("First Time App No Hidden File Is Created");
		   				i=1;
		   			}
		   			else{
		   				String type = imageForOutput.getMetaData().get("level_level").toString();
		   				int a = Integer.parseInt(type);
		   				i = a +1;
		   			
		   			}																									
		   		 	    
		   	    //end of get level from database
		    BorderPane border = new BorderPane();  
		    HBox hbox = new HBox();
		    DBObject query = new BasicDBObject().append("metadata.level",level_file);
		    List<GridFSDBFile> files = photo.find(query);
		    status = "normal";
		    border.setTop(hbox);
		    border.setCenter(list_view_to_show_file);	    
			Scene scene = new Scene(border,700,700);
			stage.setScene(scene);
			//stage.show();
			stage.setOnCloseRequest(new EventHandler<WindowEvent>() {

				@Override
				public void handle(WindowEvent arg0) {
				
					
					
				}
		      });   
		
		    file_show_with_hiearchy(list_view_to_show_file,i,items_to_show_file,photo,text_file,pdf,folder,hbox,arrow_left,arrow_right,status,scene);
       
    }
 
    /**
     * For this dummy app, the (JavaFX scenegraph) content, just says "hello, world".
     * A real app, might load an FXML or something like that.
     *
     * @return the main window application content.
     */
    private Node createContent() {
        Label hello = new Label("hello, world");
        hello.setStyle("-fx-font-size: 40px; -fx-text-fill: forestgreen;");
        Label instructions = new Label("(click to hide)");
        instructions.setStyle("-fx-font-size: 12px; -fx-text-fill: orange;");
 
        VBox content = new VBox(10, hello, instructions);
        content.setAlignment(Pos.CENTER);
 
        return content;
    }
 
    /**
     * Sets up a system tray icon for the application.
     */
    private void addAppToTray() {
        try {
            // ensure awt toolkit is initialized.
            java.awt.Toolkit.getDefaultToolkit();
 
            // app requires system tray support, just exit if there is no support.
            if (!java.awt.SystemTray.isSupported()) {
                System.out.println("No system tray support, application exiting.");
                Platform.exit();
            }
            java.awt.SystemTray tray = java.awt.SystemTray.getSystemTray();
            // set up a system tray icon.
         /*   java.awt.SystemTray tray = java.awt.SystemTray.getSystemTray();
            URL imageLoc = new URL(
                    iconImageLoc
            );*/
        //    java.awt.Image image = ImageIO.read(imageLoc);
            image= ImageIO.read(getClass().getResource("folder.png"));
            java.awt.TrayIcon trayIcon = new java.awt.TrayIcon(image);
 
            // if the user double-clicks on the tray icon, show the main app stage.
            trayIcon.addActionListener(event -> Platform.runLater(this::showStage));
 
            // if the user selects the default menu item (which includes the app name), 
            // show the main app stage.
            java.awt.MenuItem openItem = new java.awt.MenuItem("hello, world");
            openItem.addActionListener(event -> Platform.runLater(this::showStage));
 
            // the convention for tray icons seems to be to set the default icon for opening
            // the application stage in a bold font.
          //  java.awt.Font defaultFont = java.awt.Font.decode(null);
         //   java.awt.Font boldFont = defaultFont.deriveFont(java.awt.Font.BOLD);
         //   openItem.setFont(boldFont);
 
            // to really exit the application, the user must go to the system tray icon
            // and select the exit option, this will shutdown JavaFX and remove the
            // tray icon (removing the tray icon will also shut down AWT).
            java.awt.MenuItem exitItem = new java.awt.MenuItem("Exit");
            exitItem.addActionListener(event -> {
                notificationTimer.cancel();
                Platform.exit();
                tray.remove(trayIcon);
            });
 
            // setup the popup menu for the application.
            final java.awt.PopupMenu popup = new java.awt.PopupMenu();
            popup.add(openItem);
            popup.addSeparator();
            popup.add(exitItem);
            trayIcon.setPopupMenu(popup);
 
            // create a timer which periodically displays a notification message.
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
 
            // add the application tray icon to the system tray.
            tray.add(trayIcon);
        } catch (java.awt.AWTException | IOException e) {
            System.out.println("Unable to init system tray");
            e.printStackTrace();
        }
    }
 
    /**
     * Shows the application stage and ensures that it is brought ot the front of all stages.
     */
    private void showStage() {
    stage.show();
       
    }
 
    public static void main(String[] args) throws IOException, java.awt.AWTException {
        // Just launches the JavaFX application.
        // Due to way the application is coded, the application will remain running
        // until the user selects the Exit menu option from the tray icon.
        launch(args);
    }
}