// -*- Mode: Java; indent-tabs-mode: t; tab-width: 4 -*-
// ---------------------------------------------------------------------------
// Multi-Phasic Applications: SquirrelJME
//     Copyright (C) Stephanie Gawroriski <xer@multiphasicapps.net>
//     Copyright (C) Multi-Phasic Applications <multiphasicapps.net>
// ---------------------------------------------------------------------------
// SquirrelJME is under the GNU General Public License v3+, or later.
// See license.mkd for licensing and copyright information.
// ---------------------------------------------------------------------------

package cc.squirreljme.springcoat.vm;

import net.multiphasicapps.classfile.ByteCode;
import net.multiphasicapps.classfile.ClassName;
import net.multiphasicapps.classfile.Field;
import net.multiphasicapps.classfile.FieldNameAndType;

/**
 * This contains and stores the definition of a single field.
 *
 * @since 2018/07/22
 */
public final class SpringField
{
	/** The class this technically belongs to. */
	protected final ClassName inclass;
	
	/** The field definition. */
	protected final Field field;
	
	/** The field index. */
	protected final int index;
	
	/**
	 * Initializes the field.
	 *
	 * @param __cn The class this field is in.
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
		
		// Instance fields require an index
		if (__f.flags().isInstance())
		{
			// {@squirreljme.error BK0h Negative field index.}
			if (__dx < 0)
				throw new IllegalArgumentException("BK0h");	
			
			this.index = __dx;
		}
		
		// Not used for statics
		else
			this.index = -1;
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
}

