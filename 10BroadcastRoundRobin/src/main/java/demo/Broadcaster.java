package demo;

import akka.actor.ActorRef;
import java.util.ArrayList;
import akka.actor.Props;
import akka.actor.UntypedAbstractActor;
import akka.event.Logging;
import akka.event.LoggingAdapter;
import demo.MyMessage;

public class Broadcaster extends UntypedAbstractActor{
    
    // Logger attached to actor
    private final LoggingAdapter log = Logging.getLogger(getContext().getSystem(), this);
    private ArrayList<ActorRef> subs;

    public Broadcaster() {subs = new ArrayList<ActorRef>();}

    // Static function creating actor
    public static Props createActor() {
	return Props.create(Broadcaster.class, () -> {
		return new Broadcaster();
	    });
    }

    @Override
    public void onReceive(Object message) throws Throwable {
	if(message instanceof MyMessage){
	    MyMessage Message = (MyMessage) message;
	    for(ActorRef i : subs){
		i.tell(Message, getSender());
	    }
	    log.info("("+getSelf().path().name()+") received a message from ("+ getSender().path().name() +")");
	}
	if(message instanceof JoinMessage) {
	    subs.add(getSender());
	}
    }

}
