package my.easycommunity.ui.register;

/**
 * Created by Administrator on 2017/7/29.
 */

public class RegisterPresenterImpl implements  RegisterPresenter,RegisterInteractor.onCompletedLinster

{
    RegisterInteractor loginInteractor;
    RegisterView registeView ;

    public RegisterPresenterImpl(RegisterView registerView)
    {
        this.registeView = registerView;
        loginInteractor =new RegisterInteractorImpl(this);
    }

    @Override
    public void start()
    {
      //  registeView.showProgress();
    }

    @Override
    public void register(String name, String pwd)
    {
        loginInteractor.Register(name,pwd);
    }

    @Override
    public void onError(String error)
    {
        registeView.showError(error);
        registeView.hideProgress();
    }

    @Override
    public void onSuccess()
    {
        registeView.showProgress();

    }

}
