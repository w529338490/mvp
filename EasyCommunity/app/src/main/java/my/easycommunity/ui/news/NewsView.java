package my.easycommunity.ui.news;

import java.util.List;

import my.easycommunity.entity.news.Result;

/**
 * Created by Administrator on 2017/7/16.
 */

public interface NewsView
{
    void showProgress();

    void hideProgress();

    void setData(List<Result.ResultBean.DataBean> list);

    void showError();

    void addMore();

    void onItemClickLinster(Result.ResultBean.DataBean newsBean);

}
