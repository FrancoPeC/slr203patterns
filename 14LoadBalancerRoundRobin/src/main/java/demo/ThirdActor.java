package demo;

import java.util.ArrayList;
import akka.actor.ActorRef;
import akka.actor.Props;
import akka.actor.UntypedAbstractActor;
import akka.event.Logging;
import akka.event.LoggingAdapter;

public class ThirdActor extends UntypedAbstractActor{

    // Logger attached to actor
    private final LoggingAdapter log = Logging.getLogger(getContext().getSystem(), this);
    private MyMessage Message;
    private ActorRef balancer;

    public ThirdActor() {}

    static public class PrintMessage {}

    // Static function creating actor
    public static Props createActor() {
	return Props.create(ThirdActor.class, () -> {
		return new ThirdActor();
	    });
    }

    @Override
    public void onReceive(Object message) throws Throwable {
	if(message instanceof MyMessage) {
	    Message = (MyMessage) message;
	    log.info("("+getSelf().path().name()+") received the message: " + Message.s + " from ("+ getSender().path().name() +")");
	    UnsubMessage unsub = new UnsubMessage();
	    balancer.tell(unsub, getSelf());
	}
	if(message instanceof ActorRef) {
	    balancer = (ActorRef) message;
	    JoinMessage join = new JoinMessage();
	    balancer.tell(join, getSelf());
	}
	if(message instanceof PrintMessage){
	    log.info("Message: " + Message.s);
	}
    }

}
