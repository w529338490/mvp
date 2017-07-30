package my.easycommunity.ui.login;

import android.os.Handler;
import android.text.TextUtils;

import my.easycommunity.common.DaoController;
import my.easycommunity.db.gen.dbTable.User;

/**
 * Created by Administrator on 2017/7/29.
 */

public class LoginInteractorImpl implements LoginInteractor
{
    onCompletedLinster linster;
    DaoController userdao;

    public LoginInteractorImpl(onCompletedLinster linster)
    {
        this.linster = linster;
        userdao =new DaoController();
    }

    @Override
    public void Login(final String username, final String pwd)
    {

        /**
         * 延迟2秒执行，模拟网络登录
         */
        new Handler().postDelayed(new Runnable()
        {
            @Override
            public void run()
            {
                doLogin(username, pwd);
            }
        },2000);

    }

    private void doLogin(String username, String pwd)
    {
        if(TextUtils.isEmpty(username)){
            linster.onError("用户名为空");
            return;
        }else if(TextUtils.isEmpty(pwd)){
            linster.onError("密码错误");
            return;
        }

        if(userdao.isExitObject(User.class,"name",username)){
         String pass= ((User)userdao.getSigleObject(User.class,"name",username).get(0)).pwd;
            if(pass.equals(pwd) ){
                linster.onSuccess();
            }else {
                linster.onError("密码不正确");
            }
        }

    }
}
