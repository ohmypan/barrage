package com.huahong.barrage.filter;

/**
 * @Author pgz
 * @Description $
 * @Date 2020/5/4$ $
 * @Param $
 * @return $
 **/
public class BadMessageFilter implements TextFilter {


    @Override
    public String filter(String text) {
        return this.badMessageFilter(text);
    }

    private String badMessageFilter(String text){
        String reg = "(?:你妈逼|吊|操)";
        return text.replaceAll(reg,"|");
    }
}
