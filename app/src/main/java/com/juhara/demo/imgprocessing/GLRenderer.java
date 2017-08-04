package com.juhara.demo.imgprocessing;

import javax.microedition.khronos.egl.EGLConfig;
import javax.microedition.khronos.opengles.GL10;

import android.content.Context;
import android.opengl.GLES20;
import android.opengl.GLSurfaceView.Renderer;
import android.opengl.Matrix;

public class GLRenderer implements Renderer {

	private Context g_context=null;
	private int g_hShader=-1;
	private Quad g_quad=null;
	private int g_resTexId=0;
	
	private int g_matMVPHandle=-1;
	private int g_posHandle=-1;
	private int g_texHandle=-1;
	private int g_texCoordHandle=-1;
	private boolean g_recompile_shader=false;
	private String g_vertexShader=null;	
	
	public synchronized String getVertexShader() 
	{
		return g_vertexShader;
	}

	public synchronized void setVertexShader(String g_vertexShader) 
	{
		this.g_vertexShader = g_vertexShader;
	}

	private String g_fragmentShader=null;
	
	public synchronized String getFragmentShader() 
	{
		return g_fragmentShader;
	}

	public synchronized void setFragmentShader(String g_fragmentShader) 
	{
		this.g_fragmentShader = g_fragmentShader;
	}

	private float[] mMVPMatrix=new float[16];
	private float[] mProjectionMatrix=new float[16];
	private float[] mViewMatrix=new float[16];
	private float[] mModelMatrix=new float[16];
	
	private float g_scale=2.0f;
	private float g_time=0.0f;
	private long lastTimeMillis=System.currentTimeMillis();
	private int g_timeHandle=-1;
	
	public synchronized float getScale() 
	{
		return g_scale;
	}

	public synchronized void setScale(float g_scale) 
	{
		this.g_scale = g_scale;
	}

	public synchronized int getResTexId() 
	{
		return g_resTexId;
	}


	public synchronized void setResTexId(int resTexId) {
		this.g_resTexId = resTexId;
	}


	public GLRenderer(Context ctx) 
	{
		g_context=ctx;
	}
	
	
	private int initShader() 
	{
		int vtxShader= ShaderHelper.compileShader(
				       GLES20.GL_VERTEX_SHADER, 
				       g_vertexShader);
		int pxlShader= ShaderHelper.compileShader(
			       GLES20.GL_FRAGMENT_SHADER, 
			       g_fragmentShader);
		int a_hShader=ShaderHelper.createProgram(vtxShader, pxlShader, 
				new String[] { "a_Position", "a_TexCoordinate" });
		
		g_recompile_shader=false;
		return a_hShader;
	}


	private void initGeometry() 
	{
		g_quad = new Quad();
        g_texHandle=TextureHelper.loadTextureFromRes(g_context, g_resTexId);

	}

	private void initViewMatrix() 
	{

		// Position the eye in front of the origin.
		final float eyeX = 0.0f;
		final float eyeY = 0.0f;
		final float eyeZ = -0.5f;

		// We are looking toward the distance
		final float lookX = 0.0f;
		final float lookY = 0.0f;
		final float lookZ = -1.0f;

		// Set our up vector. This is where our head would be pointing were we
		// holding the camera.
		final float upX = 0.0f;
		final float upY = 1.0f;
		final float upZ = 0.0f;

		// Set the view matrix. This matrix can be said to represent the camera
		// position.
		// NOTE: In OpenGL 1, a ModelView matrix is used, which is a combination
		// of a model and
		// view matrix. In OpenGL 2, we can keep track of these matrices
		// separately if we choose.
		Matrix.setLookAtM(mViewMatrix, 0, eyeX, eyeY, eyeZ, lookX, lookY,
				lookZ, upX, upY, upZ);

		
	}
	
	private void initProjectionMatrix(int width, int height) 
	{
		// Set the OpenGL viewport to the same size as the surface.
		GLES20.glViewport(0, 0, width, height);

		// Create a new perspective projection matrix. The height will stay the
		// same
		// while the width will vary as per aspect ratio.
		final float ratio = (float) width / height;
		final float left = -ratio;
		final float right = ratio;
		final float bottom = -1.0f;
		final float top = 1.0f;
		final float near = 0.5f;
		final float far = 10.0f;
		
		Matrix.frustumM(mProjectionMatrix, 0, left, right, bottom, top, near, far);		
	}
	
	private void init() 
	{
		initViewMatrix();
		g_hShader=initShader();
		initGeometry();
		
        GLES20.glClearColor(0.0f, 0.0f, 0.0f, 0.0f);
        
        //sebenarnya tidak diperlukan karena kita hanya
        //merender sebuah quad yang selalu menghadap kamera
		GLES20.glEnable(GLES20.GL_CULL_FACE);
	}
	
	/*
	 * Jika kode shader menggunakan variabel
	 * bernama elapsed_time. Kita update isinya dengan
	 * waktu sejak update terakhir dalam detik.
	 */
	private void updateTime() 
	{
		if (g_timeHandle!=-1)
		{
			long currentmillis=System.currentTimeMillis();
			//g_time=(currentmillis-lastTimeMillis)/1000.0f;
			//GLES20.glUniform1f(g_timeHandle, g_time);
			GLES20.glUniform1f(g_timeHandle, 0.05f);
			lastTimeMillis=currentmillis;
		}
	}
	

	private void setTransform() 
	{
		Matrix.setIdentityM(mModelMatrix, 0);
		Matrix.translateM(mModelMatrix, 0, 0.0f, 0.0f, -1.5f);
		Matrix.scaleM(mModelMatrix, 0, g_scale, g_scale, 1.0f);
		
		Matrix.multiplyMM(mMVPMatrix, 0, mViewMatrix, 0, mModelMatrix, 0);

		// This multiplies the modelview matrix by the projection matrix, and
		// stores the result in the MVP matrix
		// (which now contains model * view * projection).
		Matrix.multiplyMM(mMVPMatrix, 0, mProjectionMatrix, 0, mMVPMatrix, 0);
		
		g_quad.setMVPMatrix(mMVPMatrix);
		
	}
	
	/*
	 * request untuk kompilasi ulang shader
	 * Ini diperlukan karena kita tidak dapat memanggil
	 * OpenGL ES API diluar GLThread
	 */
    public synchronized void recompileShader() 
    {
       g_recompile_shader=true;	
    }

	@Override
	public void onDrawFrame(GL10 gl) 
	{
        GLES20.glClear(GLES20.GL_DEPTH_BUFFER_BIT | GLES20.GL_COLOR_BUFFER_BIT);
        if (g_recompile_shader==true)
        {
        	g_hShader=initShader();
        }

        GLES20.glUseProgram(g_hShader);
        
		// ambil handle untuk data yang akan di pass
		g_matMVPHandle = GLES20.glGetUniformLocation(g_hShader,
				"u_MVPMatrix");
		g_texHandle = GLES20.glGetUniformLocation(g_hShader,
				"u_Texture");
		g_timeHandle = GLES20.glGetUniformLocation(g_hShader,
				"elapsed_time");
		g_posHandle = GLES20.glGetAttribLocation(g_hShader,
				"a_Position");
		g_texCoordHandle = GLES20.glGetAttribLocation(g_hShader,
				"a_TexCoordinate");
		
		updateTime();
		
        setTransform();
		g_quad.setVertexHandle(g_posHandle);
		g_quad.setShaderTexCoordHandle(g_texCoordHandle);
		g_quad.setShaderTexHandle(g_texHandle);
		g_quad.setShaderMVPHandle(g_matMVPHandle);




//		GLES20.glUniform1f(GLES20.glGetUniformLocation(g_hShader, "aspectRatio"), 1.5f);
//		float[] center = new float[2];
//		center[0] = center[1] = 0.5f;
//		GLES20.glUniform2fv(GLES20.glGetUniformLocation(g_hShader, "center"), 1, center, 0);
//		GLES20.glUniform1f(GLES20.glGetUniformLocation(g_hShader, "radius"), 0.5f);

		int scaleHandle = GLES20.glGetUniformLocation(g_hShader, "scale");
		GLES20.glUniform1f(scaleHandle, 1.0f);

        g_quad.draw();
	}
    
	@Override
	public void onSurfaceChanged(GL10 gl, int width, int height) 
	{
        initProjectionMatrix(width,height);
	}

	@Override
	public void onSurfaceCreated(GL10 arg0, EGLConfig arg1) 
	{
		init();
	}

}
