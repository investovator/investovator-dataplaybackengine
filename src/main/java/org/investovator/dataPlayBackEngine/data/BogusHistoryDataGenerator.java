package org.investovator.dataPlayBackEngine.data;

import org.investovator.core.data.HistoryDataAPI;
import org.investovator.core.data.types.HistoryOrderData;
import org.investovator.core.excelimporter.HistoryData;


import java.util.Date;

/**
 * @author: ishan
 * @version: ${Revision}
 */
public class BogusHistoryDataGenerator implements HistoryDataAPI {
    @Override
    public HistoryOrderData[] getTradingData(Date startTime, Date endTime, String stockId) {

        HistoryOrderData arr[] =new HistoryOrderData[2];
        int i=0;
        for(int j=0;j<arr.length;j++){
            arr[j]=new HistoryOrderData("2012-10-3-19-45-33",i++,23.04,"Goog",false);
        }

        return arr;


    }

    @Override
    public HistoryData getOHLCPData(Date date, String s) {

        //create random numbers
        int min=700;
        int max=1500;
        int num=min + (int)(Math.random() * ((max - min) + 1));


        HistoryData data=new HistoryData();
        data.setDate(date.toString());
        data.setClosingPrice(num+100);
        data.setHighPrice(num+100);
        data.setLowPrice(num+1);
        data.setOpeningPrice(num+50);

        return data;
    }
}
