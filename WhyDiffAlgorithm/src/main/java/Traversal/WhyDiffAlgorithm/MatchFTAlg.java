package Traversal.WhyDiffAlgorithm;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import org.neo4j.cypher.javacompat.ExecutionEngine;
import org.neo4j.cypher.javacompat.ExecutionResult;
import org.neo4j.graphdb.DynamicRelationshipType;
import org.neo4j.graphdb.GraphDatabaseService;
import org.neo4j.graphdb.Node;
import org.neo4j.graphdb.Path;
import org.neo4j.graphdb.PathExpander;
import org.neo4j.graphdb.Relationship;
import org.neo4j.graphdb.ResourceIterable;
import org.neo4j.graphdb.ResourceIterator;
import org.neo4j.graphdb.Result;
import org.neo4j.graphdb.Transaction;
import org.neo4j.graphdb.factory.GraphDatabaseFactory;
import org.neo4j.graphdb.traversal.BranchState;
import org.neo4j.graphdb.traversal.Evaluators;
import org.neo4j.graphdb.traversal.TraversalDescription;
import org.neo4j.helpers.collection.Iterables;

import scala.Array;
import Traversal.WhyDiffAlgorithm.App.NodeType1;
import Traversal.WhyDiffAlgorithm.NewMovie.RelationshipTypes;

@SuppressWarnings({ "deprecation", "unused" })
public class MatchFTAlg {
	static long startTime = System.currentTimeMillis();
	static GraphDatabaseService graphDB = new GraphDatabaseFactory()
			.newEmbeddedDatabase("Lib3");
	static String InvocationId1 = "6471";
	static String InvocationId2 = "5463";

	static List<String> firstActivityList = null;
	static List<String> secondActivityList = null;

	static List<String> firstActivityNewList = new ArrayList<String>();
	static List<String> secondActivityNewList = new ArrayList<String>();

	static boolean first_hop = true;

	static String lastEntityId1 = null;
	static String lastEntityId2 = null;

	static List<List<String>> divergingNodeLists = new ArrayList<List<String>>();
	static List<String> divergingNodes = new ArrayList<String>();

	public static void main(String[] args) {

		try (Transaction tx = graphDB.beginTx()) {

			boolean visited = false;

			compareFirstEntity(InvocationId1, InvocationId2);

			Traverse(InvocationId1, InvocationId2);

			System.out.println("Listing Divergent nodes");
			System.out
					.println("|-------------------------------------------------------------------------------------------------------------------------------------------------|");
			for (int i = 0; i < divergingNodeLists.size(); i++) {
				for (int j = 0; j < divergingNodes.size(); j++) {
					System.out.print("|" + divergingNodeLists.get(i).get(j)
							+ "|");
				}
				System.out.println("");
			}
			System.out
					.println("|-------------------------------------------------------------------------------------------------------------------------------------------------|");

		} catch (Exception ex) {
			System.out.println(ex.toString());

		}
	}

	private static List<String> compareFirstEntity(String invocationId1,
			String invocationId2) {
		List<String> EntityIds1 = null;
		try {

			ExecutionEngine execEngine = new ExecutionEngine(graphDB);
			ExecutionResult getEntity1 = execEngine
					.execute("MATCH (n:entity)<-[r:used]-(p:activity) WHERE p.id =~ \".*"
							+ invocationId1
							+ ".*\" MATCH (n) WHERE NOT (n)-[:wasGeneratedBy]->(:activity)"
							+ " MATCH (p)-[r1:used]->(:entity) with n,p,count(r1) as cnt WHERE cnt=1"
							+ " return n as firstEntity1 ORDER BY id(n)");

			ExecutionResult getEntity2 = execEngine
					.execute("MATCH (n:entity)<-[r:used]-(p:activity) WHERE p.id =~ \".*"
							+ invocationId2
							+ ".*\" MATCH (n) WHERE NOT (n)-[:wasGeneratedBy]->(:activity)"
							+ " MATCH (p)-[r1:used]->(:entity) with n,p,count(r1) as cnt WHERE cnt=1"
							+ " return n as firstEntity2 ORDER BY id(n)");
			for (Map<String, Object> row1 : getEntity1) {
				Node firstEntity1 = (Node) row1.get("firstEntity1");
				for (Map<String, Object> row2 : getEntity2) {
					Node firstEntity2 = (Node) row2.get("firstEntity2");
					System.out.println("Comparing Entities :"
							+ firstEntity1.getProperty("id") + "\t"
							+ firstEntity2.getProperty("id"));
					boolean isEqual = compare(
							firstEntity1.getProperty("attributes").toString(),
							firstEntity2.getProperty("attributes").toString(),
							firstEntity1.getProperty("id").toString(),
							firstEntity2.getProperty("id").toString());
					if (!isEqual) {
						divergingNodes = new ArrayList<String>();
						divergingNodes.add(firstEntity1.getProperty("id")
								.toString());
						divergingNodes.add(firstEntity2.getProperty("id")
								.toString());
						divergingNodeLists.add(divergingNodes);
					}
					break;
				}
			}
		} catch (Exception ex) {

		}
		return EntityIds1;
	}

	private static void Traverse(String invocationId12, String invocationId22) {
		do {
			if (first_hop == true) {
				firstActivityList = findFirstActivity(InvocationId1);
				secondActivityList = findFirstActivity(InvocationId2);
				first_hop = false;
			} else {
				firstActivityList = new ArrayList<String>(firstActivityNewList);
				secondActivityList = new ArrayList<String>(
						secondActivityNewList);
				firstActivityNewList.clear();
				secondActivityNewList.clear();
			}
			System.out.println("Iterate List " + firstActivityList.toString()
					+ "\t" + secondActivityList.toString());

			for (String firstActivity : firstActivityList) {

				for (String secondActivity : secondActivityList) {

					Node firstActivityNode = graphDB.findNode(
							NodeType1.activity, "id", firstActivity);

					Node SecondActivityNode = graphDB.findNode(
							NodeType1.activity, "id", secondActivity);

					ResourceIterable<Node> returnedEntity1List = getWasGeneratedBy(firstActivityNode);
					ResourceIterable<Node> returnedEntity2List = getWasGeneratedBy(SecondActivityNode);
					System.out.println("Returned Entity1List "
							+ returnedEntity1List.iterator().next()
									.getProperty("id")
							+ "\t"
							+ returnedEntity2List.iterator().next()
									.getProperty("id"));
					for (Node returned1Entity : returnedEntity1List) {
						for (Node returned2Entity : returnedEntity2List) {
							lastEntityId1 = returned1Entity.getProperty("id")
									.toString();
							lastEntityId2 = returned2Entity.getProperty("id")
									.toString();
							boolean zip = IsZipped(
									returned1Entity.getProperty("id")
											.toString(), returned2Entity
											.getProperty("id").toString());
							if (zip == true) {
								System.out.println("Comparing Entities :"
										+ returned1Entity.getProperty("id")
										+ "\t"
										+ returned2Entity.getProperty("id"));
								boolean isEqual = compare(
										returned1Entity.getProperty(
												"attributes").toString(),
										returned2Entity.getProperty(
												"attributes").toString(),
										returned1Entity.getProperty("id")
												.toString(), returned2Entity
												.getProperty("id").toString());
								boolean added = divergingNodeLists.toString().contains(returned1Entity
										.getProperty("id").toString());
								if ((!isEqual) && (!added)) {
									divergingNodes = new ArrayList<String>();
									divergingNodes.add(returned1Entity
											.getProperty("id").toString());
									divergingNodes.add(returned2Entity
											.getProperty("id").toString());
									divergingNodeLists.add(divergingNodes);
								}
								try {
									ResourceIterable<Node> returned1ActivityList = getUsed(returned1Entity);
									System.out
											.println("Returned Activity List "
													+ returned1ActivityList
															.iterator().next()
															.getProperty("id"));
									ResourceIterable<Node> returned2ActivityList = getUsed(returned2Entity);

									for (Node returnActivity1 : returned1ActivityList) {
										for (Node returnActivity2 : returned2ActivityList) {
											boolean activityzip = isActivityZipped(
													returnActivity1
															.getProperty("id")
															.toString(),
													returnActivity2
															.getProperty("id")
															.toString());
											if (activityzip == true) {
												firstActivityNewList
														.add(returnActivity1
																.getProperty(
																		"id")
																.toString());
												secondActivityNewList
														.add(returnActivity2
																.getProperty(
																		"id")
																.toString());

												System.out.println("check "
														+ returnActivity1
																.getProperty(
																		"id")
																.toString()
														+ "\t"
														+ returnActivity2
																.getProperty(
																		"id")
																.toString());

											}
										}
									}
								} catch (Exception ex) {
									System.out.println(ex.toString());
								}
							}
						}
					}
				}
			}

			System.out.println("List Display " + firstActivityNewList
					+ secondActivityNewList);
		} while (!firstActivityNewList.isEmpty()
				&& !secondActivityNewList.isEmpty());
		System.out.println("Comparing Entities :" + lastEntityId1 + "\t"
				+ lastEntityId2);
		if (!(lastEntityId1.equals(lastEntityId2))) {
			System.out.println("Difference spotted");
			divergingNodes = new ArrayList<String>();
			divergingNodes.add(lastEntityId1);
			divergingNodes.add(lastEntityId2);
			divergingNodeLists.add(divergingNodes);

		}
	}

	private static boolean isActivityZipped(String firstActivity,
			String secondActivity) {
		String initial = firstActivity.substring(0, 11);
		boolean isZipped = false;
		String[] firstsplit = firstActivity.split(initial+"/invocation/"
				+ InvocationId1 + "/block/");
		String[] secondsplit = secondActivity.split(initial+"/invocation/"
				+ InvocationId2 + "/block/");

		if (firstsplit[1].toString().contains(secondsplit[1].toString())) {
			isZipped = true;
		}
		return isZipped;
	}

	private static boolean IsZipped(String EntityAttributes1,
			String EntityAttributes2) {
		boolean ifZipped = false;
		if (EntityAttributes1.contains("tr")
				&& EntityAttributes2.contains("tr")) {
			System.out.println("Inside this if");
			String[] check1String = EntityAttributes1.toString().split("-");

			String[] check2String = EntityAttributes2.toString().split("-");
			String Entity1 = EntityAttributes1.toString()
					.substring(EntityAttributes1.indexOf(check1String[2]))
					.toString();
			String Entity2 = EntityAttributes2.toString()
					.substring(EntityAttributes2.indexOf(check2String[2]))
					.toString();
			if (Entity1.equals(Entity2)) {
				ifZipped = true;
			}
		} else {
			if (EntityAttributes1.equals(EntityAttributes2)) {
				ifZipped = true;
			}
		}
		return ifZipped;
	}

	private static boolean compare(String EntityAttributes1,
			String EntityAttributes2, String firstEntityId,
			String secondEntityId) {

		boolean compare = false;
		String checkString1 = "'esc:hashvalue'";
		if (firstEntityId.contains("tr") && secondEntityId.contains("tr")) {
			if (EntityAttributes1.equals(EntityAttributes2))
				compare = true;
			else if (EntityAttributes1.contains(checkString1)
					&& EntityAttributes2.contains(checkString1)) {
				compare = checkFurtherEntityEqual(EntityAttributes1,
						EntityAttributes2);
			}
		} else {
			if (EntityAttributes1.equals(EntityAttributes1)) {
				compare = checkFirstEntityEqual(EntityAttributes1,
						EntityAttributes2);
			}
		}

		return compare;
	}

	private static boolean checkFirstEntityEqual(String EntityAttributes1,
			String EntityAttributes2) {
		boolean compare = false;
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
				compare = true;
			} else {
				System.out.println("Difference spotted");
				compare = false;
			}
		}
		return compare;

	}

	private static boolean checkFurtherEntityEqual(String EntityAttributes1,
			String EntityAttributes2) {
		boolean compare = false;
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
				compare = true;
			} else {
				System.out.println("Difference spotted");
				compare = false;
			}
		}
		return compare;
	}

	private static ResourceIterable<Node> getWasGeneratedBy(Node Activity) {
		ResourceIterable<Node> entityNodeList = null;
		TraversalDescription entity1 = graphDB
				.traversalDescription()
				.breadthFirst()
				.relationships(
						DynamicRelationshipType.withName("wasGeneratedBy"))
				.evaluator(Evaluators.atDepth(1));
		org.neo4j.graphdb.traversal.Traverser traverser1 = entity1
				.traverse(Activity);
		entityNodeList = traverser1.nodes();
		return entityNodeList;
	}

	private static ResourceIterable<Node> getUsed(Node Entity) {
		ResourceIterable<Node> activityNodeList = null;
		TraversalDescription entity1 = graphDB.traversalDescription()
				.breadthFirst()
				.relationships(DynamicRelationshipType.withName("used"))
				.evaluator(Evaluators.atDepth(1));
		org.neo4j.graphdb.traversal.Traverser traverser1 = entity1
				.traverse(Entity);
		activityNodeList = traverser1.nodes();
		return activityNodeList;
	}

	private static List<String> findFirstActivity(String string1) {
		List<String> ActivityIds = null;
		try {

			ExecutionEngine execEngine = new ExecutionEngine(graphDB);
			ExecutionResult getActivity1 = execEngine
					.execute("MATCH (n:entity)<-[r:used]-(p:activity) WHERE p.id =~ \".*"
							+ string1
							+ ".*\" MATCH (n) WHERE NOT (n)-[:wasGeneratedBy]->(:activity)"
							+ " MATCH (p)-[r1:used]->(:entity) with n,p,count(r1) as cnt WHERE cnt=1"
							+ " return p.id as firstActivity, n.id as firstEntity");

			ResourceIterator<Object> firstActivity = getActivity1
					.columnAs("firstActivity");
			ActivityIds = new ArrayList<String>();

			while (firstActivity.hasNext()) {
				ActivityIds.add(firstActivity.next().toString());
			}
		} catch (Exception ex) {

		}
		return ActivityIds;
	}
}
