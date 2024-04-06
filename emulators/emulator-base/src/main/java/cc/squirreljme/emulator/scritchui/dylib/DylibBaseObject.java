// -*- Mode: Java; indent-tabs-mode: t; tab-width: 4 -*-
// ---------------------------------------------------------------------------
// Multi-Phasic Applications: SquirrelJME
//     Copyright (C) Stephanie Gawroriski <xer@multiphasicapps.net>
// ---------------------------------------------------------------------------
// SquirrelJME is under the Mozilla Public License Version 2.0.
// See license.mkd for licensing and copyright information.
// ---------------------------------------------------------------------------

package cc.squirreljme.emulator.scritchui.dylib;

/**
 * Base class for all brackets that are wrapped by the ScritchUI Dylib.
 *
 * @since 2024/04/04
 */
public abstract class DylibBaseObject
{
	/** Pointer to the object. */
	protected final long objectP;
	
	/**
	 * Initializes the base object.
	 *
	 * @param __objectP The object pointer.
	 * @throws NullPointerException On null arguments.
	 * @since 2024/04/04
	 */
	public DylibBaseObject(long __objectP)
		throws NullPointerException
	{
		if (__objectP == 0)
			throw new NullPointerException("NARG");
		
		this.objectP = __objectP;
	}
}
