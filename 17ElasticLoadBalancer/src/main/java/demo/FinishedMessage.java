package demo;

import java.io.Serializable;
import akka.actor.ActorRef;

public class FinishedMessage implements Serializable {
    private static final long serialVersionUID = 1L;
    public final MyMessage message;

    public FinishedMessage(MyMessage message) {
        this.message = message;
    }

}
