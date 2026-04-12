package com.pax.android.supporthub.app;

import android.app.Application;
import android.os.RemoteException;
import android.util.Log;
import android.widget.Toast;

import com.pax.market.android.app.sdk.BaseApiService;
import com.pax.market.android.app.sdk.StoreSdk;

public class BaseApplication extends Application {

    private static final String TAG = BaseApplication.class.getSimpleName();
    //todo make sure to replace with your own app's appkey and appsecret
    private static final String appkey = "";
    private static final String appSecret = "";

    @Override
    public void onCreate() {
        super.onCreate();
        initStoreSdk();

    }

    private void initStoreSdk() {
        //todo 1. Init AppKey，AppSecret and SN, make sure the appkey and appSecret is corret.
        StoreSdk.getInstance().init(getApplicationContext(), appkey, appSecret, new BaseApiService.Callback() {
            @Override
            public void initSuccess() {
                Log.i(TAG, "initSuccess.");
            }

            @Override
            public void initFailed(RemoteException e) {
                Log.i(TAG, "initFailed: " + e.getMessage());
                Toast.makeText(getApplicationContext(), "Cannot get API URL from STORE client," +
                        " Please install STORE client first.", Toast.LENGTH_LONG).show();
            }
        });
    }

}
