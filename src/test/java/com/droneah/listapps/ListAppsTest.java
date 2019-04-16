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
import static org.junit.Assert.assertEquals;

import java.util.Map;
import java.util.SortedSet;
import java.util.TreeSet;

import org.junit.Test;


/**
 * Created by shri on 16/03/2019.
 */
public class ListAppsTest {

    @Test
    public void testGetInstallerKeynameFromGuid() {

        assertEquals("007B601F8FFB56033B50413DA64D5093", ListApps.getInstallerKeyNameFromGuid("{F106B700-BFF8-3065-B305-14D36AD40539}"));

    }

    @Test
    public void testGetInstalledApps() {

		if (!System.getProperty("os.name").startsWith("Windows")) {
			// tests will fail
			return;
		}

        // Doesn't actually assert anything, but outputs the list
        Map<String, Software> list = ListApps.getInstalledApps(false);
        SortedSet<String> names = new TreeSet<>(list.keySet());

        System.out.println(names.size());

        for(String name: names) {
            System.out.println(name);
        }


    }
}
