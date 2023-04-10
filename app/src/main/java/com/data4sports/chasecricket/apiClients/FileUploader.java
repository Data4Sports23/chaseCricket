package com.data4sports.chasecricket.apiClients;

import android.content.Context;
import android.util.Log;


import com.data4sports.chasecricket.R;
import com.data4sports.chasecricket.applicationConstants.ApplicationConstants;
import com.data4sports.chasecricket.applicationConstants.TimeSupporter;

import java.io.File;
import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;

import okhttp3.MultipartBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class FileUploader implements ApplicationConstants {
    //reference
    private Context context;
    //call back
    private ServiceCallback serviceCallback;

    public interface ServiceCallback {
        void onSuccess(String url);

        void onFail(String message);
    }

    public interface ServiceProgressUpdate {
        void onProgressUpdate(int percentage);
    }

    public FileUploader(Context context) {
        this.context = context;
    }

    public FileUploader setServiceCallback(ServiceCallback serviceCallback) {
        this.serviceCallback = serviceCallback;
        return this;
    }

    /**
     * file upload main method
     */
    public void uploadFile(String filePath, String type, ServiceProgressUpdate serviceProgressUpdate) {
        //call back object null checking
        if (serviceCallback == null) {
            return;
        }
        //context reference null checking
        if (context == null) {
            //fail
            serviceCallback.onFail("Null Reference ");
            return;
        }

        //file path
        File file = null;
        if (filePath != null) {
            file = new File(filePath);
        }
        //file empty check
        if (file == null) {
            //file not found
            serviceCallback.onFail(context.getString(R.string.went_wrong));
            return;
        }
//        //object
//        ServiceFinderApplication serviceFinderApplication = ServiceFinderApplication.get(context);
//        RetrofitService retrofitService = serviceFinderApplication.getRetrofitService();
//
//        ProgressRequestBody fileBody = new ProgressRequestBody(file, getMediaType(type), serviceProgressUpdate);
//        MultipartBody.Part filePart = MultipartBody.Part.createFormData("img", getUniqueFileName(type), fileBody);
//
//        //checking which method
//        //request call
//        Call<ImageUploadResponse> uploadFile = retrofitService.fileUpload(filePart);
//
//        //call back
//        uploadFile.enqueue(new Callback<ImageUploadResponse>() {
//            @Override
//            public void onResponse(@NonNull Call<ImageUploadResponse> call,
//                                   @NonNull Response<ImageUploadResponse> response) {
//
//                if (response.code() == 200 && response.body() != null) {
//                    try {
//                        serviceCallback.onSuccess(response.body().getImageUrl());
//                    } catch (Exception e) {
//                        //fail
//                        serviceCallback.onFail(context.getString(R.string.went_wrong));
//                    }
//                } else {
//                    //fail
//                    serviceCallback.onFail(context.getString(R.string.went_wrong));
//                }
//            }
//
//            @Override
//            public void onFailure(@NonNull Call<ImageUploadResponse> call, @NonNull Throwable t) {
//                Log.e("Splash Exception", t.toString());
//                //fail
//                serviceCallback.onFail(context.getString(R.string.went_wrong));
//            }
//        });
    }

    private String getFileName(String name) {
        try {
            return URLEncoder.encode(name, "utf-8");
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
            return null;
        }
    }

    /**
     * unique file name
     *
     * @param type upload type
     * @return string
     */
    public String getUniqueFileName(String type) {
        return type + TimeSupporter.getUnitTime() + (type.equals(IMAGE) ? ".jpg" : ".pdf");
    }

    /**
     * get file type
     *
     * @param type type
     * @return 1 = image
     * 2 = video
     * 3 = doc
     */
    private int getMediaType(String type) {
        return type.equals(IMAGE) ? 1 : 3;
    }
}
