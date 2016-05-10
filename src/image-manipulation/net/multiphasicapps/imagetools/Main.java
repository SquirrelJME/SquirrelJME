// -*- Mode: Java; indent-tabs-mode: t; tab-width: 4 -*-
// ---------------------------------------------------------------------------
// Multi-Phasic Applications: SquirrelJME
//     Copyright (C) 2013-2016 Steven Gawroriski <steven@multiphasicapps.net>
//     Copyright (C) 2013-2016 Multi-Phasic Applications <multiphasicapps.net>
// ---------------------------------------------------------------------------
// SquirrelJME is under the GNU Affero General Public License v3+, or later.
// For more information see license.mkd.
// ---------------------------------------------------------------------------

package net.multiphasicapps.imagetools;

import java.io.InputStream;
import java.io.IOException;
import java.nio.channels.Channels;
import java.nio.channels.FileChannel;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardOpenOption;
import java.util.ServiceLoader;
import net.multiphasicapps.imagereader.ImageReader;

/**
 * This is the main entry class for the image manipulation tools.
 *
 * @since 2016/05/10
 */
public class Main
{
	/** Services which are available. */
	private static final ServiceLoader<ImageReader> _SERVICES =
		ServiceLoader.<ImageReader>load(ImageReader.class);
	
	/**
	 * Main entry point for the program.
	 *
	 * @param __args Program args.
	 * @since 2016/05/10
	 */
	public static void main(String... __args)
	{
		// Must always exist
		if (__args == null)
			__args = new String[0];
		
		// {@squirreljme.error AS01 Expected a command to be passed to be used
		// during image manipulation.}
		if (__args.length <= 0)
			throw new IllegalArgumentException("AS01");
		
		// Which commands?
		String command = __args[0];
		switch (command)
		{
				// {@squirreljme.error AS03 Unknown command (The command)}
			default:
				throw new IllegalArgumentException(String.format("AS03 %s",
					command));
		}
	}
	
	/**
	 * Attempts to load the given image from the given path.
	 *
	 * @param __path The path to load the image from.
	 * @param __dims The number of dimensions to use.
	 * @return The array to the image data
	 * @throws IOException If the image does not exist.
	 * @throws NullPointerException On null arguments.
	 * @since 2016/05/10
	 */
	public static int[] loadImage(String __path, int[] __dims)
		throws IOException, NullPointerException
	{
		// Check
		if (__path == null || __dims == null)
			throw new NullPointerException("NARG");
		
		// Determine the extension, if any
		int ls = __path.lastIndexOf('/'),
			ld = __path.lastIndexOf('.');
		String ext;
		if (ld > ls && ld >= 0)
			ext = __path.substring(ld + 1);
		else
			ext = "";
		
		// Go through all services
		for (ImageReader ir : _SERVICES)
		{
			// Can handle the given extension or path?
			if (!(ir.canRead(ext) || ir.canRead(__path)))
				continue;
			
			// Try opening as a file first
			Path p = Paths.get(__path);
			try (FileChannel fc = FileChannel.open(p, StandardOpenOption.READ))
			{
				return ir.readImage(Channels.newInputStream(fc), __dims);
			}
			
			// Could not read it, try as a resource instead
			catch (IOException e)
			{
				try (InputStream is = Main.class.getResourceAsStream(__path))
				{
					// It might not exist
					if (is != null)
						return ir.readImage(is, __dims);
				}
				
				// Could not read this either
				catch (IOException eb)
				{
				}
			}
		}
		
		// {@squirreljme.error AS02 The specified path does not exist on the
		// disk or as a resource within the classpath. (The path to the image)}
		throw new IOException(String.format("AS02 %s", __path));
	}
}

