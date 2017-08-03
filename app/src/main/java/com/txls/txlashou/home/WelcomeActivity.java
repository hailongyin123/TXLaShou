package com.txls.txlashou.home;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.view.View;
import android.widget.ImageView;

import com.txls.txlashou.R;

/**
 * 启动页
 */
public class WelcomeActivity extends SuperActivity {
    //声明控件
    private ImageView start_app;
    private final long SPLASH_LENGTH = 1000;
    Handler mHandler = new Handler();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_welcome);
        mHandler.postDelayed(new Runnable() {
            @Override
            public void run() {
                Intent intent = new Intent(WelcomeActivity.this, LoginActivity.class);
                startActivity(intent);
                finish();
            }
        }, SPLASH_LENGTH);
        //1秒后跳转至应用主界面IndexActivity
//        //渐变动画效果
//        Animation animation = new AlphaAnimation(0.1f, 1.0f);
//        animation.setDuration(5000);
//        start_app.setAnimation(animation);
//        animation.start();
//        final String CACHE_IMG = Environment.getExternalStorageDirectory()+"/demo/";
//        final int TAG_PHOTO_CAMERA=200;
//
//        Intent intent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
//
//        String fileName = "defaultImage.jpg";
//
//        File file = new File(CACHE_IMG, fileName);
//
//        Uri imageUri= FileProvider.getUriForFile(this,"me.xifengwanzhao.fileprovider", file);//这里进行替换uri的获得方式
//
//        intent.putExtra(MediaStore.EXTRA_OUTPUT, imageUri);
//
//        intent.addFlags(Intent.FLAG_GRANT_READ_URI_PERMISSION);//这里加入flag
//
//        startActivityForResult(intent, TAG_PHOTO_CAMERA);

    }

    @Override
    public void initView() {
        start_app = (ImageView) findViewById(R.id.img_start_app);
    }


    @Override
    public void onClick(View v) {

    }
}
