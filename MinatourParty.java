
// Gonen Matias
// CAP 4520
// Parallel & Distributed Processing
// Spring 2022
import java.util.concurrent.atomic.*;
import java.util.concurrent.ThreadLocalRandom;
import java.util.concurrent.ConcurrentHashMap;
import java.util.HashMap;
import java.util.HashSet;

public class MinatourParty {
	// Strategy Explanation:
	// Prior to the party, all the Test Subjects (participants/threads) have agreed
	// to draw a line on the ground to differentiate between the Test Subjects
	// who have entered the labyrinth and those who have not.
	//
	// Initially, all Test Subjects are going to be on the right side of the line.
	// When a Test Subject leaves the labyrinth, he shall stand on the left side of
	// the line.
	//
	// If a Test Subject leaves the labyrinth and notices that no other Test
	// Subjects
	// are left on the right side of the line, he notifies Minatour that all test
	// subjects have visited the labyrinth.
	//
	// (Similarly, we can hypothetically say the Test Subjects used a piece of cake
	// to smear their faces or something for differentiation purposes. But, in
	// reality, it's harder notice when everyone has smeared their faces... It's
	// easier to see when everyone has crossed the line)
	//
	// Solution runtime is O(n) best case, but since it is a simulation
	// It could take more than that (until all Test Subjects visit the labyrinth)
	// This is a thought through choice I made, since I want to allow any guest
	// to enter as many times as Minatour wishes.

	public static AtomicInteger cakes_eaten = new AtomicInteger(0); // There is a cake initially
	public static AtomicBoolean has_cake = new AtomicBoolean(true); // There is a cake initially
	public static AtomicBoolean interrupted = new AtomicBoolean(false);
	private static int NUM_TEST_SUBJECTS = 8; // The threads silly

	// Implementation of knowing who crossed the line (visited)
	public static ConcurrentHashMap<Integer, Boolean> visited = new ConcurrentHashMap<>();

	// For command-line arguments
	private static HashMap<String, String> options = new HashMap<>() {
		{
			put("-threads", "8");
		}
	};
	private static HashSet<String> intArgs = new HashSet<>() {
		{
			add("-threads");
		}
	};

	private static void parseArgs(String[] args) {
		for (int i = 0; i < args.length; i++) {
			String arg = args[i];

			// Not really complicating things here.. can use libraries for this
			// Just parse number of threads
			if (arg.charAt(0) == '-' && arg.length() >= 2 && options.containsKey(arg)) {
				if (intArgs.contains(arg)) {
					// Parse numeric arg
					if (i + 1 < args.length && args[i + 1].matches("\\d+")) {
						options.put(arg, args[i + 1]);
						i++;
					}
				}
				else {
					// TODO if I want more type of args
				}
			}
		}

		// Apply configuration
		NUM_TEST_SUBJECTS = Integer.max(2, Integer.parseInt(options.get("-threads")));
	}

	// Function to let Minatour know that all
	// Test Subjects have entered the labyrinth
	public static void notifyMinatour() {
		interrupted.set(true);
	}

	public static void main(String[] args) throws Exception {
		int num_visits = 0, tot_cakes;

		// Configuration
		parseArgs(args);

		// Initialize the Test Subject threads:)
		// Linear O(n), where n is NUM_TEST_SUBJECTS
		Thread[] test_subjects = new Thread[NUM_TEST_SUBJECTS];
		for (int i = 0; i < NUM_TEST_SUBJECTS; i++) {
			test_subjects[i] = new Thread(new LabyrinthRun(i));
		}

		// Minatour Decides to call a random Test Subject to enter the labyrinth each
		// time. This loop simulates a realistic "game" scenario, where Minatour chooses
		// any random Test Subject to enter the labyrinth. The loop ends once Minatour
		// has been notified that all Test Subjects have entered the maze at least once.
		for (int i = ThreadLocalRandom.current().nextInt(0, NUM_TEST_SUBJECTS); !interrupted.get(); i = ThreadLocalRandom
				.current().nextInt(0, NUM_TEST_SUBJECTS)) {

			// Increment visits statistic
			num_visits++;

			if (test_subjects[i].isAlive())
				test_subjects[i].start();
			else
				test_subjects[i].run();

			// wait for i-th Test Subject to leave labyrinth
			test_subjects[i].join();
		}

		// To print statistics
		tot_cakes = cakes_eaten.get();

		// Conclude the party
		System.out.println("\nAll " + NUM_TEST_SUBJECTS + " guests have now visited the labyrinth");

		// Did the Test Subjects enjoy the cake?
		if (tot_cakes != 0) {
			if (tot_cakes == 1)
				System.out.println("1 cake was eaten");
			else
				System.out.println("A total of " + tot_cakes + " cakes were eaten (" + num_visits + " visits)");
		} else
			System.out.println("No cakes were eaten");

		// Minatour was happy everyone showed up:)
		System.out.println("Minatour is very happy");
	}

	// Thread worker
	static class LabyrinthRun implements Runnable {

		private int id;

		// Constructor
		public LabyrinthRun(int id) {
			this.id = id;
		}

		// Visiting the labyrinth
		public void run() {
			// Test Subject now visiting the labyrinth
			System.out.println("Test Subject (" + id + ") - now visiting labyrinth");

			// Check if there is a cake
			if (!MinatourParty.has_cake.get()) {
				System.out.println("Test Subject (" + id + ") - requests cake from Minatour");
				MinatourParty.has_cake.set(true); // "Got" new cake
				MinatourParty.cakes_eaten.incrementAndGet(); // count number of cakes eaten
			}

			// Decide to eat cake
			if (ThreadLocalRandom.current().nextBoolean()) {
				System.out.println("Test Subject (" + id + ") - decided to eat a piece of cake");
				MinatourParty.has_cake.set(false); // cake was eaten :D
			} else
				System.out.println("Test Subject (" + id + ") - decided to leave the cake for the next guest");

			// Now exist the labyrinth and be on the side
			// of everyone else that visited the labyrinth
			visited.put(this.id, true);

			// Upon exist, see if any of the test Subjects
			// remain on the right side of the line. Notify
			// Minatour if all Test subjects have visited the labyrinth
			if (visited.size() >= NUM_TEST_SUBJECTS)
				MinatourParty.notifyMinatour();
		}
	}
}