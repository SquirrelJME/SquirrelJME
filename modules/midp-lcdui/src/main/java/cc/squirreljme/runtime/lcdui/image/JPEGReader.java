// -*- Mode: Java; indent-tabs-mode: t; tab-width: 4 -*-
// ---------------------------------------------------------------------------
// SquirrelJME
//     Copyright (C) Stephanie Gawroriski <xer@multiphasicapps.net>
// ---------------------------------------------------------------------------
// SquirrelJME is under the Mozilla Public License Version 2.0.
// See license.mkd for licensing and copyright information.
// ---------------------------------------------------------------------------

package cc.squirreljme.runtime.lcdui.image;

import cc.squirreljme.runtime.cldc.debug.Debugging;
import java.io.DataInputStream;
import java.io.InputStream;
import javax.microedition.lcdui.Image;

/**
 * This class is used to process JPEG image files.
 *
 * @since 2019/05/06
 */
public final class JPEGReader
{
	/** The input data. */
	protected final DataInputStream in;
	
	/**
	 * Initializes the reader.
	 *
	 * @param __in The stream to read from.
	 * @throws NullPointerException On null arguments.
	 * @since 2019/05/06
	 */
	public JPEGReader(InputStream __in)
		throws NullPointerException
	{
		if (__in == null)
			throw new NullPointerException("NARG");
		
		this.in = new DataInputStream(__in);
	}
	
	/**
	 * Parses the JPEG.
	 *
	 * @return The resulting image.
	 * @since 2019/05/06
	 */
	public Image parse()
	{
		DataInputStream in = this.in;
		
		Debugging.todoNote("Implement JPEG decoding", new Object[] {});
		return Image.createRGBImage(new int[16], 4, 4, false);
	}
}

