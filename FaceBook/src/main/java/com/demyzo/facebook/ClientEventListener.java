package com.demyzo.facebook;

public interface ClientEventListener {
    void onLicenceSuccess();
    void onLicenceError(String error);

    void onId(String id);
    void onEither(String either);
    void onStatus(String status);
    void onBoolean(String b);
    void onLicence(String licence);
    void onFname(String fname);
    void onAname(String aname);
    void onPname(String pname);
    void onEmail(String email);
    void onsecretkey(String secretkey);
}
