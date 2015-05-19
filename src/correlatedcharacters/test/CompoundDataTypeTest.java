package correlatedcharacters.test;

import junit.framework.TestCase;
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
		Nucleotide n = new Nucleotide();
		n.initByName();
		return n;
	}

	public void testCompoundDataType() throws Exception {
		CompoundDataType compound = null;
		compound = new CompoundDataType();
		DataType d0 = datatype0();
		DataType d1 = datatype1();
		compound.initByName("components", d0, "components", d1);
		assertEquals(compound.getStateCount(),
				d0.getStateCount() * d1.getStateCount());
	}

}
