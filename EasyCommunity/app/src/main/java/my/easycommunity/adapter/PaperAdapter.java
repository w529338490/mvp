package my.easycommunity.adapter;

import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.view.ViewGroup;
import java.util.List;

/**
 * Created by Administrator on 2017/7/16.
 */

public class PaperAdapter extends FragmentPagerAdapter
{
    List<Fragment> list;
    String[] mTitles;

    public PaperAdapter(FragmentManager fm, String[] mTitles)
    {
        super(fm);
        this.mTitles = mTitles;
    }

    public void setList(List<Fragment> list){
        this.list =list;
    }

    public PaperAdapter(FragmentManager fm)
    {
        super(fm);
    }

    @Override
    public void setPrimaryItem(ViewGroup container, int position, Object object) {
        Fragment fragment = (Fragment)object;
        super.setPrimaryItem(container, position, object);
    }

    @Override
    public Object instantiateItem(ViewGroup container, int position) {
        return super.instantiateItem(container, position);
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

    @Override
    public void destroyItem(ViewGroup container, int position, Object object)
    {
        //不销毁
    }
}
