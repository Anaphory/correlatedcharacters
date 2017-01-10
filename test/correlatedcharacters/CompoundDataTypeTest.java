package correlatedcharacters;

import junit.framework.TestCase;
import beast.core.parameter.IntegerParameter;
import beast.evolution.datatype.Binary;
import beast.evolution.datatype.DataType;
import beast.evolution.datatype.Nucleotide;
import correlated.polycharacter.CompoundDataType;

public class CompoundDataTypeTest extends TestCase {

	static public DataType datatype0() {
		Nucleotide n = new Nucleotide();
		n.initByName();
		return n;
	}

	static public DataType datatype1() {
		Binary n = new Binary();
		n.initByName();
		return n;
	}

	public void testSizeCompoundDataType() throws Exception {
		CompoundDataType compound = null;
		compound = new CompoundDataType();
		DataType d0 = datatype0();
		DataType d1 = datatype1();
		compound.initByName("components", d0, "components", d1);
		assertEquals(compound.getStateCount(), d0.getStateCount() * d1.getStateCount());
	}

	public void testCompositionCompoundDataType() throws Exception {
		CompoundDataType compound = null;
		compound = new CompoundDataType();
		DataType d0 = datatype0();
		DataType d1 = datatype1();
		compound.initByName("components", d0, "components", d1, "ambiguities",
				new IntegerParameter(new Integer[] { 0 }));
		int counter = 0;
		for (int i = 0; i < d0.getStateCount(); ++i) {
			for (int j = 0; j < d1.getStateCount(); ++j) {
				assertEquals(i, compound.compoundState2componentState(counter, 0));
				assertEquals(j, compound.compoundState2componentState(counter, 1));
				++counter;
			}
		}
	}

	public void testDecompositionCompoundDataType() {
		CompoundDataType compound = null;
		compound = new CompoundDataType();
		DataType d0 = datatype0();
		DataType d1 = datatype1();
		compound.initByName("components", d0, "components", d1, "ambiguities",
				new IntegerParameter(new Integer[] { 0 }));
		int counter = 0;
		for (int i = 0; i < d0.getStateCount(); ++i) {
			for (int j = 0; j < d1.getStateCount(); ++j) {
				assertEquals(counter, compound.componentState2compoundState(new int[] { i, j }));
				++counter;
			}
		}
	}

	public void testGetStateSetIsCompatibleWithIndexOperations() {
		CompoundDataType compound = null;
		compound = new CompoundDataType();
		DataType d0 = datatype0();
		DataType d1 = datatype1();
		Integer ambiguities = 1;
		compound.initByName("components", d0, "components", d1,
				"ambiguities", new IntegerParameter(new Integer[] { ambiguities }));
		for (int i = 0; i < d0.getStateCount(); ++i) {
			for (int j = 0; j < d1.getStateCount(); ++j) {
				int k = compound.componentState2compoundState(new int[] { i, j });
				assertEquals(true, compound.getStateSet(k)[k]);
			}
		}
	}

	public void testRecompositionCompoundDataType() {
		CompoundDataType compound = null;
		compound = new CompoundDataType();
		DataType d0 = datatype0();
		DataType d1 = datatype1();
		compound.initByName("components", d0, "components", d1);
		for (int i = 0; i < d0.getStateCount(); ++i) {
			for (int j = 0; j < d1.getStateCount(); ++j) {
				assertEquals(i, compound
						.compoundState2componentState(compound.componentState2compoundState(new int[] { i, j }), 0));
				assertEquals(j, compound
						.compoundState2componentState(compound.componentState2compoundState(new int[] { i, j }), 1));
			}
		}
	}
}
