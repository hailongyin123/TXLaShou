package com.txls.txlashou.bean;

import java.io.Serializable;

/**
 * 作者：YHL
 * 时间： 2017/2/21 18:35
 */

public class UserBean  implements Serializable{


    /**
     * message : 执行成功
     * response : {"authSign":0,"token":"sYOrQ1qLlj7e9mtSb9VhqA8ojQS4GSRYwLNkqV1jLkKWEgQbcgtXHfHvfslIOz4e","userId":2}
     * result : 2000
     * code : 2000
     */

    private String message;
    private ResponseBean response;
    private String result;
    private String code;
    /**
     * 单例
     */
    private UserBean(){}
    static UserBean userBean;
    public static UserBean getInstance() {
        if (userBean == null) {
            userBean = new UserBean();
        }
        return userBean;
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

    public String getResult() {
        return result;
    }

    public void setResult(String result) {
        this.result = result;
    }

    public String getCode() {
        return code;
    }

    public void setCode(String code) {
        this.code = code;
    }

    public static class ResponseBean implements Serializable{
        /**
         * authSign : 0
         * token : sYOrQ1qLlj7e9mtSb9VhqA8ojQS4GSRYwLNkqV1jLkKWEgQbcgtXHfHvfslIOz4e
         * userId : 2
         */
        public  String contrPdf;
        private int authSign;
        private String token;
        private int userId;
        private String checkCode;
        private String contractPath;
        private int paymentPasswordStatus;
        private String contrNbr;

        public String getContrNbr() {
            return contrNbr;
        }

        public String getContrPdf() {
            return contrPdf;
        }

        public void setContrPdf(String contrPdf) {
            this.contrPdf = contrPdf;
        }

        public void setContrNbr(String contrNbr) {
            this.contrNbr = contrNbr;
        }

        public int getPaymentPasswordStatus() {
            return paymentPasswordStatus;
        }

        public void setPaymentPasswordStatus(int paymentPasswordStatus) {
            this.paymentPasswordStatus = paymentPasswordStatus;
        }

        public String getContractPath() {
            return contractPath;
        }

        public void setContractPath(String contractPath) {
            this.contractPath = contractPath;
        }

        public String getRealName() {
            return realName;
        }

        public void setRealName(String realName) {
            this.realName = realName;
        }

        public String getIdNo() {
            return idNo;
        }

        public void setIdNo(String idNo) {
            this.idNo = idNo;
        }

        public String getIssue() {
            return issue;
        }

        public void setIssue(String issue) {
            this.issue = issue;
        }

        public String getRepayAmount() {
            return repayAmount;
        }

        public void setRepayAmount(String repayAmount) {
            this.repayAmount = repayAmount;
        }

        public String getLoanAmount() {
            return loanAmount;
        }

        public void setLoanAmount(String loanAmount) {
            this.loanAmount = loanAmount;
        }

        public String getIssueRepayAmount() {
            return issueRepayAmount;
        }

        public void setIssueRepayAmount(String issueRepayAmount) {
            this.issueRepayAmount = issueRepayAmount;
        }

        public String getOverdueStatus() {
            return overdueStatus;
        }

        public void setOverdueStatus(String overdueStatus) {
            this.overdueStatus = overdueStatus;
        }

        /**
         * 认证后用户信息字段
         */
        private String realName;
        private String idNo;
        private String issue;
        private String repayAmount;
        private String loanAmount;
        private String issueRepayAmount;
        private String overdueStatus;


        public String getCheckCode() {
            return checkCode;
        }

        public void setCheckCode(String checkCode) {
            this.checkCode = checkCode;
        }

        public int getAuthSign() {
            return authSign;
        }

        public void setAuthSign(int authSign) {
            this.authSign = authSign;
        }

        public String getToken() {
            return token;
        }

        public void setToken(String token) {
            this.token = token;
        }

        public int getUserId() {
            return userId;
        }

        public void setUserId(int userId) {
            this.userId = userId;
        }
    }
}
