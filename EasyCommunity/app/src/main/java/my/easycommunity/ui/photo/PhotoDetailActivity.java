package my.easycommunity.ui.photo;

import android.graphics.Bitmap;
import android.graphics.drawable.BitmapDrawable;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.GestureDetector;
import android.view.MotionEvent;
import android.view.View;
import android.widget.Button;
import android.widget.FrameLayout;
import android.widget.ImageView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.bumptech.glide.request.animation.GlideAnimation;
import com.bumptech.glide.request.target.SimpleTarget;
import java.util.List;
import butterknife.ButterKnife;
import butterknife.InjectView;
import my.easycommunity.R;
import my.easycommunity.entity.photo.GankPhoto;
import my.easycommunity.utill.DownLoadUtil;
import my.easycommunity.utill.ProssBarUtil;
import my.easycommunity.utill.ToastUtil;

public class PhotoDetailActivity extends AppCompatActivity
{
    @InjectView(R.id.img) ImageView img;
    @InjectView(R.id.progress) FrameLayout progress;
    @InjectView(R.id.save_btn) Button save;
    private GestureDetector gestureDetector;
    private int count=-1;  //指示器
    List<GankPhoto.ResultsBean> photoList ;
    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_photo_detail);
        ButterKnife.inject(this);
        gestureDetector= new GestureDetector(this,new myGestureDetector());
        photoList = (List<GankPhoto.ResultsBean>) this.getIntent().getSerializableExtra("photoList");
        count=this.getIntent().getIntExtra("position",0);
        loadImage();
        saveImage();
    }

    private void saveImage()
    {
        save.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View v)
            {
                Bitmap ImageBit = ((BitmapDrawable)img.getDrawable()).getBitmap();
                DownLoadUtil.saveImageToNative(ImageBit);
            }
        });
    }

    @Override
    public boolean onTouchEvent(MotionEvent event)
    {
        gestureDetector.onTouchEvent(event);
        return super.onTouchEvent(event);
    }

    private class myGestureDetector extends GestureDetector.SimpleOnGestureListener
    {
        @Override
        public boolean onFling(MotionEvent e1, MotionEvent e2, float velocityX, float velocityY)
        {
            float  start =e1.getX();
            float   end=e2.getX();
            //向右
            if(end -start >=0){
                count +=1;
                loadImage();

            }
            else if(end -start <0){
                count -=1;
                loadImage();
            }
            return super.onFling(e1, e2, velocityX, velocityY);
        }
    }

    private void loadImage()
    {
        if(count<=0){
            count=0;
        }
        else  if(count >=photoList.size()){
            count=photoList.size()-1;
        }
       ProssBarUtil.showBar(progress);
          Glide.with(this)
                    .load(photoList.get(count).getUrl())
                    .asBitmap() //必须
                    .centerCrop()
                    .diskCacheStrategy( DiskCacheStrategy.NONE )
                    .into(new SimpleTarget<Bitmap>()
                    {
                        @Override
                        public void onResourceReady(Bitmap resource, GlideAnimation<? super Bitmap> glideAnimation)
                        {
                            if(resource!=null){
                                img.setImageBitmap(resource);
                                ProssBarUtil.hideBar(progress);
                            }
                        }
                    });
    }
}
