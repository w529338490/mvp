package my.easycommunity.eventbus;

/**
 * Created by Administrator on 2017/7/16.
 */

public class WebViewEvent
{
    private String url;
    private  String titttle;


    public WebViewEvent(String url, String titttle)
    {
        this.url = url;
        this.titttle = titttle;
    }

    public String getUrl()
    {
        return url;
    }

    public String getTitttle()
    {
        return titttle;
    }


}
