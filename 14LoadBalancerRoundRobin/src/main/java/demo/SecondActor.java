package demo;

import akka.actor.ActorRef;
import akka.actor.Props;
import akka.actor.UntypedAbstractActor;
import akka.event.Logging;
import akka.event.LoggingAdapter;

public class SecondActor extends UntypedAbstractActor{

    // Logger attached to actor
    private final LoggingAdapter log = Logging.getLogger(getContext().getSystem(), this);
    private MyMessage Message;
    private ActorRef balancer;

    public SecondActor() {}

    static public class PrintMessage {}

    // Static function creating actor
    public static Props createActor() {
	return Props.create(SecondActor.class, () -> {
		return new SecondActor();
	    });
    }

    @Override
    public void onReceive(Object message) throws Throwable {
	if(message instanceof MyMessage) {
	    Message = (MyMessage) message;
	    log.info("("+getSelf().path().name()+") received the message: " + Message.s + " from ("+ getSender().path().name() +")");
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
