package com.data4sports.chasecricket.apiClients;

import android.content.Context;
import android.support.annotation.NonNull;

import com.data4sports.chasecricket.R;
import com.data4sports.chasecricket.applicationConstants.ApplicationConstants;
import com.data4sports.chasecricket.models.MyApplication;
import com.data4sports.chasecricket.models.ProfileDetailsModel;

import java.util.Map;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class ProfileDetailApiController implements ApplicationConstants {
    //context
    private Context context = null;
    //header
    private Map<String, String> header;

    private ProfileDetailsModel.ProfileCallback planCallback = null;

    public ProfileDetailApiController() {
    }

    public ProfileDetailApiController setContext(Context context) {
        this.context = context;
        return this;
    }

    public ProfileDetailApiController setHeader(Map<String, String> header) {
        this.header = header;
        return this;
    }

    public ProfileDetailApiController setProfileCallback(ProfileDetailsModel.ProfileCallback  planCallback) {
        this.planCallback = planCallback;
        return this;
    }

    public void getProfileDetailsCall(int userid) {

        /*
         * Web service contact
         * #  library : Retrofit
         */
        //call back object null checking
        if (planCallback == null) {
            return;
        }
        //activity reference null checking
        if (context == null) {
            //fail
            planCallback.onFail("Null Reference ");
            return;
        }
        //header map null checking
        if (header == null) {
            //fail
            planCallback.onFail(context.getString(R.string.went_wrong));
            return;
        }
                            MyApplication cricketChaseApplication = MyApplication.get(context);
                            RetrofitService retrofitService = cricketChaseApplication.getRetrofitService();
                            //request call
                            //login using email
                            Call<ProfileDetailsModel.Response> getUser = retrofitService.getUserDetails(header,userid);
                            getUser.enqueue(new Callback<ProfileDetailsModel.Response>() {
                                @Override
                                public void onResponse(@NonNull Call<ProfileDetailsModel.Response> call,
                                                       @NonNull Response<ProfileDetailsModel.Response> response) {

                                    //call success checking
                                    if (response.code() == 200) {
                                        try {
                                            //response data
                                            //  DashboardModel.Response resultBean = response.body();
                                            //null checking
                                            if (response.body() == null) {
                                                //fail
                                                planCallback.onFail(context.getString(R.string.went_wrong));
                                                return;
                                            }
                                            //success
                                            planCallback.onSuccess(true, response.body());
                                        } catch (Exception e) {
                                            //fail
                                            planCallback.onFail(context.getString(R.string.went_wrong));
                                        }
                                    } else {
                                        //fail
                                        planCallback.onFail(ErrorCodeDisplay.error(response.code()));
                                    }

                                }

                                @Override
                                public void onFailure(@NonNull Call<ProfileDetailsModel.Response> call,
                                                      @NonNull Throwable t) {

                                }
                            });

                        }
                    }

