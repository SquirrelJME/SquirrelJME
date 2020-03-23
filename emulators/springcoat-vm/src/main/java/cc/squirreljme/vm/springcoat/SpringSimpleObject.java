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
@Deprecated
public final class SpringSimpleObject
	implements SpringObject
{
	/** The type of object this is. */
	@Deprecated
	protected final SpringClass type;
	
	/** The monitor for this object, every object has one. */
	@Deprecated
	protected final SpringMonitor monitor =
		new SpringMonitor();
	
	/** The pointer for this object. */
	@Deprecated
	protected final SpringPointerArea pointer;
	
	/** The reference chain for this object. */
	@Deprecated
	protected final ReferenceChainer refChain =
		new ReferenceChainer();
	
	/** Counter for references. */
	@Deprecated
	protected final ReferenceCounter refCounter =
		new ReferenceCounter();
	
	/** Field storage in the class. */
	@Deprecated
	private final SpringFieldStorage[] _fields;
	
	/** String representation. */
	@Deprecated
	private Reference<String> _string;
	
	/**
	 * Initializes the object.
	 *
	 * @param __cl The class of the object.
	 * @param __spm The manager for pointers.
	 * @throws NullPointerException On null arguments.
	 * @since 2018/09/08
	 */
	@Deprecated
	public SpringSimpleObject(SpringClass __cl, SpringPointerManager __spm)
		throws NullPointerException
	{
		if (__cl == null || __spm == null)
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
		
		// Allocate pointer
		this.pointer = __spm.allocateAndBind(__cl.instancesize, this);
	}
	
	/**
	 * Returns the field by the given field.
	 *
	 * @param __f The field to get.
	 * @return The storage for the field.
	 * @throws NullPointerException On null arguments.
	 * @since 2018/11/19
	 */
	@Deprecated
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
	@Deprecated
	public final SpringFieldStorage fieldByIndex(int __dx)
	{
		return this._fields[__dx];
	}
	
	/**
	 * {@inheritDoc}
	 * @since 2018/09/15
	 */
	@Override
	@Deprecated
	public final SpringMonitor monitor()
	{
		return this.monitor;
	}
	
	/**
	 * {@inheritDoc}
	 * @since 2019/12/21
	 */
	@Override
	@Deprecated
	public final SpringPointerArea pointerArea()
	{
		return this.pointer;
	}
	
	/**
	 * {@inheritDoc}
	 * @since 2020/03/13
	 */
	@Override
	@Deprecated
	public ReferenceChainer refChainer()
	{
		return this.refChain;
	}
	
	/**
	 * {@inheritDoc}
	 * @since 2020/03/13
	 */
	@Override
	@Deprecated
	public ReferenceCounter refCounter()
	{
		return this.refCounter;
	}
	
	/**
	 * {@inheritDoc}
	 * @since 2018/09/15
	 */
	@Override
	@Deprecated
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
	@Deprecated
	public final SpringClass type()
	{
		return this.type;
	}
}

