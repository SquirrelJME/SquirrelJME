// ---------------------------------------------------------------------------
// SquirrelJME
//     Copyright (C) Stephanie Gawroriski <xer@multiphasicapps.net>
// ---------------------------------------------------------------------------
// SquirrelJME is under the GNU General Public License v3+, or later.
// See license.mkd for licensing and copyright information.
// ---------------------------------------------------------------------------

package cc.squirreljme.runtime.lcdui.image;

import cc.squirreljme.runtime.cldc.debug.Debugging;
import java.io.IOException;
import java.io.InputStream;
import javax.microedition.lcdui.Image;

/**
 * This class is used to read and process ZIP files into images.
 *
 * @since 2021/12/04
 */
public class GIFReader
{
	/** The source data stream. */
	protected final InputStream in;
	
	/**
	 * Initializes the GIF reader.
	 * 
	 * @param __in The stream to read from.
	 * @throws NullPointerException On null arguments.
	 * @since 2021/12/04
	 */
	public GIFReader(InputStream __in)
		throws NullPointerException
	{
		if (__in == null)
			throw new NullPointerException("NARG");
		
		this.in = __in;
	}
	
	/**
	 * Parses the image.
	 * 
	 * @return The resultant image.
	 * @throws IOException On null arguments.
	 * @since 2021/12/04
	 */
	protected Image parse()
		throws IOException
	{
		throw Debugging.todo();
	}
}
