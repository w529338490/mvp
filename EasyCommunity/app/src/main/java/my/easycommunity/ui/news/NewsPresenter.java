package my.easycommunity.ui.news;

import my.easycommunity.BasePresenter;

/**
 * Created by Administrator on 2017/7/16.
 */

public interface NewsPresenter extends BasePresenter
{
    void getDate(String type);
    void stopNetWork();
    void itemOnclickLinster(int position);
}
