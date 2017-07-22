package my.easycommunity.ui.photo;

import my.easycommunity.BasePresenter;

/**
 * Created by Administrator on 2017/7/18.
 */
public interface PhotoPresenter  extends BasePresenter{

    void getDate(int  page);
    void stopNetWork();
    void itemOnclickLinster(int position);
}
