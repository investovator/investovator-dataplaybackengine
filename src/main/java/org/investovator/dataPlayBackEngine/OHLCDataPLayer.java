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
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.TreeMap;

/**
 * @author: ishan
 * @version: ${Revision}
 */
public class OHLCDataPLayer {

    //used to determine the cache size
    public static int CACHE_SIZE=100;

    CompanyStockTransactionsData transactionDataAPI;
    CompanyData companyDataAPI;

    //to keep track of the date
    Date today;
    //to keep the game state
    boolean gameStarted;
    //to keep track of the attributes needed
    TradingDataAttribute[] attributes;


    //to cache the stock trading data items
    HashMap<String, HashMap<Date, HashMap<TradingDataAttribute, Float>>> ohlcDataCache;

    public OHLCDataPLayer(String[] stocks, String dateFormat, TradingDataAttribute[] attributes) throws ParseException {
        //for testing
        this.transactionDataAPI = new BogusHistoryDataGenerator();
        this.companyDataAPI = new BogusCompnayDataGenerator();
        //testing end

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

    public Date getEarliestCommonDate(String[] stocks){
        Date earliestDate=null;

        //Date(in order) - [stocks]
        TreeMap<Date,ArrayList<String>> counter=new TreeMap<Date, ArrayList<String>>();

        //iterate all the stocks
        for(String stock:stocks){
            //get all the dates for that stock
            Date[] dates=transactionDataAPI.getDataDaysRange(CompanyStockTransactionsData.DataType.OHLC,stock);

            //add them to the map
            for(Date date:dates){
                //if the arraylist has not been initialized
                if(counter.containsKey(stock)){
                    counter.put(date,new ArrayList<String>());
                }

                ArrayList<String> stockList=counter.get(date);
                counter.put(date,stockList);
            }
        }

        //iterate the map in the ascending order and determine the date which has all the stocks
        for(Date date:counter.keySet()){
            if(counter.get(date).size()==stocks.length){
                earliestDate=date;
                break;
            }
        }


        return  earliestDate;

    }


    /**
     * TODO - DELETE
     * @param stock stock name
     * @param date  date for which the price of the stock required
     * @return the price of the stock on the "date" or negative values if there is no more data
     */

//    public float getOHLCPrice(String stock, String date) {
//
//        Date currentTime = null;
//        float price = 0;
//
//        try {
//            currentTime = DateUtils.dateStringToDateObject(date, DateUtils.DATE_FORMAT_1);
//        } catch (ParseException e) {
//            e.printStackTrace();  //To change body of catch statement use File | Settings | File Templates.
//        }
//
//
//        boolean inCache = false;
//
//        //if the stocks data is already in the cache
//        if (ohlcDataCache.containsKey(stock)) {
//            //if the requested date is in the cache
//            if (ohlcDataCache.get(stock).containsKey(currentTime)) {
//                inCache = true;
//                //just the closing price is enough for now
//                //TODO- remove the used items in the cache
//                price = ohlcDataCache.get(stock).get(currentTime).get(TradingDataAttribute.CLOSING_PRICE);
//                ohlcDataCache.get(stock).remove(currentTime);
//
//            }
//
//        } ////
//
//        //if the stock data is not in the cache
//        if (!inCache) {
//            //define the attributes needed
//            TradingDataAttribute attributes[] = new TradingDataAttribute[2];
//
//            //just the closing price is enough for now
//            attributes[0] = TradingDataAttribute.DAY;
//            attributes[1] = TradingDataAttribute.CLOSING_PRICE;
//
//            //let's take the next 100 of rows
//            try {
//                StockTradingData data = transactionDataAPI.getTradingData(CompanyStockTransactionsData.DataType.OHLC,
//                        stock, currentTime, attributes, 100);
//
//                //if any data was returned
//                if (data != null) {
//
//                    //remove the old set of data for this stock and add a new set
//                    if (ohlcDataCache.containsKey(stock)) {
//                        ohlcDataCache.remove(stock);
//                    }
//                    //add the new data
//                    ohlcDataCache.put(stock, data.getTradingData());
//                    price = ohlcDataCache.get(stock).get(currentTime).get(TradingDataAttribute.CLOSING_PRICE);
//                } else {
//                    price = -1;
//                }
//
//            } catch (DataAccessException e) {
//                e.printStackTrace();  //To change body of catch statement use File | Settings | File Templates.
//            }
//
//        }
//
//        return price;
//    }


    /**
     * @return Company StockId and Name pairs
     * @throws DataAccessException
     */
    public HashMap<String, String> getStocksList() throws DataAccessException {

        return companyDataAPI.getCompanyIDsNames();


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
