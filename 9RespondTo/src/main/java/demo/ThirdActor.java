package demo;

import akka.actor.Props;
import akka.actor.UntypedAbstractActor;
import akka.event.Logging;
import akka.event.LoggingAdapter;
import demo.MyMessage;

public class ThirdActor extends UntypedAbstractActor{
    
    // Logger attached to actor
    private final LoggingAdapter log = Logging.getLogger(getContext().getSystem(), this);

    public ThirdActor() {}

    // Static function creating actor
    public static Props createActor() {
	return Props.create(ThirdActor.class, () -> {
		return new ThirdActor();
	    });
    }

    @Override
    public void onReceive(Object message) throws Throwable {
	if(message instanceof MyMessage){
	    MyMessage Message = (MyMessage) message;
	    Message = new MyMessage(Message.s + " World", Message.dest);
	    Message.dest.tell(Message, getSelf());
	    log.info("("+getSelf().path().name()+") received a message from ("+ getSender().path().name() +")");
	}
    }

}
