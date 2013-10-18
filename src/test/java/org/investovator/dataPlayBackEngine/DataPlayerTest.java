package org.investovator.dataPlayBackEngine;

import org.investovator.dataPlayBackEngine.data.BogusCompnayDataGenerator;
import org.investovator.dataPlayBackEngine.utils.DateUtils;
import org.junit.Before;
import org.junit.Test;

import java.util.HashMap;

/**
 * @author: ishan
 * @version: ${Revision}
 */
public class DataPlayerTest {

    OHLCDataPLayer player;

    @Before
    public void setUp() throws Exception {

        String[] stocks=new String[2];
        stocks[0]="GOOG";
        stocks[1]="APPL";
        String startDate="2011-12-13-15-55-32";
        player=new OHLCDataPLayer(stocks,startDate, DateUtils.DATE_FORMAT_1);


    }

    @Test
    public void testRunPlayback() throws Exception {

    }

    @Test
    public void testStopPlayback() throws Exception {

    }

    @Test
    public void testGetOHLCPrice() throws Exception {


        for(int a=0;a<5;a++){

            float data=player.getOHLCPrice("G","2012-10-3-19-45-3"+Integer.toString(a));

            System.out.println("2012-10-3-19-45-3"+Integer.toString(a)+"-->"+data);
            Thread.sleep(100);
        }



    }

    @Test
    public void testGetStocksList() throws Exception{
        HashMap<String, String> map= player.getStocksList();

        for(String stock:map.keySet()){
            if(stock.equalsIgnoreCase("APPL")){
                assert (map.get(stock).equalsIgnoreCase("Apple Computers"));
            }
            else if(stock.equalsIgnoreCase("GOOG")){
                assert (map.get(stock).equalsIgnoreCase("Google Lanka"));
            }
        }
    }
}
