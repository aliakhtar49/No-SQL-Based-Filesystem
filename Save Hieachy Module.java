
import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.LinkOption;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.attribute.BasicFileAttributes;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;

import sun.management.counter.Variability;

import com.mongodb.BasicDBObject;
import com.mongodb.DB;
import com.mongodb.DBCursor;
import com.mongodb.DBObject;
import com.mongodb.Mongo;
import com.mongodb.MongoClient;
import com.mongodb.gridfs.GridFS;
import com.mongodb.gridfs.GridFSDBFile;
import com.mongodb.gridfs.GridFSInputFile;

import javafx.application.Application;
import javafx.beans.value.ChangeListener;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.scene.Group;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.ContentDisplay;
import javafx.scene.control.ContextMenu;
import javafx.scene.control.Label;
import javafx.scene.control.ListView;
import javafx.scene.control.MenuItem;
import javafx.scene.control.cell.TextFieldListCell;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.input.DragEvent;
import javafx.scene.input.Dragboard;
import javafx.scene.input.MouseButton;
import javafx.scene.input.MouseEvent;
import javafx.scene.input.TransferMode;
import javafx.stage.Stage;
import javafx.util.StringConverter;
import javafx.util.converter.DefaultStringConverter;


public class save_final_with_dynamic_folder1 extends Application{
	static String current_Path = " " ;
	static int file_level=0;
	static String parent_folder_name= "root";

	public static void main(String[] args) {
		// TODO Auto-generated method stub
        launch(args);
	}
	
	 public static String getFileExtension(String  fileName) {
	        if(fileName.lastIndexOf(".") != -1 && fileName.lastIndexOf(".") != 0)
	        return fileName.substring(fileName.lastIndexOf(".")+1);
	        else return "";
	    }
	 
	@Override
	public void start(Stage primaryStage) throws Exception {
		Image text_file = new Image(getClass().getResourceAsStream("text_file.png"));
		Image pdf = new Image(getClass().getResourceAsStream("pdf.gif"));
		Image folder = new Image(getClass().getResourceAsStream("folder.png"));
		ListView<String> list_view_to_show_file = new ListView<String>();
		ObservableList<String> items_to_show_file = FXCollections.observableArrayList();
		Mongo mongo = new MongoClient("localhost",27017);	
		DB db1 = mongo.getDB("fyp1");
	    GridFS photo = new GridFS(db1,"photo"); 
	    list_view_to_show_file.setEditable(true);
	
	    String newFileName = ".^<>/";
		GridFSDBFile imageForOutput1 = null;
		try {
			 imageForOutput1 =photo.findOne(newFileName);
			
		} catch (Exception e) {
			
		}
		finally
		{
			if(imageForOutput1 == null){
				GridFSInputFile input;
	     		File image_file = new File("c:\\ali.txt");
	     		 DBObject obj = new BasicDBObject();
	      		obj.put( "level_level", file_level );
			input = photo.createFile(image_file);
			input.setFilename(".^<>/");
			input.setMetaData(obj);
			input.save();
				System.out.println("no is there first Time pen");
			}
			else{
				//check if it contain one file if it contain one file then no file is there therwise load all file system by using loop
				/*String type = imageForOutput.getMetaData().get("level_level").toString();
				int a = Integer.parseInt(type);*/		
			}																									
		}
		
		//Group and scene is initailized for creating windows
		 Group root = new Group();
	     Scene scene = new Scene(root, 551, 400);
	   // end Group and scene is initailized for creating windows 
 
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
		                     		obj.put("level", file_level);
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
		                         else{
		                        	 System.out.println("Folder Is Dropped");
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
	        
	        
	        /*Add  cell Factory for making a custom list =================================================================
		     * ==========================================================================================================
		     * ==========================================================================================================*/
		    
		    StringConverter<String> identityStringConverter = new DefaultStringConverter();
		    list_view_to_show_file.setCellFactory(lv -> new TextFieldListCell<String>(identityStringConverter) {
	            private ImageView folder1 = new ImageView(folder);
	            private ImageView pdf1 = new ImageView(pdf);
	            private ImageView text_file1 = new ImageView(text_file);

	            @Override
	            public void updateItem(String item, boolean empty) {
	                super.updateItem(item, empty);
	                if (! empty && ! isEditing()) {
	                	String extension  = getFileExtension(item);
	                    setStaticGraphic(item,extension);
	                	//System.out.println(item);
	                	 setOnMouseClicked(new EventHandler<MouseEvent>() {

	             			@Override
	             			public void handle(MouseEvent e) {
	             				if (e.getButton() == MouseButton.SECONDARY)
	             	            {
	             					DateFormat dateFormat = new SimpleDateFormat("yyyy/MM/dd HH:mm:ss");
	             					Date date = new Date();
	             					
	             				
	             					ContextMenu menu = new ContextMenu();
	             					MenuItem folder_create = new MenuItem("Open");
	             					folder_create.setGraphic(new ImageView(folder));
	             					folder_create.setOnAction(new EventHandler<ActionEvent>() {

	             						@Override
	             						public void handle(ActionEvent arg0) {
	             						
	             							//if(true) when it is folder that is n extension it incrment level 
	             							if(getFileExtension(item) == ""){
	             								parent_folder_name = item;
	             								list_view_to_show_file.getItems().clear();
												current_Path = current_Path+ "//" + item;
												file_level++;
												String newFileName = ".^<>/";
												//Update Meta data of File when level Increase
												GridFSDBFile imageForOutput =photo.findOne(newFileName);
												imageForOutput.setMetaData(new BasicDBObject("level_level",file_level));
												imageForOutput.save();
	             								System.out.println("Folder");
	             							}
	             							/*else{
	             								System.out.println("File ");
	             							}*/
	             							//System.out.println("Get File Extension " + getFileExtension(item));
	             							
	             							
	             						}
	             					});
	             					
	             					menu.getItems().addAll(folder_create);
	             					
	             					setContextMenu(menu);
	             				
	             					
	             	                //System.out.println("Desired Click");
	             	            }
	             	            else
	             	            {
	             	                //System.out.println("No right click");
	             	            }
	             			//	items_of_labels.remove(labels.getSelectionModel().getSelectedIndex());
	             				
	             				
	             			}
	             		});
	                	
	                  
	                }
	            }
	            @Override
	            public void cancelEdit() {
	            	System.out.println(list_view_to_show_file.getEditingIndex());
	            	if(list_view_to_show_file.getEditingIndex() == -1 ){
	            		super.cancelEdit();
	            	}
	            	else{
	            		String extension =  getFileExtension(list_view_to_show_file.getItems().get(list_view_to_show_file.getEditingIndex()));
	                	String item = list_view_to_show_file.getItems().get(list_view_to_show_file.getEditingIndex());
	                	super.cancelEdit();
	                	setStaticGraphic(item, extension);
	            	}         
	            }
	         
	            @Override
	            public void commitEdit(String newValue) {
	            	//here save filename and 
	            	
	            	String old_value = list_view_to_show_file.getItems().get(list_view_to_show_file.getEditingIndex());
	            	DBObject update_file = new BasicDBObject().append("filename", old_value).append("metadata.level", file_level);
					GridFSDBFile file = photo.findOne(update_file);
					String extension = file.getMetaData().get("extension").toString();
					
	            //	String extension  = getFileExtension(old_value);
	            	if(newValue.equals("")){       		            		
	            		cancelEdit();
	            	}
	            	else{
	            		System.out.println(newValue);
	            		file.put("filename" , newValue);
	            		file.save();
	            			super.commitEdit(newValue);                    
	                       
	            	
	            			 setStaticGraphic(newValue,extension);
	            		
	            		
	            	}            		               
	            }
	            
	            private void setStaticGraphic(String item , String extension) {
	            	//System.out.println(getFileExtension(item));
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
			                     		obj.put("level", file_level);
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
	        root.getChildren().addAll(list_view_to_show_file);
	        primaryStage.setScene(scene);
	        primaryStage.show();
	        
	}

}

