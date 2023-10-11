package com.adobe.aem.guides.wknd.core.servlets;

public class APIResponseDetails {

    private int responseCode;

    private String responseBody;

    public APIResponseDetails() {

    }

    public APIResponseDetails(int responseCode, String responseBody) {
        this.responseCode = responseCode;
        this.responseBody = responseBody;
    }

    public int getResponseCode() {
        return responseCode;
    }

    public void setResponseCode(int responseCode) {
        this.responseCode = responseCode;
    }

    public String getResponseBody() {
        return responseBody;
    }

    public void setResponseBody(String responseMessage) {
        this.responseBody = responseMessage;
    }

    @Override
    public String toString() {
        return "ResponseCode :" + responseCode + " ResponseBody:" + responseBody;
    }

}
