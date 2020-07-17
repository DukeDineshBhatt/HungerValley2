package my.dinesh.hungervalley;

import android.content.Context;

public class Application extends android.app.Application {
    private String someVariable;
    private String cartVariable;
    private String userId;

    public String baseurl = "https://www.fast2sms.com/";

    private static Context context;
    public static Context getContext() {
        return context;
    }

    @Override
    public void onCreate() {
        super.onCreate();

        context = getApplicationContext();
    }

    public String getCartVariable() {
        return cartVariable;
    }

    public String setCartVariable(String cartVariable) {
        this.cartVariable = cartVariable;
        return cartVariable;
    }

    public String getSomeVariable() {
        return someVariable;
    }


    public String setSomeVariable(String someVariable) {
        this.someVariable = someVariable;
        return someVariable;
    }

    public String getUserId() {
        return userId;
    }

    public String setUserId(String userId) {
        this.userId = userId;
        return userId;
    }
}