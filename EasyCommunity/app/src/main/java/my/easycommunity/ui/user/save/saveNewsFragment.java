package my.easycommunity.ui.user.save;

import com.trello.rxlifecycle.components.support.RxFragment;

import my.easycommunity.ui.video.VideoFragmet;

/**
 * Created by Administrator on 2017/7/30.
 */

public class saveNewsFragment extends RxFragment
{
    private static saveNewsFragment instance;

    public static saveNewsFragment newInstance(){
        if(instance == null){
            instance=new saveNewsFragment();
        }
        return instance;
    }
}
