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

import beast.core.Description;
import beast.core.Input;
import beast.core.Input.Validate;
import beast.core.parameter.IntegerParameter;
import beast.evolution.substitutionmodel.GeneralSubstitutionModel;

@Description("Specifies transition probability matrix with restrictions on the rates such that "
		+ "one of the is equal to one and the others are specified relative to "
		+ "this unit rate. Works for any number of states.")
public class CorrelatedSubstitutionModel extends GeneralSubstitutionModel {
	public Input<IntegerParameter> shapeInput = new Input<IntegerParameter>(
			"shape", "component parameter dimensions", Validate.REQUIRED);

	@Override
	public void initAndValidate() throws Exception {
		frequencies = frequenciesInput.get();

		updateMatrix = true;
		nrOfStates = 1;
		int nonzeroTransitions = 0;
		for (int shape : shapeInput.get().getValues()) {
			nrOfStates *= shape;
			nonzeroTransitions += shape - 1;
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
	@Override
	protected void setupRateMatrix() {
		double[] fFreqs = frequencies.getFreqs();

		int next = 0;

		for (int i = 0; i < nrOfStates; i++) {
			rateMatrix[i][i] = 0;

			int componentStep = 1;

			for (int component = 0; component < shapeInput.get().getDimension(); ++component) {
				int componentRange = shapeInput.get().getNativeValue(component);
				int valueInThisComponent = (i / componentStep) % componentRange;

				int j = i - componentStep * valueInThisComponent;
				for (int counter = 0; counter < componentRange; ++counter) {
					if (counter != valueInThisComponent) {
						rateMatrix[i][j + counter * componentStep] = relativeRates[next];
						++next;
					}
				}
				componentStep *= componentRange;
			}
		}

		// bring in frequencies
		for (int i = 0; i < nrOfStates; i++) {
			for (int j = i + 1; j < nrOfStates; j++) {
				rateMatrix[i][j] *= fFreqs[j];
				rateMatrix[j][i] *= fFreqs[i];
			}
		}

		// set up diagonal
		for (int i = 0; i < nrOfStates; i++) {
			double fSum = 0.0;
			for (int j = 0; j < nrOfStates; j++) {
				if (i != j)
					fSum += rateMatrix[i][j];
			}
			rateMatrix[i][i] = -fSum;
		}
		// normalise rate matrix to one expected substitution per unit time
		double fSubst = 0.0;
		for (int i = 0; i < nrOfStates; i++)
			fSubst += -rateMatrix[i][i] * fFreqs[i];

		for (int i = 0; i < nrOfStates; i++) {
			for (int j = 0; j < nrOfStates; j++) {
				rateMatrix[i][j] = rateMatrix[i][j] / fSubst;
			}
		}

	} // setupRateMatrix

} // class GeneralSubstitutionModel
