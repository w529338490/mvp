package my.easycommunity.ui.register;

import android.support.design.widget.Snackbar;
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
import my.easycommunity.utill.MyLruChace;
import my.easycommunity.utill.ProssBarUtil;
import my.easycommunity.utill.ToastUtil;

public class RegisterActivity extends AppCompatActivity implements RegisterView
{
    /**
     * 用户名
     */
    @InjectView(R.id.username)
    EditText name;
    /**
     * 密码
     */
    @InjectView(R.id.userpwd)
    EditText password;

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

    private RegisterPresenter presenter ;

    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);
        ButterKnife.inject(this);
        presenter =new RegisterPresenterImpl(this);

        regest_in_button.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View v)
            {
                if(presenter!=null){
                    presenter.start();
                    presenter.register(name.getText().toString(),password.getText().toString());
                }

            }
        });
    }

    @Override
    public void showProgress()
    {
        ProssBarUtil.hideBar(login_form);
        ProssBarUtil.showBar(progress);
        ToastUtil.show("注册成功！");
        finish();
    }

    @Override
    public void hideProgress()
    {
        ProssBarUtil.hideBar(progress);
        ProssBarUtil.showBar(login_form);
    }

    @Override
    public void showError(String e)
    {
        ToastUtil.show(e);

    }
    @Override
    public void onBackPressed()
    {
      finish();
    }
}
