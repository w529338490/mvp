package my.easycommunity.ui.photo;

import java.util.List;

import my.easycommunity.BaseView;
import my.easycommunity.entity.news.Result;
import my.easycommunity.entity.photo.GankPhoto;

/**
 * Created by Administrator on 2017/7/18.
 */
public interface PohotoView extends BaseView {

    void onItemClickLinster(List<GankPhoto.ResultsBean> photoList , int currentPosition );
    void setData(List<GankPhoto.ResultsBean> photoList);
    void  addMoreErroe();
}
