// -*- Mode: Java; indent-tabs-mode: t; tab-width: 4 -*-
// ---------------------------------------------------------------------------
// Multi-Phasic Applications: SquirrelJME
//     Copyright (C) Steven Gawroriski <steven@multiphasicapps.net>
//     Copyright (C) Multi-Phasic Applications <multiphasicapps.net>
// ---------------------------------------------------------------------------
// SquirrelJME is under the GNU General Public License v3+, or later.
// See license.mkd for licensing and copyright information.
// ---------------------------------------------------------------------------

package net.multiphasicapps.squirreljme.jit;

import java.lang.ref.Reference;
import java.lang.ref.WeakReference;
import net.multiphasicapps.squirreljme.jit.link.IdentifierSymbol;
import net.multiphasicapps.squirreljme.jit.link.MemberTypeSymbol;
import net.multiphasicapps.squirreljme.jit.link.ClassExport;
import net.multiphasicapps.squirreljme.jit.link.Export;
import net.multiphasicapps.squirreljme.jit.link.MemberFlags;

/**
 * This represents a member that is exported.
 *
 * @since 2017/04/04
 */
public abstract class ExportedMember
	implements Export
{
	/** The exported class. */
	protected final ClassExport exportedclass;
	
	/** The member flags. */
	protected final MemberFlags flags;
	
	/** The member name. */
	protected final IdentifierSymbol name;
	
	/** The member type. */
	protected final MemberTypeSymbol type;
	
	/** String representation. */
	private volatile Reference<String> _string;
	
	/**
	 * Initializes the base exported member.
	 *
	 * @param __ce The containing class.
	 * @param __flags The flags for the member.
	 * @param __name The name of the member.
	 * @param __type The type of the member.
	 * @throws NullPointerException On null arguments.
	 * @since 2017/04/09
	 */
	ExportedMember(ClassExport __ce, MemberFlags __flags,
		IdentifierSymbol __name, MemberTypeSymbol __type)
		throws NullPointerException
	{
		// Check
		if (__ce == null || __flags == null || __name == null ||
			__type == null)
			throw new NullPointerException("NARG");
		
		// Set
		this.exportedclass = __ce;
		this.flags = __flags;
		this.name = __name;
		this.type = __type;
	}
	
	/**
	 * {@inheritDoc}
	 * @since 2017/04/01
	 */
	@Override
	public boolean equals(Object __o)
	{
		// Check
		if (!(__o instanceof ExportedMember))
			return false;
		
		ExportedMember o = (ExportedMember)__o;
		return this.flags.equals(o.flags) &&
			this.name.equals(o.name) &&
			this.type.equals(o.type);
	}
	
	/**
	 * Returns the class that is exporting this member.
	 *
	 * @return The class this is being exported by.
	 * @since 2017/05/20
	 */
	public final ClassExport exportedClass()
	{
		return this.exportedclass;
	}
	
	/**
	 * {@inheritDoc}
	 * @since 2017/04/01
	 */
	@Override
	public final int hashCode()
	{
		return this.flags.hashCode() ^ this.name.hashCode() ^
			this.type.hashCode();
	}
	
	/**
	 * {@inheritDoc}
	 * @since 2017/04/01
	 */
	@Override
	public final String toString()
	{
		Reference<String> ref = this._string;
		String rv;
		
		// Check
		if (ref == null || null == (rv = ref.get()))
			this._string = new WeakReference<>((rv = this.name + ":" +
				this.type + "(" + this.flags + ")"));
		
		return rv;
	}
}

