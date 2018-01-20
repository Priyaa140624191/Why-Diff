package Traversal.WhyDiffAlgorithm;

import java.awt.SecondaryLoop;
import java.io.File;
import java.util.List;

public class Proba
{
	public void makeGraph(List<String> firstGraph,List<String> firstEnt, List<String> firstAct, List<String> secondGraph,List<String> secondEnt, List<String> secondAct)
	{
		Proba p = new Proba();
		p.start(firstGraph,firstEnt, firstAct);
		p.start1(secondGraph,secondEnt, secondAct);
		p.start2(firstGraph,firstEnt, firstAct,secondGraph,secondEnt, secondAct);
		//p.start(secondGraph);
		/*Proba p1 = new Proba();
		p1.start(secondGraph);*/
//		p.start2();
	}
	public void makeDelta(List<String> deltaList, List<String> firstAct)
	{
		Proba p = new Proba();
		p.makeDeltaGraph(deltaList, firstAct);
	}
	private void makeDeltaGraph(List<String> deltaList, List<String> firstAct) {
		// TODO Auto-generated method stub

		// TODO Auto-generated method stub
		//String a1 = null;
		GraphViz gv = new GraphViz();
		gv.addln(gv.start_graph());
		for(String a:firstAct)
		{
			gv.addln(a+"[shape=box];");
			
		}
		for(String a:deltaList)
		{
			gv.addln(a+"[dir=back];");
			
		}

		gv.addln(gv.end_graph());
		gv.increaseDpi();   // 106 dpi

		String type = "gif";
		String repesentationType= "dot";
		File out = new File("delta"+"."+ type);   // Linux
		gv.writeGraphToFile( gv.getGraph(gv.getDotSource(), type, repesentationType), out );
	
	}
	private void start2(List<String> firstGraph, List<String> firstEnt,
			List<String> firstAct, List<String> secondGraph,
			List<String> secondEnt, List<String> secondAct) {
		// TODO Auto-generated method stub
		//String a1 = null;
		GraphViz gv = new GraphViz();
		gv.addln(gv.start_graph());
		for(String a:firstAct)
		{
			gv.addln(a+"[shape=box];");
			
		}
		for(String a:firstGraph)
		{
			gv.addln(a+"[dir=back];");
			
		}

		for(String a:secondAct)
		{
			gv.addln(a+"[shape=box];");
			
		}
		for(String a:secondGraph)
		{
			gv.addln(a+"[dir=back];");
			
		}
		
		gv.addln(gv.end_graph());
		gv.increaseDpi();   // 106 dpi

		String type = "gif";
		String repesentationType= "dot";
		File out = new File("all"+"."+ type);   // Linux
		gv.writeGraphToFile( gv.getGraph(gv.getDotSource(), type, repesentationType), out );
	}

	/**
	 * Construct a DOT graph in memory, convert it
	 * to image and store the image in the file system.
	 * @param deltaStringList 
	 */
	public void start(List<String> firstGraph, List<String> firstEnt, List<String> firstAct)
	{
		//String a1 = null;
		GraphViz gv = new GraphViz();
		gv.addln(gv.start_graph());
		for(String a:firstAct)
		{
			gv.addln(a+"[shape=box];");
			
		}
		for(String a:firstGraph)
		{
			gv.addln(a+"[dir=back];");
			
		}

		
		gv.addln(gv.end_graph());
		gv.increaseDpi();   // 106 dpi

		String type = "gif";
		String repesentationType= "dot";
		File out = new File("out"+"."+ type);   // Linux
		gv.writeGraphToFile( gv.getGraph(gv.getDotSource(), type, repesentationType), out );
	}
	
	/**
	 * Construct a DOT graph in memory, convert it
	 * to image and store the image in the file system.
	 * @param deltaStringList 
	 */
	public void start1(List<String> secondGraph, List<String> secondEnt, List<String> secondAct)
	{
		//String a1 = null;
		GraphViz gv = new GraphViz();
		gv.addln(gv.start_graph());
		for(String a:secondAct)
		{
			gv.addln(a+"[shape=box];");
			
		}
		for(String a:secondGraph)
		{
			gv.addln(a+"[dir=back];");
			
		}
		gv.addln(gv.end_graph());
		gv.increaseDpi();   // 106 dpi

		String type = "gif";
		String repesentationType= "dot";
		File out = new File("out1"+"."+ type);   // Linux
		gv.writeGraphToFile( gv.getGraph(gv.getDotSource(), type, repesentationType), out );
	}
}
