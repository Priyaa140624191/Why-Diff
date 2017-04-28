package Traversal.WhyDiffAlgorithm;

import org.neo4j.graphdb.GraphDatabaseService;
import org.neo4j.graphdb.Label;
import org.neo4j.graphdb.Node;
import org.neo4j.graphdb.RelationshipType;
import org.neo4j.graphdb.ResourceIterator;
import org.neo4j.graphdb.Transaction;
import org.neo4j.graphdb.factory.GraphDatabaseFactory;
import org.neo4j.graphdb.traversal.Evaluators;
import org.neo4j.graphdb.traversal.TraversalDescription;

import Traversal.WhyDiffAlgorithm.App.NodeType1;
import Traversal.WhyDiffAlgorithm.App.RelationType1;
import Traversal.WhyDiffAlgorithm.NewMovie.Labels;
import Traversal.WhyDiffAlgorithm.NewMovie.RelationshipTypes;

public class FTTraversal {
	public static void main(String[] args) {
		GraphDatabaseService graphDB = new GraphDatabaseFactory()
				.newEmbeddedDatabase("newdb");
		System.out.println("hi");
		try (Transaction tx = graphDB.beginTx()) {
			/*
			 * ResourceIterator<Node> movies = graphDB.findNodes(
			 * NodeType1.entity ); System.out.println( "Movies:" ); while(
			 * movies.hasNext() ) { Node movie = movies.next();
			 * System.out.println( "\t" + movie.getProperty( "id" ) ); }
			 * 
			 * // Find all users ResourceIterator<Node> users =
			 * graphDB.findNodes( NodeType1.activity ); System.out.println(
			 * "Users:" ); while( users.hasNext() ) { Node user = users.next();
			 * System.out.println( "\t" + user.getProperty( "id" ) ); }
			 * System.out.println("Has not found any nodes");
			 */
			String firstId = "esc:svi-esc/document/663/664";
			String secondId = "esc:svi-esc/document/663/664";
			String activityNode = null;
			String entityNode = null;
			ResourceIterator<Node> entity = graphDB.findNodes(NodeType1.entity,
					"id", firstId);
			Node firstEntity = entity.next();
			Node secondEntity = entity.next();
			// System.out.println(entity.next().getProperty("attributes"));
			// System.out.println(entity.next().getProperty("attributes"));
			do {
				String firstEntityAttributes = (String) firstEntity.getProperty("attributes");
				String secondEntityAttributes = (String) firstEntity.getProperty("attributes");
				
				TraversalDescription activity = graphDB.traversalDescription()
						.breadthFirst().relationships(RelationType1.used)
						.evaluator(Evaluators.atDepth(1));
				org.neo4j.graphdb.traversal.Traverser traverser = activity
						.traverse(firstEntity);
				System.out.println("Entity Traversal");
				for (Node friend : traverser.nodes()) {
					activityNode = (String) friend.getProperty("id");
					System.out.println("Activity Node" + activityNode);
				}

				Node activity1 = graphDB.findNode(NodeType1.activity, "id",
						activityNode);
				TraversalDescription entity1 = graphDB.traversalDescription()
						.breadthFirst().relationships(RelationType1.wasGeneratedBy)
						.evaluator(Evaluators.atDepth(1));
				org.neo4j.graphdb.traversal.Traverser traverser1 = entity1
						.traverse(activity1);
				System.out.println("Activity Traversal");
				for (Node friend : traverser1.nodes()) {
					entityNode = (String) friend.getProperty("id");
					System.out.println("Entity Node" + entityNode);
				}
				firstId = entityNode;
				firstEntity = graphDB.findNode(NodeType1.entity,
						"id", firstId);
				
				TraversalDescription activity2 = graphDB.traversalDescription()
						.breadthFirst().relationships(RelationType1.used)
						.evaluator(Evaluators.atDepth(1));
				org.neo4j.graphdb.traversal.Traverser traverser2 = activity2
						.traverse(secondEntity);
				System.out.println("Entity Traversal");
				for (Node friend : traverser2.nodes()) {
					activityNode = (String) friend.getProperty("id");
					System.out.println("Activity Node" + activityNode);
				}

				Node activity22 = graphDB.findNode(NodeType1.activity, "id",
						activityNode);
				TraversalDescription entity2 = graphDB.traversalDescription()
						.breadthFirst().relationships(RelationType1.wasGeneratedBy)
						.evaluator(Evaluators.atDepth(1));
				org.neo4j.graphdb.traversal.Traverser traverser12 = entity2
						.traverse(activity22);
				System.out.println("Activity Traversal");
				for (Node friend : traverser12.nodes()) {
					entityNode = (String) friend.getProperty("id");
					System.out.println("Entity Node" + entityNode);
				}
				secondId = entityNode;
				secondEntity = graphDB.findNode(NodeType1.entity,
						"id", secondId);
				
				compare(firstEntityAttributes, secondEntityAttributes);
			} while (! (firstId.contains("esc:svi-esc/document/132101/132102")));

			
			
			// System.out.println(entity.);
			/*
			 * TraversalDescription myFriends = graphDB.traversalDescription()
			 * .depthFirst() .relationships(RelationType1.used)
			 * .evaluator(Evaluators.atDepth(1));
			 * org.neo4j.graphdb.traversal.Traverser traverser = myFriends
			 * .traverse(entity); System.out.println("Entity Traversal"); for
			 * (Node friend : traverser.nodes()) { System.out.println("\t" +
			 * friend.getProperty("id")); }
			 */
		} catch (Exception ex) {
			System.out.println(ex.toString());

		}
	}

	private static void compare(String firstEntityAttributes,
			String secondEntityAttributes) {
		// TODO Auto-generated method stub
		String checkString1 = "'esc:hashvalue'";
		if (firstEntityAttributes.contains(checkString1)
				&& secondEntityAttributes.contains(checkString1))
			checkFurtherEntityEqual(firstEntityAttributes, secondEntityAttributes);
		else
			checkFirstEntityEqual(firstEntityAttributes, secondEntityAttributes);
		
		
	}

	private static void checkFirstEntityEqual(String EntityAttributes1,
			String EntityAttributes2) {
		// TODO Auto-generated method stub

		// TODO Auto-generated method stub
		String checkString = "'prov:label'";
		if (EntityAttributes1.toString().contains(checkString)
				&& EntityAttributes1.toString().contains(checkString)) {
			// System.out.println("If Entity Attributes has prov:label");
			int provLabel1 = EntityAttributes1.indexOf("'prov:label'");
			// System.out.println("starting index:" + provLabel);
			int provType1 = EntityAttributes1.indexOf("'prov:type'");
			int provLabel2 = EntityAttributes2.indexOf("'prov:label'");
			// System.out.println("starting index:" + provLabel);
			int provType2 = EntityAttributes2.indexOf("'prov:type'");
			String fileLabel1 = EntityAttributes1.substring(provLabel1,
					provType1);
			String fileLabel2 = EntityAttributes2.substring(provLabel2,
					provType2);
			if (fileLabel1.equals(fileLabel2)) {
				System.out.println("Both are equal");
			} else
				System.out.println("Difference spotted");
		}	
	}

	private static void checkFurtherEntityEqual(String EntityAttributes1,
			String EntityAttributes2) {
		// TODO Auto-generated method stub

		// TODO Auto-generated method stub
		String checkString1 = "'esc:hashvalue'";
		if (EntityAttributes1.toString().contains(checkString1)
				&& EntityAttributes2.toString().contains(checkString1)) {
			// System.out.println("If Entity Attributes has prov:label");
			int provLabel1 = EntityAttributes1.indexOf("'esc:hashvalue'");
			// System.out.println("starting index:" + provLabel);
			int provType1 = EntityAttributes1.indexOf("'prov:type'");
			int provLabel2 = EntityAttributes2.indexOf("'esc:hashvalue'");
			// System.out.println("starting index:" + provLabel);
			int provType2 = EntityAttributes2.indexOf("'prov:type'");
			String fileLabel1 = EntityAttributes1.substring(provLabel1,
					provType1);
			String fileLabel2 = EntityAttributes2.substring(provLabel2,
					provType2);
			if (fileLabel1.equals(fileLabel2)) {
				System.out.println("Both are equal");
			} else
				System.out.println("Difference spotted");
		}
	
		
	}
}
