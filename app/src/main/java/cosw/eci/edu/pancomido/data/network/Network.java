package cosw.eci.edu.pancomido.data.network;

import java.util.List;

import cosw.eci.edu.pancomido.data.model.LoginWrapper;
import cosw.eci.edu.pancomido.data.model.Todo;
import cosw.eci.edu.pancomido.data.model.Token;
import cosw.eci.edu.pancomido.data.model.User;

/**
 * Created by estudiante on 10/31/17.
 */

public interface Network
{
    void login(User loginWrapper, RequestCallback<Token> requestCallback);

    void createTodo(Todo todo, RequestCallback requestCallback);

    void getTodoList(RequestCallback<List<Todo>> requestCallback);
}
