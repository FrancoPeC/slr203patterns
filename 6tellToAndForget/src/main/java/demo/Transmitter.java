package demo;

import akka.actor.Props;
import akka.actor.UntypedAbstractActor;
import akka.event.Logging;
import akka.event.LoggingAdapter;
import demo.MyMessage;

public class Transmitter extends UntypedAbstractActor{
    
    // Logger attached to actor
    private final LoggingAdapter log = Logging.getLogger(getContext().getSystem(), this);

    public Transmitter() {}

    // Static function creating actor
    public static Props createActor() {
	return Props.create(Transmitter.class, () -> {
		return new Transmitter();
	    });
    }

    @Override
    public void onReceive(Object message) throws Throwable {
	if(message instanceof MyMessage){
	    MyMessage Message = (MyMessage) message;
	    Message.dest.tell(Message, getSender());
	    log.info("("+getSelf().path().name()+") received a message from ("+ getSender().path().name() +")");
	}
    }

}
