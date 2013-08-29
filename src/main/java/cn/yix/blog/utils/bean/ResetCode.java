package cn.yix.blog.utils.bean;

/**
 * Created with IntelliJ IDEA.
 * User: yxdave
 * Date: 13-5-26
 * Time: 下午5:03
 */
public class ResetCode {
    public static final int TYPE_RESET_PWD = 0;
    public static final int TYPE_RESET_EMAIL = 1;
    public static final int TYPE_CONFIRM_EMAIL = 2;
    private int codeType;
    private long createTime;
    private String uid;

    public ResetCode(int codeType, long createTime, String uid) {
        this.codeType = codeType;
        this.createTime = createTime;
        this.uid = uid;
    }

    public String generateResetCode(){
        StringBuilder codeBuilder = new StringBuilder();
        switch (codeType){
            case TYPE_RESET_PWD:
                codeBuilder.append("pwd");
                break;
            case TYPE_RESET_EMAIL:
                codeBuilder.append("ema");
                break;
            case TYPE_CONFIRM_EMAIL:
                codeBuilder.append("cem");
                break;
        }
        String timeHex = Long.toHexString(createTime);
        if (timeHex.length()<10){
            codeBuilder.append("0");
        }
        codeBuilder.append(timeHex.length());
        codeBuilder.append(timeHex);
        codeBuilder.append(uid);
        return codeBuilder.toString().toLowerCase();
    }

    public int getCodeType() {
        return codeType;
    }

    public long getCreateTime() {
        return createTime;
    }

    public String getUid() {
        return uid;
    }
}
