/* 
 * Copyright (C) 2015 Gereon Kaiping <gereon.kaiping@soton.ac.uk>
 *
 * This file is part of the BEAST2 package correlatedcharacters.
 *
 * This program is free software: you can redistribute it and/or modify
 * it under the terms of the GNU General Public License as published by
 * the Free Software Foundation, either version 3 of the License, or
 * (at your option) any later version.
 *
 * This program is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU Lesser General Public License for more details.
 *
 * You should have received a copy of the GNU Lesser General Public License
 * along with this program.  If not, see <http://www.gnu.org/licenses/>.
 */
package correlatedcharacters.polycharacter;

import beast.core.Citation;
import beast.core.Description;
import beast.core.Input;
import beast.core.Input.Validate;
import beast.core.parameter.IntegerParameter;
import beast.evolution.substitutionmodel.GeneralSubstitutionModel;

@Description("Specifies transition probability matrix for a collection of multiple characters."
		+ " At every infinitesimal time step, only one component can change values, so some transition rates are 0, the others arbitrary"
		+ " with restrictions on the rates such that"
		+ " one of the is equal to one and the others are specified relative to"
		+ " this unit rate. Works for any number of states.")
@Citation("Pagel, M., Meade, A., 2006."
		+ " Bayesian Analysis of Correlated Evolution of Discrete Characters"
		+ " by Reversible-Jump Markov Chain Monte Carlo."
		+ " The American Naturalist 167, 808--825. doi:10.1086/503444")
public class CorrelatedSubstitutionModel extends GeneralSubstitutionModel {
	public Input<IntegerParameter> shapeInput = new Input<IntegerParameter>(
			"shape", "component parameter dimensions", Validate.REQUIRED);
	// TODO: XOR an alignment, XOR a CompoundDataType
	
	protected Integer[] shape;
	
	@Override
	public void initAndValidate() throws Exception {
		frequencies = frequenciesInput.get();

		shape = shapeInput.get().getValues();
		
		updateMatrix = true;
		nrOfStates = 1;
		int nonzeroTransitions = 0;
		for (int size : shape) {
			nrOfStates *= size;
			nonzeroTransitions += size - 1;
		}

		if (nrOfStates != frequencies.getFreqs().length) {
			throw new Exception("Dimension of input 'frequencies' is "
					+ frequencies.getFreqs().length + " but the "
					+ "shape input gives a total dimension of " + nrOfStates);
		}

		if (ratesInput.get().getDimension() != nrOfStates * nonzeroTransitions) {
			throw new Exception("Dimension of input 'rates' is "
					+ ratesInput.get().getDimension() + " but a "
					+ "rate matrix of dimension " + nrOfStates + "x"
					+ nonzeroTransitions + "=" + nrOfStates
					* nonzeroTransitions + " was " + "expected");
		}

		eigenSystem = createEigenSystem();
		// eigenSystem = new DefaultEigenSystem(m_nStates);

		rateMatrix = new double[nrOfStates][nrOfStates];
		relativeRates = new double[ratesInput.get().getDimension()];
		storedRelativeRates = new double[ratesInput.get().getDimension()];
	} // initAndValidate

	/**
	 * sets up rate matrix *
	 */
	protected void setupRateMatrix() {
		//Reset the rate matrix to zero. This is important, because DefaultEigenSystem overwrites it, and sets some zero entries to non-zero.
		rateMatrix = new double[rateMatrix.length][rateMatrix[0].length];
		
		double[] fFreqs = frequencies.getFreqs();

		int next = 0;

		for (int k = 0; k < rateMatrix.length; ++k) {
			double rowsum = 0.;
			for (int l = 0; l < rateMatrix[k].length; ++l) {
				if (k != l) {
					// Ascertain that the components corresponding to `k`
					// and `l` differ in precisely one component.
					int hammingDistance = 0; 
					for (int component=0; component<shape.length; ++component) {
						if (CompoundDataType.compoundState2componentState(shape, k, component) !=
								CompoundDataType.compoundState2componentState(shape, l, component)) {
							++hammingDistance;
						}
					}
					if (hammingDistance == 1) {
						rateMatrix[k][l] = ratesInput.get().getArrayValue(next);
						rowsum += rateMatrix[k][l];
						++next;
					}
						
				}
			}
		}

		// bring in frequencies
		for (int i = 0; i < nrOfStates; i++) {
			for (int j = i+1; j < nrOfStates; j++) {
				rateMatrix[i][j] *= fFreqs[j];
				rateMatrix[j][i] *= fFreqs[i];
			}
		}
		
		// set the diagonal
		for (int i = 0; i < nrOfStates; i++) {
			double rowsum = 0;
			for (int j = 0; j < nrOfStates; j++) {
				if (j != i) {
					rowsum += rateMatrix[i][j] ;					
				}
			}
			rateMatrix[i][i] = -rowsum;
		}

		// normalise rate matrix to one expected substitution per unit time
		double fSubst = 0.0;
		for (int i = 0; i < nrOfStates; i++) {
			fSubst += -rateMatrix[i][i] * fFreqs[i];
		}
		for (int i = 0; i < nrOfStates; i++) {
			for (int j = 0; j < nrOfStates; j++) {
				rateMatrix[i][j] = rateMatrix[i][j] / fSubst;
			}
		}
		// System.out.println(">" + Arrays.deepToString(rateMatrix));
	} // setupRateMatrix

} // class GeneralSubstitutionModel
