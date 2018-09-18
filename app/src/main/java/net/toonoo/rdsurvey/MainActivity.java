package net.toonoo.rdsurvey;

import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.crashlytics.android.Crashlytics;
import com.google.firebase.auth.FirebaseAuth;

public class MainActivity extends AppCompatActivity implements ConnectivityReceiver.ConnectivityReceiverListener {

    final private int REQUEST_CODE_ASK_PERMISSIONS = 123;
    private EditText etEmail;
    private EditText etPassword;
    private FirebaseAuth firebaseAuth;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        this.getSupportActionBar().hide();
        checkConnection();
        logUser();

        etEmail = (EditText) findViewById( R.id.etEmail );
        etPassword = (EditText) findViewById( R.id.etPassword );



        Button mShowDialog = (Button) findViewById(R.id.signin);
        mShowDialog.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                AlertDialog.Builder mBuilder = new AlertDialog.Builder(MainActivity.this);
                View mView = getLayoutInflater().inflate(R.layout.dialog_login, null);
                final EditText mEmail = (EditText) mView.findViewById(R.id.etEmail);
                final EditText mPassword = (EditText) mView.findViewById(R.id.etPassword);

                Button mLogin = (Button) mView.findViewById(R.id.btnLogin);
                mBuilder.setView(mView);
                final AlertDialog dialog = mBuilder.create();
                dialog.show();

                mLogin.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        if(!mEmail.getText().toString().isEmpty() && !mPassword.getText().toString().isEmpty()){
                            Toast.makeText(MainActivity.this,
                                    R.string.success_login_msg,
                                    Toast.LENGTH_SHORT).show();
                            dialog.dismiss();
                        }else{
                            Toast.makeText(MainActivity.this,
                                    R.string.error_login_msg,
                                    Toast.LENGTH_SHORT).show();
                        }
                    }
                });
            }
        });
    }

//    public void btnUserLogin_Click(View v) {
//        final ProgressDialog progressDialog = ProgressDialog.show(MainActivity.this, "Please wait...", "Proccessing...", true);
//
//        (firebaseAuth.signInWithEmailAndPassword(etEmail.getText().toString(), etPassword.getText().toString()))
//                .addOnCompleteListener(new OnCompleteListener<AuthResult>() {
//                    @Override
//                    public void onComplete(@NonNull Task<AuthResult> task) {
//                        progressDialog.dismiss();
//
//                        if (task.isSuccessful()) {
//                            Toast.makeText(MainActivity.this, "Login successful", Toast.LENGTH_LONG).show();
//                            Intent i = new Intent(MainActivity.this, map.class);
//                            i.putExtra("Email", firebaseAuth.getCurrentUser().getEmail());
//                            startActivity(i);
//                        } else {
//                            Log.e("ERROR", task.getException().toString());
//                            Toast.makeText(MainActivity.this, task.getException().getMessage(), Toast.LENGTH_LONG).show();
//
//                        }
//                    }
//                });
//    }

    private void logUser() {
        Crashlytics.setUserIdentifier("12345");
        Crashlytics.setUserEmail("user@fabric.io");
        Crashlytics.setUserName("Test User");
    }

    private void checkConnection() {
        boolean isConnected = ConnectivityReceiver.isConnected();
        showSnack(isConnected);
    }
    private void showSnack(boolean isConnected) {
        if (isConnected) {
            Toast.makeText(MainActivity.this, "เชื่อมต่อสำเร็จ", Toast.LENGTH_LONG).show();
            Button btn = (Button) findViewById(R.id.btnScan);
            btn.setEnabled(true);

        } else {
            AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(this);
            alertDialogBuilder.setTitle("Internet Connection Status");
            alertDialogBuilder.setMessage("Internet Loss");
            Toast.makeText(MainActivity.this, "No Internet Connection", Toast.LENGTH_LONG).show();
            alertDialogBuilder.setPositiveButton("Ok", new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface arg0, int arg1) {
                    Toast.makeText(MainActivity.this,"No Internet Connection",Toast.LENGTH_LONG).show();
                }
            });
            AlertDialog alertDialog = alertDialogBuilder.create();
            alertDialog.show();
            Button btn = (Button) findViewById(R.id.btnScan);
            btn.setEnabled(false);
        }
    }

    @Override
    protected void onResume() {
        super.onResume();
        MyApplication.getInstance().setConnectivityListener(this);
    }

    public void onNetworkConnectionChanged(boolean isConnected) {
        showSnack(isConnected);
    }


    public void  loadWebPage(View v){
        Intent intent = new Intent(this, map.class);
        startActivity(intent);
    }

    public void  btn(View v){
        Intent intent = new Intent(this, QRScan.class);
        startActivity(intent);
    }

    public void onBackPressed() {
        Intent startMain = new Intent(Intent.ACTION_MAIN);
        startMain.addCategory(Intent.CATEGORY_HOME);
        startMain.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        startActivity(startMain);
        finish();
    }
}
