package com.mrefive.freebay;

import android.app.Activity;
import android.app.DatePickerDialog;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Bitmap;
import android.graphics.Color;
import android.graphics.Point;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.provider.MediaStore;
import android.support.v4.app.Fragment;
import android.util.Base64;
import android.util.Log;
import android.view.Display;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.view.inputmethod.InputMethodManager;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.RelativeLayout;
import android.widget.Spinner;
import android.widget.TextClock;
import android.widget.TextView;
import android.widget.TimePicker;
import android.widget.Toast;

import com.mrefive.freebay.Camera.CameraForOffer;
import com.mrefive.freebay.OwnOffersDB.OwnOffersDatabase;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.net.URLEncoder;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.Locale;
import java.util.StringTokenizer;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.TimeoutException;


public class OfferFragment extends Fragment {

    //instances
    private ProfileFragment.OnFragmentInteractionListener mListener;
    SharedPreferences prefs;
    private Calendar calendar;

    //variables
    int containerWidth, containerHeight;
    private int countOfPosts;

    private boolean pictureTaken = false;

    private Uri uri;
    private String UOID, datecontrol, dateputin, lat, lng, imageName;

    //instances
    private Drawable picture_taken;

    //views
    private ImageButton imagebuttonCamera;
    private RelativeLayout relativeLayoutOfferFrag, offerimagecontainer, takePictureButton;
    private EditText title, descr, time, date;
    private Button submitButton;
    private Spinner categoriesSpinner;
    private TextClock textClock;
    private TimePicker tp;
    private DatePicker dp;
    private TextView showDate;


    private DatePickerDialog dpd;


    public OfferFragment() {
        // Required empty public constructor
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        prefs = getContext().getSharedPreferences("com.mrefive.freebay", Context.MODE_PRIVATE);

        countOfPosts= prefs.getInt("countOfPosts",0);

        //getWindowSize
        Display display = getActivity().getWindowManager().getDefaultDisplay();
        Point size = new Point();
        display.getRealSize(size);
        containerWidth = size.x;
        containerHeight = size.y;

        //getCalendar
        calendar = Calendar.getInstance();

        System.out.println("-------------------------OfferFragment2 container size hxw: " + containerHeight + "x" + containerWidth);

    }

    @Override
    public View onCreateView(LayoutInflater inflater, final ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_offer, container, false);

        //create cameraimageview
        relativeLayoutOfferFrag = (RelativeLayout) view.findViewById(R.id.LinLayOfferFragment);
        offerimagecontainer = (RelativeLayout) view.findViewById(R.id.offerimagecontainer);

        imagebuttonCamera = (ImageButton) view.findViewById(R.id.buttonGetPicture);
        takePictureButton = (RelativeLayout) view.findViewById(R.id.offerimagecontainer);
        categoriesSpinner = (Spinner) view.findViewById(R.id.categoriesSpinner);
        title = (EditText) view.findViewById(R.id.offerTitle);
        descr = (EditText) view.findViewById(R.id.offerDescription);
        submitButton = (Button) view.findViewById(R.id.submitOffer);

        showDate = (TextView) view.findViewById(R.id.showDateDue);

        //hide keyboard
        InputMethodManager imm = (InputMethodManager)getActivity().getSystemService(Context.INPUT_METHOD_SERVICE);
        imm.hideSoftInputFromWindow(view.getWindowToken(), 0);

        //define spinner items
        String[] items = new String[]{"Chose a category", "Food", "Toys", "Cloths", "Essentials", "Other"};
        ArrayAdapter<String> adapter = new ArrayAdapter<String>(getContext(), R.layout.categories_dropdown, items);
        categoriesSpinner.setAdapter(adapter);

        title.setHint("Enter a title here");
        descr.setHint("Describe your offer here");
        showDate.setHint("When does your offer end?");

        //On CLICK LISTERNERS-----------------------------------------------------------------------------------------------------------
        takePictureButton.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {

                WindowManager.LayoutParams layoutParams = getActivity().getWindow().getAttributes();
                layoutParams.dimAmount=1.0f;
                getActivity().getWindow().setAttributes(layoutParams);

                takePictureButton.setBackgroundColor(Color.parseColor("#5460E3"));

                Intent camera = new Intent(getActivity(), CameraForOffer.class);
                startActivityForResult(camera, 1);

            }
        });

        showDate.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                dpd = new DatePickerDialog(getContext(), new DatePickerDialog.OnDateSetListener() {
                    @Override
                    public void onDateSet(DatePicker view, int year, int monthOfYear, int dayOfMonth) {
                        showDate.setText(String.format("%02d.%02d.%04d",dayOfMonth , monthOfYear+1, year));
                        datecontrol = String.format("%04d/%02d/%02d", year, monthOfYear+1, dayOfMonth);
                    }
                },
                calendar.get(Calendar.YEAR), calendar.get(Calendar.MONTH), calendar.get(Calendar.DAY_OF_MONTH));
                dpd.setTitle("Select Date");
                dpd.show();
            }
        });

        submitButton.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {

                if(isNetworkAvailable()) {

                    if (isContentTrue()) {
                        prepareMissingContent();
                        startUpload();
                    }

                } else {
                    LayoutInflater inflater = getActivity().getLayoutInflater();

                    View layout = inflater.inflate(R.layout.toastnoconnection,
                            (ViewGroup) getActivity().findViewById(R.id.toastnoconnection_id));

                    //set a message
                    TextView text = (TextView) layout.findViewById(R.id.textnoconection);
                    text.setText(getResources().getString(R.string.submitnoconnection));

                    // Toast...
                    Toast toast = new Toast(getContext());
                    toast.setGravity(Gravity.FILL,0,0);
                    toast.setDuration(Toast.LENGTH_LONG);
                    toast.setView(layout);
                    toast.show();
                }
            }
        });

        view.requestFocus();
        return view;
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {

        if (requestCode == 1) {

            if(resultCode == Activity.RESULT_OK) {

                Bitmap bitmap=null;

                //get image from uri
                String uriString = data.getExtras().getString("picture_uri");
                uri = Uri.parse(uriString);
                try {
                    bitmap = MediaStore.Images.Media.getBitmap(getActivity().getContentResolver(), uri);
                } catch (IOException e) {
                    Log.d("Offer Fragment", "Failed to retrieve image from Uri ffs");
                    e.printStackTrace();
                }

                picture_taken = new BitmapDrawable(getResources(), bitmap);

                //set background in takePicturebuttonm

                RelativeLayout.LayoutParams layoutParamsTPButton = (RelativeLayout.LayoutParams) takePictureButton.getLayoutParams();
                layoutParamsTPButton.height= takePictureButton.getWidth();
                takePictureButton.setLayoutParams(layoutParamsTPButton);
                Log.d("OfferFragment", "picture container layout widht / height: " + takePictureButton.getWidth() + " / " + takePictureButton.getHeight());


                takePictureButton.setBackground(picture_taken);
                pictureTaken=true;
            }

            if(resultCode == Activity.RESULT_CANCELED) {
                takePictureButton.setBackgroundColor(Color.parseColor("#ffffffff"));
            }
        }

        if (requestCode == 2) {
            Bitmap photo = (Bitmap) data.getExtras().get("data");

            //test--------------------------------------------------------------------------------------------------------------------------------------------0
            Drawable d = new BitmapDrawable(Bitmap.createScaledBitmap(photo, offerimagecontainer.getWidth(), offerimagecontainer.getHeight(), false));
            offerimagecontainer.setBackground(d);

            imagebuttonCamera.setBackground(null);
            try{
                SimpleDateFormat sdf = new SimpleDateFormat("yyyyMMdd_HHmmss");
                String currentDateandTime = sdf.format(new Date()).replace(" ","");
                FileOutputStream out = new FileOutputStream(Environment.getExternalStorageDirectory().getAbsolutePath() + "/bao/bao"+currentDateandTime+".jpg");
                photo.compress(Bitmap.CompressFormat.JPEG, 100, out);
            }catch (Exception e){
                e.printStackTrace();
            }
        }
    }


    @Override
    public void onStart() {
        super.onStart();
    }

    @Override
    public void onResume() {
        super.onResume();
    }

    @Override
    public void onDetach() {
        super.onDetach();
        mListener = null;
    }

    public String getTitle() {
        return "Create an offer";
    }


    private boolean isNetworkAvailable() {
        //check for internet connection
        return new CheckInternetConnection(getContext()).isNetworkConnected();
    }

    private boolean isContentTrue() {

        boolean submitIt = true;

        if(countOfPosts>MainActivity.MAXOWNOFFERS) {
            Toast.makeText(getContext(),"You have too many offers. Please delete one or wait it to expire", Toast.LENGTH_LONG).show();
            submitIt=false;
        }
        //check if data is complete
        if(!pictureTaken && submitIt) {
            Toast.makeText(getContext(),"Please take a picture of your offer", Toast.LENGTH_LONG).show();
            submitIt=false;
        }
        if((categoriesSpinner.getSelectedItem().toString().equals("Chose a category")) && submitIt) {
            Toast.makeText(getContext(),"Please chose a category", Toast.LENGTH_LONG).show();
            submitIt=false;
        }
        if(title.getText().toString().equals("") && submitIt) {
            Toast.makeText(getContext(),"Please give your offer a title", Toast.LENGTH_LONG).show();
            submitIt=false;
        }
        if(descr.getText().toString().equals("") && submitIt) {
            Toast.makeText(getContext(),"Please describe your offer", Toast.LENGTH_LONG).show();
            submitIt=false;
        }

        if(datecontrol==null && submitIt) {
            submitIt=false;
            Toast.makeText(getContext(), "Enter an upcoming date please", Toast.LENGTH_LONG).show();
        }

        if(submitIt) {
            SimpleDateFormat formatdate = new SimpleDateFormat("yyyy/MM/dd");
            try {
                Date dateput = formatdate.parse(datecontrol);
                Date datenow = formatdate.parse(String.valueOf(new StringBuilder().append(calendar.get(Calendar.MONTH) + 1).append("/").append(calendar.get(Calendar.DAY_OF_YEAR)).append("/")
                        .append(calendar.get(Calendar.YEAR)).append(" ")));
                if (!(dateput.compareTo(datenow) > -1)) {
                    Toast.makeText(getContext(), "Enter an upcoming date please", Toast.LENGTH_LONG).show();
                    submitIt = false;
                }
            } catch (ParseException e) {
                // TODO Auto-generated catch block
                e.printStackTrace();
                Log.d("OfferFragment", "DATECHECKINGERROR");
                submitIt = false;
            }
        }

        if(submitIt) {
            return true;
        }

        return false;
    }

    private void prepareMissingContent(){

        GPSTracker gpsTracker = new GPSTracker(getContext());
        lat = Double.toString(gpsTracker.getLatitude());
        lng = Double.toString(gpsTracker.getLongitude());

        //time and date of creating the offer
        long millis = System.currentTimeMillis();
        SimpleDateFormat sdf = new SimpleDateFormat("yyyyMMddHHmmssz");
        Date resultdate = new Date(millis);
        dateputin = sdf.format(resultdate);
    }

    private void renamePictureFile(){
        String fileName = "TMP_" + MainActivity.ANDROID_ID;
        File oldfile = new File(Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_PICTURES), "FreeBay" + File.separator + fileName);
        File newfile = new File(Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_PICTURES), "FreeBay" + File.separator + imageName);
        oldfile.renameTo(newfile);
    }

    private void startUpload() {

        //CreateUOID createUOID = new CreateUOID(getContext());
        try {
            CreateUOID createUOID = new CreateUOID(getActivity()) {

                @Override
                protected void onPostExecute(String s) {
                    UOID = s;
                    Log.d("OfferFragment", "UOID: " +UOID);

                    imageName = dateputin + "_" + UOID + ".jpg";

                    //upload image
                    Bitmap bm = null;
                    try {
                        bm = MediaStore.Images.Media.getBitmap(getActivity().getContentResolver(), uri);
                        ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream();
                        bm.compress(Bitmap.CompressFormat.JPEG, 100, byteArrayOutputStream);
                        byte[] byteArray = byteArrayOutputStream.toByteArray();

                        String encodedImagetmp = Base64.encodeToString(byteArray, Base64.DEFAULT);

                        // Upload image to server
                        new ImageToServer(getContext()).execute(encodedImagetmp, imageName);
                    } catch (IOException e) {
                        Log.d("OfferFragment", "submit Image failed onClick....");
                        e.printStackTrace();
                    }

                    renamePictureFile();

                    //upload data to server database
                    OwnOfferDataToServer ownOfferDataToServer = new OwnOfferDataToServer(getContext()) {

                        @Override
                        protected void onPostExecute(String result) {
                            Log.d("OfferFragment","POSTING OFFER RESULT: " +result);

                            if(result.equals("Offer Successfully Created!")){

                                OwnOffersDatabase ownOffersDatabase = new OwnOffersDatabase(getContext());
                                String[] data = new String[10];
                                data[0] = UOID;
                                data[1] = MainActivity.ANDROID_ID;
                                data[2] = categoriesSpinner.getSelectedItem().toString();
                                data[3] = title.getText().toString();
                                data[4] = descr.getText().toString();
                                data[5] = dateputin;
                                data[6] = showDate.getText().toString();
                                data[7] = lat;
                                data[8] = lng;
                                data[9] = imageName;
                                ownOffersDatabase.addEntry(ownOffersDatabase, data);

                                countOfPosts++;
                                prefs.edit().putInt("countOfPosts", countOfPosts).apply();
                                Toast.makeText(getContext(), "Offer successfully created!", Toast.LENGTH_LONG).show();
                            }
                        }
                    };

                    ownOfferDataToServer.execute(UOID, MainActivity.ANDROID_ID,title.getText().toString(),
                            descr.getText().toString(), categoriesSpinner.getSelectedItem().toString(), dateputin, showDate.getText().toString(),
                            lat,lng,imageName);
                }
            };

            createUOID.execute();
        } catch (Exception e) {
        }
    }
}

