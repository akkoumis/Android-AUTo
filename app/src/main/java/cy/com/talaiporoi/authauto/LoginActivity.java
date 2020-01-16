package cy.com.talaiporoi.authauto;

import android.content.SharedPreferences;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.constraint.ConstraintLayout;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.Toast;

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
        constraintLayout= (ConstraintLayout)findViewById(R.id.loginConstraintLayout);
        progressBar = (ProgressBar) findViewById(R.id.progressBar);
        loginButton = (Button) findViewById(R.id.loginButton);
        registerButton = (Button) findViewById(R.id.registerButton);
        emailText = (EditText) findViewById(R.id.emailText);
        passwordText = (EditText) findViewById(R.id.passwordText);

    }

    public void login(View v) {
        String emailString = sqlEscapeString(emailText.getText().toString()),
                passwordString = sqlEscapeString(passwordText.getText().toString());
        emailString = emailString.substring(1, emailString.length() - 1);
        passwordString = passwordString.substring(1, passwordString.length() - 1);

        String url = Api.URL_READ_LOGIN + "&email=" + emailString + "&password=" + passwordString;
        Log.d("koumis", "URL: " + url);
        PerformNetworkRequest request = new PerformNetworkRequest(url, null, CODE_GET_REQUEST);
        request.execute();
    }

    public void register(View v) {

    }

    //inner class to perform network request extending an AsyncTask
    private class PerformNetworkRequest extends AsyncTask<Void, Void, String> {

        //the url where we need to send the request
        String url;

        //the parameters
        HashMap<String, String> params;

        //the request code to define whether it is a GET or POST
        int requestCode;

        //constructor to initialize values
        PerformNetworkRequest(String url, HashMap<String, String> params, int requestCode) {
            this.url = url;
            this.params = params;
            this.requestCode = requestCode;
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

            try {
                SharedPreferences sharedPref;
                sharedPref = getSharedPreferences("authauto", MODE_PRIVATE);
                JSONObject object = new JSONObject(s);
                if (!object.isNull("customer")) {
                    //if (requestCode == CODE_GET_REQUEST) {
                    Log.d("koumis", "Login successful!");
                    Snackbar snackbar = Snackbar
                            .make(constraintLayout, "Login Successful!", Snackbar.LENGTH_LONG);
                    snackbar.show();

                    sharedPref.edit().putString("id", object.getString("id")).commit();
                    sharedPref.edit().putString("username", object.getString("username")).commit();
                    sharedPref.edit().putString("email", object.getString("email")).commit();
                    sharedPref.edit().putString("password", object.getString("password")).commit();
                    //}
                    //refreshing the herolist after every operation
                    //so we get an updated list
                    //we will create this method right now it is commented
                    //because we haven't created it yet
                    //refreshHeroList(object.getJSONArray("heroes"));
                } else {
                    Log.d("koumis", "Wrong login...");

                    sharedPref.edit().remove("id").commit();
                    sharedPref.edit().remove("username").commit();
                    sharedPref.edit().remove("email").commit();
                    sharedPref.edit().remove("password").commit();
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
