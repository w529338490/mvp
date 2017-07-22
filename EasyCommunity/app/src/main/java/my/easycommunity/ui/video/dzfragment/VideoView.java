package my.easycommunity.ui.video.dzfragment;

import java.util.List;

import my.easycommunity.BaseView;
import my.easycommunity.entity.photo.GankPhoto;
import my.easycommunity.entity.video.Video;

/**
 * Created by Administrator on 2017/7/18.
 */
public interface VideoView extends BaseView {

    void setData(List<Video.DataBean.DataBeans> listData);
}
