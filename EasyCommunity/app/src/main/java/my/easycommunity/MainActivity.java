package my.easycommunity;

import android.content.res.Configuration;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.design.widget.AppBarLayout;
import android.support.design.widget.BottomNavigationView;
import android.support.design.widget.NavigationView;
import android.support.design.widget.Snackbar;
import android.support.design.widget.TabLayout;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.support.v4.view.GravityCompat;
import android.support.v4.view.PagerAdapter;
import android.support.v4.view.ViewPager;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.widget.Toolbar;
import android.view.MenuItem;
import android.view.View;
import android.widget.FrameLayout;
import android.widget.Toast;

import butterknife.ButterKnife;
import butterknife.InjectView;

import com.orhanobut.logger.Logger;
import com.trello.rxlifecycle.components.support.RxAppCompatActivity;
import java.util.ArrayList;
import my.easycommunity.adapter.PaperAdapter;
import my.easycommunity.ui.news.NewsFragment;
import my.easycommunity.ui.photo.PhotoFragment;
import my.easycommunity.ui.user.UserFrament;
import my.easycommunity.ui.video.VideoFragmet;
import my.easycommunity.utill.ToastUtil;

public class MainActivity extends RxAppCompatActivity implements View.OnClickListener, NavigationView.OnNavigationItemSelectedListener{

    @InjectView(R.id.mDrawerLayout)
    DrawerLayout mDrawerLayout;
    @InjectView(R.id.toolbar)
    Toolbar toolbar;

    @InjectView(R.id.tab)
    TabLayout tab;

    @InjectView(R.id.paper)
    ViewPager pager;
    PaperAdapter adapter;

    @InjectView(R.id.nv)
    NavigationView nv;

    @InjectView(R.id.appBarLayout)
    AppBarLayout appBarLayout;

    @InjectView(R.id.tab_bottom)
    BottomNavigationView tab_bottom ;

    @InjectView(R.id.fragment_Container)
    FrameLayout fragment_Container;

    private FragmentManager   fragmetManager =getSupportFragmentManager();
    private Fragment mCurrentFragment;


    ArrayList<Fragment> list = new ArrayList<>();
    private final String[] mTitles = {"头条", "科技", "社会", "国内", "娱乐"};
    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        ButterKnife.inject(this);
        mCurrentFragment = VideoFragmet.newInstance();

        if(savedInstanceState == null) {
            FragmentTransaction mCurTransaction = null;
            mCurTransaction=   fragmetManager.beginTransaction();
            mCurTransaction.add(R.id.fragment_Container, VideoFragmet.newInstance(), "video")
                    .hide(VideoFragmet.newInstance());
            mCurTransaction.add(R.id.fragment_Container, PhotoFragment.newInstance(),"photo")
                    .hide(PhotoFragment.newInstance());
            mCurTransaction.add(R.id.fragment_Container, UserFrament.newInstance(),"user")
                   .hide(UserFrament.newInstance());
            mCurTransaction.commitAllowingStateLoss();
        }else {
            getSupportFragmentManager().beginTransaction()
                    .hide(fragmetManager.findFragmentByTag("video")).commit();
            getSupportFragmentManager().beginTransaction()
                    .hide(fragmetManager.findFragmentByTag("photo")).commit();
            getSupportFragmentManager().beginTransaction()
                    .hide(fragmetManager.findFragmentByTag("user")).commit();
        }



        initView();
        initData();
    }

    private void initView() {
        showHome();

        nv.setNavigationItemSelectedListener(this);
        tab.setupWithViewPager(pager);
        toolbar.setNavigationOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View v)
            {
                mDrawerLayout.openDrawer(GravityCompat.START);
            }
        });

        //设置Toolbar和DrawerLayout实现动画和联动
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(
                this, mDrawerLayout, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        mDrawerLayout.addDrawerListener(toggle);
        toggle.syncState();
    }

    private void initData()
    {
        for (int i = 0; i < mTitles.length-2; i++)
        {
            NewsFragment newsf = NewsFragment.newInstance(i,appBarLayout);
            list.add(newsf);
        }
        adapter = new PaperAdapter(getSupportFragmentManager(), list, mTitles);
        pager.setAdapter(adapter);
        pager.addOnAdapterChangeListener(new ViewPager.OnAdapterChangeListener()
        {
            @Override
            public void onAdapterChanged(@NonNull ViewPager viewPager, @Nullable PagerAdapter oldAdapter, @Nullable PagerAdapter newAdapter)
            {
               Logger.e("oldAdapter======"+oldAdapter+"newAdapter================"+newAdapter);
            }
        });
        tab_bottom.setOnNavigationItemSelectedListener(
                new BottomNavigationView.OnNavigationItemSelectedListener() {
                    @Override
                    public boolean onNavigationItemSelected(@NonNull MenuItem item) {

                        switch (item.getItemId()){
                            case R.id.item_home:
                                showHome();
                                break;
                            case R.id.item_videos:
                                goneHome();
                                Fragment fFragment = fragmetManager.findFragmentByTag("video");
                                changeBottomFragment(fFragment,"video");
                                break;
                            case R.id.item_welfare:
                                goneHome();
                                Fragment pFragment = fragmetManager.findFragmentByTag("photo");

                               changeBottomFragment(pFragment,"photo");

                                break;
                            case R.id.item_user:
                                goneHome();
                                Fragment uFragment = fragmetManager.findFragmentByTag("user");
                                changeBottomFragment(uFragment,"user");

                                break;
                        }
                        return true;
                    }
                });
    }

    private void changeBottomFragment(Fragment fFragment,String name) {

        if( mCurrentFragment.isAdded()){
            FragmentTransaction transaction = fragmetManager.beginTransaction();
            transaction.hide(mCurrentFragment).commit();
            mCurrentFragment.setMenuVisibility(false);
            mCurrentFragment.setUserVisibleHint(false);
        }
        if ( fFragment.isAdded()) {
            FragmentTransaction transaction = fragmetManager.beginTransaction();
            transaction.show(fFragment).commit();
            fFragment.setMenuVisibility(true);
            fFragment.setUserVisibleHint(true);
        }
        mCurrentFragment =fFragment;
    }

    @Override
    public boolean onNavigationItemSelected(@NonNull MenuItem item)
    {
        switch (item.getItemId())
        {
            case R.id.nav_user:

                break;
            case R.id.nav_video:
                break;
            case R.id.nav_story:
                break;
            case R.id.nav_artcle:
                break;
        }
        return true;
    }
    long nowTime;

    @Override
    public void onClick(View view) {

        switch (view.getId()){

        }

    }

    @Override
    public void onBackPressed()
    {
        if (mDrawerLayout.isDrawerOpen(nv))
        {
            mDrawerLayout.closeDrawer(nv);
            return;
        }
        if (System.currentTimeMillis() - nowTime > 2000)
        {
            nowTime = System.currentTimeMillis();
            Snackbar snackbar = Snackbar.make(mDrawerLayout, "再按一次返回键退出程序", Snackbar.LENGTH_SHORT);
            snackbar.getView().setBackgroundColor(getResources().getColor(R.color.colorPrimary));
            snackbar.show();
        } else
        {
            finish();
        }
    }

    private void goneHome(){
        appBarLayout.setVisibility(View.GONE);
        pager.setVisibility(View.GONE);
        fragment_Container.setVisibility(View.VISIBLE);
    }
    private void showHome(){
        appBarLayout.setVisibility(View.VISIBLE);
        pager.setVisibility(View.VISIBLE);
        fragment_Container.setVisibility(View.GONE);
    }
    private static String makeFragmentName(int viewId, long id) {
        return "android:switcher:" + viewId + ":" + id;
    }

    @Override
    public void onConfigurationChanged(Configuration newConfig) {
        super.onConfigurationChanged(newConfig);
        //newConfig.orientation获得当前屏幕状态是横向或者竖向
        //Configuration.ORIENTATION_PORTRAIT 表示竖向
        //Configuration.ORIENTATION_LANDSCAPE 表示横屏
        if(newConfig.orientation==Configuration.ORIENTATION_PORTRAIT){
            Toast.makeText(MainActivity.this, "现在是竖屏", Toast.LENGTH_SHORT).show();
        }
        if(newConfig.orientation==Configuration.ORIENTATION_LANDSCAPE){
            Toast.makeText(MainActivity.this, "现在是横屏", Toast.LENGTH_SHORT).show();
        }
    }
}
