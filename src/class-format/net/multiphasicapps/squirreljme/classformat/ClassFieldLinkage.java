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

import java.lang.ref.Reference;
import java.lang.ref.WeakReference;

/**
 * This is a link from a method which access a field.
 *
 * @since 2016/09/06
 */
public final class GenericFieldLinkage
	extends ClassMemberLinkage<JITFieldReference>
{
	/** The type of link this is. */
	protected final JITFieldAccessType type;
	
	/** String reference. */
	private volatile Reference<String> _string;
	
	/**
	 * Initializes the link of one method to a field.
	 *
	 * @param __from The source method.
	 * @param __to The target field.
	 * @param __t How the field was accessed.
	 * @throws NullPointerException On null arguments.
	 * @since 2016/09/06
	 */
	public GenericFieldLinkage(JITMethodReference __from,
		JITFieldReference __to, JITFieldAccessType __t)
		throws NullPointerException
	{
		super(__from, __to);
		
		// Check
		if (__t == null)
			throw new NullPointerException("NARG");
		
		// Set
		this.type = __t;
	}
	
	/**
	 * {@inheritDoc}
	 * @since 2016/09/06
	 */
	@Override
	public boolean equals(Object __o)
	{
		// Must match
		if (!(__o instanceof GenericFieldLinkage))
			return false;
		
		// Check super first
		if (!super.equals(__o))
			return false;
		
		return this.type.equals(((GenericFieldLinkage)__o).type);
	}
	
	/**
	 * {@inheritDoc}
	 * @since 2016/09/06
	 */
	@Override
	public int hashCode()
	{
		return super.hashCode() ^ this.type.hashCode();
	}
	
	/**
	 * {@inheritDoc}
	 * @since 2016/09/06
	 */
	@Override
	public String toString()
	{
		// Get
		Reference<String> ref = this._string;
		String rv;
		
		// Cache?
		if (ref == null || null == (rv = ref.get()))
			this._string = new WeakReference<>((rv = this.from + "->" +
				this.to + "~[" + this.type + "]"));
		
		return rv;
	}
	
	/**
	 * Returns the type of access which was performed.
	 *
	 * @return The field access type.
	 * @since 2016/09/06
	 */
	public JITFieldAccessType type()
	{
		return this.type;
	}
}

