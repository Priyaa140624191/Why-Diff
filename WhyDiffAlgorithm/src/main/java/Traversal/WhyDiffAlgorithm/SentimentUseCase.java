package Traversal.WhyDiffAlgorithm;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

import org.neo4j.cypher.internal.compiler.v2_0.functions.Substring;
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
public class SentimentUseCase {
	static GraphDatabaseService graphDB = new GraphDatabaseFactory()
			.newEmbeddedDatabase("Sentiment10");

	static int inputCount = 0;

	static String InvocationId1 = "627072";
	static String InvocationId2 = "627065";

	static List<List<String>> divergingNodeLists = new ArrayList<List<String>>();
	static List<String> divergingNodes = new ArrayList<String>();

	static List<String> firstActivityList = null;
	static List<String> secondActivityList = null;

	static List<String> firstActivityNewList = new ArrayList<String>();
	static List<String> secondActivityNewList = new ArrayList<String>();

	static boolean first_hop = true;

	static String lastEntityId1 = null;
	static String lastEntityId2 = null;

	static List<String> ZippedKeepTrack = new ArrayList<String>();

	static int totalEntityNodes = 0;

	static int checkNonIso = 0;

	private static ResourceIterable<Node> firstActivityRemainingList;
	private static ResourceIterable<Node> secondActivityRemainingList;

	private static List<String> firstExtra = new ArrayList<String>();
	private static List<String> secondExtra = new ArrayList<String>();

	private static char format;

	/*************** GRAPH VISUALISATION **************/

	private static List<String> first = new ArrayList<String>();
	private static List<String> second = new ArrayList<String>();

	private static List<String> firstEnt = new ArrayList<String>();
	private static List<String> secondEnt = new ArrayList<String>();

	private static List<String> firstAct = new ArrayList<String>();
	private static List<String> secondAct = new ArrayList<String>();

	private static List<String> firstAll = new ArrayList<String>();
	private static List<String> secondAll = new ArrayList<String>();

	private static List<String> firstGraph = new ArrayList<String>();
	private static List<String> secondGraph = new ArrayList<String>();

	private static int countEntity1 = 0;
	private static int countEntity2 = 0;

	private static int countActivity1 = 0;
	private static int countActivity2 = 0;

	private static int previousNodeCountUsed1 = 0;
	private static int previousNodeCountUsed2 = 0;
	private static int previousNodeCountGen1 = 0;
	private static int previousNodeCountGen2 = 0;

	private static List<String> deltaList = new ArrayList<String>();

	private static List<String> deltaActList = new ArrayList<String>();

	private static List<String> divergingActList = new ArrayList<String>();

	private static ArrayList<String> deltaEntList = new ArrayList<String>();

	private static String original = "";

	private static String edittedStringAct;

	private static String edittedStringEnt;

	/*************** GRAPH VISUALISATION **************/

	public static void main(String[] args) {

		try (Transaction tx = graphDB.beginTx()) {

			boolean visited = false;

			compareFirstEntity(InvocationId1, InvocationId2);
			Traverse(InvocationId1, InvocationId2);
			compareLastEntity(InvocationId1, InvocationId2);

			/********
			 * ReSyncing
			 * 
			 */
			reSyncing();

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

			System.out.println("Size Checking " + firstAll.size() + "\t"
					+ secondAll.size());

			makeFirstGraph(InvocationId1);
			makeSecondGraph(InvocationId2);

			makeDeltaGraph();
			/*
			 * Proba p1 = new Proba(); p1.makeGraph(firstGraph, secondGraph);
			 */

			System.out.println(firstGraph.toString());
			System.out.println(first.toString());
			System.out.println(secondGraph.toString());
			System.out.println(second.toString());

			System.out
					.println("---------------------------------------------------");
			System.out.println(firstEnt.toString());
			System.out.println(secondEnt.toString());
			System.out.println(firstAct.toString());
			System.out.println(secondAct.toString());
			System.out.println(firstAll.toString());
			System.out.println(secondAll.toString());

			System.out
					.println("---------------------------------------------------");

			Proba p = new Proba();

			p.makeGraph(firstGraph, firstEnt, firstAct, secondGraph, secondEnt,
					secondAct);

			if (first.size() < second.size())
				p.makeDelta(deltaList, deltaActList, deltaEntList,
						"Nodes_Inserted");
			else
				p.makeDelta(deltaList, deltaActList, deltaEntList,
						"Nodes_Deleted");

			TryJS d = new TryJS();
			d.display();

		} catch (Exception ex) {
			System.out.println(ex.toString());
		}
	}

	private static void makeDeltaGraph() {

		deltaList = new ArrayList<String>();

		deltaList.addAll(firstGraph);
		deltaList.addAll(secondGraph);

		deltaActList = new ArrayList<String>();
		deltaActList.addAll(firstAct);
		deltaActList.addAll(secondAct);

		/*for (int i = 0; i < deltaActList.size(); i++)
		{
			String original = deltaActList.get(i).toString();
			String newString = deltaActList.get(i).toString() + "[shape=box][style=filled][fillcolor = mediumspringgreen]";
			deltaActList.get(i).replace(original, original);
		}*/
		deltaEntList = new ArrayList<String>();
		deltaEntList.addAll(firstEnt);
		deltaEntList.addAll(secondEnt);

		/*for (int i = 0; i < deltaEntList.size(); i++)
		{
			String original = deltaEntList.get(i).toString();
			String newString = deltaEntList.get(i).toString() + "[style=filled][fillcolor = pink]";
			deltaEntList.get(i).replace(original, original);
		}*/
		
		deltaList.remove(firstGraph.get(firstGraph.size() - 1));
		deltaList.remove(secondGraph.get(secondGraph.size() - 1));

		deltaEntList.remove(firstEnt.get(firstEnt.size() - 1));
		deltaEntList.remove(secondEnt.get(secondEnt.size() - 1));

		if (first.size() < second.size()) {
			swapTwoList(firstGraph, secondGraph);
			swapTwoList(first, second);
			// swapTwoList(firstAct,secondAct);
			// swapTwoList(firstEnt,secondEnt);
		}
		/*
		 * if (first.size() < second.size()) {
		 * 
		 * deltaList.add(secondAct.get(secondAct.size() - 1) + "->" +
		 * firstEnt.get(firstEnt.size() - 1) + "_" +
		 * secondEnt.get(secondEnt.size() - 1)); } else {
		 */
		deltaList.add(firstAct.get(firstAct.size() - 1) + "->"
				+ firstEnt.get(firstEnt.size() - 1) + "\u2261"
				+ secondEnt.get(secondEnt.size() - 1));

		deltaList.add(secondAct.get(secondAct.size() - 1) + "->"
				+ firstEnt.get(firstEnt.size() - 1) + "\u2261"
				+ secondEnt.get(secondEnt.size() - 1));
		// }

		if (divergingNodeLists.toString().contains(
				first.get(first.size() - 1).toString())
				&& divergingNodeLists.toString().contains(
						second.get(second.size() - 1).toString()))
			deltaEntList
					.add(firstEnt.get(firstEnt.size() - 1)
							+ "\u2261"
							+ secondEnt.get(secondEnt.size() - 1)
							+ "[peripheries=2, style=filled, fillcolor = pink, color=orangered]");
		else
			deltaEntList.add(firstEnt.get(firstEnt.size() - 1) + "\u2261"
					+ secondEnt.get(secondEnt.size() - 1)
					+ "[style=filled, fillcolor = pink");

		int length = (first.size() >= second.size()) ? first.size() : second
				.size();
		System.out.println("Length " + length);
		for (int i = 0; i < first.size(); i++) {
			for (int j = 0; j < second.size(); j++) {
				if (first.get(i).toString().contains("invocation")
						&& second.get(j).toString().contains("invocation")) {

					if (isActivityZipped1(first.get(i).toString(), second
							.get(j).toString())
							&& firstAll.get(i).contains("a")
							&& secondAll.get(j).contains("a")) {

						for (int k = 0; k < deltaList.size(); k++) {

							if (deltaList.get(k).contains(firstAll.get(i))
									|| deltaList.get(k).contains(
											secondAll.get(j))) {

								String originalString = deltaList.get(k)
										.toString();
								edittedStringAct = firstAll.get(i).toString();
								edittedStringAct = firstAll.get(i).toString()
										+ "\u2261"
										+ secondAll.get(j).toString();

								/*
								 * if (!deltaList.toString().contains(
								 * originalString.replace(firstAll.get(i)
								 * .toString(), edittedString))
								 */
								deltaList.remove(k);
								if (!originalString.contains(secondAll.get(j)
										.toString())) {
									if (!deltaList.toString().contains(
											originalString.replace(firstAll
													.get(i).toString(),
													edittedStringAct))) {
										// deltaList.remove(k);
										deltaList.add(k, originalString
												.replace(firstAll.get(i)
														.toString(),
														edittedStringAct));
									}
								}
								if (!originalString.contains(firstAll.get(i)
										.toString())) {
									System.out.println("Original "
											+ originalString);
									if (!deltaList.toString().equals(
											originalString.replace(secondAll
													.get(j).toString(),
													edittedStringAct))) {
										System.out.println("Replaced: "
												+ originalString.replace(
														secondAll.get(j)
																.toString(),
														edittedStringAct));
										deltaList.add(k, originalString
												.replace(secondAll.get(j)
														.toString(),
														edittedStringAct));
									}
								}
								
								
								for (int p = 0; p < deltaActList.size(); p++) {
									if (deltaActList.get(p).contains(
											firstAll.get(i))) {
										deltaActList.remove(p);
										if (!deltaActList.toString().contains(
												edittedStringAct)) {

											if (divergingNodeLists
													.toString()
													.contains(
															first.get(i)
																	.toString())
													&& divergingNodeLists
															.toString()
															.contains(
																	second.get(
																			j)
																			.toString()))
											
												deltaActList
														.add(p,
																edittedStringAct
																		+ "[shape=box][peripheries=2,style=filled, fillcolor=mediumspringgreen, color=orangered]");
												
													
											else
												deltaActList
														.add(p,
																edittedStringAct
																		+ "[shape=box][style=filled,fillcolor = mediumspringgreen];");
										}
									}
								}

							}
						}
						
				
					} else {

						for (int k = 0; k < deltaList.size(); k++) {
							if (deltaList.get(k).contains(firstAll.get(i))
									|| deltaList.get(k).contains(
											secondAll.get(j))) {
								String originalString = deltaList.get(k)
										.toString();

								String edittedString = firstAll.get(i)
										.toString();

								deltaList.remove(k);

								if (!deltaList.toString().contains(
										originalString.replace(firstAll.get(i)
												.toString(), edittedString)))
									deltaList.add(k, originalString.replace(
											firstAll.get(i).toString(),
											edittedString));
							}
						}
					}
				} else {
					if (IsZipped1(first.get(i).toString(), second.get(j)
							.toString())) {
						for (int k = 0; k < deltaList.size(); k++) {
							if (deltaList.get(k).contains(firstAll.get(i))
									|| deltaList.get(k).contains(
											secondAll.get(j))) {
								String originalString = deltaList.get(k)
										.toString();
								String edittedStringEnt = firstAll.get(i)
										.toString()
										+ "\u2261"
										+ secondAll.get(j).toString();
								deltaList.remove(k);
								if (!originalString.contains(secondAll.get(j)
										.toString())) {

									if (!deltaList.toString().contains(
											originalString.replace(firstAll
													.get(i).toString(),
													edittedStringEnt))) {
										deltaList.add(k, originalString
												.replace(firstAll.get(i)
														.toString(),
														edittedStringEnt));
									}
								}
								if (!originalString.contains(firstAll.get(i)
										.toString())) {
									if (!deltaList.toString().contains(
											originalString.replace(secondAll
													.get(j).toString(),
													edittedStringEnt))) {
										deltaList.add(k, originalString
												.replace(secondAll.get(j)
														.toString(),
														edittedStringEnt));
									}
								}
								for (int p = 0; p < deltaEntList.size(); p++) {
									if (deltaEntList.get(p).contains(
											firstAll.get(i))) {
										deltaEntList.remove(p);
										if (!deltaEntList.toString().contains(
												edittedStringEnt)) {
											if (divergingNodeLists
													.toString()
													.contains(
															first.get(i)
																	.toString())
													&& divergingNodeLists
															.toString()
															.contains(
																	second.get(
																			j)
																			.toString()))
												deltaEntList
														.add(p,
																edittedStringEnt
																		+ "[peripheries=2, style=filled, fillcolor = pink, color=orangered]");
											else
												deltaEntList
														.add(p,
																edittedStringEnt
																		+ "[style=filled,fillcolor = pink];");
										}
									}
								}
							}
						}
					}

					else {

						for (int k = 0; k < deltaList.size(); k++) {
							if (deltaList.get(k).contains(firstAll.get(i))
									|| deltaList.get(k).contains(
											secondAll.get(j))) {

								String originalString = deltaList.get(k)
										.toString();

								String edittedString = firstAll.get(i)
										.toString();

								deltaList.remove(k);

								if (!deltaList.toString().contains(
										originalString.replace(firstAll.get(i)
												.toString(), edittedString)))
									deltaList.add(k, originalString.replace(
											firstAll.get(i).toString(),
											edittedString));
							}
						}
					}
				}
			}
		}

		for (int i = 0; i < deltaActList.size(); i++) {
			for (int j = 0; j < secondAct.size(); j++) {
				if (secondAct.get(j).toString()
						.equals(deltaActList.get(i).toString()))
					deltaActList.remove(i);
			}
		}

		for (int i = 0; i < deltaEntList.size(); i++) {
			for (int j = 0; j < secondEnt.size(); j++) {
				if (secondEnt.get(j).toString()
						.equals(deltaEntList.get(i).toString()))
					deltaEntList.remove(i);
			}
		}
		if (!deltaList.get(deltaList.size() - 1).contains(
				edittedStringAct + "->" + firstEnt.get(firstEnt.size() - 1)
						+ "\u2261" + secondEnt.get(secondEnt.size() - 1)))
			deltaList.add(edittedStringAct + "->"
					+ firstEnt.get(firstEnt.size() - 1) + "\u2261"
					+ secondEnt.get(secondEnt.size() - 1));

		Set<String> hs = new HashSet<>();
		hs.addAll(deltaList);
		deltaList.clear();
		deltaList.addAll(hs);
	}

	private static void swapTwoList(List<String> firstGraph2,
			List<String> secondGraph2) {
		List<String> tmpList = new ArrayList<String>();
		firstGraph2 = secondGraph2;
		secondGraph2 = tmpList;
	}

	private static void reSyncing() {
		for (int i = 0; i < firstExtra.size(); i++) {

			for (int j = 0; j < secondExtra.size(); j++) {

				if (firstExtra.get(i).toString().contains("invocation")
						&& secondExtra.get(j).toString().contains("invocation")
						&& isActivityZipped(firstExtra.get(i).toString(),
								secondExtra.get(j).toString())) {

					Node actNode1 = graphDB.findNode(NodeType1.activity, "id",
							firstExtra.get(i).toString());
					Node actNode2 = graphDB.findNode(NodeType1.activity, "id",
							secondExtra.get(j).toString());
					for (int k = 0; k < divergingNodeLists.size(); k++) {
						if (divergingNodeLists.get(k).toString()
								.contains(firstExtra.get(i).toString())) {
							divergingNodeLists
									.remove(divergingNodeLists.get(k));
						}
						if (divergingNodeLists.get(k).toString()
								.contains(secondExtra.get(j).toString())) {
							divergingNodeLists
									.remove(divergingNodeLists.get(k));
						}
					}
					if (checkActivityEqual(actNode1.getProperty("properties")
							.toString(), actNode2.getProperty("properties")
							.toString())) {
					} else {
						divergingNodes = new ArrayList<String>();
						divergingNodes.add(firstExtra.get(i).toString());
						divergingNodes.add(secondExtra.get(j).toString());
						divergingNodeLists.add(divergingNodes);
					}
				}

				if (!firstExtra.get(i).toString().contains("invocation")
						&& !secondExtra.get(j).toString()
								.contains("invocation")
						&& IsZipped(firstExtra.get(i).toString(), secondExtra
								.get(j).toString())) {
					System.out.println("Entities Zipped");
					Node entNode1 = graphDB.findNode(NodeType1.entity, "id",
							firstExtra.get(i).toString());
					Node entNode2 = graphDB.findNode(NodeType1.entity, "id",
							secondExtra.get(j).toString());
					for (int k = 0; k < divergingNodeLists.size(); k++) {
						if (divergingNodeLists.get(k).toString()
								.contains(firstExtra.get(i).toString())) {
							divergingNodeLists
									.remove(divergingNodeLists.get(k));
						}
						if (divergingNodeLists.get(k).toString()
								.contains(secondExtra.get(j).toString())) {
							divergingNodeLists
									.remove(divergingNodeLists.get(k));
						}
					}
					if (checkFurtherEntityEqual(
							entNode1.getProperty("attributes").toString(),
							entNode2.getProperty("attributes").toString())) {
						System.out.println("Entities Matched");
					} else {
						divergingNodes = new ArrayList<String>();
						divergingNodes.add(firstExtra.get(i).toString());
						divergingNodes.add(secondExtra.get(j).toString());
						divergingNodeLists.add(divergingNodes);
					}
				}

			}
		}

	}

	private static void compareFirstEntity(String invocationId1,
			String invocationId2) {
		ExecutionEngine execEngine = new ExecutionEngine(graphDB);
		ExecutionResult getEntity1 = execEngine
				.execute("MATCH (n:entity)<-[r:used]-(p:activity) WHERE p.id =~ \".*"
						+ invocationId1
						+ ".*\" MATCH (n) WHERE NOT (n)-[:wasGeneratedBy]->(:activity)"
						+ " MATCH (p)-[r1:used]->(:entity) with n,p"
						+ " return n as firstEntity1 ORDER BY id(n)");

		ExecutionResult getEntity2 = execEngine
				.execute("MATCH (n:entity)<-[r:used]-(p:activity) WHERE p.id =~ \".*"
						+ invocationId2
						+ ".*\" MATCH (n) WHERE NOT (n)-[:wasGeneratedBy]->(:activity)"
						+ " MATCH (p)-[r1:used]->(:entity) with n,p "
						+ " return n as firstEntity2 ORDER BY id(n)");
		for (Map<String, Object> row1 : getEntity1) {
			for (Map<String, Object> row2 : getEntity2) {

				Node firstEntity1 = (Node) row1.get("firstEntity1");
				Node firstEntity2 = (Node) row2.get("firstEntity2");
				addToGraphViz1(firstEntity1.getProperty("id").toString());
				addToGraphViz2(firstEntity2.getProperty("id").toString());
				if (!firstEntity1.getProperty("id").equals(
						firstEntity2.getProperty("id")))
					System.out.println("Difference spotted");
				break;
			}
		}
	}

	private static void addToGraphViz1(String firstEntity) {
		if (!first.toString().contains(firstEntity)) {
			first.add(firstEntity);
			if (countEntity1 != 0) {
				firstEnt.add("e\u2081" + countEntity1);
				firstAll.add("e\u2081" + countEntity1);
				/*
				 * firstGraph.add(firstAct.get(countActivity1-1) + "<-e" +
				 * countEntity1);
				 */

			} else {
				firstEnt.add("e\u2081" + countEntity1);
				firstAct.add("a\u2081" + countActivity1+"[shape=box]");
				firstAll.add("e\u2081" + countEntity1);
				firstAll.add("a\u2081" + countActivity1);
				firstGraph.add("e\u2081" + countEntity1 + "->"
						+ firstAct.get(countActivity1));
				// ++countActivity1;
			}
			++countEntity1;
		}
	}

	private static void addToGraphViz2(String secondEntity) {
		if (!second.toString().contains(secondEntity)) {
			second.add(secondEntity);
			if (countEntity2 != 0) {
				secondEnt.add("e\u2082" + countEntity2);
				secondAll.add("e\u2082" + countEntity2);
				/*
				 * secondGraph.add(secondAct.get(countActivity2-1) + "<-e" +
				 * countEntity2);
				 */
			} else {
				secondEnt.add("e\u2082" + countEntity2);
				secondAct.add("a\u2082" + countActivity2);
				secondAll.add("e\u2082" + countEntity2);
				secondAll.add("a\u2082" + countActivity2);
				secondGraph.add("e\u2082" + countEntity2 + "->"
						+ secondAct.get(countActivity2));
				// ++countActivity2;
			}
			++countEntity2;
		}
	}

	private static void addActToGraphViz1(String firstActivity) {
		if (!first.toString().contains(firstActivity)) {
			first.add(firstActivity);
			firstAct.add("a\u2081" + countActivity1);
			firstAll.add("a\u2081" + countActivity1);
			/*
			 * firstGraph.add(firstEnt.get(countEntity1 - 1) + "<-a" +
			 * countActivity1);
			 */

		}
	}

	private static void addActToGraphViz2(String firstActivity) {
		if (!second.toString().contains(firstActivity)) {
			second.add(firstActivity);
			secondAct.add("a\u2082" + countActivity2);
			secondAll.add("a\u2082" + countActivity2);
			/*
			 * secondGraph.add(secondEnt.get(countEntity2 - 1) + "<-a" +
			 * countActivity2);
			 */
			// ++countActivity2;
		}
	}

	private static List<String> compareLastEntity(String invocationId1,
			String invocationId2) {
		List<String> EntityIds1 = null;
		try {

			ExecutionEngine execEngine = new ExecutionEngine(graphDB);
			ExecutionResult getEntity1 = execEngine
					.execute("MATCH (n:entity)-[r:wasGeneratedBy]->(p:activity) WHERE p.id =~ \".*"
							+ invocationId1
							+ ".*\" MATCH (n) WHERE NOT (n)<-[:used]-(:activity)"
							+ " MATCH (p)<-[r1:wasGeneratedBy]-(:entity) with n,p,count(r1) as cnt WHERE cnt=1"
							+ " return n as firstEntity1 ORDER BY id(n)");
			ExecutionResult getEntity2 = execEngine
					.execute("MATCH (n:entity)-[r:wasGeneratedBy]->(p:activity) WHERE p.id =~ \".*"
							+ invocationId2
							+ ".*\" MATCH (n) WHERE NOT (n)<-[:used]-(:activity)"
							+ " MATCH (p)<-[r1:wasGeneratedBy]-(:entity) with n,p,count(r1) as cnt WHERE cnt=1"
							+ " return n as firstEntity2 ORDER BY id(n)");
			for (Map<String, Object> row1 : getEntity1) {
				Node firstEntity1 = (Node) row1.get("firstEntity1");
				for (Map<String, Object> row2 : getEntity2) {
					Node firstEntity2 = (Node) row2.get("firstEntity2");
					addToGraphViz1(firstEntity1.getProperty("id").toString());
					addToGraphViz2(firstEntity2.getProperty("id").toString());
					int provLabel1 = firstEntity1.getProperty("attributes")
							.toString().indexOf("'prov:label'");
					int provType1 = firstEntity1.getProperty("attributes")
							.toString().indexOf("'prov:type'");
					int provLabel2 = firstEntity2.getProperty("attributes")
							.toString().indexOf("'prov:label'");
					int provType2 = firstEntity2.getProperty("attributes")
							.toString().indexOf("'prov:type'");
					/*String fileLabel1 = firstEntity1.getProperty("attributes")
							.toString()
							.substring(provLabel1 + 13, provType1 - 6);
					String fileLabel2 = firstEntity2.getProperty("attributes")
							.toString()
							.substring(provLabel2 + 14, provType2 - 6);
					System.out.println("File Label "+fileLabel1);
					System.out.println("File Label "+fileLabel2);*/
					
					 boolean isEqual =
					 firstEntity1.getProperty("id").toString() == firstEntity2
					 .getProperty("id").toString();
					
					/*FileComparison f = new FileComparison();
					boolean isNotEqual = f.fileCompare(fileLabel1, fileLabel2);*/
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
				/*
				 * for (String firstAct : firstActivityList) {
				 * addActToGraphViz1(firstAct); } for (String secondAct :
				 * secondActivityList) { addActToGraphViz2(secondAct); }
				 */
				first_hop = false;
			} else {
				firstActivityList = new ArrayList<String>(firstActivityNewList);
				secondActivityList = new ArrayList<String>(
						secondActivityNewList);
				firstActivityNewList.clear();
				secondActivityNewList.clear();
			}
			for (String firstActivity : firstActivityList) {
				System.out.println(secondActivityList.toString());
				for (String secondActivity : secondActivityList) {
					if (!first.toString().contains(firstActivity.toString()))
						first.add(firstActivity.toString());
					if (!second.toString().contains(secondActivity.toString()))
						second.add(secondActivity.toString());
					/*
					 * firstAct.add("a" + countActivity1); secondAct.add("a" +
					 * countActivity2);
					 */
					Node firstActivityNode = graphDB.findNode(
							NodeType1.activity, "id", firstActivity);

					Node SecondActivityNode = graphDB.findNode(
							NodeType1.activity, "id", secondActivity);

					/********** First activity comparison *************/
					/************
					 * 30/10/2017
					 */
					boolean activityzip1 = isActivityZipped(firstActivity,
							secondActivity);
					if (activityzip1 == true) {
						boolean isEqualact1 = checkActivityEqual(firstActivityNode.getProperty("properties").toString(),
								SecondActivityNode.getProperty("properties").toString());
						boolean addedact1 = divergingNodeLists.toString()
								.contains(firstActivity.toString());
						if (!isEqualact1 && !addedact1) {
							divergingNodes = new ArrayList<String>();
							divergingNodes.add(firstActivity);
							divergingNodes.add(secondActivity);
							divergingNodeLists.add(divergingNodes);
						}
					}
					/****************/

					ResourceIterable<Node> returnedEntity1List = getWasGeneratedBy(firstActivityNode);
					ResourceIterable<Node> returnedEntity2List = getWasGeneratedBy(SecondActivityNode);

					/*
					 * for (Node item : returnedEntity1List) { ++count; }
					 * System.out.println("Count " + count); count = 0;
					 */

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
								boolean isEqual1 = compare(
										returned1Entity.getProperty(
												"attributes").toString(),
										returned2Entity.getProperty(
												"attributes").toString(),
										returned1Entity.getProperty("id")
												.toString(), returned2Entity
												.getProperty("id").toString());
								boolean added1 = divergingNodeLists.toString()
										.contains(
												returned1Entity.getProperty(
														"id").toString());
								if (isEqual1) {
									ZippedKeepTrack.add(returned1Entity
											.getProperty("id").toString());
									ZippedKeepTrack.add(returned2Entity
											.getProperty("id").toString());
								}
								if ((!isEqual1) && (!added1)) {
									divergingNodes = new ArrayList<String>();
									divergingNodes.add(returned1Entity
											.getProperty("id").toString());
									divergingNodes.add(returned2Entity
											.getProperty("id").toString());
									divergingNodeLists.add(divergingNodes);
								}

							}

							try {
								ResourceIterable<Node> returned1ActivityList = getUsed(returned1Entity);
								ResourceIterable<Node> returned2ActivityList = getUsed(returned2Entity);
								if (returned1ActivityList == null
										&& returned2ActivityList == null)
									System.out.println("Activities null");

								if (returned1ActivityList.iterator().hasNext()
										&& returned2ActivityList.iterator()
												.hasNext()) {
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
												boolean isEqual = checkActivityEqual(
														returnActivity1
																.getProperty(
																		"properties")
																.toString(),
														returnActivity2
																.getProperty(
																		"properties")
																.toString());
												boolean added = divergingNodeLists
														.toString()
														.contains(
																returnActivity1
																		.getProperty(
																				"id")
																		.toString());
												if (isEqual) {
													ZippedKeepTrack
															.add(returnActivity1
																	.getProperty(
																			"id")
																	.toString());
													ZippedKeepTrack
															.add(returnActivity2
																	.getProperty(
																			"id")
																	.toString());
												}
												if (!isEqual && !added) {
													divergingNodes = new ArrayList<String>();
													divergingNodes
															.add(returnActivity1
																	.getProperty(
																			"id")
																	.toString());
													divergingNodes
															.add(returnActivity2
																	.getProperty(
																			"id")
																	.toString());
													divergingNodeLists
															.add(divergingNodes);
												}
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
											}
										}
									}
									/************************************
									 * Added on 31/10/2017 Checks for extra
									 * activities in Workflow Graph1
									 */
								}
								if (returned1ActivityList.iterator().hasNext()) {
									firstActivityRemainingList = returned1ActivityList;
									addRemainingNodes(firstActivityRemainingList);
								}
								if (returned2ActivityList.iterator().hasNext()) {
									secondActivityRemainingList = returned2ActivityList;
									addRemainingNodes1(secondActivityRemainingList);
								}
							} catch (Exception ex) {
								System.out.println(ex.toString());
							}

						}
					}
				}
			}

			System.out.println("List Display " + firstActivityNewList
					+ secondActivityNewList);
		} while (!firstActivityNewList.isEmpty()
				&& !secondActivityNewList.isEmpty());
		checkNonIso = 0;
		if (!(lastEntityId1.contains(lastEntityId2)))
			System.out.println("Difference spotted");
	}

	private static void addRemainingNodes(ResourceIterable<Node> RemainingList) {
		do {
			for (Node returnActivity : RemainingList) {
				firstnodes(returnActivity);
				ResourceIterable<Node> RemainingEntityList = getWasGeneratedBy(returnActivity);
				for (Node returnEntity : RemainingEntityList) {
					firstnodes(returnEntity);
					RemainingList = getUsed(returnEntity);
				}
			}
		} while (RemainingList.iterator().hasNext());
	}

	private static void addRemainingNodes1(ResourceIterable<Node> RemainingList) {
		do {
			for (Node returnActivity : RemainingList) {
				secondnodes(returnActivity);
				ResourceIterable<Node> RemainingEntityList = getWasGeneratedBy(returnActivity);
				for (Node returnEntity : RemainingEntityList) {
					secondnodes(returnEntity);
					RemainingList = getUsed(returnEntity);
				}
			}
		} while (RemainingList.iterator().hasNext());
	}

	private static void firstnodes(Node returned) {
		if (!divergingNodeLists.toString().contains(
				returned.getProperty("id").toString())
				&& returned.getProperty("id").toString()
						.contains(InvocationId1)) {

			divergingNodes = new ArrayList<String>();
			divergingNodes.add(returned.getProperty("id").toString());
			divergingNodes.add("**************ABSENT**************");
			divergingNodeLists.add(divergingNodes);
			

			firstExtra.add(returned.getProperty("id").toString());
		}
	}

	private static void secondnodes(Node returned) {
		if (!divergingNodeLists.toString().contains(
				returned.getProperty("id").toString())
				&& returned.getProperty("id").toString()
						.contains(InvocationId2)) {

			divergingNodes = new ArrayList<String>();
			divergingNodes.add("**************ABSENT**************");
			divergingNodes.add(returned.getProperty("id").toString());
			divergingNodeLists.add(divergingNodes);

			secondExtra.add(returned.getProperty("id").toString());
		}
	}

	private static boolean checkActivityEqual(String ActivityAttributes1,
			String ActivityAttributes2) {
		boolean compare = false;
		// TODO Auto-generated method stub
		System.out.println("Check act properties "+ActivityAttributes1+" "+ActivityAttributes2);
		try {


			if (ActivityAttributes1.equals(ActivityAttributes2)) {
					System.out.println("Both activities are equal ");
					compare = true;
				} else {
					System.out.println("Difference Spotted ");
				}
		}catch (Exception ex) {
			System.out.println(ex.toString());
		}
		return compare;
	}

	private static boolean isActivityZipped(String firstActivity,
			String secondActivity) {
		String initial = firstActivity.substring(0, 13);
		boolean isZipped = false;

		String[] firstsplit = firstActivity.split(initial + "/invocation/"
				+ InvocationId1 + "/block/");
		String[] secondsplit = secondActivity.split(initial + "/invocation/"
				+ InvocationId2 + "/block/");
		if (firstsplit[1].toString().contains(secondsplit[1].toString())) {
			isZipped = true;
		}
		if (isZipped != true
				&& !divergingNodeLists.toString().contains(firstActivity)
				&& !ZippedKeepTrack.toString().contains(firstActivity)) {
			divergingNodes = new ArrayList<String>();
			divergingNodes.add(firstActivity);
			divergingNodes.add("**************ABSENT**************");
			divergingNodeLists.add(divergingNodes);

			firstExtra.add(firstActivity);
		}
		if (isZipped != true
				&& !divergingNodeLists.toString().contains(secondActivity)
				&& !ZippedKeepTrack.toString().contains(secondActivity)) {
			divergingNodes = new ArrayList<String>();
			divergingNodes.add("**************ABSENT**************");
			divergingNodes.add(secondActivity);
			divergingNodeLists.add(divergingNodes);

			secondExtra.add(secondActivity);
		}
		return isZipped;
	}

	private static boolean isActivityZipped1(String firstActivity,
			String secondActivity) {
		String initial = firstActivity.substring(0, 13);
		boolean isZipped = false;

		String[] firstsplit = firstActivity.split(initial + "/invocation/"
				+ InvocationId1 + "/block/");
		String[] secondsplit = secondActivity.split(initial + "/invocation/"
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
			String[] check1String = EntityAttributes1.toString().split("-");
			String[] check2String = EntityAttributes2.toString().split("-");
			String Entity1 = EntityAttributes1.toString()
					.substring(EntityAttributes1.indexOf(check1String[6]))
					.toString();
			String Entity2 = EntityAttributes2.toString()
					.substring(EntityAttributes2.indexOf(check2String[6]))
					.toString();
			System.out.println("Entity1 " + Entity1 + "Entity 2 " + Entity2);
			if (Entity1.equals(Entity2)) {
				ifZipped = true;

				/****
				 * To remove duplicate comparison
				 */
				for (int i = 0; i < divergingNodeLists.size(); i++) {
					if (divergingNodeLists.get(i).toString().contains(Entity1)) {
						divergingNodeLists.remove(divergingNodeLists.get(i));
					} else if (divergingNodeLists.get(i).toString()
							.contains(Entity2)) {
						divergingNodeLists.remove(divergingNodeLists.get(i));
					}
				}
				/****
				 * To remove duplicate comparison
				 */

			} else if (!divergingNodeLists.toString().contains(Entity1)
					&& Entity1 != null) {
				divergingNodes = new ArrayList<String>();
				divergingNodes.add(EntityAttributes1);
				divergingNodes.add("**************ABSENT**************");
				divergingNodeLists.add(divergingNodes);
			} else if (!divergingNodeLists.toString().contains(Entity2)
					&& Entity2 != null) {
				divergingNodes = new ArrayList<String>();
				divergingNodes.add("**************ABSENT**************");
				divergingNodes.add(EntityAttributes2);
				divergingNodeLists.add(divergingNodes);
			}
			System.out.println("Check Entity Attributes " + EntityAttributes1
					+ "Entity Attributes2 " + EntityAttributes2);
		} else {
			if (!divergingNodeLists.toString().contains(EntityAttributes1)
					&& !EntityAttributes1.contains("esc")) {
				divergingNodes = new ArrayList<String>();
				divergingNodes.add(EntityAttributes1);
				divergingNodes.add("**************ABSENT**************");
				divergingNodeLists.add(divergingNodes);
			}
			checkNonIso = 1;
			if (EntityAttributes1.equals(EntityAttributes2)) {
				ifZipped = true;
			}
		}
		return ifZipped;
	}

	private static boolean IsZipped1(String EntityAttributes1,
			String EntityAttributes2) {
		boolean ifZipped = false;
		if (EntityAttributes1.contains("tr")
				&& EntityAttributes2.contains("tr")) {
			String[] check1String = EntityAttributes1.toString().split("-");
			String[] check2String = EntityAttributes2.toString().split("-");
			String Entity1 = EntityAttributes1.toString()
					.substring(EntityAttributes1.indexOf(check1String[6]))
					.toString();
			String Entity2 = EntityAttributes2.toString()
					.substring(EntityAttributes2.indexOf(check2String[6]))
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
		if (EntityAttributes1.contains(checkString1)
				&& EntityAttributes2.contains(checkString1)) {
			compare = checkFurtherEntityEqual(EntityAttributes1,
					EntityAttributes2);
		} else {

			compare = checkFirstEntityEqual(EntityAttributes1,
					EntityAttributes2);
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
			System.out.println("Check for sure " + EntityAttributes1 + "\t"
					+ EntityAttributes2);
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
		for (Node item : entityNodeList) {
			if (item.getProperty("id").toString().contains(InvocationId1)) {
				addToGraphViz1(item.getProperty("id").toString());

			} else if (item.getProperty("id").toString()
					.contains(InvocationId2)) {
				addToGraphViz2(item.getProperty("id").toString());
			}
		}
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

		for (Node item : activityNodeList) {

			if (item.getProperty("id").toString().contains(InvocationId1)) {

				/********** Added on 4/01/2018 *******/
				previousNodeCountUsed1++;
				System.out.println(item.getProperty("id").toString());
				/********** Added on 4/01/2018 *******/

				if (!(first.toString().contains(item.getProperty("id")
						.toString()))) {
					countActivity1++;
					addActToGraphViz1(item.getProperty("id").toString());
				}
				// formatCharacters(countActivity1);
				if (!firstGraph.contains(firstEnt.get(countEntity1 - 1)
						+ "->a\u2081" + countActivity1))
					firstGraph.add(firstEnt.get(countEntity1 - 1) + "->a\u2081"
							+ countActivity1);
				System.out.println(firstGraph.toString());
			} else {
				/********** Added on 4/01/2018 *******/
				previousNodeCountUsed2++;
				System.out.println("previousNodeCountUsed: invocation 2: "
						+ previousNodeCountGen2);
				System.out.println("count ent : " + countEntity2);
				System.out.println(item.getProperty("id").toString());
				/********** Added on 4/01/2018 *******/

				if (!(second.toString().contains(item.getProperty("id")
						.toString()))) {
					countActivity2++;
					addActToGraphViz2(item.getProperty("id").toString());
				}
				if (!secondGraph.contains(secondEnt.get(countEntity2 - 1)
						+ "->a\u2082" + countActivity2))
					secondGraph.add(secondEnt.get(countEntity2 - 1)
							+ "->a\u2082" + countActivity2);
				System.out.println(secondGraph.toString());
			}
		}
		return activityNodeList;
	}

	/*
	 * private static void formatCharacters(int countActivity) {
	 * if(countActivity == 1) format = "U+00B9"; else if(countActivity == 2)
	 * format = "\u00B2"; else if(countActivity == 3) format = "\u00B3"; else
	 * format = (char)(0x966+countActivity); }
	 */

	private static List<String> findFirstActivity(String string1) {
		List<String> ActivityIds = null;
		try {

			ExecutionEngine execEngine = new ExecutionEngine(graphDB);
			ExecutionResult getActivity1 = execEngine
					.execute("MATCH (n:entity)<-[r:used]-(p:activity) WHERE p.id =~ \".*"
							+ string1
							+ ".*\" MATCH (n) WHERE NOT (n)-[:wasGeneratedBy]->(:activity)"
							+ " MATCH (p)-[r1:used]->(:entity) with n,p,count(r1) as cnt WHERE cnt=1"
							+ " return p.id as firstActivity");

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

	private static int findEntityCount(String string1) {

		ExecutionEngine execEngine = new ExecutionEngine(graphDB);

		ExecutionResult getInputCount = execEngine
				.execute("MATCH (n:entity)<-[r:used]-(p:activity) WHERE p.id =~ \".*"
						+ string1
						+ ".*\"MATCH (n) WHERE NOT (n)-[:wasGeneratedBy]->(:activity)"
						+ "return count(n) as FirstEntityCount");
		ResourceIterator<Object> firstEntityCount = getInputCount
				.columnAs("FirstEntityCount");
		inputCount = inputCount
				+ Integer.parseInt(firstEntityCount.next().toString());

		ExecutionResult getEntityCount = execEngine
				.execute("MATCH (n:entity) WHERE n.id =~ \".*" + string1
						+ ".*\" return count(n) as Count");
		ResourceIterator<Object> EntityCount = getEntityCount.columnAs("Count");
		inputCount = inputCount
				+ Integer.parseInt(EntityCount.next().toString());

		ExecutionResult getOutputCount = execEngine
				.execute("MATCH (n:entity)-[r:wasGeneratedBy]->(p:activity) WHERE p.id =~ \".*"
						+ string1
						+ ".*\"MATCH (n) WHERE NOT (n)<-[:used]-(:activity)"
						+ "return count(n) as lastEntityCount");
		ResourceIterator<Object> lastEntityCount = getOutputCount
				.columnAs("lastEntityCount");
		inputCount = inputCount
				+ Integer.parseInt(lastEntityCount.next().toString());

		return inputCount;
	}

	private static void makeFirstGraph(String invocationId1) {
		// TODO Auto-generated method stub
		ExecutionEngine execEngine = new ExecutionEngine(graphDB);

		ExecutionResult getGraph = execEngine
				.execute("MATCH (a)-[r:wasGeneratedBy]->(b:activity) WHERE b.id =~ \".*"
						+ invocationId1
						+ ".*\"RETURN a,r,b as getGeneratedByRelationship");
		int i = 0;
		int j = 1;
		while (getGraph.hasNext()) {
			if (!firstGraph.contains("a\u2081" + i + "->" + "e\u2081" + j))
				firstGraph.add("a\u2081" + i + "->" + "e\u2081" + j);
			System.out.println("a\u2081" + i + "->" + "e\u2081" + j);
			i++;
			j++;
			getGraph.next();
		}
		/*
		 * while (getRelationCount.hasNext()) { String newString =
		 * getRelationCount.next().toString();
		 * System.out.println("Original and New String " + original + " " +
		 * newString); if (!original.equals(newString)) { i++; } if
		 * (!firstGraph.contains("a\u2081" + (i-1) + "->" + "e\u2081" + j))
		 * firstGraph.add("a\u2081" + (i-1) + "->" + "e\u2081" + j);
		 * System.out.println("a\u2081" + (i-1) + "->" + "e\u2081" + j);
		 * 
		 * 
		 * original = newString; System.out.println(original); //
		 * System.out.println("Get Generated Next "+getRelationCount.next());
		 * j++; // getRelationCount.next(); }
		 */
	}

	private static void makeSecondGraph(String invocationId2) {
		// TODO Auto-generated method stub
		ExecutionEngine execEngine = new ExecutionEngine(graphDB);

		ExecutionResult getGraph = execEngine
				.execute("MATCH (a)-[r:wasGeneratedBy]->(b:activity) WHERE b.id =~ \".*"
						+ invocationId2
						+ ".*\"RETURN a,r,b as getGeneratedByRelationship");
		int i = 0;
		int j = 1;
		while (getGraph.hasNext()) {
			if (!secondGraph.contains("a\u2082" + i + "->" + "e\u2082" + j))
				secondGraph.add("a\u2082" + i + "->" + "e\u2082" + j);
			System.out.println("a\u2082" + i + "->" + "e\u2082" + j);
			i++;
			j++;
			getGraph.next();
		}
	}

}