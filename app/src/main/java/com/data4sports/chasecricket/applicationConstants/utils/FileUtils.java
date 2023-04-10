package com.data4sports.chasecricket.applicationConstants.utils;

import android.content.Context;
import android.util.Log;

import com.data4sports.chasecricket.applicationConstants.AppConstants;
import com.data4sports.chasecricket.models.Events;
import com.data4sports.chasecricket.models.Match;
import com.data4sports.chasecricket.models.Player;

import java.io.BufferedInputStream;
import java.io.File;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.URL;
import java.net.URLConnection;
import java.nio.file.Files;
import java.nio.file.Paths;

import io.realm.Realm;
import io.realm.RealmResults;
import okhttp3.MediaType;
import okhttp3.MultipartBody;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.RequestBody;
import okhttp3.Response;

public class FileUtils {



    public static void fileUtils(Context context){
        String filePath = "";
        try {
            filePath = context.getFilesDir() + "/" + AppConstants.GAME_ID + ".realm";
            Log.d("TAG", "fileUtils: File Path Done " + filePath);
            File file = new File(filePath);
            Thread thread = new Thread(new Runnable() {

                @Override
                public void run() {
                    try  {
                        //Your code goes here
                        doActualRequest(file,context);
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                }
            });

            thread.start();


//            File yourFile = new File( yourFilePath );
//            Log.d("TAG", "fileUtils: Path ", yourFilePath);
        }catch (Exception e){
            Log.d("TAG", "fileUtils: Exceptions " + e);
        }
        // return filePath;

    }

    private static void doActualRequest(File file, Context context) {


        Log.d("TAG", "doActualRequest: File Name Game id " + AppConstants.GAME_ID);
        String url = "http://chaseweb.data4sports.com/api/match/backup/"+ AppConstants.GAME_ID;
        String urlRestore = "http://chaseweb.data4sports.com/api/match/restore/{1033170}";
        OkHttpClient okHttpClient = new OkHttpClient();
        RequestBody requestBody = new MultipartBody.Builder().setType(MultipartBody.FORM)
                .addFormDataPart("backup_file",file.getName(),RequestBody.create(MediaType.parse(".realm"),file))
                .build();
        Request request = new Request.Builder().url(url).post(requestBody).build();
        try {
            Response response = okHttpClient.newCall(request).execute();
            Log.d("TAG", "doActualRequest: Response Value " + response);
           // dowmload(context);
        }catch (Exception e){
            e.printStackTrace();
        }

    }





}

