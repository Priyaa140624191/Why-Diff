package uk.ac.ncl;

import java.util.ArrayList;
import java.util.List;

import org.neo4j.graphdb.DynamicLabel;
import org.neo4j.graphdb.GraphDatabaseService;
import org.neo4j.graphdb.Label;
import org.neo4j.graphdb.Node;

import uk.ac.ncl.App.NodeType1;
import uk.ac.ncl.App.RelationType1;

public class CreateFirstGraph {
	static List<String> entityNodes = CreateEntityIds.getEntityIds();
	static List<String> activityNodes = CreateActivityIds.getActivityIds();
	static void FirstGraph() {
		// TODO Auto-generated method stub
		List<org.neo4j.graphdb.Node> entityNodeList = createEntityNodes();
		List<org.neo4j.graphdb.Node> activityNodeList = createActivityNodes();
		System.out.println(entityNodeList.size());
		System.out.println(activityNodeList.size());
		
		for (int i = 0; i < 6; i++) {
			System.out.println(entityNodeList.get(i).getId());
			System.out.println(activityNodeList.get(i).getId());
			org.neo4j.graphdb.Node entity1 = App.graphDB
					.getNodeById(entityNodeList.get(i).getId());
			org.neo4j.graphdb.Node activity1 = App.graphDB
					.getNodeById(activityNodeList.get(i).getId());

			entity1.createRelationshipTo(activity1,
					RelationType1.wasGeneratedBy);
		}
		for (int i = 0,j=1; i < 6; i++,j++) {
			System.out.println(entityNodeList.get(j).getId());
			System.out.println(activityNodeList.get(i).getId());
			org.neo4j.graphdb.Node entity1 = App.graphDB
					.getNodeById(entityNodeList.get(j).getId());
			org.neo4j.graphdb.Node activity1 = App.graphDB
					.getNodeById(activityNodeList.get(i).getId());

			activity1.createRelationshipTo(entity1,
					RelationType1.used);
		}
		org.neo4j.graphdb.Node entity1 = App.graphDB
				.getNodeById(entityNodeList.get(7).getId());
		org.neo4j.graphdb.Node activity1 = App.graphDB
				.getNodeById(activityNodeList.get(4).getId());
		activity1.createRelationshipTo(entity1,
				RelationType1.used);
		
		org.neo4j.graphdb.Node entity2 = App.graphDB
				.getNodeById(entityNodeList.get(7).getId());
		org.neo4j.graphdb.Node activity2 = App.graphDB
				.getNodeById(activityNodeList.get(6).getId());
		entity2.createRelationshipTo(activity2,
				RelationType1.wasGeneratedBy);
		
		for(int i = 6,j=8; i < 10; i++,j++){
			org.neo4j.graphdb.Node entity = App.graphDB
					.getNodeById(entityNodeList.get(j).getId());
			org.neo4j.graphdb.Node activity = App.graphDB
					.getNodeById(activityNodeList.get(i).getId());

			activity.createRelationshipTo(entity,
					RelationType1.used);
		}
		for(int i = 8,j=7; i < 12; i++,j++){
			org.neo4j.graphdb.Node entity = App.graphDB
					.getNodeById(entityNodeList.get(i).getId());
			org.neo4j.graphdb.Node activity = App.graphDB
					.getNodeById(activityNodeList.get(j).getId());

			entity.createRelationshipTo(activity,
					RelationType1.wasGeneratedBy);
		}			
		org.neo4j.graphdb.Node entity = App.graphDB
				.getNodeById(entityNodeList.get(12).getId());
		org.neo4j.graphdb.Node activity = App.graphDB
				.getNodeById(activityNodeList.get(8).getId());
		activity.createRelationshipTo(entity,
				RelationType1.used);
		
		org.neo4j.graphdb.Node entitylast = App.graphDB
				.getNodeById(entityNodeList.get(13).getId());
		org.neo4j.graphdb.Node activitylast = App.graphDB
				.getNodeById(activityNodeList.get(10).getId());
		activitylast.createRelationshipTo(entitylast,
				RelationType1.used);
		
	}
	private static List<Node> createEntityNodes() {
		List<String> propertyList = new ArrayList<String>();
		// Attributes for esc:svi-esc/document/132101/132102
		propertyList
				.add("'esc:version':'1','prov:label':'svi-classification.csv','prov:type':['prov:Entity','provone:Document']");
		// Attributes for
		// tr-132076-58644E52-D670-0900-6105-0CD62F6E70C7-joined-data
		propertyList
				.add("'esc:datatype':'data-wrapper','esc:hashvalue':'e7ATkYBEHwF5a8fVeggzwg==','prov:type':['prov:Entity','provone:Document','esc:TransientData'],'recomp:datasize':453995.");
		// Attributes for
		// tr-132076-633AC5C9-C332-56E0-D839-B89A4110B147-output-data
		propertyList
				.add("'esc:datatype':'data-wrapper','esc:hashvalue':'uxs6jJoGNk37hh8gRdn46A==','prov:type':['prov:Entity','provone:Document','esc:TransientData'],'recomp:datasize':13620.");
		// Attributes for
		// tr-132076-C0F901FD-9C13-45D0-8D53-788F4C687F62-filtered-data
		propertyList
				.add("'esc:datatype':'data-wrapper','esc:hashvalue':'MTVoI7uy9QPtBcUyXzf4rA==','prov:type':['prov:Entity','provone:Document','esc:TransientData'],'recomp:datasize':13575.");
		// Attributes for
		// tr-132076-44B152A5-F3B0-A4F9-1D71-A5BF36443F52-output-1
		propertyList
				.add("'esc:datatype':'data-wrapper','esc:hashvalue':'mhvDnDNoHmZxI1lzsyzoRQ==','prov:type':['prov:Entity','provone:Document','esc:TransientData'],'recomp:datasize':452558.");
		// Attributes for
		// tr-132076-82B4F4C6-47EF-2AEF-B49B-B67DB40B2303-imported-data
		propertyList
				.add("'esc:datatype':'data-wrapper','esc:hashvalue':'N1i6Ok3zYQx5C3jgyS4QUw==','prov:type':['prov:Entity','provone:Document','esc:TransientData'],'recomp:datasize':116773551.");
		// Attributes for
		// esc:svi-esc/document/646/647
		propertyList
				.add("'esc:version':'1', 'prov:label':'variant_summary-160502.txt', 'prov:type':['prov:Entity', 'provone:Document']");
		
		//Attributes for tr-132076-03D691E5-0AF3-D9A4-C9D1-4E3AC66C7B90-output-1
		propertyList.add("'esc:datatype':'data-wrapper','esc:hashvalue':'9zBQqO+mCfJMRg+0JKL0QA==','prov:type':['prov:Entity','provone:Document','esc:TransientData'],'recomp:datasize':365297.");

		
		//Attributes for tr-132076-C6D21A25-ED33-EB7E-5732-99603181330C-filtered-data
		propertyList.add("'esc:datatype':'data-wrapper','esc:hashvalue':'ZO1KVVuqrloHy+vy5D8t5g==','prov:type':['prov:Entity','provone:Document','esc:TransientData'],'recomp:datasize':95680.");
		
		//Attributes for tr-132076-36ED8706-6AE1-83E0-97D8-EB10CD8080FC-imported-data
		propertyList.add("'esc:datatype':'data-wrapper','esc:hashvalue':'wwg+vTnxUrRst6kdUvN6fA==','prov:type':['prov:Entity','provone:Document','esc:TransientData'],'recomp:datasize':3370301.");
		
		//Attributes for tr-132076-DF7E5B8F-5986-A516-5A78-00F45AAD393D-filtered-data
		propertyList.add("'esc:datatype':'data-wrapper','esc:hashvalue':'fFg+VHZKcmzaFmlsEnkipA==','prov:type':['prov:Entity','provone:Document','esc:TransientData'],'recomp:datasize':19949445.");
		
		//Attributes for tr-132076-BFF867C8-81FF-BAD2-4CEF-6F6DC6F0769A-imported-data
		propertyList.add("'esc:datatype':'data-wrapper','esc:hashvalue':'QX7i4XhdiEnlz1KYp5/tSA==','prov:type':['prov:Entity','provone:Document','esc:TransientData'],'recomp:datasize':38516550.");
		
		//Attributes for esc:svi-esc/document/716/717
		propertyList.add("'esc:version':1,'prov:label':'genemap2-160607-esc.txt','prov:type':['prov:Entity','provone:Document']");
		
		//Attributes for esc:svi-esc/document/663/664
		propertyList.add("'esc:version':1,'prov:label':'MUN0785.csv','prov:type':['prov:Entity','provone:Document']");

		List<org.neo4j.graphdb.Node> neoEntityNodeList = new ArrayList<>();
		for (int i = 0, j=0; i < entityNodes.size(); i++,j++) {
			System.out.println(entityNodes.get(i));
			org.neo4j.graphdb.Node entityNode = App.graphDB
					.createNode(NodeType1.entity);
			Label label = DynamicLabel.label(entityNodes.get(i));
			entityNode.addLabel(label);
			entityNode.setProperty("id", entityNodes.get(i).toString());
			entityNode
					.setProperty("attributes", propertyList.get(j).toString());
			neoEntityNodeList.add(entityNode);
		}
		return neoEntityNodeList;
		// TODO Auto-generated method stub
	}

	private static List<Node> createActivityNodes() {
		List<String> propertyList = new ArrayList<String>();
		// Attributes for
		// esc:svi-esc/invocation/132076/block/E460FCCB-DB54-F2DB-945D-B5AC8B0D2348
		propertyList
				.add("'2017-03-13T16:28:25.852+0000','2017-03-13T16:28:28.908+0000',{'esc:dataConsumed':453995,'esc:dataProduced':2909,'prov:label':'CSVExport','prov:type':['prov:Activity','provone:Execution','esc:blockExecution']}.");
		// Attributes for
		// esc:svi-esc/invocation/132076/block/58644E52-D670-0900-6105-0CD62F6E70C7
		propertyList
				.add("'2017-03-13T16:28:23.153+0000','2017-03-13T16:28:25.617+0000',{'esc:dataConsumed':459762,'esc:dataProduced':453995,'prov:label':'3wayRowJoin','prov:type':['prov:Activity','provone:Execution','esc:blockExecution']}.");
		// Attributes for
		// esc:svi-esc/invocation/132076/block/633AC5C9-C332-56E0-D839-B89A4110B147
		propertyList
				.add("'2017-03-13T16:28:17.291+0000','2017-03-13T16:28:22.212+0000',{'esc:dataConsumed':13575,'esc:dataProduced':13620,'prov:label':'Select','prov:type':['prov:Activity','provone:Execution','esc:blockExecution']}.");
		// Attributes for
		// esc:svi-esc/invocation/132076/block/C0F901FD-9C13-45D0-8D53-788F4C687F62
		propertyList
				.add("'2017-03-13T16:27:21.909+0000','2017-03-13T16:27:26.653+0000',{'esc:dataConsumed':452558,'esc:dataProduced':455446,'prov:label':'Where','prov:type':['prov:Activity','provone:Execution','esc:blockExecution']}.");
		// Attributes for
		// esc:svi-esc/invocation/132076/block/44B152A5-F3B0-A4F9-1D71-A5BF36443F52
		propertyList
				.add("'2017-03-13T16:26:08.707+0000','2017-03-13T16:27:21.739+0000',{'esc:dataConsumed':117138848,'esc:dataProduced':452558,'prov:label':'Join','prov:type':['prov:Activity','provone:Execution','esc:blockExecution']}.");
		// Attributes for
		// esc:svi-esc/invocation/132076/block/82B4F4C6-47EF-2AEF-B49B-B67DB40B2303
		propertyList
				.add("'2017-03-13T16:25:51.133+0000','2017-03-13T16:26:08.295+0000',{'esc:dataConsumed':0,'esc:dataProduced':116773551,'prov:label':'CSVImport','prov:type':['prov:Activity','provone:Execution','esc:blockExecution']}.");
		
		// Attributes for esc:svi-esc/invocation/132076/block/03D691E5-0AF3-D9A4-C9D1-4E3AC66C7B90
		propertyList
		.add("'esc:svi-esc/invocation/132076/block/03D691E5-0AF3-D9A4-C9D1-4E3AC66C7B90','2017-03-13T16:25:29.279+0000','2017-03-13T16:25:47.629+0000',{'esc:dataConsumed':20045125,'esc:dataProduced':365297,'prov:label':'Join','prov:type':['prov:Activity','provone:Execution','esc:blockExecution']}.");
		
		// Attributes for esc:svi-esc/invocation/132076/block/C6D21A25-ED33-EB7E-5732-99603181330C
		propertyList.add("'2017-03-13T16:24:51.293+0000','2017-03-13T16:24:56.945+0000',{'esc:dataConsumed':3370301,'esc:dataProduced':96167,'prov:label':'Where','prov:type':['prov:Activity','provone:Execution','esc:blockExecution']}.");
				
		// Attributes for esc:svi-esc/invocation/132076/block/36ED8706-6AE1-83E0-97D8-EB10CD8080FC
		propertyList.add("'2017-03-13T16:24:47.415+0000','2017-03-13T16:24:50.987+0000',{'esc:dataConsumed':0,'esc:dataProduced':3370301,'prov:label':'CSVImport','prov:type':['prov:Activity','provone:Execution','esc:blockExecution']}.");
		
		// Attributes for esc:svi-esc/invocation/132076/block/DF7E5B8F-5986-A516-5A78-00F45AAD393D
		propertyList.add("'2017-03-13T16:25:14.253+0000','2017-03-13T16:25:21.969+0000',{'esc:dataConsumed':38516550,'esc:dataProduced':19951170,'prov:label':'Where','prov:type':['prov:Activity','provone:Execution','esc:blockExecution']}.");
		
		// Attributes for "esc:svi-esc/invocation/132076/block/BFF867C8-81FF-BAD2-4CEF-6F6DC6F0769A");
		propertyList.add("'2017-03-13T16:25:00.481+0000','2017-03-13T16:25:14.063+0000',{'esc:dataConsumed':0,'esc:dataProduced':38516550,'prov:label':'CSVImport','prov:type':['prov:Activity','provone:Execution','esc:blockExecution']}.");
				
		List<org.neo4j.graphdb.Node> neoActivityNodeList = new ArrayList<>();
		for (int i = 0; i < activityNodes.size(); i++) {
			org.neo4j.graphdb.Node activityNode = App.graphDB
					.createNode(NodeType1.activity);
			Label label1 = DynamicLabel.label(activityNodes.get(i).toString());
			activityNode.addLabel(label1);
			activityNode.setProperty("id", activityNodes.get(i).toString());
			activityNode.setProperty("attributes", propertyList.get(i)
					.toString());
			neoActivityNodeList.add(activityNode);
		}
		return neoActivityNodeList;
		// TODO Auto-generated method stub
	}
}
