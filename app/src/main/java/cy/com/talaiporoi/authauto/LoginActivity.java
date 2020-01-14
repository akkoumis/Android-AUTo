package cy.com.talaiporoi.authauto;

import android.os.Bundle;

import com.BoardiesITSolutions.AndroidMySQLConnector.Connection;
import com.BoardiesITSolutions.AndroidMySQLConnector.Exceptions.InvalidSQLPacketException;
import com.BoardiesITSolutions.AndroidMySQLConnector.Exceptions.MySQLConnException;
import com.BoardiesITSolutions.AndroidMySQLConnector.Exceptions.MySQLException;
import com.BoardiesITSolutions.AndroidMySQLConnector.IConnectionInterface;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.android.material.snackbar.Snackbar;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import android.view.View;
import android.widget.Toast;

import java.io.IOException;

public class LoginActivity extends AppCompatActivity {

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

        connectToDB();


    }

    private void connectToDB(){
        Connection mysqlConnection = new Connection("127.0.0.1", "root",
                "root", 3306, "authauto", new IConnectionInterface()
        {
            @Override
            public void actionCompleted()
            {
                //You are now connected to the database
                Toast toast = Toast.makeText(getApplicationContext(), "DB Connected", Toast.LENGTH_SHORT);
                toast.show();

            }

            @Override
            public void handleInvalidSQLPacketException(InvalidSQLPacketException e)
            {
                //Handle the error
            }

            @Override
            public void handleMySQLException(MySQLException e)
            {
                //Handle the error
            }

            @Override
            public void handleIOException(IOException e)
            {
                //Handle the error
            }

            @Override
            public void handleMySQLConnException(MySQLConnException e)
            {
                //Handle the error
            }

            @Override
            public void handleException(Exception e)
            {
                //Handle the error
            }
        });
        //The below line isn't required, however, the MySQL action, whether connecting or executing a statement
        //(basically anything that uses the this connection object) does its action in a thread, so the call
        //back you receive will still be in its thread. If you are performing a GUI operation in your callback you need to
        //switch the main thread. You can either do this yourself when required, or pass true as the first parameter
        //so that when you receive the call back it is already switched to the main thread
        mysqlConnection.returnCallbackToMainThread(true, LoginActivity.this);
    }

}
