package Images;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;

import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;
import org.json.simple.parser.ParseException;

import javafx.application.Application;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.scene.Group;
import javafx.scene.Scene;
import javafx.scene.control.Menu;
import javafx.scene.control.MenuBar;
import javafx.scene.control.MenuItem;
import javafx.stage.FileChooser;
import javafx.stage.Stage;
import jdk.nashorn.internal.scripts.JS;

public class trim_example extends Application {

	public static void main(String[] args) {
		launch(args);
		//String name = "ali akhtar A sdd DD";
		//System.out.println(name.replaceAll("\\s+","").toLowerCase());

	}

	@Override
	public void start(Stage stage) throws Exception {

		MenuBar menuBar = new MenuBar();
		menuBar.setPrefWidth(1000);
		Menu fileMenu = new Menu("File");
	    MenuItem import_menu_item = new MenuItem("Import...");
	    fileMenu.getItems().add(import_menu_item);
	    //exitMenuItem.setOnAction(actionEvent -> Platform.exit());
	    import_menu_item.setOnAction(new EventHandler<ActionEvent>() {
			
			@Override
			public void handle(ActionEvent arg0) {
				FileChooser fileChooser = new FileChooser();
				File selectedFile = fileChooser.showOpenDialog(null);
				 FileChooser.ExtensionFilter extFilter = new FileChooser.ExtensionFilter("JSON files (*.json)", "*.json");
		         fileChooser.getExtensionFilters().add(extFilter);
		         //fileChooser.setInitialDirectory(arg0);
		         fileChooser.setTitle("Choose Import Json  File");
		
				if (selectedFile != null) {
					//System.out.println("File selected: " + selectedFile.getPath().toString());
				
					JSONParser parser1 = new JSONParser();
			 		Object test_file;
					try {
						String parent = "aa";
						int level = 2;
						test_file = parser1.parse(new FileReader(selectedFile.getPath().toString()));
						JSONObject test_file_json = (JSONObject) test_file;
						
						JSONObject get_imported_files_json = (JSONObject) test_file_json;
						JSONObject get_metadata_json_from_imported_files_json= (JSONObject) get_imported_files_json.get("import_files");
						System.out.println(get_metadata_json_from_imported_files_json.get("filename"));
						JSONObject get_metadata_individual_field = (JSONObject) get_metadata_json_from_imported_files_json.get("metadata");
						get_metadata_individual_field.put("level", level);
						get_metadata_individual_field.put("parent_folder_name", parent);					
						String filename = test_file_json.get("import_type").toString();
				 		if(filename.equals("file")){
				 			Object import_files = new JSONObject();
				 			import_files = test_file_json.get("import_files");
				 			
				 		
				 			
				 			Object import_chunks = new JSONObject();
				 			import_chunks = test_file_json.get("import_chunks");
				 			 FileWriter import_files_file = new FileWriter("C:\\a\\import_files.json");
				 			 import_files_file.write(import_files.toString());
				 			 import_files_file.flush();
				 			 import_files_file.close();
				 	         
				 	         FileWriter import_chunks_file = new FileWriter("C:\\a\\import_chunks.json");
				 	         import_chunks_file.write(import_chunks.toString());
				 	         import_chunks_file.flush();
				 	         import_chunks_file.close();
				 			
				 			System.out.println(import_chunks);
				 		}
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
				else {
					System.out.println("File selection cancelled");
				    
				}
				
			}
		});

	    menuBar.getMenus().addAll(fileMenu);
		Group root= new Group();
		Scene scene = new Scene(root,500,500);
		root.getChildren().addAll(menuBar);
		stage.setScene(scene);
        stage.show();		
		
	}

}
