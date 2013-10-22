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

import org.investovator.core.data.api.utils.TradingDataAttribute;

import java.util.Date;
import java.util.HashMap;

/**
 * @author: ishan
 * @version: ${Revision}
 */
public class StockEvent {

    private String stockId;
    private HashMap<TradingDataAttribute, Float> data;
    private Date time;

//    public StockEvent(String stockId, HashMap<TradingDataAttribute, Float> data, Date time) {
//        this.stockId = stockId;
//        this.data = data;
//        this.time = time;
//    }

    public StockEvent(String stockId, HashMap<TradingDataAttribute, String> data, Date time) {
        this.stockId = stockId;

        this.data=new HashMap<TradingDataAttribute, Float>();
        for(TradingDataAttribute attr:data.keySet()){
            this.data.put(attr,Float.parseFloat(data.get(attr)));
        }
        //this.data = data;
        this.time = time;
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
        return time;
    }

    public void setTime(Date time) {
        this.time = time;
    }
}
