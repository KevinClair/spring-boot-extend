package com.extend.log.cat.dubbo;


import com.dianping.cat.Cat;

/**
 * DubboCat.
 *
 * @author KevinClair
 */
public class DubboCat {

    private static boolean isEnable=true;

    /**
     * 禁用dubbo cat
     */
    public static void disable(){
        isEnable=false;
    }

    /**
     * 启用dubbo cat
     */
    public static void enable(){
        isEnable=true;
    }

    /**
     * 是否有效
     * @return
     */
    public static boolean isEnable(){
        boolean isCatEnabled = false;
        try {
            isCatEnabled = Cat.getManager().isCatEnabled();
        } catch (Throwable e) {
            Cat.logError(e);
        }
        return isCatEnabled && isEnable;
    }
}
