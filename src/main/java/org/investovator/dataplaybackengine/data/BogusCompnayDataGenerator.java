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


package org.investovator.dataplaybackengine.data;

import org.investovator.core.data.api.CompanyData;
import org.investovator.core.data.api.utils.CompanyInfo;
import org.investovator.core.data.exeptions.DataAccessException;
import org.investovator.core.data.exeptions.DataNotFoundException;

import java.util.ArrayList;
import java.util.HashMap;

/**
 * @author: ishan
 * @version: ${Revision}
 */
public class BogusCompnayDataGenerator implements CompanyData {
    @Override
    public HashMap<String, String> getCompanyIDsNames() throws DataAccessException {
        HashMap<String, String> map =new HashMap<String, String>();
        map.put("APPL","Apple Computers");
        map.put("GOOG","Google Lanka");

        return map;

    }

    @Override
    public void addCompanyData(String symbol, String companyName, int shares) throws DataAccessException {
        //To change body of implemented methods use File | Settings | File Templates.
    }

    @Override
    public HashMap<String, Integer> getCompanyIDsTotalShares() throws DataAccessException {
        return null;  //To change body of implemented methods use File | Settings | File Templates.
    }

    @Override
    public String getCompanyName(String s) throws DataAccessException {
        return null;  //To change body of implemented methods use File | Settings | File Templates.
    }

    @Override
    public int getCompanyNoOfShares(String s) throws DataAccessException {
        return 0;  //To change body of implemented methods use File | Settings | File Templates.
    }

    @Override
    public String getInfo(CompanyInfo companyInfo, String s) throws DataAccessException, DataNotFoundException {
        return null;  //To change body of implemented methods use File | Settings | File Templates.
    }

    @Override
    public void addInfo(CompanyInfo companyInfo, String s, String s2) throws DataAccessException {
        //To change body of implemented methods use File | Settings | File Templates.
    }


    @Override
    public ArrayList<String> getAvailableStockIds() throws DataAccessException {
        return null;  //To change body of implemented methods use File | Settings | File Templates.
    }
}
