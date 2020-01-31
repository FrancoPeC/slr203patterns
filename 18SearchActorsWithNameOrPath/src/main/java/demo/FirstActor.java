package demo;

import akka.actor.ActorRef;
import akka.actor.ActorSelection;
import akka.actor.Identify;
import akka.actor.ActorIdentity;
import akka.actor.Props;
import akka.actor.UntypedAbstractActor;
import akka.event.Logging;
import akka.event.LoggingAdapter;

public class FirstActor extends UntypedAbstractActor{

    // Logger attached to actor
    private final LoggingAdapter log = Logging.getLogger(getContext().getSystem(), this);
    private int instance;

    public FirstActor() {instance = 1;}

    // Static function creating actor
    public static Props createActor() {
	return Props.create(FirstActor.class, () -> {
		return new FirstActor();
	    });
    }


    @Override
    public void onReceive(Object message) throws Throwable {
	if(message instanceof ActorRef){
	    log.info("["+getSelf().path().name()+"] received message from ["+ getSender().path().name() +"]");
		}
	if(message instanceof CreateMessage) {
	    log.info("["+getSelf().path().name()+"] received a create message from ["+ getSender().path().name() +"]");
	    getContext().actorOf(FirstActor.createActor(), "actor" + instance);
	    instance++;
	}

	if(message instanceof ActorIdentity) {
	    ActorIdentity m = (ActorIdentity) message;
	    try{
		ActorRef actor = m.getActorRef().get();
		log.info("["+getSelf().path().name()+"] received a message from ["+ actor.path().name() +"] with path: [" + actor.path() + "]");
	    }catch(Exception e){}
	}
    }
}
