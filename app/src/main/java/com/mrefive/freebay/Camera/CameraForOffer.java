package com.mrefive.freebay.Camera;

import android.app.ActionBar;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Matrix;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.hardware.Camera;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.app.Activity;
import android.os.Environment;
import android.provider.MediaStore;
import android.support.annotation.Nullable;
import android.util.AttributeSet;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.FrameLayout;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.Toast;

import com.mrefive.freebay.MainActivity;
import com.mrefive.freebay.R;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;

public class CameraForOffer extends Activity {

    private static final float PICTURE_WIDTH = 640;
    private static final float PICTURE_HEIGHT = 480;



    private static final float PICTURE_PROPORTION = PICTURE_WIDTH/PICTURE_HEIGHT;

    //0 = not taken ; 1= image taken
    private int photoState;
    private String currentFilePath;

    private Camera camera;
    private CameraPreview cameraPreview;
    private Context context;

    private Uri picture_uri;
    private Drawable picture_drawable;

    //layouts
    private FrameLayout preview;
    private LinearLayout cameraInterface;

    private Button buttonCapture;
    private Button buttonFlash;
    private Button buttonCancel;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_camera_for_offer);

        context = this;
        photoState = 0;

        preview = (FrameLayout) findViewById(R.id.camera_preview);
        cameraInterface = (LinearLayout) findViewById(R.id.camera_interface);

        buttonCapture = (Button) findViewById(R.id.button_caputre);
        buttonFlash = (Button) findViewById(R.id.button_flash);
        buttonCancel = (Button) findViewById(R.id.button_cancel);


        checkCameraHardware(this);

        //get a camera
        camera = getCameraInstance();

        //set Params
        final Camera.Parameters parameters = camera.getParameters();

        parameters.setFocusMode(Camera.Parameters.FOCUS_MODE_CONTINUOUS_PICTURE);
        Camera.Size biggestPossibleSize = getBestCameraSize((int) PICTURE_WIDTH, (int) PICTURE_HEIGHT, parameters);
        parameters.setPictureSize(biggestPossibleSize.width, biggestPossibleSize.height);

        Camera.Size bestPreviewSize = getBestPreviewSize((int) PICTURE_WIDTH, (int) PICTURE_HEIGHT, parameters);
        parameters.setPreviewSize(bestPreviewSize.width, bestPreviewSize.height);

        Log.d("CameraForOffer", "actual preview size: " + parameters.getPreviewSize().width + " " + parameters.getPreviewSize().height);

        camera.setParameters(parameters);

        //create preview
        cameraPreview = new CameraPreview(this, camera);
        preview.addView(cameraPreview);

        buttonCapture.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(photoState==0) {
                    camera.takePicture(null, null, pictureCallback);
                    Toast.makeText(context, "Picture Taaaken woohooo", Toast.LENGTH_SHORT).show();
                }
                if(photoState==1) {
                    Intent returnIntent = new Intent();
                    returnIntent.putExtra("picture_uri", picture_uri.toString());
                    setResult(Activity.RESULT_OK, returnIntent);


                    finish();
                    resetCam();
                }
                photoState=1;
            }
        });

        preview.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

            }
        });

        buttonFlash.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(photoState==0){
                    Camera.Parameters parameters = camera.getParameters();

                    Log.d("CameraForOffer", "Flash button pressed");

                    if (!parameters.getFlashMode().equals(Camera.Parameters.FLASH_MODE_TORCH)) {
                        parameters.setFlashMode(Camera.Parameters.FLASH_MODE_TORCH);
                        Log.d("CameraForOffer", "Camera.Parameters.FLASH_MODE_TORCH");
                    } else {
                        parameters.setFlashMode(Camera.Parameters.FLASH_MODE_OFF);
                        Log.d("CameraForOffer", "Camera.Parameters.FLASH_MODE_OFF");
                    }
                    camera.setParameters(parameters);
                }

                if(photoState==1){
                    Toast.makeText(context, R.string.noFlashAvailable, Toast.LENGTH_LONG).show();
                }

            }
        });

        buttonCancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                if(photoState==0) {
                    Intent returnIntent = new Intent();
                    setResult(Activity.RESULT_CANCELED, returnIntent);
                    finish();
                }

                if(photoState==1) {
                    preview.setBackground(null);
                    preview.addView(cameraPreview);
                    photoState=0;
                }
            }
        });



    }

    @Override
    protected void onResume() {
        super.onResume();
    }

    @Override
    public void onWindowFocusChanged(boolean hasFocus) {
        super.onWindowFocusChanged(hasFocus);
        Log.d("CameraForOffer", "-----------------------------------" + preview.getWidth() + preview.getMeasuredWidth() + PICTURE_PROPORTION);
        preview.setLayoutParams(new RelativeLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, (int) (preview.getMeasuredWidth() * PICTURE_PROPORTION)));

        //RelativeLayout.LayoutParams paramsCI = new RelativeLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT,(int) (preview.getHeight() / 4) );

        RelativeLayout.LayoutParams paramsCI = (RelativeLayout.LayoutParams) cameraInterface.getLayoutParams();

        paramsCI.height = preview.getHeight()/4;

        cameraInterface.setLayoutParams(paramsCI);
    }

    @Override
    protected void onPause() {
        super.onPause();
        releaseCamera();
    }

    public static Camera getCameraInstance() {
        Camera c = null;
        try {
            c = Camera.open();
        } catch (Exception e) {
            //no cam available
        }

        return c;
    }

    private void releaseCamera() {
        if(camera!=null) {
            camera.stopPreview();
            cameraPreview=null;
            camera.release();
            camera=null;
        }
    }

    //check if the device has a camera
    private boolean checkCameraHardware(Context context) {
        if (context.getPackageManager().hasSystemFeature(PackageManager.FEATURE_CAMERA)) {
            return true;
        } else {
            //no camera available
            Toast.makeText(this, R.string.noCamerafound, Toast.LENGTH_LONG).show();

            finish();
            return false;
        }
    }

    private void resetCam() {
        camera.startPreview();
        cameraPreview = new CameraPreview(this, camera);
    }

    /*
    Camera.PictureCallback jpegCallback = new Camera.PictureCallback() {
        @Override
        public void onPictureTaken(byte[] data, Camera camera) {
            new SaveImageTask().execute(data);
            resetCam();
        }
    };
    */

    Camera.PictureCallback pictureCallback = new Camera.PictureCallback() {
        @Override
        public void onPictureTaken(byte[] data, Camera camera) {

            File pictureFile = getOutputMediaFile();
            if(pictureFile==null) {
                Log.d("CameraForOffer", "Taken picture: Picture File = null...");
                return;
            }
            try {
                FileOutputStream fos = new FileOutputStream(pictureFile);
                fos.write(data);
                fos.close();
                Log.d("CameraForOffer", "Taken picture: File written to memory....");


                //crop / rotate image and save it
                Bitmap bitmap = BitmapFactory.decodeFile(pictureFile.getAbsolutePath());
                Log.d("CameraForOffer", "Path to the picture: " + pictureFile.getAbsolutePath());
                Bitmap bitmapCropped = Bitmap.createBitmap(bitmap, 0, 0, 480, 480);

                Matrix matrix = new Matrix();
                matrix.postRotate(90);
                Bitmap rotatedBitmap = Bitmap.createBitmap(bitmapCropped, 0, 0, bitmapCropped.getWidth(), bitmapCropped.getHeight(), matrix, true);

                //make drawable for activityresult
                picture_drawable = new BitmapDrawable(getResources(), rotatedBitmap);


                try {
                    //FileOutputStream out = new FileOutputStream(getOutputMediaFile());
                    FileOutputStream out = new FileOutputStream(pictureFile.getAbsolutePath());
                    //try better quality
                    rotatedBitmap.compress(Bitmap.CompressFormat.JPEG, 100, out);
                    out.flush();
                    out.close();
                } catch (Exception e) {
                    Log.d("ComeraForOffer", "File was not overwritten by cropped rotated jpeg....");
                    e.printStackTrace();
                }


                preview.removeAllViews();

                picture_uri = Uri.fromFile(pictureFile);

                //Drawable d = Drawable.createFromPath(pictureFile.getAbsolutePath());
                Drawable d = new BitmapDrawable(getResources(), rotatedBitmap);
                preview.setBackground(d);

            }catch (FileNotFoundException e) {

            }catch (IOException e) {

            }


            //new SaveImageTask().execute(data);
        }
    };

    private static File getOutputMediaFile(){
        File mediaStorageDir = new File(Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_PICTURES),"FreeBay");
        if (!mediaStorageDir.exists()) {
            if(!mediaStorageDir.mkdirs()) {
                Log.d("CameraForOffer", "Taken picture: Failed to create dir...");
                return null;
            }
        }
        //create a media file name
        String fileName = "TMP_" + MainActivity.ANDROID_ID;

        File mediaFile;
        mediaFile= new File(mediaStorageDir.getPath() + File.separator + fileName);

        return mediaFile;
    }

    private class SaveImageTask extends AsyncTask<byte[], Void, Void> {
        @Override
        protected Void doInBackground(byte[]... data) {
            FileOutputStream outputStream = null;

            try{
                Log.d("CameraForOffer", "Trying to save image in Environment.....");
photoState=0;
                File sdCard = Environment.getExternalStorageDirectory();
                File dir = new File(sdCard.getAbsolutePath() + "/FreeBayCamtest");
                dir.mkdirs();

                String fileName = String.format("%d.jpg", System.currentTimeMillis());
                File outFile = new File(dir, fileName);

                outputStream = new FileOutputStream(outFile);
                outputStream.write(data[0]);
                outputStream.flush();
                outputStream.close();

            } catch (FileNotFoundException e) {
                Log.d("CameraForOffer", "Image saving failed!!!!!!!.....");

            } catch (IOException e) {
                Log.d("CameraForOffer", "Image saving failed!!!!!!!.....");

            }

            return  null;
        }
    }

    //get biggest camerasize nearest to quadr
    private Camera.Size getBestCameraSize(int width, int height, Camera.Parameters parameters) {
        Camera.Size result = null;

        Log.d("CameraForOffer", "width: " +  width);
        Log.d("CameraForOffer", "height: "+ height);

        for (Camera.Size size : parameters.getSupportedPictureSizes()) {
            if(size.width<=width && size.height<=height) {
                if(result==null) {
                    result = size;
                } else {
                    int resultArea = result.width*result.height;
                    int newArea = size.width*size.height;

                    if(newArea>resultArea) {
                        result=size;
                    }
                }
            }
        }

        Log.d("CameraForOffer", "result.width: " + result.width + " result.height: "+result.height);
        return result;
    }

    //get biggest camerasize nearest to quadr
    private Camera.Size getBestPreviewSize(int width, int height, Camera.Parameters parameters) {
        Camera.Size result = null;

        Log.d("CameraForOffer", "width: " +  width);
        Log.d("CameraForOffer", "height: "+ height);

        for (Camera.Size size : parameters.getSupportedPreviewSizes()) {
            if(size.width<=width && size.height<=height) {
                if(result==null) {
                    result = size;
                } else {
                    int resultArea = result.width*result.height;
                    int newArea = size.width*size.height;

                    if(newArea>resultArea) {
                        result=size;
                    }
                }
            }
        }

        Log.d("CameraForOffer", "Preview : result.width: " + result.width + " result.height: "+result.height);
        return result;
    }

}
