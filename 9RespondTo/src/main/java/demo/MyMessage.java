package demo;

import java.io.Serializable;
import akka.actor.ActorRef;

public class MyMessage implements Serializable {
    private static final long serialVersionUID = 1L;
    public final String s;
    public final ActorRef dest;

    public MyMessage(String s, ActorRef dest) {
        this.s = s;
	this.dest = dest;
    }

}
