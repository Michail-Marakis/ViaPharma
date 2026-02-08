package gr.softeng.team09.util;

import android.app.Activity;
import android.content.res.Configuration;
import android.content.res.Resources;

import java.util.Locale;

/**
 * The type Locale helper.
 */
public class LocaleHelper {

    /**
     * Sets locale.
     *
     * @param activity the activity
     * @param lang     the lang
     */
    public static void setLocale(Activity activity, String lang) {
        Locale locale = new Locale(lang);
        Locale.setDefault(locale);

        Resources res = activity.getResources();
        Configuration config = new Configuration(res.getConfiguration());
        config.setLocale(locale);

        res.updateConfiguration(config, res.getDisplayMetrics());
        activity.recreate();
    }
}
