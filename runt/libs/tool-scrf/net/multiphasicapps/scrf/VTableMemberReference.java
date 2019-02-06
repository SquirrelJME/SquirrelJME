// -*- Mode: Java; indent-tabs-mode: t; tab-width: 4 -*-
// ---------------------------------------------------------------------------
// Multi-Phasic Applications: SquirrelJME
//     Copyright (C) Stephanie Gawroriski <xer@multiphasicapps.net>
//     Copyright (C) Multi-Phasic Applications <multiphasicapps.net>
// ---------------------------------------------------------------------------
// SquirrelJME is under the GNU General Public License v3+, or later.
// See license.mkd for licensing and copyright information.
// ---------------------------------------------------------------------------

package net.multiphasicapps.scrf;

import java.lang.ref.Reference;
import java.lang.ref.WeakReference;
import net.multiphasicapps.classfile.ClassName;
import net.multiphasicapps.classfile.MemberDescriptor;
import net.multiphasicapps.classfile.MemberName;

/**
 * This represents a reference to a member.
 *
 * @since 2019/02/06
 */
public abstract class VTableMemberReference
{
	/** Is this static? */
	protected final boolean isstatic;
	
	/** The class this is in. */
	protected final ClassName inclass;
	
	/** The member name. */
	protected final MemberName membername;
	
	/** The member type. */
	protected final MemberDescriptor membertype;
	
	/** String representation. */
	private Reference<String> _string;
	
	/**
	 * Initializes the reference.
	 *
	 * @param __s Is this static?
	 * @param __cl The class name.
	 * @param __mn The member name.
	 * @param __mt The member type.
	 * @throws NullPointerException On null arguments.
	 * @since 2019/02/06
	 */
	public VTableMemberReference(boolean __s, ClassName __cl, MemberName __mn,
		MemberDescriptor __mt)
		throws NullPointerException
	{
		if (__cl == null || __mn == null || __mt == null)
			throw new NullPointerException("NARG");
		
		this.isstatic = __s;
		this.inclass = __cl;
		this.membername = __mn;
		this.membertype = __mt;
	}
	
	/**
	 * {@inheritDoc}
	 * @since 2019/02/06
	 */
	@Override
	public boolean equals(Object __o)
	{
		if (__o == this)
			return true;
		
		if (!(__o instanceof VTableMemberReference))
			return false;
		
		VTableMemberReference o = (VTableMemberReference)__o;
		return this.isstatic == o.isstatic &&
			this.inclass.equals(o.inclass) &&
			this.membername.equals(o.membername) &&
			this.membertype.equals(o.membertype);
	}
	
	/**
	 * {@inheritDoc}
	 * @since 2019/02/06
	 */
	@Override
	public int hashCode()
	{
		return (this.isstatic ? 1 : 0) ^
			this.inclass.hashCode() ^
			this.membername.hashCode() ^
			this.membertype.hashCode();
	}
	
	/**
	 * {@inheritDoc}
	 * @since 2019/02/06
	 */
	@Override
	public String toString()
	{
		Reference<String> ref = this._string;
		String rv;
		
		if (ref == null || null == (rv = ref.get()))
			this._string = new WeakReference<>((rv = String.format(
				"%s %s::%s:%s", (this.isstatic ? "static" : "instance"),
				this.inclass, this.membername, this.membertype)));
		
		return rv;
	}
}

