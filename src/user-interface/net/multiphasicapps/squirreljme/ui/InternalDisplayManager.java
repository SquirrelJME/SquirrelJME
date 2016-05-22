// -*- Mode: Java; indent-tabs-mode: t; tab-width: 4 -*-
// ---------------------------------------------------------------------------
// Multi-Phasic Applications: SquirrelJME
//     Copyright (C) 2013-2016 Steven Gawroriski <steven@multiphasicapps.net>
//     Copyright (C) 2013-2016 Multi-Phasic Applications <multiphasicapps.net>
// ---------------------------------------------------------------------------
// SquirrelJME is under the GNU Affero General Public License v3+, or later.
// For more information see license.mkd.
// ---------------------------------------------------------------------------

package net.multiphasicapps.squirreljme.ui;

import java.lang.ref.Reference;

/**
 * This is the internal implementation of the display manager.
 *
 * Internal classes are not meant to be used by the end user, but only by
 * the implementation of a display manager.
 *
 * @see UIDisplayManager
 * @since 2016/05/21
 */
public abstract class InternalDisplayManager
	extends InternalElement<UIDisplayManager>
{
	/**
	 * Initializes the internal display manager.
	 *
	 * @param __ref The external element.
	 * @throws NullPointerException On null arguments.
	 * @since 2016/05/21
	 */
	public InternalDisplayManager(Reference<UIDisplayManager> __ref)
		throws NullPointerException
	{
		super(__ref);
	}
	
	/**
	 * Creates a new internal display element.
	 *
	 * @param __ref The reference to the external display.
	 * @return The internal display element.
	 * @throws UIException If it could not be created.
	 * @since 2016/05/22
	 */
	public abstract InternalDisplay internalCreateDisplay(
		Reference<UIDisplay> __ref)
		throws UIException;
	
	/**
	 * Creates a new internal image.
	 *
	 * @param __ref The reference to the external image.
	 * @return The internal image.
	 * @throws UIException If the image could not be created.
	 * @since 2016/05/22
	 */
	public abstract InternalImage internalCreateImage(
		Reference<UIImage> __ref)
		throws UIException;
	
	/**
	 * Returns an array with width/height pairs which indicates the preferred
	 * sizes of the icons to use.
	 *
	 * Dimensions returned by the array will be corrected to a minimal bound
	 * of a single pixel.
	 *
	 * @return The preferred sizes which icons should be in width/height pairs,
	 * may return {@code null} to indicate that no icons should be displayed.
	 * @throws UIException If the preferred sizes could not be determined.
	 * @since 2016/05/22
	 */
	public abstract int[] internalPreferredIconSizes()
		throws UIException;
}

