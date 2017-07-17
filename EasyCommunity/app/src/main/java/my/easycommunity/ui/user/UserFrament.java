package my.easycommunity.ui.user;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import com.trello.rxlifecycle.components.support.RxFragment;
import my.easycommunity.R;

/**
 * Created by Administrator on 2017/7/17.
 */
public class UserFrament extends RxFragment {
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
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container,
            @Nullable Bundle savedInstanceState) {

        if(view==null){
            view =inflater.inflate(R.layout.fragment_user,container,false);

        }
        return view;
    }
}
