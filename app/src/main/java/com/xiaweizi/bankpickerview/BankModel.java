package com.xiaweizi.bankpickerview;

import com.google.gson.annotations.SerializedName;

/**
 * <pre>
 *     author : xiaweizi
 *     class  : com.xiaweizi.bankpickerview.BankModel
 *     e-mail : 1012126908@qq.com
 *     time   : 2018/01/18
 *     desc   :
 * </pre>
 */

public class BankModel {
    /** 描述 */
    @SerializedName("summ")
    public String bankDesc = "";
    /** 银行行号 */
    @SerializedName("name")
    public String bankName = "";
    /** 银行图标 */
    @SerializedName("logo")
    public String bankLogo = "";
    /** 行号 */
    @SerializedName("numb")
    public String bankCode = "";

    @Override
    public String toString() {
        return "BankModel:" + "\n" +
                "bankDesc='" + bankDesc + '\n' +
                "bankName='" + bankName + '\n' +
                ",bankCode='" + bankCode + '\n';
    }
}
