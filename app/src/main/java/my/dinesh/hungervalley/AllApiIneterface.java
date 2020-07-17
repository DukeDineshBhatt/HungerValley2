package my.dinesh.hungervalley;

import retrofit2.Call;
import retrofit2.http.Header;
import retrofit2.http.Multipart;
import retrofit2.http.POST;
import retrofit2.http.Part;

public interface AllApiIneterface {


    @Multipart
    @POST("dev/bulk")
    Call<OtpBean> getOtp(
            @Part("sender_id") String sender_id,
            @Part("language") String language,
            @Part("route") String route,
            @Part("numbers") String numbers,
            @Part("message") String message,
            @Part("variables") String variables,
            @Part("variables_values") String variables_values,
            @Header("authorization") String authorization

    );
}
