package demo;

import java.io.Serializable;
import akka.actor.ActorRef;

public class Response implements Serializable {
    private static final long serialVersionUID = 1L;
    public final String s;

    public Response(String s) {
        this.s = s;
    }

}
