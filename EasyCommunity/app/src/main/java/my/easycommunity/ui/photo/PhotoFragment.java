package my.easycommunity.ui.photo;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import butterknife.ButterKnife;
import butterknife.InjectView;
import com.trello.rxlifecycle.components.support.RxFragment;
import my.easycommunity.R;

/**
 * Created by Administrator on 2017/7/17.
 */
public class PhotoFragment extends RxFragment {

    private static PhotoFragment instance;
    private View view;
    @InjectView(R.id.recyclerView) RecyclerView recyclerView;
    private GridLayoutManager manager ;

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
            ButterKnife.inject(this,view);
        }
        return  view;
    }
}
