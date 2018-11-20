// -*- Mode: Java; indent-tabs-mode: t; tab-width: 4 -*-
// ---------------------------------------------------------------------------
// Multi-Phasic Applications: SquirrelJME
//     Copyright (C) Stephanie Gawroriski <xer@multiphasicapps.net>
//     Copyright (C) Multi-Phasic Applications <multiphasicapps.net>
// ---------------------------------------------------------------------------
// SquirrelJME is under the GNU General Public License v3+, or later.
// See license.mkd for licensing and copyright information.
// ---------------------------------------------------------------------------

package cc.squirreljme.vm.springcoat;

import java.lang.ref.Reference;
import java.lang.ref.WeakReference;

/**
 * This is a representation of an object within the virtual machine.
 *
 * @since 2018/09/08
 */
public final class SpringSimpleObject
	implements SpringObject
{
	/** The type of object this is. */
	protected final SpringClass type;
	
	/** The monitor for this object, every object has one. */
	protected final SpringMonitor monitor =
		new SpringMonitor();
	
	/** Field storage in the class. */
	private final SpringFieldStorage[] _fields;
	
	/** String representation. */
	private Reference<String> _string;
	
	/**
	 * Initializes the object.
	 *
	 * @param __cl The class of the object.
	 * @throws NullPointerException On null arguments.
	 * @since 2018/09/08
	 */
	public SpringSimpleObject(SpringClass __cl)
		throws NullPointerException
	{
		if (__cl == null)
			throw new NullPointerException("NARG");
		
		this.type = __cl;
		
		// Setup field array
		int n;
		SpringFieldStorage[] fields;
		this._fields = (fields =
			new SpringFieldStorage[(n = __cl.instanceFieldCount())]);
		
		// Initialize variable for all fields
		int i = 0;
		for (SpringField f : __cl.fieldTable())
			fields[i++] = new SpringFieldStorage(f);
	}
	
	/**
	 * Returns the field by the given field.
	 *
	 * @param __f The field to get.
	 * @return The storage for the field.
	 * @throws NullPointerException On null arguments.
	 * @since 2018/11/19
	 */
	public final SpringFieldStorage fieldByField(SpringField __f)
		throws NullPointerException
	{
		if (__f == null)
			throw new NullPointerException("NARG");
		
		return this.fieldByIndex(__f.index());
	}
	
	/**
	 * Returns the field by the given index.
	 *
	 * @param __dx The field to get.
	 * @return The storage for the field by the specified index.
	 * @since 2018/09/16
	 */
	public final SpringFieldStorage fieldByIndex(int __dx)
	{
		return this._fields[__dx];
	}
	
	/**
	 * {@inheritDoc}
	 * @since 2018/09/15
	 */
	@Override
	public final SpringMonitor monitor()
	{
		return this.monitor;
	}
	
	/**
	 * {@inheritDoc}
	 * @since 2018/09/15
	 */
	@Override
	public final String toString()
	{
		Reference<String> ref = this._string;
		String rv;
		
		if (ref == null || null == (rv = ref.get()))
			this._string = new WeakReference<>((rv = String.format(
				"%s@%08x", this.type.name(), System.identityHashCode(this))));
		
		return rv;
	}
	
	/**
	 * {@inheritDoc}
	 * @since 2018/09/09
	 */
	@Override
	public final SpringClass type()
	{
		return this.type;
	}
}

