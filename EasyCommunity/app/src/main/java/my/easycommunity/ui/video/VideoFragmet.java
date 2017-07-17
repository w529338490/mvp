package my.easycommunity.ui.video;

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
public class VideoFragmet extends RxFragment  {
    private View view;

    private static VideoFragmet instance;

    public static  VideoFragmet newInstance(){
        if(instance == null){
            instance=new VideoFragmet();
        }
        return instance;
    }
    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container,
            @Nullable Bundle savedInstanceState) {

        if(view==null){
            view =inflater.inflate(R.layout.fragment_video,container,false);

        }
        return view;
    }
}
