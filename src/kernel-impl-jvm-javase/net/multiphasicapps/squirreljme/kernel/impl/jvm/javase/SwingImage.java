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

import java.awt.GraphicsConfiguration;
import java.awt.GraphicsDevice;
import java.awt.GraphicsEnvironment;
import java.awt.Image;
import java.awt.image.BufferedImage;
import java.awt.Transparency;
import java.lang.ref.Reference;
import net.multiphasicapps.imagereader.ImageData;
import net.multiphasicapps.imagereader.ImageType;
import net.multiphasicapps.squirreljme.ui.InternalImage;
import net.multiphasicapps.squirreljme.ui.UIException;
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
	
	/**
	 * {@inheritDoc}
	 * @since 2016/05/23
	 */
	@Override
	protected Object internalCreateMapping(ImageData __id)
		throws UIException
	{
		// Check
		if (__id == null)
			throw new NullPointerException("NARG");
		
		// Get details
		int width = __id.width();
		int height = __id.height();
		
		// Depends on the image type
		switch (__id.type())
		{
				// Unknown, use a slow means of creating a mapping copy
			default:
				{
					// Create compatible image
					BufferedImage rv = GraphicsEnvironment.
						getLocalGraphicsEnvironment().getDefaultScreenDevice().
						getDefaultConfiguration().createCompatibleImage(
							width, height, Transparency.TRANSLUCENT);
					
					// Copy pixel by pixel
					for (int y = 0; y < height; y++)
						for (int x = 0; x < width; x++)
							throw new Error("TODO");
					
					// Return it
					return rv;
				}
		}
	}
}

