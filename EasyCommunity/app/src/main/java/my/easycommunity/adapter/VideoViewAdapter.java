package my.easycommunity.adapter;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.jakewharton.rxbinding.view.RxView;
import java.util.List;
import java.util.concurrent.TimeUnit;
import butterknife.ButterKnife;
import fm.jiecao.jcvideoplayer_lib.JCVideoPlayer;
import fm.jiecao.jcvideoplayer_lib.JCVideoPlayerStandard;
import my.easycommunity.R;
import my.easycommunity.entity.video.Video;
import rx.Observer;

/**
 * Created by Administrator on 2017/3/27.
 */

public class VideoViewAdapter extends RecyclerView.Adapter<VideoViewAdapter.Holder>
{

    List<Video.DataBean.DataBeans> results;
    LayoutInflater inflater;
    private Context context;
    public VideoViewAdapter(  Context context )
    {
        this.context = context;
        inflater = LayoutInflater.from(context);
    }

    public void setData(  List<Video.DataBean.DataBeans> data)
    {
        this.results = data;
        notifyDataSetChanged();
    }
    @Override
    public Holder onCreateViewHolder(ViewGroup parent, int viewType)
    {

        Holder holder = null;
        if (holder == null)
        {
            holder = new Holder(inflater.inflate(R.layout.videoview_adapter, parent, false));
        }
        return holder;
    }

    @Override
    public void onBindViewHolder(final Holder holder, final int position)
    {

        if (results != null && results.get(position) != null && results.get(position).group != null)
        {
            final String thunbUrl=results.get(position)
                    .getGroup().medium_cover
                    .getUrl_list().get(0).url;

            if(results.get(position).getGroup().text!=null &&
                    !results.get(position).getGroup().text.equals("")){
                holder.custom_videoplayer.setUp(
                        results.get(position).group.mp4_url,
                        JCVideoPlayer.SCREEN_LAYOUT_LIST,
                        results.get(position).getGroup().text
                );

                if(!TextUtils.isEmpty(thunbUrl)){
                    Glide.with(context)
                            .load(String.valueOf(thunbUrl))
                            .override(150,150)
                            .diskCacheStrategy(DiskCacheStrategy.NONE)
                            .centerCrop()
                            .into(holder.custom_videoplayer.thumbImageView);
                }
            }

            RxView.clicks(holder.saved)
                    .throttleFirst(1, TimeUnit.SECONDS)
                    .subscribe(new Observer<Object>() {
                        @Override
                        public void onCompleted() {
                        }

                        @Override
                        public void onError(Throwable e) {

                        }

                        @Override
                        public void onNext(Object o) {

                        }
                    });
        }
    }
    @Override
    public int getItemCount()
    {
        return results == null?0:results.size();
    }

    public class Holder extends RecyclerView.ViewHolder
    {
        TextView tittle;
        JCVideoPlayerStandard custom_videoplayer;
        TextView saved ;
        TextView img_share ;
        public Holder(View view)
        {
            super(view);
            ButterKnife.inject(this,view);
            tittle = (TextView) view.findViewById(R.id.tittle);
            saved = (TextView) view.findViewById(R.id.saved);
            img_share = (TextView) view.findViewById(R.id.img_share);
            custom_videoplayer = (JCVideoPlayerStandard) view.findViewById(R.id.custom_videoplayer);

        }
    }

}
