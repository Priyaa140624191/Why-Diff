package uk.ac.ncl;

import java.util.Map;

import org.neo4j.cypher.javacompat.ExecutionEngine;
import org.neo4j.cypher.javacompat.ExecutionResult;
import org.neo4j.graphdb.GraphDatabaseService;
import org.neo4j.graphdb.Node;
import org.neo4j.graphdb.NotInTransactionException;
import org.neo4j.graphdb.Relationship;
import org.neo4j.graphdb.factory.GraphDatabaseFactory;

public class ForwardTraversal {
	static GraphDatabaseFactory graphDbFactory = new GraphDatabaseFactory();
	static GraphDatabaseService graphDb = graphDbFactory
			.newEmbeddedDatabase("target/data");
	
	static Node EntityId1;
	static Node EntityId2;
	static String Activity1 = null;
	static String Activity2 = null;
	static String Entity_Id1 = null;
	static String Entity_Id2 = null;
	static String EntityAttributes1 = null;
	static String EntityAttributes2 = null;
	static Relationship relation1;
	static Relationship relation2;
	static ExecutionEngine execEngine = new ExecutionEngine(graphDb);
	public static void main(String[] args) {
		

		String firstId = "esc:svi-esc/document/663/664";
		String secondId = "esc:svi-esc/document/663/664";
		
		checkFirstGraphEntity(firstId);
		checkSecondGraphEntity(secondId);
		System.out.println("**************************************");
		System.out.println("Checking Entity");
		System.out.println("Entity 1" +firstId);
		System.out.println("Entity 2" +firstId);
		System.out.println("**************************************");
		checkFirstEntityEqual();
		
		Activity1 = getRelationshipsFirstEntity(firstId);
		Activity2 = getRelationshipsSecondEntity(secondId);
		
		Entity_Id1 = checkFirstGraphActivity(Activity1);
		Entity_Id2 = checkSecondGraphActivity(Activity2);
		System.out.println("**************************************");
		System.out.println("Checking Entity");
		System.out.println("Entity 1 " +Entity_Id1);
		System.out.println("Entity 2 " +Entity_Id2);
		System.out.println("**************************************");
		checkFurtherEntityEqual();
		
		Activity1 = getRelationshipsFirstEntity(Entity_Id1);
		Activity2 = getRelationshipsSecondEntity(Entity_Id2);
		
		Entity_Id1 = checkFirstGraphActivity(Activity1);
		Entity_Id2 = checkSecondGraphActivity(Activity2);
		System.out.println("**************************************");
		System.out.println("Checking Entity");
		System.out.println("Entity 1 " +Entity_Id1);
		System.out.println("Entity 2 " +Entity_Id2);
		System.out.println("**************************************");
		checkFurtherEntityEqual();
		
		
		/*
		 * try { ExecutionResult getAllRelationship2 = execEngine
		 * .execute("MATCH (:entity {id: \"" + secondId +
		 * "\"})-[r]-(p:activity) RETURN r as Relationships, p.id as ActivityNode"
		 * );
		 * 
		 * for (Map<String, Object> row : getAllRelationship2) { relation2 =
		 * (Relationship) row.get("Relationships");
		 * System.out.println("First RelationType" + relation2.getType());
		 * Activity2 = (String) row.get("ActivityNode");
		 * System.out.println("Activity Id" + Activity2); } } catch
		 * (NotInTransactionException ex) { System.out.println(ex.toString()); }
		 */
		/*
		 * System.out.println(getAllRelationship1.dumpToString());
		 * System.out.println(getAllRelationship2.dumpToString());
		 */
		// System.out.println("Ending index:" + provType);
		// System.out.println(ActivityAttribute.substring(provLabel,
		// provType));

		/*
		 * ExecutionResult get2EntityNode = execEngine .execute(
		 * "MATCH (n:entity) WHERE n.id=\"esc:svi-esc/document/663/664\" return n.attributes as EntityAttributes limit 1"
		 * );
		 */

		/*
		 * ExecutionResult getEntityAttributes = execEngine .execute(
		 * "MATCH (n1:entity) RETURN n1 as entityNode, n1.id as EntityId, n1.attributes as EntityAttributes"
		 * );
		 * 
		 * for (Map<String, Object> row : getEntityAttributes) { String EntityId
		 * = (String) row.get("EntityId"); System.out.println("EntityId " +
		 * EntityId); try { ExecutionResult getUsedRelationship = execEngine
		 * .execute("MATCH (:entity {id: \"" + EntityId +
		 * "\"})-[r]-(p:activity) RETURN r as Relationships, p.id as ActivityNode"
		 * ); // for (Map<String, Object> row1 : getRelationship) { // } // For
		 * finding last node :
		 * 
		 * ExecutionResult getLastNode = execEngine .execute(
		 * "MATCH (s:entity) WHERE NOT (s)<-[:used]-(:activity) RETURN s" );
		 * 
		 * 
		 * System.out.println("Relationships:" +
		 * getUsedRelationship.dumpToString());
		 * 
		 * for (Map<String, Object> row1 : getUsedRelationship) {
		 * System.out.println("Inside for 11111"); String ActivityAttribute =
		 * (String) row1 .get("ActivityNode");
		 * System.out.println(ActivityAttribute); try { ExecutionResult
		 * getLastNode = execEngine .execute(
		 * "MATCH (s:entity) WHERE NOT (s)<-[:used]-(:activity) RETURN s.id as lastEntity"
		 * ); ResourceIterator<Object> Lastnode = getLastNode
		 * .columnAs("lastEntity"); if (EntityId == Lastnode.next() || EntityId
		 * == Lastnode.next()) { System.out.println("Reached Last node " +
		 * EntityId); } } catch (NotInTransactionException ex) {
		 * System.out.println(ex.toString()); }
		 * 
		 * for (Map<String, Object> row2 : getLastNode) { String lastActivityId=
		 * (String) row2.get("LastActivityId"); if(lastActivityId ==
		 * ActivityAttribute) { System.out.println("End of Graph"); } }
		 * 
		 * 
		 * } } catch (NotInTransactionException ex) {
		 * System.out.println(ex.toString()); } // System.out.println("EntityId"
		 * + EntityId); String EntityAttribute = (String)
		 * row.get("EntityAttributes"); //
		 * System.out.println("EntityAttributes"+EntityAttribute); Node
		 * EntityNode = (Node) row.get("entityNode"); //
		 * System.out.println(EntityNode.getId()); try {
		 * 
		 * if (EntityNode.hasRelationship()) { // System.out.println("Degree " +
		 * EntityNode.getDegree()); } } catch (NotInTransactionException ex) {
		 * System.out.println(ex.toString()); }
		 * 
		 * String checkString2 = "'prov:label'"; if
		 * (EntityAttribute.toString().contains(checkString2)) { //
		 * System.out.println("If Entity Attributes has prov:label"); int
		 * provLabel = EntityAttribute.indexOf("'prov:label'"); //
		 * System.out.println("starting index:" + provLabel); int provType =
		 * EntityAttribute.indexOf("'prov:type'"); //
		 * System.out.println("Ending index:" + provType); //
		 * System.out.println(EntityAttribute.substring(provLabel, //
		 * provType)); } }
		 * 
		 * ExecutionResult exec_getActivity = execEngine .execute(
		 * "MATCH (n1:activity) RETURN n1.id as ActivityId, n1.attributes as ActivityAttributes"
		 * ); ResourceIterator<Object> Activityids = exec_getActivity
		 * .columnAs("ActivityId"); ResourceIterator<Object> ActivityAttributes
		 * = exec_getActivity .columnAs("ActivityAttributes"); while
		 * (Activityids.hasNext() && ActivityAttributes.hasNext()) { //
		 * System.out.println("Inside while ids"); //
		 * System.out.println(Activityids.next()); String ActivityAttribute =
		 * ActivityAttributes.next().toString(); //
		 * System.out.println(ActivityAttribute); int provLabel =
		 * ActivityAttribute.indexOf("'prov:label'"); //
		 * System.out.println("starting index:" + provLabel); int provType =
		 * ActivityAttribute.indexOf("'prov:type'"); //
		 * System.out.println("Ending index:" + provType); //
		 * System.out.println(ActivityAttribute.substring(provLabel, //
		 * provType)); } ExecutionResult execResult2 = execEngine
		 * .execute("MATCH p=()-[r:used]->() RETURN p"); String results2 =
		 * execResult2.dumpToString(); // System.out.println(results2);
		 * 
		 * ExecutionResult execResult_generatedBy = execEngine
		 * .execute("MATCH p=()-[r:wasGeneratedBy]->() RETURN p"); String
		 * results_generatedBy = execResult_generatedBy.dumpToString(); //
		 * System.out.println(results_generatedBy);
		 */}

	private static void checkFirstGraphEntity(String firstId) {
		// TODO Auto-generated method stub
		ExecutionResult getEntityNode1 = execEngine
				.execute("MATCH (n:entity) WHERE n.id=\""
						+ firstId
						+ "\" return n as entityId, n.attributes as entityAttributes limit 1");
		for (Map<String, Object> row : getEntityNode1) {
			EntityId1 = (Node) row.get("entityId");
			System.out.println("First Entity" + EntityId1.getId());
			EntityAttributes1 = (String) row.get("entityAttributes");
			System.out.println("Entity Attributes" + EntityAttributes1);
		}
	}
	private static void checkSecondGraphEntity(String secondId) {
		// TODO Auto-generated method stub
		ExecutionResult getEntityNode2 = execEngine
				.execute("MATCH (n:entity) WHERE n.id=\""
						+ secondId
						+ "\" return n as entityId, n.attributes as entityAttributes ORDER BY id(n) DESC LIMIT 1");
		for (Map<String, Object> row : getEntityNode2) {
			EntityId2 = (Node) row.get("entityId");
			System.out.println("Second Entity" + EntityId2.getId());
			EntityAttributes2 = (String) row.get("entityAttributes");
			System.out.println("Entity Attributes" + EntityAttributes2);
		
		}
	}
	private static void checkFirstEntityEqual() {
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
			}
		}
	}
	private static String getRelationshipsFirstEntity(String firstId) {
		// TODO Auto-generated method stub
		try {
			ExecutionResult getAllRelationship1 = execEngine
					.execute("MATCH (:entity {id: \""
							+ firstId
							+ "\"})-[r]-(p:activity) RETURN r as Relationships, p.id as ActivityNode");
			for (Map<String, Object> row : getAllRelationship1) {
				relation1 = (Relationship) row.get("Relationships");
				System.out.println("First RelationType " + relation1.getType());
				Activity1 = (String) row.get("ActivityNode");
				System.out.println("Activity Id " + Activity1);
			}
		} catch (NotInTransactionException ex) {

		}
		return Activity1;
	}
	private static String getRelationshipsSecondEntity(String secondId) {
		// TODO Auto-generated method stub
		try {
			ExecutionResult getAllRelationship2 = execEngine
					.execute("MATCH (:entity {id: \""
							+ secondId
							+ "\"})-[r]-(p:activity) RETURN r as Relationships, p.id as ActivityNode ORDER BY id(p) DESC");
			// System.out.println(getAllRelationship2.dumpToString());

			for (Map<String, Object> row : getAllRelationship2) {
				System.out.println("Inside for");
				relation2 = (Relationship) row.get("Relationships");
				System.out
						.println("Second RelationType " + relation2.getType());
				Activity2 = (String) row.get("ActivityNode");
				System.out.println("Activity Id " + Activity2);
			}
		} catch (NotInTransactionException ex) {
                  
		}
		return Activity2;
	}
	private static String checkFirstGraphActivity(String activity1) {
		// TODO Auto-generated method stub
		try {
			ExecutionResult getAllRelationship1 = execEngine
					.execute("MATCH (:activity {id: \""
							+ activity1
							+ "\"})<-[r]-(p:entity) RETURN p as entityNode, p.id as entityId, p.attributes as entityAttributes");
			//System.out.println(getAllRelationship1.dumpToString());
			for (Map<String, Object> row : getAllRelationship1) {
				System.out.println("Inside forr");
				/*relation1 = (Relationship) row.get("Relation");
				System.out.println("First RelationType " + relation.getType());*/
				EntityId1 = (Node) row.get("entityNode");
				System.out.println("Entity Id " + EntityId1.getId());
				Entity_Id1 = (String) row.get("entityId");
				System.out.println("Entity Iden " + Entity_Id1);
				EntityAttributes1 = (String)row.get("entityAttributes");
				System.out.println("Entity Attributes "+EntityAttributes1);
			}
		} catch (NotInTransactionException ex) {

		}
		return Entity_Id1;
	}
	private static String checkSecondGraphActivity(String activity2) {
		// TODO Auto-generated method stub
		try {
			ExecutionResult getAllRelationship1 = execEngine
					.execute("MATCH (:activity {id: \""
							+ activity2
							+ "\"})<-[r]-(p:entity) RETURN r as Relationships, p as entityNode, p.id as entityId, p.attributes as entityAttributes");
			for (Map<String, Object> row : getAllRelationship1) {
				/*relation2 = (Relationship) row.get("Relationships");
				System.out.println("Second RelationType " + relation2.getType());*/
				EntityId2 = (Node) row.get("entityNode");
				System.out.println("Entity Id " + EntityId2.getId());
				Entity_Id2 = (String) row.get("entityId");
				System.out.println("Entity Iden " + Entity_Id2);
				EntityAttributes2 = (String)row.get("entityAttributes");
				System.out.println("Entity Attributes "+EntityAttributes2);
			}
		} catch (NotInTransactionException ex) {

		}
		return Entity_Id2;
	}
	private static void checkFurtherEntityEqual() {
		// TODO Auto-generated method stub
		String checkString1 = "'esc:hashvalue'";
		if (EntityAttributes1.toString().contains(checkString1)
				&& EntityAttributes1.toString().contains(checkString1)) {
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
			}
		}
	}

}
