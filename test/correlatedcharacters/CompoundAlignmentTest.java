package correlatedcharacters;

import junit.framework.TestCase;

import java.util.ArrayList;
import java.util.List;

import beast.evolution.alignment.Alignment;
import beast.evolution.alignment.Sequence;
import beast.evolution.datatype.DataType;
import beast.evolution.datatype.StandardData;
import correlated.polycharacter.CompoundAlignment;
import correlated.polycharacter.CompoundDataType;

public class CompoundAlignmentTest extends TestCase {
	static public Alignment alignment0() {
		Sequence zer, one, two, thr, fou;
		try {
			zer = new Sequence("zer", "1011");
			one = new Sequence("one", "0000");
			two = new Sequence("two", "0101");
			thr = new Sequence("thr", "0101");
			fou = new Sequence("fou", "1001");
		} catch (Exception e) {
			throw (RuntimeException) e;
		}
		
		Alignment data = new Alignment();
		try {
			data.initByName("sequence", zer, "sequence", one, "sequence", two, "sequence", thr, "sequence", fou,
					"dataType", "standard", "id", "Alignment0");
		} catch (Exception e) {
			throw (RuntimeException) e;
		}
		return data;
	}

	static public CompoundDataType datatype0() {
		Alignment alignment0 = alignment0();
		List<DataType> types = new ArrayList<DataType>();
		Integer[] sizes = new Integer[alignment0.getSiteCount()];
		DataType standard = new StandardData();
		for (int i = 0; i < alignment0.getSiteCount(); ++i) {
			types.add(standard);
			sizes[i] = 2;
		}
		return new CompoundDataType(types, sizes, sizes);
	}

	public void testCompoundAlignment() throws Exception {
		// Test whether basic functionality (data type size, manual recovery of
		// component-wise values) is guaranteed.
		Alignment a0 = alignment0();
		CompoundDataType c = datatype0();
		CompoundAlignment compound = new CompoundAlignment();
		compound.initByName("alignment", a0, "dataType", "userDataType", "userDataType", c);

		assertEquals("The compound alignment should have one column", compound.getSiteCount(), 1);

		Integer expectedStateCount = 1;
		for (int stateCount : c.getStateCounts()) {
			expectedStateCount *= stateCount;
		}
		assertEquals("The state count of the only column should be the product of all `alignment` state counts",
				compound.getStateCounts().get(0), expectedStateCount);
	}

	public void testCompoundAlignmentWithoutDataType() throws Exception {
		// Test whether basic functionality (data type size, manual recovery of
		// component-wise values) is guaranteed.
		Alignment a0 = alignment0();
		CompoundDataType c = datatype0();
		CompoundAlignment compound = new CompoundAlignment();
		compound.initByName("alignment", a0);

		assertEquals("The compound alignment should have one column", 1, compound.getSiteCount());

		Integer expectedStateCount = 1;
		for (int stateCount : c.getStateCounts()) {
			expectedStateCount *= stateCount;
		}
		assertEquals("The state count of the only column should be the product of all `alignment` state counts",
				expectedStateCount, compound.getStateCounts().get(0));
	}

	public void testRecoverCompoundAlignment() throws Exception {
		// Test whether CompoundDataType.compoundState2componentState can
		// recover the patterns from individual alignments.
		CompoundAlignment compound = null;
		compound = new CompoundAlignment();
		Alignment a0 = alignment0();
		CompoundDataType c = datatype0();
		compound.initByName("alignment", a0, "dataType", "userDataType", "userDataType", c);

		for (int taxon = 0; taxon < compound.getTaxonCount(); ++taxon) {
			int p = compound.getPattern(taxon, 0);
			for (int component = 0; component < c.getComponentCount(); ++component) {
				assertEquals(
						String.format("In taxon %s: Site %d incorrectly reconstructed from %d.",
								compound.getTaxaNames().get(taxon), component, p),
						a0.getPattern(taxon, component), c.compoundState2componentState(p, component));
			}
		}
	}
}
