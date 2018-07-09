package robot.nsk.com.robot_app.fragment;


import android.animation.ObjectAnimator;
import android.app.Activity;
import android.content.Context;
import android.content.SharedPreferences;
import android.support.v4.app.Fragment;
import android.support.v7.widget.AppCompatSeekBar;
import android.view.View;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.orhanobut.logger.Logger;
import com.wang.avi.AVLoadingIndicatorView;

import java.text.DecimalFormat;
import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.TimeUnit;

import butterknife.BindView;
import butterknife.OnClick;
import io.reactivex.Observable;
import io.reactivex.Observer;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.disposables.Disposable;
import io.reactivex.functions.Consumer;
import io.reactivex.functions.Predicate;
import io.reactivex.schedulers.Schedulers;
import robot.nsk.com.robot_app.R;
import robot.nsk.com.robot_app.activitys.FinishedActivity;
import robot.nsk.com.robot_app.bean.user.ChangeRegulateStatus;
import robot.nsk.com.robot_app.bean.voice.SelfRegulations;
import robot.nsk.com.robot_app.commom.Config;
import robot.nsk.com.robot_app.net.API;
import robot.nsk.com.robot_app.net.service.UserService;
import robot.nsk.com.robot_app.units.IntentUtil;
import robot.nsk.com.robot_app.units.NetWorkUtils;
import robot.nsk.com.robot_app.units.RxJavaDownloder;
import robot.nsk.com.robot_app.units.ToastUtil;
import robot.nsk.com.robot_app.units.ValueUtil;
import robot.nsk.com.robot_app.units.player.Play3;

/**
 * A simple {@link Fragment} subclass.
 */
public class PlaySoundFragment extends BaseFragment {
    @BindView(R.id.play_home)
    ImageView playHome;
    @BindView(R.id.play_pro)
    AppCompatSeekBar playPro;
    @BindView(R.id.play_back)
    ImageView playBack;
    @BindView(R.id.video_time_total)
    TextView time_total;
    @BindView(R.id.contrller)
    ImageView contrller;

    @BindView(R.id.avl)
    AVLoadingIndicatorView avi;
    @BindView(R.id.loding_rl)
    RelativeLayout loding_rl;
    @BindView(R.id.palying_parent)
    ImageView palying_parent;
    @BindView(R.id.contrller2)
    ImageView contrller2;

    private RxJavaDownloder rxJavaDownloder;
    private Play3 player;
    private int max_time;
    private int current_time;
    ObjectAnimator animatorRotation;
    //网络获取的音频 数据
    private SelfRegulations data;
    private int recordPisiton;
    private int currentPosition;
    private boolean finishAdjust = false;
    private Disposable disposable, disposableDownloder;
    private Activity mActivity;
    private boolean dayComplete = false;
    private int beforePisition = 0;

    public PlaySoundFragment() {
        // Required empty public constructor
    }

    @Override
    int bindRootView() {
        return R.layout.fragment_play_sound;
    }

    @Override
    void initView() {
        contrller.setVisibility(View.GONE);
        if (avi != null) {
            loding_rl.setVisibility(View.VISIBLE);
            avi.show();
            avi.setVisibility(View.VISIBLE);
        }

        if (getActivity() != null) {
            player = new Play3(getActivity());
        }
        rxJavaDownloder = new RxJavaDownloder();
        animatorRotation = ObjectAnimator.ofFloat(palying_parent, "rotation", 0f, 359f);
        animatorRotation.setRepeatCount(-1);
        animatorRotation.setDuration(36000);

        Map<String, Integer> params = new HashMap();

        params.put("user_id", (Integer) Config.CACHE.get("user_id"));
        params.put("record_id", (Integer) Config.CACHE.get("record_id"));
        if (Config.CACHE.get("status") != null && (int) Config.CACHE.get("status") == 0) {
            params.put("select_num", 2);
        } else {
            params.put("select_num", 0);
        }
        params.put("sex", (Integer) Config.CACHE.get("sex"));
        params.put("plan_id", (Integer) Config.CACHE.get("plan_id"));
        Config.CACHE.put("player", player);
        Logger.e(params + ">>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>");
        //获取音频文件相关 数据
        API.newInstance().getservice(UserService.class).getSelfRegulations(params)
                .filter(new Predicate<SelfRegulations>() {
                    @Override
                    public boolean test(SelfRegulations selfRegulations) throws Exception {
                        boolean next = false;
                        next = selfRegulations != null && selfRegulations.status == 200 && selfRegulations.data != null;
                        if (!next) {
                            if (avi != null) {
                                loding_rl.setVisibility(View.GONE);
                                avi.setVisibility(View.GONE);
                            }
                        }
                        return next;
                    }
                })
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .compose(this.<SelfRegulations>bindToLifecycle())
                .subscribe(new Consumer<SelfRegulations>() {
                    @Override
                    public void accept(SelfRegulations selfRegulations) throws Exception {
                        if (selfRegulations.data.userTreatmentPlan != null && selfRegulations.data.audio_list != null) {

                            if (selfRegulations.data.count_plan.equals(selfRegulations.data.userTreatmentPlan.days)) {
                                dayComplete = false;
                            } else {
                                dayComplete = true;
                            }

                            Config.CACHE.put("dayComplete", dayComplete);
                            Config.CACHE.put("count_plan", ValueUtil.checkAndReturnInt(-1, selfRegulations.data.count_plan));
                            Config.CACHE.put("plan_id", Integer.valueOf(selfRegulations.data.userTreatmentPlan.id + ""));
                            Config.CACHE.put("count_cotent", ValueUtil.checkAndReturnString("睡眠", selfRegulations.data.userTreatmentPlan.symptomName));
                            finishAdjust = selfRegulations.data.userTreatmentPlan.days.equals(selfRegulations.data.count_plan);
                            int total_time = selfRegulations.data.total_time;
//                             selfRegulations.data.unset_time = 100;
//                             selfRegulations.data.current_time ="2070";
                            if (selfRegulations.data.unset_time != 0) {
                                total_time = total_time - selfRegulations.data.unset_time;
                                beforePisition = Integer.valueOf(selfRegulations.data.unset_time + "") * 1000;
                                // current_time = current_time + ;
                            } else {
                                beforePisition = 0;
                            }

                            max_time = selfRegulations.data.total_time * 1000;
                            data = selfRegulations;
                            long minute = (total_time) / 60;
                            long second = (total_time - minute * 60) % 60;
                            if (second < 10) {
                                time_total.setText(minute + ":" + "0" + second);
                            } else {
                                time_total.setText(minute + ":" + second);
                            }
                            Logger.e(max_time + "max_time==========" + beforePisition + "beforePisition=============" + selfRegulations.toString());

                            //下载
                            long startTime = System.currentTimeMillis();
                            rxJavaDownloder.DownLoaderAudioFile(selfRegulations.data.audio_list, (Integer) Config.CACHE.get("sex"), startTime);
                        }
                    }
                }, new Consumer<Throwable>() {
                    @Override
                    public void accept(Throwable throwable) throws Exception {
                        Logger.e(throwable.toString());
                        if (avi != null) {

                            loding_rl.setVisibility(View.GONE);
                            avi.setVisibility(View.GONE);

                        }
                    }
                });

        //下载完成 播放音频
        rxJavaDownloder.setRxJavaDownloderLisnter(new RxJavaDownloder.RxJavaDownloderLisnter() {
            @Override
            public void onComplete() {
                if (avi != null) {
                    loding_rl.setVisibility(View.GONE);
                    avi.setVisibility(View.GONE);
                }
                if (animatorRotation != null) {
                    animatorRotation.start();
                }
                //添加 数据源
                if (Config.CACHE.get("sex") != null && player != null) {
                    player.AddMediaSource(data.data.audio_list, (Integer) Config.CACHE.get("sex"));
                    player.paly();
                    playPro.setMax(max_time);
                    //更新 进度条
                    updataProssBar();
                }
            }

            @Override
            public void stopDown(Disposable disposable) {
                disposableDownloder = disposable;
            }

            @Override
            public void netWorkBad() {
                ToastUtil.toast("当前网络连接失败！");
                avi.setVisibility(View.GONE);

            }
        });
    }

    private void updataProssBar() {
        Observable.interval(1, 1, TimeUnit.SECONDS)
                .doOnNext(new Consumer<Long>() {
                    @Override
                    public void accept(Long aLong) throws Exception {
                        recordPisiton = current_time;
                    }
                })
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Observer<Long>() {
                    @Override
                    public void onSubscribe(Disposable d) {
                        disposable = d;
                        Config.CACHE.put("disposable", disposable);
                    }

                    @Override
                    public void onNext(Long aLong) {

                        current_time = player.getCurrentPosition() + beforePisition;
                        int dex = current_time - recordPisiton;
                        if (dex > 0) {
                            currentPosition += dex;
                        }
                        playPro.setProgress(currentPosition);
                        //保留两位 小数
                        double progress = Double.valueOf(currentPosition) / Double.valueOf(max_time);
                        DecimalFormat df = new DecimalFormat("0.00");
                        String pr = df.format(progress).toString();
                        if (Config.CACHE != null) {
                            Config.CACHE.put("progress", pr);
                            Config.CACHE.put("current_time", currentPosition);

                        }

                        Logger.e(currentPosition + "================");
                        //倒计时
                        int c_total = max_time - currentPosition;
                        long minute = (c_total) / 60000;
                        long second = (c_total / 1000 - minute * 60) % 60;
                        String minuteS = minute + "";
                        String secondS = second + "";
                        if (minute < 10) {
                            minuteS = "0" + minuteS;
                        }
                        if (second < 10) {
                            secondS = " 0 " + second;
                        }
                        if (second < 0) {
                            time_total.setText("00" + ":" + "00");
                        } else {
                            time_total.setText(minuteS + ":" + secondS);
                        }

                        finishAdjust = true;
                        //播放结束
                        if (player != null && player.hasCommplete() && finishAdjust) {
                            disposable.dispose();
                            Config.CACHE.put("player", null);
                            //保存数据
                            saveVoiceInfo(currentPosition, pr);
                        }

                    }

                    @Override
                    public void onError(Throwable e) {
                        Logger.e(e.toString());
                    }

                    @Override
                    public void onComplete() {

                    }
                });
    }

    private void saveVoiceInfo(int currentPosition, String pr) {

        Map save = new HashMap();
        save.put("user_id", Config.CACHE.get("user_id"));
        save.put("record_id", (Integer) Config.CACHE.get("record_id"));
        save.put("status", 2);
        save.put("plan_id", Config.CACHE.get("plan_id"));
        save.put("complete_time", "");
        save.put("current_time", currentPosition);
        save.put("progress", pr);
        Logger.e(save + "======================");

        if (context != null && NetWorkUtils.isWifiConnected(context)) {
            API.newInstance().getservice(UserService.class).changeRegulateStatus(save)
                    .subscribeOn(Schedulers.io())
                    .compose(this.<ChangeRegulateStatus>bindToLifecycle())
                    .observeOn(AndroidSchedulers.mainThread())
                    .subscribe(new Consumer<ChangeRegulateStatus>() {
                        @Override
                        public void accept(ChangeRegulateStatus changeRegulateStatus) throws Exception {
                            if (changeRegulateStatus != null && changeRegulateStatus.status == 200) {
                                IntentUtil.getIntent().goActivity(context, FinishedActivity.class);
                                //  finish();
                            }
                        }
                    }, new Consumer<Throwable>() {
                        @Override
                        public void accept(Throwable throwable) throws Exception {
                            Logger.e(throwable.toString());
                        }
                    });
        } else {
            //保存 数据库
            SharedPreferences preferences = context.getSharedPreferences("ViceInfo", Context.MODE_PRIVATE);
            SharedPreferences.Editor edit = preferences.edit();
            edit.putInt("user_id", Integer.valueOf(Config.CACHE.get("user_id") + ""));
            edit.putInt("record_id", Integer.valueOf(Config.CACHE.get("record_id") + ""));
            edit.putInt("status", 2);
            edit.putInt("plan_id", Integer.valueOf("" + Config.CACHE.get("planId")));
            edit.putInt("complete_time", currentPosition);
            edit.commit();

            IntentUtil.getIntent().goActivity(context, FinishedActivity.class);
            //    finish();
        }
    }

    @Override
    void initData() {
        contrller2.setVisibility(View.VISIBLE);
        initView();
    }

    @Override
    void destroyData() {
        Config.CACHE.put("progress", "-1");
        Config.CACHE.put("current_time", -1);
        Config.CACHE.put("player", null);

        if (animatorRotation != null) {
            animatorRotation.end();
            animatorRotation = null;
        }

        if (avi != null) {
            loding_rl.setVisibility(View.VISIBLE);
            avi.setVisibility(View.VISIBLE);
        }
        if (disposable != null) {
            disposable.dispose();
            disposable = null;
        }
        if (disposableDownloder != null) {
            disposableDownloder.dispose();
            disposableDownloder = null;
        }
        if (player != null) {
            player.relecs();
            player = null;
        }

    }

    @OnClick({R.id.play_home, R.id.play_back, R.id.play_pro, R.id.palying_parent})
    public void onViewClicked(View view) {
        switch (view.getId()) {
            case R.id.play_home:
                myFragmentManeger.startFragment2(MainFragment.class, "MainFragment", true);

                break;
            case R.id.play_back:
                myFragmentManeger.startFragment2(TestResultFragment.class, "TestResultFragment", true);

                break;
            case R.id.play_pro:
                break;
            case R.id.palying_parent:

                palying_parent.setSelected(!palying_parent.isSelected());
                if (palying_parent.isSelected() && animatorRotation != null) {
                    animatorRotation.pause();
                    if (player != null) {
                        player.pause();
                        contrller2.setVisibility(View.GONE);
                        contrller.setVisibility(View.VISIBLE);
                    }

                } else {

                    if (player != null) {
                        player.paly();
                        contrller2.setVisibility(View.VISIBLE);
                        contrller.setVisibility(View.GONE);

                    }
                    if (animatorRotation != null) {
                        animatorRotation.resume();
                    }
                }
                break;
        }
    }

    @Override
    public void onDestroy() {
        super.onDestroy();

    }
}
