/**
 * 
 */
package correlatedcharacters.polycharacter;

import java.util.ArrayList;
import java.util.List;

import beast.core.Description;
import beast.core.Input;
import beast.core.Input.Validate;
import beast.core.util.Log;
import beast.evolution.alignment.Alignment;
import beast.evolution.alignment.Sequence;
import beast.evolution.datatype.DataType;
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
	public Input<List<Alignment>> alignmentsInput = new Input<List<Alignment>>(
			"alignments", "The component alignments", new ArrayList<Alignment>(), Validate.OPTIONAL);
	protected List<Alignment> alignments;

	@Override
	public void initAndValidate() throws Exception {
		alignments = alignmentsInput.get();
		
		if (dataTypeInput.get() == NUCLEOTIDE) {
			// dataTypeInput has not been set, and therefore has the default
			// value: Construct new data type
			CompoundDataType compoundDataType = new CompoundDataType();
			List<DataType> componentDataTypes = new ArrayList<DataType>(alignments.size());
			for (Alignment alignment : alignments) {
				componentDataTypes.add(alignment.getDataType());
			}
			//TODO: AAAAAAARG
			//FIXME!!
			compoundDataType.initByName("components", componentDataTypes.get(0));
			m_dataType = compoundDataType;
		}


		// Given that we take alignments, we don't need to sort, just to check.
		taxaNames = alignments.get(0).getTaxaNames();
		stateCounts = alignments.get(0).getStateCounts();
		maxStateCount = 1;
		// counts, the list of sequences, starts at everything in state 0.
		for (int i = 0; i < taxaNames.size(); ++i) {
			ArrayList<Integer> zero = new ArrayList<Integer>();
			zero.add(0);
			counts.add(zero);
		}
		for (Alignment alignment : alignments) {
			List<String> otherNames = alignment.getTaxaNames();
			if (otherNames.size() != taxaNames.size()) {
				throw new Exception(
						"Taxa do not match between component alignments");
			} else {
				for (int j = 0; j < taxaNames.size(); ++j) {
					if (taxaNames.get(j) != otherNames.get(j)) {
						throw new Exception(
								"Taxa do not match between component alignments");
					}
				}
			}

			List<Integer> otherStateCounts = alignment.getStateCounts();
			for (int j : otherStateCounts) {
				if (j != 1) {
					throw new Exception(
							"CompoundAlignment only understands singular characters");
				}
			}

			List<List<Integer>> otherCounts = alignment.getCounts();
			for (int j = 0; j < otherCounts.size(); ++j) {
				counts.get(j).set(0,
						counts.get(j).get(0)
						* alignment.getDataType().getStateCount()
						+ otherCounts.get(j).get(0));
			}
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
					throw new Exception(
							"Two sequences with different length found: "
									+ nLength + " != " + seq.size());
				}
			}
		}
		if (siteWeights != null && siteWeights.length != nLength) {
			throw new RuntimeException("Number of weights ("
					+ siteWeights.length + ") does not match sequence length ("
					+ nLength + ")");
		}

		calcPatterns();
		Log.info.println(toString(false));
	}

	static public void sortByTaxonName(List<Sequence> seqs) {
	}
}
