package com.data4sports.chasecricket.models;

public class ProfileDetailsModel {
    public interface ProfileCallback {

        void onSuccess(boolean result, Response resultBean);

        void onFail(String message);

    }
    public static class Response {

        /**
         * name : test1
         * email : test2
         */

        private String name;
        private String email;

        public String getName() {
            return name;
        }

        public void setName(String name) {
            this.name = name;
        }

        public String getEmail() {
            return email;
        }

        public void setEmail(String email) {
            this.email = email;
        }
    }
}
