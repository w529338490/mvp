package my.easycommunity;

import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.design.widget.AppBarLayout;
import android.support.design.widget.NavigationView;
import android.support.design.widget.Snackbar;
import android.support.design.widget.TabLayout;
import android.support.v4.app.Fragment;
import android.support.v4.view.GravityCompat;
import android.support.v4.view.ViewPager;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.view.MenuItem;
import android.view.View;

import com.lcodecore.tkrefreshlayout.TwinklingRefreshLayout;
import com.orhanobut.logger.Logger;
import com.trello.rxlifecycle.components.support.RxAppCompatActivity;

import java.util.ArrayList;

import butterknife.ButterKnife;
import butterknife.InjectView;
import my.easycommunity.adapter.PaperAdapter;
import my.easycommunity.ui.news.NewsFragment;
import rx.subscriptions.CompositeSubscription;

public class MainActivity extends RxAppCompatActivity implements NavigationView.OnNavigationItemSelectedListener{

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


    ArrayList<Fragment> list = new ArrayList<>();
    private final String[] mTitles = {"头条", "科技", "社会", "国内", "娱乐"};
    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        ButterKnife.inject(this);
        initData();
    }

    private void initData()
    {

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

        for (int i = 0; i < mTitles.length; i++)
        {
            NewsFragment newsf = NewsFragment.newInstance(i,appBarLayout);

            list.add(newsf);

        }
        adapter = new PaperAdapter(getSupportFragmentManager(), list, mTitles);
        pager.setAdapter(adapter);
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


}
