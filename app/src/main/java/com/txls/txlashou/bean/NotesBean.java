package com.txls.txlashou.bean;

import java.io.Serializable;
import java.util.List;

/**
 * 作者：YHL
 * 时间： 2017/2/28 16:41
 */

public class NotesBean implements Serializable{

    /**
     * code : 2000
     * message : 执行成功
     * response : {"repayRecord":[{"bankNo":"7610","repayAmount":166.89,"repayTime":1480093452000,"repayType":2},{"bankNo":"7610","repayAmount":166.89,"repayTime":1482682674000,"repayType":2},{"bankNo":"7610","repayAmount":166.88,"repayTime":1485361556000,"repayType":2},{"bankNo":"7610","repayAmount":166.89,"repayTime":1488040019000,"repayType":2}]}
     */

    private String code;
    private String message;
    private ResponseBean response;

    public String getCode() {
        return code;
    }

    public void setCode(String code) {
        this.code = code;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public ResponseBean getResponse() {
        return response;
    }

    public void setResponse(ResponseBean response) {
        this.response = response;
    }

    public static class ResponseBean implements Serializable{
        private List<RepayRecordBean> repayRecord;

        public List<RepayRecordBean> getRepayRecord() {
            return repayRecord;
        }

        public void setRepayRecord(List<RepayRecordBean> repayRecord) {
            this.repayRecord = repayRecord;
        }

        public static class RepayRecordBean implements Serializable{
            /**
             * bankNo : 7610
             * repayAmount : 166.89
             * repayTime : 1480093452000
             * repayType : 2
             */

            private String bankNo;
            private double repayAmount;
            private String repayTime;
            private int repayType;

            public String getBankNo() {
                return bankNo;
            }

            public void setBankNo(String bankNo) {
                this.bankNo = bankNo;
            }

            public double getRepayAmount() {
                return repayAmount;
            }

            public void setRepayAmount(double repayAmount) {
                this.repayAmount = repayAmount;
            }

            public String getRepayTime() {
                return repayTime;
            }

            public void setRepayTime(String repayTime) {
                this.repayTime = repayTime;
            }

            public int getRepayType() {
                return repayType;
            }

            public void setRepayType(int repayType) {
                this.repayType = repayType;
            }
        }
    }
}
