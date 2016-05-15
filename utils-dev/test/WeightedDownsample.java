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
import java.util.HashMap;
import java.util.Map;
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
		
		// The integral scale
		int ixscale = (int)xscale;
		int iyscale = (int)yscale;
		
		// Color weight map
		Map<Integer, Double> weights = new HashMap<>();
		
		// Go through all output pixel positions
		for (int dy = 0; dy < th; dy++)
			for (int dx = 0; dx < tw; dx++)
			{
				// Clear the weights
				weights.clear();
				
				// Go through the target region
				int ssy = (int)(dy * yscale);
				int ssx = (int)(dx * xscale);
				for (int sy = ssy; sy < ssy + ixscale; sy++)
					for (int sx = ssx; sx < ssx + iyscale; sx++)
					{
						// Get RGB value here
						int q = in.getRGB(sx, sy) | 0xFF000000;
						
						// Get existing weight, start at zero if missing
						Double d = weights.get(q);
						if (d == null)
							d = 0.0D;
						
						// Add weight of color
						d += __weight(q);
						
						// Set it
						weights.put(q, d);
					}
				
				// Use the heaviest color
				int v = 0xFFFF00FF;	// Use magenta for background just in case
				double w = 0.0D;
				for (Map.Entry<Integer, Double> e : weights.entrySet())
				{
					// Get values
					int ek = e.getKey();
					double ev = e.getValue();
					
					// Higher?
					if (ev > w)
					{
						v = ek;
						w = ev;
					}
				}
				
				// Write RGB data
				out.setRGB(dx, dy, v);
			}
		
		// Write output image
		try (OutputStream os = new FileOutputStream(__args[3]))
		{
			ImageIO.write(out, "png", os);
		}
	}
	
	/**
	 * Returns the weight of the given color.
	 *
	 * @param __c The color to weigh.
	 * @return The weight of it.
	 * @since 2016/05/14
	 */
	public static double __weight(int __c)
	{
		switch (__c & 0x00FFFFFF)
		{
				// Outline
			case 0x030633:
				return 20.0D;
				
				// Light fur color
			case 0xFEFFFC:
			case 0xFFFFFF:
				return 1.0D;
				
				// Darker fur color
			case 0xCACEDD:
				return 2.0D;
				
				// Light Ear pink
			case 0xffd1ce:
				return 1.0D;
				
				// Dark ear pink
			case 0xeaaaa0:
				return 2.0D;
				
				// Eye color
			case 0xf85150:	// light
			case 0xf90f0f:	// medium
			case 0xa51815:	// dark
				return 50.0D;
			
				// Unknown, weighs nothing
			default:
				return 0.0D;
		}
	}
}

