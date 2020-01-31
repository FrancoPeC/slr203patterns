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
    private ArrayList<ActorRef> topics;

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
	    this.Message = (MyMessage) message;
	    log.info("("+getSelf().path().name()+") received the message: " + Message.s + " from ("+ getSender().path().name() +")");
	    UnsubMessage unsub = new UnsubMessage();
	    topics.get(0).tell(unsub, getSelf());
	}
	if(message instanceof ActorRef) {
	    ActorRef Message = (ActorRef) message;
	    topics = new ArrayList<ActorRef>();
	    topics.add(Message);
	    JoinMessage join = new JoinMessage();
	    Message.tell(join, getSelf());
	}
	if(message instanceof PrintMessage){
	    log.info("Message: " + this.Message.s);
	}
    }

}
