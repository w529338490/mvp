package my.easycommunity.utill;

import android.view.View;

/**
 * Created by Administrator on 2017/7/18.
 */
public class ProssBarUtil {
    public static void showBar(Object bar){
        if((View)bar!=null){
            ((View) bar).setVisibility(View.VISIBLE);
        }
    }
    public  static  void hideBar(Object bar){
        if((View)bar!=null){
            ((View) bar).setVisibility(View.GONE);
        }
    }
}
