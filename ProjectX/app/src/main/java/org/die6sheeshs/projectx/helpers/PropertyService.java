package org.die6sheeshs.projectx.helpers;

import android.content.Context;

import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.Properties;

public class PropertyService {

    private static Context assets_context = null;
    private static Properties properties = null;

    /**
     * Reads property by given name.<br>
     * Requires context to be already registered
     * @param name Name of property in file
     * @return Value of given property, empty string if none was found
     */
    public static String readProperty(String name) {
        if (checkProperty()) {
            return properties.getProperty(name, "");
        } else {
            return "";
        }
    }

    /**
     * Initializes Property Service context, this is needed to find the properties file in the assets folder.<br>
     * This should be done initially in some main activity class.
     * @param context
     */
    public static void registerContext(Context context) {
        assets_context = context;
    }

    /**
     * Makes sure that a context has been set and creates properties instance if none was created yet
     * @return Boolean indicating success
     */
    private static boolean checkProperty() {
        if (assets_context == null) return false;

        if (properties == null) {
            try (InputStream input = assets_context.getAssets().open("config.properties")) {

                properties = new Properties();
                properties.load(input);

            } catch (IOException ex) {
                ex.printStackTrace();
            }
        }

        return true;
    }

}
