package my.easycommunity.eventbus;

/**
 * Created by Administrator on 2017/7/28.
 */

public class MainFragmentEvent
{
    private  int flag= 0;
    public MainFragmentEvent(int flag)
    {
        this.flag=flag;
    }

    public int getFlag()
    {
        return flag;
    }

    public void setFlag(int flag)
    {
        this.flag = flag;
    }
}
