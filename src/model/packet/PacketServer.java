package src.model.packet;

public class PacketServer {
    private State state;

    public PacketServer(State state) {
        this.state = state;
    }

    public State getState() {
        return state;
    }

    public void setState(State state) {
        this.state = state;
    }
}
