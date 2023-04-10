package com.data4sports.chasecricket.models;

import android.graphics.Bitmap;

/*
 * Created by akhil on 03/07/18.
 */
public interface ImageLoadCallback {
    void onLoadFailed(boolean result);

    void  onLoadSuccess(Bitmap bitmapImage);
}
