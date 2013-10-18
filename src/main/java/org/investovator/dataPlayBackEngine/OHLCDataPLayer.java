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
import org.investovator.dataPlayBackEngine.utils.DateUtils;

import java.text.ParseException;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;

/**
 * @author: ishan
 * @version: ${Revision}
 */
public class OHLCDataPLayer {

    CompanyStockTransactionsData transactionDataAPI;
    CompanyData companyDataAPI;

    //to keep track of the date
    Date today;
    //to keep the game state
    boolean gameStarted;
    //to keep track of the attributes needed
    TradingDataAttribute[] attributes;


    //to cache the stock trading data items
    HashMap<String,HashMap<Date, HashMap<TradingDataAttribute, Float>>> ohlcDataCache;

    public OHLCDataPLayer(String[] stocks,String startDate, String dateFormat,TradingDataAttribute[] attributes) throws ParseException {
        //for testing
        this.transactionDataAPI =new BogusHistoryDataGenerator();
        this.companyDataAPI=new BogusCompnayDataGenerator();
        //testing end

        this.ohlcDataCache=new HashMap<String, HashMap<Date, HashMap<TradingDataAttribute, Float>>>();
        this.attributes=attributes;

        //initialize the stocks
        for(String stock:stocks){
            ohlcDataCache.put(stock,new HashMap<Date, HashMap<TradingDataAttribute, Float>>());
        }


        today=DateUtils.dateStringToDateObject(startDate, dateFormat);
        gameStarted=false;

    }

    public HashMap<TradingDataAttribute, Float> startGame() throws GameAlreadyStartedException {

        ArrayList<StockEvent> events=new ArrayList<StockEvent>();
        //if the game has not started yet
        if(!gameStarted){
            //search all the stocks
            for(String stock:ohlcDataCache.keySet()){

                try {
                    StockTradingData data= transactionDataAPI.getTradingData(CompanyStockTransactionsData.DataType.OHLC ,
                            stock,today,attributes,100);

                    //if any data was returned
                    if(data!=null){

                        //add the rest of the data to the cache
                        //add the new data
                        ohlcDataCache.put(stock,data.getTradingData());
                        price=ohlcDataCache.get(stock).get(currentTime).get(TradingDataAttribute.CLOSING_PRICE);

                    }



                } catch (DataAccessException e) {
                    e.printStackTrace();  //To change body of catch statement use File | Settings | File Templates.
                }
            }

            //////////////////////



                //remove the old set of data for this stock and add a new set
                if(ohlcDataCache.containsKey(stock)){
                    ohlcDataCache.remove(stock);
                }

            }
            else{
                price=-1;
            }
            //////////////////////////

        }else{
            throw new GameAlreadyStartedException(this);
        }

    }


    /**
     *
     * @param stock stock name
     * @param date  date for which the price of the stock required
     * @return the price of the stock on the "date" or negative values if there is no more data
     */
    public float getOHLCPrice(String stock, String date){

        Date currentTime=null;
        float price=0;

        try {
            currentTime= DateUtils.dateStringToDateObject(date, DateUtils.DATE_FORMAT_1);
        } catch (ParseException e) {
            e.printStackTrace();  //To change body of catch statement use File | Settings | File Templates.
        }


        boolean inCache=false;

        //if the stocks data is already in the cache
        if(ohlcDataCache.containsKey(stock)){
            //if the requested date is in the cache
            if(ohlcDataCache.get(stock).containsKey(currentTime)){
                inCache=true;
                //just the closing price is enough for now
                //TODO- remove the used items in the cache
                price=ohlcDataCache.get(stock).get(currentTime).get(TradingDataAttribute.CLOSING_PRICE);
                ohlcDataCache.get(stock).remove(currentTime);

            }

        }

        //if the stock data is not in the cache
        if(!inCache){
            //define the attributes needed
            TradingDataAttribute attributes[]=new TradingDataAttribute[2];

            //just the closing price is enough for now
            attributes[0]=TradingDataAttribute.DAY;
            attributes[1]=TradingDataAttribute.CLOSING_PRICE;

            //let's take the next 100 of rows
            try {
                StockTradingData data= transactionDataAPI.getTradingData(CompanyStockTransactionsData.DataType.OHLC ,
                        stock,currentTime,attributes,100);

                //if any data was returned
                if(data!=null){

                    //remove the old set of data for this stock and add a new set
                    if(ohlcDataCache.containsKey(stock)){
                        ohlcDataCache.remove(stock);
                    }
                    //add the new data
                    ohlcDataCache.put(stock,data.getTradingData());
                    price=ohlcDataCache.get(stock).get(currentTime).get(TradingDataAttribute.CLOSING_PRICE);
                }
                else{
                    price=-1;
                }

            } catch (DataAccessException e) {
                e.printStackTrace();  //To change body of catch statement use File | Settings | File Templates.
            }

        }

        return price;
    }

    /**
     *
     * @return Company StockId and Name pairs
     * @throws DataAccessException
     */
    public HashMap<String,String> getStocksList() throws DataAccessException {

        return companyDataAPI.getCompanyIDsNames();


    }
}
