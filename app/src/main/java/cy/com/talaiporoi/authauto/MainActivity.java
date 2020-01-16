package cy.com.talaiporoi.authauto;

import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.NavigationView;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;


import com.BoardiesITSolutions.AndroidMySQLConnector.Connection;
import com.BoardiesITSolutions.AndroidMySQLConnector.Exceptions.InvalidSQLPacketException;
import com.BoardiesITSolutions.AndroidMySQLConnector.Exceptions.MySQLConnException;
import com.BoardiesITSolutions.AndroidMySQLConnector.Exceptions.MySQLException;
import com.BoardiesITSolutions.AndroidMySQLConnector.IConnectionInterface;
import com.BoardiesITSolutions.AndroidMySQLConnector.IResultInterface;
import com.BoardiesITSolutions.AndroidMySQLConnector.ResultSet;
import com.BoardiesITSolutions.AndroidMySQLConnector.Statement;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;

import java.io.IOException;


public class MainActivity extends AppCompatActivity
        implements NavigationView.OnNavigationItemSelectedListener, OnMapReadyCallback {

    private Connection mysqlConnection;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        /*FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Snackbar.make(view, "Replace with your own action", Snackbar.LENGTH_LONG)
                        .setAction("Action", null).show();
            }
        });*/

        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(
                this, drawer, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        drawer.addDrawerListener(toggle);
        toggle.syncState();

        NavigationView navigationView = (NavigationView) findViewById(R.id.nav_view);
        navigationView.setNavigationItemSelectedListener(this);

        /*SupportMapFragment mapFragment = (SupportMapFragment) getSupportFragmentManager()
                .findFragmentById(R.id.mapView);
        mapFragment.getMapAsync(this);*/

        connectToDB();


    }

    private void login() {
        Log.d("koumis", "Executing SELECT...");
        Statement statement = mysqlConnection.createStatement();
        statement.executeQuery("SELECT DATABASE();", new IResultInterface() {
            @Override
            public void executionComplete(ResultSet resultSet) {
                //Toast toast = Toast.makeText(getApplicationContext(), "Number of rows = " + resultSet.getNumRows(), Toast.LENGTH_SHORT);
                //toast.show();
                Log.d("koumis", "Select query Num of rows = " + resultSet.getNumRows());
            }

            @Override
            public void handleInvalidSQLPacketException(InvalidSQLPacketException ex) {
                Log.e("koumis", "InvalidSQLPacketException Error");
            }

            @Override
            public void handleMySQLException(MySQLException ex) {
                Log.e("koumis", "MySQLException Error");

            }

            @Override
            public void handleIOException(IOException ex) {
                Log.e("koumis", "IOException Error");

            }

            @Override
            public void handleMySQLConnException(MySQLConnException ex) {
                Log.e("koumis", "MySQLConnException Error");

            }

            @Override
            public void handleException(Exception ex) {
                Log.e("koumis", "Exception Error");

            }

        });
    }

    private void connectToDB() {
        Log.d("koumis", "DB Connecting...");

        mysqlConnection = new Connection("10.0.2.2", "root",
                "root", 3306, "authauto", new IConnectionInterface() {
            @Override
            public void actionCompleted() {
                //You are now connected to the database
                //Toast toast = Toast.makeText(getApplicationContext(), "DB Connected", Toast.LENGTH_SHORT);
                //toast.show();
                Log.d("koumis", "DB Connected!");
                login();
            }

            @Override
            public void handleInvalidSQLPacketException(InvalidSQLPacketException e) {
                //Handle the error
            }

            @Override
            public void handleMySQLException(MySQLException e) {
                //Handle the error
            }

            @Override
            public void handleIOException(IOException e) {
                //Handle the error
            }

            @Override
            public void handleMySQLConnException(MySQLConnException e) {
                //Handle the error
                Log.e("koumis", "Connection Error");
            }

            @Override
            public void handleException(Exception e) {
                //Handle the error
            }
        });
        //The below line isn't required, however, the MySQL action, whether connecting or executing a statement
        //(basically anything that uses the this connection object) does its action in a thread, so the call
        //back you receive will still be in its thread. If you are performing a GUI operation in your callback you need to
        //switch the main thread. You can either do this yourself when required, or pass true as the first parameter
        //so that when you receive the call back it is already switched to the main thread
        mysqlConnection.returnCallbackToMainThread(true, MainActivity.this);
    }

    @Override
    public void onMapReady(GoogleMap googleMap) {
        // Add a marker in Sydney, Australia,
        // and move the map's camera to the same location.
        LatLng thessaloniki = new LatLng(40.636, 22.942);
        googleMap.addMarker(new MarkerOptions().position(thessaloniki));//.title("Marker in Sydney"))
        googleMap.moveCamera(CameraUpdateFactory.newLatLng(thessaloniki));
    }


    @Override
    public void onBackPressed() {
        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        if (drawer.isDrawerOpen(GravityCompat.START)) {
            drawer.closeDrawer(GravityCompat.START);
        } else {
            super.onBackPressed();
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    @SuppressWarnings("StatementWithEmptyBody")
    @Override
    public boolean onNavigationItemSelected(MenuItem item) {
        // Handle navigation view item clicks here.
        int id = item.getItemId();

        if (id == R.id.menu_my_vehicles) {
            Intent intent = new Intent(this, MyVehiclesActivity.class);

            startActivity(intent);
        }

        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        drawer.closeDrawer(GravityCompat.START);
        return true;
    }


    /**
     * Called when the user taps the My vehicles button

     public void viewMyVehicles(MenuItem item) {
     Intent intent = new Intent(this, MyVehiclesActivity.class);

     startActivity(intent);
     }*/
}
