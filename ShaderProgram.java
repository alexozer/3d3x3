import java.util.ArrayList;
 
import org.lwjgl.opengl.GL11;
import org.lwjgl.opengl.GL20;
 
 
public class ShaderProgram {
	private int id;
	private ArrayList<Shader> shaderList;
	
	public ShaderProgram() {
		shaderList = new ArrayList<Shader>();
		id = GL20.glCreateProgram();
	}
 
	public void addShader(Shader shader) {
		shaderList.add(shader);
	}
	
	public boolean create() {
		for (Shader shader : shaderList)
			GL20.glAttachShader(id, shader.get());
		
		GL20.glLinkProgram(id);
		
		for (Shader shader : shaderList)
			GL20.glDetachShader(id, shader.get());
		
		return GL20.glGetProgrami(id, GL20.GL_LINK_STATUS) == GL11.GL_TRUE;
	}
	
	public int get() {
		return id;
	}
	
	public String getLog() {
		int infoLogLength = GL20.glGetProgrami(id,
				GL20.GL_INFO_LOG_LENGTH);
		return GL20.glGetProgramInfoLog(id, infoLogLength);
	}
}
