package com.pax.android.supporthub.sdk;

import com.pax.android.supporthub.sdk.apis.SupportHubApi;
import com.pax.market.android.app.sdk.BaseApiService;
import com.pax.market.android.app.sdk.StoreSdk;
import com.pax.market.api.sdk.java.base.exception.NotInitException;


public class SupportHubSdk extends StoreSdk {

    private static volatile SupportHubSdk instance;

    private SupportHubApi supportHubApi;


    public SupportHubSdk() {
        StoreSdk.getInstance().acquireSemaphore();
        this.context = StoreSdk.getInstance().context;
        this.url = StoreSdk.getInstance().url;
        this.appKey = StoreSdk.getInstance().appKey;
        this.appSecret = StoreSdk.getInstance().appSecret;
        this.terminalSn = StoreSdk.getInstance().terminalSn;
        this.terminalModel = StoreSdk.getInstance().terminalModel;
    }

    public static SupportHubSdk getInstance() {
        if (instance == null) {
            synchronized (SupportHubSdk.class) {
                if (instance == null) {
                    instance = new SupportHubSdk();
                }
            }
        }
        return instance;
    }

    /**
     * Get supportHubApi instance
     *
     * @return supportHubApi
     * @throws NotInitException init fail
     */
    public SupportHubApi supportHubApi() throws NotInitException {
        if (terminalSn == null) {
            throw new NotInitException("Not initialized");
        }
        if (supportHubApi == null) {
            supportHubApi = new SupportHubApi(url, appKey, appSecret, terminalSn);
        }
        supportHubApi.setBaseUrl(getDcUrl(context, supportHubApi.getBaseUrl(), true));
        supportHubApi.setProxyDelegate(BaseApiService.getInstance(context));
        return supportHubApi;
    }

}
