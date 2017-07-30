package my.easycommunity.ui.register;

/**
 * Created by Administrator on 2017/7/29.
 */

public interface RegisterView
{
    void showProgress();

    void hideProgress();

    void showError(String e);
}
