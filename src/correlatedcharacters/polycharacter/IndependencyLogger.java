package correlatedcharacters.polycharacter;

import java.io.PrintStream;
import java.util.List;
import java.util.Random;

import beast.core.BEASTObject;
import beast.core.Description;
import beast.core.Distribution;
import beast.core.Input;
import beast.core.Loggable;
import beast.core.State;
import beast.core.parameter.RealParameter;

@Description("A prior reporting on whether two traits are evolving dependently or independently"
		+ " in a CorrelatedSubstitutionModel.")
public class IndependencyLogger extends BEASTObject implements Loggable {
	public Input<CorrelatedSubstitutionModel> csmInput = new Input<CorrelatedSubstitutionModel>("model",
			"The CorrelatedSubstitutionModel this logger is reporting");
	
	protected Object trueOutput = true; 
	protected Object falseOutput = false; 
	
	@Override
	public void init(PrintStream out) {
		int components = csmInput.get().getShape().length;
		for (int component1 = 0; component1 < components; ++component1) {
			for (int component2 = 0; component2 < component1; ++component2) {
		        out.printf("%s_%d_depends_on_%d\t", getID(), component1, component2);				
		        out.printf("%s_%d_depends_on_%d\t", getID(), component2, component1);				
			}			
		}
	}

	@Override
	public void log(int sample, PrintStream out) {
		CorrelatedSubstitutionModel csm = csmInput.get();
		int components = csmInput.get().getShape().length;
		for (int component1 = 0; component1 < components; ++component1) {
			for (int component2 = 0; component2 < component1; ++component2) {
				if (csm.depends(component1, component2)) {
					out.print(trueOutput);
				} else {
					out.print(falseOutput);
				}
				out.print("\t");				
				if (csm.depends(component2, component1)) {
					out.print(trueOutput);
				} else {
					out.print(falseOutput);
				}
				out.print("\t");				
			}			
		}
		
	}

	@Override
	public void close(PrintStream out) {
        // nothing to do		
	}

	@Override
	public void initAndValidate() {}

}
