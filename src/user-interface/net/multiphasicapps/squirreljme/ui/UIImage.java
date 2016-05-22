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

/**
 * This is an image which may be shown by the user interface. The image may
 * come in many formats and internally is usually backed by a buffer containing
 * the image data. Due to the wide range of systems supported by SquirrelJME
 * an input image may be associated with multiple actual images which represent
 * the size of the image along with specific color details. The internal
 * representation may choose an image of a specific type to be displayed. For
 * example on low resolution systems, a 16x16 image may be chosen while on
 * a system which a much higher pixel density might choose one that is of a
 * higher resolution.
 *
 * @since 2016/05/22
 */
public final class UIImage
	extends UIElement
{
	/**
	 * Initializes the image.
	 *
	 * @param __dm The owning display manager.
	 * @since 2016/05/22
	 */
	UIImage(UIDisplayManager __dm)
	{
		super(__dm);
	}
}

