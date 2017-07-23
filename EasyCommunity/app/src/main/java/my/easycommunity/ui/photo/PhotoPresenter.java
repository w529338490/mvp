package my.easycommunity.ui.photo;

import java.util.List;

import my.easycommunity.MyBasePresenter;
import my.easycommunity.base.BasePresenter;
import my.easycommunity.entity.photo.GankPhoto;

/**
 * Created by Administrator on 2017/7/18.
 */
public interface PhotoPresenter  extends BasePresenter
{
    void onItemClickLinster(List<GankPhoto.ResultsBean> photoList , int currentPosition );
}
