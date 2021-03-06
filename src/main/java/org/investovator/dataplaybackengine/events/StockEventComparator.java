/*
 * investovator, Stock Market Gaming Framework
 *     Copyright (C) 2013  investovator
 *
 *     This program is free software: you can redistribute it and/or modify
 *     it under the terms of the GNU General Public License as published by
 *     the Free Software Foundation, either version 3 of the License, or
 *     (at your option) any later version.
 *
 *     This program is distributed in the hope that it will be useful,
 *     but WITHOUT ANY WARRANTY; without even the implied warranty of
 *     MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 *     GNU General Public License for more details.
 *
 *     You should have received a copy of the GNU General Public License
 *     along with this program.  If not, see <http://www.gnu.org/licenses/>.
 */


package org.investovator.dataplaybackengine.events;

import java.io.Serializable;
import java.util.Comparator;

/**
 * A comparator to compare Stock Update events
 *
 * @author: ishan
 * @version: ${Revision}
 */
public class StockEventComparator implements Comparator,Serializable {
    @Override
    public int compare(Object o1, Object o2) {
        StockUpdateEvent event1 = (StockUpdateEvent) o1;
        StockUpdateEvent event2 = (StockUpdateEvent) o2;

        int decision;

        if (event1.getTime().compareTo(event2.getTime()) < 0) {
            decision = -1;
        } else if (event1.getTime().compareTo(event2.getTime()) > 0) {
            decision = 1;
        } else {
            decision = 0;
        }

        return decision;


    }
}
