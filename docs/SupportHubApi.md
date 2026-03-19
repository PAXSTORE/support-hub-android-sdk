# Support Hub Api Integration

By integrating this method, you can upload tickets to MaxStore.

### 1：**Check the appKey and appSecret that are the same with the web, and check again, and again, three times should be fine.**

### 2：Initialization of Sdk

Refer to the [SetUp](../README.md)

### 3：Initialization of app

**You need to add this flag in AndroidManifest.xml so that MaxStore can recognize this application and send log file**

```
<meta-data 
			android:name="PAXVAS_SupportHub"
            android:value="true"/>
```

com.pax.market.android.app.sdk.ext.apis.FileUploader extends BaseApi

### 1. The Constructor of SupportHubApi

```
    public SupportHubApi(String baseUrl, String appKey, String appSecret, String terminalSN) {
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

### Support Hub Api

**com.pax.market.api.sdk.java.base.dto.SdkObject**--The base object

| Property     | Type   | Description                              |
| ------------ | ------ | ---------------------------------------- |
| businessCode | int    | The result code, the default value is -1 |
| message      | String | The result message                       |

| businessCode | Description                                                  |
| ------------ | ------------------------------------------------------------ |
| 113          | Your request is invalid, please try again or contact marketplace administrator |
| 100001       | You do not have permission to report faults. Please contact your administrator |
| 100002       | Ticket issue title is mandatory                              |
| 100003       | Ticket issue title is too long                               |
| 100004       | Ticket issue description is mandatory                        |
| 100005       | Ticket issue description is too long                         |
| 100006       | Email is invalid                                             |
| 100007       | Phone No. is invalid                                         |
| 100008       | Maximum allowed photo size for upload is 5 MB                |
| 100009       | Photo type is invalid                                        |
| 100010       | Ticket contact name is too long                              |
| 17101        | The format of the uploaded image file does not meet the requirements. |
| 17102        | The number of uploaded pictures exceeded five.               |

#### 1. Upload Ticket API

```
public TicketNumberDto uploadTicket(UploadTicketDto uploadTicketDto, List<File> imageFile, Context context, boolean needCompress)
```

| Parameter       | Type            | Description                                                  |
| --------------- | --------------- | ------------------------------------------------------------ |
| uploadTicketDto | UploadTicketDto | UploadTicket dto                                             |
| imageFile       | List<File>      | Send attached images to the ticket -- Images should not be too large. If they exceed 5MB, they must be compressed. The API only supports single images up to 5MB in size. |
| context         | Context         | Android context                                              |
| needCompress    | boolean         | Do the attached images on the work order need to be compressed? |

**com.pax.android.supporthub.sdk.apis.dto.UploadTicketDto**

| Property         | Type   | Description                 |
| ---------------- | ------ | --------------------------- |
| issueTitle       | String | Ticket issue title          |
| issueDescription | String | Ticket issue describe       |
| contactName      | String | Ticket contact person name  |
| email            | String | Ticket E-mail address       |
| phone            | String | Ticket contact phone number |



**com.pax.android.supporthub.sdk.apis.dto.TicketNumberDto**

The TicketNumberDto

| Property     | Type   | Description                                                  |
| ------------ | ------ | ------------------------------------------------------------ |
| ticketNumber | String | If the submission is successful, return the ticket number; otherwise, return an SdkObject Included error code and message. |

#### 2. Get Support Phone API

```
public SupportPhoneDto getSupportPhone()
```

com.pax.android.supporthub.sdk.apis.dto.SupportPhoneDto

| Property | Type   | Description                                                  |
| -------- | ------ | ------------------------------------------------------------ |
| number   | String | If the retrieval is successful, return the support number; otherwise, return the err SDK object. |



#### 3. Get History Ticket List API

```
public PageInfoDto<HistoryTicketDto> getHistoryTicketList(String search, String status, Long startTime,Long endTime, int startOffset, int limit) 
```

| Parameter   | Type   | Description                                                  |
| ----------- | ------ | ------------------------------------------------------------ |
| search      | String | Search keyword (id、ticket number 、title and  description)  |
| status      | String | Ticket status filter: **O** (Open), **I** (In Progress), **H** (On Hold), **C** (Closed); pass null or empty string for all statuses |
| startTime   | Long   | Filter by creation start time (Unix timestamp in milliseconds) |
| endTime     | Long   | Filter by creation end time (Unix timestamp in milliseconds) |
| startOffset | int    | Pagination start offset (0-based)                            |
| limit       | int    | Number of records per page                                   |

**com.pax.android.supporthub.sdk.apis.dto.PageInfoDto**

| Property   | Type    | Description                                               |
| ---------- | ------- | --------------------------------------------------------- |
| list       | List<T> | Data list (generic type, contains paginated data records) |
| totalCount | long    | Total number of records                                   |

**com.pax.android.supporthub.sdk.apis.dto.HistoryTicketDto**

| Property          | Type   | Description                                                  |
| ----------------- | ------ | ------------------------------------------------------------ |
| id                | Long   | Ticket unique id                                             |
| ticketNumber      | String | Ticket number                                                |
| ticketTitle       | String | Ticket title                                                 |
| ticketDescription | String | Ticket description                                           |
| createdDate       | Long   | Ticket creation timestamp (Unix timestamp in milliseconds)   |
| status            | String | Ticket status: **O** (Open), **I** (In Progress), **H** (On Hold), **C** (Closed) |



#### 4. Get Ticket Detail API

```
public SupportHubTicketDetailInfo getTicketDetail(Long ticketId)
```

| Parameter | Type | Description      |
| --------- | ---- | ---------------- |
| ticketId  | Long | Ticket unique id |



**com.pax.android.supporthub.sdk.apis.dto.SupportHubTicketDetailInfo**

| Property         | Type                     | Description                                                  |
| ---------------- | ------------------------ | ------------------------------------------------------------ |
| id               | Long                     | Ticket unique identifier                                     |
| serialNo         | String                   | Device serial number                                         |
| source           | String                   | Ticket source:<br />"T"-Terminal Report<br />"R"-Reseller   Escalation<br />"M"-Market Escalation<br />"I"-Manual Creation |
| createdDate      | Long                     | Ticket creation timestamp (Unix timestamp in milliseconds)   |
| updatedDate      | Long                     | Ticket last update timestamp (Unix timestamp in milliseconds) |
| status           | String                   | Ticket status: **O** (Open), **I** (In Progress), **H** (On Hold), **C** (Closed) |
| ticketNumber     | String                   | Ticket number                                                |
| issueTitle       | String                   | Ticket issue title                                           |
| issueDescription | String                   | Ticket issue description                                     |
| screenshot1      | String                   | Screenshot 1 URL                                             |
| screenshot2      | String                   | Screenshot 2 URL                                             |
| screenshot3      | String                   | Screenshot 3 URL                                             |
| screenshot4      | String                   | Screenshot 4 URL                                             |
| screenshot5      | String                   | Screenshot 5 URL                                             |
| contactName      | String                   | Contact person name                                          |
| email            | String                   | Contact email address                                        |
| phone            | String                   | Contact phone number                                         |
| activityInfoList | List<TicketActivityInfo> | Ticket activity history list                                 |

**com.pax.android.supporthub.sdk.apis.dto.TicketActivityInfo**

| Property          | Type         | Description                                                  |
| ----------------- | ------------ | ------------------------------------------------------------ |
| id                | Long         | Activity record ID                                           |
| oldStatus         | String       | Previous status                                              |
| newStatus         | String       | New status                                                   |
| oldPriority       | String       | Previous priority                                            |
| newPriority       | String       | New priority                                                 |
| oldAssignee       | UserInfo     | Previous assignee                                            |
| newAssignee       | UserInfo     | New assignee                                                 |
| actionType        | int          | Action type: **1** Create, **2** Edit, **3** Assign, **4** Status, **5** Priority, **6** Comment, **7** Attachment, **8** Upgrade |
| content           | String       | Content: title, edit field, new ticket number, or comment content |
| userInfo          | UserInfo     | Operator user info                                           |
| screenshotFileIds | List<String> | Attached screenshot url list                                 |
| createdDate       | Long         | Activity timestamp (Unix timestamp in milliseconds)          |

| Action Type | Description( return TicketActivityInfo Property) |
| ----------- | ------------------------------------------------ |
| 1           | Create ticket      --- content                   |
| 2           | Edit ticket           --  content                |
| 3           | Assign handler  -- oldAssignee, newAssignee      |
| 4           | Status change   -- oldStatus, newStatus          |
| 5           | Priority change -- oldPriority, newPriority      |
| 6           | Comment          -- content                      |
| 7           | Attachment       -- screenshotFileIds            |
| 8           | Upgrade            -- content                    |

**com.pax.android.supporthub.sdk.apis.dto.UserInfo**

| Property | Type   | Description        |
| -------- | ------ | ------------------ |
| id       | long   | User unique id     |
| name     | String | User name          |
| nickName | String | User nickname      |
| email    | String | User email address |