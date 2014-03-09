import org.lwjgl.util.vector.Matrix4f;
import org.lwjgl.util.vector.Vector3f;
 
public class RubiksCubeFace {
	private Node centerCubie;
	private Vector3f axis;
	
	public RubiksCubeFace(Node centerCubie, Vector3f axis) {
		this.centerCubie = centerCubie;
		this.axis = axis;		
	}
	
	private Node[] edgeCubies = new Node[4];
	
	public void addEdge(Node edge, int id) {
		edgeCubies[id] = edge;
	}
	
	private Node[] cornerCubies = new Node[4];
	
	public void addCorner(Node corner, int id) {
		cornerCubies[id] = corner;
	}
	
	private float currRatio = 0;
	
	public void rotateTo(float ratio, boolean direction, float turnPortion) {
		float incrementAngle = (ratio - currRatio) * 2 * (float)Math.PI * turnPortion;
		currRatio = ratio % 1;
		incrementAngle = direction ? incrementAngle : -incrementAngle;
		
		rotateAboutFace(centerCubie, incrementAngle);
		for(int i = 0; i < edgeCubies.length; i++)	{
			rotateAboutFace(edgeCubies[i], incrementAngle);
			rotateAboutFace(cornerCubies[i], incrementAngle);
		}
	}
	
	// Multiplies rotation on LEFT side
	private void rotateAboutFace(Node cubie, float incAngle) {
		Matrix4f rotateMat = new Matrix4f();
		rotateMat.rotate(incAngle, axis);
		Matrix4f oldMat = cubie.getTransformMatrix();
		cubie.setTransformMatrix(Matrix4f.mul(rotateMat, oldMat, null));
	}
	
	public void resetMatrixPointers(boolean lastTurnDirection) {
		if(lastTurnDirection) {
			rotateMatrixForward(edgeCubies);
			rotateMatrixForward(cornerCubies);
		} else {
			rotateMatrixBackwards(edgeCubies);
			rotateMatrixBackwards(cornerCubies);
		}
	}
	
	private void rotateMatrixForward(Node[] cubies) {
		Matrix4f temp = cubies[3].getTransformMatrix();
		for(int i = cubies.length - 1; i > 0; i--) {
			cubies[i].setTransformMatrix(cubies[i - 1].getTransformMatrix());
		}
		cubies[0].setTransformMatrix(temp);
	}
	
	private void rotateMatrixBackwards(Node[] cubies) {
		Matrix4f temp = cubies[0].getTransformMatrix();
		for(int i = 0; i < cubies.length - 1; i++) {
			cubies[i].setTransformMatrix(cubies[i + 1].getTransformMatrix());
		}
		cubies[3].setTransformMatrix(temp);
	}
}
