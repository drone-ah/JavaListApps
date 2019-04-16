/*
    This file is part of JavaListApps.

    JavaListApps is free software: you can redistribute it and/or modify
    it under the terms of the GNU Lesser General Public License as published by
    the Free com.droneah.listapps.Software Foundation, either version 3 of the License, or
    (at your option) any later version.

    JavaListApps is distributed in the hope that it will be useful,
    but WITHOUT ANY WARRANTY; without even the implied warranty of
    MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
    GNU General Public License for more details.

    You should have received a copy of the GNU General Public License
    along with JavaListApps.  If not, see <https://www.gnu.org/licenses/>.
*/

package com.droneah.listapps;

import java.time.LocalDateTime;
import java.time.ZoneId;
import java.util.Map;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import com.sun.jna.platform.win32.Advapi32Util;
import com.sun.jna.platform.win32.Advapi32Util.InfoKey;
import com.sun.jna.platform.win32.WinNT;
import com.sun.jna.platform.win32.WinReg;
import com.sun.jna.platform.win32.WinReg.HKEYByReference;

/**
 *
 */
public class ListApps {

    public static Map<String, Software> getInstalledApps(boolean includeUpdates) {

        SoftwareList list = new SoftwareList();

        getUninstallKeyPrograms(list, WinReg.HKEY_LOCAL_MACHINE, "Software\\Microsoft\\Windows\\CurrentVersion\\Uninstall", includeUpdates);
        getUninstallKeyPrograms(list, WinReg.HKEY_LOCAL_MACHINE, "Software\\WoW6432Node\\Microsoft\\Windows\\CurrentVersion\\Uninstall", includeUpdates);

        String[] subKeys = Advapi32Util.registryGetKeys(WinReg.HKEY_USERS);

        for(String subKey: subKeys) {
            try {
                getUninstallKeyPrograms(list, WinReg.HKEY_USERS, subKey + "\\Software\\Microsoft\\Windows\\CurrentVersion\\Uninstall", includeUpdates);
            } catch (Exception e) {
                // ignore
            }
        }

        return list;

    }

    static SoftwareList getUninstallKeyPrograms(SoftwareList list, WinReg.HKEY hkey, String rootKey, boolean includeUpdates) {

        String[] subKeys = Advapi32Util.registryGetKeys(hkey, rootKey);

        for(String subKeyName: subKeys) {

            Map<String, Object> vals = Advapi32Util.registryGetValues(hkey, rootKey + "\\" + subKeyName);

            // Not a system component
            if (vals.get("SystemComponent") == null || !vals.get("SystemComponent").equals(1)) {

                String name = (String) vals.get("DisplayName");;
                String version = (String) vals.get("DisplayVersion");
                String installLocation = (String) vals.get("InstallLocation");
                String icon = (String) vals.get("DisplayIcon");
                String publisher = (String) vals.get("Publisher");

                // Get Installation Date/Time
                // NOTE: Windows Installer et al will use the InstallDate property to set the installation date, but
                // this is simpler, and for our purposes, probably accurate enough. Besides, InstallDate property is only
                // granular enough to day, while this one goes all the way to milliseconds (though accuracy is questionable)
                HKEYByReference subKey = Advapi32Util.registryGetKey(hkey,  rootKey + "\\" + subKeyName, WinNT.KEY_READ);
                InfoKey infoKey = Advapi32Util.registryQueryInfoKey(subKey.getValue(), 0);
                LocalDateTime installDate = infoKey
                                                .lpftLastWriteTime
                                                .toDate()
                                                .toInstant()
                                                .atZone(ZoneId.systemDefault())
                                                .toLocalDateTime();


                // Not Windows Installer
                if (vals.get("WindowsInstaller") == null || !vals.get("WindowsInstaller").equals(1)) {

                    String releaseType = (String) vals.get("ReleaseType");

                    Software software = new Software(rootKey + "\\" + subKeyName, name, publisher, installLocation, installDate, version, icon);

                    Pattern windowsUpdateRegex = Pattern.compile("KB[0-9]{6}$");
                    Matcher m = windowsUpdateRegex.matcher(subKeyName);
                    // Is program an update
                    if (m.find() ||
                            (vals.get("ParentKeyName")) != null && !vals.get("ParentKeyName").equals("") ||
                            (releaseType != null &&
                                    (releaseType.equals("Security Update") ||
                                     releaseType.equals("Update Rollup") ||
                                     releaseType.equals("Hotfix")))) {

                        if (name != null && !name.equals("") && includeUpdates) {
                            list.put(software.getDisplayName(), software);
                        }
                    } else {
                        String uninstallValue = (String) vals.get("UninstallString");

                        if (uninstallValue != null && !uninstallValue.equals("") && name != null && !name.equals("")) {
                            list.put(software.getDisplayName(), software);
                        }

                    }
                } else {
                    // Windows Installer
                    String msiKeyName = getInstallerKeyNameFromGuid(subKeyName);

                    Map<String, Object> msiVals = Advapi32Util.registryGetValues(hkey, "Software\\Classes\\Installer\\Products\\" + msiKeyName);

                    String msiName = (String) msiVals.get("ProductName");
                    if (msiName != null && !msiName.equals("")) {
                        name = msiName;
                    }

                    String msiIcon = (String) msiVals.get("ProductIcon");
                    if (msiIcon != null && !msiIcon.equals("")) {
                        icon = msiIcon;
                    }

                    if (!name.equals("")) {
                        Software software = new Software(rootKey + "\\" + subKeyName, name, publisher, installLocation, installDate, version, icon);
                        list.put(software.getDisplayName(), software);
                    }

                }
            }

        }

        return list;
    }

    /**
     * Perform windows-ness to transalate key name to Guid for Windows Installer Apps
     * @param subKeyName key to translate
     * @return translated key
     */
    static String getInstallerKeyNameFromGuid(String subKeyName) {
        String key = subKeyName.replace("{", "")
                               .replace("}", "");


        String[] parts = key.split("-");

        String msiName = "";
        // reverse the first three parts
        for (int i = 0; i <= 2; i++) {
            msiName += new StringBuilder().append(parts[i]).reverse();
        }

        // for the last two parts, reverse each character pari
        for (int i = 3; i <= 4; i++) {
            for (int j = 0; j < parts[i].length(); j+=2) {
                msiName += parts[i].charAt(j+1);
                msiName += parts[i].charAt(j);
            }

        }

        return msiName;

    }

}
