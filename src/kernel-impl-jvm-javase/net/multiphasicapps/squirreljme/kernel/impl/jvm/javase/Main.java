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
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.IOException;
import java.io.Reader;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import javax.imageio.ImageIO;
import net.multiphasicapps.io.hex.HexInputStream;
import net.multiphasicapps.squirreljme.kernel.Kernel;

/**
 * Main entry point for the Java SE JVM launcher interface kernel.
 *
 * @since 2016/05/14
 */
public class Main
{
	/** Icons for the console. */
	public static final List<Image> ICONS;
	
	/**
	 * Loads icons now because when the Swing code runs, the main library
	 * messes up the classpath which causes resources to not be found.
	 *
	 * @since 2016/05/17
	 */
	static
	{
		// Common sizes
		int[] css = new int[]{16, 32, 48};
		int n = css.length;
		InputStream[] csi = new InputStream[n];
		
		// Find resources
		for (int i = 0; i < n; i++)
			csi[i] = Main.class.getResourceAsStream("/net/" +
				"multiphasicapps/squirreljme/mascot/png/low/" +
				"head_" + css[i] + "x" + css[i] + ".png.hex");
		
		// Find plugins
		ImageIO.scanForPlugins();
		
		// Make images from them
		BufferedImage[] bis = new BufferedImage[n];
		for (int i = 0; i < n; i++)
			try
			{
				// Must exist
				InputStream is = csi[i];
				if (is == null)
					throw new NullPointerException();
				
				// Load it
				try (InputStream real = new HexInputStream(
					new InputStreamReader(is, "utf-8")))
				{
					bis[i] = ImageIO.read(real);
				}
			}
			
			// Make dummy image
			catch (NullPointerException|IOException e)
			{
				System.err.println(e);
				int dim = css[i];
				bis[i] = new BufferedImage(dim, dim,
					BufferedImage.TYPE_INT_ARGB);
			}
		
		// Set icons
		ICONS = Collections.<Image>unmodifiableList(Arrays.<Image>asList(bis));
	}
	
	/**
	 * Main entry point.
	 *
	 * @param __args Program arguments.
	 * @since 2016/05/14
	 */
	public static void main(String... __args)
	{
		// Initialize the kernel, it is the most important
		Kernel kern = new JVMJavaSEKernel(__args);
	}
}

