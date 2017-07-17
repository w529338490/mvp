package my.easycommunity.ui.photo;

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
public class PhotoFragment extends RxFragment {

    private static PhotoFragment instance;

    private View view;

    public static  PhotoFragment newInstance(){
        if(instance == null){
            instance=new PhotoFragment();
        }
        return instance;
    }
    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container,
            @Nullable Bundle savedInstanceState) {


        if(view==null) {
            view = inflater.inflate(R.layout.fragment_pohto, container, false);
        }
        return  view;
    }
}
