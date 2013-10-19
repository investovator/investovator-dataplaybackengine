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


package org.investovator.dataPlayBackEngine;

import org.investovator.core.data.api.CompanyData;
import org.investovator.core.data.api.CompanyStockTransactionsData;
import org.investovator.core.data.api.utils.StockTradingData;
import org.investovator.core.data.api.utils.TradingDataAttribute;
import org.investovator.core.data.exeptions.DataAccessException;
import org.investovator.dataPlayBackEngine.data.BogusCompnayDataGenerator;
import org.investovator.dataPlayBackEngine.data.BogusHistoryDataGenerator;
import org.investovator.dataPlayBackEngine.events.StockEvent;
import org.investovator.dataPlayBackEngine.exceptions.GameAlreadyStartedException;
import org.investovator.dataPlayBackEngine.exceptions.GameFinishedException;
import org.investovator.dataPlayBackEngine.utils.DateUtils;

import java.text.ParseException;
import java.util.*;

/**
 * @author: ishan
 * @version: ${Revision}
 */
public class OHLCDataPLayer extends DataPlayer {

    //used to determine the cache size
    public static int CACHE_SIZE=100;

    //to keep track of the date
    Date today;
    //to keep the game state
    boolean gameStarted;
    //to keep track of the attributes needed
    TradingDataAttribute[] attributes;

    //to cache the stock trading data items
    HashMap<String, HashMap<Date, HashMap<TradingDataAttribute, Float>>> ohlcDataCache;

    public OHLCDataPLayer(String[] stocks,TradingDataAttribute[] attributes) throws ParseException {

        this.ohlcDataCache = new HashMap<String, HashMap<Date, HashMap<TradingDataAttribute, Float>>>();
        this.attributes = attributes;

        //initialize the stocks
        for (String stock : stocks) {
            ohlcDataCache.put(stock, new HashMap<Date, HashMap<TradingDataAttribute, Float>>());
        }


        gameStarted = false;

    }

    /**
     * Starts the game.
     * <p/>
     * Assumption - Assumes that a suitable start date has been set by the setStartDate() method.
     *
     * @return list of events needed to start the game
     * @throws GameAlreadyStartedException
     */
    public StockEvent[] startGame() throws GameAlreadyStartedException {

        ArrayList<StockEvent> events = new ArrayList<StockEvent>();
        //if the game has not started yet
        if (!gameStarted) {
            //search all the stocks
            for (String stock : ohlcDataCache.keySet()) {

                try {
                    StockTradingData data = transactionDataAPI.getTradingData(CompanyStockTransactionsData.DataType.OHLC,
                            stock, today, attributes, OHLCDataPLayer.CACHE_SIZE);

                    //if any data was returned
                    if (data != null) {
                        //get the relevant data
                        events.add(new StockEvent(stock, data.getTradingDataEntry(today), today));
                        //remove that entry from map
                        data.getTradingData().remove(today);

                        //add the rest of the data to the cache
                        ohlcDataCache.put(stock, data.getTradingData());

                    }

                } catch (DataAccessException e) {
                    e.printStackTrace();  //To change body of catch statement use File | Settings | File Templates.
                }
            }


        } else {
            throw new GameAlreadyStartedException(this);
        }

        today=DateUtils.incrementTimeByDays(1,today);
        return events.toArray(new StockEvent[events.size()]);

    }

    /**
     * Used to set the game starting date at the start of the game.
     *
     * @param startDate
     * @param dateFormat
     * @throws ParseException
     */
    public void setStartDate(String startDate, String dateFormat ) throws ParseException {
        today = DateUtils.dateStringToDateObject(startDate, dateFormat);

    }

    /**
     * returns the  stock events in the next day. If a stock does not contain the price information
     * for the requested day, its <b>data</> map will be null.
     *
     * @return An array of StockEvent's if the data is present for at least a single stock. If data is not present
     * for any stock, returns a null.
     */
    public StockEvent[] playNextDay() throws GameFinishedException {
        ArrayList<StockEvent> events = new ArrayList<StockEvent>();

        //iterate all the stocks
        for(String stock:ohlcDataCache.keySet()) {
            //if the date is in the cache
            if (ohlcDataCache.get(stock).containsKey(today)) {
                events.add(new StockEvent(stock,ohlcDataCache.get(stock).get(today),today));

                //remove the used items from the cache
                ohlcDataCache.get(stock).remove(today);

            }
            else{
                events.add(new StockEvent(stock,null,today));
            }

        }

        //if no data has been found in the cache
        if (events.size()==0) {

            //to track whether at least a single stock has data in the future
            boolean hasData=false;

            //search all the stocks
            for (String stock : ohlcDataCache.keySet()) {

                try {

                    StockTradingData data = transactionDataAPI.getTradingData(CompanyStockTransactionsData.DataType.OHLC,
                            stock, today, attributes, OHLCDataPLayer.CACHE_SIZE);

                    //if any data was returned
                    if (data != null) {
                        //get the relevant data
                        events.add(new StockEvent(stock, data.getTradingDataEntry(today), today));
                        //remove that entry from map
                        data.getTradingData().remove(today);

                        //clear the cache first
                        ohlcDataCache.remove(stock);
                        //add the rest of the data to the cache
                        ohlcDataCache.put(stock, data.getTradingData());

                        hasData=true;

                    }

                } catch (DataAccessException e) {
                    //TODO - change this exception handling code to act on whatever the exception that the
                    //core module will throw when no data is present for a given stock from the given time
                    //onwards.
                    //TODO - catch that exception before the DataAccessException ;)

                    ///////
                   e.printStackTrace();  //To change body of catch statement use File | Settings | File Templates.
                }
            }

            //if none of the stocks has future data
            if(!hasData){
                throw new GameFinishedException("OHLC");
            }

        }

        //if no data has been found from the DB query too
        if(events.size()==0){
            return null;
        }

        today=DateUtils.incrementTimeByDays(1,today);
        return events.toArray(new StockEvent[events.size()]);

    }


    /**
     * Be cautious enough to call this method before playNextDay() has been called.
     * Else it will show the next day
     *
     * @return next day in the game
     */
    public Date getNextDay(){
        return today;
    }

    /**
     *
     * @return current day in the game
     */
    public Date getToday(){
        return DateUtils.decrementTimeByDays(1,today);
    }
}
