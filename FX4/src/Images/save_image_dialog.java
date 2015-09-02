package Images;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.util.logging.Level;
import java.util.logging.Logger;

import org.json.simple.JSONObject;

import javafx.application.Application;
import javafx.event.EventHandler;
import javafx.scene.Group;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.input.MouseEvent;
import javafx.stage.FileChooser;
import javafx.stage.Stage;

public class save_image_dialog extends Application{
	 public static void SaveFile(JSONObject obj, File file){
	        try {
	            FileWriter fileWriter = null;
	             
	            fileWriter = new FileWriter(file);
	            fileWriter.write(obj.toString());
	            fileWriter.close();
	        } catch (IOException ex) {
	            Logger.getLogger(save_image_dialog.class.getName()).log(Level.SEVERE, null, ex);
	        }
	         
	    }

	public static void main(String[] args) {
		launch(args);

	}

	@Override
	public void start(Stage stage) throws Exception {
		Group root  = new Group();
		Scene scene = new Scene(root,500,500);
		stage.setScene(scene);
		stage.show();
		Button btn = new Button("export");
		btn.setOnMouseClicked(new EventHandler<MouseEvent>() {

			@Override
			public void handle(MouseEvent arg0) {
				JSONObject obj = new JSONObject();
				obj.put("chunks", "value");
				obj.put("file", "value of file");
				obj.put("type", "file");
				FileChooser fileChooser = new FileChooser();
				  
	              //Set extension filter
	              FileChooser.ExtensionFilter extFilter = new FileChooser.ExtensionFilter("JSON files (*.json)", "*.json");
	              fileChooser.getExtensionFilters().add(extFilter);
	              
	              //Show save file dialog
	              File file = fileChooser.showSaveDialog(stage);
	              
	              if(file != null){
	                  SaveFile(obj, file);
	              }
				
			}
		});
		root.getChildren().addAll(btn);
	
	
	
		
	}

}
