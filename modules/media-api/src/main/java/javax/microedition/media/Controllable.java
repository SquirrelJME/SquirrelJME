// -*- Mode: Java; indent-tabs-mode: t; tab-width: 4 -*-
// ---------------------------------------------------------------------------
// SquirrelJME
//     Copyright (C) Stephanie Gawroriski <xer@multiphasicapps.net>
// ---------------------------------------------------------------------------
// SquirrelJME is under the Mozilla Public License Version 2.0.
// See license.mkd for licensing and copyright information.
// ---------------------------------------------------------------------------

package javax.microedition.media;


import cc.squirreljme.runtime.cldc.annotation.Api;

/**
 * Represents a controllable aspect of a media source.
 *
 * @since 2025/06/03
 */
@Api
public interface Controllable
{
	/**
	 * Returns the specified control.
	 *
	 * @param __control The control to obtain.
	 * @return The resultant control or {@code null} if there is none.
	 * @throws IllegalArgumentException On null arguments.
	 * @throws IllegalStateException If this is called during the incorrect
	 * player state.
	 * @since 2025/06/03
	 */
	@Api
	Control getControl(String __control)
		throws IllegalArgumentException, IllegalStateException;
	
	/**
	 * Returns all the valid controls.
	 *
	 * @return The set of controls.
	 * @since 2025/06/03
	 */
	@Api
	Control[] getControls();
}


