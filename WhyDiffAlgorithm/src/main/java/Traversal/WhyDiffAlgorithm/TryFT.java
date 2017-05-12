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

public class TryFT {
	static long startTime = System.currentTimeMillis();
	static GraphDatabaseService graphDB = new GraphDatabaseFactory()
			.newEmbeddedDatabase("newdb");

	public static void main(String[] args) {

		try (Transaction tx = graphDB.beginTx()) {
			String firstId = null, firstActivityId = null;
			String secondId = null, secondActivityId = null;
			String returnedEntity = null, returnedSecondEntity = null;
			String firstActivity = "esc:svi-esc/invocation/132076/block/BFF867C8-81FF-BAD2-4CEF-6F6DC6F0769A";
			String secondActivity = "esc:svi-esc/invocation/132084/block/BFF867C8-81FF-BAD2-4CEF-6F6DC6F0769A";

			String firstEntityAttributes = null;
			String secondEntityAttributes = null;

			String returnedActivity = null;
			String returnedSecondActivity = null;

			Node activity1 = graphDB.findNode(NodeType1.activity, "id",
					firstActivity);
			Node activity2 = graphDB.findNode(NodeType1.activity, "id",
					secondActivity);

			System.out.println("Activity 1 and Activity 2 " + activity1
					+ activity2);

			List<String> addVisited = new ArrayList<String>();
			List<String> addVisited1 = new ArrayList<String>();
			String returnedEntity1 = checkactivity(activity1);
			String returnedEntity2 = checkactivity(activity2);
			do {
				returnedEntity = checkactivity(activity1);
				Node firstEntity = graphDB.findNode(NodeType1.entity, "id",
						returnedEntity);
				firstId = (String) firstEntity.getProperty("id");
				// System.out.println("Entity Id "+firstId);
				firstEntityAttributes = (String) firstEntity
						.getProperty("attributes");
				returnedActivity = checkEntity(firstEntity);
				System.out.println("firstId " + firstId + "\t"
						+ firstEntityAttributes);
				System.out.println("retn Activity " + returnedActivity);
				System.out.println("Activity Traversal");
				System.out.println("Check Multiple Relationships");
				System.out
						.println("********************************************************");
				Iterable<Relationship> rnship = activity1.getRelationships();
				String checkEntityVisited = null;

				System.out.println("Relationship Count "
						+ activity1.getDegree());
				for (Relationship rn : rnship) {
					checkEntityVisited = (String) rn.getOtherNode(activity1)
							.getProperty("id");
					System.out.println("Entity Id " + checkEntityVisited);
					System.out.println("first Id " + firstId);
					// if(!firstId.contains(returnedEntity1))
					if (!firstId.contains(checkEntityVisited)) {
						// System.out.println("hi");
						addVisited.add(checkEntityVisited);
					}
				}
				System.out.println("addVisited " + addVisited.toString());
				addVisited.remove(addVisited.size() - 1);
				if (addVisited.contains(returnedEntity1)) {
					addVisited.remove(returnedEntity1);
				}
				System.out.println("addVisited " + addVisited.toString());
				// System.out.println("hi");
				System.out.println("retn Activity " + returnedActivity);

				firstActivityId = returnedActivity;
				System.out.println("first Activity Id " + firstActivityId);
				activity1 = graphDB.findNode(NodeType1.activity, "id",
						firstActivityId);
				System.out.println("first Activity Id " + firstActivityId);
				// returnedSecondActivity = checkEntity(secondEntity);

				returnedSecondEntity = checkactivity(activity2);
				System.out.println(returnedSecondEntity);
				Node secondEntity = graphDB.findNode(NodeType1.entity, "id",
						returnedSecondEntity);
				secondId = (String) secondEntity.getProperty("id");
				secondEntityAttributes = (String) secondEntity
						.getProperty("attributes");
				returnedSecondActivity = checkEntity(secondEntity);
				System.out.println("Activity Traversal");
				System.out.println("Check Multiple Relationships");
				System.out
						.println("********************************************************");
				Iterable<Relationship> rnship1 = activity2.getRelationships();
				String checkEntityVisited1 = null;
				System.out.println("Relationship Count "
						+ activity2.getDegree());
				for (Relationship rn : rnship1) {
					checkEntityVisited1 = (String) rn.getOtherNode(activity2)
							.getProperty("id");
					if (!secondId.equals(checkEntityVisited1)) {
						addVisited1.add(checkEntityVisited1);
					}
				}
				addVisited1.remove(addVisited1.size() - 1);
				if (addVisited1.contains(returnedEntity2)) {
					addVisited1.remove(returnedEntity2);
				}
				// System.out.println("Check immediate***");
				if (!(addVisited.isEmpty()) && !(addVisited1.isEmpty())) {
					// System.out.println("Check immediate");
					checkUnvisitedNodes(addVisited, addVisited1);
					// System.out.println("**Check immediate**");
					addVisited.clear();
					addVisited1.clear();
				}
				try {
					secondActivityId = returnedSecondActivity;
					activity2 = graphDB.findNode(NodeType1.activity, "id",
							secondActivityId);
				} catch (NullPointerException ex) {

				}
				/*
				 * firstActivityId = returnedActivity;
				 * 
				 * activity1 = graphDB.findNode(NodeType1.activity, "id",
				 * firstActivityId);
				 */
				System.out.println("Comparing Entities " + firstId + "\t\t"
						+ secondId);
				compare(firstEntityAttributes, secondEntityAttributes);
			} while (firstActivityId != null && secondActivityId != null);

		} catch (Exception ex) {

		}
		long stopTime = System.currentTimeMillis();
		long elapsedTime = stopTime - startTime;
		System.out.println(elapsedTime);
	}

	private static void checkUnvisitedNodes(List<String> addVisited,
			List<String> addVisited1) {
		System.out.println("Display unvisted" + addVisited.toString());
		System.out.println("Display unvisted 1" + addVisited1.toString());
		for (int i = 0, j = 0; i < addVisited.size(); i++, j++) {
			String DisplayId = addVisited.get(i);
			String DisplayId2 = addVisited1.get(j);
			try {
				ResourceIterator<Node> missedentity1 = graphDB.findNodes(
						NodeType1.entity, "id", DisplayId);
				ResourceIterator<Node> missedentity2 = graphDB.findNodes(
						NodeType1.entity, "id", DisplayId2);
				Node missedFirstEntity = missedentity1.next();
				Node missedSecondEntity = missedentity2.next();
				System.out.println("Comparing Entities "
						+ (String) missedFirstEntity.getProperty("id") + "\t\t"
						+ (String) missedSecondEntity.getProperty("id"));
				compare((String) missedFirstEntity.getProperty("attributes"),
						(String) missedSecondEntity.getProperty("attributes"));
				String activity1 = null;
				String activity2 = null;
				do {
					activity1 = checkEntity1(missedFirstEntity);
					activity2 = checkEntity1(missedSecondEntity);
					Node activityNode1 = null;
					Node activityNode2 = null;
					System.out.println("act null " + activity1 + activity2);
					if (activity1 != null && activity2 != null) {
						activityNode1 = graphDB.findNode(NodeType1.activity,
								"id", activity1);
						activityNode2 = graphDB.findNode(NodeType1.activity,
								"id", activity2);
						System.out.println("Display act "
								+ activityNode1.getProperty("id") + " "
								+ activityNode2.getProperty("id"));
					} else {
						break;
					}
					String entity1 = checkactivity1(activityNode1);
					String entity2 = checkactivity1(activityNode2);
					try {
						missedFirstEntity = graphDB.findNode(NodeType1.entity,
								"id", entity1);
						missedSecondEntity = graphDB.findNode(NodeType1.entity,
								"id", entity2);
					} catch (NullPointerException ex) {

					}
					System.out.println("check after ***");
					System.out.println("Comparing Entities "
							+ (String) missedFirstEntity.getProperty("id")
							+ "\t\t"
							+ (String) missedSecondEntity.getProperty("id"));
					compare((String) missedFirstEntity
							.getProperty("attributes"),
							(String) missedSecondEntity
									.getProperty("attributes"));
					System.out.println("check null");
				} while (activity1 != null && activity2 != null);
			} catch (Exception ex) {
				System.out.println(ex.toString());
			}
		}
	}

	private static String checkEntity(Node entity1) {
		String activityNode = null;
		TraversalDescription activity1 = graphDB.traversalDescription()
				.breadthFirst().expand(new PathExpander<Object>() {
					@Override
					public Iterable<Relationship> expand(Path path,
							BranchState<Object> state) {
						switch (path.length()) {
						case 0:
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
						throw new UnsupportedOperationException();
					}
				}).evaluator(Evaluators.atDepth(1));
		org.neo4j.graphdb.traversal.Traverser traverser = activity1
				.traverse(entity1);
		for (Node friend : traverser.nodes()) {
			try {
				activityNode = (String) friend.getProperty("id");
			} catch (NullPointerException ex) {

			}
			// System.out.println("Current Node" + activityNode);
		}
		return activityNode;
	}

	private static String checkEntity1(Node entity1) {
		String activityNode = null;
		TraversalDescription activity1 = graphDB.traversalDescription()
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
						throw new UnsupportedOperationException();
					}
				}).evaluator(Evaluators.atDepth(1));
		org.neo4j.graphdb.traversal.Traverser traverser = activity1
				.traverse(entity1);
		for (Node friend : traverser.nodes()) {
			try {
				activityNode = (String) friend.getProperty("id");
			} catch (NullPointerException ex) {

			}
			// System.out.println("Current Node" + activityNode);
		}
		return activityNode;
	}

	private static String checkactivity(Node activity) {
		String entityNode = null;
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
			try {
				entityNode = (String) friend.getProperty("id");
				System.out.println("Entity Node" + entityNode);
			} catch (NullPointerException ex) {

			}
		}
		return entityNode;
	}

	private static String checkactivity1(Node activity) {
		String entityNode = null;
		TraversalDescription entity1 = graphDB.traversalDescription()
				.breadthFirst().expand(new PathExpander<Object>() {
					@Override
					public Iterable<Relationship> expand(Path path,
							BranchState<Object> state) {
						switch (path.length()) {
						case 0:
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
		org.neo4j.graphdb.traversal.Traverser traverser1 = entity1
				.traverse(activity);
		for (Node friend : traverser1.nodes()) {
			try {
				entityNode = (String) friend.getProperty("id");
				System.out.println("Entity Node" + entityNode);
			} catch (NullPointerException ex) {

			}
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
		String checkString = "'prov:label'";
		if (EntityAttributes1.toString().contains(checkString)
				&& EntityAttributes1.toString().contains(checkString)) {
			int provLabel1 = EntityAttributes1.indexOf("'prov:label'");
			int provType1 = EntityAttributes1.indexOf("'prov:type'");
			int provLabel2 = EntityAttributes2.indexOf("'prov:label'");
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
		String checkString1 = "'esc:hashvalue'";
		if (EntityAttributes1.toString().contains(checkString1)
				&& EntityAttributes2.toString().contains(checkString1)) {
			int provLabel1 = EntityAttributes1.indexOf("'esc:hashvalue'");
			int provType1 = EntityAttributes1.indexOf("'prov:type'");
			int provLabel2 = EntityAttributes2.indexOf("'esc:hashvalue'");
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

