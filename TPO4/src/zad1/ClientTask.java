/**
 * @author Podleśny Jakub S20540
 */

package zad1;

import java.util.List;
import java.util.concurrent.Callable;
import java.util.concurrent.FutureTask;

public class ClientTask extends FutureTask<String> {

    public ClientTask(Callable<String> callable) {
        super(callable);
    }

    public static ClientTask create(Client c, List<String> reqs, boolean showSendRes) {
        Callable<String> callable = () -> {
            c.connect();
            c.send("login " + c.getId());
            reqs.forEach(re -> {
                String resp = c.send(re);
                if(showSendRes) System.out.println(resp);
            });
            return c.send("bye and log transfer");
        };
        return new ClientTask(callable);
    }

}
