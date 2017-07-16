package my.easycommunity.ui.webview;

import android.os.Build;
import android.support.v4.app.Fragment;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.view.KeyEvent;
import android.view.View;
import android.view.WindowManager;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.widget.FrameLayout;

import com.google.gson.annotations.JsonAdapter;
import com.orhanobut.logger.Logger;

import butterknife.ButterKnife;
import butterknife.InjectView;
import de.greenrobot.event.EventBus;
import my.easycommunity.R;
import my.easycommunity.eventbus.WebViewEvent;
import my.easycommunity.utill.MarqueeTextView;

import static com.orhanobut.logger.Logger.init;
import static com.orhanobut.logger.Logger.log;

public class WebActivity extends AppCompatActivity implements webView
{
    @InjectView(R.id.toolbar)
    Toolbar toobar;

    @InjectView(R.id.webView)
    WebView webView;
    @InjectView(R.id.tittle)
    MarqueeTextView textView;

    @InjectView(R.id.progress)
    FrameLayout progress;


    WebViewEvent wEvent;
    private  WebPresenterImpl  webPresenter;

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
        { // webview can
            // go back
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
    @Override
    protected void onDestroy()
    {
        super.onDestroy();
        // onDestroy的时候 销毁 EvenBus
        EventBus.getDefault().unregister(this);//反注册EventBus

        webPresenter.unsubscribe();
    }



}
