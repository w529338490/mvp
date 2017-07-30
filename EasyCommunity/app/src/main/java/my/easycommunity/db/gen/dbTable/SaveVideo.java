package my.easycommunity.db.gen.dbTable;

import org.greenrobot.greendao.annotation.Entity;
import org.greenrobot.greendao.annotation.Id;
import org.greenrobot.greendao.annotation.NotNull;
import org.greenrobot.greendao.annotation.Unique;
import org.greenrobot.greendao.annotation.Generated;

/**
 * Created by Administrator on 2017/7/30.
 */
@Entity
public class SaveVideo
{
    @Id(autoincrement = true )
    public Long id;
    //哪个用户
    @NotNull
    String username;
    @NotNull
    String thumburl;  //视屏缩略图
    @NotNull
    String videouri; //视屏地址
    String tittle;   //视屏标题
    @Generated(hash = 239883010)
    public SaveVideo(Long id, @NotNull String username, @NotNull String thumburl,
            @NotNull String videouri, String tittle) {
        this.id = id;
        this.username = username;
        this.thumburl = thumburl;
        this.videouri = videouri;
        this.tittle = tittle;
    }
    @Generated(hash = 308173506)
    public SaveVideo() {
    }
    public String getUsername() {
        return this.username;
    }
    public void setUsername(String username) {
        this.username = username;
    }
    public String getThumburl() {
        return this.thumburl;
    }
    public void setThumburl(String thumburl) {
        this.thumburl = thumburl;
    }
    public String getVideouri() {
        return this.videouri;
    }
    public void setVideouri(String videouri) {
        this.videouri = videouri;
    }
    public String getTittle() {
        return this.tittle;
    }
    public void setTittle(String tittle) {
        this.tittle = tittle;
    }
    public Long getId() {
        return this.id;
    }
    public void setId(Long id) {
        this.id = id;
    }

}
