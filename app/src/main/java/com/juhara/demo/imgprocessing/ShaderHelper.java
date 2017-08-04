package com.juhara.demo.imgprocessing;

import android.opengl.GLES20;
import android.util.Log;

public class ShaderHelper {
	/* utk debugging */
   private static final String TAG="ShaderHelper";
	
   public static int compileShader(final int shaderType, String shaderSource) 
   {
	   int shaderHandle=GLES20.glCreateShader(shaderType);
	   if (shaderHandle !=0)
	   {
		   GLES20.glShaderSource(shaderHandle, shaderSource);
		   GLES20.glCompileShader(shaderHandle);
		   
			final int[] compileStatus = new int[1];
			GLES20.glGetShaderiv(shaderHandle, GLES20.GL_COMPILE_STATUS, compileStatus, 0);
			// kompilasi gagal, hapus shader.
			if (compileStatus[0] == 0) 
			{
				Log.e(TAG, "Error compiling shader: " + GLES20.glGetShaderInfoLog(shaderHandle));
				GLES20.glDeleteShader(shaderHandle);
				shaderHandle = 0;
			}
	   }
	   return shaderHandle;
   }
   
   public static int createProgram(final int vtxShader, 
		   final int pxlShader,
		   final String[] attributes) 
   {
	   int programHandle=GLES20.glCreateProgram();
	   if (programHandle!=0)
	   {
	      GLES20.glAttachShader(programHandle, vtxShader);
	      GLES20.glAttachShader(programHandle, pxlShader);
		  // Bind attributes
		  if (attributes != null)
		  {
				final int size = attributes.length;
				for (int i = 0; i < size; i++)
				{
					GLES20.glBindAttribLocation(programHandle, i, attributes[i]);
				}						
		  }	      
	      GLES20.glLinkProgram(programHandle);
	      
	      //uji linking status
		  final int[] linkStatus = new int[1];
	      GLES20.glGetProgramiv(programHandle, GLES20.GL_LINK_STATUS, linkStatus, 0);
	      if (linkStatus[0]==0)
	      {
	    	 //linking gagal
			Log.e(TAG, "Error linking program: " + GLES20.glGetProgramInfoLog(programHandle));
			GLES20.glDeleteProgram(programHandle);
			programHandle = 0;
	    	  
	      }
	   }
	   return programHandle;
   }
}
