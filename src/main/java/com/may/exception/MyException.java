package com.may.exception;

/**
 * ClassName: MyException
 * CreateTime 2017年11月28日 下午1:42:48
 * author : liqinchao
 * Description: 自定义异常
 */
public class MyException extends Exception {
    /**
     *
     */
    private static final long serialVersionUID = 1L;
    /**
     * 操作失败-level
     */
    public static final int ERROR_LEVEL = 0;
    /**
     * 警告
     */
    public static final int WARNING_LEVEL = 2;
    /**
     * 参数错误
     */
    public static final int ERROR_LEVEL_PARAM = 3;
    /**
     * 参数错误消息
     */
    public static final String ERROR_MSG_PARAM = "参数错误";

    private int level;

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
        this.level = ERROR_LEVEL;
    }

    public MyException(Throwable cause) {
        super(cause);
        this.level = ERROR_LEVEL;
    }

    public int getLevel() {
        return level;
    }

    public void setLevel(int level) {
        this.level = level;
    }
    static public void throwParamError(String message)throws MyException {
        MyException we = new MyException(message, ERROR_LEVEL_PARAM);
        we.printStackTrace();
        throw we;
    }

}
