package Traversal.WhyDiffAlgorithm;

import org.neo4j.graphdb.Direction;
import org.neo4j.graphdb.DynamicRelationshipType;
import org.neo4j.graphdb.GraphDatabaseService;
import org.neo4j.graphdb.Node;
import org.neo4j.graphdb.Path;
import org.neo4j.graphdb.PathExpander;
import org.neo4j.graphdb.Relationship;
import org.neo4j.graphdb.ResourceIterator;
import org.neo4j.graphdb.Transaction;
import org.neo4j.graphdb.factory.GraphDatabaseFactory;
import org.neo4j.graphdb.traversal.BranchState;
import org.neo4j.graphdb.traversal.Evaluators;
import org.neo4j.graphdb.traversal.TraversalDescription;
import org.neo4j.helpers.collection.Iterables;

import Traversal.WhyDiffAlgorithm.App.NodeType1;

public class TryNew {
	static GraphDatabaseService graphDB = new GraphDatabaseFactory()
	.newEmbeddedDatabase("newdb");
	public static void main(String[] args) {
		
		System.out.println("hi");
		String firstId = "esc:svi-esc/document/663/664";
		String returnedEntity = null;
		try (Transaction tx = graphDB.beginTx()) {
			int resCnt = 0;
			ResourceIterator<Node> entity = graphDB.findNodes(NodeType1.entity,
					"id", firstId);
			Node firstEntity = entity.next();
			//Node firstEntity = entity.next();
			do
			{
			for (Relationship rn : firstEntity.getRelationships(Direction.BOTH)) {
				System.out.println("Relationship Type " + rn.getId());
				System.out.println("Relationship Nodes " + rn.getNodes());
				Node activity = rn.getOtherNode(firstEntity);
				System.out.println(activity.getProperty("id"));
				returnedEntity = checkactivity(activity);
				System.out.println("Returned Entity "+returnedEntity);
				for (Relationship activityRel : activity
						.getRelationships(Direction.BOTH)) {

					Node entity1 = activityRel.getOtherNode(activity);
					String returnedActivity = checkEntity(entity1);
					System.out.println("Returned Activity "+returnedActivity);
					if (entity1.getId() == firstEntity.getId())
						continue;
					resCnt++;
				}
				System.out.println("Count "+resCnt);
			}
			firstId = returnedEntity;
			firstEntity = graphDB.findNode(NodeType1.entity,
					"id", firstId);
			}while (! (firstId.contains("esc:svi-esc/document/132101/132102")));
		} catch (Exception ex) {

		}
	}

	private static String checkEntity(Node entity1) {
		String activityNode = null;
		// TODO Auto-generated method stub
		TraversalDescription activity1 = graphDB.traversalDescription()
				.breadthFirst().expand(new PathExpander<Object>() {
					@Override
					public Iterable<Relationship> expand(Path path,
							BranchState<Object> state) {
						switch (path.length()) {
						case 0:
							System.out.println(path.endNode()
									.getRelationships(
											DynamicRelationshipType
													.withName("used")));
							return path.endNode().getRelationships(
									DynamicRelationshipType
											.withName("used"));
						case 1:
							return path
									.endNode()
									.getRelationships(
											DynamicRelationshipType
													.withName("wasGeneratedBy"));
						default:
							return Iterables.empty();
						}
					}

					@Override
					public PathExpander<Object> reverse() {
						// not used for unidirectional traversals
						throw new UnsupportedOperationException();
					}
				}).evaluator(Evaluators.atDepth(1));
		org.neo4j.graphdb.traversal.Traverser traverser = activity1
				.traverse(entity1);
		System.out.println("Entity Traversal");
		for (Node friend : traverser.nodes()) {
			activityNode = (String) friend.getProperty("id");
			System.out.println("Current Node" + activityNode);
		}
		return activityNode;
	}

	private static String checkactivity(Node activity) {
		String entityNode = null;
		// TODO Auto-generated method stub
		TraversalDescription entity1 = graphDB.traversalDescription()
				.breadthFirst().expand(new PathExpander<Object>() {
					@Override
					public Iterable<Relationship> expand(Path path,
							BranchState<Object> state) {
						switch (path.length()) {
						case 0:

							return path
									.endNode()
									.getRelationships(
											DynamicRelationshipType
													.withName("wasGeneratedBy"));
						case 1:
							return path.endNode().getRelationships(
									DynamicRelationshipType
											.withName("used"));
						default:
							return Iterables.empty();
						}
					}

					@Override
					public PathExpander<Object> reverse() {
						// not used for unidirectional traversals
						throw new UnsupportedOperationException();
					}
				}).evaluator(Evaluators.atDepth(1));
		org.neo4j.graphdb.traversal.Traverser traverser1 = entity1
				.traverse(activity);
		for (Node friend : traverser1.nodes()) {
			entityNode = (String) friend.getProperty("id");
			System.out.println("Entity Node" + entityNode);
		}
		return entityNode;
	}
}
