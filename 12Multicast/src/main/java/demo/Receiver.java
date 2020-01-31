package demo;

import akka.actor.ActorRef;
import akka.actor.Props;
import akka.actor.UntypedAbstractActor;
import akka.event.Logging;
import akka.event.LoggingAdapter;

public class Receiver extends UntypedAbstractActor{

    // Logger attached to actor
    private final LoggingAdapter log = Logging.getLogger(getContext().getSystem(), this);
    private MyMessage Message;

    public Receiver() {}

    static public class PrintMessage {}

    // Static function creating actor
    public static Props createActor() {
	return Props.create(Receiver.class, () -> {
		return new Receiver();
	    });
    }

    @Override
    public void onReceive(Object message) throws Throwable {
	if(message instanceof MyMessage) {
	    this.Message = (MyMessage) message;
	    log.info("("+getSelf().path().name()+") received the message: " + Message.s + " from ("+ getSender().path().name() +")");
	}
	if(message instanceof PrintMessage){
	    log.info("Message: " + this.Message.s);
	}
    }

}
