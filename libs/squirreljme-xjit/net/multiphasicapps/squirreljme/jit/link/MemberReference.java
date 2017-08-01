// -*- Mode: Java; indent-tabs-mode: t; tab-width: 4 -*-
// ---------------------------------------------------------------------------
// Multi-Phasic Applications: SquirrelJME
//     Copyright (C) Stephanie Gawroriski <xer@multiphasicapps.net>
//     Copyright (C) Multi-Phasic Applications <multiphasicapps.net>
// ---------------------------------------------------------------------------
// SquirrelJME is under the GNU General Public License v3+, or later.
// See license.mkd for licensing and copyright information.
// ---------------------------------------------------------------------------

package net.multiphasicapps.squirreljme.jit.link;

import java.lang.ref.Reference;
import java.lang.ref.WeakReference;
import net.multiphasicapps.squirreljme.jit.link.ClassNameSymbol;
import net.multiphasicapps.squirreljme.jit.link.IdentifierSymbol;
import net.multiphasicapps.squirreljme.jit.link.MemberTypeSymbol;

/**
 * This represents a reference to a member.
 *
 * @since 2016/08/14
 */
public abstract class MemberReference
{
	/** The class name. */
	protected final ClassNameSymbol classname;
	
	/** The member name. */
	protected final IdentifierSymbol membername;
	
	/** The member type. */
	protected final MemberTypeSymbol membertype;
	
	/** The string representation. */
	private volatile Reference<String> _string;
	
	/**
	 * Initializes the member reference.
	 *
	 * @param __cn The class name.
	 * @param __mn The member name.
	 * @param __mt the member type.
	 * @throws NullPointerException On null arguments.
	 * @since 2016/08/14
	 */
	MemberReference(ClassNameSymbol __cn, IdentifierSymbol __mn,
		MemberTypeSymbol __mt)
		throws NullPointerException
	{
		// Check
		if (__cn == null || __mn == null || __mt == null)
			throw new NullPointerException("NARG");
		
		// Set
		this.classname = __cn;
		this.membername = __mn;
		this.membertype = __mt;
	}
	
	/**
	 * Returns the class name.
	 *
	 * @return The class name.
	 * @since 2016/08/14
	 */
	public final ClassNameSymbol className()
	{
		return this.classname;
	}
	
	/**
	 * {@inheritDoc}
	 * @since 2016/08/14
	 */
	@Override
	public boolean equals(Object __o)
	{
		// Must be this class
		if (!(__o instanceof MemberReference))
			return false;
		
		// Check
		MemberReference o = (MemberReference)__o;
		return this.classname.equals(o.classname) &&
			this.membername.equals(o.membername) &&
			this.membertype.equals(o.membertype);
	}
	
	/**
	 * {@inheritDoc}
	 * @since 2016/08/14
	 */
	@Override
	public int hashCode()
	{
		return this.classname.hashCode() ^
			this.membername.hashCode() ^
			this.membertype.hashCode();
	}
	
	/**
	 * Returns the member name.
	 *
	 * @return The member name.
	 * @since 2016/08/14
	 */
	public final IdentifierSymbol memberName()
	{
		return this.membername;
	}
	
	/**
	 * Returns the member type.
	 *
	 * @return The member type.
	 * @since 2016/08/14
	 */
	public MemberTypeSymbol memberType()
	{
		return this.membertype;
	}
	
	/**
	 * {@inheritDoc}
	 * @since 2016/08/14
	 */
	@Override
	public final String toString()
	{
		// Get
		Reference<String> ref = this._string;
		String rv;
		
		// Create?
		if (ref == null || null == (rv = ref.get()))
			this._string = new WeakReference<>((rv =
				String.format("{class=%s, name=%s, type=%s}", this.classname,
					this.membername, this.membertype)));
		
		// Return
		return rv;
	}
}

