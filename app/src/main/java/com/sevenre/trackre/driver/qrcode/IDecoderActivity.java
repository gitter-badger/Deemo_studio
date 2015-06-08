package com.sevenre.trackre.driver.qrcode;

import android.graphics.Bitmap;
import android.os.Handler;

import com.sevenre.trackre.driver.qrcode.camera.CameraManager;
import com.google.zxing.Result;

public interface IDecoderActivity {

    public ViewfinderView getViewfinder();

    public Handler getHandler();

    public CameraManager getCameraManager();

    public void handleDecode(Result rawResult, Bitmap barcode);
}
