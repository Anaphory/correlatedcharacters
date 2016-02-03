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

package correlatedcharacters;

import java.util.ArrayList;
import java.util.Arrays;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.junit.runners.Parameterized;
import org.junit.runners.Parameterized.Parameters;

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
@RunWith(Parameterized.class)
public class CorrelatedSubstitutionModelTest extends TestCase {

	Integer[] shape;
	Double[] rates;
	Frequencies freqs;
	Double[] expectedResult;

	public CorrelatedSubstitutionModelTest(Integer[] shapeV, Double[] ratesV, Double[] freqsV, Double[] expectedResultV)
			throws Exception {
		this.shape = shapeV;
		this.rates = ratesV;
		freqs = new Frequencies();
		freqs.initByName("frequencies", new RealParameter(freqsV), "estimate", false);
		this.expectedResult = expectedResultV;
	}

	private class CSMwithPublicMatrix extends CorrelatedSubstitutionModel {
		public double[][] getMatrix() {
			setupRelativeRates();
			setupRateMatrix();
			return getRateMatrix();
		}
	}

	@Parameters
	public static Iterable<Object[]> data() {
		return Arrays.asList(new Object[][] {
				// [0]
				{ new Integer[] { 2, 2 }, new Double[] { 1., 1., 1., 1., 1., 1., 1., 1. },
						new Double[] { 0.25, 0.25, 0.25, 0.25 },
						new Double[] { -1., 0.5, 0.5, 0.0, 0.5, -1., 0.0, 0.5, 0.5, 0.0, -1., 0.5, 0.0, 0.5, 0.5,
								-1. }, },
				// [1]
				{ new Integer[] { 2, 3 },
						new Double[] { 1., 1., 1., 1., 1., 1., 1., 1., 1., 1., 1., 1., 1., 1., 1., 1., 1., 1. },
						new Double[] { 0.25, 0.25, 0.125, 0.125, 0.125, 0.125 },
						new Double[] { -1.0, 0.5, 0.25, 0.25, 0.0, 0.0, 0.5, -1.0, 0.25, 0.0, 0.25, 0.0, 0.5, 0.5,
								-1.25, 0.0, 0.0, 0.25, 0.5, 0.0, 0.0, -1.0, 0.25, 0.25, 0.0, 0.5, 0.0, 0.25, -1.0, 0.25,
								0.0, 0.0, 0.25, 0.25, 0.25, -0.75 }, },
				// [2]
				{ new Integer[] { 2, 3 },
						new Double[] { 1., 2., 1., 1., 1., 1., 1., 1., 1., 1., 1., 1., 1., 1., 1., 1., 1., 1. },
						new Double[] { 0.1666666666666666666666666666, 0.1666666666666666666666666666,
								0.1666666666666666666666666666, 0.1666666666666666666666666666,
								0.1666666666666666666666666666, 0.1666666666666666666666666666 },
						new Double[] { -1.2631578947368423, 0.6315789473684211, 0.31578947368421056,
								0.31578947368421056, 0.0, 0.0, 0.31578947368421056, -0.9473684210526317,
								0.31578947368421056, 0.0, 0.31578947368421056, 0.0, 0.31578947368421056,
								0.31578947368421056, -0.9473684210526317, 0.0, 0.0, 0.31578947368421056,
								0.31578947368421056, 0.0, 0.0, -0.9473684210526317, 0.31578947368421056,
								0.31578947368421056, 0.0, 0.31578947368421056, 0.0, 0.31578947368421056,
								-0.9473684210526317, 0.31578947368421056, 0.0, 0.0, 0.31578947368421056,
								0.31578947368421056, 0.31578947368421056, -0.9473684210526317 }, },

				// [3]
				{ new Integer[] { 2, 2 }, new Double[] { 2., 1., 4., 3., 5., 6., 7., 8. },
						new Double[] { 0.25, 0.25, 0.25, 0.25 },
						new Double[] { -0.3333333333333333, 0.1111111111111111, 0.2222222222222222, 0.0,
								0.3333333333333333, -0.7777777777777778, 0.0, 0.4444444444444444, 0.5555555555555556,
								0.0, -1.2222222222222223, 0.6666666666666666, 0.0, 0.7777777777777778,
								0.8888888888888888, -1.6666666666666667 }, },
				// [4]
				{ new Integer[] { 2, 2 }, new Double[] { 1., 2., 1., 8., 4., 2., 4., 8. },
						new Double[] { 0.25, 0.25, 0.25, 0.25 },
						new Double[] { -0.4, 0.26666666666666666, 0.13333333333333333, 0.0, 1.0666666666666667, -1.2,
								0.0, 0.13333333333333333, 0.5333333333333333, 0.0, -0.8, 0.26666666666666666, 0.0,
								0.5333333333333333, 1.0666666666666667, -1.6 }, },
				// [5]
				{ new Integer[] { 2, 2 }, new Double[] { 1., 2., 1., 7., 4., 2., 4., 9. },
						new Double[] { 0.25, 0.25, 0.25, 0.25 },
						new Double[] { -0.4, 0.26666666666666666, 0.13333333333333333, 0.0, 0.9333333333333333,
								-1.0666666666666667, 0.0, 0.13333333333333333, 0.5333333333333333, 0.0, -0.8,
								0.26666666666666666, 0.0, 0.5333333333333333, 1.2, -1.7333333333333334 }, },
				// [6]
				{ new Integer[] { 2, 2, 2 },
						new Double[] { 1., 2., 4., 1., 2., 8., 1., 16., 4., 1., 16., 8., 32., 2., 4., 32., 2., 8., 32.,
								16., 4., 32., 16., 8. },
						new Double[] { 0.125, 0.125, 0.125, 0.125, 0.125, 0.125, 0.125, 0.125, },
						new Double[] { -0.2222222222222222, 0.12698412698412698, 0.06349206349206349, 0.0,
								0.031746031746031744, 0.0, 0.0, 0.0, 0.25396825396825395, -0.3492063492063492, 0.0,
								0.06349206349206349, 0.0, 0.031746031746031744, 0.0, 0.0, 0.5079365079365079, 0.0,
								-0.6666666666666666, 0.12698412698412698, 0.0, 0.0, 0.031746031746031744, 0.0, 0.0,
								0.5079365079365079, 0.25396825396825395, -0.7936507936507936, 0.0, 0.0, 0.0,
								0.031746031746031744, 1.0158730158730158, 0.0, 0.0, 0.0, -1.2063492063492063,
								0.12698412698412698, 0.06349206349206349, 0.0, 0.0, 1.0158730158730158, 0.0, 0.0,
								0.25396825396825395, -1.3333333333333333, 0.0, 0.06349206349206349, 0.0, 0.0,
								1.0158730158730158, 0.0, 0.5079365079365079, 0.0, -1.6507936507936507,
								0.12698412698412698, 0.0, 0.0, 0.0, 1.0158730158730158, 0.0, 0.5079365079365079,
								0.25396825396825395, -1.7777777777777777 }, }, });
	}

	@Test
	public void testCSM() throws Exception {
		CSMwithPublicMatrix csm = new CSMwithPublicMatrix();
		csm.initByName("rates", new RealParameter(rates), "frequencies", freqs, "shape", new IntegerParameter(shape));

		final double[][] result = csm.getMatrix();

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

	@Test
	public void testCSMDependencyChecker() throws Exception {
		CSMwithPublicMatrix csm = new CSMwithPublicMatrix();

		int nrOfStates = 1;
		for (int d : shape) {
			nrOfStates *= d;
		}

		csm.initByName("rates", new RealParameter(rates), "frequencies", freqs, "shape", new IntegerParameter(shape));

		final double[][] result = csm.getMatrix();

		for (int component = 0; component < shape.length; ++component) {
			for (int dependsOn = 0; dependsOn < shape.length; ++dependsOn) {
				if (component != dependsOn) {
					System.out.printf("%d depends on %d?\n", component, dependsOn);

					// Calculate explicitly whether the rates are dependent.
					boolean byHand = false;

					// Iterate over all states.
					for (int rateFrom = 0; rateFrom < nrOfStates; ++rateFrom) {
						int[] fromComponents = CompoundDataType.compoundState2componentStates(shape, rateFrom);
						int[] toComponents = Arrays.copyOf(fromComponents, fromComponents.length);

						// Take all alternative values of component, compared to
						// from.
						for (int to = 0; to < shape[component]; ++to) {
							if (to != fromComponents[component]) {

								// This gives us an index in the matrix
								// differing from rateFrom only in component.
								toComponents[component] = to;
								int rateTo = CompoundDataType.componentState2compoundState(shape, toComponents);

								// Together, they give us a rate.
								// Calculate the rate where `component`
								// changes to `to`, in any context.
								double rate = result[rateFrom][rateTo];

								// Look up all the other rates where `component`
								// changes from `from` to `to`, but where
								// `dependsOn` takes all other values.
								// If any of them deviates from the
								// previously calculated rate, the
								// `component` evolution depends on
								// `dependsOn`.
								for (int dependentState = 0; dependentState < shape[dependsOn]; ++dependentState) {
									int[] otherFromComponents = Arrays.copyOf(fromComponents, fromComponents.length);
									otherFromComponents[dependsOn] = dependentState;
									int[] otherToComponents = Arrays.copyOf(toComponents, toComponents.length);
									otherToComponents[dependsOn] = dependentState;
									int otherRateFrom = CompoundDataType.componentState2compoundState(shape,
											otherFromComponents);
									int otherRateTo = CompoundDataType.componentState2compoundState(shape,
											otherToComponents);
									double otherRate = result[otherRateFrom][otherRateTo];
									if (rate != otherRate) {
										System.out.printf("Rate %d=%s,%d=%s (%f) did not match %d=%s,%d=%s (%f)\n",
												rateFrom,
												Arrays.toString(CompoundDataType.compoundState2componentStates(shape,
														rateFrom)),
												rateTo,
												Arrays.toString(
														CompoundDataType.compoundState2componentStates(shape, rateTo)),
												rate, otherRateFrom,
												Arrays.toString(CompoundDataType.compoundState2componentStates(shape,
														otherRateFrom)),
												otherRateTo, Arrays.toString(CompoundDataType
														.compoundState2componentStates(shape, otherRateTo)),
												otherRate);
										byHand = true;
									}
								}
							}
						}
					}
					// Use this to check the CSM method `.depends()`
					boolean fromMethod = csm.depends(component, dependsOn);
					System.out.print(fromMethod);
					System.out.print(" / actually: ");
					System.out.print(byHand);
					System.out.print("\n");
					assertEquals(byHand, fromMethod);
				}
			}
		}
		System.out.print("\n");

	}

	@Test
	public void testCSMwithCDT() throws Exception {
		// Test whether CorrelatedSubstitutionModel plays well with
		// CompoundDataType, i.e. whether they have the same sort order of
		// components, endian-ness etc.
		int nComponents = shape.length;

		// Prepare a CompoundDataType that fits the rates matrix
		ArrayList<StandardData> components = new ArrayList<StandardData>(nComponents);
		for (Integer size : shape) {
			StandardData sd = new StandardData();
			sd.initByName("nrOfStates", size);
			components.add(sd);
		}
		CompoundDataType cdt = new CompoundDataType();
		cdt.initByName("components", components);

		// Generate the rates matrix
		CSMwithPublicMatrix csm = new CSMwithPublicMatrix();
		csm.initByName("rates", new RealParameter(rates), "frequencies", freqs, "shape", new IntegerParameter(shape));

		final double[][] result = csm.getMatrix();

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