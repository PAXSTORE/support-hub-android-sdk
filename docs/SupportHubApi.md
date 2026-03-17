# Upload Log File Integration

By integrating this method, you can upload log files to MaxStore.

### 1：**Check the appKey and appSecret that are the same with the web, and check again, and again, three times should be fine.**

### 2：Initialization of Sdk

Refer to the [SetUp](../README.md)

### 3：Upload Log File API

**You need to add this flag in AndroidManifest.xml so that MaxStore can recognize this application and send log file**

```
	<meta-data
        android:name="Third_Logcat"
        android:value="true"/>
```

**If you need to receive notifications from the MaxStore backend, you need to register the following service**

```
	<service android:name=".UploadLogcatService"
            android:exported="false">
            <intent-filter>
                <action android:name="com.sdk.ext.service.ACTION_TO_DO_UPLOAD_LOGCAT"/>
                <category android:name="${applicationId}"/>
            </intent-filter>
        </service>
```

com.pax.market.android.app.sdk.ext.apis.FileUploader extends BaseApi

### 1. The Constructor of FileUploader

```
    public FileUploader(String baseUrl, String appKey, String appSecret, String terminalSN) {
        super(baseUrl, appKey, appSecret, terminalSN);
    }
```

### 2. Constructor parameters description

| Parameter  | Type   | Description     |
| ---------- | ------ | --------------- |
| baseUrl    | String | The base url    |
| appKey     | String | The app key     |
| appSecret  | String | The app secret  |
| terminalSN | String | The terminal SN |

#### 3. Upload Log File API

```
public SdkObject uploadLogFile(File file, ApkLogDto apkLogDto)
```

| Parameter | Type      | Description                                           |
| --------- | --------- | ----------------------------------------------------- |
| file      | File      | printer/scanner/payment(Device type)                  |
| apkLogDto | ApkLogDto | com.pax.market.android.app.sdk.ext.apis.dto.ApkLogDto |

**com.pax.market.api.sdk.java.base.dto.SdkObject**

The base object

| Property     | Type   | Description                              |
| ------------ | ------ | ---------------------------------------- |
| businessCode | int    | The result code, the default value is -1 |
| message      | String | The result message                       |

**com.pax.market.android.app.sdk.ext.apis.dto.ApkLogDto**

The ApkLogDto

| Property    | Type   | Description                                  |
| ----------- | ------ | -------------------------------------------- |
| packageName | String | The package name of the uploaded application |
| title       | String | Title of the file content                    |
| logFileName | String | The name of the uploaded file                |