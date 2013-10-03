package org.investovator.dataPlayBackEngine.data;

import org.investovator.core.org.investovator.data.HistoryDataAPI;
import org.investovator.core.org.investovator.data.types.HistoryOrderData;

import java.util.Date;

/**
 * @author: ishan
 * @version: ${Revision}
 */
public class BogusHistoryDataGenerator implements HistoryDataAPI {
    @Override
    public HistoryOrderData[] getData(Date startTime, Date endTime, String stockId) {

        HistoryOrderData arr[] =new HistoryOrderData[4];
        int i=0;
        for(int j=0;j<arr.length;j++){
            arr[j]=new HistoryOrderData("2012-10-3-19-45-33",i++,23.04,"Goog",false);
        }

        return arr;


    }
}
