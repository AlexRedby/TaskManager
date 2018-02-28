package src.model.packet;

public class PacketUser extends PacketClient {
    private String login;

    public PacketUser(Action action, String login){
        super(action);
        this.login = login;
    }

    public String getLogin() {
        return login;
    }

    public void setLogin(String login) {
        this.login = login;
    }
}
