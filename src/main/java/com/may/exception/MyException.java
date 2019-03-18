package com.may.exception;

/**
 * ClassName: MyException
 * CreateTime 2017年11月28日 下午1:42:48
 * author : may
 * Description: 自定义异常
 */

import com.may.enums.ResultEnum;

/**
 * ClassName : MyException
 * Author : liqinchao
 * CreateTime : 2019/3/18 18:11
 * Description : 自定义异常
 */
public class MyException extends Exception {
    /**
     *
     */
    private static final long serialVersionUID = 1L;

    private int level;

    public MyException() {
        super(ResultEnum.ERR.getDisplay());
        this.level = ResultEnum.ERR.getValue();
    }

    public MyException(String message, int level) {
        super(message);
        this.level = level;
    }

    public MyException(Throwable e, int level) {
        super(e);
        this.level = level;
    }

    public MyException(String message, Throwable e, int level) {
        super(message, e);
        this.level = level;
    }

    public MyException(String message) {
        super(message);
        this.level = ResultEnum.ERR.getValue();
    }

    public MyException(Throwable cause) {
        super(cause);
        this.level = ResultEnum.ERR.getValue();
    }

    public int getLevel() {
        return level;
    }

    public void setLevel(int level) {
        this.level = level;
    }

}
