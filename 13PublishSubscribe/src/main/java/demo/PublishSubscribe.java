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
public class PublishSubscribe {

    public static void main(String[] args) {

	final ActorSystem system = ActorSystem.create("system");
	final LoggingAdapter log = Logging.getLogger(system, "main");

	// Instantiate first and second actor
	final ActorRef pub1 = system.actorOf(Publisher.createActor(), "Publisher1");
	final ActorRef pub2 = system.actorOf(Publisher.createActor(), "Publisher2");
	final ActorRef a = system.actorOf(SecondActor.createActor(), "a");	
	final ActorRef b = system.actorOf(SecondActor.createActor(), "b");
	final ActorRef c = system.actorOf(ThirdActor.createActor(), "c");
	final ActorRef topic1 = system.actorOf(Topic.createActor(), "topic1");
	final ActorRef topic2 = system.actorOf(Topic.createActor(), "topic2");

	// send to a the reference of transmitter by message
	b.tell(topic1, ActorRef.noSender());
	a.tell(topic1, ActorRef.noSender());
	b.tell(topic2, ActorRef.noSender());
	c.tell(topic2, ActorRef.noSender());

	pub1.tell(topic1, ActorRef.noSender());
	pub2.tell(topic2, ActorRef.noSender());

	MyMessage Message = new MyMessage("start");
		
	pub1.tell(Message, ActorRef.noSender());
	pub2.tell(Message, ActorRef.noSender());
		
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
