import java.nio.FloatBuffer;
import java.util.ArrayList;
 
import org.lwjgl.BufferUtils;
import org.lwjgl.util.vector.Matrix4f;
 
//Now we don't have to remember what version of OpenGL
//that every constant and function was introduced in
import static org.lwjgl.opengl.GL11.*;
import static org.lwjgl.opengl.GL15.*;
import static org.lwjgl.opengl.GL20.*;
import static org.lwjgl.opengl.GL30.*;
import static org.lwjgl.opengl.GL32.*;
 
// Stores a transformation matrix and child Nodes
public class Node {
	private Matrix4f transformMatrix;
	private ArrayList<Node> nodeList = new ArrayList<Node>();
	
	// Does this node draw a cube?
	private boolean isVisible = false;
	
	public Node(boolean visible) {
		this.transformMatrix = new Matrix4f();
		this.isVisible = visible;
	}
	
	public Node(Matrix4f transform, boolean visible) {
		this.transformMatrix = transform;
		this.isVisible = visible;
	}
	
	public Node setTransformMatrix(Matrix4f transform) {
		this.transformMatrix = transform;
		
		return this;
	}
	
	public Matrix4f getTransformMatrix() {
		return this.transformMatrix;
	}
	
	public Node addChild(Node child) {
		nodeList.add(child);
		
		return this;
	}
	
	/*public void addChildToBottom(Node child) {
		if(this.nodeList.isEmpty()) {
			addChild(child);
		} else {
			for(Node childNode : this.nodeList) {
				childNode.addChildToBottom(child);
			}
		}
	}
	*/
	
	public void transform(Matrix4f transform) {
		Matrix4f.mul(transform, this.transformMatrix, this.transformMatrix);
	}
	
	// Precondition: type of indices is unsigned short
	public void render(int modelToCameraMatrixUnif, int numOfIndices) {
		render(modelToCameraMatrixUnif, numOfIndices, new Matrix4f());
	}
	
	public void render(int modelToCameraMatrixUnif, int numOfIndices, Matrix4f parentTransform) {
		Matrix4f totalTransform = new Matrix4f();
		Matrix4f.mul(parentTransform, this.transformMatrix, totalTransform);
		
		if(isVisible) {
			FloatBuffer fb = BufferUtils.createFloatBuffer(16);
			totalTransform.store(fb);
			fb.flip();
			glUniformMatrix4(modelToCameraMatrixUnif, false, fb);
			
			glDrawElements(GL_TRIANGLES, numOfIndices, GL_UNSIGNED_SHORT, 0);
		}
		
		for(Node childNode : this.nodeList) {
			childNode.render(modelToCameraMatrixUnif, numOfIndices, totalTransform);
		}
	}
}
