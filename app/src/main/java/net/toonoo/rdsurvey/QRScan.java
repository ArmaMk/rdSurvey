package net.toonoo.rdsurvey;

import android.Manifest;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.media.MediaPlayer;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Toast;

import me.dm7.barcodescanner.zxing.ZXingScannerView;
import com.google.zxing.Result;

public class QRScan extends AppCompatActivity implements ZXingScannerView.ResultHandler{

    final private int REQUEST_CODE_ASK_PERMISSIONS = 123;

    private ZXingScannerView zXingScannerView;

    @Override
    public void onCreate(Bundle state) {
        super.onCreate(state);
        setContentView(R.layout.activity_qrscan);
        View view = null;
        scane(view);
    }

    @Override
    public void onResume() {
        super.onResume();
        zXingScannerView.setResultHandler(this); // Register ourselves as a handler for scan results.
        zXingScannerView.startCamera();          // Start camera on resume
    }

    @Override
    public void onPause() {
        super.onPause();
        zXingScannerView.stopCamera();           // Stop camera on pause
    }

    @Override
    public void onBackPressed()
    {
        Intent intent=new Intent(this,MainActivity.class);
        startActivity(intent);
        finish();
    }

    @Override
    public void handleResult(Result result) {
        Toast.makeText(getApplicationContext(), result.getText(), Toast.LENGTH_SHORT).show();
        zXingScannerView.resumeCameraPreview(this);

        Intent intent = new Intent(QRScan.this, DetailActivity.class);
        intent.putExtra("url" , result.getText());
        startActivity(intent);
        zXingScannerView.stopCamera();
    }
    public void scane(View view) {
        int hasWriteContactsPermission = checkSelfPermission( Manifest.permission.CAMERA);
        if (hasWriteContactsPermission != PackageManager.PERMISSION_GRANTED) {
            requestPermissions(new String[]{Manifest.permission.CAMERA},
                    REQUEST_CODE_ASK_PERMISSIONS);
        }
        QRScan();
        MediaPlayer mp = MediaPlayer.create(getApplicationContext(), R.raw.sound_effects_button);
        mp.start();
    }

    private void QRScan() {
        zXingScannerView = new ZXingScannerView(getApplicationContext());
        setContentView(zXingScannerView);
        zXingScannerView.setResultHandler(this);
        zXingScannerView.startCamera();

    }


    @Override
    public void onRequestPermissionsResult(int requestCode, String[] permissions, int[] grantResults) {
        switch (requestCode) {
            case REQUEST_CODE_ASK_PERMISSIONS:
                if (grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                    // Permission Granted
                    QRScan();
                } else {
                    // Permission Denied
                    Toast.makeText(QRScan.this, "Camera Permission Denied", Toast.LENGTH_SHORT)
                            .show();
                    onBackPressed();
                }
                break;
            default:
                super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        }
    }
}
