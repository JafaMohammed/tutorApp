package com.mojaafar.tutorapp.util;

/**
 *
 */

public class AppConstants {

    /**
     * Connection timeout duration
     */
    public static final int CONNECT_TIMEOUT = 60 * 1000;
    /**
     * Connection Read timeout duration
     */
    public static final int READ_TIMEOUT = 60 * 1000;
    /**
     * Connection write timeout duration
     */
    public static final int WRITE_TIMEOUT = 60 * 1000;
    /**
     * Base URL
     */
    public static final String BASE_URL = "https://api.safaricom.co.ke/";
    /**
     * global topic to receive app wide push notifications
     */
    public static final String TOPIC_GLOBAL = "global";

    // broadcast receiver intent filters
    public static final String REGISTRATION_COMPLETE = "registrationComplete";
    public static final String PUSH_NOTIFICATION = "pushNotification";

    // id to handle the notification in the notification tray
    public static final int NOTIFICATION_ID = 100;
    public static final int NOTIFICATION_ID_BIG_IMAGE = 101;
    public static final String SHARED_PREF = "ah_firebase";

    //STKPush Properties
    public static final String BUSINESS_SHORT_CODE = "360054";
    public static final String PASSKEY = "b103011487fabd3131d71916c0b26c399fb2a325afa46e3da07434ab23ffc715";
    public static final String TRANSACTION_TYPE = "CustomerPayBillOnline";
    public static final String PARTYB = "360054";
    public static final String CALLBACKURL = "https://spurquoteapp.ga/pusher/psher.php?title=stk_push&message=result&push_type=individual&regId=";

}
