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

import dev.shadowtail.classfile.xlate.JavaStackEnqueueList;
import dev.shadowtail.classfile.xlate.JavaStackState;
import java.util.Collection;
import java.util.LinkedHashMap;
import java.util.Map;
import net.multiphasicapps.classfile.ByteCode;
import net.multiphasicapps.classfile.ClassName;
import net.multiphasicapps.classfile.ExceptionHandlerTable;
import net.multiphasicapps.classfile.InstructionAddressRange;
import net.multiphasicapps.classfile.StackMapTable;

/**
 * This class is used to keep track of the exceptions in the methods.
 *
 * @since 2019/03/21
 */
public final class ExceptionHandlerRanges
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
	public ExceptionHandlerRanges(ByteCode __bc)
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
	 * Creates a enqueue and table representation.
	 *
	 * @param __ops The object positions.
	 * @param __pc The PC address.
	 * @return The stack and table information.
	 * @throws NullPointerException On null arguments.
	 * @since 2019/03/22
	 */
	public final ExceptionEnqueueAndTable enqueueAndTable(
		JavaStackEnqueueList __ops, int __pc)
		throws NullPointerException
	{
		if (__ops == null)
			throw new NullPointerException("NARG");
		
		return new ExceptionEnqueueAndTable(__ops, this.tableOf(__pc));
	}
	
	/**
	 * Creates a class, stack, and exception table representation.
	 *
	 * @param __cn The class name.
	 * @param __ops The stack state.
	 * @param __pc The address.
	 * @return The class, stack, and exception table
	 * @throws NullPointerException On null arguments.
	 * @since 2019/04/03
	 */
	public final ExceptionClassStackAndTable classStackAndTable(
		ClassName __cn, JavaStackState __ops, int __pc)
		throws NullPointerException
	{
		if (__cn == null || __ops == null)
			throw new NullPointerException("NARG");
		
		return new ExceptionClassStackAndTable(__cn,
			this.stackAndTable(__ops, __pc));
	}
	
	/**
	 * Creates a stack and table representation.
	 *
	 * @param __ops The object positions.
	 * @param __pc The PC address.
	 * @return The stack and table information.
	 * @throws NullPointerException On null arguments.
	 * @since 2019/03/22
	 */
	public final ExceptionStackAndTable stackAndTable(JavaStackState __ops,
		int __pc)
		throws NullPointerException
	{
		if (__ops == null)
			throw new NullPointerException("NARG");
		
		return new ExceptionStackAndTable(__ops, this.tableOf(__pc));
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

