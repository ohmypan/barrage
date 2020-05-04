package com.huahong.barrage.filter;


import org.springframework.stereotype.Component;
import org.springframework.util.StringUtils;

/**
 * @Author pgz
 * @Description $
 * @Date 2020/5/4$ $
 * @Param $
 * @return $
 **/
@Component
public class YellowFilter implements TextFilter {

    /**
     * 过滤
     * @param text
     * @return
     */
    @Override
    public String filter(String text) {
        return this.yellowFilter(text);
    }

    /**
     * 过滤带有黄色信息的字符
     * @param text
     * @return
     */
    private String yellowFilter(String text){
        assert !StringUtils.isEmpty(text);
        String reg = "(?:黄色|根硕|骚)";
        return text.replaceAll(reg,"*");
    }
}
