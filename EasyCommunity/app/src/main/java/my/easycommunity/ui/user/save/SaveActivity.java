package my.easycommunity.ui.user.save;

import android.support.design.widget.TabLayout;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;

import java.util.ArrayList;
import java.util.List;

import butterknife.ButterKnife;
import butterknife.InjectView;
import my.easycommunity.R;
import my.easycommunity.adapter.PaperAdapter;

public class SaveActivity extends AppCompatActivity
{
    @InjectView(R.id.tab)
    TabLayout tabLayout;
    @InjectView(R.id.paper)
    ViewPager paper ;
    ArrayList<Fragment> list = new ArrayList<>();
    SaveAdapter adapter;
    private final String[] mTitles = {"我的新闻","我的美图","我的视频"};
    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_save);
        ButterKnife.inject(this);
        initView();
    }

    private void initView() {
        list.add(saveNewsFragment.newInstance());
        list.add(savePhotoFragment.newInstance());
        list.add(saveVideoFragment.newInstance());
        adapter =new SaveAdapter(getSupportFragmentManager(),mTitles);
        adapter.setList(list);
        paper.setAdapter(adapter);
        tabLayout.setupWithViewPager(paper);
    }


    private class  SaveAdapter extends FragmentPagerAdapter{
        List<Fragment> list;
        String[] mTitles;
        public SaveAdapter(FragmentManager fm, String[] mTitles)
        {
            super(fm);
            this.mTitles = mTitles;
        }

        public void setList(List<Fragment> list){
            this.list =list;
            notifyDataSetChanged();
        }

        public SaveAdapter(FragmentManager fm)
        {
            super(fm);
        }

        @Override
        public Fragment getItem(int position)
        {
            return list ==null ? null:list.get(position);
        }

        @Override
        public int getCount()
        {
            return list==null ? 0 : list.size();
        }
        @Override
        public CharSequence getPageTitle(int position)
        {
            return mTitles[position];
        }
    }
}
