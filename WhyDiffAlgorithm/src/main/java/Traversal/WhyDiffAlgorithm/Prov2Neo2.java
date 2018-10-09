package Traversal.WhyDiffAlgorithm;

import java.io.BufferedReader;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import org.neo4j.graphdb.DynamicLabel;
import org.neo4j.graphdb.GraphDatabaseService;
import org.neo4j.graphdb.Label;
import org.neo4j.graphdb.Node;
import org.neo4j.graphdb.ResourceIterable;
import org.neo4j.graphdb.Transaction;
import org.neo4j.graphdb.factory.GraphDatabaseFactory;

import Traversal.WhyDiffAlgorithm.App.NodeType1;
import Traversal.WhyDiffAlgorithm.App.RelationType1;

public class Prov2Neo2 {
	static GraphDatabaseService graphDB;
	List<org.neo4j.graphdb.Node> neoActivityNodeList = new ArrayList<>();
	List<org.neo4j.graphdb.Node> neoEntityNodeList = new ArrayList<>();

	public enum NodeType1 implements Label {
		entity, agent, activity;
	}

	public void createEntityNodes() {
		String line = null;

		FileReader fileReader = null;
		FileReader fileReader1 = null;
		try {
			fileReader = new FileReader("hdb-20171003-135417831.pl");
			fileReader1 = new FileReader("hdb-20171003-135417831.pl");
		} catch (FileNotFoundException e1) {
			// TODO Auto-generated catch block
			e1.printStackTrace();
		}

		// Always wrap FileReader in BufferedReader.
		BufferedReader bufferedReader = new BufferedReader(fileReader);
		int i = 0;
		// int j = 0;
		try {
			while ((line = bufferedReader.readLine()) != null) {
				if (line.contains("document("))
				/*
				 * System.out.println(line); i++;
				 */
				{
					if (i == 0)
						System.out.println("Skipped Line " + line.toString());
					// String[] parts = line.split("\"");
					// String id = parts[1]
					else {
						String[] parts = line.split("\"");
						String id = parts[1];
						String[] parts1 = line.split("\\{");
						String properties = parts1[1];
						String[] properties1 = properties.split("\\}");

						/*
						 * if (id.toString().contains("invocation")) {
						 * 
						 * org.neo4j.graphdb.Node a1 = createActivityNodes(id,
						 * i, properties1[0]); }else {
						 */
						org.neo4j.graphdb.Node e1 = createEntityNodes(id, i,
								properties1[0]);
						// }
					}
					i++;
					/*
					 * String[] parts = line.split("\""); String entityId =
					 * parts[3]; String activityId = parts[5];
					 * 
					 * org.neo4j.graphdb.Node e1 = createEntityNodes(entityId,
					 * i); org.neo4j.graphdb.Node a1 =
					 * createActivityNodes(activityId, i);
					 * e1.createRelationshipTo(a1,
					 * RelationType1.wasGeneratedBy); i++;
					 * System.out.println("End of first if");
					 */
				}
			}
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

	private void createActivityNodes() {
		// String fileName = "hdb-20171003-135417831.pl";
		// String activityId;

		// This will reference one line at a time
		String line = null;

		FileReader fileReader = null;
		// FileReader fileReader1 = null;
		try {
			fileReader = new FileReader("hdb-20171003-135417831.pl");
			// fileReader1 = new FileReader("hdb-20171003-135417831.pl");
		} catch (FileNotFoundException e1) {
			// TODO Auto-generated catch block
			e1.printStackTrace();
		}

		// Always wrap FileReader in BufferedReader.
		BufferedReader bufferedReader = new BufferedReader(fileReader);
		int i = 0;
		// int j = 0;
		try {
			while ((line = bufferedReader.readLine()) != null) {
				if (line.contains("execution("))
				/*
				 * System.out.println(line); i++;
				 */
				{
					if (i == 0)
						System.out.println("Skipped Line " + line.toString());
					// String[] parts = line.split("\"");
					// String id = parts[1]
					else {
						String[] parts = line.split("\"");
						String id = parts[1];
						String[] parts1 = line.split("\\{");
						String properties = parts1[1];
						String[] properties1 = properties.split("\\}");

						/*
						 * if (id.toString().contains("invocation")) {
						 * 
						 * org.neo4j.graphdb.Node a1 = createActivityNodes(id,
						 * i, properties1[0]); }else {
						 */
						org.neo4j.graphdb.Node a1 = createActivityNodes(id, i,
								properties1[0]);
						// }
					}
					i++;
					/*
					 * String[] parts = line.split("\""); String entityId =
					 * parts[3]; String activityId = parts[5];
					 * 
					 * org.neo4j.graphdb.Node e1 = createEntityNodes(entityId,
					 * i); org.neo4j.graphdb.Node a1 =
					 * createActivityNodes(activityId, i);
					 * e1.createRelationshipTo(a1,
					 * RelationType1.wasGeneratedBy); i++;
					 * System.out.println("End of first if");
					 */
				}
			}
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

	private void createWasGeneratedByRelations() {
		String line = null;

		FileReader fileReader = null;
		try {
			fileReader = new FileReader("hdb-20171003-135417831.pl");
		} catch (FileNotFoundException e1) {
			// TODO Auto-generated catch block
			e1.printStackTrace();
		}

		// Always wrap FileReader in BufferedReader.
		BufferedReader bufferedReader = new BufferedReader(fileReader);
		int i = 0;
		// int j = 0;
		try {
			while ((line = bufferedReader.readLine()) != null) {

				if (line.contains("wasGeneratedBy(")) {
					// System.out.println("Line "+line);
					String[] parts = line.split("\"");
					// System.out.println("Parts " + parts[5]);
					String activityParts1 = parts[5];
					String entityParts1 = parts[3];
					//System.out.println("Activity "+activityParts1 +"Entity "+entityParts1);
					try {
						Node actNode1 = graphDB.findNode(NodeType1.activity,
								"id", activityParts1);
						Node entityNode1 = graphDB.findNode(NodeType1.entity,
								"id", entityParts1);
												 
						 entityNode1
							.createRelationshipTo(actNode1, RelationType1.wasGeneratedBy);
						// break;
						// }
					} catch (Exception ex) {
						// System.out.println("Could not find node");
					}
				}
				i++;
			}

		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

	}

	void createUsedRelations() {

		String line = null;

		FileReader fileReader = null;
		try {
			fileReader = new FileReader("hdb-20171003-135417831.pl");
		} catch (FileNotFoundException e1) {
			// TODO Auto-generated catch block
			e1.printStackTrace();
		}

		// Always wrap FileReader in BufferedReader.
		BufferedReader bufferedReader = new BufferedReader(fileReader);
		int i = 0;
		// int j = 0;
		try {
			while ((line = bufferedReader.readLine()) != null) {

				if (line.contains("used(")) {

					// System.out.println("Line "+line);
					String[] parts = line.split("\"");
					// System.out.println("Parts " + parts[5]);
					String activityParts1 = parts[3];
					String entityParts1 = parts[5];
					 System.out.println("Activity " + activityParts1 +
					 "Entity " + entityParts1);
					
					try {
						Node actNode1 = graphDB.findNode(NodeType1.activity,
								"id", activityParts1);
						Node entityNode1 = graphDB.findNode(NodeType1.entity,
								"id", entityParts1);

						System.out.println("Entity Matched "
								+ entityNode1.getProperty("id"));
						actNode1.createRelationshipTo(entityNode1, RelationType1.used);

					} catch (Exception ex) {
						// System.out.println("Could not find node");
					}
				}
				i++;
			}

		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

	}

	private Node createEntityNodes(String entityId, int i, String properties1) {
		// TODO Auto-generated method stub
		org.neo4j.graphdb.Node entityNode = graphDB
				.createNode(NodeType1.entity);
		Label label1 = DynamicLabel.label(Integer.toString(i));
		entityNode.addLabel(label1);
		entityNode.setProperty("id", entityId);
		entityNode.setProperty("properties", properties1);
		neoEntityNodeList.add(entityNode);
		return entityNode;
	}

	private Node createActivityNodes(String activityId, int i,
			String properties1) {
		// TODO Auto-generated method stub
		// TODO Auto-generated method stub
		org.neo4j.graphdb.Node activityNode = graphDB
				.createNode(NodeType1.activity);
		Label label1 = DynamicLabel.label(Integer.toString(i));
		activityNode.addLabel(label1);
		activityNode.setProperty("id", activityId);
		activityNode.setProperty("attributes", properties1);
		neoActivityNodeList.add(activityNode);
		return activityNode;
	}

	/**
	 * @param args
	 *            the command line arguments
	 */
	public static void main(String[] args) {

		GraphDatabaseFactory graphDB1 = new GraphDatabaseFactory();
		graphDB = graphDB1.newEmbeddedDatabase("Post73");
		try (Transaction tx = graphDB.beginTx()) {
			Prov2Neo2 app = new Prov2Neo2();
			app.createEntityNodes();
			app.createActivityNodes();
			app.createWasGeneratedByRelations();
			app.createUsedRelations();
			tx.success();
		} catch (Exception ex) {
			System.out.println(ex.toString());
		}
		graphDB.shutdown();
	}

}
