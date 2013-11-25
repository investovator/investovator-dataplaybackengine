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


package org.investovator.dataplaybackengine.game;

import org.investovator.core.data.api.utils.TradingDataAttribute;
import org.investovator.dataplaybackengine.GameObserver;
import org.investovator.dataplaybackengine.data.UserDataCustomImpl;
import org.investovator.dataplaybackengine.datagenerators.BogusCompnayTestDataGenerator;
import org.investovator.dataplaybackengine.datagenerators.BogusHistoryTestDataGenerator;
import org.investovator.dataplaybackengine.events.PlaybackFinishedEvent;
import org.investovator.dataplaybackengine.events.StockUpdateEvent;
import org.investovator.dataplaybackengine.player.DailySummaryDataPLayer;
import org.investovator.dataplaybackengine.player.RealTimeDataPlayer;
import org.investovator.dataplaybackengine.utils.DateUtils;
import org.junit.Before;
import org.junit.Test;

import java.util.ArrayList;

/**
 * @author: ishan
 * @version: ${Revision}
 */
public class RealTimeDataPlayerTest {

    RealTimeDataPlayer player;
    GameObserver observer;

    //to keep track of which item this is
    int counter=0;

    public RealTimeDataPlayerTest( ) {
        observer=new GameObserver();
    }

    @Before
    public void setUp() throws Exception {

        String[] stocks=new String[1];
        stocks[0]="GOOG";
        //stocks[1]="APPL";
        String startDate="2011-12-13-15-55-32";

        //define the attributes needed
        ArrayList<TradingDataAttribute> attributes=new ArrayList<TradingDataAttribute>();

        //just the closing price is enough for now
        attributes.add(TradingDataAttribute.DAY);
        attributes.add(TradingDataAttribute.PRICE);

        //create a multiplayer game
        player=new RealTimeDataPlayer(stocks,DateUtils.dateStringToDateObject(startDate,DateUtils.DATE_FORMAT_1),
                attributes,TradingDataAttribute.PRICE,
                true,new UserDataCustomImpl(),
                new BogusCompnayTestDataGenerator(),new BogusHistoryTestDataGenerator());


        //set the data api
//        player.setTransactionDataAPI(new BogusHistoryTestDataGenerator());

        player.joinGame(observer,"test");
        player.startPlayback(1);

    }

    @Test
    public void testGamePlay() throws Exception {
        while (!observer.isGameFinished()){
            boolean matches=false;

            if(observer.getEvents().size()>0){
                StockUpdateEvent event=(StockUpdateEvent)observer.getEvents().remove(0);

                if (event.getData().get(TradingDataAttribute.DAY)==counter){
                    counter++;
                    if(event.getData().get(TradingDataAttribute.PRICE)==counter) {
                        matches=true;
                        counter++;

                    }


                }
                assert(matches);
            }

            else{
                Thread.sleep(100);
            }

        }

        //if the game has finished, there should be a Game end event
        if(observer.getEvents().size()==1){
            if(observer.getEvents().remove(0) instanceof PlaybackFinishedEvent){
                assert(true);
            }
            else{
                assert(false);
            }


        }
        else{
            assert(false);
        }

    }
}
