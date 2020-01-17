package cy.com.talaiporoi.authauto;

import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.constraint.ConstraintLayout;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.widget.EditText;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;

import static android.database.DatabaseUtils.sqlEscapeString;
import static android.view.View.GONE;

public class RegisterActivity extends AppCompatActivity {

    private EditText nameText, surnameText, usernameText, emailText, passwordText;
    private String name, surname, username, email, password;
    private ConstraintLayout constraintLayout;
    private SharedPreferences sharedPref;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);
        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        FloatingActionButton fab = findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Snackbar.make(view, "Replace with your own action", Snackbar.LENGTH_LONG)
                        .setAction("Action", null).show();
            }
        });
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        constraintLayout = (ConstraintLayout) findViewById(R.id.registerConstraintLayout);


        nameText = (EditText) findViewById(R.id.nameText);
        surnameText = (EditText) findViewById(R.id.surnameText);
        emailText = (EditText) findViewById(R.id.regEmailText);
        passwordText = (EditText) findViewById(R.id.regPasswordText);
        usernameText = (EditText) findViewById(R.id.usernameText);

        sharedPref = getApplicationContext().getSharedPreferences("authauto", MODE_PRIVATE);
    }

    public void register(View v) {
        Log.d("koumis", "Register button clicked...");
        name = sqlEscapeString(nameText.getText().toString());
        name = name.substring(1, name.length() - 1);
        surname = sqlEscapeString(surnameText.getText().toString());
        surname = surname.substring(1, surname.length() - 1);
        email = sqlEscapeString(emailText.getText().toString());
        email = email.substring(1, email.length() - 1);
        password = sqlEscapeString(passwordText.getText().toString());
        password = password.substring(1, password.length() - 1);
        username = sqlEscapeString(usernameText.getText().toString());
        username = username.substring(1, username.length() - 1);

        if (TextUtils.isEmpty(name) || TextUtils.isEmpty(surname) || TextUtils.isEmpty(username) || TextUtils.isEmpty(email) || TextUtils.isEmpty(password)) {
            Log.d("koumis", "Empty fields...");
            AlertDialog alertDialog = new AlertDialog.Builder(this).create();
            alertDialog.setTitle("Empty fields");
            alertDialog.setMessage("Please make sure you have filled all the necessary information.");
            alertDialog.setButton(AlertDialog.BUTTON_NEUTRAL, "OK",
                    new DialogInterface.OnClickListener() {
                        public void onClick(DialogInterface dialog, int which) {
                            dialog.dismiss();
                        }
                    });
            alertDialog.show();
        } else {
            String url = Api.URL_CREATE_CUSTOMER;
            HashMap<String, String> params = new HashMap<>();
            params.put("name", name);
            params.put("surname", surname);
            params.put("email", email);
            params.put("username", username);
            params.put("password", password);
            Log.d("koumis", "URL: " + url + "\nParams: " + params);

            PerformNetworkRequest request = new PerformNetworkRequest(url, params, getApplicationContext());
            request.execute();
        }
    }

    //inner class to perform network request extending an AsyncTask
    private class PerformNetworkRequest extends AsyncTask<Void, Void, String> {

        //the url where we need to send the request
        String url;

        //the parameters
        HashMap<String, String> params;

        //the request code to define whether it is a GET or POST
        int requestCode;
        Context context;

        //constructor to initialize values
        PerformNetworkRequest(String url, HashMap<String, String> params, Context context) {
            this.url = url;
            this.params = params;
            this.requestCode = requestCode;
            this.context = context;
        }

        //when the task started displaying a progressbar
        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            Log.d("koumis", "Pre-execution of request...");
            //progressBar.setVisibility(View.VISIBLE); //TODO
        }


        //this method will give the response from the request
        @Override
        protected void onPostExecute(String s) {
            super.onPostExecute(s);
            Log.d("koumis", "Post-execution of request...");
            Log.d("koumis", "Code = " + requestCode);
            Log.d("koumis", s);
            //progressBar.setVisibility(GONE); //TODO
            //sharedPref = PreferenceManager.getDefaultSharedPreferences(this.context);

            try {
                JSONObject object = new JSONObject(s);
                if (!object.getBoolean("error")) {

                    //if (requestCode == CODE_GET_REQUEST) {
                    Log.d("koumis", "Registration successful!");
                    //Snackbar snackbar = Snackbar
                    //        .make(constraintLayout, "Registration Successful!", Snackbar.LENGTH_LONG);
                    //snackbar.show();

                    Log.d("koumis", "Saving login data...");
                    //Log.d("koumis", "id = " + object.getJSONObject("customer").getString("id"));
                    //sharedPref.edit().putString("id", object.getJSONObject("customer").getString("id")).apply();
                    //sharedPref.edit().putString("username", object.getJSONObject("customer").getString("username")).apply();
                    sharedPref.edit().putString("email", email).apply();
                    sharedPref.edit().putString("password", password).apply();
                    //sharedPref.edit().putBoolean("loginSuccessful", true).apply();

                    Log.d("koumis", "Showing confirmation dialog...");
                    AlertDialog alertDialog = new AlertDialog.Builder(RegisterActivity.this).create();
                    alertDialog.setTitle("Registration completed");
                    alertDialog.setMessage("Feel free to login.");
                    alertDialog.setButton(AlertDialog.BUTTON_NEUTRAL, "OK",
                            new DialogInterface.OnClickListener() {
                                public void onClick(DialogInterface dialog, int which) {
                                    dialog.dismiss();
                                }
                            });
                    alertDialog.show();

                } else {
                    Log.d("koumis", "Wrong registration...");

                    //sharedPref.edit().remove("id").apply();
                    //sharedPref.edit().remove("username").apply();
                    //sharedPref.edit().remove("email").apply();
                    //sharedPref.edit().remove("password").apply();
                    //sharedPref.edit().remove("loginSuccessful").apply();

                    AlertDialog alertDialog = new AlertDialog.Builder(RegisterActivity.this).create();
                    alertDialog.setTitle("Registration not completed");
                    alertDialog.setMessage("Email and/or username already used. Please try a different combination.");
                    alertDialog.setButton(AlertDialog.BUTTON_NEUTRAL, "OK",
                            new DialogInterface.OnClickListener() {
                                public void onClick(DialogInterface dialog, int which) {
                                    dialog.dismiss();
                                }
                            });
                    alertDialog.show();
                }
            } catch (JSONException e) {
                e.printStackTrace();
            }
        }

        //the network operation will be performed in background
        @Override
        protected String doInBackground(Void... voids) {
            RequestHandler requestHandler = new RequestHandler();

            //if (requestCode == CODE_POST_REQUEST)
            return requestHandler.sendPostRequest(url, params);


            //if (requestCode == CODE_GET_REQUEST)
            //return requestHandler.sendGetRequest(url);

            //return null;
        }
    }

}
