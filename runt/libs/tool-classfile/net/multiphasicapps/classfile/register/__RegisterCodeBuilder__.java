// -*- Mode: Java; indent-tabs-mode: t; tab-width: 4 -*-
// ---------------------------------------------------------------------------
// Multi-Phasic Applications: SquirrelJME
//     Copyright (C) Stephanie Gawroriski <xer@multiphasicapps.net>
//     Copyright (C) Multi-Phasic Applications <multiphasicapps.net>
// ---------------------------------------------------------------------------
// SquirrelJME is under the GNU General Public License v3+, or later.
// See license.mkd for licensing and copyright information.
// ---------------------------------------------------------------------------

package net.multiphasicapps.classfile.register;

import java.util.Arrays;
import java.util.LinkedHashMap;
import java.util.Map;

/**
 * This is used to build {@link RegisterCode} and add instructions to it.
 *
 * @since 2019/03/16
 */
final class __RegisterCodeBuilder__
{
	/** Temporary instruction layout. */
	final Map<Integer, __TempInstruction__> _instructions =
		new LinkedHashMap<>();
	
	/** Label positions. */
	final Map<__Label__, Integer> _labels =
		new LinkedHashMap<>();
	
	/** Next address to use. */
	int _nextaddr;
	
	/**
	 * Initializes the code builder at the default start address.
	 *
	 * @since 2019/03/22
	 */
	__RegisterCodeBuilder__()
	{
		this._nextaddr = 0;
	}
	
	/**
	 * Initializes the code builder at the given start address.
	 *
	 * @param __pc The address to start at.
	 * @since 2019/03/22
	 */
	__RegisterCodeBuilder__(int __pc)
	{
		this._nextaddr = __pc;
	}
	
	/**
	 * Adds a new instruction.
	 *
	 * @param __op The operation to add.
	 * @param __args The arguments to the operation.
	 * @return The resulting temporary instruction.
	 * @since 2019/03/16
	 */
	public final __TempInstruction__ add(int __op, Object... __args)
	{
		// Create instruction
		int atdx;
		__TempInstruction__ rv = new __TempInstruction__(
			(atdx = this._nextaddr++), __op, __args);
		
		// Debug
		todo.DEBUG.note("@%d -> %s %s", atdx,
			RegisterOperationMnemonics.toString(__op), Arrays.asList(__args));
		
		// Store and return the instruction, it will have the address
		this._instructions.put(atdx, rv);
		return rv;
	}
	
	/**
	 * Builds the register code, all references to other portions in the
	 * code itself are resolved.
	 *
	 * @return The built register code.
	 * @since 2019/03/22
	 */
	public final RegisterCode build()
	{
		throw new todo.TODO();
	}
	
	/**
	 * Adds a label at the current position.
	 *
	 * @param __lo The locality.
	 * @param __dx The index.
	 * @return The added label.
	 * @since 2019/03/22
	 */
	public final __Label__ label(String __lo, int __dx)
	{
		return this.label(new __Label__(__lo, __dx), this._nextaddr);
	}
	
	/**
	 * Adds a label.
	 *
	 * @param __lo The locality.
	 * @param __dx The index.
	 * @param __pc The address to target.
	 * @return The added label.
	 * @since 2019/03/22
	 */
	public final __Label__ label(String __lo, int __dx, int __pc)
	{
		return this.label(new __Label__(__lo, __dx), __pc);
	}
	
	/**
	 * Adds a label at the current position.
	 *
	 * @param __l The label to add.
	 * @param __pc The address to target.
	 * @return The added label.
	 * @since 2019/03/22
	 */
	public final __Label__ label(__Label__ __l)
	{
		return this.label(__l, this._nextaddr);
	}
	
	/**
	 * Adds a label.
	 *
	 * @param __pc The address to target.
	 * @return The added label.
	 * @since 2019/03/22
	 */
	public final __Label__ label(__Label__ __l, int __pc)
	{
		// Debug
		todo.DEBUG.note("Label %s -> @%d", __l, __pc);
		
		// Add
		this._labels.put(__l, __pc);
		return __l;
	}
}

