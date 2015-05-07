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

import junit.framework.TestCase;
import beast.core.Description;
import beast.core.parameter.IntegerParameter;
import beast.core.parameter.RealParameter;
import beast.evolution.substitutionmodel.Frequencies;

/**
 * Test CorrelatedSubstitution matrix construction
 *
 */
@Description("Test CorrelatedSubstitution matrix construction")
public class CSMTest extends TestCase {

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
			return new double[] { -1., 0.5, 0.5, 0.0, 0.5, -1., 0.0, 0.5, 0.5,
					0.0, -1., 0.5, 0.0, 0.5, 0.5, -1. };
		}
	};

	Instance[] all = { test0 };

	public void testCSM() throws Exception {
		for (Instance test : all) {

			Frequencies freqs = new Frequencies();
			freqs.initByName("frequencies", new RealParameter(test.getFreqs()),
					"estimate", false);

			CSMwithPublicMatrix csm = new CSMwithPublicMatrix();
			RealParameter rates = new RealParameter(test.getRates());

			IntegerParameter shape = new IntegerParameter(test.getShape());

			csm.initByName("rates", rates, "frequencies", freqs, "shape", shape);

			final double[][] result = csm.getMatrix();
			final double[] expectedResult = test.getExpectedResult();

			int i = 0;
			for (int k = 0; k < result.length; ++k) {
				for (int l = 0; l < result[k].length; ++l) {
					assertEquals(expectedResult[i], result[k][l], 1e-10);
					System.out.println(i + " : "
							+ (result[k][l] - expectedResult[i]));
					++i;
				}
			}
			assertEquals(i, expectedResult.length);
		}
	}
}