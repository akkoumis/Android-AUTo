package cy.com.talaiporoi.authauto;

import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.AsyncTask;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.support.constraint.ConstraintLayout;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ProgressBar;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;

import static android.database.DatabaseUtils.sqlEscapeString;
import static android.view.View.GONE;

public class LoginActivity extends AppCompatActivity {

    private static final int CODE_GET_REQUEST = 1024;
    private static final int CODE_POST_REQUEST = 1025;

    private ProgressBar progressBar;
    private Button loginButton, registerButton;
    private EditText emailText, passwordText;
    private ConstraintLayout constraintLayout;
    private SharedPreferences sharedPref;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        /*FloatingActionButton fab = findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Snackbar.make(view, "Replace with your own action", Snackbar.LENGTH_LONG)
                        .setAction("Action", null).show();
            }
        });*/
        constraintLayout = (ConstraintLayout) findViewById(R.id.loginConstraintLayout);
        progressBar = (ProgressBar) findViewById(R.id.progressBar);
        loginButton = (Button) findViewById(R.id.loginButton);
        registerButton = (Button) findViewById(R.id.registerButton);
        emailText = (EditText) findViewById(R.id.emailText);
        passwordText = (EditText) findViewById(R.id.passwordText);

        sharedPref = getApplicationContext().getSharedPreferences("authauto", MODE_PRIVATE);
        if(sharedPref.contains("email"))
            emailText.setText(sharedPref.getString("email",""));
        if(sharedPref.contains("password"))
            passwordText.setText(sharedPref.getString("password",""));

    }

    public void login(View v) {
        String emailString = sqlEscapeString(emailText.getText().toString()),
                passwordString = sqlEscapeString(passwordText.getText().toString());
        emailString = emailString.substring(1, emailString.length() - 1);
        passwordString = passwordString.substring(1, passwordString.length() - 1);

        String url = Api.URL_READ_LOGIN + "&email=" + emailString + "&password=" + passwordString;
        Log.d("koumis", "URL: " + url);
        PerformNetworkRequest request = new PerformNetworkRequest(url, null, CODE_GET_REQUEST, getApplicationContext());
        request.execute();


    }

    public void registerActivity(View v) {
        Log.d("koumis", "Starting Register Activity...");
        Intent intent = new Intent(this, RegisterActivity.class);
        //intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);

        startActivity(intent);
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
        PerformNetworkRequest(String url, HashMap<String, String> params, int requestCode, Context context) {
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
            progressBar.setVisibility(View.VISIBLE);
        }


        //this method will give the response from the request
        @Override
        protected void onPostExecute(String s) {
            super.onPostExecute(s);
            Log.d("koumis", "Post-execution of request...");
            Log.d("koumis", "Code = " + requestCode);
            Log.d("koumis", s);
            progressBar.setVisibility(GONE);
            //sharedPref = PreferenceManager.getDefaultSharedPreferences(this.context);

            try {
                JSONObject object = new JSONObject(s);
                if (!object.isNull("customer")) {

                    //if (requestCode == CODE_GET_REQUEST) {
                    Log.d("koumis", "Login successful!");
                    Snackbar snackbar = Snackbar
                            .make(constraintLayout, "Login Successful!", Snackbar.LENGTH_LONG);
                    snackbar.show();

                    Log.d("koumis", "Saving user data...");
                    //Log.d("koumis", "id = " + object.getJSONObject("customer").getString("id"));
                    sharedPref.edit().putString("id", object.getJSONObject("customer").getString("id")).apply();
                    sharedPref.edit().putString("username", object.getJSONObject("customer").getString("username")).apply();
                    sharedPref.edit().putString("email", object.getJSONObject("customer").getString("email")).apply();
                    sharedPref.edit().putString("password", object.getJSONObject("customer").getString("password")).apply();
                    sharedPref.edit().putBoolean("loginSuccessful", true).apply();

                    Log.d("koumis", "Starting Main Activity...");
                    Intent intent = new Intent(context, MainActivity.class);
                    intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                    //intent.putExtra("id", object.getString("id"));
                    //intent.putExtra("username", object.getString("username"));
                    //intent.putExtra("email", object.getString("email"));
                    //intent.putExtra("password", object.getString("password"));
                    getApplicationContext().startActivity(intent);

                } else {
                    Log.d("koumis", "Wrong login...");

                    sharedPref.edit().remove("id").apply();
                    sharedPref.edit().remove("username").apply();
                    sharedPref.edit().remove("email").apply();
                    sharedPref.edit().remove("password").apply();
                    sharedPref.edit().remove("loginSuccessful").apply();

                    AlertDialog alertDialog = new AlertDialog.Builder(LoginActivity.this).create();
                    alertDialog.setTitle("Login not completed");
                    alertDialog.setMessage("Please check the email address and the password. Then, try again.");
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

            if (requestCode == CODE_POST_REQUEST)
                return requestHandler.sendPostRequest(url, params);


            if (requestCode == CODE_GET_REQUEST)
                return requestHandler.sendGetRequest(url);

            return null;
        }
    }

}
