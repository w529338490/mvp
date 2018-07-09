package robot.nsk.com.robot_app.fragment;


import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;
import com.orhanobut.logger.Logger;
import com.wang.avi.AVLoadingIndicatorView;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.TimeUnit;

import butterknife.BindView;
import butterknife.OnClick;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.functions.Consumer;
import io.reactivex.functions.Predicate;
import io.reactivex.schedulers.Schedulers;
import robot.nsk.com.robot_app.R;
import robot.nsk.com.robot_app.activitys.FinishedActivity;
import robot.nsk.com.robot_app.adapters.AjustRecoverAdapter;
import robot.nsk.com.robot_app.bean.user.TestResultList;
import robot.nsk.com.robot_app.commom.Config;
import robot.nsk.com.robot_app.net.API;
import robot.nsk.com.robot_app.net.service.UserService;
import robot.nsk.com.robot_app.units.IntentUtil;
import robot.nsk.com.robot_app.units.ValueUtil;

/**
 * A simple {@link Fragment} subclass.
 */
public class AjustRecordFragment extends BaseFragment {
    @BindView(R.id.ajust_rlist)
    RecyclerView ajustRlist;
    List<TestResultList.DataBean.ReportListBean> list = new ArrayList<>();
    @BindView(R.id.home)
    ImageView home;
    @BindView(R.id.hist_tory)
    TextView histTory;
    @BindView(R.id.hist_tory_back)
    ImageView histToryBack;

    @BindView(R.id.avl)
    AVLoadingIndicatorView avi;
    private AjustRecoverAdapter ajustRecoverAdapter;
    ImageView now_adjust;

    public AjustRecordFragment() {
        // Required empty public constructor
    }
    @Override
    int bindRootView() {
        return R.layout.fragment_ajust_record;
    }

    @Override
    void initView() {
        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(context);
        linearLayoutManager.setOrientation(LinearLayoutManager.HORIZONTAL);
        ajustRlist.setLayoutManager(linearLayoutManager);
        ajustRecoverAdapter = new AjustRecoverAdapter(R.layout.ajust_recover_item, list);
        ajustRecoverAdapter.setEmptyView(View.inflate(context, R.layout.ajust_nodata, null));
        ajustRecoverAdapter.getEmptyView().setVisibility(View.GONE);
        ajustRlist.setAdapter(ajustRecoverAdapter);
        initData();
    }

    @Override
    void initData() {
        if(avi != null){
            avi.show();
            avi.setVisibility(View.VISIBLE);
        }

        API.newInstance().getservice(UserService.class).getTestResultList(""+ Config.CACHE.get("user_id"))
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .compose(this.<TestResultList>bindToLifecycle())
                .subscribe(new Consumer<TestResultList>() {
                    @Override
                    public void accept(TestResultList testResultList) throws Exception {
                        avi.setVisibility(View.GONE);
                        if(testResultList != null && testResultList.status == 200 &&
                                testResultList.data != null && testResultList.data.report_list != null &&
                                testResultList.data.report_list.size() > 0)
                        {
                            ajustRecoverAdapter.getData().clear();
                            avi.setVisibility(View.GONE);
                            list = testResultList.data.report_list ;
                            ajustRecoverAdapter.addData(list);

                        }else {
                            ajustRecoverAdapter.getEmptyView().setVisibility(View.VISIBLE);
                        }

                    }
                }, new Consumer<Throwable>() {
                    @Override
                    public void accept(Throwable throwable) throws Exception {
                        Logger.e(throwable.toString());
                        avi.setVisibility(View.GONE);
                        ajustRecoverAdapter.getEmptyView().setVisibility(View.VISIBLE);
                    }
                });
        ajustRecoverAdapter.setItemViewClickedListener(new AjustRecoverAdapter.ItemViewClickedListener() {
            @Override
            public void click(TestResultList.DataBean.ReportListBean bean, View itemView) {

                if(bean != null && bean.regulate_status == 1){

                    Config.CACHE.put("dayComplete" ,false);
                    Config.CACHE.put("count_plan" , ValueUtil.checkAndReturnInt(-1,bean.countPlan));
                    Config.CACHE.put("count_cotent",ValueUtil.checkAndReturnString("睡眠",bean.symptomName));
                    IntentUtil.getIntent() .goActivity(context,FinishedActivity.class);

                }
                if(bean != null  && bean.regulate_status == 2 || bean.regulate_status == 3){
                    Config.CACHE.put("record_id",Integer.valueOf(bean.record_id));
                    Config.CACHE.put("history",0);
                    myFragmentManeger.startFragment(TestResultFragment.class,"TestResultFragment");

                }

            }
        });
        now_adjust =ajustRecoverAdapter.getEmptyView().findViewById(R.id.now_adjust);
        if(now_adjust != null){
            now_adjust.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                 //   finish();
                    myFragmentManeger.startFragment(MusureFragment.class,"MusureFragment");
                }
            });
        }
    }

    @Override
    void destroyData() {
        if(list != null){
            list.clear();
        }
        ajustRecoverAdapter.getEmptyView().setVisibility(View.GONE);

        if(avi != null){
            avi.setVisibility(View.VISIBLE);
        }
        if( ajustRecoverAdapter!=null){
            ajustRecoverAdapter.getData().clear();
            ajustRecoverAdapter.notifyDataSetChanged();
        }

    }

    @OnClick({R.id.home, R.id.hist_tory, R.id.hist_tory_back})
    public void onViewClicked(View view) {
        switch (view.getId()) {
            case R.id.home:
                myFragmentManeger.startFragment(MainFragment.class,"MainFragment");

                //finish();
                break;
            case R.id.hist_tory:

                break;
            case R.id.hist_tory_back:
            //    finish();
                myFragmentManeger.popFragment(false);
                break;
        }
    }
}
