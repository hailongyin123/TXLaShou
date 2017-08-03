package com.txls.txlashou.bean;

import java.io.Serializable;
import java.util.List;

/**
 * 作者：YHL
 * 时间： 2017/2/27 15:45
 */

public class BillListBean implements Serializable{
    /**
     * message : 执行成功
     * response : {"repayTotalAmount":1999,"repayFinalTime":"2016-08-14","repayPlan":[{"SCHEDULE_ID":120673,"issueRepayAmount":873.64,"repayTime":"2016-08-14","bankNo":"0139","overdueFee":0,"currTerm":7,"issue":"7/10","repayStatus":3},{"SCHEDULE_ID":16,"issueRepayAmount":237.12,"repayTime":"2016-07-14","bankNo":"0139","overdueFee":0,"currTerm":6,"issue":"6/10","repayStatus":3},{"SCHEDULE_ID":15,"issueRepayAmount":237.12,"repayTime":"2016-06-14","bankNo":"0139","overdueFee":0,"currTerm":5,"issue":"5/10","repayStatus":3},{"SCHEDULE_ID":14,"issueRepayAmount":237.12,"repayTime":"2016-05-14","bankNo":"0139","overdueFee":0,"currTerm":4,"issue":"4/10","repayStatus":3},{"SCHEDULE_ID":13,"issueRepayAmount":367.12,"repayTime":"2016-04-14","bankNo":"0139","overdueFee":0,"currTerm":3,"issue":"3/10","repayStatus":3},{"SCHEDULE_ID":12,"issueRepayAmount":447.12,"repayTime":"2016-03-14","bankNo":"0139","overdueFee":0,"currTerm":2,"issue":"2/10","repayStatus":3},{"SCHEDULE_ID":11,"issueRepayAmount":267.12,"repayTime":"2016-02-14","bankNo":"0139","overdueFee":0,"currTerm":1,"issue":"1/10","repayStatus":3}]}
     * code : 2000
     */

    private String message;
    private ResponseBean response;
    private String code;

    /**
     * 单例
     */
    private BillListBean(){}
    static BillListBean billListBean;
    public static BillListBean getInstance() {
        if (billListBean == null) {
            billListBean = new BillListBean();
        }
        return billListBean;
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

    public String getCode() {
        return code;
    }

    public void setCode(String code) {
        this.code = code;
    }

    public static class ResponseBean implements Serializable{
        /**
         * repayTotalAmount : 1999.0
         * repayFinalTime : 2016-08-14
         * repayPlan : [{"SCHEDULE_ID":120673,"issueRepayAmount":873.64,"repayTime":"2016-08-14","bankNo":"0139","overdueFee":0,"currTerm":7,"issue":"7/10","repayStatus":3},{"SCHEDULE_ID":16,"issueRepayAmount":237.12,"repayTime":"2016-07-14","bankNo":"0139","overdueFee":0,"currTerm":6,"issue":"6/10","repayStatus":3},{"SCHEDULE_ID":15,"issueRepayAmount":237.12,"repayTime":"2016-06-14","bankNo":"0139","overdueFee":0,"currTerm":5,"issue":"5/10","repayStatus":3},{"SCHEDULE_ID":14,"issueRepayAmount":237.12,"repayTime":"2016-05-14","bankNo":"0139","overdueFee":0,"currTerm":4,"issue":"4/10","repayStatus":3},{"SCHEDULE_ID":13,"issueRepayAmount":367.12,"repayTime":"2016-04-14","bankNo":"0139","overdueFee":0,"currTerm":3,"issue":"3/10","repayStatus":3},{"SCHEDULE_ID":12,"issueRepayAmount":447.12,"repayTime":"2016-03-14","bankNo":"0139","overdueFee":0,"currTerm":2,"issue":"2/10","repayStatus":3},{"SCHEDULE_ID":11,"issueRepayAmount":267.12,"repayTime":"2016-02-14","bankNo":"0139","overdueFee":0,"currTerm":1,"issue":"1/10","repayStatus":3}]
         */

        private double repayTotalAmount;
        private String repayFinalTime;
        private List<RepayPlanBean> repayPlan;

        public double getRepayTotalAmount() {
            return repayTotalAmount;
        }

        public void setRepayTotalAmount(double repayTotalAmount) {
            this.repayTotalAmount = repayTotalAmount;
        }

        public String getRepayFinalTime() {
            return repayFinalTime;
        }

        public void setRepayFinalTime(String repayFinalTime) {
            this.repayFinalTime = repayFinalTime;
        }

        public List<RepayPlanBean> getRepayPlan() {
            return repayPlan;
        }

        public void setRepayPlan(List<RepayPlanBean> repayPlan) {
            this.repayPlan = repayPlan;
        }

        public static class RepayPlanBean implements Serializable{
            /**
             * SCHEDULE_ID : 120673
             * issueRepayAmount : 873.64
             * repayTime : 2016-08-14
             * bankNo : 0139
             * overdueFee : 0
             * currTerm : 7
             * issue : 7/10
             * repayStatus : 3
             */

            private int SCHEDULE_ID;
            private double issueRepayAmount;
            private String repayTime;
            private String bankNo;
            private int overdueFee;
            private int currTerm;
            private String issue;
            private int repayStatus;
            private String timePoint;

            public int getClickStatus() {
                return clickStatus;
            }

            public void setClickStatus(int clickStatus) {
                this.clickStatus = clickStatus;
            }

            private int clickStatus;


            public String getTimePoint() {
                return timePoint;
            }

            public void setTimePoint(String timePoint) {
                this.timePoint = timePoint;
            }

            public int getSCHEDULE_ID() {
                return SCHEDULE_ID;
            }

            public void setSCHEDULE_ID(int SCHEDULE_ID) {
                this.SCHEDULE_ID = SCHEDULE_ID;
            }

            public double getIssueRepayAmount() {
                return issueRepayAmount;
            }

            public void setIssueRepayAmount(double issueRepayAmount) {
                this.issueRepayAmount = issueRepayAmount;
            }

            public String getRepayTime() {
                return repayTime;
            }

            public void setRepayTime(String repayTime) {
                this.repayTime = repayTime;
            }

            public String getBankNo() {
                return bankNo;
            }

            public void setBankNo(String bankNo) {
                this.bankNo = bankNo;
            }

            public int getOverdueFee() {
                return overdueFee;
            }

            public void setOverdueFee(int overdueFee) {
                this.overdueFee = overdueFee;
            }

            public int getCurrTerm() {
                return currTerm;
            }

            public void setCurrTerm(int currTerm) {
                this.currTerm = currTerm;
            }

            public String getIssue() {
                return issue;
            }

            public void setIssue(String issue) {
                this.issue = issue;
            }

            public int getRepayStatus() {
                return repayStatus;
            }

            public void setRepayStatus(int repayStatus) {
                this.repayStatus = repayStatus;
            }
        }
    }
}
