// -*- Mode: Java; indent-tabs-mode: t; tab-width: 4 -*-
// ---------------------------------------------------------------------------
// Multi-Phasic Applications: SquirrelJME
//     Copyright (C) 2013-2016 Steven Gawroriski <steven@multiphasicapps.net>
//     Copyright (C) 2013-2016 Multi-Phasic Applications <multiphasicapps.net>
// ---------------------------------------------------------------------------
// SquirrelJME is under the GNU Affero General Public License v3+, or later.
// For more information see license.mkd.
// ---------------------------------------------------------------------------

import java.awt.image.BufferedImage;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.InputStream;
import java.io.OutputStream;
import javax.imageio.ImageIO;

/**
 * This reads an image and performs a weighted down sampling of it.
 *
 * The color selection is that of SquirrelJME's mascot.
 *
 * @since 2016/05/14
 */
public class WeightedDownsample
{
	/**
	 * Main entry point.
	 *
	 * @param __args Program arguents.
	 * @throws Throwable On any exception.
	 * @since 2016/05/15
	 */
	public static void main(String... __args)
		throws Throwable
	{
		// Check
		if (__args == null || __args.length < 4)
			throw new IllegalArgumentException("Usage: (image) (w) (h) (out)");
		
		// Read input image
		BufferedImage in;
		try (InputStream is = new FileInputStream(__args[0]))
		{
			// Read image data
			in = ImageIO.read(is);
		}
		
		// Target size?
		int tw = Integer.decode(__args[1]);
		int th = Integer.decode(__args[2]);
		
		// Create target image
		BufferedImage out = new BufferedImage(tw, th,
			BufferedImage.TYPE_INT_RGB);
		
		// The image scale
		double xscale = (double)in.getWidth() / (double)tw;
		double yscale = (double)in.getHeight() / (double)th;
		
		// Go through all output pixel positions
		for (int dy = 0; dy < th; dy++)
			for (int dx = 0; dx < tw; dx++)
			{
				// Determine the RGB data to use
				int v = in.getRGB((int)(dx * xscale), (int)(dy * yscale));
				
				// Write RGB data
				out.setRGB(dx, dy, v);
			}
		
		// Write output image
		try (OutputStream os = new FileOutputStream(__args[3]))
		{
			ImageIO.write(out, "png", os);
		}
	}
}

