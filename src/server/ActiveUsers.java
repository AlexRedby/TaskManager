package src.server;

import java.util.ArrayList;
import java.util.List;

public class ActiveUsers {
    List<String> activeUsers;

    public ActiveUsers() {
        activeUsers = new ArrayList<>();
    }

    public boolean contains(String login) {
        return activeUsers.contains(login);
    }

    public void add(String login) throws Exception {
        if (contains(login)) {
            throw new Exception("Такой пользователь уже есть");
        }
        activeUsers.add(login);
    }

    public void remove(String login) {
        if (login != null && !login.isEmpty())
            activeUsers.remove(login);
    }
}
