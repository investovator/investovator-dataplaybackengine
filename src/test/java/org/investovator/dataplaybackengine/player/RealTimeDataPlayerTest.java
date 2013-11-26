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


package org.investovator.dataplaybackengine.player;

import org.investovator.core.commons.utils.Portfolio;
import org.investovator.core.data.api.utils.TradingDataAttribute;
import org.investovator.dataplaybackengine.datagenerators.UserDataCustomImpl;
import org.investovator.dataplaybackengine.datagenerators.BogusCompnayTestDataGenerator;
import org.investovator.dataplaybackengine.datagenerators.BogusHistoryTestDataGenerator;
import org.investovator.dataplaybackengine.market.OrderType;
import org.investovator.dataplaybackengine.util.GameObserver;
import org.junit.Test;

import java.util.ArrayList;
import java.util.Date;

/**
 * @author: ishan
 * @version: ${Revision}
 */
public class RealTimeDataPlayerTest {
    @Test
    public void testExecuteOrder() throws Exception {
        RealTimeDataPlayer player;

        String username="test";

        String[] stocks=new String[1];
        stocks[0]="GOOG";
        String startDate="2011-12-13-15-55-32";

        //define the attributes needed
        ArrayList<TradingDataAttribute> attributes=new ArrayList<TradingDataAttribute>();

        //just the closing price is enough for now
        attributes.add(TradingDataAttribute.DAY);
        attributes.add(TradingDataAttribute.PRICE);

        //create a multiplayer game
//        player=new DailySummaryDataPLayer(stocks,attributes,TradingDataAttribute.PRICE,false,new UserDataCustomImpl(),
//                new BogusCompnayTestDataGenerator(),new BogusHistoryTestDataGenerator());

        player=new RealTimeDataPlayer(stocks,new Date(),attributes,TradingDataAttribute.PRICE,false,
                new UserDataCustomImpl(),
                new BogusCompnayTestDataGenerator(),new BogusHistoryTestDataGenerator());

        //set the date
//        player.setStartDate(startDate, DateUtils.DATE_FORMAT_1);

        GameObserver observer=new GameObserver();

//        //to keep track of which item this is
//        int counter=0;

        player.joinGame(observer,username );

        player.startGame();

//        StockUpdateEvent event=(StockUpdateEvent)observer.getEvents().get(0);

        //wait two seconds
        Thread.sleep(2000);

        //buy a stock
        player.executeOrder(stocks[0],100, OrderType.BUY,username);
        //check the portfolio for that stock
        Portfolio portfolio=player.getMyPortfolio(username);
        assert(portfolio.getShares().size()>0);

        //stop the player
        player.stopGame();

    }
}
