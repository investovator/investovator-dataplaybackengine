/*
 * investovator, Stock Market Gaming Framework
 *     Copyright (C) 2013  investovator
 *
 *     This program is free software: you can redistribute it and/or modify
 *     it under the terms of the GNU General Public License as published by
 *     the Free Software Foundation, either version 3 of the License, or
 *     (at your option) any later version.
 *
 *     This program is distributed in the hope that it will be useful,
 *     but WITHOUT ANY WARRANTY; without even the implied warranty of
 *     MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 *     GNU General Public License for more details.
 *
 *     You should have received a copy of the GNU General Public License
 *     along with this program.  If not, see <http://www.gnu.org/licenses/>.
 */


package org.investovator.dataplaybackengine;

import org.investovator.core.data.api.utils.TradingDataAttribute;
import org.investovator.dataplaybackengine.datagenerators.BogusHistoryTestDataGenerator;
import org.investovator.dataplaybackengine.events.StockUpdateEvent;
import org.investovator.dataplaybackengine.exceptions.GameFinishedException;
import org.investovator.dataplaybackengine.player.DailySummaryDataPLayer;
import org.investovator.dataplaybackengine.utils.DateUtils;
import org.junit.Before;
import org.junit.Test;

import java.util.ArrayList;

/**
 * @author: ishan
 * @version: ${Revision}
 */
public class DailySummaryDataPLayerSinglePlayerTest {

    DailySummaryDataPLayer player;
    boolean notStarted;

    private static String APPL="APPL";
    private static String GOOG="GOOG";

    @Before
    public void setUp() throws Exception {

        notStarted=true;

        String[] stocks=new String[2];
        stocks[0]=GOOG;
        stocks[1]=APPL;
        String startDate="2011-12-13-15-55-32";

        //define the attributes needed
        ArrayList<TradingDataAttribute> attributes=new ArrayList<TradingDataAttribute>();

        //just the closing price is enough for now
        attributes.add(TradingDataAttribute.DAY);
        attributes.add(TradingDataAttribute.PRICE);

        //create a multiplayer game
        player=new DailySummaryDataPLayer(stocks,attributes,TradingDataAttribute.PRICE,false);

        //set the date
        player.setStartDate(startDate, DateUtils.DATE_FORMAT_1);

        //set the data api
        player.setTransactionDataAPI(new BogusHistoryTestDataGenerator());

        player.joinSingleplayerGame();
    }

    @Test
    public void testGamePlay() throws Exception {
        boolean gameRunning=false;
        StockUpdateEvent[] events=null;
        int googCounter=0;
        int applCounter=0;

        if(notStarted){
              events=player.startGame();
            gameRunning=true;
        }
        while (gameRunning){
            for(StockUpdateEvent event:events){
                if(event.getStockId().equalsIgnoreCase(APPL)){
                    assert(event.getData().get(TradingDataAttribute.DAY)==applCounter++);
                    assert(event.getData().get(TradingDataAttribute.PRICE)==applCounter++);

                }
                else if(event.getStockId().equalsIgnoreCase(GOOG)){
                    assert(event.getData().get(TradingDataAttribute.DAY)==googCounter++);
                    assert(event.getData().get(TradingDataAttribute.PRICE)==googCounter++);
                }
            }
            try {

                events=player.playNextDay();
            } catch (GameFinishedException ex){
                gameRunning=false;
            }
        }


    }
}
