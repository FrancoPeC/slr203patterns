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
public class Convergecast {

    public static void main(String[] args) {

	final ActorSystem system = ActorSystem.create("system");
	final LoggingAdapter log = Logging.getLogger(system, "main");

	// Instantiate first and second actor
	final ActorRef a = system.actorOf(ThirdActor.createActor(), "a");
	final ActorRef b = system.actorOf(FirstActor.createActor(), "b");
	final ActorRef c = system.actorOf(FirstActor.createActor(), "c");
	final ActorRef d = system.actorOf(SecondActor.createActor(), "d");
	final ActorRef merger = system.actorOf(Merger.createActor(), "merger");

	// send to a the reference of transmitter by message
	a.tell(merger, ActorRef.noSender());
	b.tell(merger, ActorRef.noSender());
	c.tell(merger, ActorRef.noSender());
	merger.tell(d, ActorRef.noSender());
		
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
