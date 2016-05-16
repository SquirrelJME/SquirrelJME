// -*- Mode: Java; indent-tabs-mode: t; tab-width: 4 -*-
// ---------------------------------------------------------------------------
// Multi-Phasic Applications: SquirrelJME
//     Copyright (C) 2013-2016 Steven Gawroriski <steven@multiphasicapps.net>
//     Copyright (C) 2013-2016 Multi-Phasic Applications <multiphasicapps.net>
// ---------------------------------------------------------------------------
// SquirrelJME is under the GNU Affero General Public License v3+, or later.
// For more information see license.mkd.
// ---------------------------------------------------------------------------

import java.awt.Color;
import java.awt.color.ColorSpace;
import java.awt.Font;
import java.awt.FontMetrics;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.image.BufferedImage;
import java.awt.image.WritableRaster;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.InputStream;
import java.io.OutputStream;
import java.util.AbstractMap;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.TreeSet;
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
	/** Unknown colors. */
	protected static final Set<Integer> UNKNOWN_COLORS =
		new TreeSet<>();
	
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
		BufferedImage inbase;
		try (InputStream is = new FileInputStream(__args[0]))
		{
			// Read image data
			inbase = ImageIO.read(is);
		}
		
		// Create a new copy using another colorspace
		int ow = inbase.getWidth(),
			oh = inbase.getHeight();
		BufferedImage in = new BufferedImage(ow, oh,
			BufferedImage.TYPE_INT_RGB);
		in.createGraphics().drawImage(inbase, 0, 0, null);
		inbase = null;
		
		// Target size?
		int tw = Integer.decode(__args[1]);
		int th = Integer.decode(__args[2]);
		
		// Create target image
		BufferedImage out = new BufferedImage(tw, th,
			BufferedImage.TYPE_INT_RGB);
		
		// The image scale
		double xscale = (double)ow / (double)tw;
		double yscale = (double)oh / (double)th;
		
		// The integral scale
		int ixscale = (int)xscale;
		int iyscale = (int)yscale;
		
		// Determine the pixels with the most colors used
		Map<Integer, Integer> totals = new HashMap<>();
		for (int sy = 0; sy < oh; sy++)
			for (int sx = 0; sx < ow; sx++)
			{
				// Get value here
				int rgb = in.getRGB(sx, sy) & 0xFFFFFF;
				
				Integer i = totals.get(rgb);
				if (i == null)
					totals.put(rgb, 1);
				else
					totals.put(rgb, i + 1);
			}
		
		// Make an array instead
		List<int[]> mostcolors = new ArrayList<>();
		for (Map.Entry<Integer, Integer> e : totals.entrySet())
			mostcolors.add(new int[]{e.getKey(), e.getValue()});
		
		// Place into a list
		Collections.sort(mostcolors, new Comparator<int[]>()
			{
				/**
				 * {@inheritDoc}
				 * @since 2016/05/14
				 */
				@Override
				public int compare(int[] __a, int[] __b)
				{
					// Compare the counts
					return -Integer.compare(__a[1], __b[1]);
				}
			});
		
		// Draw a color chart
		{
			// Determine font to use
			Font f = Font.decode(Font.MONOSPACED);
			BufferedImage fi = new BufferedImage(1, 1,
				BufferedImage.TYPE_INT_RGB);
			Graphics2D gfx = (Graphics2D)(fi.createGraphics());
			gfx.setFont(f);
			FontMetrics fm = gfx.getFontMetrics(f);
			int ch = fm.getHeight();
			
			// Setup output image
			int n = mostcolors.size();
			BufferedImage cti = new BufferedImage(320, ch * n,
				BufferedImage.TYPE_INT_RGB);
			gfx = (Graphics2D)(cti.createGraphics());
			gfx.setFont(f);
			for (int i = 0, dy = 0; i < n; i++, dy += ch)
			{
				// Get color
				int[] vv = mostcolors.get(i);
				int col = vv[0];
				int cnt = vv[1];
				
				// Set color to the desired one
				gfx.setColor(new Color(col));
				gfx.fillRect(0, dy, ch, ch);
				
				// Background for fill
				gfx.setColor(Color.WHITE);
				gfx.fillRect(ch, dy, 320, ch);
				
				// Back to black
				gfx.setColor(Color.BLACK);
				gfx.drawLine(ch, dy, ch, dy + ch);
				gfx.drawString(String.format("#%06X (%d)", col, cnt),
					ch + 1, dy + ch);
			}
			
			// Write output image
			try (OutputStream os = new FileOutputStream("table.png"))
			{
				ImageIO.write(cti, "png", os);
			}
		}
		
		// Color weight map
		Map<Integer, Double> weights = new HashMap<>();
		
		// Go through all output pixel positions
		for (int dy = 0; dy < th; dy++)
		{
			// The last chosen color
			int last = 0xFF00FF;
			
			// Go through all pixels
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
						int q = __getRGB(in, sx, sy);
						
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
				int v = 0xFF00FF;	// Use magenta for background just in case
				double w = 0.0D;
				int sv = 0xFF00FF;
				double sw = 0.0D;
				for (Map.Entry<Integer, Double> e : weights.entrySet())
				{
					// Get values
					int ek = e.getKey();
					double ev = e.getValue();
					
					// Higher?
					if (ev > w)
					{
						// Set old best
						sv = v;
						sw = w;
						
						// Set new best
						v = ek;
						w = ev;
					}
				}
				
				// If the selected color has no weight then use the last one
				if (w <= 0.0D)
					v = last;
				else
				{
					// If there is a secondary color, average it
					if (sw > 0.0D)
					{
						// Setup color
						Color bc = new Color(v);
						Color sc = new Color(sv);
						
						// Get color components
						float[] bb = bc.getRGBComponents(null);
						float[] ss = sc.getRGBComponents(null);
						
						// Average them
						float xr = __avg(w, bb[0], sw, ss[0]);
						float xg = __avg(w, bb[1], sw, ss[1]);
						float xb = __avg(w, bb[2], sw, ss[2]);
						
						// Setup new color
						v = new Color(xr, xg, xb).getRGB();
					}
					
					// Set last
					last = v;
				}
				
				// Write RGB data
				out.setRGB(dx, dy, v);
			}
		}
		
		// Write output image
		try (OutputStream os = new FileOutputStream(__args[3]))
		{
			ImageIO.write(out, "png", os);
		}
		
		// Print unknown colors
		/*System.out.println("Unknown colors:");
		for (int u : UNKNOWN_COLORS)
			System.out.printf("%06x%n", u);*/
	}
	
	/**
	 * Averages two colors.
	 *
	 * @param __ab Weight of first color.
	 * @param __a First color.
	 * @param __wb Weight of second color.
	 * @param __b Second color.
	 * @return The new color.
	 * @since 2016/05/14
	 */
	public static float __avg(double __wa, float __a, double __wb, float __b)
	{
		if (true)
			return __a;
		if (true)
			return (float)(((__a * (__wa * 2.0D)) + (__b * __wb)) /
				((__wa * 2.0D) + __wb));
		return (__a + __b) / 2.0F;
	}
	
	/**
	 * Converts the color from a float to an integer.
	 *
	 * @param __v The input color.
	 * @return The integer version of it.
	 * @since 2016/05/14
	 */
	public static int __cap(float __v)
	{
		return Math.min(255, Math.max(0, (int)((float)__v * 255.0F)));
	}
	
	/**
	 * Returns the pixel value in linear color space.
	 *
	 * @param __bi The image to read from.
	 * @param __x The x coordinate.
	 * @param __y The y coordinate.
	 * @since 2016/05/14
	 */
	public static int __getRGB(BufferedImage __bi, int __x, int __y)
	{
		if (false)
		{
			Color c = new Color(__bi.getRGB(__x, __y));
			
			// Convert to linear RGB
			float[] u = c.getColorComponents(ColorSpace.getInstance(
				ColorSpace.CS_LINEAR_RGB), null);
			
			// Return that
			return (__cap(u[2]) << 16) |
				(__cap(u[1]) << 8) |
				(__cap(u[0]));
		}
		
		// Get raster
		WritableRaster wr = __bi.getRaster();
		
		// Build RGB data
		return (wr.getSample(__x, __y, 0) << 16) |
			(wr.getSample(__x, __y, 1) << 8) |
			(wr.getSample(__x, __y, 2));
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
				// Outside area
			case 0xFC00FF:
			case 0xFF00FF:
				return 0.0000001D;
				
				// Outline
			case 0x030633:
				return 20.0D;
				
				// Light fur color
			case 0xFEFFFC:
				return 1.0D;
				
				// Darker fur color
			case 0xCACEDE:
				return 2.0D;
				
				// Light Ear pink
			case 0xFFD0CD:
				return 1.0D;
				
				// Dark ear pink
			case 0xEAAAA0:
				return 2.0D;
				
				// Eye color
			case 0xF75150:	// light
			case 0xF91110:	// medium
			case 0xA51616:	// dark
				return 50.0D;
			
				// Unknown, weighs nothing
			default:
				// Debug
				UNKNOWN_COLORS.add(__c);
				
				// Little weight
				return 0.1D;
		}
	}
}

