// Holds the coordinate and color data for one piece of a Rubik's cube
public class Cubie {
  public static final int numberOfVertices = 24;
	
	public static float[] vertexData = {
		//Front
		+1.0f, +1.0f, +1.0f,
		+1.0f, -1.0f, +1.0f,
		-1.0f, -1.0f, +1.0f,
		-1.0f, +1.0f, +1.0f,
 
		//Top
		+1.0f, +1.0f, +1.0f,
		-1.0f, +1.0f, +1.0f,
		-1.0f, +1.0f, -1.0f,
		+1.0f, +1.0f, -1.0f,
 
		//Left
		+1.0f, +1.0f, +1.0f,
		+1.0f, +1.0f, -1.0f,
		+1.0f, -1.0f, -1.0f,
		+1.0f, -1.0f, +1.0f,
 
		//Back
		+1.0f, +1.0f, -1.0f,
		-1.0f, +1.0f, -1.0f,
		-1.0f, -1.0f, -1.0f,
		+1.0f, -1.0f, -1.0f,
 
		//Bottom
		+1.0f, -1.0f, +1.0f,
		+1.0f, -1.0f, -1.0f,
		-1.0f, -1.0f, -1.0f,
		-1.0f, -1.0f, +1.0f,
 
		//Right
		-1.0f, +1.0f, +1.0f,
		-1.0f, -1.0f, +1.0f,
		-1.0f, -1.0f, -1.0f,
		-1.0f, +1.0f, -1.0f,
 
		//Front
		1.0f, 0.5f, 0.0f, 1.0f,
		1.0f, 0.5f, 0.0f, 1.0f,
		1.0f, 0.5f, 0.0f, 1.0f,
		1.0f, 0.5f, 0.0f, 1.0f,
 
		//Top
		1.0f, 1.0f, 0.0f, 1.0f,
		1.0f, 1.0f, 0.0f, 1.0f,
		1.0f, 1.0f, 0.0f, 1.0f,
		1.0f, 1.0f, 0.0f, 1.0f,
 
		//Left
		0.0f, 0.0f, 1.0f, 1.0f,
		0.0f, 0.0f, 1.0f, 1.0f,
		0.0f, 0.0f, 1.0f, 1.0f,
		0.0f, 0.0f, 1.0f, 1.0f,
 
		//Back
		1.0f, 0.0f, 0.0f, 1.0f,
		1.0f, 0.0f, 0.0f, 1.0f,
		1.0f, 0.0f, 0.0f, 1.0f,
		1.0f, 0.0f, 0.0f, 1.0f,
 
		//Bottom
		1.0f, 1.0f, 1.0f, 1.0f,
		1.0f, 1.0f, 1.0f, 1.0f,
		1.0f, 1.0f, 1.0f, 1.0f,
		1.0f, 1.0f, 1.0f, 1.0f,
 
		//Right
		0.0f, 0.8f, 0.0f, 1.0f,
		0.0f, 0.8f, 0.0f, 1.0f,
		0.0f, 0.8f, 0.0f, 1.0f,
		0.0f, 0.8f, 0.0f, 1.0f,
	};
	
	/* 
	 * Every group of 3 values refers to three positions (3 floats each) and colors
	 * (4 floats each) from vertexData
	 */
	public static final short indexData[] =
	{
		0, 1, 2,
		2, 3, 0,
 
		4, 5, 6,
		6, 7, 4,
 
		8, 9, 10,
		10, 11, 8,
 
		12, 13, 14,
		14, 15, 12,
 
		16, 17, 18,
		18, 19, 16,
 
		20, 21, 22,
		22, 23, 20,
	};
}
