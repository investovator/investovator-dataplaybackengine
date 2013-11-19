package org.investovator.dataplaybackengine.datagenerators;

import org.investovator.core.data.api.CompanyStockTransactionsData;
import org.investovator.core.data.api.utils.StockTradingData;
import org.investovator.core.data.api.utils.StockTradingDataImpl;
import org.investovator.core.data.api.utils.TradingDataAttribute;
import org.investovator.core.data.exeptions.DataAccessException;
import org.investovator.dataplaybackengine.utils.DateUtils;

import java.io.File;
import java.text.ParseException;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.Random;

/**
 * @author: ishan
 * @version: ${Revision}
 */
public class BogusHistoryTestDataGenerator implements CompanyStockTransactionsData {

    Random rn = new Random(new Date().getTime());
    boolean googleOHCLReturned =false;
    boolean appleOHCLReturned =false;
    boolean googTickerReturned =false;



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
    public StockTradingData getTradingData(DataType dataType, String symbol, Date startingDate,Date endDate,int numOfRows, ArrayList<TradingDataAttribute> tradingDataAttributes) throws DataAccessException {


        HashMap<Date, HashMap<TradingDataAttribute, String>> marketData = new
                HashMap<Date, HashMap<TradingDataAttribute, String>>();


        Date time=startingDate;
        //generate bogus data
        if(dataType==DataType.OHLC && !appleOHCLReturned && symbol.equals("APPL")){

            int i=0;
            HashMap<TradingDataAttribute, String> tradingData= new HashMap<TradingDataAttribute, String>();

            tradingData.put(TradingDataAttribute.DAY,Integer.toString(i));
            i++;
            tradingData.put(TradingDataAttribute.PRICE,Integer.toString(i));
            i++;
            marketData.put(time,tradingData);
            time=DateUtils.incrementTimeByDays(1,time);

            tradingData= new HashMap<TradingDataAttribute, String>();
            tradingData.put(TradingDataAttribute.DAY,Integer.toString(i));
            i++;
            tradingData.put(TradingDataAttribute.PRICE,Integer.toString(i));
            i++;
            marketData.put(time,tradingData);
            time=DateUtils.incrementTimeByDays(1,time);


            tradingData= new HashMap<TradingDataAttribute, String>();
            tradingData.put(TradingDataAttribute.DAY,Integer.toString(i));
            i++;
            tradingData.put(TradingDataAttribute.PRICE,Integer.toString(i));
            i++;
            marketData.put(time,tradingData);
            time=DateUtils.incrementTimeByDays(1,time);


            tradingData= new HashMap<TradingDataAttribute, String>();
            tradingData.put(TradingDataAttribute.DAY,Integer.toString(i));
            i++;
            tradingData.put(TradingDataAttribute.PRICE,Integer.toString(i));
            i++;
            marketData.put(time,tradingData);

            appleOHCLReturned =true;
            return new StockTradingDataImpl(symbol,tradingDataAttributes,
                    marketData);

        }

        else if(dataType==DataType.OHLC && !googleOHCLReturned && symbol.equals("GOOG")){

            int i=0;
            HashMap<TradingDataAttribute, String> tradingData= new HashMap<TradingDataAttribute, String>();

            tradingData.put(TradingDataAttribute.DAY,Integer.toString(i));
            i++;
            tradingData.put(TradingDataAttribute.PRICE,Integer.toString(i));
            i++;
            marketData.put(time,tradingData);
            time=DateUtils.incrementTimeByDays(1,time);

            tradingData= new HashMap<TradingDataAttribute, String>();
            tradingData.put(TradingDataAttribute.DAY,Integer.toString(i));
            i++;
            tradingData.put(TradingDataAttribute.PRICE,Integer.toString(i));
            i++;
            marketData.put(time,tradingData);
            time=DateUtils.incrementTimeByDays(1,time);


            tradingData= new HashMap<TradingDataAttribute, String>();
            tradingData.put(TradingDataAttribute.DAY,Integer.toString(i));
            i++;
            tradingData.put(TradingDataAttribute.PRICE,Integer.toString(i));
            i++;
            marketData.put(time,tradingData);
            time=DateUtils.incrementTimeByDays(1,time);


            tradingData= new HashMap<TradingDataAttribute, String>();
            tradingData.put(TradingDataAttribute.DAY,Integer.toString(i));
            i++;
            tradingData.put(TradingDataAttribute.PRICE,Integer.toString(i));
            i++;
            marketData.put(time,tradingData);

            googleOHCLReturned =true;
            return new StockTradingDataImpl(symbol,tradingDataAttributes,
                    marketData);

        }

        else if(dataType==DataType.TICKER && !googTickerReturned && symbol.equals("GOOG")){

            int i=0;
            HashMap<TradingDataAttribute, String> tradingData= new HashMap<TradingDataAttribute, String>();

            tradingData.put(TradingDataAttribute.DAY,Integer.toString(i));
            i++;
            tradingData.put(TradingDataAttribute.PRICE,Integer.toString(i));
            i++;
            marketData.put(time,tradingData);
            time=DateUtils.incrementTimeBySeconds(1,time);

            tradingData= new HashMap<TradingDataAttribute, String>();
            tradingData.put(TradingDataAttribute.DAY,Integer.toString(i));
            i++;
            tradingData.put(TradingDataAttribute.PRICE,Integer.toString(i));
            i++;
            marketData.put(time,tradingData);
            time=DateUtils.incrementTimeBySeconds(1,time);


            tradingData= new HashMap<TradingDataAttribute, String>();
            tradingData.put(TradingDataAttribute.DAY,Integer.toString(i));
            i++;
            tradingData.put(TradingDataAttribute.PRICE,Integer.toString(i));
            i++;
            marketData.put(time,tradingData);
            time=DateUtils.incrementTimeBySeconds(1,time);


            tradingData= new HashMap<TradingDataAttribute, String>();
            tradingData.put(TradingDataAttribute.DAY,Integer.toString(i));
            i++;
            tradingData.put(TradingDataAttribute.PRICE,Integer.toString(i));
            i++;
            marketData.put(time,tradingData);

            googTickerReturned =true;
            return new StockTradingDataImpl(symbol,tradingDataAttributes,
                    marketData);

        }
        else {
            throw new DataAccessException("Data is over");
        }


        //todo - handle the REalTime players data tests

    }

    @Override
    public Date[] getDataDaysRange(DataType dataType, String s) {
        int numOfDates=20;
        Date[] dates=new Date[numOfDates];
        for(int i=0;i<numOfDates;i++){
            if(i==0){
                if(dataType==DataType.OHLC){
                    try {
                        dates[i]=DateUtils.dateStringToDateObject("2011-12-13-15-55-32",DateUtils.DATE_FORMAT_1);
                    } catch (ParseException e) {
                        e.printStackTrace();  //To change body of catch statement use File | Settings | File Templates.
                    }
                } else{

                    try {
                        dates[i]=DateUtils.dateStringToDateObject("2011-12-13-15-55-35",DateUtils.DATE_FORMAT_1);
                    } catch (ParseException e) {
                        e.printStackTrace();  //To change body of catch statement use File | Settings | File Templates.
                    }
                }
            }
            else{
                if(dataType==DataType.TICKER){
                    //increase by one second than the i-1 th date
                    dates[i]=DateUtils.decrementTimeBySeconds(1,dates[i-1]);
                }else{
                    //increase by one day than the i-1 th date
                    dates[i]=DateUtils.decrementTimeByDays(1,dates[i-1]);
                }
            }
        }
        return dates;
    }

    @Override
    public void importCSV(DataType type, String stockId, String dateFormat, File file) throws DataAccessException {
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

    @Override
    public boolean isDataAvailable(DataType type, String stockId) throws DataAccessException {
        return true;  //To change body of implemented methods use File | Settings | File Templates.
    }

}
