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
		Timeout timeout = Timeout.create(Duration.ofSeconds(5));
		Future<Object> future = Patterns.ask(b,new Request("req1"), timeout);
		Response res1 = (Response) Await.result(future, timeout.duration());
		log.info("["+getSelf().path().name()+"] received the response: " + res1.s + " from ["+ getSender().path().name() +"]");
		future = Patterns.ask(b,new Request("req2"), timeout);
		res1 = (Response) Await.result(future, timeout.duration());
		log.info("["+getSelf().path().name()+"] received the response: " + res1.s + " from ["+ getSender().path().name() +"]");
		future = Patterns.ask(b,new Request("req3"), timeout);
		res1 = (Response) Await.result(future, timeout.duration());
		log.info("["+getSelf().path().name()+"] received the response: " + res1.s + " from ["+ getSender().path().name() +"]");
	    }
	    else{
		log.info("["+getSelf().path().name()+"] received the message: " + Message.s + " from ["+ getSender().path().name() +"]");
	    }
	}
	if(message instanceof ActorRef){
	    this.b = (ActorRef) message;
	    log.info("["+getSelf().path().name()+"] received message from ["+ getSender().path().name() +"]");
	}
    }

}
