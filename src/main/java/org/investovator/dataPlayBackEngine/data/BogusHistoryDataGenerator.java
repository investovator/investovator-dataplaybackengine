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

}
