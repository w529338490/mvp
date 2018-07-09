package robot.nsk.com.robot_app.fragment;


import android.support.v4.app.Fragment;
import android.view.View;
import android.widget.ImageView;

import butterknife.BindView;
import butterknife.OnClick;
import robot.nsk.com.robot_app.R;

/**
 * A simple {@link Fragment} subclass.
 */
public class PreperFragment extends BaseFragment {
    @BindView(R.id.prep_back)
    ImageView prepBack;
    @BindView(R.id.netx_step)
    ImageView netxStep;
    public PreperFragment() {
        // Required empty public constructor
    }

    @Override
    int bindRootView() {
        return R.layout.fragment_preper;
    }

    @Override
    void initView() {

    }

    @Override
    void initData() {

    }


    @OnClick({R.id.prep_back, R.id.netx_step})
    public void onViewClicked(View view) {
        switch (view.getId()) {
            case R.id.prep_back:
                myFragmentManeger.popFragment(true);
          //      finish();
                break;
            case R.id.netx_step:
                myFragmentManeger.startFragment(AdjustFragment.class,"AdjustFragment");
        //        IntentUtil.getIntent().goActivity(this,AdjustActivity.class);
                break;
        }
    }

    @Override
    void destroyData() {
        myFragmentManeger.removeFragment("PreperFragment");
    }

}
