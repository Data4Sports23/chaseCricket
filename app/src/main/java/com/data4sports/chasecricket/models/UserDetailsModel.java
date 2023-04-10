package com.data4sports.chasecricket.models;

public class UserDetailsModel {
    public interface PlanCallback {

        void onSuccess(boolean result, Response resultBean);

        void onFail(String message);

    }
    public static class Response {


        private String plan;

        public String getPlan() {
            return plan;
        }

        public void setPlan(String plan) {
            this.plan = plan;
        }
    }
}
