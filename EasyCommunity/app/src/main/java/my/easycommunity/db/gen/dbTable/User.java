package my.easycommunity.db.gen.dbTable;

import org.greenrobot.greendao.annotation.Entity;
import org.greenrobot.greendao.annotation.Id;
import org.greenrobot.greendao.annotation.NotNull;
import org.greenrobot.greendao.annotation.Unique;
import org.greenrobot.greendao.annotation.Generated;

/**
 * Created by Administrator on 2017/7/24.
 * 用户表
 */
@Entity
public class User
{
    @Id (autoincrement = true)
    public Long id;
    @Unique @NotNull
    public String name;
    @NotNull
    public long pwd;
    @Generated(hash = 652780227)
    public User(Long id, @NotNull String name, long pwd) {
        this.id = id;
        this.name = name;
        this.pwd = pwd;
    }
    @Generated(hash = 586692638)
    public User() {
    }
    public Long getId() {
        return this.id;
    }
    public void setId(Long id) {
        this.id = id;
    }
    public String getName() {
        return this.name;
    }
    public void setName(String name) {
        this.name = name;
    }
    public long getPwd() {
        return this.pwd;
    }
    public void setPwd(long pwd) {
        this.pwd = pwd;
    }
}