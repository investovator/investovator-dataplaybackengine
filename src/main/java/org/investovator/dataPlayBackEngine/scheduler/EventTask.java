package org.investovator.dataPlayBackEngine.scheduler;

import org.investovator.core.data.api.CompanyStockTransactionsData;
import org.investovator.core.data.exeptions.DataAccessException;
import org.investovator.dataPlayBackEngine.events.EventManager;
import org.investovator.dataPlayBackEngine.events.StockEvent;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.*;
import java.util.concurrent.ConcurrentHashMap;

/**
 * @author: ishan
 * @version: ${Revision}
 */
public class EventTask extends TimerTask {
    int i=0;

    //start time
    private Date currentTime;
    //Data interface
    CompanyStockTransactionsData dataApi;

    //to cache the stock trading data items
    ConcurrentHashMap<String,HashMap<Date,Float>> dataCache;

    EventManager eventManager;


    public EventTask(String[] stocks, String startT, CompanyStockTransactionsData api) {
        this.dataApi=api;
        eventManager=new EventManager();

        dataCache=new ConcurrentHashMap<String,HashMap<Date,Float>>();

        //add the stocks to the cache
        for(String stock:stocks){
            dataCache.put(stock,new HashMap<Date,Float>());
        }

        //String sourceDate=currentTime;
        SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd-kk-mm-ss"); //should be in format year-month-date-24hr-minute-second
        try {
             currentTime =format.parse(startT);

        } catch (ParseException e) {
            e.printStackTrace();
        }

    }

    @Override
    public void run() {

        boolean inCache=false;

        //iterate all the stocks
        for(String stock:dataCache.keySet()){

            //if the cache for a stock is not empty
            if(!dataCache.get(stock).isEmpty()){
                //if the requested date is in the cache
                if(dataCache.get(stock).containsKey(currentTime)){
                    inCache=true;

                    //todo- Assumed that the maximum resolution of "time" for the data in the data base is 1 second
                    //if there are events matching the current time stamp
                    eventManager.notifyListners(new StockEvent(stock,dataCache.get(stock).get(currentTime),currentTime));
                    //remove the fired event related dataset
                    dataCache.get(stock).remove(currentTime);


                }

            }
        }


        //if a matching item to the time stamp was not in the cache
        //TODO - this part would get called even if there was actually no stock events in the dataset in that time
        if (!inCache){
            //for each stock, search for events
            for(String stock:dataCache.keySet()){
                try {
                    HashMap<Date, Float> stockData=dataApi.getTradingData(stock,currentTime,100);
                    //todo- Assumed that the maximum resolution of "time" for the data in the data base is 1 second
                    //if there are events matching the current time stamp
                    if(stockData.containsKey(currentTime)){
                        eventManager.notifyListners(new StockEvent(stock,stockData.get(currentTime),currentTime));
                        //remove the fired event related dataset
                        stockData.remove(currentTime);
                    }

                    //update the cache
                    dataCache.remove(stock);
                    dataCache.put(stock,stockData);

                } catch (DataAccessException e) {
                    e.printStackTrace();  //To change body of catch statement use File | Settings | File Templates.
                }
            }

        }


        // in order to point to the next time interval
        currentTime=incrementTimeBySeconds(1);
    }


    /**
     * Increases the current time by the given number of seconds
     *
     * @param seconds the number of seconds to increase by
     */
    private Date incrementTimeBySeconds(int seconds){
        Calendar cal = Calendar.getInstance();
        cal.setTime(currentTime);
        cal.add(Calendar.SECOND, seconds); //minus number would decrement the days
        return cal.getTime();

    }

    public void setObserver(Observer observer){
        eventManager.addObserver(observer);
    }
}
