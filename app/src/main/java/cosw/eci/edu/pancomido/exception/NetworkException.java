package cosw.eci.edu.pancomido.exception;

import java.io.IOException;

/**
 * Created by Nicolas on 10/31/17.
 */

public class NetworkException extends Exception{

    public NetworkException(Object o, IOException e) {
        super(e);
    }
}
