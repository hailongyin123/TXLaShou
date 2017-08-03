package com.txls.txlashou.bean;

import java.io.Serializable;

/**
 * 作者：YHL
 * 时间： 2017/2/28 18:07
 */

public class PrePayBean implements Serializable{


    /**
     * response : {"periods":24,"residueAmount":1706.28,"loanAmount":2878,"residuePeriods":13,"advanceServiceFee":3.19,"advanceAmount":1736.95}
     * code : 2000
     * message : 执行成功
     */

    private ResponseBean response;
    private String code;
    private String message;

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

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public static class ResponseBean implements Serializable{
        /**
         * periods : 24
         * residueAmount : 1706.28
         * loanAmount : 2878.0
         * residuePeriods : 13
         * advanceServiceFee : 3.19
         * advanceAmount : 1736.95
         */

        private int periods;
        private double residueAmount;
        private double loanAmount;
        private int residuePeriods;
        private double advanceServiceFee;
        private double advanceAmount;

        public double getRepayAmount() {
            return repayAmount;
        }

        public void setRepayAmount(double repayAmount) {
            this.repayAmount = repayAmount;
        }

        private double repayAmount;

        public int getPeriods() {
            return periods;
        }

        public void setPeriods(int periods) {
            this.periods = periods;
        }

        public double getResidueAmount() {
            return residueAmount;
        }

        public void setResidueAmount(double residueAmount) {
            this.residueAmount = residueAmount;
        }

        public double getLoanAmount() {
            return loanAmount;
        }

        public void setLoanAmount(double loanAmount) {
            this.loanAmount = loanAmount;
        }

        public int getResiduePeriods() {
            return residuePeriods;
        }

        public void setResiduePeriods(int residuePeriods) {
            this.residuePeriods = residuePeriods;
        }

        public double getAdvanceServiceFee() {
            return advanceServiceFee;
        }

        public void setAdvanceServiceFee(double advanceServiceFee) {
            this.advanceServiceFee = advanceServiceFee;
        }

        public double getAdvanceAmount() {
            return advanceAmount;
        }

        public void setAdvanceAmount(double advanceAmount) {
            this.advanceAmount = advanceAmount;
        }
    }
}
