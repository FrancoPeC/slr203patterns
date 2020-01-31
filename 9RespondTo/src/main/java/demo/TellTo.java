package demo;

import java.util.ArrayList;

import akka.actor.ActorRef;
import akka.actor.ActorSystem;
import akka.event.Logging;
import akka.event.LoggingAdapter;
import demo.MyMessage;
import demo.SecondActor.PrintMessage;

/**
 * @author Remi SHARROCK
 * @description
 */
public class TellTo {

    public static void main(String[] args) {

	final ActorSystem system = ActorSystem.create("system");
	final LoggingAdapter log = Logging.getLogger(system, "main");

	// Instantiate first and second actor
	final ActorRef a = system.actorOf(FirstActor.createActor(), "a");
	final ActorRef b = system.actorOf(ThirdActor.createActor(), "b");
	final ActorRef c = system.actorOf(SecondActor.createActor(), "c");

	// send to a the reference of transmitter by message
	a.tell(b, ActorRef.noSender());
	a.tell(c, ActorRef.noSender());

	MyMessage Message = new MyMessage("start", ActorRef.noSender());
		
	a.tell(Message, ActorRef.noSender());
		
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
