package Images;

import java.awt.Desktop;
import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.io.RandomAccessFile;
import java.net.URL;
import java.nio.file.Path;
import java.nio.file.Paths;

import javafx.scene.media.Media;
import javafx.scene.media.MediaPlayer;

public class test {

	public static void main(String[] args) throws IOException {
		 File file = new File("C:\\Users\\dell\\Desktop\\Book1.xlsx");
		 Desktop dt = Desktop.getDesktop();
		 //Process p = Runtime.getRuntime().exec("dt.open(file)");
		 //Runtime rt = Runtime.getRuntime();
		 // Process proc = rt.exec("C:\\siraliismail.wma");
		
		 dt.open(file);				  
	}

}
