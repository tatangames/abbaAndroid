package com.tatanstudios.abba.extras;

import android.content.Context;
import android.content.res.Configuration;

import java.util.Locale;

public class LanguageUtils {

    public static String getSystemLanguage() {
        Locale defaultLocale = Locale.getDefault();
        return defaultLocale.getLanguage();
    }

    public static boolean isLanguageSupported(String languageCode, String[] supportedLanguages) {
        for (String supportedLanguage : supportedLanguages) {
            if (supportedLanguage.equals(languageCode)) {
                return true;
            }
        }
        return false;
    }

   /* public static String getPreferredLanguage(String[] supportedLanguages, String defaultLanguage) {
        String systemLanguage = getSystemLanguage();

        if (isLanguageSupported(systemLanguage, supportedLanguages)) {
            return systemLanguage;
        } else {
            // Use the default language (English in this case)
            return defaultLanguage;
        }
    }*/

    public static String getCurrentLanguage(Context context) {
        Configuration configuration = context.getResources().getConfiguration();
        return configuration.locale.getLanguage();
    }

}
