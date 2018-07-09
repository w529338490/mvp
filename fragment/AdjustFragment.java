package robot.nsk.com.robot_app.fragment;

import android.animation.Animator;
import android.animation.AnimatorListenerAdapter;
import android.animation.AnimatorSet;
import android.animation.ObjectAnimator;
import android.media.MediaPlayer;
import android.support.v4.app.Fragment;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;
import butterknife.BindView;
import butterknife.OnClick;
import robot.nsk.com.robot_app.R;
import robot.nsk.com.robot_app.commom.Config;

/**
 * A simple {@link Fragment} subclass.
 */
public class AdjustFragment extends BaseFragment {
    @BindView(R.id.textView7)
    TextView textView7;
    @BindView(R.id.textView8)
    TextView textView8;
    @BindView(R.id.man_voice_choose)
    ImageView  manVoiceChoose;
    @BindView(R.id.woman_voice_choose)
    ImageView womanVoiceChoose;

    @BindView(R.id.woman_tv)
    TextView woman_tv;
    @BindView(R.id.man_tv)
    TextView man_tv;
    @BindView(R.id.go_adjust)
    ImageView goAdjust;
    @BindView(R.id.man_song)
    ImageView man_song;
    @BindView(R.id.woman_song)
    ImageView woman_song;
    @BindView(R.id.back)
    ImageView back;
    MediaPlayer mp,mp2;
    ObjectAnimator animatorMan  ,animatorWoman;
    AnimatorSet set;
    boolean checkout = false;

    public AdjustFragment() {
        super();
        // Required empty public constructor
    }

    @Override
    int bindRootView() {
        return R.layout.fragment_adjust;
    }
    @Override
    void initView() {
        manVoiceChoose.setSelected(true);
        Config.CACHE.put("sex",0);
        set  =new AnimatorSet();

        animatorMan = ObjectAnimator.ofFloat(man_song,"alpha",1,1);
        animatorWoman = ObjectAnimator.ofFloat(woman_song,"alpha",1,1);
        animatorMan.setDuration(700);
        animatorMan.setRepeatCount(-1);
        animatorWoman.setDuration(700);
        animatorWoman.setRepeatCount(-1);
        animatorMan.addListener(new AnimatorListenerAdapter() {
            @Override
            public void onAnimationRepeat(Animator animation) {
                super.onAnimationRepeat(animation);
                if(checkout){
                    man_song.setImageResource(R.drawable.horn_d);
                }else {
                    man_song.setImageResource(R.drawable.horn_s);
                }
                checkout = !checkout;
            }
        });

        animatorWoman.addListener(new AnimatorListenerAdapter() {
            @Override
            public void onAnimationRepeat(Animator animation) {
                super.onAnimationRepeat(animation);
                if(checkout){
                    woman_song.setImageResource(R.drawable.horn_d);
                }else {
                    woman_song.setImageResource(R.drawable.horn_s);
                }
                checkout = !checkout;
            }
        });
    }

    @Override
    void initData() {

    }
    @Override
    public void onPause() {
        super.onPause();
        if (mp != null) {
            mp.release();
            mp = null;
        }
    }

        @Override
    void destroyData() {
            myFragmentManeger.removeFragment("AdjustFragment");
            man_song.setImageResource(R.drawable.horn_d);
            woman_song.setImageResource(R.drawable.horn_d);
            if(animatorWoman != null){
                animatorWoman.end();
            }
            if(animatorMan != null){
                animatorMan.end();
            }
            if(mp !=null ){
                mp.release();
                mp = null ;
            }
    }

    @OnClick({R.id.go_adjust, R.id.man_voice_choose, R.id.woman_voice_choose, R.id.man_song , R.id.woman_song , R.id.back ,R.id.man_tv,R.id.woman_tv})
    public void onViewClicked(View view) {
        switch (view.getId()){
            case R.id.go_adjust:
                myFragmentManeger.startFragment(PlaySoundFragment.class,"PlaySoundFragment");

                break;
            case R.id.man_voice_choose:
                Config.CACHE.put("sex",0);
                manVoiceChoose.setSelected(true);
                womanVoiceChoose.setSelected(false);
                break;
            case R.id.woman_voice_choose:
                Config.CACHE.put("sex",1);
                manVoiceChoose.setSelected(false);
                womanVoiceChoose.setSelected(true);
                break;

            case R.id.man_song:
                manVioce();
                break;

            case R.id.woman_song:
                womanVice();
                break;

            case R.id.man_tv :
                manVioce();
                break;
            case R.id.woman_tv :
                womanVice();
                break;
            case R.id.back:
                myFragmentManeger.popFragment(true);

                break;


        }
    }

    private void womanVice() {
        if(animatorMan != null ){
            animatorMan.pause();
        }

        if(animatorWoman != null){
            animatorWoman.start();
        }

        if( mp != null ){
            mp.release();
            mp = null ;
            mp = MediaPlayer.create(context, R.raw.shitingnv);
            mp.start();
            mp.setOnCompletionListener(new MediaPlayer.OnCompletionListener() {
                @Override
                public void onCompletion(MediaPlayer mp) {
                    animatorWoman.pause();
                    woman_song.setImageResource(R.drawable.horn_d);

                }
            });
        }else {
            mp = MediaPlayer.create(context, R.raw.shitingnv);
            mp.start();
            mp.setOnCompletionListener(new MediaPlayer.OnCompletionListener() {
                @Override
                public void onCompletion(MediaPlayer mp) {
                    animatorWoman.pause();
                    woman_song.setImageResource(R.drawable.horn_d);

                }
            });
        }

        Config.CACHE.put("sex",1);
        manVoiceChoose.setSelected(false);
        womanVoiceChoose.setSelected(true);
    }

    private void manVioce() {
        if(animatorMan != null ){
            animatorMan.start();
        }

        if(animatorWoman != null){
            animatorWoman.pause();
        }

        if( mp != null ){
            mp.release();
            mp = null ;
            mp = MediaPlayer.create(context, R.raw.shitingnan);
            mp.start();
            mp.setOnCompletionListener(new MediaPlayer.OnCompletionListener() {
                @Override
                public void onCompletion(MediaPlayer mp) {
                    animatorMan.pause();
                    man_song.setImageResource(R.drawable.horn_d);

                }
            });
        }else {
            mp = MediaPlayer.create(context, R.raw.shitingnan);
            mp.start();
            mp.setOnCompletionListener(new MediaPlayer.OnCompletionListener() {
                @Override
                public void onCompletion(MediaPlayer mp) {
                    animatorMan.pause();
                    man_song.setImageResource(R.drawable.horn_d);
                }
            });
        }
        Config.CACHE.put("sex",0);
        manVoiceChoose.setSelected(true);
        womanVoiceChoose.setSelected(false);
    }
}
