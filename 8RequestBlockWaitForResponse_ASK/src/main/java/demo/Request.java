package demo;

import java.io.Serializable;
import akka.actor.ActorRef;

public class Request implements Serializable {
    private static final long serialVersionUID = 1L;
    public final String s;

    public Request(String s) {
        this.s = s;
    }

}
