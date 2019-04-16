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

import java.util.Collection;
import java.util.HashSet;
import java.util.Set;

/**
 * Created by shri on 16/03/2019.
 */
public class Software {

    public Software(String regKey, String displayName, String publisher, String installLocation, String version, String icon) {
        addRegKey(regKey);
        setDisplayName(displayName);
        setPublisher(publisher);
        setInstallLocation(installLocation);
        setVersion(version);
        setIcon(icon);
    }

    private Set<String> regKeys = new HashSet<>();

    private String displayName;

    private String publisher;

    private String installLocation;

    private String version;

    private String icon;

    public void addRegKey(String key) {

        if (key == null) {
            return;
        }
        regKeys.add(key);
    }

    public void addRegKeys(Collection<String> keys) {
        regKeys.addAll(keys);
    }

    public String getDisplayName() {
        return displayName;
    }

    public void setDisplayName(String displayName) {
        this.displayName = nullifEmpty(displayName);
    }

    public String getInstallLocation() {
        return installLocation;
    }

    public void setInstallLocation(String installLocation) {
        this.installLocation = nullifEmpty(installLocation);
    }

    public String getVersion() {
        return version;
    }

    public void setVersion(String version) {
        this.version = nullifEmpty(version);
    }

    public String getIcon() {
        return icon;
    }

    public void setIcon(String icon) {
        this.icon = nullifEmpty(icon);
    }

    private String nullifEmpty(String string) {
        return string == null || string.equals("") ? null : string;
    }

    public Set<String> getRegKeys() {
        return regKeys;
    }

    public String getPublisher() {
        return publisher;
    }

    public void setPublisher(String publisher) {
        this.publisher = nullifEmpty(publisher);
    }
}
