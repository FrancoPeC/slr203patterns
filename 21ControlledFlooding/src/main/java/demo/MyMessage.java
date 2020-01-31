package demo;

import java.io.Serializable;
import akka.actor.ActorRef;

public class MyMessage implements Serializable {
    private static final long serialVersionUID = 1L;
    public final String s;
    public final Integer id;

    public MyMessage(String s, Integer id) {
        this.s = s;
	this.id = id;
    }

}
