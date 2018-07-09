package robot.nsk.com.robot_app.fragment;

import android.animation.Animator;
import android.animation.AnimatorListenerAdapter;
import android.animation.ObjectAnimator;
import android.app.Dialog;
import android.content.DialogInterface;
import android.os.Bundle;
import android.provider.Settings;
import android.support.annotation.Nullable;
import android.support.constraint.ConstraintLayout;
import android.support.v4.app.Fragment;
import android.support.v7.app.AlertDialog;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.ViewGroup.LayoutParams;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.jakewharton.rxbinding.view.RxView;
import com.orhanobut.logger.Logger;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.concurrent.TimeUnit;

import butterknife.BindView;
import butterknife.OnClick;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.disposables.Disposable;
import io.reactivex.functions.Consumer;
import io.reactivex.functions.Predicate;
import io.reactivex.schedulers.Schedulers;
import robot.nsk.com.robot_app.R;
import robot.nsk.com.robot_app.activitys.lockNewActivity;
import robot.nsk.com.robot_app.bean.user.PadBindUser;
import robot.nsk.com.robot_app.bean.user.UnbindUser;
import robot.nsk.com.robot_app.bean.user.UserHadRegulate;
import robot.nsk.com.robot_app.commom.Config;
import robot.nsk.com.robot_app.net.API;
import robot.nsk.com.robot_app.net.service.UserService;
import robot.nsk.com.robot_app.units.AppAplication;
import robot.nsk.com.robot_app.units.BaiDuYuYingUtils;
import robot.nsk.com.robot_app.units.CustomDialog;
import robot.nsk.com.robot_app.units.IntentUtil;
import robot.nsk.com.robot_app.units.ToastUtil;
import robot.nsk.com.robot_app.units.player.Play3;
import rx.functions.Action1;

/**
 * A simple {@link Fragment} subclass.
 */
public class MainFragment extends BaseFragment {
    @BindView(R.id.mesure_tv)
    ImageView mesureTv;
    @BindView(R.id.adjust_tv)
    ImageView adjustTv;
    @BindView(R.id.lock_iv)
    ImageView lock_iv;
    @BindView(R.id.battery)
    ImageView battery;
    @BindView(R.id.device)
    TextView device;
    @BindView(R.id.time_tv)
    TextView timeTv;
    @BindView(R.id.user_name)
    TextView userName;
    @BindView(R.id.id_number)
    TextView idNumber;
    @BindView(R.id.wifi_bg)
    ImageView wifiBg;
    @BindView(R.id.logout)
    ImageView logout;
    @BindView(R.id.parent)
    ConstraintLayout parent;
    boolean hasRemove = false;
    SimpleDateFormat simpleDateFormat;
    int[] power = {R.mipmap.bat_10, R.mipmap.bat_20, R.mipmap.bat_20, R.mipmap.bat_30, R.mipmap.bat_40,
            R.mipmap.bat_50, R.mipmap.bat_60, R.mipmap.bat_70, R.mipmap.bat_80, R.mipmap.bat_90, R.mipmap.bat_100};

    private BaiDuYuYingUtils baiDuYuYingUtils = BaiDuYuYingUtils.get();
    private String mainSpeak = "我是1号设备，很高兴为您服务";

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    int bindRootView() {
        return R.layout.fragment_main;
    }

    @Override
    public void initView() {

        simpleDateFormat = new SimpleDateFormat("yyyy年MM月dd日 HH:mm");// HH:mm:ss
        //view 防止 按键连续点击
        RxView.clicks(logout)
                .throttleFirst(2, TimeUnit.SECONDS)
                .subscribe(new Action1<Void>() {
                    @Override
                    public void call(Void aVoid) {
                    final CustomDialog dialog =new CustomDialog(context);
                        dialog.show();
                        dialog.setCustoListener(new CustomDialog.CustoListener() {
                            @Override
                            public void ok() {
                                dialog.dismiss();
                                String device_id = Settings.Secure.getString(context.getContentResolver(), Settings.Secure.ANDROID_ID);
                                //用户解绑
                                undinUser(device_id);
                            }
                        });
                    }
                });
        initData();
    }

    @Override
    void initData() {


        if( Config.CACHE.get("disposable") != null){
            Disposable d = (Disposable) Config.CACHE.get("disposable");
            d.dispose();
            d = null ;
        }
        if (Config.CACHE.get("device_numid") != null) {
            device.setText("设备编号：" + Config.CACHE.get("device_numid"));
        }
        Date date = new Date(System.currentTimeMillis());
        timeTv.setText("" + simpleDateFormat.format(date));
        //stop Video
        if (Config.CACHE.get("player") != null) {
            Play3 mActivity = (Play3) Config.CACHE.get("player");
            mActivity.relecs();
            mActivity = null;
        }
        iniPower();
        initWifi();

        if (hasRemove) {
            ObjectAnimator animator = ObjectAnimator.ofFloat(mesureTv, "translationX", -150, 0);
            animator.setDuration(2);
            animator.start();
        }

        adjustTv.setVisibility(View.INVISIBLE);
        if (Config.CACHE.get("loginMaster") != null) {
            adjustTv.setVisibility(View.VISIBLE);
        } else {
            if (Config.CACHE.get("user") != null) {
                BaiDuYuYingUtils.get().speak(mainSpeak);
                PadBindUser padBindUser = (PadBindUser) Config.CACHE.get("user");
                if(padBindUser.data.real_name.length() >= 5){
                   String subName =   padBindUser.data.real_name.substring(0,5);
                    userName.setText("当前用户：" + subName + "...");
                }else {
                    userName.setText("当前用户：" + padBindUser.data.real_name);
                }

                idNumber.setText(padBindUser.data.ID_number);
                //用户 是否调节
                hadRegulate(padBindUser);
            } else {
                IntentUtil.getIntent().goActivity(context, lockNewActivity.class);
            }
        }
    }

    private void initWifi() {
        if (Config.CACHE.get("wifiPower") == null) {
            return;
        }
        int integer = (int) Config.CACHE.get("wifiPower");

        if (integer <= 0 && integer >= -50) {
            wifiBg.setBackground(getResources().getDrawable(R.mipmap.wifi_bg));
        } else if (integer < -50 && integer >= -70) {
            wifiBg.setBackground(getResources().getDrawable(R.mipmap.wifi_2));
        } else if (integer < -70 && integer >= -80) {
            wifiBg.setBackground(getResources().getDrawable(R.mipmap.wifi_1));
        }
    }

    private void iniPower() {
        if (Config.CACHE.get("batteryPower") == null) {
            return;
        }
        int level = (int) Config.CACHE.get("batteryPower");
        int i = level / 10;
        battery.setBackground(getResources().getDrawable(power[i]));
    }

    private void hadRegulate(PadBindUser padBindUser) {
        API.newInstance().getservice(UserService.class).getUserHadRegulate(padBindUser.data.user_id)
                .filter(new Predicate<UserHadRegulate>() {
                    @Override
                    public boolean test(UserHadRegulate userHadRegulate) throws Exception {
                        if(userHadRegulate != null && userHadRegulate.data != null && userHadRegulate.data.is_had_regulate == 1){

                        }else {

                        }

                        return userHadRegulate != null && userHadRegulate.data != null && userHadRegulate.data.is_had_regulate == 1;
                    }
                })
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .compose(this.<UserHadRegulate>bindToLifecycle())
                .subscribe(new Consumer<UserHadRegulate>() {
                    @Override
                    public void accept(UserHadRegulate userHadRegulate) throws Exception {

                        adjustTv.setVisibility(View.VISIBLE);
                        ObjectAnimator animator = ObjectAnimator.ofFloat(mesureTv, "translationX", 0, -150);
                        animator.setDuration(500);
                        animator.addListener(new AnimatorListenerAdapter() {
                            @Override
                            public void onAnimationEnd(Animator animation) {
                                super.onAnimationEnd(animation);
                                hasRemove = true;
                            }
                        });
                        animator.start();

                    }
                }, new Consumer<Throwable>() {
                    @Override
                    public void accept(Throwable throwable) throws Exception {
                        Logger.e(throwable.toString());
                    }
                });
    }

    @OnClick({R.id.mesure_tv, R.id.adjust_tv, R.id.lock_iv})
    public void onViewClicked(View view) {
        switch (view.getId()) {
            case R.id.mesure_tv:
                myFragmentManeger.startFragment(MusureFragment.class, "MusureFragment");

                break;
            case R.id.adjust_tv:
                Config.CACHE.put("history", 1);
                myFragmentManeger.startFragment(TestResultFragment.class, "TestResultFragment");
                break;
            case R.id.lock_iv://锁屏
                //  screenOff();
                break;
        }
    }

    @Override
    void destroyData() {
        BaiDuYuYingUtils.get().stopSpeak();
    }

    public MainFragment() {
        // Required empty public constructor
    }

    private void undinUser(final String device_id) {
        API.newInstance().getservice(UserService.class).DeviceUnbindUser(device_id)
                .filter(new Predicate<UnbindUser>() {
                    @Override
                    public boolean test(UnbindUser unbindUser) throws Exception {
                        return unbindUser != null && unbindUser.status == 200;
                    }
                })
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Consumer<UnbindUser>() {
                    @Override
                    public void accept(UnbindUser unbindUser) throws Exception {
                        ToastUtil.toast("解绑成功！");
                        Config.CACHE.clear();
                        Config.CACHE.put("progress", -1);
                        Config.CACHE.put("plan_id", -1);
                        Config.CACHE.put("atLocking", 0);
                        Config.CACHE.put("current_time", -1);
                        Config.CACHE.put("device_id", device_id);
                        userName.setText("");
                        idNumber.setText("");
                        IntentUtil.getIntent().goActivity(context, lockNewActivity.class);
                    }
                }, new Consumer<Throwable>() {
                    @Override
                    public void accept(Throwable throwable) throws Exception {
                        ToastUtil.toast("解绑失败！");
                        Logger.e(throwable.toString());
                    }
                });

    }

}
