package src.model.packet;

import java.io.Serializable;

public abstract class PacketClient implements Serializable {
    private Action action;

    public PacketClient(Action action){
        this.action = action;
    }

    public Action getAction() {
        return action;
    }

    public void setAction(Action action) {
        this.action = action;
    }
}
