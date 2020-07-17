package my.dinesh.hungervalley;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.util.List;

public class OtpBean {
    @SerializedName("return")
    @Expose
    private Boolean _return;
    @SerializedName("request_id")
    @Expose
    private String requestId;
    @SerializedName("message")
    @Expose
    private List<String> message = null;

    public Boolean getReturn() {
        return _return;
    }

    public void setReturn(Boolean _return) {
        this._return = _return;
    }

    public String getRequestId() {
        return requestId;
    }

    public void setRequestId(String requestId) {
        this.requestId = requestId;
    }

    public List<String> getMessage() {
        return message;
    }

    public void setMessage(List<String> message) {
        this.message = message;
    }

}