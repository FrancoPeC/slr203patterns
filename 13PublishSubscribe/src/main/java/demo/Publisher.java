package demo;

import akka.actor.Props;
import akka.actor.ActorRef;
import akka.actor.UntypedAbstractActor;
import akka.event.Logging;
import akka.event.LoggingAdapter;

public class Publisher extends UntypedAbstractActor{

    // Logger attached to actor
    private final LoggingAdapter log = Logging.getLogger(getContext().getSystem(), this);
    private MyMessage Message;
    private ActorRef topic;
    

    public Publisher() {}

    // Static function creating actor
    public static Props createActor() {
	return Props.create(Publisher.class, () -> {
		return new Publisher();
	    });
    }

    @Override
    public void onReceive(Object message) throws Throwable {
	if(message instanceof MyMessage){
	    String s = "Hello";
	    Message = new MyMessage(s);
	    topic.tell(Message, getSelf());
	    Thread.sleep(100);
	    topic.tell(Message, getSelf());
	}
	if(message instanceof ActorRef){
	    this.topic = (ActorRef) message;
	    log.info("["+getSelf().path().name()+"] received message from ["+ getSender().path().name() +"]");
	}
    }

}
