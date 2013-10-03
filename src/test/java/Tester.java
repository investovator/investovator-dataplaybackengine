import org.investovator.dataPlayBackEngine.DataPlayer;

/**
 * @author: ishan
 * @version: ${Revision}
 */
public class Tester {
    public static void main(String[] args) {
        DataPlayer player=new DataPlayer();
        player.runPlayback(1, "GOOG");
    }

}
