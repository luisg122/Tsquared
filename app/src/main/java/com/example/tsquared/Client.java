package com.example.tsquared;

import com.loopj.android.http.AsyncHttpClient;
import com.loopj.android.http.AsyncHttpResponseHandler;
import com.loopj.android.http.RequestParams;

public class Client {
    AsyncHttpClient client;
    public void getQuestionTimeLine(AsyncHttpResponseHandler handler){
        client = new AsyncHttpClient();
        String apiUrl = "http://207.237.59.117:8080/TSquared/platform?todo=showQuestions";
        RequestParams params = new RequestParams();
        client.get(apiUrl, params, handler);
    }
}
