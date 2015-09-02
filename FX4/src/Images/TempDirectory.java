package Images;

import java.io.File;
import java.io.IOException;
import java.nio.file.FileVisitResult;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.SimpleFileVisitor;
import java.nio.file.attribute.BasicFileAttributes;

class TempDirectory {
    final Path path;

//    public TempDirectory() {
//        try {
//            path = Files.createTempDirectory("amberdb");
//        } catch (IOException e) {
//            throw new RuntimeException(e);
//        }
//    }
    public TempDirectory() {
    	path = File.createTempFile("Resume", "pdf", "C:\\");
    	
     // path = Files.createTempDirectory("amberdb");
    }
    public Path getPath() {
        return path;
    }

    public void deleteOnExit() {
    	System.out.println("Exit");
        Runtime.getRuntime().addShutdownHook(new Thread() {
            @Override
            public void run() {
                delete();
            }
        });
    }
    
  
    public void delete() {
        if (!Files.exists(path)) {
            return;
        }
        try {
            Files.walkFileTree(path, new SimpleFileVisitor<Path>() {
                @Override
                public FileVisitResult postVisitDirectory(Path dir, IOException exc)
                        throws IOException {
                    Files.deleteIfExists(dir);
                    return super.postVisitDirectory(dir, exc);
                }

                @Override
                public FileVisitResult visitFile(Path file, BasicFileAttributes attrs)
                        throws IOException {
                    Files.deleteIfExists(file);
                    return super.visitFile(file, attrs);
                }
            });
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

}
