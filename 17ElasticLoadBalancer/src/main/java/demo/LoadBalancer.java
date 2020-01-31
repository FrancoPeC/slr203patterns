package demo;

import akka.actor.Props;
import java.util.ArrayList;
import akka.actor.UntypedAbstractActor;
import akka.actor.ActorRef;
import akka.event.Logging;
import akka.event.LoggingAdapter;

public class LoadBalancer extends UntypedAbstractActor{

    // Logger attached to actor
    private final LoggingAdapter log = Logging.getLogger(getContext().getSystem(), this);
    private MyMessage Message;
    private ArrayList<ActorRef> sessions;
    private ArrayList<ArrayList<MyMessage>> messages;
    private int max;

    public LoadBalancer() {
	sessions = new ArrayList<ActorRef>();
	messages = new ArrayList<ArrayList<MyMessage>>();
    }

    static public class PrintMessage {}

    // Static function creating actor
    public static Props createActor() {
	return Props.create(LoadBalancer.class, () -> {
		return new LoadBalancer();
	    });
    }

    @Override
    public void onReceive(Object message) throws Throwable {
	if(message instanceof MyMessage) {
	    MyMessage Message = (MyMessage) message;
	    log.info("("+getSelf().path().name()+") received a message from ("+ getSender().path().name() +")");
	    if(sessions.size() < max) {
		ActorRef session = getContext().actorOf(SecondActor.createActor(getSelf()), "actor" + sessions.size());
		this.sessions.add(session);
		ArrayList<MyMessage> array = new ArrayList<MyMessage>();
		array.add(Message);
		this.messages.add(array);
		session.tell(Message, getSender());
	    }
	    else {
		int n = messages.get(0).size();
		int actorIndex = 0;
		for(int i = 0; i < messages.size(); i++) {
		    if(messages.get(i).size() < n) {
			actorIndex = i;
			break;
		    }
		}
		messages.get(actorIndex).add(Message);
		sessions.get(actorIndex).tell(Message, getSender());
	    }
	}
	if(message instanceof FinishedMessage) {
	    int i = sessions.indexOf(getSender());
	    if(messages.get(i).size() == 1) {
		sessions.get(i).tell(akka.actor.Kill.getInstance(), getSelf());
		sessions.remove(i);
		messages.remove(i);
	    }
	    else {
		FinishedMessage finish = (FinishedMessage) message;
		int iMessage = messages.get(i).indexOf(finish.message);
		messages.get(i).remove(iMessage);
	    }
	    log.info("("+getSelf().path().name()+") received a finished request from ("+ getSender().path().name() +")");
	}
	if(message instanceof MaxMessage) {
	    MaxMessage Message = (MaxMessage) message;
	    max = Message.max;
	}
    }

}
