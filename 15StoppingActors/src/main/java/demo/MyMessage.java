package demo;

import java.io.Serializable;
import akka.actor.ActorRef;

public class MyMessage implements Serializable {
    private static final long serialVersionUID = 1L;
    public String s;

    public MyMessage(String s) {this.s = s;}

}
