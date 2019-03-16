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
import java.util.HashMap;
import java.util.Map;
import java.util.Set;

/**
 * Created by shri on 16/03/2019.
 */
public class SoftwareList implements Map<String, Software> {

    Map<String, Software> list = new HashMap<>();

    @Override
    public int size() {
        return list.size();
    }

    @Override
    public boolean isEmpty() {
        return list.isEmpty();
    }

    @Override
    public boolean containsKey(Object key) {
        return list.containsKey(key);
    }

    @Override
    public boolean containsValue(Object value) {
        return list.containsValue(value);
    }

    @Override
    public Software get(Object key) {
        return list.get(key);
    }

    @Override
    public Software put(String key, Software newSoft) {

        // TODO: do merge if necessary

        Software software = list.get("key");
        if (software != null) {

            // MERGE
            if (software.getIcon() == null && newSoft.getIcon() != null) {
                software.setIcon(newSoft.getIcon());
            }

            if (software.getInstallLocation() == null && newSoft.getInstallLocation() != null) {
                software.setInstallLocation(newSoft.getInstallLocation());
            }

            if (software.getVersion() == null && newSoft.getVersion() != null) {
                software.setVersion(newSoft.getVersion());
            }

            software.addRegKeys(newSoft.getRegKeys());
            // We won't duplicate the entry
            return software;
        }

        return list.put(key, newSoft);
    }

    @Override
    public Software remove(Object key) {
        return list.remove(key);
    }

    @Override
    public void putAll(Map<? extends String, ? extends Software> m) {
        list.putAll(m);
    }

    @Override
    public void clear() {
        list.clear();
    }

    @Override
    public Set<String> keySet() {
        return list.keySet();
    }

    @Override
    public Collection<Software> values() {
        return list.values();
    }

    @Override
    public Set<Entry<String, Software>> entrySet() {
        return list.entrySet();
    }
}
