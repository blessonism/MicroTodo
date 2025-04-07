package top.sheepspace.controller;

/**
 * @author 28297
 */
public class Code {
    public static final Integer SAVE_OK = 20011;
    public static final Integer DELETE_OK = 20021;
    public static final Integer UPDATE_OK = 20031;
    public static final Integer GET_OK = 20041;
    public static final Integer SET_REMINDER_OK = 20051;

    public static final Integer SAVE_ERR = 20010;
    public static final Integer DELETE_ERR = 20020;
    public static final Integer UPDATE_ERR = 20030;
    public static final Integer GET_ERR = 20040;
    public static final Integer SET_REMINDER_ERR = 20050;

    /**
     * 业务异常
     */
    public static final Integer BUSINESS_ERR = 60002;
    public static final Integer TASK_NOT_FOUND = 60003;

    /**
     * 系统异常
     */
    public static final Integer SYSTEM_ERR = 50001;
    public static final Integer SYSTEM_TIMEOUT_ERR = 50002;
    public static final Integer SYSTEM_UNKNOWN_ERR = 59999;

    /**
     * 未知异常
     */
    public static final Integer UNKNOWN_ERR = 70001;
}
