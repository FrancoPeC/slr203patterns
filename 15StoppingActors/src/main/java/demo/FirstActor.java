package demo;

import akka.actor.ActorRef;
import akka.actor.Props;
import akka.actor.UntypedAbstractActor;
import akka.event.Logging;
import akka.event.LoggingAdapter;

public class FirstActor extends UntypedAbstractActor{

    // Logger attached to actor
    private final LoggingAdapter log = Logging.getLogger(getContext().getSystem(), this);
    // Actor reference
    private ActorRef actorRef;

    public FirstActor() {}

    // Static function creating actor Props
    public static Props createActor() {
	return Props.create(FirstActor.class, () -> {
		return new FirstActor();
	    });
    }

    @Override
    public void onReceive(Object message) throws Throwable {
	if(message instanceof StopMessage) {
	    log.info("["+getSelf().path().name()+"] received a stop message from ["+ getSender().path().name() +"]");
	    getContext().stop(getSelf());
	}
	if(message instanceof MyMessage) {
	    log.info("["+getSelf().path().name()+"] received message from ["+ getSender().path().name() +"]");
	}
    }
    @Override
    public void postStop() {
	log.info("["+getSelf().path().name()+"] was stopped");
    }


}
