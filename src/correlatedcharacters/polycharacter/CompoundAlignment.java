/**
 * 
 */
package correlatedcharacters.polycharacter;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

import beast.evolution.alignment.Alignment;
import beast.evolution.alignment.Sequence;
import beast.core.Citation;
import beast.core.Description;
import beast.core.Input;
import beast.core.Input.Validate;
import beast.core.util.Log;
import beast.evolution.datatype.DataType;
import beast.evolution.datatype.Nucleotide;
import beast.evolution.datatype.StandardData;

/**
 * @author gereon
 *
 */
@Description("Class representing a collection of alignment data")
public class CompoundAlignment extends Alignment {
	// Alignment inputs to be ignored:
	// sequence, statecount, dataType, userDataType
	// 'ascertained' stuff
	// TODO: Construct an abstract base class that does not have these.

	// Consider inputs:
	// stripInvariantSitesInput
	// siteWeightsInput
	public Input<List<Alignment>> alignmentsInput = new Input<List<Alignment>>("alignments", "The component alignments",
			new ArrayList<Alignment>(), Validate.OPTIONAL);
	protected List<Alignment> alignments;

	public CompoundAlignment(List<Alignment> inputs) {
		super();
		initAndValidate(inputs);
	}

	public CompoundAlignment() {
		super();
	}

	private void initAndValidate(List<Alignment> alments) {
		alignments = alments;
		if (dataTypeInput.get() == NUCLEOTIDE) {
			// dataTypeInput has not been set, and therefore has the default
			// value: Construct new data type
			List<DataType> componentDataTypes = new ArrayList<DataType>(alignments.size());
			for (Alignment alignment : alignments) {
				componentDataTypes.add(alignment.getDataType());
			}
			CompoundDataType compoundDataType = new CompoundDataType();

			// FIXME: Once the appropriate methods have been cleaned up, we
			// don't have to catch and reraise as RuntimeException any more.
			try {
				compoundDataType.initByName("components", componentDataTypes);
			} catch (Exception e) {
				throw new IllegalArgumentException(e.getMessage());
			}
			m_dataType = compoundDataType;
		}

		// Given that we take alignments, we don't need to sort, just to check.
		taxaNames = alignments.get(0).getTaxaNames();
		// counts, the list of sequences, starts at everything in state 0.
		// stateCounts, the list of stateCount for each sequence, starts being 1
		// everywhere.
		for (int i = 0; i < taxaNames.size(); ++i) {
			ArrayList<Integer> zero = new ArrayList<Integer>();
			zero.add(0);
			counts.add(zero);
			stateCounts.add(1);
		}
		for (Alignment alignment : alignments) {
			List<String> otherNames = alignment.getTaxaNames();
			if (otherNames.size() != taxaNames.size()) {
				throw new RuntimeException("Taxa do not match between component alignments");
			} else {
				for (int j = 0; j < taxaNames.size(); ++j) {
					if (!taxaNames.get(j).equals(otherNames.get(j))) {
						throw new RuntimeException("Taxa do not match between component alignments: expected "
								+ taxaNames.get(j) + ", found " + otherNames.get(j) + " instead");
					}
				}
			}

			List<Integer> otherStateCounts = alignment.getStateCounts();
			for (int j = 0; j < stateCounts.size(); ++j) {
				stateCounts.set(j, stateCounts.get(j) * otherStateCounts.get(j));
			}

			List<List<Integer>> otherCounts = alignment.getCounts();
			for (int j = 0; j < otherCounts.size(); ++j) {
				counts.get(j).set(0,
						counts.get(j).get(0) * alignment.getDataType().getStateCount() + otherCounts.get(j).get(0));
			}
		}

		maxStateCount = 1;
		for (int stateCount : stateCounts) {
			if (stateCount > maxStateCount) {
				maxStateCount = stateCount;
			}
		}
		if (maxStateCount != m_dataType.getStateCount()) {
			throw new RuntimeException("Size of data type (" + m_dataType.getStateCount() + ") and of alignments ("
					+ maxStateCount + ") do not match");
		}

		if (siteWeightsInput.get() != null) {
			// TODO: Do Something
		}

		// grab data from children
		// Sanity check: make sure sequences are of same length
		int nLength = counts.get(0).size();
		if (!(m_dataType instanceof StandardData)) {
			for (List<Integer> seq : counts) {
				if (seq.size() != nLength) {
					throw new RuntimeException(
							"Two sequences with different length found: " + nLength + " != " + seq.size());
				}
			}
		}
		if (siteWeights != null && siteWeights.length != nLength) {
			throw new RuntimeException(
					"Number of weights (" + siteWeights.length + ") does not match sequence length (" + nLength + ")");
		}

		calcPatterns();
		Log.info.println(toString(false));
	}

	@Override
	public void initAndValidate() {
		initAndValidate(alignmentsInput.get());
	}

	public List<Alignment> getAlignments() {
		return alignments;
	}
}
