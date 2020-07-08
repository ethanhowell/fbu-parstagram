package com.ethanjhowell.parstagram;

import android.text.format.DateUtils;

import com.parse.ParseClassName;
import com.parse.ParseFile;
import com.parse.ParseObject;
import com.parse.ParseUser;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.Locale;
import java.util.concurrent.TimeUnit;

@ParseClassName("Post")
public class Post extends ParseObject {
    public static final String KEY_CAPTION = "caption";
    public static final String KEY_USER = "user";
    public static final String KEY_IMAGE = "image";
    public static final String KEY_CREATED_KEY = "createdAt";

    // date formats for getting relative timestamp
    private static final SimpleDateFormat shortDateFormat = new SimpleDateFormat("MMM d", Locale.US);
    private static final SimpleDateFormat longDateFormat = new SimpleDateFormat("MMM d, yyyy", Locale.US);

    public Post() {
    }

    public Post(String caption, ParseFile image, ParseUser user) {
        setImage(image);
        setCaption(caption);
        setUser(user);
    }

    public String getCaption() {
        return getString(KEY_CAPTION);
    }

    public void setCaption(String caption) {
        put(KEY_CAPTION, caption);
    }

    public ParseUser getUser() {
        return getParseUser(KEY_USER);
    }

    public void setUser(ParseUser user) {
        put(KEY_USER, user);
    }

    public ParseFile getImage() {
        return getParseFile(KEY_IMAGE);
    }

    public void setImage(ParseFile image) {
        put(KEY_IMAGE, image);
    }

    public String getRelativeCreatedAt() {
        Date created = getCreatedAt();
        Calendar now = Calendar.getInstance();
        long difference = now.getTimeInMillis() - created.getTime();
        if (difference < DateUtils.MINUTE_IN_MILLIS)
            return String.format("%ss", TimeUnit.MILLISECONDS.toSeconds(difference));
        else if (difference < DateUtils.HOUR_IN_MILLIS)
            return String.format("%sm", TimeUnit.MILLISECONDS.toMinutes(difference));
        else if (difference < DateUtils.DAY_IN_MILLIS)
            return String.format("%sh", TimeUnit.MILLISECONDS.toHours(difference));
        else {
            Calendar then = Calendar.getInstance();
            then.setTime(created);
            if (then.get(Calendar.YEAR) == now.get(Calendar.YEAR))
                return shortDateFormat.format(created);
            else
                return longDateFormat.format(created);
        }
    }
}
