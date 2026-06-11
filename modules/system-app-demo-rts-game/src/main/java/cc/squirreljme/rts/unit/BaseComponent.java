// -*- Mode: Java; indent-tabs-mode: t; tab-width: 4 -*-
// ---------------------------------------------------------------------------
// Multi-Phasic Applications: SquirrelJME
//     Copyright (C) Stephanie Gawroriski <xer@multiphasicapps.net>
// ---------------------------------------------------------------------------
// SquirrelJME is under the Mozilla Public License Version 2.0.
// See license.mkd for licensing and copyright information.
// ---------------------------------------------------------------------------

package cc.squirreljme.rts.unit;

/**
 * This is the base class for all integrated components that are part of units.
 *
 * @since 2026/06/11
 */
public abstract class BaseComponent
{
	/** The type identifier for this component. */
	public final ComponentType type;
	
	/**
	 * Initializes the base component.
	 *
	 * @param __type The type of component this maps as.
	 * @throws NullPointerException On null arguments.
	 * @since 2026/06/11
	 */
	public BaseComponent(ComponentType __type)
		throws NullPointerException
	{
		if (__type == null)
			throw new NullPointerException("NARG");
		
		this.type = __type;
	}
}
