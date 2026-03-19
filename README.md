# Support Hub Android SDK


### Before integrating Support Hub Android SDK, you need to integrate Android SDK first. Below is the integrated address.

https://github.com/PAXSTORE/paxstore-3rd-app-android-sdk?tab=readme-ov-file#set-up


This is the extention for the android sdk, which includes some special features that not exist in Android Sdk. The services currently include the following features:

1. [SupportHub Tickets](docs/SupportHubApi.md)

## Requirements
**Android SDK version**
>SDK 21 or higher, depending on the terminal's paydroid version.

**Gradle's and Gradle plugin's version**
>Gradle version 8.11.1 or higher  
>Gradle plugin version 8.9+ or higher

**Support Hub Android SDK needed**

Add the dependency

```
    implementation 'com.whatspos.sdk:paxstore-3rd-app-android-sdk:10.1.0'
    implementation 'com.whatspos.sdk:paxstore-3rd-app-android-sdk-ext:10.1.0'
```

## Permissions
Support Hub Android SDK need the following permissions, please add them in AndroidManifest.xml.

`<uses-permission android:name="android.permission.INTERNET" />`<br>

## ProGuard
If you are using [ProGuard](https://www.guardsquare.com/en/products/proguard/manual) in your project add the following lines to your configuration:

Please check the our [proguard-rules.pro](https://github.com/PAXSTORE/paxstore-3rd-app-android-support-hub-sdk/blob/master/app/proguard-rules.pro)


https://github.com/PAXSTORE/paxstore-3rd-app-android-sdk/wiki/FAQ)

## License

See the [Apache 2.0 license](https://github.com/PAXSTORE/paxstore-3rd-app-android-sdk/blob/master/LICENSE) file for details.

    Copyright © 2019 Shenzhen Zolon Technology Co., Ltd. All Rights Reserved.
    
    Licensed under the Apache License, Version 2.0 (the "License");
    you may not use this file except in compliance with the License.
    You may obtain a copy of the License at following link.
    
         http://www.apache.org/licenses/LICENSE-2.0
    
    Unless required by applicable law or agreed to in writing, software
    distributed under the License is distributed on an "AS IS" BASIS,
    WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
    See the License for the specific language governing permissions and
    limitations under the License.
