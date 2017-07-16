package my.easycommunity.base;

import android.app.Activity;
import android.content.Context;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.trello.rxlifecycle.components.support.RxFragment;

import butterknife.ButterKnife;
import rx.Observable;

import static com.orhanobut.logger.Logger.init;

/**
 * Created by Administrator on 2017/7/15.
 */

public  abstract  class BaseFragment extends RxFragment
{
    private View mContentView;
    public Context mContext;
    public Observable.Transformer transformer;

    @Override
    public void onAttach(Activity activity)
    {
        super.onAttach(activity);
        this.mContext=getActivity();
    }

    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        mContentView = inflater.inflate(setContView(),container,false);
        ButterKnife.inject(mContentView);
        mContext = getContext();
        this.transformer =  this.bindToLifecycle();

        setUpView();
        setUpData();
        return mContentView;
    }

    protected abstract void setUpData();

    protected abstract void setUpView();

    public  abstract  int setContView();

}
