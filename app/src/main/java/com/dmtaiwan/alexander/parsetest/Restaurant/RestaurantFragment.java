package com.dmtaiwan.alexander.parsetest.Restaurant;

import android.app.AlertDialog;
import android.app.Fragment;
import android.app.TimePickerDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.provider.MediaStore;
import android.support.annotation.Nullable;
import android.telephony.PhoneNumberUtils;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.Spinner;
import android.widget.TimePicker;
import android.widget.Toast;

import com.dmtaiwan.alexander.parsetest.List.ListActivity;
import com.dmtaiwan.alexander.parsetest.List.ListActivityFragment;
import com.dmtaiwan.alexander.parsetest.R;
import com.dmtaiwan.alexander.parsetest.Utilities.FileHelper;
import com.dmtaiwan.alexander.parsetest.Utilities.ParseConstants;
import com.dmtaiwan.alexander.parsetest.Utilities.Restaurant;
import com.parse.ParseACL;
import com.parse.ParseFile;
import com.parse.ParseUser;
import com.squareup.picasso.Callback;
import com.squareup.picasso.Picasso;

import java.io.File;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;

/**
 * Created by Alexander on 3/22/2015.
 */
public class RestaurantFragment extends Fragment implements RestaurantView, View.OnClickListener, CompoundButton.OnCheckedChangeListener, View.OnFocusChangeListener {

    private Presenter mPresenter;
    private static final String TAG = "RESTAURANT FRAGMENT";

    public static final String NEW_RESTAURANT = "new_restaurant";
    public static final String NEW_RESTAURANT_FROM_LIST = "new_restaurant_from_list";
    public static final String NEW_RESTAURANT_FROM_MAIN = "new_restaurant_from_main";
    public static final String NEW_RESTAURANT_FROM_MAP = "new_restaurant_from_map";
    public static final String NEW_RESTAURANT_FROM_HOME_AND_MAP = "new_restaurant_from_home_and_map";
    public static final String NEW_RESTAURANT_FROM_LIST_AND_MAP = "new_restaurant_from_list_and_map";
    public static final String ADDRESS_FROM_MAP = "address_from_map";
    public static final String CITY_FROM_MAP = "city_from_map";
    public static final String NAME_FROM_MAP = "name_from_map";
    public static final String PHONE_FROM_MAP = "phone_from_map";
    public static final String MAP_CODE = "com.alex.hours.extra.map.code";
    public static final String EXTRA_RESTAURANT_ID = "com.alex.hours.extra.restaurant.id";
    private static final String mFileType = ParseConstants.KEY_FILE_TYPE;
    public static final int TAKE_PHOTO_REQUEST = 0;
    public static final int PICK_PHOTO_REQUEST = 1;
    public static final int MEDIA_TYPE_IMAGE = 2;
    public static final String MY_RESTAURNT = "myrestaurant";
    public static final String ALL_RESTAURANTS = "allrestaurants";

    private ParseACL parseACL;
    private Restaurant mRestaurant;
    protected Uri mMediaUri;
    // Declare views
    private EditText mTitleField;
    private Spinner mSpinner;
    private EditText mAddressField;
    private ImageButton mMapButton;
    private EditText mCityField;
    private EditText mPhoneField;
    private ImageButton mPhoneButton;
    private CheckBox mSunday;
    private CheckBox mMonday;
    private CheckBox mTuesday;
    private CheckBox mWednesday;
    private CheckBox mThursday;
    private CheckBox mFriday;
    private CheckBox mSaturday;
    private Button mMasterOpenButton;
    private Button mMasterCloseButton;
    private Button mSundayOpenButton;
    private Button mSundayCloseButton;
    private Button mMondayOpenButton;
    private Button mMondayCloseButton;
    private Button mTuesdayOpenButton;
    private Button mTuesdayCloseButton;
    private Button mWednesdayOpenButton;
    private Button mWednesdayCloseButton;
    private Button mThursdayOpenButton;
    private Button mThursdayCloseButton;
    private Button mFridayOpenButton;
    private Button mFridayCloseButton;
    private Button mSaturdayOpenButton;
    private Button mSaturdayCloseButton;
    private EditText mNotesField;
    private Button mTakePictureButton;
    private ImageView mRestaurantImageView;
    private Button mSaveButton;
    private Button mCancelButton;
    private int mResultCode;
    private int mRequestCode;
    private Intent mData;
    private boolean mFromCamera = false;
    private String mQueryCode;

    protected static RestaurantFragment newInstance() {
        RestaurantFragment f = new RestaurantFragment();
        return f;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setHasOptionsMenu(true);
        // setup ACL to control read/write access

        parseACL = new ParseACL(ParseUser.getCurrentUser());
        parseACL.setPublicReadAccess(true);
        parseACL.setPublicWriteAccess(false);
        parseACL.setWriteAccess("e7So6F4ytk", true);
        parseACL.setWriteAccess("45LT0GnGVU", true);
    }

    @Override
    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
        super.onCreateOptionsMenu(menu, inflater);
        inflater.inflate(R.menu.menu_list, menu);
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View v = inflater.inflate(R.layout.fragment_restaurant, container, false);
        mPresenter = new PresenterImpl(this);


        // Setup fields
        setupFields(v);
        return v;
    }


    @Override
    public void onResume() {
        super.onResume();
        if (getActivity().getIntent() != null && !mFromCamera) {
            //Switch between creating a new restaurant and editing an already created restaurant based on QueryCode that indicates how this RestaurantActivity was created
            mQueryCode = getActivity().getIntent().getStringExtra(ListActivityFragment.QUERY_CODE);
            String restaurantId = getActivity().getIntent().getStringExtra(EXTRA_RESTAURANT_ID);

            //Switch between the queryCodes
            switch (mQueryCode) {
                case RestaurantFragment.NEW_RESTAURANT:
                    break;

                case ListActivityFragment.ALL_RESTAURATNS:
                    mPresenter.StartQueryRestaurant(mQueryCode, restaurantId);
                    break;

                case ListActivityFragment.SEARCH:
                    mPresenter.StartQueryRestaurant(mQueryCode, restaurantId);
                    break;
            }

        }


    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        Log.i(TAG, "On Results");
        mFromCamera = true;
        mResultCode = resultCode;
        mRequestCode = requestCode;
        mData = data;
        byte[] fileBytes = FileHelper.getByteArrayFromFile(getActivity(),
                mMediaUri);

        Picasso.with(getActivity()).load(mMediaUri).into(mRestaurantImageView);

        if(mRestaurant!=null) {
            if (fileBytes == null) {
                Log.i("Doom", "doom");
            } else {
                fileBytes = FileHelper.reduceImageForUpload(fileBytes);
                String fileName = FileHelper.getFileName(getActivity(), mMediaUri,
                        mFileType);
                ParseFile file = new ParseFile(fileName, fileBytes);
                mRestaurant.setImage(file);
            }
        }
    }

    @Override
    public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {

    }

    @Override
    public void onClick(View v) {
        int id = v.getId();
        switch (id) {

            case R.id.phone_button:
                //Launch implicit intent to Dialer with data from mPhoneField
                Intent callIntent = new Intent(Intent.ACTION_CALL);
                callIntent.setData(Uri.parse("tel:"
                        + mPhoneField.getText().toString()));
                startActivity(callIntent);
                break;

            case R.id.sunday_hours_open:
                TimePickerDialog dialog = new TimePickerDialog(getActivity(),
                        new TimePickerDialog.OnTimeSetListener() {
                            @Override
                            public void onTimeSet(TimePicker view, int hourOfDay,
                                                  int minute) {
                                mSunday.setChecked(true);
                                mSundayOpenButton.setText(new StringBuilder()
                                        .append(padding_str(hourOfDay)).append(":")
                                        .append(padding_str(minute)));
                            }
                        }, 7, 0, true);
                dialog.show();
                break;

            case R.id.sunday_hours_close:
                Log.i("Test", "Test");
                dialog = new TimePickerDialog(getActivity(),
                        new TimePickerDialog.OnTimeSetListener() {
                            @Override
                            public void onTimeSet(TimePicker view, int hourOfDay,
                                                  int minute) {
                                mSunday.setChecked(true);
                                mSundayCloseButton.setText(new StringBuilder()
                                        .append(padding_str(hourOfDay)).append(":")
                                        .append(padding_str(minute)));
                            }
                        }, 21, 0, true);
                dialog.show();
                break;

            case R.id.monday_hours_open:
                dialog = new TimePickerDialog(getActivity(),
                        new TimePickerDialog.OnTimeSetListener() {
                            @Override
                            public void onTimeSet(TimePicker view, int hourOfDay,
                                                  int minute) {
                                mMonday.setChecked(true);
                                mMondayOpenButton.setText(new StringBuilder()
                                        .append(padding_str(hourOfDay)).append(":")
                                        .append(padding_str(minute)));
                            }
                        }, 7, 0, true);
                dialog.show();
                break;

            case R.id.monday_hours_close:
                dialog = new TimePickerDialog(getActivity(),
                        new TimePickerDialog.OnTimeSetListener() {
                            @Override
                            public void onTimeSet(TimePicker view, int hourOfDay,
                                                  int minute) {
                                mMonday.setChecked(true);
                                mMondayCloseButton.setText(new StringBuilder()
                                        .append(padding_str(hourOfDay)).append(":")
                                        .append(padding_str(minute)));
                            }
                        }, 21, 0, true);
                dialog.show();
                break;

            case R.id.tuesday_hours_open:
                dialog = new TimePickerDialog(getActivity(),
                        new TimePickerDialog.OnTimeSetListener() {
                            @Override
                            public void onTimeSet(TimePicker view, int hourOfDay,
                                                  int minute) {
                                mTuesday.setChecked(true);
                                mTuesdayOpenButton.setText(new StringBuilder()
                                        .append(padding_str(hourOfDay)).append(":")
                                        .append(padding_str(minute)));
                            }
                        }, 7, 0, true);
                dialog.show();
                break;

            case R.id.tuesday_hours_close:
                dialog = new TimePickerDialog(getActivity(),
                        new TimePickerDialog.OnTimeSetListener() {
                            @Override
                            public void onTimeSet(TimePicker view, int hourOfDay,
                                                  int minute) {
                                mTuesday.setChecked(true);
                                mTuesdayCloseButton.setText(new StringBuilder()
                                        .append(padding_str(hourOfDay)).append(":")
                                        .append(padding_str(minute)));
                            }
                        }, 21, 0, true);
                dialog.show();
                break;

            case R.id.wednesday_hours_open:
                dialog = new TimePickerDialog(getActivity(),
                        new TimePickerDialog.OnTimeSetListener() {
                            @Override
                            public void onTimeSet(TimePicker view, int hourOfDay,
                                                  int minute) {
                                mWednesday.setChecked(true);
                                mWednesdayOpenButton.setText(new StringBuilder()
                                        .append(padding_str(hourOfDay)).append(":")
                                        .append(padding_str(minute)));
                            }
                        }, 7, 0, true);
                dialog.show();
                break;

            case R.id.wednesday_hours_close:
                dialog = new TimePickerDialog(getActivity(),
                        new TimePickerDialog.OnTimeSetListener() {
                            @Override
                            public void onTimeSet(TimePicker view, int hourOfDay,
                                                  int minute) {
                                mWednesday.setChecked(true);
                                mWednesdayCloseButton.setText(new StringBuilder()
                                        .append(padding_str(hourOfDay)).append(":")
                                        .append(padding_str(minute)));
                            }
                        }, 21, 0, true);
                dialog.show();
                break;

            case R.id.thursday_hours_open:
                dialog = new TimePickerDialog(getActivity(),
                        new TimePickerDialog.OnTimeSetListener() {
                            @Override
                            public void onTimeSet(TimePicker view, int hourOfDay,
                                                  int minute) {
                                mThursday.setChecked(true);
                                mThursdayOpenButton.setText(new StringBuilder()
                                        .append(padding_str(hourOfDay)).append(":")
                                        .append(padding_str(minute)));
                            }
                        }, 7, 0, true);
                dialog.show();
                break;

            case R.id.thursday_hours_close:
                dialog = new TimePickerDialog(getActivity(),
                        new TimePickerDialog.OnTimeSetListener() {
                            @Override
                            public void onTimeSet(TimePicker view, int hourOfDay,
                                                  int minute) {
                                mThursday.setChecked(true);
                                mThursdayCloseButton.setText(new StringBuilder()
                                        .append(padding_str(hourOfDay)).append(":")
                                        .append(padding_str(minute)));
                            }
                        }, 21, 0, true);
                dialog.show();
                break;

            case R.id.friday_hours_open:
                dialog = new TimePickerDialog(getActivity(),
                        new TimePickerDialog.OnTimeSetListener() {
                            @Override
                            public void onTimeSet(TimePicker view, int hourOfDay,
                                                  int minute) {
                                mThursday.setChecked(true);
                                mFridayOpenButton.setText(new StringBuilder()
                                        .append(padding_str(hourOfDay)).append(":")
                                        .append(padding_str(minute)));
                            }
                        }, 7, 0, true);
                dialog.show();
                break;

            case R.id.friday_hours_close:
                dialog = new TimePickerDialog(getActivity(),
                        new TimePickerDialog.OnTimeSetListener() {
                            @Override
                            public void onTimeSet(TimePicker view, int hourOfDay,
                                                  int minute) {
                                mFriday.setChecked(true);
                                mFridayCloseButton.setText(new StringBuilder()
                                        .append(padding_str(hourOfDay)).append(":")
                                        .append(padding_str(minute)));
                            }
                        }, 21, 0, true);
                dialog.show();
                break;

            case R.id.saturday_hours_open:
                dialog = new TimePickerDialog(getActivity(),
                        new TimePickerDialog.OnTimeSetListener() {
                            @Override
                            public void onTimeSet(TimePicker view, int hourOfDay,
                                                  int minute) {
                                mSaturday.setChecked(true);
                                mSaturdayOpenButton.setText(new StringBuilder()
                                        .append(padding_str(hourOfDay)).append(":")
                                        .append(padding_str(minute)));
                            }
                        }, 7, 0, true);
                dialog.show();
                break;

            case R.id.saturday_hours_close:
                dialog = new TimePickerDialog(getActivity(),
                        new TimePickerDialog.OnTimeSetListener() {
                            @Override
                            public void onTimeSet(TimePicker view, int hourOfDay,
                                                  int minute) {
                                mSaturday.setChecked(true);
                                mSaturdayCloseButton.setText(new StringBuilder()
                                        .append(padding_str(hourOfDay)).append(":")
                                        .append(padding_str(minute)));
                            }
                        }, 21, 0, true);
                dialog.show();
                break;

            case R.id.master_hours_open:
                dialog = new TimePickerDialog(getActivity(),
                        new TimePickerDialog.OnTimeSetListener() {
                            @Override
                            public void onTimeSet(TimePicker view, int hourOfDay,
                                                  int minute) {
                                mSunday.setChecked(true);
                                mMonday.setChecked(true);
                                mTuesday.setChecked(true);
                                mWednesday.setChecked(true);
                                mThursday.setChecked(true);
                                mFriday.setChecked(true);
                                mSaturday.setChecked(true);
                                mSundayOpenButton.setText(new StringBuilder()
                                        .append(padding_str(hourOfDay)).append(":")
                                        .append(padding_str(minute)));
                                mMondayOpenButton.setText(new StringBuilder()
                                        .append(padding_str(hourOfDay)).append(":")
                                        .append(padding_str(minute)));
                                mTuesdayOpenButton.setText(new StringBuilder()
                                        .append(padding_str(hourOfDay)).append(":")
                                        .append(padding_str(minute)));
                                mWednesdayOpenButton.setText(new StringBuilder()
                                        .append(padding_str(hourOfDay)).append(":")
                                        .append(padding_str(minute)));
                                mThursdayOpenButton.setText(new StringBuilder()
                                        .append(padding_str(hourOfDay)).append(":")
                                        .append(padding_str(minute)));
                                mFridayOpenButton.setText(new StringBuilder()
                                        .append(padding_str(hourOfDay)).append(":")
                                        .append(padding_str(minute)));
                                mSaturdayOpenButton.setText(new StringBuilder()
                                        .append(padding_str(hourOfDay)).append(":")
                                        .append(padding_str(minute)));
                            }
                        }, 7, 0, true);
                dialog.show();
                break;

            case R.id.master_hours_close:
                dialog = new TimePickerDialog(getActivity(),
                        new TimePickerDialog.OnTimeSetListener() {
                            @Override
                            public void onTimeSet(TimePicker view, int hourOfDay,
                                                  int minute) {
                                mSundayCloseButton.setText(new StringBuilder()
                                        .append(padding_str(hourOfDay)).append(":")
                                        .append(padding_str(minute)));
                                mMondayCloseButton.setText(new StringBuilder()
                                        .append(padding_str(hourOfDay)).append(":")
                                        .append(padding_str(minute)));
                                mTuesdayCloseButton.setText(new StringBuilder()
                                        .append(padding_str(hourOfDay)).append(":")
                                        .append(padding_str(minute)));
                                mWednesdayCloseButton.setText(new StringBuilder()
                                        .append(padding_str(hourOfDay)).append(":")
                                        .append(padding_str(minute)));
                                mThursdayCloseButton.setText(new StringBuilder()
                                        .append(padding_str(hourOfDay)).append(":")
                                        .append(padding_str(minute)));
                                mFridayCloseButton.setText(new StringBuilder()
                                        .append(padding_str(hourOfDay)).append(":")
                                        .append(padding_str(minute)));
                                mSaturdayCloseButton.setText(new StringBuilder()
                                        .append(padding_str(hourOfDay)).append(":")
                                        .append(padding_str(minute)));
                            }
                        }, 21, 0, true);
                dialog.show();
                break;

            case R.id.saveButton:
                String queryCode = getActivity().getIntent().getStringExtra(ListActivityFragment.QUERY_CODE);
                if (mRestaurant == null) {
                    mRestaurant = createRestaurant();
                    mPresenter.SaveRestaurant(mQueryCode, mRestaurant);
                } else {
                    updateFields();
                    mPresenter.SaveRestaurant(mQueryCode, mRestaurant);
                }
                break;

            case R.id.cancelButton:
                getActivity().finish();
                break;

            case R.id.takePictureButton:
                AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
                builder.setItems(R.array.camera_choices, mDialogListener);
                AlertDialog dialog_camera = builder.create();
                dialog_camera.show();
                break;
        }

    }


    private Uri getOutputMediaFileUri(int mediaType) {
// check if external storage is mounted
        if (isExternalStorageAvailable()) {
            Log.i("test", "testing " + isExternalStorageAvailable());
// get URI
// 1. Get the external storage directory
            String appName = getActivity().getString(R.string.app_name);
            File mediaStorageDir = new File(
                    Environment
                            .getExternalStoragePublicDirectory(Environment.DIRECTORY_PICTURES),
                    appName);
// 2. Create our subdirectory
            if (!mediaStorageDir.exists()) {
// create directory, mkdirs returns boolean false if failed,
// return nullif failed
                if (!mediaStorageDir.mkdirs()) {
                    Log.i("DIR ERROR", "Failed to create directory");
                    return null;
                }
            }
// 3. Create a file name
// 4 Create a file
            File mediaFile;
            Date now = new Date();
            String timestamp = new SimpleDateFormat("yyyyMMdd_HHmmss",
                    Locale.US).format(now);
            String path = mediaStorageDir.getPath() + File.separator;
// Check media type and create file
            if (mediaType == MEDIA_TYPE_IMAGE) {
                mediaFile = new File(path + "IMG_" + timestamp + ".jpg");
            } else {
                return null;
            }
            Log.i("Filename", "file " + Uri.fromFile(mediaFile));
// 5 Return the file's URI
            return Uri.fromFile(mediaFile);
        } else {
            return null;
        }
    }

    private boolean isExternalStorageAvailable() {
        String state = Environment.getExternalStorageState();
        if (state.equals(Environment.MEDIA_MOUNTED)) {
            return true;
        } else {
            return false;
        }
    }


    @Override
    public void onFocusChange(View v, boolean hasFocus) {

    }

    private void checkUserForAuthorOrAdmin() {
        String currentUser = ParseUser.getCurrentUser().getObjectId().toString();
        String author = mRestaurant.getAuthor()
                .getObjectId().toString();
        if (!currentUser.equals(author)
                && !currentUser
                .equals("e7So6F4ytk")
                && !currentUser
                .equals("45LT0GnGVU")) {

            if (getActivity() != null) {
                Toast.makeText(
                        getActivity(),
                        R.string.toast_edit_error_message,
                        Toast.LENGTH_LONG).show();
            }
// todo
// mMapButton.setEnabled(false);
            mSaveButton.setEnabled(false);
            mTakePictureButton.setEnabled(false);
// }
        }
    }

    private void setupFields(View v) {
        mTitleField = (EditText) v.findViewById(R.id.restaurant_title);
        mTitleField.setOnFocusChangeListener(this);
        mSpinner = (Spinner) v.findViewById(R.id.category_spinner);
        ArrayAdapter<CharSequence> adapter = ArrayAdapter.createFromResource(
                getActivity(), R.array.category_array,
                android.R.layout.simple_spinner_dropdown_item);
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        mSpinner.setAdapter(adapter);
        mSpinner.setPrompt("test");
        mAddressField = (EditText) v.findViewById(R.id.restaurant_address);
        mAddressField.setOnFocusChangeListener(this);
        mMapButton = (ImageButton) v.findViewById(R.id.map_button);
        mMapButton.setOnClickListener(this);
        mCityField = (EditText) v.findViewById(R.id.restaurant_city);
        mCityField.setOnFocusChangeListener(this);
        mPhoneField = (EditText) v.findViewById(R.id.restaurant_phone);
        mPhoneButton = (ImageButton) v.findViewById(R.id.phone_button);
        mPhoneButton.setOnClickListener(this);
        mSunday = (CheckBox) v.findViewById(R.id.sunday_check);
        mSunday.setOnCheckedChangeListener(this);
        mMonday = (CheckBox) v.findViewById(R.id.monday_check);
        mMonday.setOnCheckedChangeListener(this);
        mTuesday = (CheckBox) v.findViewById(R.id.tuesday_check);
        mTuesday.setOnCheckedChangeListener(this);
        mWednesday = (CheckBox) v.findViewById(R.id.wednesday_check);
        mWednesday.setOnCheckedChangeListener(this);
        mThursday = (CheckBox) v.findViewById(R.id.thursday_check);
        mThursday.setOnCheckedChangeListener(this);
        mFriday = (CheckBox) v.findViewById(R.id.friday_check);
        mFriday.setOnCheckedChangeListener(this);
        mSaturday = (CheckBox) v.findViewById(R.id.saturday_check);
        mSaturday.setOnCheckedChangeListener(this);
        mMasterOpenButton = (Button) v.findViewById(R.id.master_hours_open);
        mMasterOpenButton.setOnClickListener(this);
        mMasterCloseButton = (Button) v.findViewById(R.id.master_hours_close);
        mMasterCloseButton.setOnClickListener(this);
        mSundayOpenButton = (Button) v.findViewById(R.id.sunday_hours_open);
        mSundayOpenButton.setOnClickListener(this);
        mSundayCloseButton = (Button) v.findViewById(R.id.sunday_hours_close);
        mSundayCloseButton.setOnClickListener(this);
        mMondayOpenButton = (Button) v.findViewById(R.id.monday_hours_open);
        mMondayOpenButton.setOnClickListener(this);
        mMondayCloseButton = (Button) v.findViewById(R.id.monday_hours_close);
        mMondayCloseButton.setOnClickListener(this);
        mTuesdayOpenButton = (Button) v.findViewById(R.id.tuesday_hours_open);
        mTuesdayOpenButton.setOnClickListener(this);
        mTuesdayCloseButton = (Button) v.findViewById(R.id.tuesday_hours_close);
        mTuesdayCloseButton.setOnClickListener(this);
        mWednesdayOpenButton = (Button) v
                .findViewById(R.id.wednesday_hours_open);
        mWednesdayOpenButton.setOnClickListener(this);
        mWednesdayCloseButton = (Button) v
                .findViewById(R.id.wednesday_hours_close);
        mWednesdayCloseButton.setOnClickListener(this);
        mThursdayOpenButton = (Button) v.findViewById(R.id.thursday_hours_open);
        mThursdayOpenButton.setOnClickListener(this);
        mThursdayCloseButton = (Button) v
                .findViewById(R.id.thursday_hours_close);
        mThursdayCloseButton.setOnClickListener(this);
        mFridayOpenButton = (Button) v.findViewById(R.id.friday_hours_open);
        mFridayOpenButton.setOnClickListener(this);
        mFridayCloseButton = (Button) v.findViewById(R.id.friday_hours_close);
        mFridayCloseButton.setOnClickListener(this);
        mSaturdayOpenButton = (Button) v.findViewById(R.id.saturday_hours_open);
        mSaturdayOpenButton.setOnClickListener(this);
        mSaturdayCloseButton = (Button) v
                .findViewById(R.id.saturday_hours_close);
        mSaturdayCloseButton.setOnClickListener(this);
        mNotesField = (EditText) v.findViewById(R.id.restaurant_notes);
        mNotesField.setOnFocusChangeListener(this);
        mTakePictureButton = (Button) v.findViewById(R.id.takePictureButton);
        mTakePictureButton.setOnClickListener(this);
        mRestaurantImageView = (ImageView) v
                .findViewById(R.id.restaurantImageView);
        mSaveButton = (Button) v.findViewById(R.id.saveButton);
        mSaveButton.setOnClickListener(this);
        mCancelButton = (Button) v.findViewById(R.id.cancelButton);
        mCancelButton.setOnClickListener(this);
    }


    private void loadFields() {
        mTitleField.setText(mRestaurant.getTitle());
        mTitleField.clearFocus();
        if (getArguments() != null) {
            if (getArguments().getString(MAP_CODE) != null
                    && getArguments().getString(NAME_FROM_MAP) != null) {
                mTitleField.setText(getArguments().getString(NAME_FROM_MAP));
                mTitleField.clearFocus();
            }
        }
// Figure out what the category is and set spinner to that item
        if (!(mRestaurant.getCategory() == null)) {
            String compareValue = mRestaurant.getCategory();
            ArrayAdapter<CharSequence> adapter = ArrayAdapter
                    .createFromResource(getActivity(), R.array.category_array,
                            android.R.layout.simple_spinner_dropdown_item);
            adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
            mSpinner.setAdapter(adapter);
            if (!compareValue.equals(null)) {
                int SpinnerPosition = adapter.getPosition(compareValue);
                mSpinner.setSelection(SpinnerPosition);
                SpinnerPosition = 0;
            }
        }
        mAddressField.setText(mRestaurant.getAddress());
        mAddressField.clearFocus();
        if (getArguments() != null) {
            if (getArguments().getString(MAP_CODE) != null
                    && getArguments().getString(ADDRESS_FROM_MAP) != null) {
                mAddressField.setText(getArguments()
                        .getString(ADDRESS_FROM_MAP));
                mAddressField.clearFocus();
            }
        }
        mCityField.setText(mRestaurant.getCity());
        mCityField.clearFocus();
        if (getArguments() != null) {
            if (getArguments().getString(MAP_CODE) != null
                    && getArguments().getString(CITY_FROM_MAP) != null) {
                mCityField.setText(getArguments().getString(CITY_FROM_MAP));
                mCityField.clearFocus();
            }
        }
        mPhoneField.setText(mRestaurant.getPhone());
        mPhoneField.clearFocus();
        if (getArguments() != null) {
            if (getArguments().getString(MAP_CODE) != null
                    && getArguments().getString(PHONE_FROM_MAP) != null) {
                mPhoneField.setText(getArguments().getString(PHONE_FROM_MAP));
                mPhoneField.clearFocus();
            }
        }
        mSunday.setChecked(mRestaurant.getSunday());
        mMonday.setChecked(mRestaurant.getMonday());
        mTuesday.setChecked(mRestaurant.getTuesday());
        mWednesday.setChecked(mRestaurant.getWednesday());
        mThursday.setChecked(mRestaurant.getThursday());
        mFriday.setChecked(mRestaurant.getFriday());
        mSaturday.setChecked(mRestaurant.getSaturday());
        mSundayOpenButton.setText(mRestaurant.getSundayOpenHours());
        mSundayCloseButton.setText(mRestaurant.getSundayCloseHours());
        mMondayOpenButton.setText(mRestaurant.getMondayOpenHours());
        mMondayCloseButton.setText(mRestaurant.getMondayCloseHours());
        mTuesdayOpenButton.setText(mRestaurant.getTuesdayOpenHours());
        mTuesdayCloseButton.setText(mRestaurant.getTuesdayCloseHours());
        mWednesdayOpenButton.setText(mRestaurant.getWednesdayOpenHours());
        mWednesdayCloseButton.setText(mRestaurant.getWednesdayCloseHours());
        mThursdayOpenButton.setText(mRestaurant.getThursdayOpenHours());
        mThursdayCloseButton.setText(mRestaurant.getThursdayCloseHours());
        mFridayOpenButton.setText(mRestaurant.getFridayOpenHours());
        mFridayCloseButton.setText(mRestaurant.getFridayCloseHours());
        mSaturdayOpenButton.setText(mRestaurant.getSaturdayOpenHours());
        mSaturdayCloseButton.setText(mRestaurant.getSaturdayCloseHours());
        mNotesField.setText(mRestaurant.getNotes());
        mNotesField.clearFocus();

        //Load image
        ParseFile file = mRestaurant.getImage();
        if (file != null) {
            Uri fileUri = Uri.parse(file.getUrl());
            Log.i("file", "file" + fileUri);
            if (getActivity() != null) {
                onLoading();
                Picasso.with(getActivity()).load(fileUri.toString())
                        .into(mRestaurantImageView, new Callback() {
                            @Override
                            public void onSuccess() {
                                onFinishedLoading();
                            }

                            @Override
                            public void onError() {
                            }
                        });
            }
        }
    }


    private Restaurant createRestaurant() {

        Restaurant restaurant = new Restaurant();
        restaurant.setACL(parseACL);

        restaurant.setAuthor();

        restaurant.setTitle(mTitleField.getText().toString());

        restaurant.setLowerCaseTitle(mTitleField.getText().toString()
                .toLowerCase());

        if (!mSpinner.getSelectedItem().toString().equals("Choose a Category")) {
            restaurant.setCategory(mSpinner.getSelectedItem().toString());
        }

        restaurant.setAddress(mAddressField.getText().toString());

        restaurant.setLowerCaseAddress(mAddressField.getText().toString()
                .toLowerCase());

        restaurant.setCity(mCityField.getText().toString().trim());

        restaurant.setLowerCaseCity(mCityField.getText().toString()
                .toLowerCase().trim());

        restaurant.setPhone(PhoneNumberUtils.formatNumber(mPhoneField.getText()
                .toString()));

        restaurant.setSunday(mSunday.isChecked());

        restaurant.setSundayOpenHours(mSundayOpenButton.getText().toString());

        restaurant.setSundayCloseHours(mSundayCloseButton.getText().toString());

        restaurant.setMonday(mMonday.isChecked());

        restaurant.setMondayOpenHours(mMondayOpenButton.getText().toString());

        restaurant.setMondayCloseHours(mMondayCloseButton.getText().toString());

        restaurant.setTuesday(mTuesday.isChecked());

        restaurant.setTuesdayOpenHours(mTuesdayOpenButton.getText().toString());

        restaurant.setTuesdayCloseHours(mTuesdayCloseButton.getText()
                .toString());

        restaurant.setWednesday(mWednesday.isChecked());

        restaurant.setWednesdayOpenHours(mWednesdayOpenButton.getText()
                .toString());

        restaurant.setWednesdayCloseHours(mWednesdayCloseButton.getText()
                .toString());

        restaurant.setThursday(mThursday.isChecked());
        restaurant.setThursdayOpenHours(mThursdayOpenButton.getText()
                .toString());

        restaurant.setThursdayCloseHours(mThursdayCloseButton.getText()
                .toString());

        restaurant.setFriday(mFriday.isChecked());

        restaurant.setFridayOpenHours(mFridayOpenButton.getText().toString());

        restaurant.setFridayCloseHours(mFridayCloseButton.getText().toString());

        restaurant.setSaturday(mSaturday.isChecked());

        restaurant.setSaturdayOpenHours(mSaturdayOpenButton.getText()
                .toString());

        restaurant.setSaturdayCloseHours(mSaturdayCloseButton.getText()
                .toString());

        restaurant.setNotes(mNotesField.getText().toString());

        //Set the image if one is available
        if (mMediaUri != null) {
            byte[] fileBytes = FileHelper.getByteArrayFromFile(getActivity(),
                    mMediaUri);
            fileBytes = FileHelper.reduceImageForUpload(fileBytes);
            String fileName = FileHelper.getFileName(getActivity(), mMediaUri,
                    mFileType);
            ParseFile file = new ParseFile(fileName, fileBytes);
            restaurant.setImage(file);
        }

        return restaurant;
    }

    private void updateFields() {
        mRestaurant.setTitle(mTitleField.getText().toString());
        mRestaurant.setLowerCaseTitle(mTitleField.getText().toString()
                .toLowerCase());
        if (!mSpinner.getSelectedItem().toString().equals("Choose a Category")) {
            mRestaurant.setCategory(mSpinner.getSelectedItem().toString());
        }
        mRestaurant.setAddress(mAddressField.getText().toString());
        mRestaurant.setLowerCaseAddress(mAddressField.getText().toString()
                .toLowerCase());
        mRestaurant.setCity(mCityField.getText().toString().trim());
        mRestaurant.setLowerCaseCity(mCityField.getText().toString()
                .toLowerCase().trim());
        mRestaurant.setPhone(PhoneNumberUtils.formatNumber(mPhoneField
                .getText().toString()));
        mRestaurant.setSunday(mSunday.isChecked());
        mRestaurant.setMonday(mMonday.isChecked());
        mRestaurant.setTuesday(mTuesday.isChecked());
        mRestaurant.setWednesday(mWednesday.isChecked());
        mRestaurant.setThursday(mThursday.isChecked());
        mRestaurant.setFriday(mFriday.isChecked());
        mRestaurant.setSaturday(mSaturday.isChecked());
        mRestaurant.setSundayOpenHours(mSundayOpenButton.getText().toString());
        mRestaurant
                .setSundayCloseHours(mSundayCloseButton.getText().toString());
        mRestaurant.setMondayOpenHours(mMondayOpenButton.getText().toString());
        mRestaurant
                .setMondayCloseHours(mMondayCloseButton.getText().toString());
        mRestaurant
                .setTuesdayOpenHours(mTuesdayOpenButton.getText().toString());
        mRestaurant.setTuesdayCloseHours(mTuesdayCloseButton.getText()
                .toString());
        mRestaurant.setWednesdayOpenHours(mWednesdayOpenButton.getText()
                .toString());
        mRestaurant.setWednesdayCloseHours(mWednesdayCloseButton.getText()
                .toString());
        mRestaurant.setThursdayOpenHours(mThursdayOpenButton.getText()
                .toString());
        mRestaurant.setThursdayCloseHours(mThursdayCloseButton.getText()
                .toString());
        mRestaurant.setFridayOpenHours(mFridayOpenButton.getText().toString());
        mRestaurant
                .setFridayCloseHours(mFridayCloseButton.getText().toString());
        mRestaurant.setSaturdayOpenHours(mSaturdayOpenButton.getText()
                .toString());
        mRestaurant.setSaturdayCloseHours(mSaturdayCloseButton.getText()
                .toString());
        mRestaurant.setNotes(mNotesField.getText().toString());
    }

    private static String padding_str(int c) {
        if (c >= 10)
            return String.valueOf(c);
        else
            return "0" + String.valueOf(c);
    }


    @Override
    public void onCreateRestaurant() {
        createRestaurant();
    }

    @Override
    public void onFinishedCreating() {
        Intent i = new Intent(getActivity(), ListActivity.class);
        i.putExtra(ListActivityFragment.QUERY_CODE, ListActivityFragment.ALL_RESTAURATNS);
        i.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        i.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
        startActivity(i);
        getActivity().finish();
    }

    @Override
    public void showProgress() {

    }

    @Override
    public void hideProgress() {

    }

    @Override
    public void onLoading() {
        getActivity().setProgressBarIndeterminateVisibility(true);

        //Disable buttons to prevent activity while loading
        mSaveButton.setEnabled(false);
        mCancelButton.setEnabled(false);
        mTakePictureButton.setEnabled(false);
    }

    @Override
    public void onFinishedLoading() {
        getActivity().setProgressBarIndeterminateVisibility(false);

        //Re-enable buttons onFinishedLoading
        mSaveButton.setEnabled(true);
        mCancelButton.setEnabled(true);
        mTakePictureButton.setEnabled(true);

    }

    @Override
    public void onSuccess(Restaurant restaurant) {
        mRestaurant = restaurant;
        //Check if the user is author or admin, if not disable the save and take picture button
        checkUserForAuthorOrAdmin();
        loadFields();
    }

    protected DialogInterface.OnClickListener mDialogListener = new DialogInterface.OnClickListener() {
        @Override
        public void onClick(DialogInterface dialog, int which) {
            switch (which) {
                case 0: // Take picture
                    Intent takePhotoIntent = new Intent(
                            MediaStore.ACTION_IMAGE_CAPTURE);
                    // save file path
                    mMediaUri = getOutputMediaFileUri(MEDIA_TYPE_IMAGE);
                    // if mMediaUri is null, storage error
                    if (mMediaUri == null) {
                        Toast.makeText(getActivity(),
                                R.string.external_storage_error, Toast.LENGTH_LONG)
                                .show();
                    } else {
                        // pass photo path to camera
                        takePhotoIntent
                                .putExtra(MediaStore.EXTRA_OUTPUT, mMediaUri);
                        startActivityForResult(takePhotoIntent, TAKE_PHOTO_REQUEST);
                    }
                    break;
                case 1: // Take video
                    Intent choosePhotoIntent = new Intent(Intent.ACTION_GET_CONTENT);
                    choosePhotoIntent.setType("image/*");
                    startActivityForResult(choosePhotoIntent, PICK_PHOTO_REQUEST);
                    break;
            }
        }
    };
}
