package com.mycheckins;

import android.app.DatePickerDialog;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.location.Location;
import android.os.Bundle;
import android.provider.MediaStore;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import com.mycheckins.database.DatabaseHelper;

import java.io.ByteArrayOutputStream;
import java.util.Calendar;

public class DeleteRecordActivity extends AppCompatActivity implements View.OnClickListener {
    public TextView mMapClickTV;
    public ImageView mShowImageIV;
    private double mLatitude, mLongitude;
    public TextView mLatLongTV, mUpdateTV,mDeleteTV, mDateTV, mClickImageTV;
    public DatePickerDialog datePickerDialog;
    public EditText mTitleET, mPlaceET, mDetailsET;
    public DatabaseHelper databaseHelper;
    public String title, place, details, lat_long, date,id;
    private static final int CAMERA_REQUEST = 1888;

    /**
     * Represents a geographical location.
     */
    protected Location mLastLocation;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_delete);

        mShowImageIV = (ImageView) findViewById(R.id.iv_showimage);
        mMapClickTV = (TextView) findViewById(R.id.tv_showmap);
        mLatLongTV = (TextView) findViewById(R.id.tv_latlong);
        mClickImageTV = (TextView) findViewById(R.id.tv_click);
        mClickImageTV.setOnClickListener(this);
        mUpdateTV = (TextView) findViewById(R.id.tv_update);
        mUpdateTV.setOnClickListener(this);
        mDeleteTV = (TextView) findViewById(R.id.tv_delete);
        mDeleteTV.setOnClickListener(this);
        mDateTV = (TextView) findViewById(R.id.tv_date);
        mDateTV.setOnClickListener(this);
        mTitleET = (EditText) findViewById(R.id.et_title);
        mPlaceET = (EditText) findViewById(R.id.et_place);
        mDetailsET = (EditText) findViewById(R.id.et_details);

        findViewById(R.id.tv_share).setOnClickListener(this);


        Intent intent = getIntent();

        if(intent!=null) {
            id = intent.getStringExtra("id") == null ? "" : intent.getStringExtra("id");
            title = intent.getStringExtra("title") == null ? "" : intent.getStringExtra("title");
            place = intent.getStringExtra("place") == null ? "" : intent.getStringExtra("place");
            date = intent.getStringExtra("date") == null ? "" : intent.getStringExtra("date");
            details = intent.getStringExtra("details") == null ? "" : intent.getStringExtra("details");
            lat_long = intent.getStringExtra("latlon") == null ? "" : intent.getStringExtra("latlon");
        }

        mDateTV.setText(date);
        mPlaceET.setText(place);
        mDetailsET.setText(details);
        mLatLongTV.setText(lat_long);
        mTitleET.setText(title);

        String[] latLong =   lat_long.split(",");
        if(latLong.length>1) {
            mLatitude = Double.parseDouble(latLong[0]);
            mLongitude= Double.parseDouble(latLong[1]);
        }

        mMapClickTV.setOnClickListener(this);


        databaseHelper = new DatabaseHelper(this);

        Log.e("ID===========> ", "" + id+" "+mLongitude+" "+mLatitude);
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.tv_showmap:
                Intent intent = new Intent(this, MapsActivity.class);
                intent.putExtra("LAT", mLatitude);
                intent.putExtra("LONGT", mLongitude);
                startActivity(intent);
                break;
            case R.id.tv_delete:
                int i=databaseHelper.deleteData(id);

                Intent intent1=new Intent(this,MainActivity.class);
                startActivity(intent1);
                finish();
                break;
            case R.id.tv_share:
                title = mTitleET.getText().toString().trim();
                place = mPlaceET.getText().toString().trim();
                details = mDetailsET.getText().toString().trim();
                date = mDateTV.getText().toString().trim();

                Intent intent2 = new Intent(); intent2.setAction(Intent.ACTION_SEND);
                intent2.setType("text/plain");
                intent2.putExtra(Intent.EXTRA_TEXT, "At "+place+", "+date+", "+title+" "+details );
                startActivity(Intent.createChooser(intent2, "Share via"));
                break;


            case R.id.tv_update:
                title = mTitleET.getText().toString().trim();
                place = mPlaceET.getText().toString().trim();
                details = mDetailsET.getText().toString().trim();
                lat_long = mLatitude + " || " + mLongitude;
                date = mDateTV.getText().toString().trim();

                if (TextUtils.isEmpty(title))
                    Toast.makeText(this, "Please Enter Title", Toast.LENGTH_SHORT).show();
                else if (TextUtils.isEmpty(place))
                    Toast.makeText(this, "Please Enter Place", Toast.LENGTH_SHORT).show();
                else if (TextUtils.isEmpty(date))
                    Toast.makeText(this, "Please Enter Date", Toast.LENGTH_SHORT).show();
                else
                    dataUpdated();
                break;
            case R.id.tv_date:

                final Calendar c = Calendar.getInstance();
                int mYear = c.get(Calendar.YEAR); // current year
                int mMonth = c.get(Calendar.MONTH); // current month
                int mDay = c.get(Calendar.DAY_OF_MONTH); // current day
                // date picker dialog
                datePickerDialog = new DatePickerDialog(this,
                        new DatePickerDialog.OnDateSetListener() {

                            @Override
                            public void onDateSet(DatePicker view, int year,
                                                  int monthOfYear, int dayOfMonth) {
                                // set day of month , month and year value in the edit text
                                //   todate.setText(dayOfMonth + "/" + (monthOfYear + 1) + "/" + year);

                                if (monthOfYear < 9 & dayOfMonth <= 9) {
                                    mDateTV.setText(year + "-" + "0" + (monthOfYear + 1) + "-" + "0" + dayOfMonth);
                                } else if (monthOfYear >= 9 & dayOfMonth <= 9) {
                                    mDateTV.setText(year + "-" + (monthOfYear + 1) + "-" + "0" + dayOfMonth);
                                } else if (monthOfYear >= 9 & dayOfMonth <= 9) {
                                    mDateTV.setText(year + "-" + "0" + (monthOfYear + 1) + "-" + "0" + dayOfMonth);
                                } else if (monthOfYear < 9 & dayOfMonth > 9) {
                                    mDateTV.setText(year + "-" + "0" + (monthOfYear + 1) + "-" + dayOfMonth);
                                } else {
                                    mDateTV.setText(year + "-" + (monthOfYear + 1) + "-" + dayOfMonth);
                                }
                            }
                        }, mYear, mMonth, mDay);
                datePickerDialog.show();
                break;
            case R.id.tv_click:
                takePicture();
                break;
        }
    }

    private void takePicture() {

        Intent intent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
        startActivityForResult(intent, CAMERA_REQUEST);
    }

    private void dataUpdated() {

        Log.e("Data Inserted===>>>>", "Inserted :" + "Title : " + title + "Date : " + date + "Details : " + details
                + "Place : " + place + "LatLong : " + lat_long);
        boolean isInsert = databaseHelper.updateData(id,title, date, details, place, lat_long);

        if (isInsert) {
            Toast.makeText(this, "Data saved succesfully", Toast.LENGTH_SHORT).show();
        } else {
            Toast.makeText(this, "Opps? Something wrong", Toast.LENGTH_SHORT).show();
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {

        if (requestCode == CAMERA_REQUEST && resultCode == RESULT_OK) {

            try {
                Bitmap bitmap = (Bitmap) data.getExtras().get("data");
                Log.e("Bitmap====>>>>", "" + bitmap);

                ByteArrayOutputStream stream = new ByteArrayOutputStream();
                bitmap.compress(Bitmap.CompressFormat.JPEG, 70, stream);
                byte[] byte_arr = stream.toByteArray();

                Bitmap decodedImage = BitmapFactory.decodeByteArray(byte_arr, 0, byte_arr.length);
                Log.e("Decode Image Base64==>>", "" + decodedImage);
                mShowImageIV.setImageBitmap(decodedImage);

            } catch (Exception e) {
                e.printStackTrace();
            }

            super.onActivityResult(requestCode, resultCode, data);
        }
    }
}
