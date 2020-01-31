package demo;

import akka.actor.Props;
import akka.actor.ActorRef;
import akka.actor.UntypedAbstractActor;
import akka.event.Logging;
import akka.event.LoggingAdapter;

public class SecondActor extends UntypedAbstractActor{

    // Logger attached to actor
    private final LoggingAdapter log = Logging.getLogger(getContext().getSystem(), this);
    private MyMessage Message;
    private ActorRef balancer;

    public SecondActor(ActorRef balancer) {this.balancer = balancer;}

    static public class PrintMessage {}

    // Static function creating actor
    public static Props createActor(ActorRef balancer) {
	return Props.create(SecondActor.class, () -> {
		return new SecondActor(balancer);
	    });
    }

    @Override
    public void onReceive(Object message) throws Throwable {
	if(message instanceof MyMessage) {
	    this.Message = (MyMessage) message;
	    log.info("("+getSelf().path().name()+") received a message from ("+ getSender().path().name() +")");
	    Thread.sleep(1000);
	    FinishedMessage finish = new FinishedMessage(Message);
	    balancer.tell(finish, getSelf());
	}
	if(message instanceof PrintMessage) {
	    log.info("Message: " + this.Message.s);
	}
    }

}
