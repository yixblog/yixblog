package cn.yix.blog.utils;

import cn.yix.blog.utils.bean.ResetCode;

/**
 * Created with IntelliJ IDEA.
 * User: yxdave
 * Date: 13-5-26
 * Time: 下午5:02
 */
public class ResetCodeFactory {
    public static ResetCode generateResetCode(int type, String uid) {
        if (checkType(type)) {
            return new ResetCode(type, System.currentTimeMillis(), uid);
        }
        return null;
    }

    private static boolean checkType(int type) {
        final int[] availableTypes = {ResetCode.TYPE_RESET_PWD, ResetCode.TYPE_RESET_EMAIL, ResetCode.TYPE_CONFIRM_EMAIL};
        for (int availableType : availableTypes) {
            if (type == availableType) {
                return true;
            }
        }
        return false;
    }

    public static ResetCode generateResetCode(String resetCode) {
        try {
            String head = resetCode.substring(0, 3);
            int type = -1;
            switch (head) {
                case "pwd":
                    type = ResetCode.TYPE_RESET_PWD;
                    break;
                case "ema":
                    type = ResetCode.TYPE_RESET_EMAIL;
                    break;
                case "cem":
                    type = ResetCode.TYPE_CONFIRM_EMAIL;
                    break;
            }
            String lengthString = resetCode.substring(3, 5);
            int length = Integer.parseInt(lengthString, 10);
            long timeStamp = Long.parseLong(resetCode.substring(5, 5 + length), 16);
            String uid = resetCode.substring(5 + length);
            return type >= 0 ? new ResetCode(type, timeStamp, uid) : null;
        } catch (IndexOutOfBoundsException ex) {
            return null;
        }
    }
}
