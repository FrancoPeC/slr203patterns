package demo;

import java.io.Serializable;
import akka.actor.ActorRef;

public class MaxMessage implements Serializable {
    private static final long serialVersionUID = 1L;
    public final int max;

    public MaxMessage(int max) {
        this.max = max;
    }

}
