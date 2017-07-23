package my.easycommunity.base;

import my.easycommunity.MyBasePresenter;

/**
 * Created by Administrator on 2017/7/16.
 */

public interface BasePresenter extends MyBasePresenter
{
    void getDate(Object type);
    void stopNetWork();
    void itemOnclickLinster(int position);
}
