package test.correlatedcharacters;

import junit.framework.TestCase;
import beast.core.Input;
import beast.core.Input.Validate;
import beast.evolution.alignment.Alignment;
import beast.evolution.alignment.Sequence;
import beast.evolution.datatype.UserDataType;
import correlatedcharacters.polycharacter.CompoundAlignment;
import correlatedcharacters.polycharacter.CompoundDataType;

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
		Sequence one = new Sequence("0", "0");
		Sequence two = new Sequence("1", "1");
		Sequence three = new Sequence("2", "0");
		Sequence four = new Sequence("3", "1");

		Alignment data = new Alignment();
		data.initByName("sequence", one, "sequence", two, "sequence", three,
				"sequence", four, "dataType", "binary");
		return data;
	}

	static public Alignment alignment2() throws Exception {
		Sequence one = new Sequence("0", "Y");
		Sequence two = new Sequence("1", "Z");
		Sequence three = new Sequence("2", "X");
		Sequence four = new Sequence("3", "Y");

		UserDataType ternary = new UserDataType();
		ternary.initByName("states", 3, "codelength", 1, "codeMap",
				"X=0, Y=1, Z=2");
		Alignment data = new Alignment();
		data.initByName("sequence", one, "sequence", two, "sequence", three,
				"sequence", four, "userDataType", ternary);
		return data;
	}

	public void testCompoundAlignment() throws Exception {
		// Test whether basic functionality (data type size, manual recovery of
		// component-wise values) is guaranteed.
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

	public void testRecoverCompoundAlignment() throws Exception {
		// Test whether CompoundDataType.compoundState2componentState can
		// recover the patterns from individual alignments.
		CompoundAlignment compound = null;
		compound = new CompoundAlignment();
		Alignment a0 = alignment0();
		Alignment a1 = alignment1();
		Alignment a2 = alignment2();
		compound.initByName("alignments", a0, "alignments", a1, "alignments",
				a2);

		CompoundDataType c = (CompoundDataType) compound.getDataType();

		for (int taxon = 0; taxon < compound.getTaxonCount(); ++taxon) {
			int p = compound.getPattern(taxon, 0);
			for (int component = 0; component < c.getComponentCount(); ++component) {
				Alignment a = compound.getAlignments().get(component);
				assertEquals(a.getPattern(taxon, 0),
						c.compoundState2componentState(p, component));
			}
		}
	}
}
