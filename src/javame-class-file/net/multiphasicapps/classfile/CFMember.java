// -*- Mode: Java; indent-tabs-mode: t; tab-width: 4 -*-
// ---------------------------------------------------------------------------
// Multi-Phasic Applications: SquirrelJME
//     Copyright (C) 2013-2016 Steven Gawroriski <steven@multiphasicapps.net>
//     Copyright (C) 2013-2016 Multi-Phasic Applications <multiphasicapps.net>
// ---------------------------------------------------------------------------
// SquirrelJME is under the GNU Affero General Public License v3+, or later.
// For more information see license.mkd.
// ---------------------------------------------------------------------------

package net.multiphasicapps.classfile;

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
public abstract class CFMember<S extends MemberTypeSymbol,
	F extends CFMemberFlags>
{
	/** Internal lock. */
	Object lock;	
	
	/** The class this member is in. */
	protected final CFClass inclass;
	
	/** Member name and type. */
	protected final CFMemberKey<S> nameandtype;
	
	/** The type that the symbol must be. */
	protected final Class<S> symboltype;
	
	/** The class which flags must be. */
	protected final Class<F> flagcast;
	
	/** Flags used for the member. */
	private volatile F _flags;
	
	/**
	 * Initializes the interpreted member.
	 *
	 * @param __owner The class which owns this.
	 * @param __st The descriptor symbol type.
	 * @param __nat The name and type of the member.
	 * @param __fcl The type of class flags must be.
	 * @throws ClassCastException If the {@code __type} is not a sub-class of
	 * {@code __st}.
	 * @throws NullPointerException On null arguments.
	 * @since 2016/03/17
	 */
	CFMember(CFClass __owner, Class<S> __st, CFMemberKey<S> __nat,
		Class<F> __fcl)
		throws ClassCastException, NullPointerException
	{
		// Check
		if (__owner == null || __st == null || __nat == null ||
			__fcl == null)
			throw new NullPointerException("NARG");
		
		// Set
		inclass = __owner;
		lock = inclass.lock;
		symboltype = __st;
		nameandtype = __nat;
		symboltype.cast(nameandtype.getValue());
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
		return nameandtype.getKey();
	}
	
	/**
	 * Returns the name and type of this member.
	 *
	 * @return The member name and type.
	 * @since 2016/03/20
	 */
	public final CFMemberKey<S> nameAndType()
	{
		return nameandtype;
	}
	
	/**
	 * Returns the owning class.
	 *
	 * @return The class which owns this.
	 * @since 2016/03/17
	 */
	public final CFClass outerClass()
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
	public CFMember<S, F> setFlags(F __fl)
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
		return nameandtype.getValue();
	}
}

