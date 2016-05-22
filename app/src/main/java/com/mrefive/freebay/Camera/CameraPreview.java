package com.mrefive.freebay.Camera;

import android.content.Context;
import android.hardware.Camera;
import android.util.Log;
import android.view.SurfaceHolder;
import android.view.SurfaceView;

/**
 * Created by mrefive on 1/31/16.
 */
public class CameraPreview extends SurfaceView implements SurfaceHolder.Callback{

    private SurfaceHolder surfaceHolder;
    private Camera camera;

    public CameraPreview(Context context, Camera camera) {
        super(context);

        this.camera = camera;

        //install a surfaceholder.callback, so we get notified when the underlying surface is created and destroyd
        surfaceHolder = getHolder();
        surfaceHolder.addCallback(this);

        //deprecated setting needed?


    }

    @Override
    public void surfaceCreated(SurfaceHolder holder) {
        //tell the camera now to draw the preview

        try{
            camera.setPreviewDisplay(holder);
            camera.startPreview();
        } catch (Exception e) {
            Log.d("Camera", "Error setting camera preview: " + e.getMessage());
        }

    }

    @Override
    public void surfaceChanged(SurfaceHolder holder, int format, int width, int height) {
        camera.stopPreview();
        camera.setDisplayOrientation(90);
        camera.startPreview();

    }

    @Override
    public void surfaceDestroyed(SurfaceHolder holder) {

    }
}
