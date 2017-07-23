package my.easycommunity.ui.photo;

import android.content.Intent;
import android.support.design.widget.BottomNavigationView;;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import java.io.Serializable;
import java.util.List;
import my.easycommunity.R;
import my.easycommunity.adapter.PhotoFragmentAdapter;
import my.easycommunity.base.BaseFragment;
import my.easycommunity.base.BasePresenter;
import my.easycommunity.entity.photo.GankPhoto;

/**
 * Created by Administrator on 2017/7/17.
 */
public class PhotoFragment extends BaseFragment<GankPhoto.ResultsBean> implements PohotoView {

    private static PhotoFragment instance;
    private GridLayoutManager manager ;
    private PhotoFragmentAdapter  adapter;
    public BottomNavigationView tab_bottom;
    private   PhotoPresenter  photoPresenter;

    public static  PhotoFragment newInstance( BottomNavigationView tab_bottom ){
        if(instance == null){
            instance=new PhotoFragment();
            instance.tab_bottom =tab_bottom;
        }
        return instance;
    }

    @Override
    public void onItemClickLinster(List<GankPhoto.ResultsBean> photoList, int currentPosition)
    {
        Intent intent =new Intent(this.getActivity(),PhotoDetailActivity.class );
        intent.putExtra("photoList", (Serializable) photoList);
        intent.putExtra("position",currentPosition);
        startActivity(intent);
    }

    @Override
    public int getContentRes() {
        return R.layout.fragment_pohto;
    }

    @Override
    public BasePresenter getPresenter()
    {
        if(photoPresenter ==null){
            photoPresenter =new PhotoPresenterImpl(this.bindToLifecycle(),this,new PhotoInteractorImpl());
        }
        return photoPresenter;
    }

    @Override
    public void setUpView() {
        if(getContext()!=null){
            manager =new GridLayoutManager(getContext(),2);
        }
        error_tv.setOnClickListener(this);
        if(adapter==null){
            adapter =new PhotoFragmentAdapter();
        }
        recyclerView.setLayoutManager(manager);
        recyclerView.setAdapter(adapter);
    }

    @Override
    public void iniData() {
        adapter.setOnItemClickListener(new PhotoFragmentAdapter.OnItemClickListener()
        {
            @Override
            public void click(View parentView, int position ,List<GankPhoto.ResultsBean> data)
            {
                 photoPresenter.onItemClickLinster(data,position);
            }
        });

        recyclerView.addOnScrollListener(new RecyclerView.OnScrollListener()
        {
            @Override
            public void onScrolled(RecyclerView recyclerView, int dx, int dy)
            {
                super.onScrolled(recyclerView, dx, dy);
                if(dy<=0 ){
                    if(tab_bottom!=null){
                        tab_bottom.setVisibility(View.VISIBLE);
                    }
                }else {
                    if(tab_bottom!=null){
                        tab_bottom.setVisibility(View.GONE);
                    }
                }
            }
        });
    }

    @Override
    public Object gettyp(){ return page;}

    @Override
    public boolean adapterIsEmpty() {return adapter !=null && adapter.getItemCount()!=0;}

    @Override
    public void setData(List<GankPhoto.ResultsBean> list) { adapter.setData(list);}

    @Override
    public void onItemClickLinster(GankPhoto.ResultsBean newsBean) {}

//    //离开界面时候，停止数据加载
//    @Override
//    public void setUserVisibleHint(boolean isVisibleToUser)
//    {
//        Logger.e("setUserVisibleHint"+isVisibleToUser);
//        super.setUserVisibleHint(isVisibleToUser);
//        if (!isVisibleToUser && isAdded())
//        {
//            plmpl.stopNetWork();
//        }
//    }

}
