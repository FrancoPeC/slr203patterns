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
    private ActorRef b;
    

    public FirstActor() {}

    // Static function creating actor
    public static Props createActor() {
	return Props.create(FirstActor.class, () -> {
		return new FirstActor();
	    });
    }

    @Override
    public void onReceive(Object message) throws Throwable {
	if(message instanceof MyMessage){
	    Message = (MyMessage) message;
	    if(Message.s == "start") {
		String s = "Hello";
		Message = new MyMessage(s);
		b.tell(Message, getSelf());
		s += " World";
		Message = new MyMessage(s);
		b.tell(Message, getSelf());
	    }
	    else{
		log.info("["+getSelf().path().name()+"] received the message:" + Message.s + "from ["+ getSender().path().name() +"]");
	    }
	}
	if(message instanceof ActorRef){
	    this.b = (ActorRef) message;
	    log.info("["+getSelf().path().name()+"] received message from ["+ getSender().path().name() +"]");
	}
    }

}
