package com.data4sports.chasecricket.apiClients;

import com.data4sports.chasecricket.BuildConfig;
import com.data4sports.chasecricket.applicationConstants.ApiConstants;
import com.data4sports.chasecricket.models.MetricData;
import com.data4sports.chasecricket.models.PlanModel;
import com.data4sports.chasecricket.models.ProfileDetailsModel;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

import java.util.List;
import java.util.Map;
import java.util.concurrent.TimeUnit;

import okhttp3.OkHttpClient;
import okhttp3.logging.HttpLoggingInterceptor;
import retrofit2.Call;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;
import retrofit2.http.GET;
import retrofit2.http.HeaderMap;
import retrofit2.http.Path;

public interface RetrofitService {

    class Factory implements ApiConstants {
        /**
         * t
         * retrofit set uo for our server
         *
         * @return RetrofitService
         */
        public static RetrofitService create() {
            Retrofit retrofit = new Retrofit.Builder()
                    .baseUrl(BuildConfig.BASE_URL)
                    .addConverterFactory(GsonConverterFactory.create(getGson()))
                    .client(getClient())
                    .build();
            return retrofit.create(RetrofitService.class);
        }

        /**
         * retro client set up
         *
         * @return OkHttpClient
         */
        private static OkHttpClient getClient() {
            OkHttpClient.Builder b = new OkHttpClient.Builder();
            b.readTimeout(TIMEOUT_VALUE, TimeUnit.MILLISECONDS);
            b.writeTimeout(TIMEOUT_VALUE, TimeUnit.MILLISECONDS);

            HttpLoggingInterceptor interceptor = new HttpLoggingInterceptor();
            interceptor.setLevel(HttpLoggingInterceptor.Level.BODY);
            if (BuildConfig.IS_LOG_ENABLED) {
                b.addInterceptor(new HttpLoggingInterceptor().setLevel(HttpLoggingInterceptor.Level.BODY));
            }
            return b.build();
        }

        /**
         * gson
         *
         * @return Gson
         */
        private static Gson getGson() {
            return new GsonBuilder()
                    .setLenient()
                    .create();
        }
    }

    @GET("plan")
    Call<PlanModel.Response> getPlanDetails(@HeaderMap Map<String, String> header);

    @GET("getprofile")
    Call<ProfileDetailsModel.Response> getProfileDetails(@HeaderMap Map<String, String> header);

    @GET("getuserdetails/{userid}")
    Call<ProfileDetailsModel.Response> getUserDetails(@HeaderMap Map<String, String> header,
                                                         @Path("userid") int userid);

//    @Headers({"Content-type: application/json; charset=UTF-8"})
//    @POST("customer/update_profile")
//    Call<SuccessResponse> updateProfile(@HeaderMap Map<String, String> header,
//                                        @Body ProfileUpdationModel.PostBody postData);

    @GET("stop")
    Call<List<MetricData>> MetricesContactTag(@HeaderMap Map<String, String> header);


}