package com.juhara.demo.imgprocessing;

import java.nio.ByteBuffer;
import java.nio.ByteOrder;
import java.nio.FloatBuffer;
import java.nio.ShortBuffer;

import android.opengl.GLES20;

public class Quad {
	private int matMVPHandle=0;
	private float[] mMVPMatrix = new float[16];
	
	public float[] getMVPMatrix() 
	{
		return mMVPMatrix;
	}

	public void setMVPMatrix(float[] mMVPMatrix) 
	{
		this.mMVPMatrix = mMVPMatrix;
	}

	private FloatBuffer vertexBuffer=null;
	private ShortBuffer indexBuffer=null;
	private FloatBuffer texCoordBuffer=null;
	private int texHandle=0;
	private int posShaderHandle=0;
	private int texCoordShaderHandle=0;
	
	public int getVertexHandle() {
		return posShaderHandle;
	}

	public void setVertexHandle(int vtxHandle) 
	{
		this.posShaderHandle = vtxHandle;
	}
	
	public void setShaderTexCoordHandle(int handle) {
		texCoordShaderHandle=handle;
	}
	
	public void setShaderTexHandle(int handle) {
		texHandle=handle;
	}
	
	public void setShaderMVPHandle(int handle) 
	{
	   matMVPHandle=handle;	
	}
	
	private float squareCoords[] = 
	{ 
			-1.0f,  1.0f, 0.0f,   // top left
			-1.0f, -1.0f, 0.0f,   // bottom left
			1.0f, -1.0f, 0.0f,   // bottom right
			1.0f,  1.0f, 0.0f    // top right 
	};
	
	private short squareIndices[] = 
	{ 
			0,1,2, //poligon 0 
			0,2,3  //poligon 1 
	}; 
	
	//OpenGL ES di Android, origin koordinat 
	//tekstur Y di mulai di kiri atas
	private	float textureCoords[] = 
	{ 
			0.0f, 0.0f,  // top left
			0.0f, 1.0f,  // bottom left
			1.0f, 1.0f,  // bottom right
			1.0f, 0.0f // top right
	}; 

	private FloatBuffer initFloatBuffer(float[] buffer) 
	{
		//4= tipe float butuh 4 byte
		ByteBuffer bb = ByteBuffer.allocateDirect(buffer.length * 4);
		bb.order(ByteOrder.nativeOrder());
		FloatBuffer aFloatBuffer = bb.asFloatBuffer();
		aFloatBuffer.put(buffer);
		aFloatBuffer.position(0);
		return aFloatBuffer;		
	}
	
	private ShortBuffer initShortBuffer(short[] buffer) 
	{
		//2= tipe short butuh 2 byte
		ByteBuffer bb = ByteBuffer.allocateDirect(buffer.length * 2);
		bb.order(ByteOrder.nativeOrder());
		ShortBuffer aShortBuffer = bb.asShortBuffer();
		aShortBuffer.put(buffer);
		aShortBuffer.position(0);
		return aShortBuffer;		
	}
	
	private void initGeometry() 
	{
		vertexBuffer=initFloatBuffer(squareCoords);
		texCoordBuffer=initFloatBuffer(textureCoords);
        indexBuffer=initShortBuffer(squareIndices);		
	}
	
	
	public Quad() 
	{
       initGeometry();
	}
	
	private void setTexture() 
	{
		GLES20.glActiveTexture(GLES20.GL_TEXTURE0);
		GLES20.glBindTexture(GLES20.GL_TEXTURE_2D, texHandle);
		GLES20.glUniform1i(texHandle, 0);
		
	}
	private void sendMVPMatrixToShader() 
	{
		// Pass in the combined matrix.
		GLES20.glUniformMatrix4fv(matMVPHandle, 1, false, mMVPMatrix, 0);
	}
	
	private void drawGeometry() {
		// Pass in the position information
		vertexBuffer.position(0);
		GLES20.glVertexAttribPointer(posShaderHandle, 
				3, //jumlah koordinat per vertex
				GLES20.GL_FLOAT, 
				false, 
				0, vertexBuffer);

		GLES20.glEnableVertexAttribArray(posShaderHandle);

		// Pass in the texture coordinate information
		texCoordBuffer.position(0);
		GLES20.glVertexAttribPointer(texCoordShaderHandle,
				2, GLES20.GL_FLOAT, false, 0,
				texCoordBuffer);

		GLES20.glEnableVertexAttribArray(texCoordShaderHandle);

		// Gambar square, 6=total isi index buffer = 2 poligon * 3 index/poligon
		GLES20.glDrawElements(GLES20.GL_TRIANGLES, 6, GLES20.GL_UNSIGNED_SHORT, indexBuffer);
		
		
	}
	public void draw()
	{		
        setTexture();		
	    sendMVPMatrixToShader();	
		drawGeometry();
	}
}
