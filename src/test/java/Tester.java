import org.investovator.core.data.api.utils.TradingDataAttribute;
import org.investovator.dataplaybackengine.RealTimeDataPlayer;
import org.investovator.dataplaybackengine.events.EventManager;
import org.investovator.dataplaybackengine.events.StockEvent;
import org.investovator.dataplaybackengine.utils.DateUtils;

import java.util.ArrayList;
import java.util.Observable;
import java.util.Observer;

/**
 * @author: ishan
 * @version: ${Revision}
 */
public class Tester {
    static Observer observer=new Obs();
    static RealTimeDataPlayer player;

    public Tester(Observer observer) {
    }

    public static void main(String[] args) {
        String[] stocks=new String[2];
        stocks[0]="GOOG";
        stocks[1]="APPL";
        String startDate="2011-12-13-15-55-32";

        //define the attributes needed
        ArrayList<TradingDataAttribute> attributes=new ArrayList<TradingDataAttribute>();

        //just the closing price is enough for now
        attributes.add(TradingDataAttribute.DAY);
        attributes.add(TradingDataAttribute.PRICE);
        attributes.add(TradingDataAttribute.SHARES);


        player=new RealTimeDataPlayer(stocks,startDate, DateUtils.DATE_FORMAT_1,attributes,TradingDataAttribute.PRICE);
        player.setObserver(observer);
        player.startPlayback(2);
    }

    private static class Obs implements Observer{

        @Override
        public void update(Observable o, Object arg) {

            if(arg instanceof StockEvent){

                StockEvent d= (StockEvent)arg;

                System.out.println(d.getTime()+"-"+d.getStockId());
                System.out.println("Attributes:");
                for(TradingDataAttribute attr:d.getData().keySet()){
                    System.out.println(attr+" : "+d.getData().get(attr));
                }
            }
            else if(arg== EventManager.RealTimePlayerStates.GAME_OVER){
                System.out.println("Game over");
                //don't forget to stop the player, else it'll run forever
                player.stopPlayback();

            }

        }
    }

}
