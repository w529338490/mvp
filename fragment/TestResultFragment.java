package robot.nsk.com.robot_app.fragment;


import android.support.constraint.ConstraintLayout;
import android.support.v4.app.Fragment;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.orhanobut.logger.Logger;
import com.wang.avi.AVLoadingIndicatorView;

import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.TimeUnit;

import butterknife.BindView;
import butterknife.OnClick;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.functions.Consumer;
import io.reactivex.functions.Predicate;
import io.reactivex.schedulers.Schedulers;
import robot.nsk.com.robot_app.R;
import robot.nsk.com.robot_app.bean.user.AdjustResultLast;
import robot.nsk.com.robot_app.bean.user.GetTestResult;
import robot.nsk.com.robot_app.commom.Config;
import robot.nsk.com.robot_app.net.API;
import robot.nsk.com.robot_app.net.service.UserService;
import robot.nsk.com.robot_app.units.BaiDuYuYingUtils;
import robot.nsk.com.robot_app.units.ToastUtil;

/**
 * A simple {@link Fragment} subclass.
 */
public class TestResultFragment extends BaseFragment {

    @BindView(R.id.imageView3)
    ImageView imageView3;
    @BindView(R.id.textView5)
    TextView textView5;
    @BindView(R.id.result_time)
    TextView resultTime;
    @BindView(R.id.time_pro)
    ProgressBar timePro;

    @BindView(R.id.imageView4)
    ImageView imageView4;
    @BindView(R.id.textView11)
    TextView textView11;
    @BindView(R.id.play_)
    TextView play;
    @BindView(R.id.relativeLayout)
    RelativeLayout relativeLayout;
    @BindView(R.id.adjust_status)
    ImageView adjustStatus;
    @BindView(R.id.resout_tv)
    TextView resoutTextView;

    @BindView(R.id.avl)
    AVLoadingIndicatorView avi;
    //调节页面父控件
    @BindView(R.id.adjust_p)
    RelativeLayout adjust_p ;

    // 今日调节已经完成
    @BindView(R.id.todaydone_p)
    RelativeLayout todaydone_p ;

    @BindView(R.id.commpelt)
    ImageView commpelt;

    //无需调节
    @BindView(R.id.no_need_P)
    RelativeLayout no_need_P ;
    @BindView(R.id.no_need_back)
    ImageView no_need_back;
    @BindView(R.id.error_cl)
    ConstraintLayout error_cl;

    boolean hideAdjuset = false  ,hide_no_need_ad = true;
    private  int needTip = -1;
    private  int todayDone = -1;

    private  String resultSpreak1 = "已为您生成属于您的专属调节方案，请放松身心享受调节吧 ";
    private  String   resultSpreak2 = "失眠调节是个持续的过程，加油哦";
    public TestResultFragment() {
        // Required empty public constructor
    }

    @Override
    int bindRootView() {
        return R.layout.fragment_test_result;
    }

    @Override
    void initView() {
        if(avi != null){
            avi.show();
            avi.setVisibility(View.VISIBLE);
        }
        initData();
    }

    @Override
    void initData() {

        if( Config.CACHE.get("WebToResult")!=null && (Integer) Config.CACHE.get("WebToResult") == 1){
            BaiDuYuYingUtils.get().speak(resultSpreak1);
            Config.CACHE.put("WebToResult",0);
        }else {
            BaiDuYuYingUtils.get().speak(resultSpreak2);
        }

        if(Config.CACHE.get("history") !=null && Config.CACHE.get("user_id") != null ){
            if( (int) Config.CACHE.get("history") == 0){
                //获取调节历史
                netFromHistory();
            }else {
                //获取最近调节
                netFromLast();
            }
        }else {
            avi.setVisibility(View.INVISIBLE);
        }
    }

    private void netFromLast() {
        API.newInstance().getservice(UserService.class).getNewRegulateNo( ""+ Config.CACHE.get("user_id"))
                .delay(1, TimeUnit.SECONDS)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .compose(this.<AdjustResultLast>bindToLifecycle())
                .subscribe(new Consumer<AdjustResultLast>() {
                    @Override
                    public void accept(AdjustResultLast adjustResultLast) throws Exception {
                        avi.setVisibility(View.INVISIBLE);
                        if(adjustResultLast != null && adjustResultLast.status == 200 && adjustResultLast.data != null){
                            error_cl.setVisibility(View.GONE);
                            needTip = adjustResultLast.data.needTip;
                            todayDone = adjustResultLast.data.todayDone;

                            if(needTip == 0){
                                hide_no_need_ad = false;
                            }else {
                                hide_no_need_ad = true;
                            }
                            if(needTip != -1  && needTip != 0){
                                hide_no_need_ad = true;
                                if(todayDone != -1 ){
                                    if(todayDone == 1 ){
                                        hideAdjuset = true;

                                    }else {
                                        hideAdjuset = false;
                                    }
                                }
                            }
                            initView2(adjustResultLast.data);
                        }else {
                            error_cl.setVisibility(View.VISIBLE);
                            resoutTextView.setVisibility(View.GONE);
                        }

                    }
                }, new Consumer<Throwable>() {
                    @Override
                    public void accept(Throwable throwable) throws Exception {
                        Logger.t(throwable.toString());
                        avi.setVisibility(View.INVISIBLE);
                        resoutTextView.setVisibility(View.INVISIBLE);
                        error_cl.setVisibility(View.VISIBLE);
                    }
                });
    }

    private void initView2(AdjustResultLast.DataBean dataBean) {
        resoutTextView.setVisibility(View.VISIBLE);
        // setGetTestResult(dataBean);
        if(dataBean != null ){
            //保存音频 请求参数
            Config.CACHE.put("record_id",Integer.valueOf(dataBean.recordId));
            Config.CACHE.put("plan_id",Integer.valueOf(dataBean.plan_id));
            showView();
            //没有
            resoutTextView.setText(dataBean.symptomName +"");
            resultTime.setText("第" + dataBean.current_day + "天/共" + dataBean.countPlan + "天" );
            textView11.setText(dataBean.symptomRegulate +"");
            if( dataBean.treat_type == 3 ){
                play.setText(""+ dataBean.havePlay);
            }else {
                play.setVisibility(View.INVISIBLE);
            }
            timePro.setMax(Integer.parseInt(dataBean.countPlan));
            timePro.setProgress(Integer.parseInt(dataBean.current_day));
        }
    }

    private void showView() {
        if(!hide_no_need_ad){
            todaydone_p.setVisibility(View.GONE);
            no_need_P.setVisibility(View.VISIBLE);
            adjust_p.setVisibility(View.GONE);
            resoutTextView.setVisibility(View.GONE);
        }else {
            if(hideAdjuset){
                resoutTextView.setVisibility(View.GONE);
                todaydone_p.setVisibility(View.VISIBLE);
                no_need_P.setVisibility(View.GONE);
                adjust_p.setVisibility(View.GONE);
            }else {
                todaydone_p.setVisibility(View.GONE);
                no_need_P.setVisibility(View.GONE);
                adjust_p.setVisibility(View.VISIBLE);
            }
        }
    }
    private void initView(GetTestResult.DataBean dataBean) {
        resoutTextView.setVisibility(View.VISIBLE);
        if(dataBean != null ){
            //保存音频 请求参数
            Config.CACHE.put("record_id",Integer.valueOf(dataBean.recordId));
            Config.CACHE.put("plan_id",Integer.valueOf(dataBean.plan_id));
            Config.CACHE.put("status",Integer.valueOf(dataBean.status));

            showView();
            //没有
            if(dataBean.needTip == 0){
                resoutTextView.setText(dataBean.symptomName +"");
                //    parent_rl.setVisibility(View.INVISIBLE);

            }else {
                //   parent_rl.setVisibility(View.VISIBLE);
                resoutTextView.setText(dataBean.symptomName +"");
                resultTime.setText("第" + dataBean.current_day + "天/共" + dataBean.countPlan + "天" );
                textView11.setText(dataBean.symptomRegulate +"");
                if( dataBean.treat_type == 3 ){
                    play.setText(""+ dataBean.havePlay);
                }else {
                    play.setVisibility(View.INVISIBLE);
                }
                timePro.setMax(Integer.parseInt(dataBean.countPlan));
                timePro.setProgress(Integer.parseInt(dataBean.current_day));
            }
        }
    }
    private void netFromHistory() {
        Map<String,Integer> params = new HashMap();
        params.put("user_id", (Integer) Config.CACHE.get("user_id"));
        params.put("record_id", (Integer) Config.CACHE.get("record_id"));
        params.put("isAnswer",1);
        Logger.e(params+ "======================================");
        API.newInstance().getservice(UserService.class).getTestResult(params)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .compose(this.<GetTestResult>bindToLifecycle())
                .subscribe(new Consumer<GetTestResult>() {
                    @Override
                    public void accept(GetTestResult getTestResult) throws Exception {

                        if(avi !=  null ){
                            avi.setVisibility(View.INVISIBLE);
                        }
                        if(getTestResult != null && getTestResult.status == 200  && getTestResult.data != null){
                            error_cl.setVisibility(View.GONE);
                            needTip = getTestResult.data.needTip;
                            todayDone = getTestResult.data.todayDone;

                            if(needTip == 0){
                                hide_no_need_ad = false;
                            }else {
                                hide_no_need_ad = true;
                            }
                            if(needTip != -1  && needTip != 0){
                                hide_no_need_ad = true;
                                if(todayDone != -1 ){
                                    if(todayDone == 1 ){
                                        hideAdjuset = true;
                                    }else {
                                        hideAdjuset = false;
                                    }
                                }
                            }
                            initView(getTestResult.data);
                        }else {
                            error_cl.setVisibility(View.VISIBLE);
                            resoutTextView.setVisibility(View.GONE);
                        }

                    }
                }, new Consumer<Throwable>() {
                    @Override
                    public void accept(Throwable throwable) throws Exception {
                        Logger.e(throwable.toString());
                        if(avi !=  null ){
                            avi.setVisibility(View.INVISIBLE);
                        }
                        error_cl.setVisibility(View.VISIBLE);
                        resoutTextView.setVisibility(View.GONE);
                    }
                });
    }

    @OnClick({R.id.imageView3, R.id.textView5, R.id.adjust_status , R.id.commpelt , R.id.no_need_back})
    public void onViewClicked(View view) {
        switch (view.getId()) {
            case R.id.imageView3:
                myFragmentManeger.startFragment(MainFragment.class,"MainFragment");
             //   IntentUtil.getIntent().goActivity(this,MainActivity.class);
             //   finish();
                break;
            case R.id.textView5:
                myFragmentManeger.startFragment(AjustRecordFragment.class,"AjustRecordFragment");
            //    IntentUtil.getIntent().goActivity(this,AjustRecordActivity.class);
            //    finish();
                break;
            case R.id.adjust_status:
                if(needTip != -1 && needTip != 0 && todayDone == 0 ){
                    myFragmentManeger.startFragment(PreperFragment.class,"PreperFragment");
                //    IntentUtil.getIntent().goActivity(this,PreperActivity.class);
               //     finish();
                }
                else if(needTip == 0){
                    //没有zz
                }
                else if(needTip != -1 &&  needTip != 0 && todayDone == 1){
                    //今天已接调节完成
                }
                break;
            case R.id.commpelt:
                myFragmentManeger.startFragment(AjustRecordFragment.class,"AjustRecordFragment");
                break;

            case R.id.no_need_back:
                myFragmentManeger.popFragment(false);
                break;
        }
    }
    @Override
    void destroyData() {

        needTip = -1;
        todayDone = -1;
        error_cl.setVisibility(View.GONE);
        todaydone_p.setVisibility(View.GONE);
        no_need_P.setVisibility(View.GONE);
        adjust_p.setVisibility(View.GONE);
        resoutTextView.setVisibility(View.GONE);
        if(avi!=null){
            avi.setVisibility(View.VISIBLE);
        }
    }
}
