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


package org.investovator.dataPlayBackEngine;

import org.investovator.core.data.api.CompanyData;
import org.investovator.core.data.api.CompanyStockTransactionsData;
import org.investovator.core.data.api.utils.TradingDataAttribute;
import org.investovator.core.data.exeptions.DataAccessException;
import org.investovator.dataPlayBackEngine.data.BogusCompnayDataGenerator;
import org.investovator.dataPlayBackEngine.data.BogusHistoryDataGenerator;
import org.investovator.dataPlayBackEngine.scheduler.EventTask;

import java.util.HashMap;
import java.util.Observer;
import java.util.Timer;

/**
 * @author: ishan
 * @version: ${Revision}
 */
public class RealTimeDataPlayer {

    Timer timer;
    EventTask task;
    CompanyStockTransactionsData transactionDataAPI;
    CompanyData companyDataAPI;

    public RealTimeDataPlayer(String[] stocks,String startDate,TradingDataAttribute[] attributes) {
        this.timer = new Timer();
        //for testing
        this.transactionDataAPI =new BogusHistoryDataGenerator();
        this.companyDataAPI=new BogusCompnayDataGenerator();
        //testing end
        task = new EventTask(stocks, startDate, transactionDataAPI,attributes);
    }


    /**
     * To set observers
     *
     * @param observer
     */
    public void setObserver(Observer observer){
        task.setObserver(observer);
    }

    /**
     * Start playing the data
     * @param resolution the time gaps between pushing events
     */
    public void startPlayback(int resolution) {

        timer.schedule(task, 0, resolution * 1000);
        //TODO- change the EventTask to check for the resolution when incrementing its time
    }

    /**
     * Stop the data playback
     */
    public void stopPlayback() {
        task.cancel();
        timer.cancel();
    }

    /**
     *
     * @return Company StockId and Name pairs
     * @throws org.investovator.core.data.exeptions.DataAccessException
     */
    public HashMap<String,String> getStocksList() throws DataAccessException {

        return companyDataAPI.getCompanyIDsNames();


    }


}
