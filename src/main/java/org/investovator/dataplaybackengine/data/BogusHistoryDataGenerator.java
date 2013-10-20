package org.investovator.dataplaybackengine.data;

import org.investovator.core.data.api.CompanyStockTransactionsData;
import org.investovator.core.data.api.utils.StockTradingData;
import org.investovator.core.data.api.utils.StockTradingDataImpl;
import org.investovator.core.data.api.utils.TradingDataAttribute;
import org.investovator.core.data.exeptions.DataAccessException;
import org.investovator.dataplaybackengine.utils.DateUtils;

import java.io.File;
import java.text.ParseException;
import java.util.Date;
import java.util.HashMap;
import java.util.Random;

/**
 * @author: ishan
 * @version: ${Revision}
 */
public class BogusHistoryDataGenerator implements CompanyStockTransactionsData {

    Random rn = new Random(new Date().getTime());

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

            HashMap<TradingDataAttribute, Float> tradingData= new HashMap<TradingDataAttribute, Float>();

            //add attributes
            for(TradingDataAttribute attr:tradingDataAttributes){

                tradingData.put(attr,getRandomNumber());
            }
            marketData.put(time,tradingData);

            //increment the time
            if(dataType==DataType.OHLC){
                time=DateUtils.incrementTimeByDays(1,time);
            }
            else if(dataType==DataType.TICKER){

                time= DateUtils.incrementTimeBySeconds(1,time);
            }


        }

        StockTradingData stockData=new StockTradingDataImpl(symbol,tradingDataAttributes,
                marketData);

        return stockData;
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

}