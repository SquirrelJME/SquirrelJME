// -*- Mode: Java; indent-tabs-mode: t; tab-width: 4 -*-
// ---------------------------------------------------------------------------
// Multi-Phasic Applications: SquirrelJME
//     Copyright (C) Stephanie Gawroriski <xer@multiphasicapps.net>
// ---------------------------------------------------------------------------
// SquirrelJME is under the Mozilla Public License Version 2.0.
// See license.mkd for licensing and copyright information.
// ---------------------------------------------------------------------------

package cc.squirreljme.runtime.media;

import cc.squirreljme.runtime.cldc.annotation.SquirrelJMEVendorApi;
import javax.microedition.media.Control;

/**
 * Base class for media controls.
 *
 * @param <C> The control being implemented.
 * @since 2025/06/03
 */
@SquirrelJMEVendorApi
public abstract class AbstractControl<C extends Control>
	implements Control
{
	/** The class this implements. */
	@SquirrelJMEVendorApi
	protected final Class<C> type;
	
	/**
	 * Initializes the given control.
	 *
	 * @param __type The control type this implements.
	 * @throws NullPointerException On null arguments.
	 * @since 2025/06/03
	 */
	@SquirrelJMEVendorApi
	protected AbstractControl(Class<C> __type)
		throws NullPointerException
	{
		if (__type == null)
			throw new NullPointerException("NARG");
		
		this.type = __type;
	}
	
	/**
	 * Does this match the given control name?
	 *
	 * @param __name The name to check.
	 * @return If this matches the name.
	 * @throws NullPointerException On null arguments.
	 * @since 2025/06/03
	 */
	@SquirrelJMEVendorApi
	public final boolean matches(String __name)
		throws NullPointerException
	{
		if (__name == null)
			throw new NullPointerException("NARG");
		
		// Does this match the full name?
		String fullName = this.type.getName();
		if (fullName.equals(__name))
			return true;
		
		// Otherwise, check the short name
		int ld = fullName.lastIndexOf('.');
		if (ld < 0)
			return false;
		return fullName.substring(ld + 1).equals(__name);
	}
}
