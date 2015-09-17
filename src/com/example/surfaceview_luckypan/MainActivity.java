package com.example.surfaceview_luckypan;

import android.support.v7.app.ActionBarActivity;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.ImageView;


public class MainActivity extends ActionBarActivity {

	private LuckyPan mLuckyPan;
	private ImageView mImageView;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        
        mLuckyPan = (LuckyPan) findViewById(R.id.id_luckyPan);
        mImageView = (ImageView) findViewById(R.id.id_start_btn);
        
        mImageView.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				
				if(!mLuckyPan.isStart()){
					
					mLuckyPan.LuckyStart(0);
					mImageView.setImageResource(R.drawable.stop);
				}
				else{
					
					if(!mLuckyPan.isShouldEnd()){
						
						mLuckyPan.LuckyEnd();
						mImageView.setImageResource(R.drawable.start);
					}
				}
				
			}
		});
        
        
       
    }


    
}
