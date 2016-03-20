// -*- Mode: Java; indent-tabs-mode: t; tab-width: 4 -*-
// ---------------------------------------------------------------------------
// Multi-Phasic Applications: SquirrelJME
//     Copyright (C) 2013-2016 Steven Gawroriski <steven@multiphasicapps.net>
//     Copyright (C) 2013-2016 Multi-Phasic Applications <multiphasicapps.net>
// ---------------------------------------------------------------------------
// SquirrelJME is under the GNU Affero General Public License v3+, or later.
// For more information see license.mkd.
// ---------------------------------------------------------------------------

package net.multiphasicapps.interpreter;

import java.util.HashSet;
import java.util.Set;
import net.multiphasicapps.collections.MissingCollections;
import net.multiphasicapps.descriptors.IdentifierSymbol;
import net.multiphasicapps.descriptors.MemberTypeSymbol;

/**
 * The base class represents members which exist in classes.
 *
 * @param <S> The type of symbol used for the descriptor.
 * @param <F> The flag type.
 * @since 2016/03/17
 */
public abstract class JVMMember<S extends MemberTypeSymbol,
	F extends JVMMemberFlags>
{
	/** Internal lock. */
	Object lock;	
	
	/** The class this member is in. */
	protected final JVMClass inclass;
	
	/** The type of symbol to use as the descriptor. */
	protected final Class<S> symboltype;
	
	/** The member name. */
	protected final IdentifierSymbol name;
	
	/** The type of the member. */
	protected final S type;
	
	/** The class which flags must be. */
	protected final Class<F> flagcast;
	
	/** Flags used for the member. */
	private volatile F _flags;
	
	/**
	 * Initializes the interpreted member.
	 *
	 * @param __owner The class which owns this.
	 * @param __st The descriptor symbol type.
	 * @param __name The name of the member.
	 * @param __type The type of the member.
	 * @param __fcl The type of class flags must be.
	 * @throws ClassCastException If the {@code __type} is not a sub-class of
	 * {@code __st}.
	 * @throws NullPointerException On null arguments.
	 * @since 2016/03/17
	 */
	JVMMember(JVMClass __owner, Class<S> __st, IdentifierSymbol __name,
		S __type, Class<F> __fcl)
		throws ClassCastException, NullPointerException
	{
		// Check
		if (__owner == null || __st == null || __name == null ||
			__type == null)
			throw new NullPointerException("NARG");
		
		// Set
		inclass = __owner;
		lock = inclass.lock;
		symboltype = __st;
		name = __name;
		type = symboltype.cast(__type);
		flagcast = __fcl;
	}
	
	/**
	 * Returns the flag set.
	 *
	 * @return The flag set.
	 * @throws IllegalStateException If no flags were set.
	 * @since 2016/03/20
	 */
	public final F getFlags()
		throws IllegalStateException
	{
		// Lock
		synchronized (lock)
		{
			// If not set, that is bad
			F rv = _flags;
			if (rv == null)
				throw new IllegalStateException("IN14");
			
			// Return them
			return rv;
		}
	}
	
	/**
	 * Returns the name of this member.
	 *
	 * @return The member name.
	 * @since 2016/03/17
	 */
	public final IdentifierSymbol name()
	{
		return name;
	}
	
	/**
	 * Returns the owning class.
	 *
	 * @return The class which owns this.
	 * @since 2016/03/17
	 */
	public final JVMClass outerClass()
	{
		return inclass;
	}
	
	/**
	 * Sets the flags for this member.
	 *
	 * @param __fl The flags to set.
	 * @return {@code this}.
	 * @throws ClassCastException If the input flags are of the wrong type.
	 * @throws NullPointerException On null arguments.
	 * @since 2016/03/20
	 */
	public final JVMMember<S, F> setFlags(F __fl)
		throws ClassCastException, NullPointerException
	{
		// Check
		if (__fl == null)
			throw new NullPointerException("NARG");
		
		// Lock
		synchronized (lock)
		{
			_flags = flagcast.cast(__fl);
		}
		
		// Self
		return this;
	}
	
	/**
	 * Returns the type of this member.
	 *
	 * @return The member type.
	 * @since 2016/03/17
	 */
	public final S type()
	{
		return type;
	}
}

