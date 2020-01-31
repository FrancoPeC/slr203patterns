package demo;

import akka.actor.ActorRef;
import akka.actor.ActorSystem;
import akka.actor.ActorSelection;
import akka.actor.Identify;
import akka.actor.ActorIdentity;


/**
 * @author Remi SHARROCK and Axel Mathieu
 * @description Create an actor and passing his reference to
 *				another actor by message.
 */
public class SearchActorsWithName {

	public static void main(String[] args) {
	    
	    // Instantiate an actor system
	    final ActorSystem system = ActorSystem.create("system");
	    
	    // Instantiate first and second actor
	    final ActorRef a = system.actorOf(FirstActor.createActor(), "a");
	    final ActorRef b = system.actorOf(FirstActor.createActor(), "b");
	    // send to a1 the reference of a2 by message
	    //be carefull, here it is the main() function that sends a message to a1, 
	    //not a1 telling to a2 as you might think when looking at this line:

	    CreateMessage m = new CreateMessage();
	    a.tell(m, ActorRef.noSender());
	    a.tell(m, ActorRef.noSender());

	    final Integer identifyId = 1;

	    ActorSelection actor = system.actorSelection("/user/" + "a");
	    actor.tell(new Identify(identifyId), b);
	    actor = system.actorSelection("/user/a/" + "actor1");
	    actor.tell(new Identify(identifyId), b);
	    actor = system.actorSelection("/user/a/" + "actor2");
	    actor.tell(new Identify(identifyId), b);
	    actor = system.actorSelection("/user/a");
	    actor.tell(new Identify(identifyId), b);
	    actor = system.actorSelection("/user/a/actor1");
	    actor.tell(new Identify(identifyId), b);
	    actor = system.actorSelection("/user/a/actor2");
	    actor.tell(new Identify(identifyId), b);
	    actor = system.actorSelection("/user/*");
	    actor.tell(new Identify(identifyId), b);
	    actor = system.actorSelection("/system/*");
	    actor.tell(new Identify(identifyId), b);
	    actor = system.actorSelection("/deadLetters/*");
	    actor.tell(new Identify(identifyId), b);
	    actor = system.actorSelection("/temp/*");
	    actor.tell(new Identify(identifyId), b);
	    actor = system.actorSelection("/remote/*");
	    actor.tell(new Identify(identifyId), b);

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

}
