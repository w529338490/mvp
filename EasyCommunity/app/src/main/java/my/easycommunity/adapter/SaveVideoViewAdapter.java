package my.easycommunity.adapter;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.orhanobut.logger.Logger;

import java.util.List;

import fm.jiecao.jcvideoplayer_lib.JCVideoPlayer;
import fm.jiecao.jcvideoplayer_lib.JCVideoPlayerStandard;
import my.easycommunity.R;
import my.easycommunity.db.gen.dbTable.SaveVideo;
import my.easycommunity.ui.user.save.saveVideoFragment;
import my.easycommunity.utill.ToastUtil;

/**
 * Created by Administrator on 2017/7/30.
 */

public class SaveVideoViewAdapter extends RecyclerView.Adapter<SaveVideoViewAdapter.Holder>
{
    List<SaveVideo> saveVideos;
    LayoutInflater inflater;
    private Context context;
    DownlodLiner downlinsner;

    public SaveVideoViewAdapter(List<SaveVideo> saveVideos, Context context)
    {

        this.saveVideos = saveVideos;
        this.inflater = LayoutInflater.from(context);
        this.context=context;


    }

    @Override
    public Holder onCreateViewHolder(ViewGroup parent, int viewType)
    {

        Holder holder = null;
        if (holder == null)
        {
            holder = new Holder(inflater.inflate(R.layout.savevideo_adapter, parent, false));
        }
        return holder;
    }

    @Override
    public void onBindViewHolder(Holder holder, final int position)
    {

        if (saveVideos != null && saveVideos.get(position) != null )
        {
            final String thunbUrl=saveVideos.get(position).getThumburl();
            //查看是否有下载到本地的 视屏 有则加载本地,无则加载网络

            {
                holder.custom_videoplayer.canShouwDialog=true;
                holder.custom_videoplayer.setUp(
                        saveVideos.get(position).getVideouri(),
                        JCVideoPlayer.SCREEN_LAYOUT_LIST,
                        saveVideos.get(position).getTittle());
                ToastUtil.show("播放网络视屏");

            }
            Glide.with(context)
                    .load(String.valueOf(thunbUrl))
                    .centerCrop()
                    .into(holder.custom_videoplayer.thumbImageView);

        }
        //下载 视屏
        holder.saved.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View v)
            {
                downlinsner.down(saveVideos.get(position).getVideouri(),saveVideos.get(position).getTittle());
            }
        });

        //删除保存
        holder.delete.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View view)
            {
                downlinsner.delet(saveVideos.get(position),position);
            }
        });

    }

    @Override
    public int getItemCount()
    {
        return saveVideos.size();
    }

    public class Holder extends RecyclerView.ViewHolder
    {
        TextView tittle;
        JCVideoPlayerStandard custom_videoplayer;
        TextView saved ;
        TextView img_share ;
        TextView delete ;

        public Holder(View view)
        {
            super(view);
            tittle = (TextView) view.findViewById(R.id.tittle);
            saved = (TextView) view.findViewById(R.id.saved);
            img_share = (TextView) view.findViewById(R.id.img_share);
            delete = (TextView) view.findViewById(R.id.delete);
            custom_videoplayer = (JCVideoPlayerStandard) view.findViewById(R.id.custom_videoplayer);
        }
    }
    public void SetDownlodLiner(DownlodLiner liner)
    {
        this.downlinsner=liner;
    }
    public interface  DownlodLiner
    {
        void down(String path,String tittle);
        void delet(SaveVideo currentBean,int position);
    }
}
