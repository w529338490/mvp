package robot.nsk.com.robot_app.fragment;


import android.app.Activity;
import android.content.Context;
import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.trello.rxlifecycle2.components.support.RxFragment;

import java.util.logging.Logger;

import butterknife.ButterKnife;
import butterknife.Unbinder;
import robot.nsk.com.robot_app.R;
import robot.nsk.com.robot_app.units.BaiDuYuYingUtils;

/**
 * A simple {@link Fragment} subclass.
 */
public abstract class BaseFragment extends RxFragment {
    Context context;
    Activity mActivity;
    Unbinder unbinder;
    public   View rootView;
    MyFragmentManeger myFragmentManeger  = MyFragmentManeger.getInstance();
    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        this.context =context;
        this.mActivity =getActivity();
    }

    public BaseFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        if(rootView == null){
            if(bindRootView() != 0){
                rootView = inflater.inflate(bindRootView(), null, false);
            }else {
                rootView = inflater.inflate(R.layout.fragment_base, null, false);
            }
        }
        if(getActivity() != null){
            context = getActivity();
        }
        unbinder =ButterKnife.bind(this,rootView);
        initView();
        return rootView ;
    }

    abstract  int    bindRootView();
    abstract  void    initView();
    abstract  void   initData();
    abstract  void   destroyData();



    @Override
    public void onDestroyView() {
        super.onDestroyView();
        if(unbinder !=null){
            unbinder.unbind();
        }
    }

    @Override
    public void onHiddenChanged(boolean hidden) {
        super.onHiddenChanged(hidden);
        com.orhanobut.logger.Logger.e(hidden+"=====================");
        if( !hidden){
            initData();
        }else {
            destroyData();
        }
    }

}
