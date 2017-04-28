package org.projog.content_generated_at_runtime;
import org.projog.core.udp.compiler.*;
import org.projog.core.udp.interpreter.*;
import org.projog.core.udp.*;
import org.projog.core.term.*;
import org.projog.core.*;
import static org.projog.core.term.NumericTermComparator.NUMERIC_TERM_COMPARATOR;
// testRule/1
public final class CompiledPredicate1 implements CompiledPredicate {
   private final boolean initRule0() {
      v0_X = a0;
      return true;
   }
   private final boolean retryRule0() {
      do {
         mainloop:
         switch (conjunctionCtr) {
            case 0:
            if (inlinedCtr0!=0) {
               v0_X = _1;
               v0_Y = _2;
               _0.backtrack();
            } else {
               _0 = v0_X.getTerm();
            }
            if (inlinedCtr0==0) {
               if (StaticUserDefinedPredicateFactory_2.spyPoint.isEnabled()) {
                  StaticUserDefinedPredicateFactory_2.spyPoint.logCall(StaticUserDefinedPredicateFactory_2, new Term[]{v0_X, v0_Y});
               }
            } else {
               if (StaticUserDefinedPredicateFactory_2.spyPoint.isEnabled()) {
                  StaticUserDefinedPredicateFactory_2.spyPoint.logRedo(StaticUserDefinedPredicateFactory_2, new Term[]{v0_X, v0_Y});
               }
            }
            do {
               if (inlinedCtr0>8) {
                  inlinedCtr0 = 0;
                  if (StaticUserDefinedPredicateFactory_2.spyPoint.isEnabled()) {
                     StaticUserDefinedPredicateFactory_2.spyPoint.logFail(StaticUserDefinedPredicateFactory_2, new Term[]{v0_X, v0_Y});
                  }
                  return false;
               }
               final Term[] datainlinedCtr0 = StaticUserDefinedPredicateFactory_2.data[inlinedCtr0++];
               if (_0.unify(datainlinedCtr0[0])==false) {
                  _0.backtrack();
               }
               else
               {
                  v0_Y = datainlinedCtr0[1];
                  if (StaticUserDefinedPredicateFactory_2.spyPoint.isEnabled()) {
                     StaticUserDefinedPredicateFactory_2.spyPoint.logExit(StaticUserDefinedPredicateFactory_2, new Term[]{v0_X, v0_Y});
                  }
                  break;
               }
            } while (true);
            _1 = v0_X;
            v0_X = v0_X.getTerm();
            _2 = v0_Y;
            v0_Y = v0_Y.getTerm();
            if (NUMERIC_TERM_COMPARATOR.compare(Structure.createStructure("mod", new Term[]{v0_Y.getTerm(), INTEGER_2}), INTEGER_0, c)!=0) {
               break mainloop;
            }
            return true;
         }
      } while (true);
   }
   public CompiledPredicate1(final KnowledgeBase _kb) {
      c = KnowledgeBaseUtils.getCalculatables(_kb);
      s = KnowledgeBaseUtils.getSpyPoints(_kb).getSpyPoint(new PredicateKey("testRule", 1));
      d = false;
      StaticUserDefinedPredicateFactory_2 = (org.projog.core.udp.MultipleRulesWithMultipleImmutableArgumentsPredicate)((StaticUserDefinedPredicateFactory)_kb.getPredicateFactory(new PredicateKey("test", 2))).getActualPredicateFactory();
   }
   CompiledPredicate1(final Term in0) {
      a0 = in0.getTerm();
      d = s.isEnabled();
   }
   private final void backtrack() {
      a0.backtrack();
   }
   public final boolean evaluate(final Term... args) {
      if (d) {
         if (clauseCtr==0 && !isRetrying) {
            if (d) {
               s.logCall(this, new Term[]{a0});
            }
         } else {
            if (d) {
               s.logRedo(this, new Term[]{a0});
            }
         }
      }
      if (isRetrying) {
         if (retryRule0()) {
            if (d) {
               s.logExit(this, new Term[]{a0});
            }
            return true;
         }
         isRetrying = false;
      } else {
         if (initRule0() && retryRule0()) {
            isRetrying = true;
            if (d) {
               s.logExit(this, new Term[]{a0});
            }
            return true;
         }
      }
      if (d) {
         s.logFail(this, new Term[]{a0});
      }
      return false;
   }
   public final boolean isRetryable() {
      return true;
   }
   public final boolean couldReEvaluationSucceed() {
      if (inlinedCtr0<9) {
         return true;
      }
      return false;
   }
   public final void setKnowledgeBase(KnowledgeBase kb) {
      throw new RuntimeException();
   }
   public final Predicate getPredicate(final Term... termArgs) {
      return new CompiledPredicate1(termArgs[0]);
   }
   private static final Term INTEGER_2 = new IntegerNumber(2L);
   private static final Term INTEGER_0 = new IntegerNumber(0L);
   private static Calculatables c;
   private static org.projog.core.udp.MultipleRulesWithMultipleImmutableArgumentsPredicate StaticUserDefinedPredicateFactory_2;
   private static SpyPoints.SpyPoint s;
   private final boolean d;
   private boolean isRetrying;
   private int clauseCtr;
   private int conjunctionCtr;
   private Term a0;
   private Term v0_X;
   private Term v0_Y;
   private Term _0;
   private Term _1;
   private Term _2;
   private int inlinedCtr0;
}
