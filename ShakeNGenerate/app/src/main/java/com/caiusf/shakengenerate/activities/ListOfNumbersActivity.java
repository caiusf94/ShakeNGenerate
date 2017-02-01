package com.caiusf.shakengenerate.activities;

import android.app.Activity;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.hardware.Sensor;
import android.hardware.SensorManager;
import android.os.Bundle;
import android.support.v4.app.NavUtils;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.TextView;

import com.caiusf.shakengenerate.R;
import com.caiusf.shakengenerate.adapters.CustomAdapter;
import com.caiusf.shakengenerate.listeners.ShakeEventListener;
import com.caiusf.shakengenerate.models.Language;
import com.caiusf.shakengenerate.utils.number.RandomNumberGeneration;
import com.caiusf.shakengenerate.utils.toast.ToastDisplayer;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.Locale;
import java.util.Random;

/**
 * Created by caius.florea on 03-Jan-17.
 */


public class ListOfNumbersActivity extends AppCompatActivity {

    private SensorManager mSensorManager;

    private ShakeEventListener mSensorListener;

    private ListView list;

    private int lower;
    private int upper;

    private int maxHowMany;

    private boolean isChecked;

    ListOfNumbersActivity thisContext;

    TextView textToDisplayTop;
    TextView textToDisplayBottom;

    private ArrayList<String> indexArrayList;
    private ArrayList<String> numbersArrayList;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_list);



        thisContext = this;


        mSensorManager = (SensorManager) getSystemService(Context.SENSOR_SERVICE);
        mSensorListener = new ShakeEventListener();

        textToDisplayTop = (TextView) findViewById(R.id.topTextListActivity);
        textToDisplayBottom = (TextView) findViewById(R.id.bottomTextListActivity);

        indexArrayList = new ArrayList<>();
        numbersArrayList = new ArrayList<>();

        textToDisplayBottom.setText(R.string.shakeToGenerateOtherList);

        loadLocale();


//Get the bundle
        Bundle bundle = getIntent().getExtras();

        //Extract the data…
        final int howMany = bundle.getInt("howManyNumbers");


        lower = bundle.getInt("lower");
        upper = bundle.getInt("upper");
        isChecked = bundle.getBoolean("isChecked");

        if (!isChecked) {         //generate even repeating values

            if (howMany == 1) {
                textToDisplayTop.setText(getResources().getString(R.string.generatedOne) + " " + howMany + " " + getResources().getString(R.string.numberBetween) + " " + lower + " " + getResources().getString(R.string.and) + " " + upper + " " + getResources().getString(R.string.lastVerb));
            } else {
                textToDisplayTop.setText(getResources().getString(R.string.generatedMultiple) + " " + howMany + " " + getResources().getString(R.string.numbersBetween) + " " + lower + " " + getResources().getString(R.string.and) + " " + upper + " " + getResources().getString(R.string.lastVerb));
            }

            for (int i = 0; i < howMany; i++) {
                indexArrayList.add(Integer.toString(i + 1) + ".");

                Random random = new Random();
                numbersArrayList.add(Integer.toString(random.nextInt(upper - lower + 1) + lower));
            }
        }
        else {                //generate only distinct values

            int maxPossible = Math.abs(upper - lower) + 1;
            maxHowMany = howMany;

            if(howMany > maxPossible){
                maxHowMany = maxPossible;
                if(maxHowMany == 1) {
                    ToastDisplayer.displayLongToast(ListOfNumbersActivity.this, getResources().getString(R.string.onlyOne) + " " +  maxPossible + " " + getResources().getString(R.string.uniqueNumberBetween) + " " + lower + " " + getResources().getString(R.string.and) + " " + upper);
                }else{
                    ToastDisplayer.displayLongToast(ListOfNumbersActivity.this, getResources().getString(R.string.onlyMultiple) + " " +  maxPossible + " " + getResources().getString(R.string.uniqueNumbersBetween) + " " + lower + " " + getResources().getString(R.string.and) + " " + upper);
                }
            }

            if (maxHowMany == 1) {
                textToDisplayTop.setText(getResources().getString(R.string.generatedOne) + " " + maxHowMany + " " + getResources().getString(R.string.uniqueNumberBetween) + " " + lower + " " + getResources().getString(R.string.and) + " " + upper + " " + getResources().getString(R.string.lastVerb));
            } else {
                textToDisplayTop.setText(getResources().getString(R.string.generatedMultiple) + " " + maxHowMany + " " + getResources().getString(R.string.uniqueNumbersBetween) + " " + lower + " " + getResources().getString(R.string.and) + " " + upper + " " + getResources().getString(R.string.lastVerb));
            }

            while (numbersArrayList.size() < maxHowMany) {

                String number = Integer.toString(RandomNumberGeneration.generateRandomNumber(lower,upper));

                if (!numbersArrayList.contains(number)) {
                    indexArrayList.add((numbersArrayList.size() + 1) + ".");
                    numbersArrayList.add(number);
                }


            }

        }


        list = (ListView) findViewById(R.id.listView);


        list.setAdapter(new CustomAdapter(this, indexArrayList, numbersArrayList, lower, upper, isChecked));


        mSensorListener.setOnShakeListener(new ShakeEventListener.OnShakeListener() {
                                               @Override
                                               public void onShake() {
                                                   numbersArrayList.clear();


                                                   list = (ListView) findViewById(R.id.listView);

                                                   if (!isChecked) {

                                                       for (int i = 0; i < howMany; i++) {
                                                           numbersArrayList.add(Integer.toString(RandomNumberGeneration.generateRandomNumber(lower,upper)));
                                                       }
                                                   } else {

                                                       while (numbersArrayList.size() < maxHowMany) {
                                                           String number = Integer.toString(RandomNumberGeneration.generateRandomNumber(lower,upper));

                                                           if (!numbersArrayList.contains(number)) {
                                                               numbersArrayList.add(number);
                                                           }


                                                       }

                                                   }


                                                   // Here, you set the data in your ListView
                                                   list.setAdapter(new CustomAdapter(thisContext, indexArrayList, numbersArrayList, lower, upper, isChecked));

                                               }
                                           }

        );
        // Get a support ActionBar corresponding to this toolbar
        ActionBar ab = getSupportActionBar();

        // Enable the Up button
        ab.setDisplayHomeAsUpEnabled(true);
        ab.setDisplayShowHomeEnabled(true);

    }

    @Override
    protected void onResume() {
        super.onResume();
        mSensorManager.registerListener(mSensorListener,
                mSensorManager.getDefaultSensor(Sensor.TYPE_ACCELEROMETER),
                SensorManager.SENSOR_DELAY_UI);

    }

    @Override
    protected void onPause() {
        mSensorManager.unregisterListener(mSensorListener);
        super.onPause();
    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu items for use in the action bar
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.activity_main, menu);
        return super.onCreateOptionsMenu(menu);
    }

    AlertDialog.Builder builderSingle;

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.action_info:
                AlertDialog alertDialog = new AlertDialog.Builder(ListOfNumbersActivity.this).create();
                alertDialog.setTitle(R.string.app_name);
                alertDialog.setMessage(getResources().getString(R.string.listInfoText));
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
                builderSingle = new AlertDialog.Builder(ListOfNumbersActivity.this);


                final ArrayAdapter<Language> arrayAdapter = new ArrayAdapter<>(ListOfNumbersActivity.this, android.R.layout.select_dialog_item);
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



            case android.R.id.home:
                NavUtils.navigateUpFromSameTask(this);
                return true;

            default:
                // If we got here, the user's action was not recognized.
                // Invoke the superclass to handle it.
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

        updateTexts();



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
        textToDisplayBottom.setText(R.string.shakeToGenerateOtherList);
    }


}
