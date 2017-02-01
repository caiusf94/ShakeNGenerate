package com.caiusf.shakengenerate.activities;

import android.app.Activity;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.hardware.Sensor;
import android.hardware.SensorManager;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.text.InputFilter;
import android.text.InputType;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.view.inputmethod.InputMethodManager;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.caiusf.shakengenerate.R;
import com.caiusf.shakengenerate.exceptions.BoundariesNotSetException;
import com.caiusf.shakengenerate.exceptions.ListSizeNotSetException;
import com.caiusf.shakengenerate.exceptions.ListSizeTooLargeException;
import com.caiusf.shakengenerate.exceptions.ListWithZeroItemsException;
import com.caiusf.shakengenerate.exceptions.NumberTooLargeException;
import com.caiusf.shakengenerate.exceptions.NumberTooSmallException;
import com.caiusf.shakengenerate.listeners.ShakeEventListener;
import com.caiusf.shakengenerate.models.Language;
import com.caiusf.shakengenerate.utils.number.RandomNumberGeneration;
import com.caiusf.shakengenerate.utils.toast.ToastDisplayer;

import java.util.Comparator;
import java.util.Locale;

import static com.caiusf.shakengenerate.utils.number.RandomNumberGeneration.lowerIsGreaterThanUpper;
import static com.caiusf.shakengenerate.utils.number.RandomNumberGeneration.numberTooLarge;
import static com.caiusf.shakengenerate.utils.number.RandomNumberGeneration.numberTooSmall;

public class MainActivity extends AppCompatActivity {

    private SensorManager mSensorManager;
    private ShakeEventListener mSensorListener;

    private int lower;
    private int upper;

    EditText lowerLimitField;
    EditText upperLimitField;
    TextView numberDisplayed;
    Button buttonToGenerate;

    TextView onScreenGuide;

    TextView topText;
    TextView andText;

    AlertDialog alertDialog;
    AlertDialog.Builder builderSingle;

    public int howManyNumbersToGenerate;

    private boolean shouldChangeLocale;

    Menu absTopSubMenus;

    @Override
    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);

        getSupportActionBar().setDisplayShowHomeEnabled(true);
        getSupportActionBar().setLogo(R.mipmap.ic_launcher);
        getSupportActionBar().setDisplayUseLogoEnabled(true);
        setContentView(R.layout.activity_main);


        lowerLimitField = (EditText) findViewById(R.id.lowerLimit);

        upperLimitField = (EditText) findViewById(R.id.upperLimit);


        numberDisplayed = (TextView) findViewById(R.id.numberDisplayed);

        onScreenGuide = (TextView) findViewById(R.id.shakeYourDeviceText);

        buttonToGenerate = (Button) findViewById(R.id.buttonToGenerate);

        topText = (TextView) findViewById(R.id.topText);
        andText = (TextView) findViewById(R.id.andText);

        shouldChangeLocale = true;


        loadLocale();


        lowerLimitField.setOnFocusChangeListener(new View.OnFocusChangeListener() {
            @Override
            public void onFocusChange(View v, boolean hasFocus) {
                if (!hasFocus) {
                    hideKeyboard(v);
                }
            }
        });

        upperLimitField.setOnFocusChangeListener(new View.OnFocusChangeListener() {
            @Override
            public void onFocusChange(View v, boolean hasFocus) {
                if (!hasFocus) {
                    hideKeyboard(v);
                }
            }
        });

        buttonToGenerate.setOnClickListener(new View.OnClickListener() {
                                                @Override
                                                public void onClick(View v) {
                                                    displayRandomNumberOnScreen();
                                                }
                                            }
        );


        mSensorManager = (SensorManager) getSystemService(Context.SENSOR_SERVICE);
        mSensorListener = new ShakeEventListener();

        mSensorListener.setOnShakeListener(new ShakeEventListener.OnShakeListener() {
            @Override
            public void onShake() {
                displayRandomNumberOnScreen();
                hideSoftKeyboard();
            }
        });

        FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(final View view) {
                boolean boundariesAreSet = true;
                try {
                    prepareGenerating();
                } catch (BoundariesNotSetException e) {
                    ToastDisplayer.displayShortToast(MainActivity.this, getResources().getString(R.string.specifyRange));
                    boundariesAreSet = false;
                } catch (NumberTooSmallException e) {
                    ToastDisplayer.displayShortToast(MainActivity.this, getResources().getString(R.string.nbTooSmall));
                    boundariesAreSet = false;
                } catch (NumberTooLargeException e) {
                    ToastDisplayer.displayShortToast(MainActivity.this, getResources().getString(R.string.nbTooLarge));
                    boundariesAreSet = false;
                }

                if (boundariesAreSet) {
                    final AlertDialog.Builder builder = new AlertDialog.Builder(MainActivity.this);
                    builder.setTitle(getResources().getString(R.string.generateMoreNumbersTitle));
                    builder.setMessage(getResources().getString(R.string.generateMoreNumbersMessage));

// Set up the input

                    final EditText input = new EditText(MainActivity.this);
                    final CheckBox ch = new CheckBox(MainActivity.this);

// Specify the type of input expected;
                    input.setInputType(InputType.TYPE_CLASS_NUMBER);
                    input.setFilters(new InputFilter[]{new InputFilter.LengthFilter(3)});
                    ch.setText(R.string.dontRepeat);
                    ch.setChecked(true);

                    LinearLayout.LayoutParams inputParams = new LinearLayout.LayoutParams(250, ViewGroup.LayoutParams.WRAP_CONTENT);
                    inputParams.setMargins(40, 0, 0, 0);


                    LinearLayout.LayoutParams chParams = new LinearLayout.LayoutParams(ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.WRAP_CONTENT);
                    chParams.setMargins(33, 20, 0, 0);


                    final LinearLayout layout = new LinearLayout(MainActivity.this);
                    layout.setOrientation(LinearLayout.VERTICAL);

                    layout.addView(input, inputParams);
                    layout.addView(ch, chParams);


                    builder.setView(layout);


// Set up the buttons
                    builder.setPositiveButton(R.string.ok, new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {

                            prepareGenerating();

                            boolean isChecked = ch.isChecked();

//                            lower = Integer.parseInt(lowerLimitField.getText().toString());
//                            upper = Integer.parseInt(upperLimitField.getText().toString());
                            Intent i = new Intent(getApplicationContext(), ListOfNumbersActivity.class);


                            try {
                                if (fieldIsEmpty(input)) {
                                    throw new ListSizeNotSetException();
                                } else {
                                    howManyNumbersToGenerate = Integer.parseInt(input.getText().toString());
                                }

                                if (howManyNumbersToGenerate == 0) {
                                    throw new ListWithZeroItemsException();
                                }
                                if (howManyNumbersToGenerate > 999) {
                                    throw new ListSizeTooLargeException();
                                }


                                //Create the bundle
                                Bundle bundle = new Bundle();

                                bundle.putInt("howManyNumbers", howManyNumbersToGenerate);
                                bundle.putInt("lower", lower);
                                bundle.putInt("upper", upper);
                                bundle.putBoolean("isChecked", isChecked);

                                i.putExtras(bundle);

//Fire that second activity
                                startActivity(i);


                            } catch (ListWithZeroItemsException e) {
                                ToastDisplayer.displayShortToast(MainActivity.this, getResources().getString(R.string.atLeastOneElement));
                            } catch (ListSizeNotSetException e) {
                                ToastDisplayer.displayShortToast(MainActivity.this, getResources().getString(R.string.enterNumber));
                            } catch (ListSizeTooLargeException e) {
                                ToastDisplayer.displayShortToast(MainActivity.this, getResources().getString(R.string.listTooLarge));
                            }
                        }
                    });
                    builder.setNegativeButton(R.string.cancel, new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            dialog.cancel();
                        }
                    });

                    builder.show();
                }
            }
        });


    }

    private void displayRandomNumberOnScreen() {

        try {
            prepareGenerating();
            numberDisplayed.setText(String.format("%d", RandomNumberGeneration.generateRandomNumber(lower, upper)));
            onScreenGuide.setText(R.string.shakeAgain);

        } catch (BoundariesNotSetException e) {
            e.printStackTrace();
        } catch (NumberTooSmallException e) {
            e.printStackTrace();
        } catch (NumberTooLargeException e) {
            e.printStackTrace();
        }


    }

    @Override
    protected void onResume() {
        super.onResume();

        loadLocale();
        shouldChangeLocale = true;
        mSensorManager.registerListener(mSensorListener,
                mSensorManager.getDefaultSensor(Sensor.TYPE_ACCELEROMETER),
                SensorManager.SENSOR_DELAY_UI);

    }

    @Override
    protected void onPause() {

        mSensorManager.unregisterListener(mSensorListener);
        if (ToastDisplayer.getToast() != null)
            ToastDisplayer.getToast().cancel();
        super.onPause();

    }

    @Override
    protected void onStart() {
        super.onStart();
    }

    @Override
    protected void onRestart() {
        absTopSubMenus.clear();
        onCreateOptionsMenu(absTopSubMenus);
        super.onRestart();
    }


    private void prepareGenerating() throws BoundariesNotSetException, NumberTooSmallException, NumberTooLargeException {

        if (fieldIsEmpty(lowerLimitField) || fieldIsEmpty(upperLimitField)) {
            onScreenGuide.setText(R.string.shakeDevice);
            ToastDisplayer.displayShortToast(MainActivity.this, getResources().getString(R.string.specifyRange));
            throw new BoundariesNotSetException();

        } else {
            lower = Integer.parseInt(lowerLimitField.getText().toString());
            upper = Integer.parseInt(upperLimitField.getText().toString());
        }


        if (lowerIsGreaterThanUpper(lower, upper)) {
            swapLimits();
        }

        if (numberTooSmall(lower)) {
            onScreenGuide.setText(R.string.shakeDevice);
            ToastDisplayer.displayShortToast(MainActivity.this, getResources().getString(R.string.nbTooSmall));
            throw new NumberTooSmallException();

        }

        if (numberTooLarge(upper)) {
            onScreenGuide.setText(R.string.shakeDevice);
            ToastDisplayer.displayShortToast(MainActivity.this, getResources().getString(R.string.nbTooLarge));
            throw new NumberTooLargeException();
        }


    }

    private void swapLimits() {
        int temp = lower;
        lower = upper;
        upper = temp;
    }

    private boolean fieldIsEmpty(EditText etText) {
        if (etText.getText().toString().trim().length() > 0)
            return false;

        return true;
    }

    public void hideKeyboard(View view) {
        InputMethodManager inputMethodManager = (InputMethodManager) getSystemService(INPUT_METHOD_SERVICE);
        inputMethodManager.hideSoftInputFromWindow(view.getWindowToken(), 0);
    }

    public void hideSoftKeyboard() {
        if (getCurrentFocus() != null) {
            InputMethodManager inputMethodManager = (InputMethodManager) getSystemService(INPUT_METHOD_SERVICE);
            inputMethodManager.hideSoftInputFromWindow(getCurrentFocus().getWindowToken(), 0);
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {

        absTopSubMenus = menu;
        // Inflate the menu items for use in the action bar
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.activity_main, menu);
        return super.onCreateOptionsMenu(menu);


    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.action_info:
                alertDialog = new AlertDialog.Builder(MainActivity.this).create();
                alertDialog.setTitle(R.string.app_name);
                alertDialog.setMessage(getResources().getString(R.string.infoText));
                alertDialog.setButton(AlertDialog.BUTTON_POSITIVE, getResources().getString(R.string.ok),
                        new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialog, int which) {
                                dialog.dismiss();
                            }
                        });
                alertDialog.show();
                return true;

            case R.id.action_about:
                Intent intent = new Intent(getApplicationContext(), AboutActivity.class);
                startActivity(intent);
                return true;

            case R.id.action_language:
                builderSingle = new AlertDialog.Builder(MainActivity.this);


                final ArrayAdapter<Language> arrayAdapter = new ArrayAdapter<>(MainActivity.this, android.R.layout.select_dialog_item);
                arrayAdapter.add(new Language(getResources().getString(R.string.de), "de"));
                arrayAdapter.add(new Language(getResources().getString(R.string.en), "en"));
                arrayAdapter.add(new Language(getResources().getString(R.string.fr), "fr"));
                arrayAdapter.add(new Language(getResources().getString(R.string.ro), "ro"));

                arrayAdapter.sort(new Comparator<Language>() {
                    @Override
                    public int compare(Language s, Language t1) {
                        return s.getLanguageName().compareTo(t1.getLanguageName());
                    }
                });


                builderSingle.setNegativeButton(R.string.cancel, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        dialog.dismiss();
                    }
                });

                builderSingle.setAdapter(arrayAdapter, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {

                        setLocale(arrayAdapter.getItem(which).getLanguageCode());


                    }
                });
                builderSingle.show();
                return true;

            default:

                return super.onOptionsItemSelected(item);

        }
    }

    private Locale myLocale;


    public void setLocale(String lang) {

        myLocale = new Locale(lang);

        saveLocale(lang);
        Locale.setDefault(myLocale);
        android.content.res.Configuration config = new android.content.res.Configuration();
        config.locale = myLocale;
        getBaseContext().getResources().updateConfiguration(config, getBaseContext().getResources().getDisplayMetrics());
        recreate();

//        if (_activityState == ActivityState.RESUMED || _activityState == ActivityState.CREATED) {
//            Intent refresh = new Intent(this, MainActivity.class);
//            finish();
//
//            startActivity(refresh);
//        }
    }

    public void saveLocale(String lang) {
        if (lang.equalsIgnoreCase(""))
            return;
        String langPref = "Language";
        SharedPreferences prefs = getSharedPreferences("CommonPrefs", Activity.MODE_PRIVATE);
        SharedPreferences.Editor editor = prefs.edit();
        editor.putString(langPref, lang);
        editor.apply();
    }

    public void loadLocale() {
        String langPref = "Language";
        SharedPreferences prefs = getSharedPreferences("CommonPrefs", Activity.MODE_PRIVATE);
        String language = prefs.getString(langPref, "");
        myLocale = new Locale(language);
        Locale.setDefault(myLocale);
        android.content.res.Configuration config = new android.content.res.Configuration();
        config.locale = myLocale;
        getBaseContext().getResources().updateConfiguration(config, getBaseContext().getResources().getDisplayMetrics());
        updateTexts();
    }

    private void updateTexts() {
        topText.setText(R.string.generateBetween);
        andText.setText(R.string.and);
        onScreenGuide.setText(R.string.shakeDevice);
        buttonToGenerate.setText(R.string.generateNumber);

    }


}
