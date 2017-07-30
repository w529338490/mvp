package my.easycommunity.ui.login;

import android.content.Intent;
import android.graphics.Canvas;
import android.os.Handler;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.FrameLayout;
import android.widget.ScrollView;

import butterknife.ButterKnife;
import butterknife.InjectView;
import de.greenrobot.event.EventBus;
import my.easycommunity.R;
import my.easycommunity.eventbus.MainFragmentEvent;
import my.easycommunity.ui.register.RegisterActivity;
import my.easycommunity.utill.MyLruChace;
import my.easycommunity.utill.ProssBarUtil;
import my.easycommunity.utill.ToastUtil;

public class LoginActivity extends AppCompatActivity implements  LoginView ,View.OnClickListener
{
    /**
     * 用户名
     */
    @InjectView(R.id.name)
    EditText name;
    /**
     * 密码
     */
    @InjectView(R.id.password)
    EditText password;
    /**
     * 登录按钮
     */
    @InjectView(R.id.sign_in_button)
    Button sign_in_button;
    /**
     * 注册按钮
     */
    @InjectView(R.id.regest_in_button)
    Button regest_in_button;

    /**
     * 登录界面父容器
     *
     */
    @InjectView(R.id.login_form)
    ScrollView login_form;
    /**
     * 登录时候的progress;
     */
    @InjectView(R.id.progress)
    public FrameLayout progress;

    private  LoginPresenter loginPresenter ;
    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        ButterKnife.inject(this);
        loginPresenter =new LoginPresenterImpl(this);
       // loginPresenter.start();
        setUpView();

    }

    private void setUpView()
    {
        regest_in_button.setOnClickListener(this);
        sign_in_button.setOnClickListener(this);
    }

    @Override
    public void onClick(View v)
    {
        switch (v.getId()){
            /**
             * 登录
             */
            case R.id.sign_in_button:
                loginPresenter.login(name.getText().toString(),password.getText().toString());
                break;

            /**
             * 注册
              */
            case R.id.regest_in_button:

                Intent intent =new Intent(this,RegisterActivity.class);
                startActivity(intent);

                break;
        }

    }
    @Override
    public void showProgress()
    {

        ProssBarUtil.hideBar(login_form);
        ProssBarUtil.showBar(progress);
    }

    @Override
    public void hideProgress()
    {
        ProssBarUtil.hideBar(progress);
        ProssBarUtil.showBar(login_form);
        ToastUtil.show("登录成功");
        /**
         * 保存用户，以便知道用户是登录状态
         */
        MyLruChace.Instace().sava("user",name.getText().toString());
        EventBus.getDefault().post(new MainFragmentEvent(2));
        finish();

    }

    @Override
    public void showError(String e)
    {
        ToastUtil.show(e);

    }

}
