package demo;

import java.util.ArrayList;

import akka.actor.ActorRef;
import akka.actor.ActorSystem;
import akka.event.Logging;
import akka.event.LoggingAdapter;
import demo.MyMessage;
import demo.Receiver.PrintMessage;

/**
 * @author Remi SHARROCK
 * @description
 */
public class Multicast {

    public static void main(String[] args) {

	final ActorSystem system = ActorSystem.create("system");
	final LoggingAdapter log = Logging.getLogger(system, "main");

	// Instantiate first and second actor
	final ActorRef sender = system.actorOf(Sender.createActor(), "sender");
	final ActorRef receiver1 = system.actorOf(Receiver.createActor(), "receiver1");
	final ActorRef receiver2 = system.actorOf(Receiver.createActor(), "receiver2");
	final ActorRef receiver3 = system.actorOf(Receiver.createActor(), "receiver3");
	final ActorRef multicaster = system.actorOf(Multicaster.createActor(), "multicaster");

	// send to a the reference of transmitter by message
	sender.tell(multicaster, ActorRef.noSender());
	sender.tell(receiver1, ActorRef.noSender());
	sender.tell(receiver2, ActorRef.noSender());
	sender.tell(receiver3, ActorRef.noSender());	

	MyMessage Message = new MyMessage("start", 1);
		
	sender.tell(Message, ActorRef.noSender());
		
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
