package parameterclone;

import junit.framework.TestCase;
import parameterclone.helpers.RescaledDirichlet;
import parameterclone.splitandmerge.MergeOperator;
import parameterclone.splitandmerge.SplitOperator;
import beast.core.Distribution;
import beast.core.MCMC;
import beast.core.Operator;
import beast.core.State;
import beast.core.StateNode;
import beast.core.parameter.IntegerParameter;
import beast.core.parameter.RealParameter;
import beast.math.distributions.Prior;

public class UniformTest extends TestCase {
	int length = 5;

	public void testUniformity() throws Exception {
		Double[] m_parameters = new Double[length];
		Integer[] m_indices = new Integer[length];
		Integer[] m_sizes = new Integer[length];
		for (int i = 0; i < length; ++i) {
			m_parameters[i] = 1.;
			m_indices[i] = i;
			m_sizes[i] = 1;
		}
		StateNode parameters = new RealParameter(m_parameters);
		StateNode indices = new IntegerParameter(m_indices);
		StateNode sizes = new IntegerParameter(m_sizes);
		State state = new State();
		state.initByName("stateNode", parameters, "stateNode", indices,
				"stateNode", sizes);

		RescaledDirichlet rescaledDirichlet = new RescaledDirichlet();
		rescaledDirichlet.initByName("sizes", sizes);

		Distribution prior = new Prior();
		prior.initByName("x", parameters, "distr", rescaledDirichlet);

		Operator merger = new MergeOperator();
		merger.initByName("parameters", parameters, "groupings", indices,
				"sizes", sizes, "weight", 1.);
		Operator splitter = new SplitOperator();
		splitter.initByName("parameters", parameters, "groupings", indices,
				"sizes", sizes, "weight", 1.);

		// It would be nice to have a logger that could just write the results
		// into a list, so we can easily check the likelihood that this does
		// indeed form a uniform prior, and show how to analyse results.
		MCMC mcmc = new MCMC();
		mcmc.initByName("chainLength", 100000, "preBurnin", 1, "state", state,
				"distribution", prior, "operator", merger, "operator", splitter);

		mcmc.run();
		throw new RuntimeException("The core of this test remains unimplemented");
	}
}