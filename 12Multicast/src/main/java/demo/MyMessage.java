package demo;

import java.io.Serializable;
import akka.actor.ActorRef;

public class MyMessage implements Serializable {
    private static final long serialVersionUID = 1L;
    public final String s;
    public final Integer group;

    public MyMessage(String s, Integer group) {
        this.s = s;
	this.group = group;
    }

}
