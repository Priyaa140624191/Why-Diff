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
public class Bt {
	static GraphDatabaseService graphDB = new GraphDatabaseFactory().newEmbeddedDatabase("Complex6");

	static int inputCount = 0;

	static String InvocationId1 = "140120";
	static String InvocationId2 = "140121";

	static List<List<String>> divergingNodeLists = new ArrayList<List<String>>();
	static List<String> divergingNodes = new ArrayList<String>();
	
	static List<List<String>> divergingPropertyLists = new ArrayList<List<String>>();
	static List<String> divergingProperties = new ArrayList<String>();

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

	private static int count;

	private static int count1;

	/*************** GRAPH VISUALISATION **************/

	public static void main(String[] args) {

		try (Transaction tx = graphDB.beginTx()) {

			boolean visited = false;

			compareFirstEntity(InvocationId1, InvocationId2);
			Traverse(InvocationId1, InvocationId2);
			compareLastEntity(InvocationId1, InvocationId2);
			System.out.println("Listing Divergent nodes before sync");
			System.out.println(
					"|-------------------------------------------------------------------------------------------------------------------------------------------------|");
			for (int i = 0; i < divergingNodeLists.size(); i++) {
				for (int j = 0; j < divergingNodes.size(); j++) {
					System.out.print("|" + divergingNodeLists.get(i).get(j) + "|");
				}
				System.out.println("");
			}
			System.out.println(
					"|-------------------------------------------------------------------------------------------------------------------------------------------------|");
			
			System.out.println("Listing Divergent properties before sync");
			System.out.println(
					"|-------------------------------------------------------------------------------------------------------------------------------------------------|");
			for (int i = 0; i < divergingPropertyLists.size(); i++) {
				for (int j = 0; j < divergingProperties.size(); j++) {
					System.out.print("|" + divergingPropertyLists.get(i).get(j) + "|");
				}
				System.out.println("");
			}
			System.out.println(
					"|-------------------------------------------------------------------------------------------------------------------------------------------------|");

			/********
			 * ReSyncing
			 * 
			 */
			reSyncing();

			System.out.println("Listing Divergent nodes after sync");
			System.out.println(
					"|-------------------------------------------------------------------------------------------------------------------------------------------------|");
			for (int i = 0; i < divergingNodeLists.size(); i++) {
				for (int j = 0; j < divergingNodes.size(); j++) {
					System.out.print("|" + divergingNodeLists.get(i).get(j) + "|");
				}
				System.out.println("");
			}
			System.out.println(
					"|-------------------------------------------------------------------------------------------------------------------------------------------------|");

		
			System.out.println("Size Checking " + firstAll.size() + "\t" +
			secondAll.size());

			//makeFirstGraph(InvocationId1);
			//makeSecondGraph(InvocationId2);
			System.out.println("First Graph ");
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

			createFirstGraph();
			createSecondGraph();
			Proba p = new Proba();
			p.makeGraph(firstGraph, firstEnt, firstAct, secondGraph, secondEnt,
					secondAct);

			if (first.size() < second.size())
				p.makeDelta2(deltaList,
						"Nodes_Inserted");
			else
				p.makeDelta2(deltaList,
						"Nodes_Deleted");

			TryJS d = new TryJS();
			d.display();
			
			System.out.println("Zipped count "+count);
			System.out.println("Compare count "+count1);

		} catch (Exception ex) {
			System.out.println(ex.toString());
		}
	}

	private static void createSecondGraph() {
		// TODO Auto-generated method stub
		secondEnt.add("e\u2082"+"0");
		secondEnt.add("e\u2082"+"1");
		//secondEnt.add("e\u2082"+"2");
		secondEnt.add("e\u2082"+"2");
		secondEnt.add("e\u2082"+"3");
		secondEnt.add("e\u2082"+"4");
		secondEnt.add("e\u2082"+"5");
		
		secondAct.add("a\u2082"+"0");
		//secondAct.add("a\u2082"+"1");
		secondAct.add("a\u2082"+"1");
		secondAct.add("a\u2082"+"2");
		secondAct.add("a\u2082"+"3");
		
		secondGraph.add("e\u2082"+"0"+"->"+"a\u2082"+"0");
		secondGraph.add("e\u2082"+"1"+"->"+"a\u2082"+"0");
		
		secondGraph.add("a\u2082"+"0"+"->"+"e\u2082"+"2");
		
		//secondGraph.add("e\u2082"+"2"+"->"+"a\u2082"+"1");
		
		//secondGraph.add("a\u2082"+"1"+"->"+"e\u2082"+"3");
		
		secondGraph.add("e\u2082"+"2"+"->"+"a\u2082"+"1");
		secondGraph.add("e\u2082"+"2"+"->"+"a\u2082"+"2");
		
		secondGraph.add("a\u2082"+"1"+"->"+"e\u2082"+"3");
		secondGraph.add("a\u2082"+"2"+"->"+"e\u2082"+"4");
		
		secondGraph.add("e\u2082"+"3"+"->"+"a\u2082"+"3");
		secondGraph.add("e\u2082"+"4"+"->"+"a\u2082"+"3");
		
		secondGraph.add("a\u2082"+"3"+"->"+"e\u2082"+"5");
		// TODO Auto-generated method stub
		secondEnt.add("e\u2082"+"0");
		secondEnt.add("e\u2082"+"1");
		secondEnt.add("e\u2082"+"2");
		secondEnt.add("e\u2082"+"3");
		secondEnt.add("e\u2082"+"4");
		secondEnt.add("e\u2082"+"5");
		secondEnt.add("e\u2082"+"6");
		
		secondAct.add("a\u2082"+"0");
		secondAct.add("a\u2082"+"1");
		secondAct.add("a\u2082"+"2");
		secondAct.add("a\u2082"+"3");
		secondAct.add("a\u2082"+"4");
		
		secondGraph.add("e\u2082"+"0"+"->"+"a\u2082"+"0");
		secondGraph.add("e\u2082"+"1"+"->"+"a\u2082"+"0");
		
		secondGraph.add("a\u2082"+"0"+"->"+"e\u2082"+"2");
		
		secondGraph.add("e\u2082"+"2"+"->"+"a\u2082"+"1");
		
		secondGraph.add("a\u2082"+"1"+"->"+"e\u2082"+"3");
		
		secondGraph.add("e\u2082"+"3"+"->"+"a\u2082"+"2");
		secondGraph.add("e\u2082"+"3"+"->"+"a\u2082"+"3");
		
		secondGraph.add("a\u2082"+"2"+"->"+"e\u2082"+"4");
		secondGraph.add("a\u2082"+"3"+"->"+"e\u2082"+"5");
		
		secondGraph.add("e\u2082"+"4"+"->"+"a\u2082"+"4");
		secondGraph.add("e\u2082"+"5"+"->"+"a\u2082"+"4");
		
		secondGraph.add("a\u2082"+"4"+"->"+"e\u2082"+"6");
	}

	private static void createFirstGraph() {
		// TODO Auto-generated method stub
		firstEnt.add("e\u2081"+"0");
		firstEnt.add("e\u2081"+"1");
		firstEnt.add("e\u2081"+"2");
		firstEnt.add("e\u2081"+"3");
		firstEnt.add("e\u2081"+"4");
		firstEnt.add("e\u2081"+"5");
		
		firstAct.add("a\u2081"+"0");
		firstAct.add("a\u2081"+"1");
		firstAct.add("a\u2081"+"2");
		firstAct.add("a\u2081"+"3");
		
		firstGraph.add("e\u2081"+"0"+"->"+"a\u2081"+"0");
		firstGraph.add("e\u2081"+"1"+"->"+"a\u2081"+"0");
		
		firstGraph.add("a\u2081"+"0"+"->"+"e\u2081"+"2");
		
		firstGraph.add("e\u2081"+"2"+"->"+"a\u2081"+"1");
		firstGraph.add("e\u2081"+"2"+"->"+"a\u2081"+"3");
		
		firstGraph.add("a\u2081"+"1"+"->"+"e\u2081"+"3");
		firstGraph.add("e\u2081"+"3"+"->"+"a\u2081"+"2");
		
		firstGraph.add("a\u2081"+"2"+"->"+"e\u2081"+"4");
		
		firstGraph.add("e\u2081"+"4"+"->"+"a\u2081"+"3");
		
		firstGraph.add("a\u2081"+"3"+"->"+"e\u2081"+"5");
	}

	private static void makeDeltaGraph() {
		System.out.println("First ent "+firstEnt.toString());
		deltaList = new ArrayList<String>();

		deltaList.addAll(firstGraph);
		deltaList.addAll(secondGraph);
	}

	private static void swapTwoList(List<String> firstGraph2, List<String> secondGraph2) {
		List<String> tmpList = new ArrayList<String>();
		firstGraph2 = secondGraph2;
		secondGraph2 = tmpList;
	}

	private static void reSyncing() {
		System.out.println("Extra node " + firstExtra.toString() + "|" + secondExtra);
		// System.out.println("Zipped list "+ZippedKeepTrack.toString());

		for (int i = 0; i < firstExtra.size(); i++) {

			for (int j = 0; j < secondExtra.size(); j++) {

				if (firstExtra.get(i).toString().contains("invocation")
						&& secondExtra.get(j).toString().contains("invocation")
						&& isActivityZipped(firstExtra.get(i).toString(), secondExtra.get(j).toString(),"","")) {

					Node actNode1 = graphDB.findNode(NodeType1.activity, "id", firstExtra.get(i).toString());
					Node actNode2 = graphDB.findNode(NodeType1.activity, "id", secondExtra.get(j).toString());
					for (int k = 0; k < divergingNodeLists.size(); k++) {
						if (divergingNodeLists.get(k).toString().contains(firstExtra.get(i).toString())) {
							System.out.println("specific " + firstExtra.get(i).toString());
							divergingNodeLists.remove(divergingNodeLists.get(k));
						}
						if (divergingNodeLists.get(k).toString().contains(secondExtra.get(j).toString())) {
							System.out.println("specific " + firstExtra.get(i).toString());
							divergingNodeLists.remove(divergingNodeLists.get(k));
						}
					}
					if (checkActivityEqual(actNode1.getProperty("properties").toString(),
							actNode2.getProperty("properties").toString())) {
					} else {
						divergingNodes = new ArrayList<String>();
						divergingNodes.add(firstExtra.get(i).toString());
						divergingNodes.add(secondExtra.get(j).toString());
						divergingNodeLists.add(divergingNodes);
						
						divergingProperties = new ArrayList<String>();
						divergingProperties.add(actNode1.getProperty("properties").toString());
						divergingProperties.add(actNode2.getProperty("properties").toString());
						divergingPropertyLists.add(divergingProperties);
					}
				}

				if (!firstExtra.get(i).toString().contains("invocation")
						&& !secondExtra.get(j).toString().contains("invocation")
						&& IsZipped(firstExtra.get(i).toString(), secondExtra.get(j).toString(), "", "")) {
					System.out.println(
							"Reaching here?" + firstExtra.get(i).toString() + "" + secondExtra.get(j).toString());
					Node entNode1 = graphDB.findNode(NodeType1.entity, "id", firstExtra.get(i).toString());
					Node entNode2 = graphDB.findNode(NodeType1.entity, "id", secondExtra.get(j).toString());
					for (int k = 0; k < divergingNodeLists.size(); k++) {

						if (divergingNodeLists.get(k).toString().contains(firstExtra.get(i).toString())) {
							System.out.println("specific " + firstExtra.get(i).toString());
							divergingNodeLists.remove(divergingNodeLists.get(k));
						}
						if (divergingNodeLists.get(k).toString().contains(secondExtra.get(j).toString())) {
							System.out.println("specific " + secondExtra.get(j).toString());
							divergingNodeLists.remove(divergingNodeLists.get(k));
						}
					}

					if (checkFurtherEntityEqual(entNode1.getProperty("attributes").toString(),
							entNode2.getProperty("attributes").toString())) { //
						System.out.println("Entities Matched");
					} else {
						divergingNodes = new ArrayList<String>();
						divergingNodes.add(firstExtra.get(i).toString());
						divergingNodes.add(secondExtra.get(j).toString());
						divergingNodeLists.add(divergingNodes);
						
						divergingProperties = new ArrayList<String>();
						divergingProperties.add(entNode1.getProperty("attributes").toString());
						divergingProperties.add(entNode2.getProperty("attributes").toString());
						divergingPropertyLists.add(divergingProperties);
					}

				}

			}
		}
		for (int i = 0; i < firstExtra.size(); i++) {
			for (int k = 0; k < divergingNodeLists.size(); k++) {

				if (divergingNodeLists.get(k).toString().contains(firstExtra.get(i).toString())
						&& ZippedKeepTrack.toString().contains(firstExtra.get(i).toString())) {
					System.out.println("specific " + firstExtra.get(i).toString());
					divergingNodeLists.remove(divergingNodeLists.get(k));
				}

			}
		}
		for (int j = 0; j < secondExtra.size(); j++) {
			for (int k = 0; k < divergingNodeLists.size(); k++) {
				if (divergingNodeLists.get(k).toString().contains(secondExtra.get(j).toString())
						&& ZippedKeepTrack.toString().contains(secondExtra.get(j).toString())) {
					System.out.println("specific " + secondExtra.get(j).toString());
					divergingNodeLists.remove(divergingNodeLists.get(k));
				}
			}
		}

	}

	private static void compareFirstEntity(String invocationId1, String invocationId2) {
		ExecutionEngine execEngine = new ExecutionEngine(graphDB);
		ExecutionResult getEntity1 = execEngine.execute("MATCH (n:entity)<-[r:used]-(p:activity) WHERE p.id =~ \".*"
				+ invocationId1 + ".*\" MATCH (n) WHERE NOT (n)-[:wasGeneratedBy]->(:activity)"
				+ " MATCH (p)-[r1:used]->(:entity) with n,p" + " return n as firstEntity1 ORDER BY id(n)");

		ExecutionResult getEntity2 = execEngine.execute("MATCH (n:entity)<-[r:used]-(p:activity) WHERE p.id =~ \".*"
				+ invocationId2 + ".*\" MATCH (n) WHERE NOT (n)-[:wasGeneratedBy]->(:activity)"
				+ " MATCH (p)-[r1:used]->(:entity) with n,p " + " return n as firstEntity2 ORDER BY id(n)");
		// System.out.println(getEntity1.dumpToString());
		// System.out.println(getEntity2.dumpToString());
		for (Map<String, Object> row1 : getEntity1) {
			for (Map<String, Object> row2 : getEntity2) {

				Node firstEntity1 = (Node) row1.get("firstEntity1");
				Node firstEntity2 = (Node) row2.get("firstEntity2");
				//addToGraphViz1(firstEntity1.getProperty("id").toString());
				//addToGraphViz2(firstEntity2.getProperty("id").toString());
				// System.out.println("Node check " + firstEntity1.toString() + "\t" +
				// firstEntity2.toString());
				// System.out.println("First entities
				// "+firstEntity1.getProperty("id").toString()+"\t"+firstEntity2.getProperty("id").toString());
				/*if(firstEntity1.getProperty("id").toString().equals(firstEntity2.getProperty("id").toString()))
				{
					firstEnt.add("e\u2081" + countEntity1);
					secondEnt.add("e\u2082" + countEntity2);
				}*/
				if (!firstEntity1.getProperty("id").equals(firstEntity2.getProperty("id")))
					System.out.println("Difference spotted");
				else
					System.out.println("First entities " + firstEntity1.getProperty("id").toString() + "\t"
							+ firstEntity2.getProperty("id").toString());
				break;
			}
		}

		// System.out.println("first Ent " + firstEnt.toString());
		// System.out.println("First All " + firstAll.toString());
	}

	private static void addToGraphViz1(String firstEntity) {
		// System.out.println("how " + firstEntity);
		if (!first.toString().contains(firstEntity)) {
			first.add(firstEntity);
			if (countEntity1 != 1) {
				firstEnt.add("e\u2081" + countEntity1);
				firstAll.add("e\u2081" + countEntity1);
				/*
				 * firstGraph.add(firstAct.get(countActivity1-1) + "<-e" + countEntity1);
				 */

			} else {
				firstEnt.add("e\u2081" + countEntity1);
				firstAct.add("a\u2081" + countActivity1);
				firstAll.add("e\u2081" + countEntity1);
				firstAll.add("a\u2081" + countActivity1);
				firstGraph.add("e\u2081" + countEntity1 + "->" + firstAct.get(countActivity1));
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
				 * secondGraph.add(secondAct.get(countActivity2-1) + "<-e" + countEntity2);
				 */
			} else {
				secondEnt.add("e\u2082" + countEntity2);
				secondAct.add("a\u2082" + countActivity2);
				secondAll.add("e\u2082" + countEntity2);
				secondAll.add("a\u2082" + countActivity2);
				secondGraph.add("e\u2082" + countEntity2 + "->" + secondAct.get(countActivity2));
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
			 * firstGraph.add(firstEnt.get(countEntity1 - 1) + "<-a" + countActivity1);
			 */

		}
	}

	private static void addActToGraphViz2(String firstActivity) {
		if (!second.toString().contains(firstActivity)) {
			second.add(firstActivity);
			secondAct.add("a\u2082" + countActivity2);
			secondAll.add("a\u2082" + countActivity2);
			/*
			 * secondGraph.add(secondEnt.get(countEntity2 - 1) + "<-a" + countActivity2);
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
							+ " MATCH (p)<-[r1:wasGeneratedBy]-(:entity) with n,p,count(r1) as cnt"
							+ " return n as firstEntity1 ORDER BY id(n)");
			ExecutionResult getEntity2 = execEngine
					.execute("MATCH (n:entity)-[r:wasGeneratedBy]->(p:activity) WHERE p.id =~ \".*"
							+ invocationId2
							+ ".*\" MATCH (n) WHERE NOT (n)<-[:used]-(:activity)"
							+ " MATCH (p)<-[r1:wasGeneratedBy]-(:entity) with n,p,count(r1) as cnt"
							+ " return n as firstEntity2 ORDER BY id(n)");
			for (Map<String, Object> row1 : getEntity1) {
				Node firstEntity1 = (Node) row1.get("firstEntity1");
				for (Map<String, Object> row2 : getEntity2) {
					Node firstEntity2 = (Node) row2.get("firstEntity2");
					//addToGraphViz1(firstEntity1.getProperty("id").toString());
					//addToGraphViz2(firstEntity2.getProperty("id").toString());
					int provLabel1 = firstEntity1.getProperty("attributes")
							.toString().indexOf("'prov:label'");
					int provType1 = firstEntity1.getProperty("attributes")
							.toString().indexOf("'prov:type'");
					int provLabel2 = firstEntity2.getProperty("attributes")
							.toString().indexOf("'prov:label'");
					int provType2 = firstEntity2.getProperty("attributes")
							.toString().indexOf("'prov:type'");
					String fileLabel1 = firstEntity1.getProperty("attributes")
							.toString()
							.substring(provLabel1 + 14, provType1 - 6);
					String fileLabel2 = firstEntity2.getProperty("attributes")
							.toString()
							.substring(provLabel2 + 14, provType2 - 6);
					/*
					 * boolean isEqual =
					 * firstEntity1.getProperty("id").toString() == firstEntity2
					 * .getProperty("id").toString();
					 */
					FileComparison f = new FileComparison();
					boolean isNotEqual = f.fileCompare(fileLabel1, fileLabel2);
					if (isNotEqual) {
						divergingNodes = new ArrayList<String>();
						divergingNodes.add(firstEntity1.getProperty("id")
								.toString());
						divergingNodes.add(firstEntity2.getProperty("id")
								.toString());
						divergingNodeLists.add(divergingNodes);
						
						divergingProperties = new ArrayList<String>();
						divergingProperties.add(firstEntity1.getProperty("attributes")
								.toString());
						divergingProperties.add(firstEntity2.getProperty("attributes")
								.toString());
						divergingPropertyLists.add(divergingProperties);
						
						
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
				 * for (String firstAct : firstActivityList) { addActToGraphViz1(firstAct); }
				 * for (String secondAct : secondActivityList) { addActToGraphViz2(secondAct); }
				 */
				first_hop = false;
				System.out.println("Next");
			} else {
				firstActivityList = new ArrayList<String>(firstActivityNewList);
				secondActivityList = new ArrayList<String>(secondActivityNewList);
				firstActivityNewList.clear();
				secondActivityNewList.clear();
			}
			System.out.println(firstActivityList.toString());
			System.out.println(secondActivityList.toString());
			for (String firstActivity : firstActivityList) {
				
				// System.out.println(secondActivityList.toString());
				for (String secondActivity : secondActivityList) {
				
					if (!first.toString().contains(firstActivity.toString()))
						first.add(firstActivity.toString());
					if (!second.toString().contains(secondActivity.toString()))
						second.add(secondActivity.toString());
					
					/*
					 * firstAct.add("a" + countActivity1); secondAct.add("a" + countActivity2);
					 */
					Node firstActivityNode = graphDB.findNode(NodeType1.activity, "id", firstActivity);
					Node SecondActivityNode = graphDB.findNode(NodeType1.activity, "id", secondActivity);

					/********** First activity comparison *************/
					/************
					 * 30/10/2017
					 */
					boolean activityzip1 = isActivityZipped(firstActivity, secondActivity, firstActivityNode.getProperty("properties")
							.toString(), SecondActivityNode.getProperty("properties")
							.toString());
					if (activityzip1 == true) {
						boolean isEqualact1 = checkActivityEqual(firstActivity, secondActivity);
						boolean addedact1 = divergingNodeLists.toString().contains(firstActivity.toString());
						/*
						 * if (isEqualact1) System.out.println("Zipped and equal " + firstActivity + "|"
						 * + secondActivity);
						 */
						if (!isEqualact1 && !addedact1) {
							divergingNodes = new ArrayList<String>();
							divergingNodes.add(firstActivity);
							divergingNodes.add(secondActivity);
							divergingNodeLists.add(divergingNodes);
							
							divergingProperties = new ArrayList<String>();
							divergingProperties.add(firstActivityNode.getProperty("properties")
									.toString());
							divergingProperties.add(SecondActivityNode.getProperty("properties")
									.toString());
							divergingPropertyLists.add(divergingProperties);							
							
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
							lastEntityId1 = returned1Entity.getProperty("id").toString();
							lastEntityId2 = returned2Entity.getProperty("id").toString();
							boolean zip = IsZipped(returned1Entity.getProperty("id").toString(),
									returned2Entity.getProperty("id").toString(),returned1Entity.getProperty("attributes").toString(),returned2Entity.getProperty("attributes").toString());
							if (zip == true) {
								boolean isEqual1 = compare(returned1Entity.getProperty("attributes").toString(),
										returned2Entity.getProperty("attributes").toString(),
										returned1Entity.getProperty("id").toString(),
										returned2Entity.getProperty("id").toString());

								if (isEqual1) {
									ZippedKeepTrack.add(returned1Entity.getProperty("id").toString());
									ZippedKeepTrack.add(returned2Entity.getProperty("id").toString());
								}
							} else {
								boolean added1 = divergingNodeLists.toString()
										.contains(returned1Entity.getProperty("id").toString());
								if (!divergingNodeLists.toString()
										.contains(returned1Entity.getProperty("id").toString())
										&& returned1Entity.getProperty("id").toString() != null) {
									divergingNodes = new ArrayList<String>();
									divergingNodes.add(returned1Entity.getProperty("id").toString());
									divergingNodes.add("**************ABSENT**************");
									divergingNodeLists.add(divergingNodes);
									
									divergingProperties = new ArrayList<String>();
									divergingProperties.add(returned1Entity.getProperty("attributes").toString());
									divergingProperties.add("**************ABSENT**************");
									divergingPropertyLists.add(divergingProperties);
									
								} else if (!divergingNodeLists.toString()
										.contains(returned2Entity.getProperty("id").toString())
										&& returned2Entity.getProperty("id").toString() != null) {
									divergingNodes = new ArrayList<String>();
									divergingNodes.add("**************ABSENT**************");
									divergingNodes.add(returned2Entity.getProperty("id").toString());
									divergingNodeLists.add(divergingNodes);
									
									divergingProperties = new ArrayList<String>();
									divergingProperties.add("**************ABSENT**************");
									divergingProperties.add(returned2Entity.getProperty("attributes").toString());
									divergingPropertyLists.add(divergingProperties);
								}
							}

							try {
								ResourceIterable<Node> returned1ActivityList = getUsed(returned1Entity);
								ResourceIterable<Node> returned2ActivityList = getUsed(returned2Entity);
								if (returned1ActivityList == null && returned2ActivityList == null)
									System.out.println("Activities null");

								if (returned1ActivityList.iterator().hasNext()
										&& returned2ActivityList.iterator().hasNext()) {
									for (Node returnActivity1 : returned1ActivityList) {
										for (Node returnActivity2 : returned2ActivityList) {
											boolean activityzip = isActivityZipped(
													returnActivity1.getProperty("id").toString(),
													returnActivity2.getProperty("id").toString(),returnActivity1.getProperty("properties").toString(),returnActivity2.getProperty("properties").toString());

											if (activityzip == true) {
												boolean isEqual = checkActivityEqual(
														returnActivity1.getProperty("properties").toString(),
														returnActivity2.getProperty("properties").toString());
												boolean added = divergingNodeLists.toString()
														.contains(returnActivity1.getProperty("id").toString());
												if (isEqual) {
													ZippedKeepTrack.add(returnActivity1.getProperty("id").toString());
													ZippedKeepTrack.add(returnActivity2.getProperty("id").toString());
												}
												if (!isEqual && !added) {
													divergingNodes = new ArrayList<String>();
													divergingNodes.add(returnActivity1.getProperty("id").toString());
													divergingNodes.add(returnActivity2.getProperty("id").toString());
													divergingNodeLists.add(divergingNodes);
													
													divergingProperties = new ArrayList<String>();
													divergingProperties.add(returnActivity1.getProperty("properties").toString());
													divergingProperties.add(returnActivity2.getProperty("properties").toString());
													divergingPropertyLists.add(divergingProperties);
												}
												firstActivityNewList.add(returnActivity1.getProperty("id").toString());
												secondActivityNewList.add(returnActivity2.getProperty("id").toString());
											}
										}
									}
									/************************************
									 * Added on 31/10/2017 Checks for extra activities in Workflow Graph1
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
		} while (!firstActivityNewList.isEmpty() && !secondActivityNewList.isEmpty());
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
					firstnodes1(returnEntity);
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
					secondnodes1(returnEntity);
					RemainingList = getUsed(returnEntity);
				}
			}
		} while (RemainingList.iterator().hasNext());
	}

	//For Activity
	private static void firstnodes(Node returned) {
		if (!divergingNodeLists.toString().contains(returned.getProperty("id").toString())
				&& returned.getProperty("id").toString().contains(InvocationId1)) {

			divergingNodes = new ArrayList<String>();
			divergingNodes.add(returned.getProperty("id").toString());
			divergingNodes.add("**************ABSENT**************");
			divergingNodeLists.add(divergingNodes);

			divergingProperties = new ArrayList<String>();
			divergingProperties.add(returned.getProperty("properties").toString());
			divergingProperties.add("**************ABSENT**************");
			divergingPropertyLists.add(divergingProperties);
			
			firstExtra.add(returned.getProperty("id").toString());
		}
	}
	
	//For Entity
	private static void firstnodes1(Node returned) {
		if (!divergingNodeLists.toString().contains(returned.getProperty("id").toString())
				&& returned.getProperty("id").toString().contains(InvocationId1)) {

			divergingNodes = new ArrayList<String>();
			divergingNodes.add(returned.getProperty("id").toString());
			divergingNodes.add("**************ABSENT**************");
			divergingNodeLists.add(divergingNodes);

			divergingProperties = new ArrayList<String>();
			divergingProperties.add(returned.getProperty("attributes").toString());
			divergingProperties.add("**************ABSENT**************");
			divergingPropertyLists.add(divergingProperties);
			
			firstExtra.add(returned.getProperty("id").toString());
		}
	}

	private static void secondnodes(Node returned) {
		if (!divergingNodeLists.toString().contains(returned.getProperty("id").toString())
				&& returned.getProperty("id").toString().contains(InvocationId2)) {

			divergingNodes = new ArrayList<String>();
			divergingNodes.add("**************ABSENT**************");
			divergingNodes.add(returned.getProperty("id").toString());
			divergingNodeLists.add(divergingNodes);
			
			divergingProperties = new ArrayList<String>();
			divergingProperties.add("**************ABSENT**************");
			divergingProperties.add(returned.getProperty("properties").toString());
			divergingPropertyLists.add(divergingProperties);

			secondExtra.add(returned.getProperty("id").toString());
		}
	}
	
	private static void secondnodes1(Node returned) {
		if (!divergingNodeLists.toString().contains(returned.getProperty("id").toString())
				&& returned.getProperty("id").toString().contains(InvocationId2)) {

			divergingNodes = new ArrayList<String>();
			divergingNodes.add("**************ABSENT**************");
			divergingNodes.add(returned.getProperty("id").toString());
			divergingNodeLists.add(divergingNodes);

			divergingProperties = new ArrayList<String>();
			divergingProperties.add("**************ABSENT**************");
			divergingProperties.add(returned.getProperty("attributes").toString());
			divergingPropertyLists.add(divergingProperties);
			
			secondExtra.add(returned.getProperty("id").toString());
		}
	}

	private static boolean checkActivityEqual(String ActivityAttributes1, String ActivityAttributes2) {
		count1++;
		boolean compare = false;
		// TODO Auto-generated method stub
		try {

			String[] Attribute1 = ActivityAttributes1.split("/block/");
			String[] Attribute2 = ActivityAttributes2.split("/block/");

			// TODO Auto-generated method stub
			String checkString1 = Attribute1[0] + "/block/";
			String checkString2 = Attribute2[0] + "/block/";

			if (ActivityAttributes1.toString().contains(checkString1)
					&& ActivityAttributes2.toString().contains(checkString2)) {
				// System.out.println("If Entity Attributes has prov:label");
				String[] provLabel1 = ActivityAttributes1.split(checkString1);
				// System.out.println("starting index:" + provLabel);
				// int provType1 = checkString1.length();
				String[] provLabel2 = ActivityAttributes2.split(checkString2);
				// System.out.println("starting index:" + provLabel);
				// int provType2 = checkString2.length();
				if (provLabel1[1].toString().equals(provLabel2[1].toString())) {
					// System.out.println("Both activities are equal ");
					compare = true;
				} else {
					System.out.println("Difference Spotted ");
				}
			}
		} catch (Exception ex) {
			System.out.println(ex.toString());
		}
		return compare;
	}

	private static boolean isActivityZipped(String firstActivity, String secondActivity, String firstProp, String secondProp) {
		// System.out.println("Zipping activties "+firstActivity +"|"+secondActivity);
		count++;
		String initial = firstActivity.substring(0, 11);
		boolean isZipped = false;

		String[] firstsplit = firstActivity.split(initial + "/invocation/" + InvocationId1 + "/block/");
		String[] secondsplit = secondActivity.split(initial + "/invocation/" + InvocationId2 + "/block/");
		if (firstsplit[1].toString().contains(secondsplit[1].toString())) {
			isZipped = true;
		}
		if (isZipped != true && !divergingNodeLists.toString().contains(firstActivity)
				&& !ZippedKeepTrack.toString().contains(firstActivity)) {
			divergingNodes = new ArrayList<String>();
			divergingNodes.add(firstActivity);
			divergingNodes.add("**************ABSENT**************");
			divergingNodeLists.add(divergingNodes);
			
			divergingProperties = new ArrayList<String>();
			divergingProperties.add(firstProp);
			divergingProperties.add("**************ABSENT**************");
			divergingPropertyLists.add(divergingProperties);
			
			firstExtra.add(firstActivity);
		}
		if (isZipped != true && !divergingNodeLists.toString().contains(secondActivity)
				&& !ZippedKeepTrack.toString().contains(secondActivity)) {
			divergingNodes = new ArrayList<String>();
			divergingNodes.add("**************ABSENT**************");
			divergingNodes.add(secondActivity);
			divergingNodeLists.add(divergingNodes);

			divergingProperties = new ArrayList<String>();
			divergingProperties.add("**************ABSENT**************");
			divergingProperties.add(secondProp);
			divergingPropertyLists.add(divergingProperties);
			
			secondExtra.add(secondActivity);
		}
		return isZipped;
	}

	private static boolean isActivityZipped1(String firstActivity, String secondActivity) {
		String initial = firstActivity.substring(0, 11);
		boolean isZipped = false;

		String[] firstsplit = firstActivity.split(initial + "/invocation/" + InvocationId1 + "/block/");
		String[] secondsplit = secondActivity.split(initial + "/invocation/" + InvocationId2 + "/block/");
		if (firstsplit[1].toString().contains(secondsplit[1].toString())) {
			isZipped = true;
		}
		return isZipped;
	}

	private static boolean IsZipped(String EntityAttributes1, String EntityAttributes2, String firstProp, String secondProp) {
		count++;
		boolean ifZipped = false;
		if (EntityAttributes1.contains("tr") && EntityAttributes2.contains("tr")) {
			String[] check1String = EntityAttributes1.toString().split("-");
			String[] check2String = EntityAttributes2.toString().split("-");
			String Entity1 = EntityAttributes1.toString().substring(EntityAttributes1.indexOf(check1String[6]))
					.toString();
			String Entity2 = EntityAttributes2.toString().substring(EntityAttributes2.indexOf(check2String[6]))
					.toString();
			// System.out.println("Entity1 " + Entity1 + "Entity 2 " + Entity2);
			if (Entity1.equals(Entity2)) {
				ifZipped = true;

				/****
				 * To remove duplicate comparisonIsZipped
				 * 
				 */
				for (int i = 0; i < divergingNodeLists.size(); i++) {
					if (divergingNodeLists.get(i).toString().contains(Entity1)) {
						divergingNodeLists.remove(divergingNodeLists.get(i));
						divergingPropertyLists.remove(divergingPropertyLists.get(i));
					} else if (divergingNodeLists.get(i).toString().contains(Entity2)) {
						divergingNodeLists.remove(divergingNodeLists.get(i));
						divergingPropertyLists.remove(divergingPropertyLists.get(i));
					}
				}
				/****
				 * To remove duplicate comparison
				 */

			} else if (!divergingNodeLists.toString().contains(Entity1) && Entity1 != null) {
				divergingNodes = new ArrayList<String>();
				divergingNodes.add(EntityAttributes1);
				divergingNodes.add("**************ABSENT**************");
				divergingNodeLists.add(divergingNodes);
				
				divergingProperties = new ArrayList<String>();
				divergingProperties.add(firstProp);
				divergingProperties.add("**************ABSENT**************");
				divergingPropertyLists.add(divergingProperties);
			} else if (!divergingNodeLists.toString().contains(Entity2) && Entity2 != null) {
				divergingNodes = new ArrayList<String>();
				divergingNodes.add("**************ABSENT**************");
				divergingNodes.add(EntityAttributes2);
				divergingNodeLists.add(divergingNodes);
				
				divergingProperties = new ArrayList<String>();
				divergingProperties.add("**************ABSENT**************");
				divergingProperties.add(secondProp);
				divergingPropertyLists.add(divergingProperties);
			}
			/*
			 * System.out.println( "Check Entity Attributes " + EntityAttributes1 +
			 * "Entity Attributes2 " + EntityAttributes2);
			 */
		} else {
			if (!divergingNodeLists.toString().contains(EntityAttributes1) && !EntityAttributes1.contains("esc")) {
				divergingNodes = new ArrayList<String>();
				divergingNodes.add(EntityAttributes1);
				divergingNodes.add("**************ABSENT**************");
				divergingNodeLists.add(divergingNodes);
				
				divergingProperties = new ArrayList<String>();
				divergingProperties.add(firstProp);
				divergingProperties.add("**************ABSENT**************");
				divergingPropertyLists.add(divergingProperties);
			}
			checkNonIso = 1;
			if (EntityAttributes1.equals(EntityAttributes2)) {
				ifZipped = true;
			}
		}
		return ifZipped;
	}

	private static boolean IsZipped1(String EntityAttributes1, String EntityAttributes2) {
		boolean ifZipped = false;
		if (EntityAttributes1.contains("tr") && EntityAttributes2.contains("tr")) {
			String[] check1String = EntityAttributes1.toString().split("-");
			String[] check2String = EntityAttributes2.toString().split("-");
			String Entity1 = EntityAttributes1.toString().substring(EntityAttributes1.indexOf(check1String[6]))
					.toString();
			String Entity2 = EntityAttributes2.toString().substring(EntityAttributes2.indexOf(check2String[6]))
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

	private static boolean compare(String EntityAttributes1, String EntityAttributes2, String firstEntityId,
			String secondEntityId) {
		count1++;
		boolean compare = false;
		String checkString1 = "'esc:hashvalue'";
		if (EntityAttributes1.contains(checkString1) && EntityAttributes2.contains(checkString1)) {
			compare = checkFurtherEntityEqual(EntityAttributes1, EntityAttributes2);
		} else {

			compare = checkFirstEntityEqual(EntityAttributes1, EntityAttributes2);
		}
		return compare;
	}

	private static boolean checkFirstEntityEqual(String EntityAttributes1, String EntityAttributes2) {
		boolean compare = false;
		String checkString = "'prov:label'";
		if (EntityAttributes1.toString().contains(checkString) && EntityAttributes1.toString().contains(checkString)) {
			int provLabel1 = EntityAttributes1.indexOf("'prov:label'");
			int provType1 = EntityAttributes1.indexOf("'prov:type'");
			int provLabel2 = EntityAttributes2.indexOf("'prov:label'");
			int provType2 = EntityAttributes2.indexOf("'prov:type'");
			String fileLabel1 = EntityAttributes1.substring(provLabel1, provType1);
			String fileLabel2 = EntityAttributes2.substring(provLabel2, provType2);

			if (fileLabel1.equals(fileLabel2)) {
				compare = true;
			} else {
				System.out.println("Difference spotted");
				compare = false;
			}
		}
		return compare;

	}

	private static boolean checkFurtherEntityEqual(String EntityAttributes1, String EntityAttributes2) {
		boolean compare = false;
		String checkString1 = "'esc:hashvalue'";
		if (EntityAttributes1.toString().contains(checkString1)
				&& EntityAttributes2.toString().contains(checkString1)) {
			int provLabel1 = EntityAttributes1.indexOf("'esc:hashvalue'");
			int provType1 = EntityAttributes1.indexOf("'prov:type'");
			int provLabel2 = EntityAttributes2.indexOf("'esc:hashvalue'");
			int provType2 = EntityAttributes2.indexOf("'prov:type'");
			String fileLabel1 = EntityAttributes1.substring(provLabel1, provType1);
			String fileLabel2 = EntityAttributes2.substring(provLabel2, provType2);
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
		TraversalDescription entity1 = graphDB.traversalDescription().breadthFirst()
				.relationships(DynamicRelationshipType.withName("wasGeneratedBy")).evaluator(Evaluators.atDepth(1));
		org.neo4j.graphdb.traversal.Traverser traverser1 = entity1.traverse(Activity);
		entityNodeList = traverser1.nodes();
		for (Node item : entityNodeList) {
			if (item.getProperty("id").toString().contains(InvocationId1)) {
				//addToGraphViz1(item.getProperty("id").toString());

			} else if (item.getProperty("id").toString().contains(InvocationId2)) {
				//addToGraphViz2(item.getProperty("id").toString());
			}
		}
		// System.out.println("Check entity "+entityNodeList.toString());
		return entityNodeList;
	}

	private static ResourceIterable<Node> getUsed(Node Entity) {
		ResourceIterable<Node> activityNodeList = null;
		TraversalDescription entity1 = graphDB.traversalDescription().breadthFirst()
				.relationships(DynamicRelationshipType.withName("used")).evaluator(Evaluators.atDepth(1));
		org.neo4j.graphdb.traversal.Traverser traverser1 = entity1.traverse(Entity);
		activityNodeList = traverser1.nodes();

		for (Node item : activityNodeList) {

			if (item.getProperty("id").toString().contains(InvocationId1)) {

				/********** Added on 4/01/2018 *******/
				previousNodeCountUsed1++;
				// System.out.println(item.getProperty("id").toString());
				/********** Added on 4/01/2018 *******/

				if (!(first.toString().contains(item.getProperty("id").toString()))) {
					//countActivity1++;
					//addActToGraphViz1(item.getProperty("id").toString());
				}
				// formatCharacters(countActivity1);
				/*if (!.contains(firstEnt.get(countEntity1 - 1) + "->a\u2081" + countActivity1))
					firstGraph.add(firstEnt.get(countEntity1 - 1) + "->a\u2081" + countActivity1);*/
				// System.out.println(firstGraph.toString());
			} else {
				/********** Added on 4/01/2018 *******/
				previousNodeCountUsed2++;
				/*
				 * System.out.println("previousNodeCountUsed: invocation 2: " +
				 * previousNodeCountGen2); System.out.println("count ent : " + countEntity2);
				 * System.out.println(item.getProperty("id").toString());
				 */
				/********** Added on 4/01/2018 *******/

				if (!(second.toString().contains(item.getProperty("id").toString()))) {
					/*countActivity2++;
					addActToGraphViz2(item.getProperty("id").toString());*/
				}
				/*if (!secondGraph.contains(secondEnt.get(countEntity2 - 1) + "->a\u2082" + countActivity2))
					secondGraph.add(secondEnt.get(countEntity2 - 1) + "->a\u2082" + countActivity2);*/
				// System.out.println(secondGraph.toString());
			}
		}
		return activityNodeList;
	}

	/*
	 * private static void formatCharacters(int countActivity) { if(countActivity ==
	 * 1) format = "U+00B9"; else if(countActivity == 2) format = "\u00B2"; else
	 * if(countActivity == 3) format = "\u00B3"; else format =
	 * (char)(0x966+countActivity); }
	 */

	private static List<String> findFirstActivity(String string1) {
		List<String> ActivityIds = null;
		System.out.println("Hi");
		try {

			ExecutionEngine execEngine = new ExecutionEngine(graphDB);
			ExecutionResult getActivity1 = execEngine
					.execute("MATCH (n:entity)<-[r:used]-(p:activity) WHERE p.id =~ \".*" + string1
							+ ".*\" MATCH (n) WHERE NOT (n)-[:wasGeneratedBy]->(:activity)"
							+ " MATCH (p)-[r1:used]->(:entity) with n,p,count(r1) as cnt"
							+ " return distinct(p.id) as firstActivity");

			ResourceIterator<Object> firstActivity = getActivity1.columnAs("firstActivity");
			ActivityIds = new ArrayList<String>();
			
			while (firstActivity.hasNext()) {
				ActivityIds.add(firstActivity.next().toString());
				/*if(firstActivity.next().toString().contains(InvocationId1)){
				firstAct.add("a\u2081" + countActivity1);
				firstAll.add("a\u2081" + countActivity1);
				}
				else{
					secondAct.add("a\u2081" + countActivity1);
					secondAll.add("a\u2081" + countActivity1);
				}*/
			}
		} catch (Exception ex) {

		}
		// System.out.println("First Activities " + ActivityIds.toString());
		return ActivityIds;
	}

	private static int findEntityCount(String string1) {

		ExecutionEngine execEngine = new ExecutionEngine(graphDB);

		ExecutionResult getInputCount = execEngine.execute("MATCH (n:entity)<-[r:used]-(p:activity) WHERE p.id =~ \".*"
				+ string1 + ".*\"MATCH (n) WHERE NOT (n)-[:wasGeneratedBy]->(:activity)"
				+ "return count(n) as FirstEntityCount");
		ResourceIterator<Object> firstEntityCount = getInputCount.columnAs("FirstEntityCount");
		inputCount = inputCount + Integer.parseInt(firstEntityCount.next().toString());

		ExecutionResult getEntityCount = execEngine
				.execute("MATCH (n:entity) WHERE n.id =~ \".*" + string1 + ".*\" return count(n) as Count");
		ResourceIterator<Object> EntityCount = getEntityCount.columnAs("Count");
		inputCount = inputCount + Integer.parseInt(EntityCount.next().toString());

		ExecutionResult getOutputCount = execEngine
				.execute("MATCH (n:entity)-[r:wasGeneratedBy]->(p:activity) WHERE p.id =~ \".*" + string1
						+ ".*\"MATCH (n) WHERE NOT (n)<-[:used]-(:activity)" + "return count(n) as lastEntityCount");
		ResourceIterator<Object> lastEntityCount = getOutputCount.columnAs("lastEntityCount");
		inputCount = inputCount + Integer.parseInt(lastEntityCount.next().toString());

		return inputCount;
	}

	private static void makeFirstGraph(String invocationId1) {
		// TODO Auto-generated method stub
		ExecutionEngine execEngine = new ExecutionEngine(graphDB);

		ExecutionResult getGraph = execEngine.execute("MATCH (a)-[r:wasGeneratedBy]->(b:activity) WHERE b.id =~ \".*"
				+ invocationId1 + ".*\"RETURN a,r,b as getGeneratedByRelationship");
		int i = 0;
		int j = 1;
		while (getGraph.hasNext()) {
			if (!firstGraph.contains("a\u2081" + i + "->" + "e\u2081" + j))
				firstGraph.add("a\u2081" + i + "->" + "e\u2081" + j);
			// System.out.println("a\u2081" + i + "->" + "e\u2081" + j);
			i++;
			j++;
			getGraph.next();
		}
		/*
		 * while (getRelationCount.hasNext()) { String newString =
		 * getRelationCount.next().toString();
		 * System.out.println("Original and New String " + original + " " + newString);
		 * if (!original.equals(newString)) { i++; } if (!firstGraph.contains("a\u2081"
		 * + (i-1) + "->" + "e\u2081" + j)) firstGraph.add("a\u2081" + (i-1) + "->" +
		 * "e\u2081" + j); System.out.println("a\u2081" + (i-1) + "->" + "e\u2081" + j);
		 * 
		 * 
		 * original = newString; System.out.println(original); //
		 * System.out.println("Get Generated Next "+getRelationCount.next()); j++; //
		 * getRelationCount.next(); }
		 */
	}

	private static void makeSecondGraph(String invocationId2) {
		// TODO Auto-generated method stub
		ExecutionEngine execEngine = new ExecutionEngine(graphDB);

		ExecutionResult getGraph = execEngine.execute("MATCH (a)-[r:wasGeneratedBy]->(b:activity) WHERE b.id =~ \".*"
				+ invocationId2 + ".*\"RETURN a,r,b as getGeneratedByRelationship");
		int i = 0;
		int j = 1;
		while (getGraph.hasNext()) {
			if (!secondGraph.contains("a\u2082" + i + "->" + "e\u2082" + j))
				secondGraph.add("a\u2082" + i + "->" + "e\u2082" + j);
			// System.out.println("a\u2082" + i + "->" + "e\u2082" + j);
			i++;
			j++;
			getGraph.next();
		}
	}

}
