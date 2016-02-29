package correlatedcharacters.beauti;

import java.io.File;
import java.util.List;

import beast.app.beauti.BeautiAlignmentProvider;
import beast.app.beauti.BeautiDoc;
import beast.core.BEASTInterface;
import beast.core.parameter.IntegerParameter;
import beast.core.parameter.RealParameter;
import beast.evolution.alignment.Alignment;
import correlatedcharacters.polycharacter.CompoundAlignment;

public class CompoundAlignmentProvider extends BeautiAlignmentProvider {
	@Override
	protected void addAlignments(BeautiDoc doc, List<BEASTInterface> selectedBEASTObjects) {
		for (BEASTInterface beastObject : selectedBEASTObjects) {
			// ensure ID of alignment is unique
			int k = 0;
			String id = beastObject.getID();
			while (doc.pluginmap.containsKey(id)) {
				k++;
				id = beastObject.getID() + k;
			}
			beastObject.setID(id);

			// For each compound alignment, we have a
			// CorrelatedSubstitutionModel, which needs to have its rates
			// defined.
			// So first, we need to introspect the constructed alignment, and
			// see what state counts it contains.

			Integer[] stateCounts = CompoundAlignment.guessSizes((Alignment) beastObject);
			int states = 1;
			int directions = 0;
			for (int count : stateCounts) {
				states *= count;
				directions += count - 1;
			}

			Double[] rates = new Double[states * directions];
			for (int i = 0; i < states * directions; ++i) {
				rates[i] = 0.;
			}
			rates[0] = 1.;
			RealParameter ratesInput = new RealParameter(rates);
			ratesInput.setID("rateValues." + beastObject.getID());
			doc.addPlugin(ratesInput);

			Integer[] groupings = new Integer[states * directions];
			for (int i = 0; i < states * directions; ++i) {
				groupings[i] = 0;
			}
			IntegerParameter groupingsInput = new IntegerParameter(groupings);
			groupingsInput.setID("groupings." + beastObject.getID());
			doc.addPlugin(groupingsInput);

			Integer[] sizes = new Integer[states * directions];
			for (int i = 0; i < states * directions; ++i) {
				sizes[i] = 0;
			}
			sizes[0] = states * directions;
			IntegerParameter sizesInput = new IntegerParameter(sizes);
			sizesInput.setID("sizes." + beastObject.getID());
			doc.addPlugin(sizesInput);
		}
		for (BEASTInterface beastObject : selectedBEASTObjects) {
			sortByTaxonName(((Alignment) beastObject).sequenceInput.get());
			doc.addAlignmentWithSubnet((Alignment) beastObject, getStartTemplate());
		}
	}
}
