package demo;

import akka.actor.Props;
import java.util.ArrayList;
import akka.actor.UntypedAbstractActor;
import akka.actor.ActorRef;
import akka.event.Logging;
import akka.event.LoggingAdapter;

public class SessionManager extends UntypedAbstractActor{

    // Logger attached to actor
    private final LoggingAdapter log = Logging.getLogger(getContext().getSystem(), this);
    private MyMessage Message;
    private CreateSessionMessage Request;
    private ArrayList<ActorRef> sessions, clients;
    private int counter;

    public SessionManager() {sessions = new ArrayList<ActorRef>(); clients = new ArrayList<ActorRef>(); counter = 1;}

    static public class PrintMessage {}

    // Static function creating actor
    public static Props createActor() {
	return Props.create(SessionManager.class, () -> {
		return new SessionManager();
	    });
    }

    @Override
    public void onReceive(Object message) throws Throwable {
	if(message instanceof CreateSessionMessage) {
	    ActorRef session = getContext().actorOf(Session.createActor(), "session" + counter);
	    this.sessions.add(session);
	    counter++;
	    getSender().tell(session, getSelf());
	    log.info("("+getSelf().path().name()+") received a create session request from ("+ getSender().path().name() +")");
	}
	if(message instanceof EndSessionMessage) {
	    for(int i = 0; i < clients.size(); i++) {
		if(clients.get(i) == getSender()) {
		    sessions.get(i).tell(akka.actor.Kill.getInstance(), getSelf());
		    sessions.remove(i);
		    break;
		}
	    }
	    log.info("("+getSelf().path().name()+") received an end session request from ("+ getSender().path().name() +")");
	}		
    }

}
