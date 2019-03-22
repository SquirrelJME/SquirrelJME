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

import java.util.Collection;
import java.util.LinkedHashMap;
import java.util.Map;
import net.multiphasicapps.classfile.ByteCode;
import net.multiphasicapps.classfile.ExceptionHandlerTable;
import net.multiphasicapps.classfile.InstructionAddressRange;
import net.multiphasicapps.classfile.StackMapTable;

/**
 * This class is used to keep track of the exceptions in the methods.
 *
 * @since 2019/03/21
 */
final class __ExceptionTracker__
{
	/** The full table. */
	protected final ExceptionHandlerTable full;
	
	/** Ranges for exceptions mapped to specific ranges. */
	private final Map<InstructionAddressRange, ExceptionHandlerTable> _ranges;
	
	/**
	 * Initializes the exception tracker.
	 *
	 * @param __bc The source byte code.
	 * @throws NullPointerException On null arguments.
	 * @since 2019/03/21
	 */
	__ExceptionTracker__(ByteCode __bc)
		throws NullPointerException
	{
		if (__bc == null)
			throw new NullPointerException("NARG");
		
		// Get the complete exception handler table
		ExceptionHandlerTable full = __bc.exceptions();
		this.full = full;
		
		// Load the full exception table ranges, this is used to locate
		// which table belongs to what
		this._ranges = full.mappedUniqueRanges();
		
		// Debug
		todo.DEBUG.note("EHRanges: %s", this._ranges);
	}
	
	/**
	 * Returns the table at the given PC address.
	 *
	 * @param __pc The address to get.
	 * @return The table at the given address.
	 * @since 2019/03/22
	 */
	public final ExceptionHandlerTable tableOf(int __pc)
	{
		ExceptionHandlerTable last = null;
		for (Map.Entry<InstructionAddressRange, ExceptionHandlerTable> e :
			this._ranges.entrySet())
		{
			if (e.getKey().inRange(__pc))
				return e.getValue();
			last = e.getValue();
		}
		
		// Always return the last table because the exception handlers might
		// not cover the entire method (in the case of where there are no
		// handlers, the blank table will be only at address 0 while the
		// remaining instructions are the end)
		return last;
	}
}

