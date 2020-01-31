package demo;

import java.util.ArrayList;
import akka.actor.Props;
import akka.actor.ActorRef;
import akka.actor.UntypedAbstractActor;
import akka.event.Logging;
import akka.event.LoggingAdapter;

public class Actor extends UntypedAbstractActor{

    // Logger attached to actor
    private final LoggingAdapter log = Logging.getLogger(getContext().getSystem(), this);
    private ArrayList<ActorRef> neighbors;
    private MyMessage Message;
    

    public Actor() {neighbors = new ArrayList<ActorRef>();}

    // Static function creating actor
    public static Props createActor() {
	return Props.create(Actor.class, () -> {
		return new Actor();
	    });
    }

    @Override
    public void onReceive(Object message) throws Throwable {
	if(message instanceof MyMessage) {
	    Message = (MyMessage) message;
	    log.info("["+getSelf().path().name()+"] received message: "+ Message.s +" from actor: ["+ getSender().path().name() +"]");
	    if(Message.s == "start") {
		Message = new MyMessage("Hello!");
	    }
	    for(ActorRef ac : neighbors) {
		ac.tell(Message, getSelf());
	    }
	}
	if(message instanceof ActorRef){
	    ActorRef a = (ActorRef) message;
	    neighbors.add(a);
	    log.info("["+getSelf().path().name()+"] received reference to actor: ["+ a.path().name() +"]");
	}
    }

}
