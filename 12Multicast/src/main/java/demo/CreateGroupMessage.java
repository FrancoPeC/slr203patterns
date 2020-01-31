package demo;

import java.util.ArrayList;
import java.io.Serializable;
import akka.actor.ActorRef;

public class CreateGroupMessage implements Serializable {
    private static final long serialVersionUID = 1L;
    public ArrayList<ActorRef> group;
    public Integer name;
    
    public CreateGroupMessage(Integer name, ArrayList<ActorRef> group) {this.group = group; this.name = name;}

}
