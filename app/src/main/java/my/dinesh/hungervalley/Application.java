package my.dinesh.hungervalley;

public class Application extends android.app.Application {
    private String someVariable;
    private String cartVariable;
    private String userId;

    @Override
    public void onCreate() {
        super.onCreate();


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