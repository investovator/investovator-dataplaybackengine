package org.investovator.dataPlayBackEngine.data;

import org.investovator.core.data.api.CompanyStockTransactionsData;
import org.investovator.core.data.api.utils.StockTradingData;
import org.investovator.core.data.api.utils.StockTradingDataImpl;
import org.investovator.core.data.api.utils.TradingDataAttribute;
import org.investovator.core.data.exeptions.DataAccessException;

import java.io.File;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.Random;

/**
 * @author: ishan
 * @version: ${Revision}
 */
public class BogusHistoryDataGenerator implements CompanyStockTransactionsData {





//    //old
//    public HistoryOrderData[] getTradingData(Date startTime, Date endTime, String stockId) {
//
//        HistoryOrderData arr[] =new HistoryOrderData[2];
//        int i=0;
//        for(int j=0;j<arr.length;j++){
//            arr[j]=new HistoryOrderData("2012-10-3-19-45-33",i++,23.04,"Goog",false);
//        }
//
//        return arr;
//
//
//    }

    @Override
    public StockTradingData getTradingDataOHLC(String s, Date date, TradingDataAttribute[] tradingDataAttributes, int i) throws DataAccessException {

        HashMap<Date, HashMap<TradingDataAttribute, Float>> marketData =new HashMap<Date, HashMap<TradingDataAttribute, Float>>();

        Date time=date;
        for(int j=0;j<i;j++){
            //increment the time
            time=incrementTimeBySeconds(1,time);

            HashMap<TradingDataAttribute, Float> attributesMap= new HashMap<TradingDataAttribute, Float>();
            //add each attribute
            for(TradingDataAttribute attr:tradingDataAttributes){
                //generate a random value
                Random rn = new Random();
                int maximum=1000;
                int minimum=100;
                int n = maximum - minimum + 1;
                int k = rn.nextInt() % n;
                float randomNum =  minimum + i;


                attributesMap.put(attr,randomNum);
            }

        }

        return new StockTradingDataImpl(s,tradingDataAttributes,marketData);

    }

    @Override
    public HashMap<Date, Float> getTradingData(String s, Date date, int i) throws DataAccessException {
        HashMap<Date, Float> data= new HashMap<Date, Float>();

        Date time=date;
        for(int j=0;j<i;j++){
            //increment the time
            time=incrementTimeBySeconds(1,time);

            //generate a random value
            Random rn = new Random();
            int maximum=1000;
            int minimum=100;
            int n = maximum - minimum + 1;
            int k = rn.nextInt() % n;
            float randomNum =  minimum + i;

            data.put(time,randomNum);
        }

        return data;
    }

    @Override
    public void importCSV(String s, File file) throws DataAccessException {
        //To change body of implemented methods use File | Settings | File Templates.
    }

    @Override
    public void clearTradingData(String s) throws DataAccessException {
        //To change body of implemented methods use File | Settings | File Templates.
    }

    /**
     * Increases the current time by the given number of seconds
     *
     * @param seconds the number of seconds to increase by
     */
    private Date incrementTimeBySeconds(int seconds, Date currentTime){
        Calendar cal = Calendar.getInstance();
        cal.setTime(currentTime);
        cal.add(Calendar.SECOND, seconds); //minus number would decrement the days
        return cal.getTime();

    }

//    //old
//    public HistoryData getOHLCPData(Date date, String s) {
//
//        //create random numbers
//        int min=700;
//        int max=1500;
//        int num=min + (int)(Math.random() * ((max - min) + 1));
//
//
//        HistoryData data=new HistoryData();
//        data.setDate(date.toString());
//        data.setClosingPrice(num+100);
//        data.setHighPrice(num+100);
//        data.setLowPrice(num+1);
//        data.setOpeningPrice(num+50);
//
//        return data;
//    }
}
