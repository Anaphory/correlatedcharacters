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

import java.util.ArrayList;
import java.util.List;

import beast.core.BEASTObject;
import beast.core.Citation;
import beast.core.Description;
import beast.core.Input;
import beast.core.Input.Validate;
import beast.evolution.datatype.DataType;

/**
 * @author Gereon Kaiping <anaphory@yahoo.de>
 * @since 2015-04-24
 */
@Description("Compound datatype. Represents tuples of values from other data types.")
public class CompoundDataType extends BEASTObject implements DataType {
	public Input<List<DataType>> componentsInput = new Input<List<DataType>>(
			"components", "Component data types for this compound", new ArrayList<DataType>(), Validate.REQUIRED);

	protected List<DataType> components;
	protected List<Integer> stateCounts;
	protected int stateCount = 1;

	@Override
	public void initAndValidate() throws Exception {
		components = componentsInput.get();
		stateCounts = new ArrayList<Integer>(components.size());
		for (DataType t : components) {
			int size = t.getStateCount();
			if (size > 0) {
				stateCount *= size;
				stateCounts.add(size);
			} else {
				throw new Exception("Can only compound finite DataTypes");
			}
		}
	}

	public int compoundState2componentState(int compoundState, int component) {
		for (int i = components.size() - 1; i > component; --i) {
			compoundState /= stateCounts.get(i);
		}
		return compoundState % stateCounts.get(component);
	}
	
	public int getComponentCount() {
		return components.size();
	}
	
	@Override
	public int getStateCount() {
		// TODO Auto-generated method stub
		return stateCount;
	}

    /**
     * Convert a sequence represented by a string into a sequence of integers
     * representing the state for this data type.
     * Ambiguous states should be represented by integer numbers higher than getStateCount()
     * throws exception when parsing error occur *
     */
	@Override
	public List<Integer> string2state(String sSequence) throws Exception {
		throw new Exception("CompoundDataType cannot parse strings");
	}

    /**
     * Convert an array of states into a sequence represented by a string.
     * This is the inverse of string2state()
     * throws exception when State cannot be mapped *
     */
	@Override
	public String state2string(List<Integer> nStates) throws Exception {
		throw new Exception("CompoundDataType cannot parse strings");
	}

	@Override
	public String state2string(int[] nStates) throws Exception {
		throw new Exception("CompoundDataType cannot parse strings");
	}

    /**
     * returns an array of length getStateCount() containing the (possibly ambiguous) states
     * that this state represents.
     */
	@Override
	public boolean[] getStateSet(int iState) {
		boolean[] ans = new boolean[getStateCount()];
		ans[iState] = true;
		return ans;
	}

    /**
     * returns an array with all non-ambiguous states represented by
     * a state.
     */
	@Override
	public int[] getStatesForCode(int iState) {
		return new int[]{iState};
	}

	@Override
	public boolean isAmbiguousState(int state) {
		return false;
	}

    /**
     * true if the class is completely self contained and does not need any
     * further initialisation. Notable exception: GeneralDataype
     */
	@Override
	public boolean isStandard() {
		return false;
	}

	@Override
	public String getTypeDescription() {
		String ans = "Compound of data types";
		for (DataType t : components) {
			ans += " <" + t.getTypeDescription() + ">";
		}
		return ans;
	}

	@Override
	public char getChar(int state) {
        return (char) (state + 'A');
	}

	@Override
	public String getCode(int state) {
		String ans = "";
		int i = 0;
		for (DataType t : components) {
			ans += t.getCode(compoundState2componentState(state, i));
			++i;
			if (i<components.size()) {
				ans+=",";
			}
		}
		return ans;
	}

}
