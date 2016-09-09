// -*- Mode: Java; indent-tabs-mode: t; tab-width: 4 -*-
// ---------------------------------------------------------------------------
// Multi-Phasic Applications: SquirrelJME
//     Copyright (C) 2013-2016 Steven Gawroriski <steven@multiphasicapps.net>
//     Copyright (C) 2013-2016 Multi-Phasic Applications <multiphasicapps.net>
// ---------------------------------------------------------------------------
// SquirrelJME is under the GNU General Public License v3+, or later.
// For more information see license.mkd.
// ---------------------------------------------------------------------------

package net.multiphasicapps.squirreljme.classformat;

/**
 * This is the base class for linkage from one method to another member. This
 * information is used by the linker to determine which links can actually be
 * made.
 *
 * @since 2016/09/06
 */
public abstract class ClassMemberLinkage<L extends ClassMemberReference>
{
	/** The source method. */
	protected final ClassMethodReference from;
	
	/** The target field. */
	protected final L to;
	
	/**
	 * Initializes the base linkage details.
	 *
	 * @param __from The source method.
	 * @param __to The target member.
	 * @throws NullPointerException On null arguments.
	 * @since 2016/09/06
	 */
	ClassMemberLinkage(ClassMethodReference __from, L __to)
		throws NullPointerException
	{
		// Check
		if (__from == null || __to == null)
			throw new NullPointerException("NARG");
		
		// Set
		this.from = __from;
		this.to = __to;
	}
	
	/**
	 * {@inheritDoc}
	 * @since 2016/09/06
	 */
	@Override
	public boolean equals(Object __o)
	{
		// Must be the same
		if (!(__o instanceof ClassMemberLinkage))
			return false;
		
		// Cast
		ClassMemberLinkage<?> o = (ClassMemberLinkage<?>)__o;
		return this.from.equals(o.from) && this.to.equals(o.to);
	}
	
	/**
	 * Returns the method which imports the member specified by {@link #to()}.
	 *
	 * @return The method which desires to link to another member.
	 * @since 2016/09/06
	 */
	public final ClassMethodReference from()
	{
		return this.from;
	}
	
	/**
	 * {@inheritDoc}
	 * @since 2016/09/06
	 */
	@Override
	public int hashCode()
	{
		return this.from.hashCode() ^ this.to.hashCode();
	}
	
	/**
	 * Returns the target of the link.
	 *
	 * @return The member which is linked into.
	 * @since 2016/09/06
	 */
	public final L to()
	{
		return this.to;
	}
}

