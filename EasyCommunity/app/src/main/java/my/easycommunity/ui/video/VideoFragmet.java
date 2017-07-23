package my.easycommunity.ui.video;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.design.widget.TabLayout;
import android.support.v4.app.Fragment;
import android.support.v4.view.ViewPager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import com.trello.rxlifecycle.components.support.RxFragment;
import java.util.ArrayList;
import butterknife.ButterKnife;
import butterknife.InjectView;
import my.easycommunity.R;
import my.easycommunity.adapter.PaperAdapter;
import my.easycommunity.ui.video.dzfragment.DzVideoFragment;

/**
 * Created by Administrator on 2017/7/17.
 */
public class VideoFragmet extends RxFragment  {
    private static VideoFragmet instance;
    private View view;
    @InjectView(R.id.tab) TabLayout tabLayout;
    @InjectView(R.id.paper) ViewPager pager;
    PaperAdapter adapter;
    ArrayList<Fragment> list = new ArrayList<>();
    private final String[] mTitles = {"内涵段子","搞笑视频"};
    public static  VideoFragmet newInstance(){
        if(instance == null){
            instance=new VideoFragmet();
        }
        return instance;
    }
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if(savedInstanceState == null){
            DzVideoFragment d=new DzVideoFragment().newInstance(0);
            DzVideoFragment z=new DzVideoFragment().newInstance(1);
            list.add(d);
            list.add(z);
        }else {
            list.add(new Fragment());
            list.add(new Fragment());
        }
    }
    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container,
            @Nullable Bundle savedInstanceState) {
        if(view==null){
            view =inflater.inflate(R.layout.fragment_video,container,false);
        }
        ButterKnife.inject(this,view);
        initView();
        return view;
    }
    private void initView() {
        adapter =new PaperAdapter(getChildFragmentManager(),mTitles);
        adapter.setList(list);
        pager.setAdapter(adapter);
        tabLayout.setupWithViewPager(pager);
    }
}
