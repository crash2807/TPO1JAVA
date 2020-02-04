package zad1;

import java.nio.file.FileVisitor;
import java.nio.file.Path;
import java.io.IOException;
import java.nio.ByteBuffer;
import java.nio.CharBuffer;
import java.nio.channels.FileChannel;
import java.nio.charset.Charset;
import java.nio.file.FileVisitResult;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.nio.file.StandardOpenOption;
import java.nio.file.attribute.BasicFileAttributes;

public class Futil implements FileVisitor<Path> {
	
	static String Name;
	static boolean first=true;


	public static void processDir(String dirName, String resultFileName) {
		
		Name=resultFileName;
		
		try {
			 
			Files.walkFileTree(Paths.get(dirName), new Futil());
			first=false;
			
		} catch (IOException e) {			
			e.printStackTrace();
		}
	
	}
	@Override
	public FileVisitResult postVisitDirectory(Path arg0, IOException arg1) throws IOException {
		
		return FileVisitResult.CONTINUE;
	}

	@Override
	public FileVisitResult preVisitDirectory(Path arg0, BasicFileAttributes arg1) throws IOException {
		
		return FileVisitResult.CONTINUE;
	}

	@Override
	public FileVisitResult visitFile(Path arg0, BasicFileAttributes arg1) throws IOException {
			
		 FileChannel channel=FileChannel.open(arg0);		
		 ByteBuffer buffer = ByteBuffer.allocate((int) channel.size());		 
		 channel.read(buffer);
		 Charset inCharset = Charset.forName("Cp1250"),
	             outCharset = Charset.forName("UTF-8");
		 buffer.flip();
		 CharBuffer buffer2 = inCharset.decode(buffer);
		 buffer = outCharset.encode(buffer2);
		 channel.close();
		 
		 if(first==true){
		    channel = FileChannel.open(Paths.get(Name),
					StandardOpenOption.CREATE,StandardOpenOption.WRITE,StandardOpenOption.TRUNCATE_EXISTING);
		    channel.write(buffer);
		    channel.close();
		    first=false;
		 }else{
			 channel = FileChannel.open(Paths.get(Name), StandardOpenOption.APPEND);
			 channel.write(buffer);
			 channel.close();
		 }
		
		return FileVisitResult.CONTINUE;
	}

	@Override
	public FileVisitResult visitFileFailed(Path arg0, IOException arg1) throws IOException {
		
		return FileVisitResult.CONTINUE;
	}
	
	
	
	
	
	
	
	
	

	
	
	
	
}
