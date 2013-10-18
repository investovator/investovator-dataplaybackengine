package org.investovator.dataPlayBackEngine.scheduler;

import org.investovator.core.data.api.CompanyStockTransactionsData;
import org.investovator.core.data.api.utils.StockTradingData;
import org.investovator.core.data.api.utils.TradingDataAttribute;
import org.investovator.core.data.exeptions.DataAccessException;
import org.investovator.dataPlayBackEngine.events.EventManager;
import org.investovator.dataPlayBackEngine.events.StockEvent;
import org.investovator.dataPlayBackEngine.events.StockEventComparator;

import java.text.ParseException;
import java.text.SimpleDateFormat;
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


    public EventTask(String[] stocksToWatch, String startT, CompanyStockTransactionsData api) {
        this.dataApi=api;
        eventManager=new EventManager();


        Comparator<StockEvent> comparator=new StockEventComparator();
        dataCache =new PriorityQueue<StockEvent>(EventTask.CACHE_SIZE,comparator );
        stocks=new ArrayList<String>(Arrays.asList(stocksToWatch));

        SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd-kk-mm-ss"); //should be in format year-month-date-24hr-minute-second
        try {
             currentTime =format.parse(startT);

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
            refreshCache();
            fireEvents();
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

    private void refreshCache(){

            //for each stock, search for events
            for(String stock:stocks){
                //define the attributes needed
                TradingDataAttribute attributes[]=new TradingDataAttribute[2];

                //just the closing price is enough for now
                attributes[0]=TradingDataAttribute.DAY;
                attributes[1]=TradingDataAttribute.PRICE;

                try {
                    //todo- Assumed that the maximum resolution of "time" for the data in the data base is 1 second
                    StockTradingData data=dataApi.getTradingData(CompanyStockTransactionsData.DataType.TICKER,
                            stock,currentTime,attributes,100);

                    HashMap<Date, HashMap<TradingDataAttribute, Float>> stockData= data.getTradingData();

                    //add each event to the cache
                    for(Date time:stockData.keySet()){
                        StockEvent event= new StockEvent(stock,stockData.get(time),time);
                        dataCache.add(event);
                    }

                } catch (DataAccessException e) {
                    e.printStackTrace();  //To change body of catch statement use File | Settings | File Templates.
                }


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
