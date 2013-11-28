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

import org.investovator.core.commons.events.GameEvent;
import org.investovator.core.data.api.utils.TradingDataAttribute;

import java.util.Date;
import java.util.HashMap;
import java.util.Map;

/**
 * Stock market update event. An update is a price change in a security
 *
 * @author: ishan
 * @version: ${Revision}
 */
public class StockUpdateEvent extends GameEvent {

    private String stockId;
    private HashMap<TradingDataAttribute, Float> data;
    private Date time;


    public StockUpdateEvent(String stockId, HashMap<TradingDataAttribute, String> data, Date time) {
        this.stockId = stockId;

        //if data is avaialble for this day
        if (data != null) {
            this.data = new HashMap<TradingDataAttribute, Float>();
            for (Map.Entry<TradingDataAttribute, String> entry : data.entrySet()) {
                this.data.put(entry.getKey(), Float.parseFloat(entry.getValue()));
            }
        }

        //this.data = data;
        this.time = (Date)time.clone();
    }


    public String getStockId() {
        return stockId;
    }

    public void setStockId(String stockId) {
        this.stockId = stockId;
    }

    public HashMap<TradingDataAttribute, Float> getData() {
        return data;
    }

    public void setData(HashMap<TradingDataAttribute, Float> data) {
        this.data = data;
    }

    public Date getTime() {
        return (Date)time.clone();
    }

    public void setTime(Date time) {
        this.time = (Date)time.clone();
    }

    /**
     * useful in duplicate event identification
     *
     * @param obj
     * @return
     */
    @Override
    public boolean equals(Object obj) {
        if (obj instanceof StockUpdateEvent) {
            StockUpdateEvent otherEvent = (StockUpdateEvent) obj;
            //for two events to be same the stock every attribute should match
            if (this.time.equals(otherEvent.getTime()) &&
                    this.data.equals(otherEvent.getData()) &&
                    this.stockId.equalsIgnoreCase(otherEvent.getStockId())) {
                return true;
            }
        }
        return false;
    }

    @Override
    public int hashCode() {
        assert false : "hashCode not designed";
        return 42;
    }
}
