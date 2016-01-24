package com.mrefive.freebay;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.DatePickerDialog;
import android.app.Dialog;
import android.app.TimePickerDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Bitmap;
import android.graphics.Point;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.Display;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.view.inputmethod.InputMethodManager;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.Spinner;
import android.widget.TextClock;
import android.widget.TextView;
import android.widget.TimePicker;
import android.widget.Toast;

import java.io.FileOutputStream;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.Locale;


public class OfferFragment extends Fragment {

    //instances
    private ProfileFragment.OnFragmentInteractionListener mListener;
    private DbInterface dbInterface;
    SharedPreferences prefs;
    private Calendar calendar;
    private GPSTracker gpsTracker;

    //variables
    int containerWidth, containerHeight;
    private int countOfPosts;

    private String name, description, datecontrol;

    //instances


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


    /*
    public static OfferFragment2 newInstance(String param1, String param2) {
        OfferFragment2 fragment = new OfferFragment2();
        Bundle args = new Bundle();
        fragment.setArguments(args);
        return fragment;
    }*/

    public OfferFragment() {
        // Required empty public constructor
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        prefs = getContext().getSharedPreferences("com.mrefive.freebay", Context.MODE_PRIVATE);
        dbInterface = new DbInterface(getContext());


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
        time = (EditText) view.findViewById(R.id.eTTime);
        date = (EditText) view.findViewById(R.id.eTDate);
        submitButton = (Button) view.findViewById(R.id.submitOffer);

        showDate = (TextView) view.findViewById(R.id.showDateDue);

        //hide keyboard
        InputMethodManager imm = (InputMethodManager)getActivity().getSystemService(Context.INPUT_METHOD_SERVICE);
        imm.hideSoftInputFromWindow(view.getWindowToken(), 0);


        //define spinner items
        String[] items = new String[]{"Chose a category", "Food", "Toys", "Cloths", "Essentials", "Other"};
        ArrayAdapter<String> adapter = new ArrayAdapter<String>(getContext(), R.layout.categories_dropdown, items);
        categoriesSpinner.setAdapter(adapter);

        time.setHint("Set Ending Time");
        date.setHint("Set Ending Date");
        title.setHint("Enter a title here");
        descr.setHint("Describe your offer here");

        time.setText(String.format("%02d:%02d", 12, 34));
        //showDate.setText(String.format("%02d.%02d.%04d", calendar.get(Calendar.DAY_OF_YEAR), calendar.get(Calendar.MONTH)+1, calendar.get(Calendar.YEAR)));
        showDate.setHint("When does your offer end?");
        time.setVisibility(View.INVISIBLE);
        date.setVisibility(View.INVISIBLE);

        //On CLICK LISTERNERS-----------------------------------------------------------------------------------------------------------

        takePictureButton.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                //open camera int here
                Intent cameraIntent = new Intent(android.provider.MediaStore.ACTION_IMAGE_CAPTURE);
                startActivityForResult(cameraIntent, 2);
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

        /* 000000  time and date picker !

        date.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                int mYear = calendar.get(Calendar.YEAR);
                int mMonth = calendar.get(Calendar.MONTH);
                int mDay = calendar.get(Calendar.DAY_OF_MONTH);
                System.out.println("the selected " + mDay);
                DatePickerDialog dialog = new DatePickerDialog(getContext(),
                        new mDateSetListener(), mYear, mMonth, mDay);
                dialog.setTitle("Select Date");
                dialog.show();
            }
        });

        time.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                int hour;
                if(calendar.get(Calendar.HOUR_OF_DAY)!=23) {
                    hour = calendar.get(Calendar.HOUR_OF_DAY) + 1;
                } else {
                    hour = 1;
                }
                int minute = calendar.get(Calendar.MINUTE);
                TimePickerDialog mTimePicker;
                mTimePicker = new TimePickerDialog(getContext(), new TimePickerDialog.OnTimeSetListener() {
                    @Override
                    public void onTimeSet(TimePicker timePicker, int selectedHour, int selectedMinute) {
                        String output = String.format("%02d:%02d", selectedHour, selectedMinute);
                        time.setText(output);
                    }
                }, hour, 0, true);//Yes 24 hour time
                mTimePicker.setTitle("Select Time");
                mTimePicker.show();
            }
        });

        */


        submitButton.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                boolean submitIt = true;

                //testpurpose
                Log.d("OfferFragment",Integer.toString(categoriesSpinner.getSelectedItemPosition()) );

                //check if data is complete
                if((categoriesSpinner.getSelectedItem().toString().equals("Chose category")) && submitIt) {
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


                boolean datecorrect=false;

                if((!(showDate.getText().toString().equals(""))) && submitIt) {
                    String dtStart = showDate.getText().toString();
                    SimpleDateFormat formatdate = new SimpleDateFormat("yyyy/MM/dd");
                    try {
                        Date dateput = formatdate.parse(datecontrol);
                        Date datenow = formatdate.parse(String.valueOf(new StringBuilder().append(calendar.get(Calendar.MONTH) + 1).append("/").append(calendar.get(Calendar.DAY_OF_YEAR)).append("/")
                                .append(calendar.get(Calendar.YEAR)).append(" ")));
                        if (dateput.compareTo(datenow) > -1) {
                            datecorrect = true;
                        } else {
                            Toast.makeText(getContext(), "Enter a valid date please", Toast.LENGTH_LONG).show();
                        }
                    } catch (ParseException e) {
                        // TODO Auto-generated catch block
                        e.printStackTrace();
                    }
                } else if(submitIt) {
                    Toast.makeText(getContext(), "Enter a valid date please", Toast.LENGTH_LONG).show();
                }

                if(!(datecorrect)){
                    submitIt=false;
                }

                if(submitIt) {

                    //prepare missing data for output
                    gpsTracker = new GPSTracker(getContext());

                    //calendar = null;
                    //calendar.getInstance();
                    String timeputin = String.format("%02d:%02d", calendar.get(Calendar.HOUR_OF_DAY), calendar.get(Calendar.MINUTE));
                    String dateputin = String.format("%02d.%02d.%04d", calendar.get(Calendar.DAY_OF_YEAR), calendar.get(Calendar.MONTH)+1, calendar.get(Calendar.YEAR));



                    Log.d("OfferFragment", "Offer submit hanging...");
                    if (countOfPosts < 555) {
                        //post Info
                        String method = "post";
                        DbInterface dbInterfacenew = new DbInterface(getContext());
                        dbInterfacenew.execute(method, MainActivity.ANDROID_ID.toString(),Integer.toString(categoriesSpinner.getSelectedItemPosition()), title.getText().toString(),
                                descr.getText().toString(), timeputin, dateputin, showDate.getText().toString(),
                                Double.toString(gpsTracker.getLatitude()),Double.toString(gpsTracker.getLongitude()));
                        //dbInterface.execute(method, name, description);
                        //finish();

                        //do extra
                        countOfPosts++;
                        prefs.edit().putInt("countOfPosts", countOfPosts).apply();
                    } else {
                        Toast.makeText(getContext(), "You have too many Offers", Toast.LENGTH_LONG).show();
                    }
                }
            }
        });

        view.requestFocus();

        return view;
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
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
                photo.compress(Bitmap.CompressFormat.JPEG, 90, out);
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

}

