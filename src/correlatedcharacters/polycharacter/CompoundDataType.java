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

import java.util.List;

import beast.core.Description;
import beast.core.Input;
import beast.evolution.datatype.DataType;

/**
 * @author Gereon Kaiping <anaphory@yahoo.de>
 * @since 2015-04-24
 */
@Description("Compound datatype. Represents tuples of values from other data types.")
public class CompoundDataType implements DataType {
	public Input<List<Integer>> components = new Input<List<Integer>>("value", "Component data types for this compound"); 

	@Override
	public int getStateCount() {
		// TODO Auto-generated method stub
		return 0;
	}

	@Override
	public List<Integer> string2state(String sSequence) throws Exception {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public String state2string(List<Integer> nStates) throws Exception {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public String state2string(int[] nStates) throws Exception {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public boolean[] getStateSet(int iState) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public int[] getStatesForCode(int iState) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public boolean isAmbiguousState(int state) {
		// TODO Auto-generated method stub
		return false;
	}

	@Override
	public boolean isStandard() {
		// TODO Auto-generated method stub
		return false;
	}

	@Override
	public String getTypeDescription() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public char getChar(int state) {
		// TODO Auto-generated method stub
		return 0;
	}

	@Override
	public String getCode(int state) {
		// TODO Auto-generated method stub
		return null;
	}
	
}
