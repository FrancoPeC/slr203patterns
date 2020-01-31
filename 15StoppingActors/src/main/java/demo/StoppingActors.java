package demo;
import akka.actor.ActorRef;
import akka.actor.ActorSystem;
import akka.event.Logging;
import akka.event.LoggingAdapter;
import demo.MyMessage;

import static akka.pattern.Patterns.gracefulStop;
import akka.pattern.AskTimeoutException;
import java.util.concurrent.CompletionStage;
import java.time.Duration;

/**
 * @author Remi SHARROCK and Axel Mathieu
 * @description Create an actor by passing the reference of another actor at construction time.
 */
public class StoppingActors {

 public static void main(String[] args) {

	final ActorSystem system = ActorSystem.create("system");
	final LoggingAdapter log = Logging.getLogger(system, "main");

	// Instantiate first and second actor
	final ActorRef a = system.actorOf(FirstActor.createActor(), "a");
	final ActorRef b = system.actorOf(FirstActor.createActor(), "b");
	final ActorRef c = system.actorOf(FirstActor.createActor(), "c");
	final ActorRef d = system.actorOf(FirstActor.createActor(), "d");

	// send to a the reference of transmitter by message
	MyMessage Message = new MyMessage("Hello");
	a.tell(Message, ActorRef.noSender());
	a.tell(Message, ActorRef.noSender());
	a.tell(new StopMessage(), ActorRef.noSender());
	a.tell(Message, ActorRef.noSender());
	a.tell(Message, ActorRef.noSender());

	b.tell(Message, ActorRef.noSender());
	b.tell(Message, ActorRef.noSender());
	b.tell(akka.actor.PoisonPill.getInstance(), ActorRef.noSender());
	b.tell(Message, ActorRef.noSender());
	b.tell(Message, ActorRef.noSender());
	
	c.tell(Message, ActorRef.noSender());
	c.tell(Message, ActorRef.noSender());
	c.tell(akka.actor.Kill.getInstance(), ActorRef.noSender());
	c.tell(Message, ActorRef.noSender());
	c.tell(Message, ActorRef.noSender());

	
	d.tell(Message, ActorRef.noSender());
	d.tell(Message, ActorRef.noSender());
	gracefulStop(d, Duration.ofSeconds(5), 0);
	log.info("Sent a graceful stop");
	d.tell(Message, ActorRef.noSender());
	d.tell(Message, ActorRef.noSender());
	
		
	// We wait 5 seconds before ending system (by default)
	// But this is not the best solution.
	try {
	    waitBeforeTerminate();
	} catch (InterruptedException e) {
	    e.printStackTrace();
	} finally {
	    system.terminate();
	}
    }

    public static void waitBeforeTerminate() throws InterruptedException {
	Thread.sleep(5000);
    }

    public static void sleepFor(int sec) {
	try {
	    Thread.sleep(sec * 1000);
	} catch (InterruptedException e) {
	    e.printStackTrace();
	}
    }

}

