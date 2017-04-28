package Traversal.WhyDiffAlgorithm;

import java.util.HashSet;
import java.util.Set;

import org.neo4j.graphdb.Direction;
import org.neo4j.graphdb.GraphDatabaseService;
import org.neo4j.graphdb.Label;
import org.neo4j.graphdb.Node;
import org.neo4j.graphdb.Relationship;
import org.neo4j.graphdb.RelationshipType;
import org.neo4j.graphdb.ResourceIterator;
import org.neo4j.graphdb.Transaction;
import org.neo4j.graphdb.factory.GraphDatabaseFactory;
import org.neo4j.graphdb.traversal.Evaluators;
import org.neo4j.graphdb.traversal.TraversalDescription;

/**
 * Hello world!
 * 
 */
public class NewMovie {
	public enum Labels implements Label {
		USER, MOVIE;
	}

	public enum RelationshipTypes implements RelationshipType {
		IS_FRIEND_OF, HAS_SEEN;
	}

	public static Relationship seeMovie(Node user, Node movie, int stars) {
		Relationship relationship = user.createRelationshipTo(movie,
				RelationshipTypes.HAS_SEEN);
		relationship.setProperty("stars", stars);
		return relationship;
	}

	public static void main(String[] args) {
		GraphDatabaseService graphDB = new GraphDatabaseFactory()
				.newEmbeddedDatabase("movie");
		/*
		 * Node myNode = graphDB.createNode(); myNode.setProperty("name",
		 * "Steve"); myNode.addLabel(Labels.USER);
		 */
		try (Transaction tx = graphDB.beginTx()) {
			// Node myNode = graphDB.createNode(Labels.USER);
			Node steve = graphDB.createNode(Labels.USER);
			steve.setProperty("name", "Steve");
			Node linda = graphDB.createNode(Labels.USER);
			linda.setProperty("name", "Linda");
			Node michael = graphDB.createNode(Labels.USER);
			michael.setProperty("name", "Michael");
			Node rebecca = graphDB.createNode(Labels.USER);
			rebecca.setProperty("name", "Rebecca");

			steve.createRelationshipTo(michael, RelationshipTypes.IS_FRIEND_OF);
			steve.createRelationshipTo(rebecca, RelationshipTypes.IS_FRIEND_OF);
			steve.createRelationshipTo(linda, RelationshipTypes.IS_FRIEND_OF);

			Node divergent = graphDB.createNode(Labels.MOVIE);
			divergent.setProperty("name", "Divergent");
			Node hero = graphDB.createNode(Labels.MOVIE);
			hero.setProperty("name", "Big Hero 6");
			Node cinderella = graphDB.createNode(Labels.MOVIE);
			cinderella.setProperty("name", "Cinderella");
			Node interview = graphDB.createNode(Labels.MOVIE);
			interview.setProperty("name", "The Interview");

			seeMovie(steve, divergent, 5);
			seeMovie(steve, hero, 5);
			seeMovie(steve, cinderella, 4);
			seeMovie(rebecca, hero, 5);
			seeMovie(rebecca, cinderella, 5);
			seeMovie(michael, hero, 5);
			seeMovie(michael, cinderella, 3);
			seeMovie(linda, divergent, 4);
			seeMovie(linda, hero, 5);
			seeMovie(linda, cinderella, 5);

			// Find all movies in our database
			ResourceIterator<Node> movies = graphDB.findNodes(Labels.MOVIE);
			System.out.println("Movies:");
			while (movies.hasNext()) {
				Node movie = movies.next();
				System.out.println("\t" + movie.getProperty("name"));
			}

			// Find all users
			ResourceIterator<Node> users = graphDB.findNodes(Labels.USER);
			System.out.println("Users:");
			while (users.hasNext()) {
				Node user = users.next();
				System.out.println("\t" + user.getProperty("name"));
			}
			// Compute average movie rating
			movies = graphDB.findNodes(Labels.MOVIE);
			System.out.println("Movie Ratings:");
			while (movies.hasNext()) {
				Node movie = movies.next();

				// Follow all HAS_SEEN relationships and get their star rating
				Iterable<Relationship> relationships = movie.getRelationships(
						Direction.INCOMING, RelationshipTypes.HAS_SEEN);
				int totalStars = 0;
				int relationshipCount = 0;
				for (Relationship relationship : relationships) {
					Integer stars = (Integer) relationship.getProperty("stars");
					totalStars += stars;
					relationshipCount++;
				}
				System.out.println("\t" + movie.getProperty("name")
						+ ", Viewers: " + relationshipCount
						+ ", Average rating: " + (float) totalStars
						/ relationshipCount);
			}
			users = graphDB.findNodes(Labels.USER);
			System.out.println("Users:");
			while (users.hasNext()) {
				Node user = users.next();
				System.out
						.print("\t" + user.getProperty("name") + " has seen ");
				for (Relationship relationship : user
						.getRelationships(RelationshipTypes.HAS_SEEN)) {
					Node movie = relationship.getOtherNode(user);
					System.out.print("\t" + movie.getProperty("name"));
				}
				System.out.println();
			}
			michael = graphDB.findNode(Labels.USER, "name", "Michael");

			// Find all of Michael's movies
			Set<String> michaelsMovies = new HashSet<String>();
			for (Relationship relationship : michael.getRelationships(
					Direction.OUTGOING, RelationshipTypes.HAS_SEEN)) {
				michaelsMovies.add((String) relationship.getOtherNode(michael)
						.getProperty("name"));
			}

			// Find all of Michael's friends
			Set<String> friendsMovies = new HashSet<String>();
			for (Relationship relationship : michael
					.getRelationships(RelationshipTypes.IS_FRIEND_OF)) {
				Node friend = relationship.getOtherNode(michael);

				// Find all movies that Michael's friend has seen
				for (Relationship relationship1 : friend.getRelationships(
						Direction.OUTGOING, RelationshipTypes.HAS_SEEN)) {
					// Get the stars property and only include it if it has 4 or
					// more stars
					if ((Integer) relationship1.getProperty("stars") > 3) {
						// Add this movie to our friendsMovie set
						friendsMovies.add((String) relationship1.getOtherNode(
								friend).getProperty("name"));
					}
				}
			}
			// Remove all of the movies that Michael has already seen
			friendsMovies.removeAll(michaelsMovies);

			// Show the movies with a rating of 4 or 5 that Michael hasn't seen
			System.out
					.println("Movies that Michael hasn't see, but his friends"
							+ "have seen and given a rating of 4 or higher:");
			for (String movie : friendsMovies) {
				System.out.println("\t" + movie);
			}
			michael = graphDB.findNode(Labels.USER, "name", "Michael");

			// Find Michael's friends
			TraversalDescription myFriends = graphDB.traversalDescription()
					.breadthFirst()
					.relationships(RelationshipTypes.IS_FRIEND_OF)
					.evaluator(Evaluators.atDepth(2));
			org.neo4j.graphdb.traversal.Traverser traverser = myFriends
					.traverse(michael);
			System.out.println("Michael's friends: ");
			for (Node friend : traverser.nodes()) {
				System.out.println("\t" + friend.getProperty("name"));
			}
			TraversalDescription myMovies = graphDB.traversalDescription()
					.breadthFirst().relationships(RelationshipTypes.HAS_SEEN)
					.evaluator(Evaluators.atDepth(1));
			traverser = myMovies.traverse(michael);
			System.out.println("Michael's movies: ");
			for (Node movie : traverser.nodes()) {
				System.out.println("\t" + movie.getProperty("name"));
			}
		} catch (Exception ex) {

		}
		// Find Michael

	}
}
