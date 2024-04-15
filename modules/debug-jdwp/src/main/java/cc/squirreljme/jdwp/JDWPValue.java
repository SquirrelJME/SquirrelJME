// -*- Mode: Java; indent-tabs-mode: t; tab-width: 4 -*-
// ---------------------------------------------------------------------------
// Multi-Phasic Applications: SquirrelJME
//     Copyright (C) Stephanie Gawroriski <xer@multiphasicapps.net>
// ---------------------------------------------------------------------------
// SquirrelJME is under the Mozilla Public License Version 2.0.
// See license.mkd for licensing and copyright information.
// ---------------------------------------------------------------------------

package cc.squirreljme.jdwp;

import cc.squirreljme.runtime.cldc.debug.Debugging;

/**
 * Not Described.
 *
 * @since 2024/01/26
 */
public class JDWPValue
{
	/** The tag type. */
	public final JDWPValueTag tag;
	
	/** The value type. */
	public final Object value;
	
	/**
	 * Initializes the JDWP value.
	 *
	 * @param __tag The tag type.
	 * @param __value The value used.
	 * @throws NullPointerException On null arguments.
	 * @since 2024/01/26
	 */
	public JDWPValue(JDWPValueTag __tag, Object __value)
		throws NullPointerException
	{
		if (__tag == null)
			throw new NullPointerException("NARG");
		
		this.tag = __tag;
		this.value = __value;
	}
	
	/**
	 * {@inheritDoc}
	 * @since 2024/01/26
	 */
	@Override
	public String toString()
	{
		return String.format("%s %s",
			this.tag, this.value);
	}
}
