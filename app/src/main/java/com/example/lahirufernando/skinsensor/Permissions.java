package com.example.lahirufernando.skinsensor;

/**
 * Created by lahirufernando on 30/11/2017.
 */

import android.app.Activity;
import android.content.pm.PackageManager;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;

import java.util.ArrayList;

public class Permissions {
    /**
     * This method is in order to request the permission for Camera Activity
     * @param activity - class name
     * @param requestCode - code which is used to request permission
     * @param permissions - request permission to connect to the API
     * @return - returns the request for permission
     */
    public static boolean requestPermission(
            Activity activity, int requestCode, String... permissions) {
        boolean granted = true;
        ArrayList<String> permissionsNeeded = new ArrayList<>();

        for (String s : permissions) {
            int permissionCheck = ContextCompat.checkSelfPermission(activity, s);
            boolean hasPermission = (permissionCheck == PackageManager.PERMISSION_GRANTED);
            granted &= hasPermission;
            if (!hasPermission) {
                permissionsNeeded.add(s);
            }
        }
        if (granted) {
            return true;
        } else {
            ActivityCompat.requestPermissions(activity,
                    permissionsNeeded.toArray(new String[permissionsNeeded.size()]),
                    requestCode);
            return false;
        }
    }

    /**
     * This method is used in order to grant permissions where it checks the request code with the permission code
     * @param requestCode - code which is used to request
     * @param permissionCode - permission code in order to be granted
     * @param grantResults - variable used as to guarantee permission
     * @return - returns the granted permission for Camera or Gallery to open
     */
    public static boolean permissionGranted(int requestCode, int permissionCode, int[] grantResults) {
        if (requestCode == permissionCode) {
            if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                return true;
            } else {
                return false;
            }
        }
        return false;
    }
}
