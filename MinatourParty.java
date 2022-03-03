
// Gonen Matias
// CAP 4520
// Parallel & Distributed Processing
// Spring 2022
import java.util.concurrent.atomic.*;
import java.util.concurrent.ThreadLocalRandom;
import java.util.concurrent.ConcurrentHashMap;

public class MinatourParty {
	// Strategy Explanation:
	// All the Test Subjects (participants/threads) have agreed
	// to draw a line on the ground to differentiate between the Test Subjects
	// who have entered the labrinth and those who have not.
	//
	// Initially, all Test Subjects are going to be in the a single side.
	//
	// Once they have all crossed to the other side, then they announce to minatour
	// that all Test Subjects have entered the labrinth.
	//
	// (Similarly, we can hypothetically say the Test Subjects used a piece of cake
	// to smear their faces or something for differentiation purposes. But, in
	// reality,
	// it's harder notice when everyone has smeared their faces... It's easier to
	// see when
	// everyone has crossed the line)
	//
	// The Test Subjects may enjoy the cake. (Random chance of eating the cake)

	public static AtomicInteger cakes_eaten = new AtomicInteger(0); // There is a cake initially
	public static AtomicBoolean has_cake = new AtomicBoolean(true); // There is a cake initially
	public static final int NUM_TEST_SUBJECTS = 8; // The threads silly

	// Implementation of knowing who crossed the line (visited)
	public static ConcurrentHashMap<Integer, Boolean> visited = new ConcurrentHashMap<Integer, Boolean>();

	public static void main(String[] args) throws Exception {

		// Make a list of Test Subjects to invite to the party
		Thread[] test_subjects = new Thread[NUM_TEST_SUBJECTS];
		for (int i = 0; i < NUM_TEST_SUBJECTS; i++) {
			test_subjects[i] = new Thread(new LabrinthRun(i));
		}

		// Minatour Decides to call a random Test Subject to enter the labrinth each
		// time
		for (int i = ThreadLocalRandom.current().nextInt(0, NUM_TEST_SUBJECTS); true; i = ThreadLocalRandom.current()
				.nextInt(0, NUM_TEST_SUBJECTS)) {
			test_subjects[i].start();

			// wait for i-th Test Subject to leave labrinth
			test_subjects[i].join();

			// Observe whether everyone have crossed the line
			// to know that all Test Subjects have visited the labrinth
			if (visited.size() == NUM_TEST_SUBJECTS)
				break;
		}

		// To print statistics
		int tot_cakes = cakes_eaten.get();

		// Conclude the party
		System.out.println("All guests have now visited the labrinth");

		// Did the Test Subjects enjoy the cake?
		if (tot_cakes != 0) {
			if (tot_cakes == 1)
				System.out.println("1 cake was eaten");
			else System.out.println("A total of " + tot_cakes + "cakes were eaten");
		}
		else System.out.println("No cakes were eaten");

		// Minatour was happy everyone showed up:)
		System.out.println("Minatour is very happy");
	}

	// Thread worker
	static class LabrinthRun implements Runnable {

		private int id;

		public LabrinthRun(int id) {
			this.id = id;
		}

		public void run() {
			// Test Subject now visiting the labrinth
			System.out.println("Test Subject (" + id + ") - now visiting labrinth");

			// Check if there is a cake
			if (!MinatourParty.has_cake.get()) {
				System.out.println("Test Subject (" + id + ") - requests cake from Minatour");
				MinatourParty.has_cake.set(true); 						// "Got" new cake
				MinatourParty.cakes_eaten.incrementAndGet();	// count number of cakes eaten
			}

			// Decide to eat cake
			if (ThreadLocalRandom.current().nextBoolean()) {
				System.out.println("Test Subject (" + id + ") - decided to eat a piece of cake");
				MinatourParty.has_cake.set(false); // cake was eaten :D
			} else
				System.out.println("Test Subject (" + id + ") - decided to leave the cake for the next guest");

			// Now exist the labrinth and be on the side
			// of everyone else that visited the labrinth
			visited.put(this.id, true);
		}
	}
}