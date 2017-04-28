package Traversal.WhyDiffAlgorithm;

import java.util.ArrayList;
import java.util.List;

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

public class TryBT {
	static GraphDatabaseService graphDB = new GraphDatabaseFactory()
			.newEmbeddedDatabase("newdb");

	public static void main(String[] args) {

		try (Transaction tx = graphDB.beginTx()) {
			String firstId = "esc:svi-esc/document/132101/132102";
			String secondId = "esc:svi-esc/document/132119/132120";
			ResourceIterator<Node> entity = graphDB.findNodes(NodeType1.entity,
					"id", firstId);
			Node firstEntity = entity.next();
			Node secondEntity = entity.next();
			String returnedEntity = null, returnedSecondEntity = null;
			String returnedActivity = null, returnedSecondActivity = null;

			List<String> addVisited = new ArrayList<String>();
			List<String> addVisited1 = new ArrayList<String>();
			// do {
			/*
			 * String firstEntityAttributes = (String)
			 * firstEntity.getProperty("attributes"); String
			 * secondEntityAttributes = (String)
			 * firstEntity.getProperty("attributes");
			 */
			do {
				String firstEntityAttributes = (String) firstEntity
						.getProperty("attributes");
				String secondEntityAttributes = (String) firstEntity
						.getProperty("attributes");

				returnedActivity = checkEntity(firstEntity);

				Node activity1 = graphDB.findNode(NodeType1.activity, "id",
						returnedActivity);
				returnedEntity = checkactivity(activity1);
				System.out.println("Activity Traversal");
				System.out.println("Check Multiple Relationships");
				System.out
						.println("********************************************************");
				Iterable<Relationship> rnship = activity1.getRelationships();
				String checkEntityVisited = null;

				System.out.println("Relationship Count "
						+ activity1.getDegree());
				for (Relationship rn : rnship) {
					checkEntityVisited = (String) rn.getEndNode().getProperty(
							"id");
					System.out.println("Entity Id" + checkEntityVisited);
					if (!firstId.contains(checkEntityVisited)) {
						addVisited.add(checkEntityVisited);
					}

					/*
					 * Node NewEntity = graphDB.findNode(NodeType1.entity, "id",
					 * checkEntityVisited);
					 * System.out.println("New Entity Id"+NewEntity.getId());
					 */
					// returnedActivity = checkEntity(NewEntity);
					// }

				}
				addVisited.remove(addVisited.size() - 1);

				/*
				 * 
				 * System.out.println("List Display " +addVisited.toString());
				 * if (!addVisited.isEmpty()) { firstId =
				 * addVisited.get(0).toString(); firstEntity =
				 * graphDB.findNode(NodeType1.entity, "id", firstId); } else {
				 */

				firstId = returnedEntity;
				firstEntity = graphDB.findNode(NodeType1.entity, "id", firstId);

				returnedSecondActivity = checkEntity(secondEntity);
				Node activity2 = graphDB.findNode(NodeType1.activity, "id",
						returnedSecondActivity);
				returnedSecondEntity = checkactivity(activity2);
				System.out.println("Activity Traversal");
				System.out.println("Check Multiple Relationships");
				System.out
						.println("********************************************************");
				Iterable<Relationship> rnship1 = activity2.getRelationships();
				String checkEntityVisited1 = null;
				System.out.println("Relationship Count "
						+ activity2.getDegree());
				for (Relationship rn : rnship1) {
					checkEntityVisited1 = (String) rn.getEndNode().getProperty(
							"id");
					if (!secondId.contains(checkEntityVisited1)) {
						addVisited1.add(checkEntityVisited1);
					}
				}
				addVisited1.remove(addVisited1.size() - 1);
				if (!(addVisited.isEmpty()) && !(addVisited1.isEmpty())) {
					checkUnvisitedNodes(addVisited, addVisited1);
					addVisited.clear();
					addVisited1.clear();
				}

				secondId = returnedSecondEntity;
				secondEntity = graphDB.findNode(NodeType1.entity, "id",
						secondId);
				System.out.println("Comparing Entities " + firstId + "\t\t"
						+ secondId);
				compare(firstEntityAttributes, secondEntityAttributes);
			} while (!(firstId.contains("esc:svi-esc/document/132101/132102")));

			/*
			 * org.neo4j.graphdb.traversal.Traverser traverser = activity
			 * .traverse(firstEntity); for (Node friend : traverser.nodes()) {
			 * 
			 * currentNode = (String) friend.getProperty("id"); String
			 * currentNodeAttribute = (String) friend
			 * .getProperty("attributes"); System.out.println("Current Node" +
			 * currentNode); if (currentNodeAttribute .contains("Entity") &&
			 * !(currentNode.equalsIgnoreCase(firstId))) {
			 * System.out.println("yes"); firstId = currentNode; firstEntity =
			 * graphDB.findNode(NodeType1.activity, "id", firstId);
			 * System.out.println("Relationships");
			 * 
			 * // System.out.println("First Entity" + firstId + "\t" + //
			 * firstEntity.toString()); } else if
			 * (!(currentNode.equalsIgnoreCase(firstId) && currentNodeAttribute
			 * .contains("Activity"))) { firstId = currentNode; firstEntity =
			 * graphDB.findNode(NodeType1.activity, "id", firstId);
			 * System.out.println("Relationships"); } break; }
			 */
			// } while
			// (!(firstId.contains("esc:svi-esc/document/132101/132102")));

			/*
			 * Node activity1 = graphDB.findNode(NodeType1.activity, "id",
			 * activityNode); TraversalDescription entity1 =
			 * graphDB.traversalDescription().breadthFirst() .expand(new
			 * PathExpander<Object>() {
			 * 
			 * @Override public Iterable<Relationship> expand(Path path,
			 * BranchState<Object> state) { switch (path.length()) { case 0:
			 * return path.endNode().getRelationships(
			 * DynamicRelationshipType.withName("wasGeneratedBy") ); case 1:
			 * return path.endNode().getRelationships(
			 * DynamicRelationshipType.withName("used") ); default: return
			 * Iterables.empty(); } }
			 * 
			 * @Override public PathExpander<Object> reverse() { // not used for
			 * unidirectional traversals throw new
			 * UnsupportedOperationException(); } });
			 * org.neo4j.graphdb.traversal.Traverser traverser1 = entity1
			 * .traverse(activity1); System.out.println("Activity Traversal");
			 * for (Node friend : traverser1.nodes()) { entityNode = (String)
			 * friend.getProperty("id"); System.out.println("Entity Node" +
			 * entityNode); } firstId = entityNode; firstEntity =
			 * graphDB.findNode(NodeType1.entity, "id", firstId);
			 */
			// }
			// while(!(firstId.contains("esc:svi-esc/document/132101/132102")));

		} catch (Exception ex) {

		}
	}

	private static void checkUnvisitedNodes(List<String> addVisited,
			List<String> addVisited1) {
		System.out.println("Display unvisted" + addVisited.toString());
		System.out.println("Display unvisted 1" + addVisited1.toString());
		System.out.println("try");
		String DisplayId = addVisited.get(0);
		String DisplayId2 = addVisited1.get(0);
		// System.out.println("Display Id 1"+addVisited1.get(0));
		try {
			ResourceIterator<Node> missedentity1 = graphDB.findNodes(
					NodeType1.entity, "id", DisplayId);
			ResourceIterator<Node> missedentity2 = graphDB.findNodes(
					NodeType1.entity, "id", DisplayId2);
			Node missedFirstEntity = missedentity1.next();
			Node missedSecondEntity = missedentity2.next();
			String returnedActivity1 = checkEntity(missedFirstEntity);
			String returnedActivity2 = checkactivity(missedSecondEntity);
			System.out.println("Missed Relationships " + returnedActivity1);
			System.out.println("Missed Relationships 1 " + returnedActivity2);

			compare((String) missedFirstEntity.getProperty("attributes"),
					(String) missedSecondEntity.getProperty("attributes"));
		} catch (Exception ex) {
			System.out.println(ex.toString());
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
							System.out.println(path.endNode().getRelationships(
									DynamicRelationshipType.withName("used")));
							return path.endNode().getRelationships(
									DynamicRelationshipType.withName("used"));
						case 1:
							return path.endNode().getRelationships(
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

							return path.endNode().getRelationships(
									DynamicRelationshipType
											.withName("wasGeneratedBy"));
						case 1:
							return path.endNode().getRelationships(
									DynamicRelationshipType.withName("used"));
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

	private static void compare(String firstEntityAttributes,
			String secondEntityAttributes) {
		// TODO Auto-generated method stub
		String checkString1 = "'esc:hashvalue'";
		if (firstEntityAttributes.contains(checkString1)
				&& secondEntityAttributes.contains(checkString1))
			checkFurtherEntityEqual(firstEntityAttributes,
					secondEntityAttributes);
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
