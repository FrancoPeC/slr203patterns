package demo;

import akka.actor.ActorRef;
import java.util.ArrayList;
import akka.actor.Props;
import akka.actor.UntypedAbstractActor;
import akka.event.Logging;
import akka.event.LoggingAdapter;
import demo.MyMessage;
import java.lang.Math;

public class Merger extends UntypedAbstractActor{
    
    // Logger attached to actor
    private final LoggingAdapter log = Logging.getLogger(getContext().getSystem(), this);
    private ArrayList<ActorRef> subs;
    private ActorRef dest;
    private MyMessage Message;
    private int current;
    private int mask;

    public Merger() {subs = new ArrayList<ActorRef>(); mask = 0; Message = null;}

    // Static function creating actor
    public static Props createActor() {
	return Props.create(Merger.class, () -> {
		return new Merger();
	    });
    }

    @Override
    public void onReceive(Object message) throws Throwable {
	if(message instanceof MyMessage){
	    log.info("("+getSelf().path().name()+") received a message from ("+ getSender().path().name() +")");
	    if(Message == null) {
		Message = (MyMessage) message;
		int i = 0;
		while(subs.get(i) != getSender()) i++;
		current = 1 << i;
	    }
	    else {
		MyMessage curMessage = (MyMessage) message;
		if(curMessage.s == Message.s) {
		    int i = 0;
		    while(subs.get(i) != getSender()) i++;
		    current |= 1 << i;
		}
	    }
	    if(current == mask) {
		dest.tell(Message, getSelf());
		Message = null;
	    }
	}
	if(message instanceof JoinMessage) {
	    log.info("("+getSelf().path().name()+") received a join from ("+ getSender().path().name() +")");
	    subs.add(getSender());
	    mask = (int) Math.pow(2, subs.size()) - 1;
	}
	if(message instanceof UnjoinMessage) {
	    log.info("("+getSelf().path().name()+") received an unjoin from ("+ getSender().path().name() +")");
	    int i = 0;
	    while(subs.get(i) != getSender()){
		i++;
	    }
	    subs.remove(i);
	    mask = (int) Math.pow(2, subs.size()) - 1;
	    current = 0;
	}
	if(message instanceof ActorRef)
	    dest = (ActorRef) message;
    }

}
