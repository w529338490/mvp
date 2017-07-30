package my.easycommunity.ui.user;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;

import com.trello.rxlifecycle.components.support.RxFragment;

import java.util.List;

import butterknife.ButterKnife;
import butterknife.InjectView;
import my.easycommunity.R;
import my.easycommunity.base.BaseFragment;
import my.easycommunity.base.BasePresenter;
import my.easycommunity.base.BaseView;
import my.easycommunity.ui.user.save.SaveActivity;

/**
 * Created by Administrator on 2017/7/17.
 */
public class UserFrament extends RxFragment implements  View.OnClickListener
{
    public View view;

    @InjectView(R.id.mysave)
    LinearLayout mysave;
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
        if(view == null){
            view=inflater.inflate(R.layout.fragment_user,container,false);
        }
        ButterKnife.inject(this,view);
        initview();
        setView();
        return view;
    }

    private void initview()
    {

    }

    private void setView()
    {
        mysave.setOnClickListener(this);
    }

    @Override
    public void onClick(View v)
    {
        switch (v.getId()){
            case R.id.mysave:
                if(getContext()!=null){
                    Intent save= new Intent(getContext(),SaveActivity.class);
                    startActivity(save);
                }

                break;
        }
    }
}
