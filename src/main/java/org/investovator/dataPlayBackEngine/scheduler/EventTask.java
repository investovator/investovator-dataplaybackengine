package org.investovator.dataPlayBackEngine.scheduler;

import org.investovator.core.data.api.CompanyStockTransactionsData;
import org.investovator.core.data.api.utils.StockTradingData;
import org.investovator.core.data.api.utils.TradingDataAttribute;
import org.investovator.core.data.exeptions.DataAccessException;
import org.investovator.dataPlayBackEngine.events.EventManager;
import org.investovator.dataPlayBackEngine.events.StockEvent;
import org.investovator.dataPlayBackEngine.events.StockEventComparator;
import org.investovator.dataPlayBackEngine.exceptions.GameFinishedException;
import org.investovator.dataPlayBackEngine.utils.DateUtils;

import java.text.ParseException;
import java.util.*;

/**
 * @author: ishan
 * @version: ${Revision}
 */
public class EventTask extends TimerTask {

    //used to determine the cache size
    public static int CACHE_SIZE=100;

    //start time
    private Date currentTime;
    //Data interface
    private CompanyStockTransactionsData dataApi;

    //to cache the stock trading data items. (will allow events to have the duplicate times)
    private PriorityQueue<StockEvent> dataCache;
    //to store the stocks to watch
    private ArrayList<String> stocks;

    private EventManager eventManager;

    //attributes to fetch
    TradingDataAttribute[] attributes;


    public EventTask(String[] stocksToWatch, String startT, CompanyStockTransactionsData api,
                     TradingDataAttribute[] attributes) {
        this.dataApi=api;
        eventManager=new EventManager();
        this.attributes=attributes;


        Comparator<StockEvent> comparator=new StockEventComparator();
        dataCache =new PriorityQueue<StockEvent>(EventTask.CACHE_SIZE,comparator );
        stocks=new ArrayList<String>(Arrays.asList(stocksToWatch));

        try {
            currentTime=DateUtils.dateStringToDateObject(startT,DateUtils.DATE_FORMAT_1);
        } catch (ParseException e) {
            e.printStackTrace();
        }

    }

    @Override
    public void run() {

        boolean inCache=fireEvents();
        //if no events were fired
        //TODO - this part would get called even if there was actually no stock events in the dataset in that time
        if(!inCache){
            try {
                refreshCache();
            } catch (GameFinishedException e) {
                //notify the player
                eventManager.notifyListners(EventManager.RealTimePlayerStates.GAME_OVER);
                //stop the timer
                this.cancel();
            }
            fireEvents();
        }
        // in order to point to the next time interval
        currentTime= DateUtils.incrementTimeBySeconds(1, currentTime);

    }


    public void setObserver(Observer observer){
        eventManager.addObserver(observer);
    }

    private void refreshCache() throws GameFinishedException {

        //to track whether at least a single stock has data in the future
        boolean hasData=false;

            //for each stock, search for events
            for(String stock:stocks){

                try {
                    //todo- Assumed that the maximum resolution of "time" for the data in the data base is 1 second
                    StockTradingData data=dataApi.getTradingData(CompanyStockTransactionsData.DataType.TICKER,
                            stock,currentTime,attributes,EventTask.CACHE_SIZE);

                    HashMap<Date, HashMap<TradingDataAttribute, Float>> stockData= data.getTradingData();

                    //add each event to the cache
                    for(Date time:stockData.keySet()){
                        StockEvent event= new StockEvent(stock,stockData.get(time),time);
                        dataCache.add(event);
                    }

                    hasData=true;

                } catch (DataAccessException e) {
                    e.printStackTrace();  //To change body of catch statement use File | Settings | File Templates.
                }


            }
        //if none of the stocks has data in the future
        if(!hasData){
            throw new GameFinishedException("Ticker");
        }

    }

    private boolean fireEvents(){

        boolean inCache=false;

        //while there are elements in the cache
        while (dataCache.size()>0){
            //non-destructively get the head of the queue
            StockEvent event= dataCache.peek();
            //if the event has occurred in the past
            if(event.getTime().before(currentTime)){
                //remove that item from the queue
                dataCache.poll();
                //fire the event
                eventManager.notifyListners(event);
                inCache=true;
            }
            //if the rest of the events are events of the future
            else{
                //do not loop anymore
                break;
            }

            }

        return  inCache;
    }

}
