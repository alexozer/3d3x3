import java.io.File;
import java.io.FileNotFoundException;
import java.util.Scanner;
 
import org.lwjgl.opengl.GL20;
 
public class Shader {
	private int id;
	
	public Shader(int eShaderType, String sourceFile) {
		id = GL20.glCreateShader(eShaderType);
				
		String sourceCode = null;
		try {
			sourceCode = new Scanner(new File(sourceFile)).useDelimiter("\\Z").next();
		} catch (FileNotFoundException e) {
			e.printStackTrace();
		}
		
		GL20.glShaderSource(id, sourceCode);
		GL20.glCompileShader(id);
	}
	
	public int get() {
		return id;
	}
	
	public String getLog() {
		int infoLogLength = GL20.glGetShaderi(id, GL20.GL_INFO_LOG_LENGTH);
		return GL20.glGetShaderInfoLog(id, infoLogLength);
	}
}
