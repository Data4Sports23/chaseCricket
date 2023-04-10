package com.data4sports.chasecricket.apiClients;

import android.content.Context;
import android.support.annotation.NonNull;

import com.data4sports.chasecricket.R;
import com.data4sports.chasecricket.applicationConstants.ApplicationConstants;
import com.data4sports.chasecricket.models.MyApplication;
import com.data4sports.chasecricket.models.PlanModel;

import java.util.Map;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class PlanApiController implements ApplicationConstants {
    //context
    private Context context = null;
    //header
    private Map<String, String> header;

    private PlanModel.PlanCallback planCallback = null;

    public PlanApiController() {
    }

    public PlanApiController setContext(Context context) {
        this.context = context;
        return this;
    }

    public PlanApiController setHeader(Map<String, String> header) {
        this.header = header;
        return this;
    }

    public PlanApiController setProfileCallback(PlanModel.PlanCallback planCallback) {
        this.planCallback = planCallback;
        return this;
    }

    public void getPlanDetailsCall() {

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
                            Call<PlanModel.Response> getUser = retrofitService.getPlanDetails(header);
                            getUser.enqueue(new Callback<PlanModel.Response>() {
                                @Override
                                public void onResponse(@NonNull Call<PlanModel.Response> call,
                                                       @NonNull Response<PlanModel.Response> response) {

                                    //call success checking
                                    if (response.code() == 200) {
                                        try {
                                            //response data
                                            // DashboardModel.Response resultBean = response.body();
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
                                public void onFailure(@NonNull Call<PlanModel.Response> call,
                                                      @NonNull Throwable t) {

                                }
                            });

                        }
                    }

