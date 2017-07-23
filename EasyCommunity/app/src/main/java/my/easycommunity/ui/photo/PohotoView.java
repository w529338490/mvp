package my.easycommunity.ui.photo;

import java.util.List;

import my.easycommunity.MyBaseView;
import my.easycommunity.base.BaseView;
import my.easycommunity.entity.photo.GankPhoto;

/**
 * Created by Administrator on 2017/7/18.
 */
public interface PohotoView extends BaseView<GankPhoto.ResultsBean>
{
    void onItemClickLinster(List<GankPhoto.ResultsBean> photoList , int currentPosition );
}
