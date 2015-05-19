package correlatedcharacters.test;

import junit.framework.TestCase;
import beast.evolution.alignment.Alignment;
import beast.evolution.alignment.Sequence;
import correlatedcharacters.polycharacter.CompoundAlignment;

public class CompoundAlignmentTest extends TestCase {

	static public Alignment alignment0() throws Exception {
		Sequence one = new Sequence("0", "A");
		Sequence two = new Sequence("1", "C");

		Alignment data = new Alignment();
		data.initByName("sequence", one, "sequence", two, "dataType",
				"nucleotide");
		return data;
	}

	static public Alignment alignment1() throws Exception {
		Sequence one = new Sequence("0", "G");
		Sequence two = new Sequence("1", "T");

		Alignment data = new Alignment();
		data.initByName("sequence", one, "sequence", two, "dataType",
				"nucleotide");
		return data;
	}

	public void testCompoundAlignment() throws Exception {
		CompoundAlignment compound = null;
		compound = new CompoundAlignment();
		compound.initByName("alignments", alignment0());

		System.out.println(compound.getDataType().getStateCount());
	}

}
