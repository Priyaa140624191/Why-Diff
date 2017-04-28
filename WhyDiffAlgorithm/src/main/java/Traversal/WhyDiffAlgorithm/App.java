package Traversal.WhyDiffAlgorithm;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import org.neo4j.graphdb.DynamicLabel;
import org.neo4j.graphdb.GraphDatabaseService;
import org.neo4j.graphdb.Label;
import org.neo4j.graphdb.Node;
import org.neo4j.graphdb.RelationshipType;
import org.neo4j.graphdb.ResourceIterator;
import org.neo4j.graphdb.Transaction;
import org.neo4j.graphdb.factory.GraphDatabaseFactory;
import org.w3c.dom.NodeList;

/**
 * Hello world!
 * 
 */
public class App {
	NodeList nl = null;

	static GraphDatabaseService graphDB;
	

	public enum NodeType1 implements Label {
		entity, agent, activity;
	}

	public enum RelationType1 implements RelationshipType {
		used, wasGeneratedBy;
	}

	public static void main(String[] args) {
		GraphDatabaseFactory graphDB1 = new GraphDatabaseFactory();
		graphDB = graphDB1.newEmbeddedDatabase("target/data1");
		try (Transaction tx = graphDB.beginTx()) {
			CreateFirstGraph1.FirstGraph();
			CreateSecondGraph1.SecondGraph();
			tx.success();
		} catch (Exception ex) {
			System.out.println(ex);
		}
		graphDB.shutdown();
	}

	
	
}
