package Traversal.WhyDiffAlgorithm;

import java.awt.SecondaryLoop;
import java.io.File;
import java.util.List;

public class Proba {
	public void makeGraph(List<String> firstGraph, List<String> firstEnt,
			List<String> firstAct, List<String> secondGraph,
			List<String> secondEnt, List<String> secondAct) {
		Proba p = new Proba();
		p.start(firstGraph, firstEnt, firstAct);
		p.start1(secondGraph, secondEnt, secondAct);
		p.start2(firstGraph, firstEnt, firstAct, secondGraph, secondEnt,
				secondAct);
		// p.start(secondGraph);
		/*
		 * Proba p1 = new Proba(); p1.start(secondGraph);
		 */
		// p.start2();
	}

	public void makeDelta(List<String> firstGraph, List<String> firstAct, List<String> secondAct) {
		Proba p = new Proba();
		p.makeDeltaGraph(firstGraph, firstAct, secondAct);
	}

	private void makeDeltaGraph(List<String> firstGraph, List<String> firstAct, List<String> secondAct) {
		// TODO Auto-generated method stub

		System.out.println("Delta in Proba "+firstGraph.toString());
		System.out.println("First Act in Proba "+firstAct.toString());
		// TODO Auto-generated method stub
		// String a1 = null;
		GraphViz gv = new GraphViz();
		gv.addln(gv.start_graph());

		
		for(String a:firstGraph) {
			 //gv.addln(a+"[shape=box];"); 
			gv.addln(a + "[dir=back][style=filled][fillcolor = mediumspringgreen];");
		}
		 
/*
		for (String a : secondGraph) {
			if (a.contains("a")) {
				gv.addln("node[shape=box]");
			}
			gv.addln(a + "[dir=back][style=filled][fillcolor = hotpink2];");
		}
		*/
		for(String a:firstAct) {
			if(!a.contains("[shape=box]"))
			 gv.addln(a+"[shape=box]"); 
			else
				gv.addln(a);
		}
		for(String a:secondAct) {
			if(!a.contains("fillcolor = pink"))
				 gv.addln(a+"[fillcolor = pink]"); 
				else
			 gv.addln(a); 
		}
		
		
		String a1 = "subgraph cluster0 {a0 -> a1 -> a2 -> a3;}";
		String a2 = "subgraph cluster0 {b0 -> b1 -> b2 -> b3;}";
		String a3 = "start -> a0;start -> b0;a1 -> b3;b2 -> a3;a3 -> a0;a3 -> end;b3 -> end;start [shape=Mdiamond];end [shape=Msquare];";
		gv.addln(a1);
		gv.addln(a2);
		gv.addln(a3);
		
//		for(String a:secondAct) {
//			gv.addln(a+"[shape=box][style=filled][fillcolor = hotpink2]"); 
//		}

		gv.addln(gv.end_graph());
		gv.increaseDpi(); // 106 dpi

		String type = "gif";
		String repesentationType = "dot";
		File out = new File("delta" + "." + type); // Linux
		gv.writeGraphToFile(
				gv.getGraph(gv.getDotSource(), type, repesentationType), out);

	}

	private void start2(List<String> firstGraph, List<String> firstEnt,
			List<String> firstAct, List<String> secondGraph,
			List<String> secondEnt, List<String> secondAct) {
		// TODO Auto-generated method stub
		// String a1 = null;
		GraphViz gv = new GraphViz();
		gv.addln(gv.start_graph());

		
		for (String a : firstAct) {
			gv.addln(a + "[shape=box];");

		}
		for (String a : firstGraph) {
			gv.addln(a + "[dir=back];");

		}

		for (String a : secondAct) {
			gv.addln(a + "[shape=box];");

		}
		for (String a : secondGraph) {
			gv.addln(a + "[dir=back];");

		}
	
		gv.addln(gv.end_graph());
		gv.increaseDpi(); // 106 dpi

		String type = "gif";
		String repesentationType = "dot";
		File out = new File("all" + "." + type); // Linux
		gv.writeGraphToFile(
				gv.getGraph(gv.getDotSource(), type, repesentationType), out);
	}

	/**
	 * Construct a DOT graph in memory, convert it to image and store the image
	 * in the file system.
	 * 
	 * @param deltaStringList
	 */
	public void start(List<String> firstGraph, List<String> firstEnt,
			List<String> firstAct) {
		// String a1 = null;
		GraphViz gv = new GraphViz();
		gv.addln(gv.start_graph());
		for (String a : firstAct) {
			gv.addln(a + "[shape=box];");

		}
		for (String a : firstGraph) {
			gv.addln(a + "[dir=back];");

		}
		
		gv.addln(gv.end_graph());
		gv.increaseDpi(); // 106 dpi

		String type = "gif";
		String repesentationType = "dot";
		File out = new File("out" + "." + type); // Linux
		gv.writeGraphToFile(
				gv.getGraph(gv.getDotSource(), type, repesentationType), out);
	}

	/**
	 * Construct a DOT graph in memory, convert it to image and store the image
	 * in the file system.
	 * 
	 * @param deltaStringList
	 */
	public void start1(List<String> secondGraph, List<String> secondEnt,
			List<String> secondAct) {
		// String a1 = null;
		GraphViz gv = new GraphViz();
		gv.addln(gv.start_graph());
		for (String a : secondAct) {
			gv.addln(a + "[shape=box];");

		}
		for (String a : secondGraph) {
			gv.addln(a + "[dir=back];");

		}
		gv.addln(gv.end_graph());
		gv.increaseDpi(); // 106 dpi

		String type = "gif";
		String repesentationType = "dot";
		File out = new File("out1" + "." + type); // Linux
		gv.writeGraphToFile(
				gv.getGraph(gv.getDotSource(), type, repesentationType), out);
	}
}
