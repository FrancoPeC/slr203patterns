package demo;

import java.util.ArrayList;

import akka.actor.ActorRef;
import akka.actor.ActorSystem;
import akka.event.Logging;
import akka.event.LoggingAdapter;
import demo.MyMessage;

/**
 * @author Remi SHARROCK
 * @description
 */
public class SessionChildActor {

    public static void main(String[] args) {

	final ActorSystem system = ActorSystem.create("system");
	final LoggingAdapter log = Logging.getLogger(system, "main");
	
	// Instantiate first and second actor
	final ActorRef client1 = system.actorOf(Client.createActor(), "client1");
	final ActorRef session1 = system.actorOf(Session.createActor(), "session1");
	final ActorRef manager = system.actorOf(SessionManager.createActor(), "manager");

	// send to a the reference of transmitter by message
	client1.tell(manager, ActorRef.noSender());
	manager.tell(session1, ActorRef.noSender());

	MyMessage Message = new MyMessage("start");
		
	client1.tell(Message, ActorRef.noSender());
		
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
