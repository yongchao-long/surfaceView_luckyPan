package com.example.surfaceview_luckypan;

import java.lang.reflect.TypeVariable;
import java.util.concurrent.Delayed;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.Path;
import android.graphics.RectF;
import android.renderscript.Type;
import android.util.AttributeSet;
import android.util.TypedValue;
import android.view.SurfaceHolder;
import android.view.SurfaceHolder.Callback;
import android.view.SurfaceView;
import android.widget.ImageView;

public class LuckyPan extends SurfaceView implements Callback, Runnable {

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
	
	/**
	 * �齱������
	 */
	private String[] name = new String[]{"�������", "IPAD", "��ϲ����", "IPHONE",  
            "����һֻ", "��ϲ����"};
	
	/**
	 * �����ֶ�ӦͼƬ
	 */
	private int[] Imgs = new int[]{R.drawable.danfan, R.drawable.ipad,  
            R.drawable.f040, R.drawable.iphone, R.drawable.meizi,  
            R.drawable.f040 };
	
	/**
	 * ÿ���̿����ɫ
	 */
	private int[] colors = new int[]{0xFFFFC300, 0xFFF17E01, 0xFFFFC300,  
            0xFFF17E01, 0xFFFFC300, 0xFFF17E01};
	
	/**
	 * �����ֶ�ӦͼƬ��bitmap����
	 */
	private Bitmap [] mImgsBitmaps;
	
	/**
	 * �̿�ĸ���
	 */
	private int mItemCounts = 6;
	
	/**
	 * �����̿�ķ�Χ
	 */
	private RectF mRange = new RectF();
	
	/**
	 * Բ��ֱ��
	 */
	private int mRadius;
	
	/**
	 * �������ֵĻ���
	 */
	private Paint mTextPaint;
	
	/**
	 * �����̿�Ļ���
	 */
	private Paint mArcPaint;
	
	/**
	 * �������ٶ�
	 */
	private double mSpeed;
	private volatile float mStartAngle = 0;
	
	/**
	 * �ؼ�������λ��
	 */
	private int mCenter;
	
	/** 
     * �ؼ���padding������������Ϊ4��padding��ֵһ�£���paddingleftΪ��׼ 
     */  
	private int mPadding;
	
	/**
	 * ����ͼ��bitmap
	 */
	private Bitmap mBgBitmap  = BitmapFactory.decodeResource(getResources(), R.drawable.bg2);
	
	/**
	 * ���ֵĴ�С
	 */
	private float mtextSize  = TypedValue.applyDimension(TypedValue.DENSITY_DEFAULT, 20, getResources().getDisplayMetrics());
	
	/**
	 * 
	 */
	private boolean isShouldEnd;
	
	public LuckyPan(Context context) {
		super(context,null);
		// TODO Auto-generated constructor stub
	}

	public LuckyPan(Context context, AttributeSet attrs) {
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
	protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
		// TODO Auto-generated method stub
		super.onMeasure(widthMeasureSpec, heightMeasureSpec);
		
		int width = Math.min(getMeasuredWidth(), getMeasuredHeight());
		mRadius = width - getPaddingLeft() - getPaddingRight();
		mPadding = getPaddingLeft();
		mCenter = width / 2;
		
		setMeasuredDimension(width, width);
	}

	@Override
	public void surfaceChanged(SurfaceHolder arg0, int arg1, int arg2, int arg3) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void surfaceCreated(SurfaceHolder holder) {
		
		//��ʼ�������̿�Ļ���
		mArcPaint = new Paint();
		mArcPaint.setAntiAlias(true);//����ȥ���
		mArcPaint.setDither(true);//���õ�ɫ
		
		//��ʼ���������ֵĻ���
		mTextPaint = new Paint();
		mTextPaint.setColor(0xffffffff);
		mTextPaint.setTextSize(mtextSize);
		
		//����Բ�̵ķ�Χ
		mRange = new RectF(mPadding, mPadding, mPadding + mRadius, mPadding + mRadius);
		
		//��ʼ��ͼƬ
		mImgsBitmaps = new Bitmap[mItemCounts];
		for(int i=0;i<mItemCounts;i++){
			
			mImgsBitmaps[i] = BitmapFactory.decodeResource(getResources(), Imgs[i]);
		}
		
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
			
			long start = System.currentTimeMillis();
			darw();
			long end = System.currentTimeMillis();
			
			if(end - start < 100){
				
				try {
					Thread.sleep(100 -(end - start));
				} catch (InterruptedException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
			}
		}
		
	}

	private void darw() {
		
		try {
			mCanvas = mHolder.lockCanvas();
			if(mCanvas != null){
				
				//���Ʊ���ͼ
				drawBg();
				
				
				float tmpAngle = mStartAngle;  //��ʼ�Ƕ�
                float sweepAngle = (float) (360 / mItemCounts);//ת���Ƕ�
                
				for(int i=0;i<mItemCounts;i++){
					//�����̿�
					mArcPaint.setColor(colors[i]);
					mCanvas.drawArc(mRange, tmpAngle, sweepAngle, true, mArcPaint);
					
					//�����ı�
					darwText(tmpAngle,sweepAngle,name[i]);
					
					//������Ƭ
					darwIcon(tmpAngle,mImgsBitmaps[i]);
					
					tmpAngle += sweepAngle;
					mStartAngle += mSpeed;
					
					//�ж��Ƿ�����ֹͣ��ť
					if(isShouldEnd){
						
						mSpeed -= 1;
					}
					
					if(mSpeed <=0){
						mSpeed = 0;
						isShouldEnd = false;
					}
				}
				
				
			}
		} catch (Exception e) {
		}
		finally{
			
			if(mCanvas != null){
				mHolder.unlockCanvasAndPost(mCanvas);
			}
		}
	}

	/**
	 * ����ͼƬ
	 * @param tmpAngle
	 * @param bitmap
	 */
	private void darwIcon(float startAngle, Bitmap bitmap) {
		
		//����ͼƬ��ʾ��С
		int imgWidth = mRadius / 8;
		
		float  angle = (float) ((30 + startAngle) *(Math.PI /180));
		
		float x = (float) (mCenter + mRadius/4 * Math.cos(angle));
		float y = (float) (mCenter + mRadius/4 * Math.sin(angle));
		
		//ȷ��ͼƬ��λ��
		RectF rectf = new RectF(x - imgWidth/2, y - imgWidth / 2, x + imgWidth/2, y + imgWidth / 2);
		
		mCanvas.drawBitmap(bitmap, null, rectf, null);
		
	}

	/**
	 * �����ı�
	 * @param stratAngle
	 * @param sweepAngle
	 * @param string
	 */
	private void darwText(float stratAngle, float sweepAngle, String string) {
		
		Path path = new Path();
		path.addArc(mRange, stratAngle, sweepAngle);
		//���ֵĳ���
		float textWidth = mTextPaint.measureText(string);
		//����ˮƽƫ����ʹ���־�����ʾ
		float hOffset = (float) (mRadius * Math.PI / mItemCounts / 2 - textWidth / 2);
		//���ô�ֱƫ����
		float vOffset = mRadius / 2 / 6 ;
		mCanvas.drawTextOnPath(string, path, hOffset, vOffset, mTextPaint);
		
	}

	/**
	 * ���Ʊ���ͼ
	 */
	private void drawBg() {
		
		mCanvas.drawColor(0xffffffff);
		mCanvas.drawBitmap(mBgBitmap, null, new RectF(mPadding / 2, mPadding / 2, 
				getMeasuredWidth() - mPadding /2, getMeasuredWidth() - mPadding / 2), null);
		
	}
	
	/**
	 * �����ʼת����ť
	 */
	public void LuckyStart(int index){
		
		float angle = (360 / mItemCounts);
		
		//�н��ĽǶȷ�Χ����һ��Ϊ210��270
		float from = 270 - (index + 1) * angle;
		float to = from + angle;
		
		//ֹͣʱ����ת����
		float targetFrom = 6 * 360 + from;
		float targetTo = 6 * 360 + to;
		
		/** 
         * <pre> 
         *  (v1 + 0) * (v1+1) / 2 = target ; 
         *  v1*v1 + v1 - 2target = 0 ; 
         *  v1=-1+(1*1 + 8 *1 * target)/2; 
         * </pre> 
         */  
        float v1 = (float) (Math.sqrt(1 * 1 + 8 * 1 * targetFrom) - 1) / 2;  
        float v2 = (float) (Math.sqrt(1 * 1 + 8 * 1 * targetTo) - 1) / 2;  
        
        mSpeed = v1 + Math.random() * (v2 - v1);
		isShouldEnd = false;
	}
	
	public void LuckyEnd(){
		
		isShouldEnd = true;
		mStartAngle = 0;
	}
	
	/**
	 * �ж��Ƿ���ת��
	 * @return
	 */
	public boolean isStart(){
		return mSpeed != 0;
	}
	
	public boolean isShouldEnd(){
		return isShouldEnd;
	}

}
