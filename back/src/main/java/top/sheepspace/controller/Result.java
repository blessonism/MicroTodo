package top.sheepspace.controller;


/**
 * @author 28297
 */

public class Result<T> {
    private Object data;
    private Integer code;
    private String msg;
    private T dataT;


    public Result() {
    }

    public Result(Integer code,Object data) {
        this.data = data;
        this.code = code;
    }

    public Result(Integer code, Object data, String msg) {
        this.data = data;
        this.code = code;
        this.msg = msg;
    }
    public void setDataT(T data) {
        this.data = data;
    }

    public T getDataT() {
        return dataT;
    }

    public Result(T dataT) {
        this.dataT = dataT;
    }


    public static <T> Result<T> success(T dataT,String msg) {
        Result<T> result = new Result<>(dataT);
        System.out.println(dataT.getClass());
        result.setCode(0);
        result.setMsg(msg);
        return result;
    }

    public static Result error(String code, String msg) {
        Result result = new Result();
        result.setCode(Integer.valueOf(code));
        result.setMsg(msg);
        return result;
    }

    public Object getData() {
        return data;
    }

    public void setData(Object data) {
        this.data = data;
    }

    public Integer getCode() {
        return code;
    }

    public void setCode(Integer code) {
        this.code = code;
    }

    public String getMsg() {
        return msg;
    }

    public void setMsg(String msg) {
        this.msg = msg;
    }
}
