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

    Random rn = new Random(new Date().getTime());




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
//
////    @Override
//    public StockTradingData getTradingDataOHLC(String s, Date date, TradingDataAttribute[] tradingDataAttributes, int i) throws DataAccessException {
//
//        HashMap<Date, HashMap<TradingDataAttribute, Float>> marketData =new HashMap<Date, HashMap<TradingDataAttribute, Float>>();
//
//        Date time=date;
//        for(int j=0;j<i;j++){
//            //increment the time
//            if(j!=0){
//                time=incrementTimeBySeconds(1,time);
//            }
//
//            HashMap<TradingDataAttribute, Float> attributesMap= new HashMap<TradingDataAttribute, Float>();
//            //add each attribute
//            for(TradingDataAttribute attr:tradingDataAttributes){
//                //generate a random value
//
//
//
//
//                attributesMap.put(attr,getRandomNumber());
//            }
//
//            marketData.put(time,attributesMap);
//
//        }
//
//        return new StockTradingDataImpl(s,tradingDataAttributes,marketData);
//
//    }
//
////    @Override
//    public HashMap<Date, Float> getTradingData(String s, Date date, int i) throws DataAccessException {
//        HashMap<Date, Float> data= new HashMap<Date, Float>();
//
//        Date time=date;
//        for(int j=0;j<i;j++){
//            //increment the time
//            time=incrementTimeBySeconds(1,time);
//
//
//
//            data.put(time,getRandomNumber());
//        }
//
//        return data;
//    }


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

    private float getRandomNumber(){
        //generate a random value

        int maximum=1000;
        int minimum=100;
        int n = maximum - minimum + 1;
        int k = rn.nextInt() % n;
        if(k<0) k=k*-1;
        float randomNum =  minimum + k;

        return  randomNum;
    }

    @Override
    public StockTradingData getTradingData(DataType dataType, String symbol, Date startingDate, TradingDataAttribute[] tradingDataAttributes, int numOfRows) throws DataAccessException {


        HashMap<Date, HashMap<TradingDataAttribute, Float>> marketData = new
                HashMap<Date, HashMap<TradingDataAttribute, Float>>();

        Date time=startingDate;
        for(int j=0;j<numOfRows;j++){
            //increment the time
            time=incrementTimeBySeconds(1,time);
            HashMap<TradingDataAttribute, Float> tradingData= new HashMap<TradingDataAttribute, Float>();

            //add attributes
            for(TradingDataAttribute attr:tradingDataAttributes){

                tradingData.put(attr,getRandomNumber());
            }
            marketData.put(time,tradingData);


        }

        StockTradingData stockData=new StockTradingDataImpl(symbol,tradingDataAttributes,
                marketData);

        return stockData;
    }

    @Override
    public Date[] getDataDaysRange(DataType dataType, String s) {
        return new Date[0];  //To change body of implemented methods use File | Settings | File Templates.
    }

    @Override
    public void importCSV(DataType dataType, String s, File file) throws DataAccessException {
        //To change body of implemented methods use File | Settings | File Templates.
    }

    @Override
    public void importXls(DataType dataType, String s, File file) throws DataAccessException {
        //To change body of implemented methods use File | Settings | File Templates.
    }

    @Override
    public void clearTradingData(DataType dataType, String s) throws DataAccessException {
        //To change body of implemented methods use File | Settings | File Templates.
    }

    @Override
    public void clearAllTradingData(DataType dataType) throws DataAccessException {
        //To change body of implemented methods use File | Settings | File Templates.
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
