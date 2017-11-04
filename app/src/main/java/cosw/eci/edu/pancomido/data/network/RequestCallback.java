package cosw.eci.edu.pancomido.data.network;

import cosw.eci.edu.pancomido.exception.NetworkException;

/**
 * Created by estudiante on 10/31/17.
 */

public interface RequestCallback<T>
{

    void onSuccess(T response);

    void onFailed(NetworkException e);

}