package my.easycommunity.ui.login;

/**
 * Created by Administrator on 2017/7/29.
 */

public interface LoginView
{
    void showProgress();

    void hideProgress();

    void showError(String e);
}
