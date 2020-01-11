package com.example.ecommerce.navigable;

import androidx.appcompat.app.AppCompatActivity;
import me.dm7.barcodescanner.zxing.ZXingScannerView;
import com.google.zxing.Result;

import android.content.Intent;
import android.os.Bundle;

public class ScanCodeActivity extends AppCompatActivity implements ZXingScannerView.ResultHandler{

    ZXingScannerView ScannerView;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        ScannerView=new ZXingScannerView(this);

        setContentView(ScannerView);
    }

    @Override
    public void handleResult(Result result)
    {

    }

    @Override
    protected void onPause()
    {

        super.onPause();

        ScannerView.stopCamera();
    }

    @Override
    protected void onResume()
    {
        super.onResume();

        ScannerView.setResultHandler(this);
        ScannerView.startCamera();
    }
}
