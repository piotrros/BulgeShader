package com.juhara.demo.imgprocessing;


import android.opengl.GLSurfaceView;
import android.os.Bundle;
import android.app.Activity;
import android.view.Menu;
import android.view.MenuItem;
import android.view.MotionEvent;
import android.view.ScaleGestureDetector;
import android.view.ScaleGestureDetector.OnScaleGestureListener;
import android.view.View;
import android.view.View.OnTouchListener;

public class MainActivity extends Activity {
    public class ScaleListener implements OnScaleGestureListener {

		@Override
		public boolean onScale(ScaleGestureDetector detector) 
		{
			if (renderer!=null)
			{
				float ascale=renderer.getScale() * detector.getScaleFactor();
				//batasi skala antara 0.2-5.0
				ascale = Math.max(0.2f, Math.min(ascale, 5.0f));
				renderer.setScale(ascale);
			}
			return true;
		}

		@Override
		public boolean onScaleBegin(ScaleGestureDetector detector) 
		{
			//kembalikan true agar onScale bisa dipanggil
			return true;
		}

		@Override
		public void onScaleEnd(ScaleGestureDetector detector) {

		}
	}

	private GLRenderer renderer=null;	
    private ScaleGestureDetector mScaleDetector;    
    
	@Override
	protected void onCreate(Bundle savedInstanceState) 
	{
		super.onCreate(savedInstanceState);
		mScaleDetector = new ScaleGestureDetector(this, new ScaleListener());

		renderer=new GLRenderer(this);		
		renderer.setVertexShader(ResourceHelper.readRawTextFile(this, R.raw._vertex_shader));
		renderer.setFragmentShader(ResourceHelper.readRawTextFile(this, R.raw._fragment_shader));
		renderer.setResTexId(R.drawable.kayla);
		
		GLSurfaceView view=new GLSurfaceView(this);
		view.setOnTouchListener(new OnTouchListener() {

			@Override
			public boolean onTouch(View arg0, MotionEvent ev) 
			{
				mScaleDetector.onTouchEvent(ev);
				return true;
			}});
		//hanya menggunakan OpenGL ES 2.0
		view.setEGLContextClientVersion(2);
		view.setRenderer(renderer);		
		setContentView(view);		
	}

	@Override
	public boolean onOptionsItemSelected(MenuItem item) 
	{
		int fragment_shader;
		
		switch(item.getItemId()) 
		{
		   case R.id.menu_shader_original:
				fragment_shader=R.raw._fragment_shader;			   
			   break;
		   case R.id.menu_shader_edgedetect:
				fragment_shader=R.raw.edge_detect_fragment_shader;			   
			   break;
		   case R.id.menu_shader_blur:
				fragment_shader=R.raw.blurring_fragment_shader;			   
			   break;
		   case R.id.menu_shader_invert:
				fragment_shader=R.raw.invert_fragment_shader;			   
			   break;
		   case R.id.menu_shader_emboss:
				fragment_shader=R.raw.emboss_fragment_shader;			   
			   break;
		   case R.id.menu_shader_flip:
				fragment_shader=R.raw.flip_fragment_shader;			   
			   break;
		   case R.id.menu_shader_twirl:
				fragment_shader=R.raw.twirl_fragment_shader;			   
			   break;
		   case R.id.menu_shader_warp:
				fragment_shader=R.raw.warp_fragment_shader;			   
			   break;
		   case R.id.menu_shader_toon:
				fragment_shader=R.raw.toon_fragment_shader;			   
			   break;
		   case R.id.menu_shader_hue:
				fragment_shader=R.raw.hueshift_fragment_shader;			   
			   break;
		   case R.id.menu_shader_luminance:
				fragment_shader=R.raw.luminance_fragment_shader;			   
			   break;
		   case R.id.menu_shader_mirror:
				fragment_shader=R.raw.wave_fragment_shader;			   
			   break;
			case R.id.menu_shader_bulge:
				fragment_shader=R.raw.bulge_fragment_shader;
				break;
		   default:
				fragment_shader=R.raw._fragment_shader;			   
			   break;
		}
		
		if (renderer!=null)
		{
		   renderer.setFragmentShader(ResourceHelper.readRawTextFile(this, fragment_shader));		   
		   renderer.recompileShader();
		}
		
		return super.onOptionsItemSelected(item);
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) 
	{
		getMenuInflater().inflate(R.menu.main, menu);
		return true;
	}

}
