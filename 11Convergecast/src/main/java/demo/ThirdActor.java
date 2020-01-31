package demo;

import akka.actor.Props;
import akka.actor.ActorRef;
import akka.actor.UntypedAbstractActor;
import akka.event.Logging;
import akka.event.LoggingAdapter;

public class ThirdActor extends UntypedAbstractActor{

    // Logger attached to actor
    private final LoggingAdapter log = Logging.getLogger(getContext().getSystem(), this);
    private MyMessage Message;
    private ActorRef merger;
    

    public ThirdActor() {}

    // Static function creating actor
    public static Props createActor() {
	return Props.create(ThirdActor.class, () -> {
		return new ThirdActor();
	    });
    }

    @Override
    public void onReceive(Object message) throws Throwable {
	if(message instanceof ActorRef){
	    merger = (ActorRef) message;
	    log.info("["+getSelf().path().name()+"] received message from ["+ getSender().path().name() +"]");
	    JoinMessage join = new JoinMessage();
	    merger.tell(join, getSelf());
	    Thread.sleep(100);
	    Message = new MyMessage("Hello");
	    merger.tell(Message, getSelf());
	    Thread.sleep(50);
	    UnjoinMessage unjoin = new UnjoinMessage();
	    merger.tell(unjoin, getSelf());
	}
    }
}
