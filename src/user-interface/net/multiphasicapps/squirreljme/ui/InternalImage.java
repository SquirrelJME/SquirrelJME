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
 * This is an internal representation of an image.
 *
 * @since 2016/05/22
 */
public abstract class InternalImage
	extends InternalElement<UIImage>
{
	/**
	 * Initializes the internal image.
	 *
	 * @param __r The reference to the external image.
	 * @since 2016/05/22
	 */
	public InternalImage(Reference<UIImage> __r)
	{
		super(__r);
	}
}

