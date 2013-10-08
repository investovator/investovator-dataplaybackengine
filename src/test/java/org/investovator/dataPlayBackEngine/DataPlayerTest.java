package org.investovator.dataPlayBackEngine;

import org.investovator.core.excelimporter.HistoryData;
import org.junit.Test;

import java.util.Date;

/**
 * @author: ishan
 * @version: ${Revision}
 */
public class DataPlayerTest {
    @Test
    public void testRunPlayback() throws Exception {

    }

    @Test
    public void testStopPlayback() throws Exception {

    }

    @Test
    public void testGetOHLCPrice() throws Exception {
        DataPlayer player=new DataPlayer("Goog");
        HistoryData data=player.getOHLCPrice("G","2012-10-3-19-45-33");

        for(int a=0;a<5;a++){

            System.out.println(data.getOpeningPrice()+"::"+data.getClosingPrice());
            Thread.sleep(2000);
        }



    }
}
