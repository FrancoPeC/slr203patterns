package demo;

import akka.actor.ActorRef;
import java.util.*;
import akka.actor.Props;
import akka.actor.UntypedAbstractActor;
import akka.event.Logging;
import akka.event.LoggingAdapter;
import demo.MyMessage;

public class Multicaster extends UntypedAbstractActor{
    
    // Logger attached to actor
    private final LoggingAdapter log = Logging.getLogger(getContext().getSystem(), this);
    private Dictionary dic;
    private ArrayList<ActorRef> subs;

    public Multicaster() {dic = new Hashtable();}

    // Static function creating actor
    public static Props createActor() {
	return Props.create(Multicaster.class, () -> {
		return new Multicaster();
	    });
    }

    @Override
    public void onReceive(Object message) throws Throwable {
	if(message instanceof MyMessage){
	    MyMessage Message = (MyMessage) message;
	    subs = (ArrayList<ActorRef>) dic.get(Message.group);
	    for(ActorRef i : subs){
		i.tell(Message, getSender());
	    }
	    log.info("("+getSelf().path().name()+") received a message from ("+ getSender().path().name() +")");
	}
	if(message instanceof CreateGroupMessage) {
	    CreateGroupMessage m = (CreateGroupMessage) message;
	    dic.put(m.name, m.group);
	}
    }
}
