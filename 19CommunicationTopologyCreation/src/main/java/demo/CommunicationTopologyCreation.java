package demo;

import java.util.ArrayList;

import akka.actor.ActorRef;
import akka.actor.ActorSystem;
import akka.event.Logging;
import akka.event.LoggingAdapter;

/**
 * @author Remi SHARROCK
 * @description
 */
public class CommunicationTopologyCreation {
    
    public static void main(String[] args) {
	
	final ActorSystem system = ActorSystem.create("system");
	final LoggingAdapter log = Logging.getLogger(system, "main");
	final int[][] adjacency = {{0,1,1,0},
				   {0,0,0,1},
				   {1,0,0,1},
				   {1,0,0,1}};

	ActorRef actors[] = new ActorRef[adjacency.length];
	// Instantiate first and second actor
	for (int row = 0; row < adjacency.length; row++) {
	    actors[row] = system.actorOf(Actor.createActor(), "actor" + (row + 1));
	}
	

	// send to a the reference of transmitter by message
	for (int row = 0; row < adjacency.length; row++) {
	    for(int column = 0; column < adjacency[0].length; column++) {
		if(adjacency[row][column] == 1)
		    actors[row].tell(actors[column], ActorRef.noSender());
	    }
	}

		
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
