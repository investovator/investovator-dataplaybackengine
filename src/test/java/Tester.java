import org.investovator.core.data.api.utils.TradingDataAttribute;
import org.investovator.dataPlayBackEngine.DataPlayer;
import org.investovator.dataPlayBackEngine.events.StockEvent;

import java.util.Observable;
import java.util.Observer;

/**
 * @author: ishan
 * @version: ${Revision}
 */
public class Tester {
    static Observer observer=new Obs();

    public Tester(Observer observer) {
//        this.observer = new Obs();
    }

    public static void main(String[] args) {
        String[] stocks=new String[2];
        stocks[0]="GOOG";
        stocks[1]="APPL";
        DataPlayer player=new DataPlayer(stocks);
        player.setObserver(observer);
        player.runPlayback(2);
    }

    private static class Obs implements Observer{

        @Override
        public void update(Observable o, Object arg) {

            StockEvent d= (StockEvent)arg;

            System.out.println(d.getTime()+"-"+d.getStockId());
            System.out.println("Attributes:");
            for(TradingDataAttribute attr:d.getData().keySet()){
                System.out.println(attr+" : "+d.getData().get(attr));
            }
        }
    }

}
