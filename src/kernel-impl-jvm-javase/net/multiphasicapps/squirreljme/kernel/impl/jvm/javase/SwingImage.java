// -*- Mode: Java; indent-tabs-mode: t; tab-width: 4 -*-
// ---------------------------------------------------------------------------
// Multi-Phasic Applications: SquirrelJME
//     Copyright (C) 2013-2016 Steven Gawroriski <steven@multiphasicapps.net>
//     Copyright (C) 2013-2016 Multi-Phasic Applications <multiphasicapps.net>
// ---------------------------------------------------------------------------
// SquirrelJME is under the GNU Affero General Public License v3+, or later.
// For more information see license.mkd.
// ---------------------------------------------------------------------------

package net.multiphasicapps.squirreljme.kernel.impl.jvm.javase;

import java.awt.Image;
import java.awt.image.BufferedImage;
import java.lang.ref.Reference;
import net.multiphasicapps.squirreljme.ui.InternalImage;
import net.multiphasicapps.squirreljme.ui.UIImage;

/**
 * This represents an internal image used by Swing.
 *
 * @since 2016/05/22
 */
public class SwingImage
	extends InternalImage
{
	/**
	 * Initializes the swing image.
	 *
	 * @param __ref The external reference.
	 * @since 2016/05/22
	 */
	public SwingImage(Reference<UIImage> __ref)
	{
		super(__ref);
	}
}

