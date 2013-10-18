package org.investovator.dataPlayBackEngine.scheduler;

import org.investovator.core.data.api.CompanyStockTransactionsData;
import org.investovator.core.data.api.utils.StockTradingData;
import org.investovator.core.data.api.utils.TradingDataAttribute;
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
    TreeMap<Date,HashMap<String, HashMap<TradingDataAttribute, Float>>> dataCache;

    EventManager eventManager;


    public EventTask(String[] stocks, String startT, CompanyStockTransactionsData api) {
        this.dataApi=api;
        eventManager=new EventManager();

        dataCache=new TreeMap<Date,HashMap<String, HashMap<TradingDataAttribute, Float>>>();

        newDataCache=new PriorityQueue<StockEvent>();

//        //add the stocks to the cache
//        for(String stock:stocks){
//            dataCache.put(stock,new HashMap<Date,Float>());
//        }

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

        //get the days that are less than the required time
        for(Date time:dataCache.keySet()){
            if()

        }




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



            ////////////////////////////////

            //if entries exist for this date/time
            if(dataCache.containsKey(currentTime)){
                HashMap<String, HashMap<TradingDataAttribute, Float>> event= new
                        HashMap<String, HashMap<TradingDataAttribute, Float>>();

                //iterate all the stocks for events
                for(String stock:dataCache.get(currentTime).keySet()){
                    //put the events
                    event.put(stock,dataCache.get(currentTime).)
                }
            }

            ////////////////////////////////

            //for each stock, search for events
            for(String stock:dataCache.keySet()){
                try {

                    //define the attributes needed
                    TradingDataAttribute attributes[]=new TradingDataAttribute[2];

                    //just the closing price is enough for now
                    attributes[0]=TradingDataAttribute.DAY;
                    attributes[1]=TradingDataAttribute.PRICE;

                    StockTradingData data=dataApi.getTradingData(CompanyStockTransactionsData.DataType.TICKER,
                            stock,currentTime,attributes,100);

                    HashMap<Date, HashMap<TradingDataAttribute, Float>> stockData= data.getTradingData();

                    //todo- Assumed that the maximum resolution of "time" for the data in the data base is 1 second
                    //if there are events matching the current time stamp
                    if(stockData.containsKey(currentTime)){


                        eventManager.notifyListners(stockData.get(currentTime));
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
