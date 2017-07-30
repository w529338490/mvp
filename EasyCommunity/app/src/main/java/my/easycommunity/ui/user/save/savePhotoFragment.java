package my.easycommunity.ui.user.save;

import com.trello.rxlifecycle.components.support.RxFragment;

/**
 * Created by Administrator on 2017/7/30.
 */

public class savePhotoFragment extends RxFragment
{

    private static savePhotoFragment instance;

    public static savePhotoFragment newInstance(){
        if(instance == null){
            instance=new savePhotoFragment();
        }
        return instance;
    }
}
