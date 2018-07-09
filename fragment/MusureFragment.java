package robot.nsk.com.robot_app.fragment;


import android.support.v4.app.Fragment;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import java.util.logging.Logger;

import butterknife.BindView;
import butterknife.OnClick;
import robot.nsk.com.robot_app.R;
import robot.nsk.com.robot_app.activitys.CommonWebViewActivity;
import robot.nsk.com.robot_app.bean.CommenWeBean;
import robot.nsk.com.robot_app.commom.Config;
import robot.nsk.com.robot_app.units.BaiDuYuYingUtils;
import robot.nsk.com.robot_app.units.IntentUtil;
import robot.nsk.com.robot_app.units.ToastUtil;

/**
 * A simple {@link Fragment} subclass.
 */
public class MusureFragment extends BaseFragment {
    @BindView(R.id.hist_tory_back)
    ImageView hist_tory_back;
    @BindView(R.id.sleep_diff)
    ImageView sleepDiff;
    @BindView(R.id.sleep_easy)
    ImageView sleepEasy;
    @BindView(R.id.slee_early)
    ImageView sleeEarly;
    @BindView(R.id.more_dream)
    ImageView moreDream;
    @BindView(R.id.wake_up_middle)
    ImageView wakeUpMiddle;
    @BindView(R.id.day_and_night)
    ImageView dayAndNight;
    @BindView(R.id.home)
    ImageView home;
    @BindView(R.id.tittle)
    TextView tittle;
    private  String  musureSpaek = "请选择适合您的失眠症状进行测量 ";
    public MusureFragment() {
        // Required empty public constructor
    }
    @Override
    int bindRootView() {
        return R.layout.fragment_musure;
    }

    @Override
    void initView() {
        initData();
    }

    @Override
    public void onResume() {
        super.onResume();
        com.orhanobut.logger.Logger.e("onResume");
      //  BaiDuYuYingUtils.get().speak(musureSpaek);
    }

    @Override
    void initData() {
        BaiDuYuYingUtils.get().speak(musureSpaek);
    }
    @OnClick({ R.id.sleep_diff, R.id.sleep_easy, R.id.slee_early, R.id.more_dream, R.id.wake_up_middle, R.id.day_and_night, R.id.home, R.id.hist_tory_back})
    public void onViewClicked(View view) {
        String className ="";
        CommenWeBean commenWeBean=new CommenWeBean();
        String u_id =  Config.CACHE.get("user_id") +"";
        switch (view.getId()) {
            case R.id.sleep_diff:
                //入睡困难
                className = "DifficultySleepingInventory";
                commenWeBean.setUrl(className,u_id);

                IntentUtil.getIntent().goActivitySer(getActivity(),CommonWebViewActivity.class,commenWeBean);
                break;
            case R.id.sleep_easy:
                //睡眠浅
                className = "ShallowSleepingInventory";
                commenWeBean.setUrl(className,u_id);
                IntentUtil.getIntent().goActivitySer(getActivity(),CommonWebViewActivity.class,commenWeBean);
                break;
            case R.id.slee_early:
                //早醒
                className = "EarlyAwakeningInventory";
                commenWeBean.setUrl(className,u_id);
                IntentUtil.getIntent().goActivitySer(context,CommonWebViewActivity.class,commenWeBean);
                break;
            case R.id.more_dream:
                //多梦
                className = "DreaminessInventory";
                commenWeBean.setUrl(className,u_id);
                IntentUtil.getIntent().goActivitySer(context,CommonWebViewActivity.class,commenWeBean);
                break;
            case R.id.wake_up_middle:
                //中途醒
                className = "HalfwayAwakeningInventory";
                commenWeBean.setUrl(className,u_id);
                IntentUtil.getIntent().goActivitySer(context,CommonWebViewActivity.class,commenWeBean);
                break;
            case R.id.day_and_night:
                //日间症状
                className = "DaytimeSymptomsInventory";
                commenWeBean.setUrl(className,u_id);
                IntentUtil.getIntent().goActivitySer(context,CommonWebViewActivity.class,commenWeBean);
                break;
            case R.id.home://Home
                myFragmentManeger.startFragment(MainFragment.class,"MainFragment");

                break;
            case R.id.hist_tory_back://调节记录
                myFragmentManeger.popFragment(false);
                break;
        }
    }
    @Override
    void destroyData() {

    }

}
