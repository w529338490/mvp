package my.easycommunity.ui.video.dzfragment;

import my.easycommunity.BasePresenter;

/**
 * Created by Administrator on 2017/7/18.
 */
public interface DzVideoPresenter extends BasePresenter
{
    void getDate(int  page);
    void stopNetWork();
    void itemOnclickLinster(int position);
}
