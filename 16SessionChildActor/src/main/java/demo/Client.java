package demo;

import akka.actor.Props;
import akka.actor.ActorRef;
import akka.actor.UntypedAbstractActor;
import akka.event.Logging;
import akka.event.LoggingAdapter;
import scala.concurrent.*;
import akka.util.Timeout;
import java.time.Duration;
import akka.pattern.*;

public class Client extends UntypedAbstractActor{

    // Logger attached to actor
    private final LoggingAdapter log = Logging.getLogger(getContext().getSystem(), this);
    private MyMessage Message;
    private ActorRef manager, session;
    

    public Client() {}

    // Static function creating actor
    public static Props createActor() {
	return Props.create(Client.class, () -> {
		return new Client();
	    });
    }

    @Override
    public void onReceive(Object message) throws Throwable {
	if(message instanceof MyMessage){
	    Message = (MyMessage) message;
	    if(Message.s == "start") {
		Timeout timeout = Timeout.create(Duration.ofSeconds(5));
		Future<Object> future = Patterns.ask(manager,new CreateSessionMessage(), timeout);
		session = (ActorRef) Await.result(future, timeout.duration());
		if(session != null) {
		    future = Patterns.ask(session,new MyMessage("Hello"), timeout);
		    MyMessage res1 = (MyMessage) Await.result(future, timeout.duration());
		    log.info("["+getSelf().path().name()+"] received the response: " + res1.s + " from ["+ getSender().path().name() +"]");
		    future = Patterns.ask(session, new MyMessage("Ok"), timeout);
		    res1 = (MyMessage) Await.result(future, timeout.duration());
		    log.info("["+getSelf().path().name()+"] received the response: " + res1.s + " from ["+ getSender().path().name() +"]");
		    manager.tell(new EndSessionMessage(), getSelf());
		}
	    }
	    else{
		log.info("["+getSelf().path().name()+"] received the message: " + Message.s + " from ["+ getSender().path().name() +"]");
	    }
	}
	if(message instanceof ActorRef){
	    manager = (ActorRef) message;
	    log.info("["+getSelf().path().name()+"] received message from ["+ getSender().path().name() +"]");
	}
    }

}
