import java.util.Random;
 
import org.lwjgl.util.vector.Matrix4f;
import org.lwjgl.util.vector.Vector3f;
 
// Holds the state of the cube itself
public class RubiksCube {
	private RubiksCubeFace[] faces = new RubiksCubeFace[6];
	
	/*
	 * cubies[]:
	 * 0-5: centers
	 * 6-9: bottom edges
	 * 10-13: top edges
	 * 14-21: corners
	 * 22-25: side edges
	 */
	private Node[] cubies = new Node[26];
	
	public RubiksCube(Node rootNode) {
		addCenters();
		addEdgesAndCorners();
		
		for(Node cubie : cubies) {
			rootNode.addChild(cubie);
		}
	}
	
	private final float separation = 2.1f;
	
	private Vector3f[] faceOffsets = {
			new Vector3f(0, -separation, 0),
			new Vector3f(0, 0, separation),
			new Vector3f(separation, 0, 0),
			new Vector3f(0, 0, -separation),
			new Vector3f(-separation, 0, 0),
			new Vector3f(0, separation, 0),
	};
	
	private void addCenters() {
		for(int i = 0; i < faces.length; i++) {
			Node center = new Node(true)
				.setTransformMatrix(new Matrix4f().translate(faceOffsets[i]));
			cubies[i] = center;
			faces[i] = new RubiksCubeFace(center, faceOffsets[i].normalise(null));
		}
	}
	
	private void addEdgesAndCorners() {
		for(int halfCycle = 0; halfCycle < 2; halfCycle++) {
			for(int i = 1; i < 5; i++) {
				Vector3f translateVec = new Vector3f();
				
				/* Bottom and top edges */ {
					Node edge = new Node(true);
					
					Vector3f.add(faceOffsets[halfCycle * 5], faceOffsets[i], translateVec);
					Matrix4f translateMat = new Matrix4f()
						.translate(translateVec);
					edge.setTransformMatrix(translateMat);
					
					if(halfCycle == 0) {
						faces[0].addEdge(edge, 4 - i);
					} else {
						faces[5].addEdge(edge, i - 1);
					}
					faces[i].addEdge(edge, halfCycle * 2);
					
					cubies[5 + i + (4 * halfCycle)] = edge;
				}
				
				int adjacentFace = i % 4 + 1;
				
				/* Corners */ {
					Vector3f.add(translateVec, faceOffsets[adjacentFace], translateVec);
					Matrix4f translateMat = new Matrix4f().translate(translateVec);
					
					Node corner = new Node(true);
					corner.setTransformMatrix(translateMat);
	
					if(halfCycle == 0) {
						faces[0].addCorner(corner, 4 - i);
					} else {
						faces[5].addCorner(corner, i - 1);
					}
					
					faces[i].addCorner(corner, halfCycle);
					faces[adjacentFace].addCorner(corner, 3 - halfCycle);
					
					cubies[13 + i + (4 * halfCycle)] = corner;
				}
				
				/* Side edges */ {
					if(halfCycle == 0) {
						Matrix4f translateMat = new Matrix4f()
							.translate(Vector3f.add(faceOffsets[i], faceOffsets[adjacentFace], null));
						
						Node sideEdge = new Node(true);
						sideEdge.setTransformMatrix(translateMat);
						
						faces[i].addEdge(sideEdge, 1);
						faces[adjacentFace].addEdge(sideEdge, 3);
						
						cubies[21 + i] = sideEdge;
					}
				}
			}
		}	
	}
	
	private Random rand = new Random();
	private int currFace = rand.nextInt(faces.length);
	private float currTurnRatio = 0;
	private boolean currDirection = rand.nextInt(2) == 0;
	private float currTurnPortion = 1/4f;
	
	
	public void rotateFaceAuto(float turnRatio) {
		if(turnRatio < currTurnRatio) {
			faces[currFace].rotateTo(1, currDirection, currTurnPortion);
			faces[currFace].resetMatrixPointers(currDirection);
			
			int nextFace = rand.nextInt(faces.length);
			if(nextFace == currFace) {
				currFace = (nextFace + 1) % faces.length;
			} else {
				currFace = nextFace;
			}
			
			currDirection = rand.nextInt(2) == 0;
		}
		
		faces[currFace].rotateTo(turnRatio, currDirection, currTurnPortion);
		currTurnRatio = turnRatio;
	}
	
	public void rotateFace(int face, float turnRatio, boolean direction) {
		if(turnRatio < currTurnRatio || face != currFace || direction != currDirection) {
			faces[currFace].rotateTo(1, currDirection, currTurnPortion);
			currFace = face;
			currDirection = direction;
		}
		
		faces[face].rotateTo(turnRatio, direction, currTurnPortion);
		currTurnRatio = turnRatio;
	}
}
