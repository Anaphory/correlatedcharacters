package correlatedcharacters.test;

import junit.framework.TestCase;
import beast.evolution.alignment.Alignment;
import beast.evolution.alignment.Sequence;
import correlatedcharacters.polycharacter.CompoundAlignment;

public class CompoundAlignmentTest extends TestCase {

	static public Alignment alignment0() throws Exception {
		Sequence one = new Sequence("0", "T");
		Sequence two = new Sequence("1", "G");
		Sequence three = new Sequence("2", "G");
		Sequence four = new Sequence("3", "T");

		Alignment data = new Alignment();
		data.initByName("sequence", one, "sequence", two, "sequence", three,
				"sequence", four, "dataType", "nucleotide");
		return data;
	}

	static public Alignment alignment1() throws Exception {
		Sequence one = new Sequence("0", "G");
		Sequence two = new Sequence("1", "T");
		Sequence three = new Sequence("2", "G");
		Sequence four = new Sequence("3", "T");

		Alignment data = new Alignment();
		data.initByName("sequence", one, "sequence", two, "sequence", three,
				"sequence", four, "dataType", "nucleotide");
		return data;
	}

	public void testCompoundAlignment() throws Exception {
		CompoundAlignment compound = null;
		compound = new CompoundAlignment();
		Alignment a0 = alignment0();
		Alignment a1 = alignment1();
		compound.initByName("alignments", a0, "alignments", a1);

		assertEquals(compound.getDataType().getStateCount(), a0.getDataType()
				.getStateCount() * a1.getDataType().getStateCount());
		assertEquals(compound.getPattern(0)[0], a0.getPattern(0)[0]
				* a1.getDataType().getStateCount() + a1.getPattern(0)[0]);
	}
}
