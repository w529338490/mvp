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
        doLogin(username, pwd);

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
        }else {
            linster.onError("亲，你还未注册！");
        }

    }
}
