
import java.io.File;
import java.io.IOException;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import com.mongodb.BasicDBObject;
import com.mongodb.DB;
import com.mongodb.DBCursor;
import com.mongodb.DBObject;
import com.mongodb.Mongo;
import com.mongodb.MongoClient;
import com.mongodb.gridfs.GridFS;
import com.mongodb.gridfs.GridFSDBFile;




import com.mongodb.gridfs.GridFSInputFile;
import com.mongodb.util.JSON;
import com.sun.org.apache.xerces.internal.util.Status;

import javafx.application.Application;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.geometry.Insets;
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
import javafx.scene.input.MouseButton;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.HBox;
import javafx.stage.Stage;
import javafx.util.StringConverter;
import javafx.util.converter.DefaultStringConverter;


public class display_final  extends Application {
	 static int level_file = 0; 
	 static String parent_folder_name= "root";
	 static String status;
			
	public static void main(String[] args) {
		launch(args);	
	}
	 public static String getFileExtension(String  fileName) {
	        if(fileName.lastIndexOf(".") != -1 && fileName.lastIndexOf(".") != 0)
	        return fileName.substring(fileName.lastIndexOf(".")+1);
	        else return "";
	    }
	 /*=====================================================================================================================
	  * ======================================================================================================================
	  * ======================================================================================================================
	  * =====================================================================================================================
	  * ======================================================================================================================
	  * ====================================================================================================================*/
	 //method call when double click on folder next level of hiearchy will show 
	 public  void  file_show_with_hiearchy(ListView<String> list_view_to_show_file,int i ,ObservableList<String> items_to_show_file,GridFS photo ,Image text_file,Image pdf, Image folder,HBox hbox,Image arrow_left,Image arrow_right,String status){
		// Image text_file = new Image(getClass().getResourceAsStream("text_file.png"));
		//	Image pdf = new Image(getClass().getResourceAsStream("pdf.gif"));
		//	Image folder = new Image(getClass().getResourceAsStream("folder.png"));
		 
		 
		 
		 /*Hbox */
		 
		 hbox.setPadding(new Insets(15, 15, 15, 12));
		    hbox.setSpacing(15);
		    hbox.setStyle("-fx-background-color: #336699;");	    
		    Button buttonCurrent = new Button();
		    buttonCurrent.setGraphic(new ImageView(arrow_left));
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
				level_file--;
				list_view_to_show_file.getItems().clear();
				hbox.getChildren().clear();
				
				file_show_with_hiearchy(list_view_to_show_file, i, items_to_show_file, photo, text_file, pdf, folder, hbox, arrow_left, arrow_right,"back");
				}
			});
		
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
					level_file++;
					list_view_to_show_file.getItems().clear();
					hbox.getChildren().clear();
					file_show_with_hiearchy(list_view_to_show_file, i, items_to_show_file, photo, text_file, pdf, folder, hbox, arrow_left, arrow_right,"forward");
					
				}
			});
		    
		    Label show_path = new Label();
		    show_path.setPadding(new Insets(0, 0, 0, 20));
		  //  show_path.setMaxWidth(350.0);
		    show_path.setMinWidth(350.0);
		    show_path.setMinHeight(25.0);
		    show_path.setLineSpacing(25);
		   
		    show_path.setStyle("-fx-background-color: 	#DCDCDC;");
		    
		    if(level_file == 0){
		    	buttonCurrent.setDisable(true);
		    }
		   /* else if(level_file == i){
		    	forward.setDisable(true);
		    }*/
		    else if(level_file > 0 && level_file < i ){
		    	buttonCurrent.setDisable(false);
		    	forward.setDisable(false);
		    }
		    	
		    hbox.getChildren().addAll(buttonCurrent, forward, show_path);
		 /*End f HBox*/
		    DBObject query;
		// System.out.println("Status varible shows it is " + status);
		 if(status == "normal"){
			 query = new BasicDBObject().append("metadata.level",level_file).append("metadata.parent_folder_name", parent_folder_name);
		 }
		 else{
			 query = new BasicDBObject().append("metadata.level",level_file);
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
		    	//System.out.println(files);
		    }
	        for (GridFSDBFile file : files) {        
	        	//System.out.println(file.getFilename());
	        	//System.out.println(file.getMetaData());
	        	String type = file.getMetaData().get("type").toString();
	        	list_view_to_show_file.getItems().add(file.getFilename());
	        	//System.out.println(type);
	        	
	        	
	        			       
	        }//end of for loop
	        
	        
	        
	        
	        
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
	             								list_view_to_show_file.getItems().clear();
	        									hbox.getChildren().clear();
	        									
	        									level_file = level_file + 1;
	        									parent_folder_name = item;
	        									if(level_file < i){
	        										System.out.println(level_file);
	        										//list_view_to_show_file.getItems().clear();
	        										file_show_with_hiearchy(list_view_to_show_file,i,items_to_show_file,photo,text_file,pdf,folder,hbox,arrow_left,arrow_right,"normal");
	        									}
	        									
	        									else{
	        										//Modify it on 3/23/15 when last thing it is not showing
	        							file_show_with_hiearchy(list_view_to_show_file,i,items_to_show_file,photo,text_file,pdf,folder,hbox,arrow_left,arrow_right,"normal");
	        										System.out.println("End of File Hiearcgu");
	        									} 		
	             							//When CLick on the folder to open its mean double clik on th folder
	             							}
	             							else{
	             								System.out.println("File ");
	             							}
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
	            	//System.out.println(list_view_to_show_file.getEditingIndex());
	            	if(list_view_to_show_file.getEditingIndex() == -1 ){
	            		System.out.println(list_view_to_show_file.getSelectionModel().getSelectedIndex());
	            		String extension =  getFileExtension(list_view_to_show_file.getSelectionModel().getSelectedItem());
	                	String item = list_view_to_show_file.getItems().get(list_view_to_show_file.getSelectionModel().getSelectedIndex());
	                	super.cancelEdit();
	                	setStaticGraphic(item, extension);
	            		//super.cancelEdit();
	            		
	            	}
	            	else{
	            		//String extension =  getFileExtension(list_view_to_show_file.getItems().get(list_view_to_show_file.getEditingIndex()));
	                	String item = list_view_to_show_file.getItems().get(list_view_to_show_file.getEditingIndex());
	                	DBObject update_file = new BasicDBObject().append("filename", item).append("metadata.level", level_file);
						GridFSDBFile file = photo.findOne(update_file);
						String extension = file.getMetaData().get("extension").toString();
	                	super.cancelEdit();
	                	setStaticGraphic(item, extension);
	            	}         
	            }
	         
	            @Override
	            public void commitEdit(String newValue) {
	            	//here save filename and 
	            	
	            	String old_value = list_view_to_show_file.getItems().get(list_view_to_show_file.getEditingIndex());
	            	DBObject update_file = new BasicDBObject().append("filename", old_value).append("metadata.level", level_file);
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
	        
	 }

	 //End of method  method call when double click on folder next level of hiearchy will show 
	 /*=====================================================================================================================
	  * ======================================================================================================================
	  * ======================================================================================================================
	  * =====================================================================================================================
	  * ======================================================================================================================
	  * ====================================================================================================================*/
	 
	 
	 
	 //Start of our application ============================================================================================
	 /*===================================================================================================================
	  * ===================================================================================================================
	  * ===================================================================================================================*/
	@Override
	public void start(Stage stage) throws Exception {
		Image text_file = new Image(getClass().getResourceAsStream("text_file.png"));
		Image pdf = new Image(getClass().getResourceAsStream("pdf.gif"));
		Image folder = new Image(getClass().getResourceAsStream("folder.png"));
		 Image arrow_left = new Image(getClass().getResourceAsStream("arrow_left.png"));
		 Image arrow_right = new Image(getClass().getResourceAsStream("arrow_right.png"));
		ListView<String> list_view_to_show_file = new ListView<String>();
		ObservableList<String> items_to_show_file = FXCollections.observableArrayList();
		list_view_to_show_file.setEditable(true);
		 Mongo mongo = new MongoClient("localhost",27017);	
			DB db1 = mongo.getDB("fyp1");
		    GridFS photo = new GridFS(db1,"photo"); 
	    String current_path = "/";	   	 
	    int i;
	    //get level from database
	   	    
	   	    String newFileName = ".^<>/";
	   		GridFSDBFile imageForOutput = null;
	   		try {
	   			 imageForOutput =photo.findOne(newFileName);
	   			
	   		} catch (Exception e) {
	   			
	   		}
	   		finally
	   		{
	   			if(imageForOutput == null){
	   				System.out.println("no is there first Time pen");
	   				i=1;
	   			}
	   			else{
	   				String type = imageForOutput.getMetaData().get("level_level").toString();
	   				int a = Integer.parseInt(type);
	   				i = a +1;
	   			
	   			}																									
	   		}
	   	    
	   	    //end of get level from database
	    BorderPane border = new BorderPane();  
	    HBox hbox = new HBox();
	    DBObject query = new BasicDBObject().append("metadata.level",level_file);
	    List<GridFSDBFile> files = photo.find(query);
	    status = "normal";
	    file_show_with_hiearchy(list_view_to_show_file,i,items_to_show_file,photo,text_file,pdf,folder,hbox,arrow_left,arrow_right,status);
 
	  
	 
	   
	    border.setTop(hbox);
	    border.setCenter(list_view_to_show_file);
	    
	    
	    
	    
	    
	    
	  //  hbox.getChildren().addAll(list_view_to_show_file);
		Scene scene = new Scene(border,700,700);
		stage.setScene(scene);
		stage.show();
		
		
	} //start application method end here 

}
