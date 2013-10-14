package org.investovator.dataPlayBackEngine;

import org.investovator.core.data.api.CompanyStockTransactionsData;
import org.investovator.core.data.api.utils.StockTradingData;
import org.investovator.core.data.api.utils.TradingDataAttribute;
import org.investovator.core.data.exeptions.DataAccessException;
import org.investovator.dataPlayBackEngine.data.BogusHistoryDataGenerator;
import org.investovator.dataPlayBackEngine.scheduler.EventTask;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashMap;
import java.util.Observer;
import java.util.Timer;

/**
 * @author: ishan
 * @version: ${Revision}
 */
public class DataPlayer {

    Timer timer;
    EventTask task;
    CompanyStockTransactionsData dataAPI;


   //to cache the stock trading data items
    HashMap<String,HashMap<Date, HashMap<TradingDataAttribute, Float>>> ohlcDataCache;

    public DataPlayer(String stock) {
        this.timer = new Timer();
        //for testing
        this.dataAPI=new BogusHistoryDataGenerator();
        task = new EventTask(stock, "2011-12-13-15-55-32",dataAPI);

        ohlcDataCache=new HashMap<String, HashMap<Date, HashMap<TradingDataAttribute, Float>>>();

    }

    public void setObserver(Observer observer){
        task.setObserver(observer);
    }

    public void runPlayback(int resolution) {

        timer.schedule(task, 0, resolution * 1000);
    }

    public void stopPlayback() {
        timer.cancel();
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

        SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd-kk-mm-ss"); //should be in format year-month-date-24hr-minute-second
        try {
            currentTime =format.parse(date);

        } catch (ParseException e) {
            e.printStackTrace();
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
                StockTradingData data=dataAPI.getTradingDataOHLC(stock,currentTime,attributes,100);

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
}
