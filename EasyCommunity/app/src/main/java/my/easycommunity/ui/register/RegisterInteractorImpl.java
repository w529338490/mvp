package my.easycommunity.ui.register;

import android.os.Handler;
import android.text.TextUtils;

import my.easycommunity.common.DaoController;
import my.easycommunity.db.gen.dbTable.User;

/**
 * Created by Administrator on 2017/7/29.
 */

public class RegisterInteractorImpl implements RegisterInteractor
{
    onCompletedLinster linster;
    DaoController userdao;

    public RegisterInteractorImpl(onCompletedLinster linster)
    {
        this.linster = linster;
        userdao =new DaoController();
    }

    @Override
    public void Register(final String username, final String pwd)
    {
        doRegister(username, pwd);

    }

    private void doRegister(String username, String pwd)
    {
        if(TextUtils.isEmpty(username))
        {
            linster.onError("用户名为空");
            return;
        }

        if(TextUtils.isEmpty(pwd)){
            linster.onError("密码不能为空");
            return;
        }

        if(userdao.isExitObject(User.class,"name",username))
        {
                linster.onError("该用户已经存在");
                return;
        }

        if(!TextUtils.isEmpty(username) && !TextUtils.isEmpty(pwd))
        {
            User user =new User();
            user.name =username;
            user.pwd =pwd;

            if(userdao.insertObject(user))
            {
                linster.onSuccess();

            }else
            {
                linster.onError("注册失败，请从试");

            }
        }

    }
}
