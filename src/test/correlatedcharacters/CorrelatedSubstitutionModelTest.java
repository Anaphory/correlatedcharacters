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

package test.correlatedcharacters;

import java.util.ArrayList;
import java.util.Arrays;

import correlatedcharacters.polycharacter.CompoundDataType;
import correlatedcharacters.polycharacter.CorrelatedSubstitutionModel;
import junit.framework.TestCase;
import beast.core.Description;
import beast.core.parameter.IntegerParameter;
import beast.core.parameter.RealParameter;
import beast.evolution.datatype.StandardData;
import beast.evolution.substitutionmodel.Frequencies;

/**
 * Test CorrelatedSubstitution matrix construction
 *
 */
@Description("Test CorrelatedSubstitution matrix construction")
public class CorrelatedSubstitutionModelTest extends TestCase {

	public class CSMwithPublicMatrix extends CorrelatedSubstitutionModel {
		public double[][] getMatrix() {
			setupRelativeRates();
			setupRateMatrix();
			return getRateMatrix();
		}
	}

	public interface Instance {
		Integer[] getShape();

		Double[] getRates();

		Double[] getFreqs();

		double[] getExpectedResult();
	}

	protected Instance test0 = new Instance() {
		// Test a 2x2 compound character with equal rates and frequencies
		public Integer[] getShape() {
			return new Integer[] { 2, 2 };
		}

		public Double[] getRates() {
			return new Double[] { 1., 1., 1., 1., 1., 1., 1., 1. };
		}

		public Double[] getFreqs() {
			return new Double[] { 0.25, 0.25, 0.25, 0.25 };
		}

		public double[] getExpectedResult() {
			return new double[] { -1., 0.5, 0.5, 0.0, 0.5, -1., 0.0, 0.5, 0.5, 0.0, -1., 0.5, 0.0, 0.5, 0.5, -1. };
		}
	};

	protected Instance test1 = new Instance() {
		// Test a 2x2 compound character with equal rates and different
		// frequencies
		public Integer[] getShape() {
			return new Integer[] { 2, 3 };
		}

		public Double[] getRates() {
			return new Double[] { 1., 1., 1., 1., 1., 1., 1., 1., 1., 1., 1., 1., 1., 1., 1., 1., 1., 1. };
		}

		public Double[] getFreqs() {
			return new Double[] { 0.25, 0.25, 0.125, 0.125, 0.125, 0.125 };
		}

		public double[] getExpectedResult() {
			return new double[] { -1.0, 0.5, 0.25, 0.25, 0.0, 0.0, 0.5, -1.0, 0.25, 0.0, 0.25, 0.0, 0.5, 0.5, -1.25,
					0.0, 0.0, 0.25, 0.5, 0.0, 0.0, -1.0, 0.25, 0.25, 0.0, 0.5, 0.0, 0.25, -1.0, 0.25, 0.0, 0.0, 0.25,
					0.25, 0.25, -0.75 };
		}
	};

	protected Instance test2 = new Instance() {
		// Test a 2x3 compound character with equal rates and frequencies
		public Integer[] getShape() {
			return new Integer[] { 2, 3 };
		}

		public Double[] getRates() {
			return new Double[] { 1., 1., 1., 1., 1., 1., 1., 1., 1., 1., 1., 1., 1., 1., 1., 1., 1., 1. };
		}

		public Double[] getFreqs() {
			return new Double[] { 0.25, 0.25, 0.125, 0.125, 0.125, 0.125 };
		}

		public double[] getExpectedResult() {
			return new double[] { -1.0, 0.5, 0.25, 0.25, 0.0, 0.0, 0.5, -1.0, 0.25, 0.0, 0.25, 0.0, 0.5, 0.5, -1.25,
					0.0, 0.0, 0.25, 0.5, 0.0, 0.0, -1.0, 0.25, 0.25, 0.0, 0.5, 0.0, 0.25, -1.0, 0.25, 0.0, 0.0, 0.25,
					0.25, 0.25, -0.75 };
		}
	};

	protected Instance test3 = new Instance() {
		// Test a 2x2 compound character with non-equal rates
		public Integer[] getShape() {
			return new Integer[] { 2, 2 };
		}

		public Double[] getRates() {
			return new Double[] { 1., 1., 1., .5, .5, 1., .5, .5 };
		}

		public Double[] getFreqs() {
			return new Double[] { 0.25, 0.25, 0.25, 0.25 };
		}

		public double[] getExpectedResult() {
			return new double[] { -1.333333333333333333, 0.666666666666666666, 0.666666666666666666, 0.0,
					0.333333333333333333, -1., 0.0, 0.666666666666666666, 0.333333333333333333, 0.0, -1.,
					0.666666666666666666, 0.0, 0.333333333333333333, 0.333333333333333333, -0.666666666666666666 };
		}
	};

	protected Instance test4 = new Instance() {
		// Test a 2x2 dependent compound character with non-equal rates
		public Integer[] getShape() {
			return new Integer[] { 2, 2 };
		}

		public Double[] getRates() {
			return new Double[] { 1., 1., 1., .5, 1., .5, .5, .5 };
		}

		public Double[] getFreqs() {
			return new Double[] { 0.25, 0.25, 0.25, 0.25 };
		}

		public double[] getExpectedResult() {
			return new double[] { -1.333333333333333333, 0.666666666666666666, 0.666666666666666666, 0.0,
					0.333333333333333333, -1., 0.0, 0.666666666666666666, 0.666666666666666666, 0.0, -1.,
					0.333333333333333333, 0.0, 0.333333333333333333, 0.333333333333333333, -0.666666666666666666 };
		}
	};

	protected Instance test5 = new Instance() {
		// Test a 2x2 dependent compound character with non-equal frequencies,
		// where the passed rates make up for the error introduced by the
		// frequencies.
		public Integer[] getShape() {
			return new Integer[] { 2, 2 };
		}

		public Double[] getRates() {
			return new Double[] { 1., 1., 1., .5, 1., .5, .5, .5 };
		}

		public Double[] getFreqs() {
			return new Double[] { 0.25, 0.25, 0.25, 0.25 };
		}

		public double[] getExpectedResult() {
			return new double[] { -1.333333333333333333, 0.666666666666666666, 0.666666666666666666, 0.0,
					0.333333333333333333, -1., 0.0, 0.666666666666666666, 0.666666666666666666, 0.0, -1.,
					0.333333333333333333, 0.0, 0.333333333333333333, 0.333333333333333333, -0.666666666666666666 };
		}
	};

	Instance[] all = { test0, test1, test2, test3, test4 };

	public void testCSM() throws Exception {
		for (Instance test : all) {

			Frequencies freqs = new Frequencies();
			freqs.initByName("frequencies", new RealParameter(test.getFreqs()), "estimate", false);

			CSMwithPublicMatrix csm = new CSMwithPublicMatrix();
			RealParameter rates = new RealParameter(test.getRates());

			IntegerParameter shape = new IntegerParameter(test.getShape());

			csm.initByName("rates", rates, "frequencies", freqs, "shape", shape);

			final double[][] result = csm.getMatrix();
			final double[] expectedResult = test.getExpectedResult();

			System.out.printf("%s\n", Arrays.deepToString(result));
			System.out.printf("%s\n\n", Arrays.toString(expectedResult));

			int i = 0;
			for (int k = 0; k < result.length; ++k) {
				for (int l = 0; l < result[k].length; ++l) {
					assertEquals(expectedResult[i], result[k][l], 1e-10);
					++i;
				}
			}
			assertEquals(i, expectedResult.length);
		}
	}

	public void testCSMDependencyChecker() throws Exception {
		for (Instance test : all) {
			Frequencies freqs = new Frequencies();
			freqs.initByName("frequencies", new RealParameter(test.getFreqs()), "estimate", false);

			CSMwithPublicMatrix csm = new CSMwithPublicMatrix();
			RealParameter rates = new RealParameter(test.getRates());

			Integer[] shape = test.getShape();
			int nrOfStates = 1;
			for (int d : shape) {
				nrOfStates *= d;
			}

			csm.initByName("rates", rates, "frequencies", freqs, "shape", new IntegerParameter(shape));

			final double[][] result = csm.getMatrix();

			for (int component = 0; component < shape.length; ++component) {
				for (int dependsOn = 0; dependsOn < shape.length; ++dependsOn) {
					if (component != dependsOn) {
						// Use the CSM method `.depends()` to judge whether the
						// rates are dependent.

						System.out.printf("%d depends on %d?\n", component, dependsOn);

						boolean fromMethod = csm.depends(component, dependsOn);

						// Now, do the same calculation explicitly.
						// Iterate over all rates where component changes.
						boolean byHand = false;
						for (int rateFrom = 0; rateFrom < nrOfStates; ++rateFrom) {
							int[] fromComponents = CompoundDataType.compoundState2componentStates(shape, rateFrom);
							int[] toComponents = fromComponents.clone();
							for (int to = 0; to < shape[component]; ++to) {
								if (to != fromComponents[component]) {
									toComponents[component] = to;

									int rateTo = CompoundDataType.componentState2compoundState(shape, toComponents);
									// Calculate the rate where `component`
									// changes to `to`, in any context.
									double rate = result[rateFrom][rateTo];

									// Calculate the rates where `component`
									// changes to `to` and – ceteris paribus –
									// `dependsOn` takes all other values.
									// If any of them deviates from the
									// previously calculated rate, the
									// `component` evolution depends on
									// `dependsOn`.
									for (int dependentState = 0; dependentState < shape[dependsOn]; ++dependentState) {
										fromComponents[dependsOn] = dependentState;
										toComponents[dependsOn] = dependentState;
										double otherRate = result[CompoundDataType.componentState2compoundState(shape,
												fromComponents)][CompoundDataType.componentState2compoundState(shape,
														toComponents)];
										if (rate != otherRate) {
											System.out.printf("Rate %d,%d (%f) did not match %d,%d (%f)\n", 
													rateFrom, rateTo, rate,
													CompoundDataType.componentState2compoundState(shape,
															fromComponents),
													CompoundDataType.componentState2compoundState(shape,
															toComponents),
													otherRate);
											byHand = true;
										}
									}
								}
							}
						}
						System.out.print(fromMethod);
						System.out.print(" / ");
						System.out.print(byHand);
						System.out.print("\n");
						assertEquals(fromMethod, byHand);
					}
				}
			}
			System.out.print("\n");
		}
	}

	public void testCSMwithCDT() throws Exception {
		// Test whether CorrelatedSubstitutionModel plays well with
		// CompoundDataType, i.e. whether they have the same sort order of
		// components, endian-ness etc.
		for (Instance test : all) {
			int nComponents = test.getShape().length;

			// Prepare a CompoundDataType that fits the rates matrix
			ArrayList<StandardData> components = new ArrayList<StandardData>(nComponents);
			for (Integer size : test.getShape()) {
				StandardData sd = new StandardData();
				sd.initByName("nrOfStates", size);
				components.add(sd);
			}
			CompoundDataType cdt = new CompoundDataType();
			cdt.initByName("components", components);

			// Generate the rates matrix
			Frequencies freqs = new Frequencies();
			freqs.initByName("frequencies", new RealParameter(test.getFreqs()), "estimate", false);

			CSMwithPublicMatrix csm = new CSMwithPublicMatrix();
			RealParameter rates = new RealParameter(test.getRates());

			IntegerParameter shape = new IntegerParameter(test.getShape());

			csm.initByName("rates", rates, "frequencies", freqs, "shape", shape);

			final double[][] result = csm.getMatrix();

			System.out.printf("%s\n", Arrays.deepToString(result));

			// Using the data type, check the shape of the matrix
			for (int k = 0; k < result.length; ++k) {
				Double diag = null;
				Double rowsum = 0.;
				for (int l = 0; l < result[k].length; ++l) {
					if (k == l) {
						diag = result[k][l];
						// Diagonal entries must be negative
						assertTrue(result[k][l] <= 0);
					} else if (result[k][l] != 0) {
						rowsum += result[k][l];
						// Ascertain that the components corresponding to `k`
						// and `l` differ in precisely one component.
						int hammingDistance = 0;
						for (int component = 0; component < nComponents; ++component) {
							if (cdt.compoundState2componentState(k, component) != cdt.compoundState2componentState(l,
									component)) {
								++hammingDistance;
							}
						}
						assertEquals(1, hammingDistance);
					}
				}
				assertNotNull(diag);
				assertEquals(-diag, rowsum, 1e-10);
			}
		}
	}
}