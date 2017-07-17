package my.easycommunity;

import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.design.widget.AppBarLayout;
import android.support.design.widget.BottomNavigationView;
import android.support.design.widget.NavigationView;
import android.support.design.widget.Snackbar;
import android.support.design.widget.TabLayout;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.support.v4.view.GravityCompat;
import android.support.v4.view.ViewPager;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.widget.Toolbar;
import android.view.MenuItem;
import android.view.View;
import android.widget.FrameLayout;
import butterknife.ButterKnife;
import butterknife.InjectView;
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

    private FragmentManager fragmetManager;
    private FragmentTransaction mCurTransaction = null;

    private Fragment mCurrentFragment;

    ArrayList<Fragment> list = new ArrayList<>();
    private final String[] mTitles = {"头条", "科技", "社会", "国内", "娱乐"};
    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        ButterKnife.inject(this);


        if(savedInstanceState == null) {
            fragmetManager =getSupportFragmentManager();
            mCurTransaction=   fragmetManager.beginTransaction();
            mCurrentFragment = VideoFragmet.newInstance();
            mCurTransaction.add(R.id.fragment_Container, VideoFragmet.newInstance(), "video");
            mCurTransaction.add(R.id.fragment_Container, PhotoFragment.newInstance(),"photo");
            mCurTransaction.add(R.id.fragment_Container, UserFrament.newInstance(),"user");
            mCurTransaction.commitAllowingStateLoss();
        }

        initView();
        initData();
    }

    private void initView() {


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

        fragmetManager=  getSupportFragmentManager();


    }

    private void initData()
    {


        for (int i = 0; i < mTitles.length; i++)
        {
            NewsFragment newsf = NewsFragment.newInstance(i,appBarLayout);
            list.add(newsf);
        }

        adapter = new PaperAdapter(getSupportFragmentManager(), list, mTitles);
        pager.setAdapter(adapter);


        tab_bottom.setOnNavigationItemSelectedListener(
                new BottomNavigationView.OnNavigationItemSelectedListener() {
                    @Override
                    public boolean onNavigationItemSelected(@NonNull MenuItem item) {

                        switch (item.getItemId()){
                            case R.id.item_home:
                                ToastUtil.show("首页");
                                showHome();
                                break;
                            case R.id.item_videos:

                                goneHome();
                                ToastUtil.show("视屏");
                                Fragment fFragment = fragmetManager.findFragmentByTag("video");
                                changeBottomFragment(fFragment);
                                break;
                            case R.id.item_welfare:
                                goneHome();
                                Fragment pFragment = fragmetManager.findFragmentByTag("photo");
                                changeBottomFragment(pFragment);
                                ToastUtil.show("图片");
                                break;
                            case R.id.item_user:
                                goneHome();
                                Fragment uFragment = fragmetManager.findFragmentByTag("user");
                                changeBottomFragment(uFragment);
                                ToastUtil.show("我");
                                break;
                        }
                        return true;
                    }
                });
    }

    private void changeBottomFragment(Fragment fFragment) {
        if (mCurrentFragment != null) {
            mCurrentFragment.setMenuVisibility(false);
            mCurrentFragment.setUserVisibleHint(false);
        }
        if (fFragment != null) {

            fFragment.setMenuVisibility(false);
            fFragment.setUserVisibleHint(false);
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
}
