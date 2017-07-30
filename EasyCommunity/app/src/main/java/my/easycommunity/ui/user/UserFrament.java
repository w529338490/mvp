package my.easycommunity.ui.user;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.trello.rxlifecycle.components.support.RxFragment;

import java.util.List;

import my.easycommunity.base.BaseFragment;
import my.easycommunity.base.BasePresenter;
import my.easycommunity.base.BaseView;

/**
 * Created by Administrator on 2017/7/17.
 */
public class UserFrament extends RxFragment
{
    private View view;

    private static UserFrament instance;

    public static  UserFrament newInstance(){
        if(instance == null){
            instance=new UserFrament();
        }
        return instance;
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState)
    {
        return super.onCreateView(inflater, container, savedInstanceState);
    }
}
