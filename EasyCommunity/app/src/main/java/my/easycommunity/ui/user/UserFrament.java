package my.easycommunity.ui.user;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import com.trello.rxlifecycle.components.support.RxFragment;

import java.util.List;

import my.easycommunity.R;
import my.easycommunity.base.BaseFragment;
import my.easycommunity.base.BasePresenter;

/**
 * Created by Administrator on 2017/7/17.
 */
public class UserFrament extends BaseFragment implements UserView
{
    private View view;

    private static UserFrament instance;

    public static  UserFrament newInstance(){
        if(instance == null){
            instance=new UserFrament();
        }
        return instance;
    }

    @Override
    public int getContentRes()
    {
        return 0;
    }

    @Override
    public BasePresenter getPresenter()
    {
        return null;
    }

    @Override
    public void setUpView()
    {

    }

    @Override
    public void iniData()
    {

    }

    @Override
    public Object gettyp()
    {
        return null;
    }

    @Override
    public boolean adapterIsEmpty()
    {
        return false;
    }

    @Override
    public void setData(List list)
    {

    }

    @Override
    public void onItemClickLinster(Object newsBean)
    {

    }
}
