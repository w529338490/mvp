package my.easycommunity;

import android.content.IntentFilter;
import android.net.ConnectivityManager;
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
import my.easycommunity.broadcast.NetWorkStateReceiver;
import my.easycommunity.ui.news.NewsFragment;
import my.easycommunity.ui.photo.PhotoFragment;
import my.easycommunity.ui.user.UserFrament;
import my.easycommunity.ui.video.VideoFragmet;

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

    //网络变化 broadcast
    private NetWorkStateReceiver netWorkStateReceiver;;

    ArrayList<Fragment> list = new ArrayList<>();
    private final String[] mTitles = {"头条", "科技", "社会", "国内", "娱乐"};
    boolean fromSavedInstanceState  = true;

    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        ButterKnife.inject(this);
        mCurrentFragment = VideoFragmet.newInstance();
        adapter = new PaperAdapter(getSupportFragmentManager(), mTitles);

        if(savedInstanceState == null){
            FragmentTransaction mCurTransaction = null;
            mCurTransaction = fragmetManager.beginTransaction();
            mCurTransaction.add(R.id.fragment_Container, VideoFragmet.newInstance(), "video")
                    .hide(VideoFragmet.newInstance());
            mCurTransaction.add(R.id.fragment_Container, PhotoFragment.newInstance(tab_bottom),"photo")
                    .hide(PhotoFragment.newInstance(tab_bottom));
            mCurTransaction.add(R.id.fragment_Container, UserFrament.newInstance(),"user")
                    .hide(UserFrament.newInstance());
            mCurTransaction.commitAllowingStateLoss();

        }else {
            getSupportFragmentManager().beginTransaction()
                    .hide(getSupportFragmentManager().findFragmentByTag("video"))
                    .commitAllowingStateLoss();
            ((PhotoFragment)getSupportFragmentManager().findFragmentByTag("photo")).tab_bottom=tab_bottom;
            getSupportFragmentManager().beginTransaction()
                    .hide(getSupportFragmentManager().findFragmentByTag("photo"))
                    .commitAllowingStateLoss();

            getSupportFragmentManager().beginTransaction()
                    .hide(getSupportFragmentManager().findFragmentByTag("user"))
                    .commitAllowingStateLoss();
            tab_bottom.setSelectedItemId(0);
        }
            for (int i = 0; i < mTitles.length; i++)
            {
                NewsFragment newsf = NewsFragment.newInstance(i);
                list.add(newsf);
            }
              adapter.setList(list);
        initView();
        initData();
    }

    private void initView() {
        showHome();
        pager.setAdapter(adapter);
        nv.setNavigationItemSelectedListener(this);
        tab.setupWithViewPager(pager);
        //设置Toolbar和DrawerLayout实现动画和联动
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(
                this, mDrawerLayout, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        mDrawerLayout.addDrawerListener(toggle);
        toggle.syncState();
    }

    private void initData()
    {

        toolbar.setNavigationOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View v)
            {
                mDrawerLayout.openDrawer(GravityCompat.START);
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
    protected void onResume() {
        super.onResume();
        if (netWorkStateReceiver == null)
        {
            netWorkStateReceiver = new NetWorkStateReceiver();
        }
        IntentFilter filter = new IntentFilter();
        filter.addAction(ConnectivityManager.CONNECTIVITY_ACTION);
        registerReceiver(netWorkStateReceiver, filter);
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
    //用户离开界面 (例如 按home键)
    @Override
    protected void onUserLeaveHint() {
        fromSavedInstanceState =false;
        super.onUserLeaveHint();
    }
    @Override
    public void onSaveInstanceState(Bundle savedInstanceState) {
        //不保存，新闻页面的Fragment
      //  不保存，界面以及fragment，fragmetManager信息，以便在 内存不足等极端情况下重启时候，重新构建项目
        FragmentTransaction transaction=  getSupportFragmentManager().beginTransaction();
        if(fromSavedInstanceState){
            for(int i =3 ; i<getSupportFragmentManager().getFragments().size();i++){

                transaction.remove(getSupportFragmentManager()
                                .getFragments().get(i));
            }
            transaction.commitAllowingStateLoss();
        }
        tab_bottom.setSelectedItemId(0);
        super.onSaveInstanceState(savedInstanceState);
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
            System.exit(0);
        }
    }
    @Override
    protected void onDestroy() {
        super.onDestroy();
        if(netWorkStateReceiver!= null && netWorkStateReceiver.isOrderedBroadcast()){
            unregisterReceiver(netWorkStateReceiver);
        }
    }
}
