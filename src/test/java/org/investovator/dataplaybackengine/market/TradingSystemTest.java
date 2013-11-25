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


package org.investovator.dataplaybackengine.market;

import org.investovator.core.data.api.utils.TradingDataAttribute;
import org.junit.Test;

import java.util.ArrayList;
import java.util.HashMap;

/**
 * @author: ishan
 * @version: ${Revision}
 */
public class TradingSystemTest {
    @Test
    public void testExecuteOrder() throws Exception {
        //define the attributes needed
        ArrayList<TradingDataAttribute> attributes=new ArrayList<TradingDataAttribute>();

        //just the closing price is enough for now
        attributes.add(TradingDataAttribute.DAY);
        attributes.add(TradingDataAttribute.PRICE);

        String stock="GOOG";

        HashMap<TradingDataAttribute,String> priceList=new HashMap<>();
        priceList.put(TradingDataAttribute.PRICE,"12.0");

        TradingSystem ts=new TradingSystem(attributes,TradingDataAttribute.PRICE);
        //add the current price
        ts.updateStockPrice(stock,priceList);

        //carry out a buy order
        float expense=ts.executeOrder(stock,1,500,OrderType.BUY);
        assert(expense==12);

        //update the stock price
        priceList.put(TradingDataAttribute.PRICE,"10.0");
        ts.updateStockPrice(stock,priceList);
        //carry out a sell order
        float income=ts.executeOrder(stock,1,500,OrderType.SELL);
        assert(income==10);

    }

    @Test
    public void testGetStockPrice() throws Exception {
        //define the attributes needed
        ArrayList<TradingDataAttribute> attributes=new ArrayList<TradingDataAttribute>();

        //just the closing price is enough for now
        attributes.add(TradingDataAttribute.DAY);
        attributes.add(TradingDataAttribute.PRICE);

        String stock="GOOG";

        HashMap<TradingDataAttribute,String> priceList=new HashMap<>();
        priceList.put(TradingDataAttribute.PRICE,"12.0");

        TradingSystem ts=new TradingSystem(attributes,TradingDataAttribute.PRICE);
        //add the current price
        ts.updateStockPrice(stock,priceList);

        assert(ts.getStockPrice(stock)==12);

    }

    @Test
    public void testGetMarketTurnover() throws Exception {
        //define the attributes needed
        ArrayList<TradingDataAttribute> attributes=new ArrayList<TradingDataAttribute>();

        //just the closing price is enough for now
        attributes.add(TradingDataAttribute.DAY);
        attributes.add(TradingDataAttribute.PRICE);

        String stock="GOOG";

        HashMap<TradingDataAttribute,String> priceList=new HashMap<>();
        priceList.put(TradingDataAttribute.PRICE,"12.0");

        TradingSystem ts=new TradingSystem(attributes,TradingDataAttribute.PRICE);
        //add the current price
        ts.updateStockPrice(stock,priceList);

        //carry out a buy order
        float expense=ts.executeOrder(stock,1,500,OrderType.BUY);
        assert(expense==12);

        //update the stock price
        priceList.put(TradingDataAttribute.PRICE,"10.0");
        ts.updateStockPrice(stock,priceList);
        //carry out a sell order
        float income=ts.executeOrder(stock,1,500,OrderType.SELL);
        assert(income==10);

        assert(ts.getMarketTurnover()==(10+12));

    }

    @Test
    public void testUpdateStockPrice() throws Exception {
        //define the attributes needed
        ArrayList<TradingDataAttribute> attributes=new ArrayList<TradingDataAttribute>();

        //just the closing price is enough for now
        attributes.add(TradingDataAttribute.DAY);
        attributes.add(TradingDataAttribute.PRICE);

        String stock="GOOG";

        HashMap<TradingDataAttribute,String> priceList=new HashMap<>();
        priceList.put(TradingDataAttribute.PRICE,"12.0");

        TradingSystem ts=new TradingSystem(attributes,TradingDataAttribute.PRICE);
        //add the current price
        ts.updateStockPrice(stock,priceList);

        assert(ts.getStockPrice(stock)==12);
    }

    @Test
    public void testGetTotalTrades() throws Exception {
        //define the attributes needed
        ArrayList<TradingDataAttribute> attributes=new ArrayList<TradingDataAttribute>();

        //just the closing price is enough for now
        attributes.add(TradingDataAttribute.DAY);
        attributes.add(TradingDataAttribute.PRICE);

        String stock="GOOG";

        HashMap<TradingDataAttribute,String> priceList=new HashMap<>();
        priceList.put(TradingDataAttribute.PRICE,"12.0");

        TradingSystem ts=new TradingSystem(attributes,TradingDataAttribute.PRICE);
        //add the current price
        ts.updateStockPrice(stock,priceList);

        //carry out a buy order
        float expense=ts.executeOrder(stock,1,500,OrderType.BUY);
        assert(expense==12);

        //update the stock price
        priceList.put(TradingDataAttribute.PRICE,"10.0");
        ts.updateStockPrice(stock,priceList);
        //carry out a sell order
        float income=ts.executeOrder(stock,1,500,OrderType.SELL);

        //check the number of trades
        assert(ts.getTotalTrades()==2);

    }
}
