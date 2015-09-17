package com.example.surfaceview_luckypan;

import android.content.Context;
import android.graphics.Canvas;
import android.util.AttributeSet;
import android.view.SurfaceHolder;
import android.view.SurfaceHolder.Callback;
import android.view.SurfaceView;

public class SurfaceView_example extends SurfaceView implements Callback, Runnable {

	private SurfaceHolder mHolder;
	/**
	 * ��SurfaceHolder�󶨵�Canvas
	 */
	private Canvas mCanvas;
	/**
	 * ���ڻ��Ƶ��߳�
	 */
	private Thread t;
	/**
	 * �̵߳Ŀ��ƿ���
	 */
	private boolean isRunning;
	
	public SurfaceView_example(Context context) {
		super(context,null);
		// TODO Auto-generated constructor stub
	}

	public SurfaceView_example(Context context, AttributeSet attrs) {
		super(context, attrs);
		
		mHolder = getHolder();
		mHolder.addCallback(this);
		
		//���ÿɻ�ȡ����
		setFocusable(true);
		setFocusableInTouchMode(true);
		
		//���ó���
		this.setKeepScreenOn(true);
		
	}

	@Override
	public void surfaceChanged(SurfaceHolder arg0, int arg1, int arg2, int arg3) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void surfaceCreated(SurfaceHolder holder) {
		
		isRunning = true;
		t = new Thread(this);
		t.start();
	}

	@Override
	public void surfaceDestroyed(SurfaceHolder arg0) {
		
		isRunning = false;
		
	}

	@Override
	public void run() {
		
		while(isRunning){
			
			darw();
		}
		
	}

	private void darw() {
		
		try {
			mCanvas = mHolder.lockCanvas();
			if(mCanvas != null){
				//drawing
			}
		} catch (Exception e) {
		}
		finally{
			
			if(mCanvas != null){
				mHolder.unlockCanvasAndPost(mCanvas);
			}
		}
	}

}
