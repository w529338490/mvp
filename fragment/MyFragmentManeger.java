package robot.nsk.com.robot_app.fragment;


import android.os.Handler;
import android.os.Looper;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;

import com.orhanobut.logger.Logger;


import robot.nsk.com.robot_app.units.ToastUtil;

/**
 * Created by Administrator on 2018/6/8.
 */

// fragment 管理类
public class MyFragmentManeger {
    private Handler maninHandler = new Handler(Looper.getMainLooper()) ;
    private static MyFragmentManeger myFragmentManeger ;
    public   FragmentManager mFragmentManager;
    private  FragmentTransaction ft;
    private BaseFragment currentFragment = null;
    private int  fragment_continer_id;

    public static MyFragmentManeger getInstance(){
        if (myFragmentManeger == null) {
            synchronized (MyFragmentManeger.class) {
                if (myFragmentManeger == null) {
                    myFragmentManeger = new MyFragmentManeger();
                }
            }
        }
        return myFragmentManeger;
    }

    public void setmFragmentManager(FragmentManager mFragmentManager) {
        if(mFragmentManager != null){
            this.mFragmentManager = mFragmentManager;
        }
    }

    public void setFragment_continer(int fragment_continer_id) {
        this.fragment_continer_id = fragment_continer_id;
    }

    public void setCurrentFragment(BaseFragment fragment) {
        this.currentFragment = fragment;
    }

    public void startFragment(final Class  fragment , final String tag) {
        this.startFragment2(fragment,tag,false);

    }
    public void startFragment2(final  Class  fragment , final  String tag  ,final Boolean remove ) {
        if(mFragmentManager == null){
            return;
        }
        if(currentFragment == null){
            return;
        }

        maninHandler.post(new Runnable() {
            @Override
            public void run() {
                if(ft == null){
                    ft = mFragmentManager.beginTransaction();
                }

                BaseFragment tagFragment=null;
                if(mFragmentManager.findFragmentByTag(tag) == null){
                    try {
                        tagFragment = (BaseFragment) fragment.newInstance();
                    } catch (InstantiationException e) {
                        e.printStackTrace();
                    } catch (IllegalAccessException e) {
                        e.printStackTrace();
                    }
                    if(!tagFragment.isAdded()){
                        ft.add(fragment_continer_id,tagFragment,tag);
                    }
                }else {
                    tagFragment = (BaseFragment) mFragmentManager.findFragmentByTag(tag);
                }
                if(currentFragment.equals(tagFragment) ){
                    ft.show(currentFragment);
                }else {
                    ft.show(tagFragment);
                    ft.hide(currentFragment);
                }
                if(remove){
                    ft.remove(currentFragment);
                }
                ft.commitAllowingStateLoss();
                ft = null ;
                currentFragment = tagFragment;

            }
        });

    }

    //按返回键  不断弹出fragment
    public void popFragment( final boolean remove){

        ToastUtil.toast(Thread.currentThread().getName()+"==============");
        maninHandler.post(new Runnable() {
            @Override
            public void run() {
                int currnetIdtor ;
                if(mFragmentManager != null && mFragmentManager.getFragments().size() > 0){
                    currnetIdtor = mFragmentManager.getFragments().indexOf(currentFragment);
                    if( currnetIdtor <= mFragmentManager.getFragments().size()-1 ){
                        currnetIdtor --;
                        if(currnetIdtor <= 0){
                            currnetIdtor= 0;
                        }
                        BaseFragment tagFragment = (BaseFragment) mFragmentManager.getFragments().get(currnetIdtor);
                        if(ft == null){
                            ft = mFragmentManager.beginTransaction();
                        }

                        if(tagFragment!= null){
                            ft.show(tagFragment);
                            ft.hide(currentFragment);
                        }
                        if(remove){
                            ft.remove(currentFragment);
                        }
                        ft.commitAllowingStateLoss();
                        ft = null ;
                        currentFragment = tagFragment;
                    }
                }
            }
        });

    }



    public void removeFragment(  final  String tag ){
        maninHandler.post(new Runnable() {
            @Override
            public void run() {
                if(ft == null){
                    ft = mFragmentManager.beginTransaction();
                }
                Fragment tagFragment = mFragmentManager.findFragmentByTag(tag);
                if(tagFragment== null){
                    return;
                }else {
                    ft.remove(tagFragment);
                    ft.commitNowAllowingStateLoss();
                    ft = null;
                }

            }
        });
    }
    boolean checkMainThread(){
        if(Thread.currentThread().getName().equals("main")){
            return true;
        }
        return false;
    }
}
