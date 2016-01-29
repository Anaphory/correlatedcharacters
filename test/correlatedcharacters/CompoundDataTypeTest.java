package correlatedcharacters;

import junit.framework.TestCase;
import beast.evolution.datatype.Binary;
import beast.evolution.datatype.DataType;
import beast.evolution.datatype.Nucleotide;
import correlatedcharacters.polycharacter.CompoundDataType;

public class CompoundDataTypeTest extends TestCase {

	static public DataType datatype0() throws Exception {
		Nucleotide n = new Nucleotide();
		n.initByName();
		return n;
	}

	static public DataType datatype1() throws Exception {
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
		assertEquals(compound.getStateCount(),
				d0.getStateCount() * d1.getStateCount());
	}

	public void testCompositionCompoundDataType() throws Exception {
		CompoundDataType compound = null;
		compound = new CompoundDataType();
		DataType d0 = datatype0();
		DataType d1 = datatype1();
		compound.initByName("components", d0, "components", d1);
		int counter = 0;
		for (int i=0; i<d0.getStateCount(); ++i) {
			for (int j=0; j<d1.getStateCount(); ++j) {
				assertEquals(i,
						compound.compoundState2componentState(counter, 0));
				assertEquals(j,
						compound.compoundState2componentState(counter, 1));
				++counter;
			}
		}
	}
	
	public void testDecompositionCompoundDataType() throws Exception {
		CompoundDataType compound = null;
		compound = new CompoundDataType();
		DataType d0 = datatype0();
		DataType d1 = datatype1();
		compound.initByName("components", d0, "components", d1);
		int counter = 0;
		for (int i=0; i<d0.getStateCount(); ++i) {
			for (int j=0; j<d1.getStateCount(); ++j) {
				assertEquals(counter,
						compound.componentState2compoundState(new int[] {i, j}));
				++counter;
			}
		}		
	}
	
	public void testRecompositionCompoundDataType() throws Exception {
		CompoundDataType compound = null;
		compound = new CompoundDataType();
		DataType d0 = datatype0();
		DataType d1 = datatype1();
		compound.initByName("components", d0, "components", d1);
		for (int i=0; i<d0.getStateCount(); ++i) {
			for (int j=0; j<d1.getStateCount(); ++j) {
				assertEquals(i,
						compound.compoundState2componentState(
								compound.componentState2compoundState(new int[] {i, j}),
								0));
				assertEquals(j,
						compound.compoundState2componentState(
								compound.componentState2compoundState(new int[] {i, j}),
								1));
			}
		}		
	}
}
