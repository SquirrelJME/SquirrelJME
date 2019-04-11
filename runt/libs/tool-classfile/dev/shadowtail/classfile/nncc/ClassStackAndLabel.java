// -*- Mode: Java; indent-tabs-mode: t; tab-width: 4 -*-
// ---------------------------------------------------------------------------
// Multi-Phasic Applications: SquirrelJME
//     Copyright (C) Stephanie Gawroriski <xer@multiphasicapps.net>
// ---------------------------------------------------------------------------
// SquirrelJME is under the GNU General Public License v3+, or later.
// See license.mkd for licensing and copyright information.
// ---------------------------------------------------------------------------

package dev.shadowtail.classfile.nncc;

import dev.shadowtail.classfile.xlate.JavaStackState;
import java.lang.ref.Reference;
import java.lang.ref.WeakReference;
import net.multiphasicapps.classfile.ClassName;

/**
 * This stores a class, a stack state, and a label.
 *
 * @since 2019/04/11
 */
public final class ClassStackAndLabel
{
	/** The name of the class. */
	public final ClassName classname;
	
	/** The state of the resulting stack. */
	public final JavaStackState stack;
	
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
	 * @param __s The stack state.
	 * @param __l The label to jump to.
	 * @throws NullPointerException On null arguments.
	 * @since 2019/04/11
	 */
	public ClassStackAndLabel(ClassName __n, JavaStackState __s,
		NativeCodeLabel __l)
		throws NullPointerException
	{
		if (__n == null || __s == null || __l == null)
			throw new NullPointerException("NARG");
		
		this.classname = __n;
		this.stack = __s;
		this.label = __l;
	}
	
	/**
	 * {@inheritDoc}
	 * @since 2019/04/11
	 */
	@Override
	public boolean equals(Object __o)
	{
		throw new todo.TODO();
	}
	
	/**
	 * {@inheritDoc}
	 * @since 2019/04/11
	 */
	@Override
	public final int hashCode()
	{
		throw new todo.TODO();
	}
	
	/**
	 * {@inheritDoc}
	 * @since 2019/04/11
	 */
	@Override
	public final String toString()
	{
		throw new todo.TODO();
	}
}

