package my.easycommunity.ui.login;

/**
 * Created by Administrator on 2017/7/29.
 */

public class LoginPresenterImpl implements  LoginPresenter,LoginInteractor.onCompletedLinster

{
    LoginInteractor loginInteractor;
    LoginView  loginView ;

    public LoginPresenterImpl(LoginView loginView)
    {
        this.loginView = loginView;
        loginInteractor =new LoginInteractorImpl(this);
    }

    @Override
    public void start()
    {
        loginView.showProgress();
    }
    @Override
    public void onError(String error)
    {
        loginView.showError(error);
    }

    @Override
    public void onSuccess()
    {
        loginView.hideProgress();

    }



    @Override
    public void login(String name, String pwd)
    {
        loginInteractor.Login(name,pwd);
    }
}
