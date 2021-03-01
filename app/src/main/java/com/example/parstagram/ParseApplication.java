package com.example.parstagram;

import android.app.Application;

import com.example.parstagram.models.Post;
import com.parse.Parse;
import com.parse.ParseObject;

public class ParseApplication extends Application {

    @Override
    public void onCreate() {
        super.onCreate();

        ParseObject.registerSubclass(Post.class);

        Parse.initialize(new Parse.Configuration.Builder(this)
                .applicationId("d39BIvtwmAiVRkBmYEMIlk6kjVWTUjJ3qEMYJ4AR")
                .clientKey("Ln7sMbqvKy8YMRnbCpSNjBfhACQs1agbt46WqUye")
                .server("https://parseapi.back4app.com")
                .build()
        );
    }
}
