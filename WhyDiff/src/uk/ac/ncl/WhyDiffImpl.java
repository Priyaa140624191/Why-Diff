package uk.ac.ncl;

import java.io.File;

import org.projog.api.Projog;
import org.projog.api.QueryResult;
import org.projog.api.QueryStatement;
import org.projog.core.term.Atom;

public class WhyDiffImpl {
   public static void main(String[] args) {
	  File f = new File("test1.pl");
      Projog p = new Projog();
      p.consultFile(f);
      QueryStatement s1 = p.query("document(Doc,DocAttrs).");
      QueryResult r1 = s1.getResult();
      while (r1.next()) {
         System.out.println("X = " + r1.getTerm("Doc") + " Y = " + r1.getTerm("DocAttrs"));
      }
//      QueryResult r2 = s1.getResult();
//      r2.setTerm("X", new Atom("d"));
//      while (r2.next()) {
//         System.out.println("Y = " + r2.getTerm("Y"));
//      }
//      
//      QueryStatement s2 = p.query("testRule(X).");
//      QueryResult r3 = s2.getResult();
//      while (r3.next()) {
//         System.out.println("X = " + r3.getTerm("X"));
//      }
//      
//      QueryStatement s3 = p.query("test(X, Y), Y<3.");
//      QueryResult r4 = s3.getResult();
//      while (r4.next()) {
//         System.out.println("X = " + r4.getTerm("X") + " Y = " + r4.getTerm("Y"));
//      }
   }
}