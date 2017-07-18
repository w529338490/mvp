package my.easycommunity.ui.webview;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.KeyEvent;
import android.view.MotionEvent;
import android.view.View;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.widget.FrameLayout;
import android.widget.RatingBar;
import android.widget.RelativeLayout;
import butterknife.ButterKnife;
import butterknife.InjectView;
import com.orhanobut.logger.Logger;
import de.greenrobot.event.EventBus;
import my.easycommunity.R;
import my.easycommunity.eventbus.WebViewEvent;
import my.easycommunity.utill.MarqueeTextView;

public class WebActivity extends AppCompatActivity implements webView,View.OnClickListener,View.OnTouchListener
{
    @InjectView(R.id.toolbar)
    Toolbar toobar;
    @InjectView(R.id.webView)
    WebView webView;
    @InjectView(R.id.tittle)
    MarqueeTextView textView;

    @InjectView(R.id.progress)
    FrameLayout progress;

    @InjectView(R.id.bottom_ll)
    RelativeLayout bottom_ll;
    @InjectView(R.id.ratingbar)
    RatingBar ratingbar;
    WebViewEvent wEvent;
    private  WebPresenterImpl  webPresenter;

    private boolean canHide =true;

    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_web);

        ButterKnife.inject(this);
        EventBus.getDefault().registerSticky(this);
        webPresenter =new WebPresenterImpl(this);
        webPresenter.start();
         initView();
    }

    private void initView()
    {
        webPresenter.checkData(wEvent);
        bottom_ll.setOnClickListener(this);
        ratingbar.setOnClickListener(this);
        webView.setOnTouchListener(this);
        WebSettings webSettings = webView.getSettings();
        webSettings.setCacheMode(WebSettings.LOAD_DEFAULT);

        toobar.setNavigationOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View v)
            {
                finish();
            }
        });

        webView.setOnKeyListener(new View.OnKeyListener()
        {
            @Override
            public boolean onKey(View v, int keyCode, KeyEvent event)
            {
                if (keyCode == KeyEvent.KEYCODE_BACK && webView.canGoBack())
                {
                    webView.goBack();
                    return true;
                }
                    return false;
            }
        });
    }

    public void onEventMainThread(WebViewEvent event)
    {
        this.wEvent = event;
    }

    @Override
    public void showProgress()
    {
        progress.setVisibility(View.VISIBLE);
    }

    @Override
    public void hideProgress()
    {
        progress.setVisibility(View.GONE);
    }

    @Override
    public void showData()
    {
        webView.loadUrl(wEvent.getUrl());
        textView.setText( wEvent.getTitttle()  );

    }


    private float dy;
    float my;

    @Override
    public void onClick(View v)
    {
        switch (v.getId()){

            case R.id.ratingbar:
                ratingbar.setMax(1);
                break;
            case R.id.bottom_ll:
                Logger.e("bottom_ll");
                canHide=false;
                break;
        }
    }

    @Override
    public boolean onTouch(View v, MotionEvent event)
    {
        switch (event.getAction())
        {
            case MotionEvent.ACTION_DOWN:
                dy = event.getY();

                break;

            case MotionEvent.ACTION_MOVE:
                my = event.getY();
                float del = my - dy;
                if (del <= 0)
                {
                       bottom_ll.setVisibility(View.GONE);
                    if(!canHide){
                        bottom_ll.setVisibility(View.VISIBLE);
                        if(del<=-500){
                            canHide =true;
                        }
                    }

                } else
                {
                      bottom_ll.setVisibility(View.VISIBLE);
                }
                break;
        }
        return false;
    }

    @Override
    protected void onDestroy()
    {
        super.onDestroy();
        EventBus.getDefault().unregister(this);//反注册EventBus
        webPresenter.unsubscribe();
        webPresenter=null;
        wEvent =null;
    }

}
