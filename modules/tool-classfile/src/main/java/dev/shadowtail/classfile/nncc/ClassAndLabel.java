// -*- Mode: Java; indent-tabs-mode: t; tab-width: 4 -*-
// ---------------------------------------------------------------------------
// Multi-Phasic Applications: SquirrelJME
//     Copyright (C) Stephanie Gawroriski <xer@multiphasicapps.net>
// ---------------------------------------------------------------------------
// SquirrelJME is under the GNU General Public License v3+, or later.
// See license.mkd for licensing and copyright information.
// ---------------------------------------------------------------------------

package dev.shadowtail.classfile.nncc;

import cc.squirreljme.runtime.cldc.debug.Debugging;
import java.lang.ref.Reference;
import net.multiphasicapps.classfile.ClassName;

/**
 * This stores a class and a label.
 *
 * @since 2019/04/13
 */
public final class ClassAndLabel
{
	/** The name of the class. */
	public final ClassName classname;
	
	/** Label to exception point. */
	public final NativeCodeLabel label;
	
	/** String form. */
	private Reference<String> _string;
	
	/** Hash. */
	private int _hash;
	
	/**
	 * Initializes the class.
	 *
	 * @param __n The class name.
	 * @param __l The label to jump to.
	 * @throws NullPointerException On null arguments.
	 * @since 2019/04/13
	 */
	public ClassAndLabel(ClassName __n, NativeCodeLabel __l)
		throws NullPointerException
	{
		if (__n == null || __l == null)
			throw new NullPointerException("NARG");
		
		this.classname = __n;
		this.label = __l;
	}
	
	/**
	 * {@inheritDoc}
	 * @since 2019/04/13
	 */
	@Override
	public boolean equals(Object __o)
	{
		if (this == __o)
			return true;
		
		if (!(__o instanceof ClassAndLabel))
			return false;
		
		if (this.hashCode() != __o.hashCode())
			return false;
		
		ClassAndLabel o = (ClassAndLabel)__o;
		return this.classname.equals(o.classname) &&
			this.label.equals(o.label);
	}
	
	/**
	 * {@inheritDoc}
	 * @since 2019/04/13
	 */
	@Override
	public final int hashCode()
	{
		int rv = this._hash;
		if (rv == 0)
			this._hash = (rv = this.classname.hashCode() ^
				this.label.hashCode());
		return rv;
	}
	
	/**
	 * {@inheritDoc}
	 * @since 2019/04/13
	 */
	@Override
	public final String toString()
	{
		throw Debugging.todo();
	}
}

