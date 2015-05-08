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
import beast.core.Input.Validate;
import beast.evolution.datatype.DataType;
import beast.evolution.datatype.DataType.Base;

/**
 * @author Gereon Kaiping <anaphory@yahoo.de>
 * @since 2015-04-24
 */
@Description("Compound datatype. Represents tuples of values from other data types.")
public class CompoundDataType extends Base implements DataType {
	public Input<List<DataType>> components = new Input<List<DataType>>("", "",
			Validate.REQUIRED);

	int stateCount;

	@Override
	public void initAndValidate () throws Exception {
		stateCount = 1;
		for (DataType comp: components.get()) {
			int states = comp.getStateCount();
			if (states == -1) {
				// A component with infinitely many states will lead to problems.
				throw new Exception("Component data type with infinite number of states encountered");
			}
			stateCount *= states;
		}
	}

	/**
	 * Get the number of states for this data type.
	 * 
	 * @return number of states for this data type. Assuming there is a finite
	 *         number of states, or -1 otherwise.
	 */
	@Override
	public int getStateCount() {
		return stateCount;
	}

	@Override
	public String getTypeDescription() {
		// TODO Auto-generated method stub
		return "compound: <...>";
	}
}
