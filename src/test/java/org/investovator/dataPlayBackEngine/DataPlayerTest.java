package org.investovator.dataPlayBackEngine;

import org.junit.Test;

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
        String[] stocks=new String[2];
        stocks[0]="GOOG";
        stocks[1]="APPL";
        DataPlayer player=new DataPlayer(stocks);
        float data=player.getOHLCPrice("G","2012-10-3-19-45-33");

        for(int a=0;a<5;a++){

            System.out.println(data);
            Thread.sleep(2000);
        }



    }
}
