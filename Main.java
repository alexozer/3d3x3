public class Main {
	private int theProgram;
	private int modelToCameraMatrixUnif, cameraToClipMatrixUnif;
	
	private final float frustumScale, zNear = 1.0f, zFar = 45.0f;
	private float[] perspectiveMatrix;
 
	private int vertexAttrib, colorAttrib;
	
	private float calcFrustumScale(float fFovDeg) {
		double fFovRad = Math.toRadians(fFovDeg);
		return (float) (1.0 / Math.tan(fFovRad / 2.0));
	}
	
	private void initProgram()
	{
		ShaderProgram program = new ShaderProgram();
		
		program.addShader(new Shader(GL_VERTEX_SHADER, "screen.vert"));
		program.addShader(new Shader(GL_FRAGMENT_SHADER, "screen.frag"));
		
		boolean success = program.create();
		
		if(!success) {
			System.out.println(program.getLog());
		}
		
		this.theProgram = program.get();
		
		this.vertexAttrib = glGetAttribLocation(this.theProgram, "position");
		this.colorAttrib = glGetAttribLocation(this.theProgram, "color");
		
		modelToCameraMatrixUnif = 
				glGetUniformLocation(this.theProgram, "modelToCameraMatrix");
		cameraToClipMatrixUnif = 
				glGetUniformLocation(this.theProgram, "cameraToClipMatrix");
		
		this.perspectiveMatrix = new float[16];
		this.perspectiveMatrix[0] = frustumScale;
		this.perspectiveMatrix[5] = frustumScale;
		this.perspectiveMatrix[10] = (zFar + zNear) / (zNear - zFar);
		this.perspectiveMatrix[14] = (2 * zFar * zNear) / (zNear - zFar);
		this.perspectiveMatrix[11] = -1.0f;
		FloatBuffer fb = BufferUtils.createFloatBuffer(perspectiveMatrix.length);
		fb.put(perspectiveMatrix).flip();
		
		glUseProgram(this.theProgram);
		glUniformMatrix4(cameraToClipMatrixUnif, false, fb);
		glUseProgram(0);
	}
 
	private int vertexBufferObject, indexBufferObject;
	private int vao;
 
	// Initializes the buffers where the vertex, color, and index data goes
	private void initVertexBuffer()
	{
		vertexBufferObject = glGenBuffers();
		
		glBindBuffer(GL_ARRAY_BUFFER, vertexBufferObject);	
		FloatBuffer fb = BufferUtils.createFloatBuffer(Cubie.vertexData.length);
		fb.put(Cubie.vertexData).flip();
		glBufferData(GL_ARRAY_BUFFER, fb, GL_STATIC_DRAW);
		glBindBuffer(GL_ARRAY_BUFFER, 0);
		
		indexBufferObject = glGenBuffers();
		glBindBuffer(GL_ELEMENT_ARRAY_BUFFER, indexBufferObject);	
		ShortBuffer sb = BufferUtils.createShortBuffer(Cubie.indexData.length);
		sb.put(Cubie.indexData).flip();
		glBufferData(GL_ELEMENT_ARRAY_BUFFER, sb, GL_STATIC_DRAW);
		glBindBuffer(GL_ELEMENT_ARRAY_BUFFER, 0);
	}
	
	// Initializes the Vertex Object Array
	private void initVAO()
	{
		this.vao = glGenVertexArrays();
		glBindVertexArray(this.vao);
		
		int colorDataOffset = (Float.SIZE / 8) * 3 * Cubie.numberOfVertices;
		glBindBuffer(GL_ARRAY_BUFFER, this.vertexBufferObject);
		glEnableVertexAttribArray(vertexAttrib);
		glEnableVertexAttribArray(colorAttrib);
		glVertexAttribPointer(vertexAttrib, 3, GL_FLOAT, false, 0, 0);
		glVertexAttribPointer(colorAttrib, 4, GL_FLOAT, false, 0, colorDataOffset);
		glBindBuffer(GL_ELEMENT_ARRAY_BUFFER, indexBufferObject);
		
		glBindVertexArray(0);
	}
	
	// Initializes OpenGL
	private void init()
	{
		initProgram();
		initVertexBuffer();
		initVAO();
		
		glEnable(GL_CULL_FACE);
		glCullFace(GL_BACK);
		glFrontFace(GL_CW);
		
		glEnable(GL_DEPTH_TEST);
		glDepthMask(true);
		glDepthFunc(GL_LEQUAL);
		glDepthRange(0.0, 1.0);
	}
	
	// Returns a value in the range [0, 1) for the progress in the loop
	private float calcLoopRatio(int loopDuration) {
		return calcLoopRatio(loopDuration, getElapsedTime());
	}
	
	private float calcLoopRatio(int loopDuration, float currTime) {
		return (currTime % loopDuration) / loopDuration;
	}
	
	private final Vector3f xAxis = new Vector3f(1, 0, 0);
	private final Vector3f yAxis = new Vector3f(0, 1, 0);
	private final Vector3f zAxis = new Vector3f(0, 0, 1);
	
	private Node rootNode = new Node(false);
	private RubiksCube theCube = new RubiksCube(rootNode);
 
	private void display()
	{
		glClearColor(0.0f, 0.0f, 0.0f, 0.0f);
		glClearDepth(1.0);
		glClear(GL_COLOR_BUFFER_BIT | GL_DEPTH_BUFFER_BIT);
 
		glUseProgram(this.theProgram);
		glBindVertexArray(vao);
		
		Matrix4f allPiecesTransform = new Matrix4f();
		allPiecesTransform.translate(new Vector3f(0, 0, -20));
		
		long frameTime = getElapsedTime();
		
		allPiecesTransform.rotate((2 * (float)Math.PI) * calcLoopRatio(5000, frameTime), this.xAxis);
		allPiecesTransform.rotate((2 * (float)Math.PI) * calcLoopRatio(10000, frameTime), this.zAxis);
		rootNode.setTransformMatrix(allPiecesTransform);
		
		// Because the cube's Nodes were already added to the Node tree,
		// simply changing their transformation matrices updates the tree
		theCube.rotateFaceAuto(calcLoopRatio(400, frameTime));
 
		rootNode.render(modelToCameraMatrixUnif, Cubie.indexData.length);
		
		glBindVertexArray(0);
		glUseProgram(0);
	}
	
	private long initialTime;
	
	private long getElapsedTime() {
		return ( (Sys.getTime() * 1000) / Sys.getTimerResolution() ) - this.initialTime;
	}
	
	private void reshape(int width, int height) {
		this.perspectiveMatrix[0] = this.frustumScale / ((float)width / height);
		this.perspectiveMatrix[5] = this.frustumScale;
		
		FloatBuffer fb = BufferUtils.createFloatBuffer(this.perspectiveMatrix.length);
		fb.put(this.perspectiveMatrix).flip();
		
		glUseProgram(this.theProgram);
		glUniformMatrix4(this.cameraToClipMatrixUnif, false, fb);
		glUseProgram(0);
		
		glViewport(0, 0, width, height);
	}
 
	private final int kWidth, kHeight;
	
	public Main(int width, int height) {
		this.initialTime = getElapsedTime();
		this.kWidth = width;
		this.kHeight = height;
		
		frustumScale = calcFrustumScale(45.0f);
	}
	
	public static void main(String... args) {
		Main cool = new Main(800, 800);
		
		try {
			Display.setDisplayMode(new DisplayMode(cool.kWidth, cool.kHeight));
			Display.create();
		} catch(LWJGLException e) {
			e.printStackTrace();
			System.exit(-1);
		}
		cool.init();
		
		Display.setTitle("Rubiks Cube");
		cool.reshape(Display.getWidth(), Display.getHeight());
		Display.setResizable(true);
		
		while(!Display.isCloseRequested() && !Keyboard.isKeyDown(Keyboard.KEY_ESCAPE)) {
			if(Display.wasResized()) 
				cool.reshape(Display.getWidth(), Display.getHeight());
			cool.display();
			Display.sync(60);
			Display.update();
		}
		
		Display.destroy();
	}
}
