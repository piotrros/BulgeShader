package com.juhara.demo.imgprocessing;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.opengl.GLES20;
import android.opengl.GLUtils;

public class TextureHelper {
	public static int loadTextureFromRes(final Context ctx, final int resId) 
	{
        final int[] textures = new int[1];
        GLES20.glGenTextures(1, textures, 0);

        final int textureId = textures[0];
        GLES20.glBindTexture(GLES20.GL_TEXTURE_2D, textureId);
		
        GLES20.glTexParameteri(GLES20.GL_TEXTURE_2D, GLES20.GL_TEXTURE_MIN_FILTER, GLES20.GL_NEAREST);
		GLES20.glTexParameteri(GLES20.GL_TEXTURE_2D, GLES20.GL_TEXTURE_MAG_FILTER, GLES20.GL_NEAREST);

		final BitmapFactory.Options options = new BitmapFactory.Options();
		options.inScaled = false;   // No pre-scaling
	    Bitmap bitmap = BitmapFactory.decodeResource(ctx.getResources(), resId,options);

	    GLUtils.texImage2D(GLES20.GL_TEXTURE_2D, 0, bitmap, 0);
	    bitmap.recycle();
	       
	    return textureId;
		
	}

}
