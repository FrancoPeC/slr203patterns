package demo;

import akka.actor.ActorRef;
import java.util.ArrayList;
import akka.actor.Props;
import akka.actor.UntypedAbstractActor;
import akka.event.Logging;
import akka.event.LoggingAdapter;
import demo.MyMessage;

public class Balancer extends UntypedAbstractActor{
    
    // Logger attached to actor
    private final LoggingAdapter log = Logging.getLogger(getContext().getSystem(), this);
    private ArrayList<ActorRef> subs;
    private int turn;

    public Balancer() {subs = new ArrayList<ActorRef>(); turn = 0;}

    // Static function creating actor
    public static Props createActor() {
	return Props.create(Balancer.class, () -> {
		return new Balancer();
	    });
    }

    @Override
    public void onReceive(Object message) throws Throwable {
	if(message instanceof MyMessage){
	    MyMessage Message = (MyMessage) message;
	    if(subs.size() > 0) {
		subs.get(turn).tell(Message, getSender());
		if(turn == subs.size() - 1) turn = 0;
		else turn ++;
		}
	    log.info("("+getSelf().path().name()+") received a message from ("+ getSender().path().name() +")");
	}
	if(message instanceof JoinMessage) {
	    subs.add(getSender());
	    log.info("("+getSelf().path().name()+") received a join message from ("+ getSender().path().name() +")");
	}
	if(message instanceof UnsubMessage) {
	    subs.remove(getSender());
	    turn = 0;
	    log.info("("+getSelf().path().name()+") received an unsubscribe message from ("+ getSender().path().name() +")");
	}

    }

}
