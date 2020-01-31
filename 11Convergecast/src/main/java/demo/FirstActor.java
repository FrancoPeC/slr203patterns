package demo;

import akka.actor.Props;
import akka.actor.ActorRef;
import akka.actor.UntypedAbstractActor;
import akka.event.Logging;
import akka.event.LoggingAdapter;

public class FirstActor extends UntypedAbstractActor{

    // Logger attached to actor
    private final LoggingAdapter log = Logging.getLogger(getContext().getSystem(), this);
    private MyMessage Message;
    private ActorRef merger;
    

    public FirstActor() {}

    // Static function creating actor
    public static Props createActor() {
	return Props.create(FirstActor.class, () -> {
		return new FirstActor();
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
	    Thread.sleep(100);
	    Message = new MyMessage("Hello World");
	    merger.tell(Message, getSelf());
	}
    }
}
