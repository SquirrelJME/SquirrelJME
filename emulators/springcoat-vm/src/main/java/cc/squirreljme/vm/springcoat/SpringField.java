// -*- Mode: Java; indent-tabs-mode: t; tab-width: 4 -*-
// ---------------------------------------------------------------------------
// SquirrelJME
//     Copyright (C) Stephanie Gawroriski <xer@multiphasicapps.net>
// ---------------------------------------------------------------------------
// SquirrelJME is under the Mozilla Public License Version 2.0.
// See license.mkd for licensing and copyright information.
// ---------------------------------------------------------------------------

package cc.squirreljme.vm.springcoat;

import net.multiphasicapps.classfile.ClassName;
import net.multiphasicapps.classfile.Field;
import net.multiphasicapps.classfile.FieldFlags;
import net.multiphasicapps.classfile.FieldNameAndType;

/**
 * This contains and stores the definition of a single field.
 *
 * @since 2018/07/22
 */
public final class SpringField
	implements SpringMember
{
	/** The class this technically belongs to. */
	protected final ClassName inclass;
	
	/** The field definition. */
	protected final Field field;
	
	/** The field index. */
	protected final int index;
	
	/** Are we watching writes? */
	volatile boolean _watchWrite;
	
	/** Are we watching reads? */
	volatile boolean _watchRead;
	
	/**
	 * Initializes the field.
	 *
	 * @param __cl The class this field is in.
	 * @param __f The field definition.
	 * @param __dx The field index, this is ignored for statics and cannot be
	 * negative for instances.
	 * @throws IllegalArgumentException If the field index is negative.
	 * @throws NullPointerException On null arguments.
	 * @since 2018/09/08
	 */
	SpringField(ClassName __cl, Field __f, int __dx)
		throws IllegalArgumentException, NullPointerException
	{
		if (__cl == null || __f == null)
			throw new NullPointerException("NARG");
		
		this.inclass = __cl;
		this.field = __f;
		this.index = __dx;
		
		// Instance fields require an index
		if (__f.flags().isInstance())
		{
			/* {@squirreljme.error BK17 Negative field index.} */
			if (__dx < 0)
				throw new IllegalArgumentException("BK17");
		}
	}
	
	/**
	 * {@inheritDoc}
	 * @since 2018/09/09
	 */
	@Override
	public final FieldFlags flags()
	{
		return this.field.flags();
	}
	
	/**
	 * {@inheritDoc}
	 * @since 2018/09/09
	 */
	@Override
	public final ClassName inClass()
	{
		return this.inclass;
	}
	
	/**
	 * Returns the index of this field.
	 *
	 * @return The field index.
	 * @since 2018/09/16
	 */
	public final int index()
	{
		return this.index;
	}
	
	/**
	 * Are we doing debug watching?
	 * 
	 * @param __write Watch writes or reads?
	 * @return If we are doing that kind of watching.
	 * @since 2021/04/30
	 */
	protected boolean isDebugWatching(boolean __write)
	{
		if (__write)
			return this._watchWrite;
		return this._watchRead;
	}
	
	/**
	 * Is this a static field?
	 *
	 * @return If this is a static field.
	 * @since 2018/09/09
	 */
	public final boolean isStatic()
	{
		return this.field.flags().isStatic();
	}
	
	/**
	 * {@inheritDoc}
	 * @since 2018/09/09
	 */
	@Override
	public final FieldNameAndType nameAndType()
	{
		return this.field.nameAndType();
	}
	
	/**
	 * {@inheritDoc}
	 * @since 2021/03/19
	 */
	@Override
	public String toString()
	{
		return String.format("Field %s:%s",
			this.inclass, this.field);
	}
}

