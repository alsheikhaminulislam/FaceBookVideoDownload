package com.demyzo.facebook;

import android.Manifest;
import android.app.Activity;
import android.content.Context;
import android.content.pm.PackageManager;
import android.os.AsyncTask;
import android.util.Log;


//import org.jsoup.Jsoup;
//import org.jsoup.nodes.Document;

import androidx.annotation.NonNull;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;

import com.demyzo.facebook.jsoup.Jsoup;
import com.demyzo.facebook.jsoup.nodes.Document;

import java.io.File;
import java.io.IOException;
import java.net.MalformedURLException;
import java.net.URL;

public class FaceBookInitializing {
    private static Context activity;
    private static String VideoUrl;
    private static String URLl;
    private static int STORE_PERMISSION_CODE = 10212;
    private static EventListener eventListener;
    private static boolean isUrl = false;
    private static  FaceBookInitializing faceBookInitializing;

    public static FaceBookInitializing getInstance(Context context ){
        activity = context;
        if (faceBookInitializing ==null){
            faceBookInitializing = new FaceBookInitializing();
        }
        return faceBookInitializing;
    }
    public void  addEventListener( String URL ,EventListener eventlistener){
        eventListener = eventlistener;
        URLl = URL;
        if (ContextCompat.checkSelfPermission(activity , Manifest.permission.WRITE_EXTERNAL_STORAGE ) == PackageManager.PERMISSION_GRANTED ){
            startProcess();
        }else {
            eventListener.onError("PLEASE GRANTED WRITE EXTERNAL STORAGE PERMISSION");
        }
    }

    private static void startProcess() {
        try {
            Utils.createFileFolder();
            URL url = new URL(URLl);
            String host = url.getHost();
            Log.e("initViews: ", host);
            if (host.contains("facebook.com")) {
                isUrl = true;
                if (isUrl){
                    Utils.showProgressDialog(activity);
                    new callGetFacebookData().execute(URLl);
                }else {
                    eventListener.onError("Error Facebook link.");
                }
            } else {
                Utils.setToast(activity, "Enter valid url");
            }

        } catch (Exception e) {
            e.printStackTrace();
        }
    }


    private static class callGetFacebookData extends AsyncTask<String, Void, Document> {
        Document facebookDoc;
        @Override
        protected void onPreExecute() {
            super.onPreExecute();
        }
        @Override
        protected Document doInBackground(String... urls) {
            try {
                facebookDoc = Jsoup.connect(urls[0]).get();
            } catch (IOException e) {
                e.printStackTrace();
                Log.d("TAG", "doInBackground: Error");
            }
            return facebookDoc;
        }
        protected void onPostExecute(Document result) {
            try {
                VideoUrl = result.select("meta[property=\"og:video\"]").last().attr("content");
                Log.e("onPostExecute: ", VideoUrl);
                if (!VideoUrl.equals("")) {
                    try {
                        Utils.hideProgressDialog(activity);
                        eventListener.onStart("Downloading.....");
                        Utils.startDownload(VideoUrl,  Utils.RootDirectoryFacebook, activity, getFilenameFromURL(VideoUrl));
                        VideoUrl = "";
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                } else {
                }
            } catch (NullPointerException e) {
                e.printStackTrace();
            }
        }
    }
    private static String getFilenameFromURL(String url) {
        try {
            return new File(new URL(url).getPath()).getName()+".mp4";
        } catch (MalformedURLException e) {
            e.printStackTrace();
            return System.currentTimeMillis() + ".mp4";
        }
    }
}
