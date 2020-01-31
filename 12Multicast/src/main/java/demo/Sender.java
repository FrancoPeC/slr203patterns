package demo;

import java.util.ArrayList;
import akka.actor.Props;
import akka.actor.ActorRef;
import akka.actor.UntypedAbstractActor;
import akka.event.Logging;
import akka.event.LoggingAdapter;

public class Sender extends UntypedAbstractActor{

    // Logger attached to actor
    private final LoggingAdapter log = Logging.getLogger(getContext().getSystem(), this);
    private MyMessage Message;
    private ActorRef multicaster, rec1, rec2, rec3;

    public Sender() {multicaster = rec1 = rec2 = rec3 = null;}

    // Static function creating actor
    public static Props createActor() {
	return Props.create(Sender.class, () -> {
		return new Sender();
	    });
    }

    @Override
    public void onReceive(Object message) throws Throwable {
	if(message instanceof MyMessage){
	    ArrayList<ActorRef> group = new ArrayList<ActorRef>();
	    group.add(rec1);
	    group.add(rec2);
	    CreateGroupMessage groupMessage = new CreateGroupMessage(1, group);
	    multicaster.tell(groupMessage, getSelf());
	    group = new ArrayList<ActorRef>();
	    group.add(rec2);
	    group.add(rec3);
	    groupMessage = new CreateGroupMessage(2, group);
	    multicaster.tell(groupMessage, getSelf());
	    Message = new MyMessage("Hello", 1);
	    multicaster.tell(Message, getSelf());
	    Message = new MyMessage("World", 2);
	    multicaster.tell(Message, getSelf());
	}
	if(message instanceof ActorRef){
	    if(multicaster == null) multicaster = (ActorRef) message;
	    else if(rec1 == null) rec1 = (ActorRef) message;
	    else if(rec2 == null) rec2 = (ActorRef) message;
	    else if(rec3 == null) rec3 = (ActorRef) message;
	    log.info("["+getSelf().path().name()+"] received message from ["+ getSender().path().name() +"]");
	}
    }

}
