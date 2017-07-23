package my.easycommunity.base;

import java.util.List;

import my.easycommunity.entity.news.Result;

/**
 * Created by Administrator on 2017/7/16.
 */

public interface BaseView<T>
{
    void showProgress();

    void hideProgress();

    void setData(List<T> list);

    void showError();

    void  addMoreErroe();

    void onItemClickLinster(T newsBean);

}
