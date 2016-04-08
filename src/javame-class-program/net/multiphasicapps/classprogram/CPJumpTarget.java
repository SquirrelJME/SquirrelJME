// -*- Mode: Java; indent-tabs-mode: t; tab-width: 4 -*-
// ---------------------------------------------------------------------------
// Multi-Phasic Applications: SquirrelJME
//     Copyright (C) 2013-2016 Steven Gawroriski <steven@multiphasicapps.net>
//     Copyright (C) 2013-2016 Multi-Phasic Applications <multiphasicapps.net>
// ---------------------------------------------------------------------------
// SquirrelJME is under the GNU Affero General Public License v3+, or later.
// For more information see license.mkd.
// ---------------------------------------------------------------------------

package net.multiphasicapps.classprogram;

import java.lang.ref.Reference;
import java.lang.ref.WeakReference;
import net.multiphasicapps.descriptors.BinaryNameSymbol;

/**
 * This represents a jump target for an instruction which translates to other
 * instructions within the method.
 *
 * It is either explicit (specified targets), or implied (natural program
 * progression).
 *
 * @since 2016/03/30
 */
public class CPJumpTarget
{
	/** The owning program. */
	protected final CPProgram program;
	
	/** The type of jump. */
	protected final CPJumpType type;
	
	/** Target address. */
	protected final int address;
	
	/** The exception to throw. */
	protected final BinaryNameSymbol exception;
	
	/** String representation cache. */
	private volatile Reference<String> _string;
	
	/**
	 * Initializes the jump target.
	 *
	 * @param __prg The program this jump is within.
	 * @param __type The type of jump to perform.
	 * @param __addr The target address of the jump.
	 * @throws CPProgramException If the address is out of bounds.
	 * @throws NullPointerException On null arguments.
	 * @since 2016/03/30
	 */
	public CPJumpTarget(CPProgram __prg, CPJumpType __type, int __addr)
		throws IllegalArgumentException, CPProgramException,
			NullPointerException
	{
		this(__prg, __type, null, __addr);
	}
	
	/**
	 * Initializes the jump target.
	 *
	 * @param __prg The program this jump is within.
	 * @param __type The type of jump to perform.
	 * @param __bn The name of the exception being thrown.
	 * @param __addr The target address of the jump.
	 * @throws IllegalArgumentException If it is not
	 * exceptional and a binary name was specified, or a binary name was
	 * not specified and this is an exception.
	 * @throws CPProgramException If the address is out of bounds.
	 * @throws NullPointerException On null arguments, except for {@link __bn}
	 * unless the jump type is an exception.
	 * @since 2016/03/30
	 */
	public CPJumpTarget(CPProgram __prg, CPJumpType __type,
		BinaryNameSymbol __bn, int __addr)
		throws IllegalArgumentException, CPProgramException,
			NullPointerException
	{
		// Check
		if (__prg == null || __type == null)
			throw new NullPointerException("NARG");
		
		// {@squirreljme.error CP08 The target jump address is outside of the
		// bounds of the program. (The target address)}
		if (__addr < 0 || __addr >= __prg.size())
			throw new CPProgramException(String.format("CP08 %d", __addr));
		
		// {@squirreljme.error CP09 An exceptional jump target must have the
		// name of the thrown exception, if not an exception no binary name
		// must be specified.}
		if ((__type == CPJumpType.EXCEPTIONAL) != (__bn != null))
			throw new IllegalArgumentException("CP09");
		
		// Set
		program = __prg;
		type = __type;
		exception = __bn;
		address = __addr;
	}
	
	/**
	 * Returns the address to jump to.
	 *
	 * @return The address to jump to.
	 * @since 2016/03/30
	 */
	public int address()
	{
		return address;
	}
	
	/**
	 * Returns the jump type that this jump makes.
	 *
	 * @return The jump type used for this jump.
	 * @since 2016/03/30
	 */
	public CPJumpType getType()
	{
		return type;
	}
	
	/**
	 * {@inheritDoc}
	 * @since 2016/03/30
	 */
	@Override
	public String toString()
	{
		// Get reference
		Reference<String> ref = _string;
		String rv;
		
		// Create?
		if (ref == null || null == (rv = ref.get()))
		{
			// Build it
			StringBuilder sb = new StringBuilder("JT:");
			sb.append(type.name());
			
			// Address
			sb.append('@');
			sb.append(address);
			
			// Throws an exception?
			BinaryNameSymbol x = exception;
			if (x != null)
			{
				sb.append('^');
				sb.append(x);
			}
			
			// Finish it
			_string = new WeakReference<>((rv = sb.toString()));
		}
		
		// Return it
		return rv;
	}
}

