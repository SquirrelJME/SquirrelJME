// -*- Mode: Java; indent-tabs-mode: t; tab-width: 4 -*-
// ---------------------------------------------------------------------------
// SquirrelJME
//     Copyright (C) Stephanie Gawroriski <xer@multiphasicapps.net>
// ---------------------------------------------------------------------------
// SquirrelJME is under the Mozilla Public License Version 2.0.
// See license.mkd for licensing and copyright information.
// ---------------------------------------------------------------------------

package cc.squirreljme.runtime.lcdui.image;

import cc.squirreljme.jvm.mle.callbacks.NativeImageLoadCallback;
import cc.squirreljme.runtime.cldc.debug.Debugging;
import java.io.DataInputStream;
import java.io.InputStream;

/**
 * This class is used to process JPEG image files.
 *
 * @since 2019/05/06
 */
public final class JPEGReader
	implements ImageReader
{
	/** The input data. */
	protected final DataInputStream in;
	
	/** The image loader to use. */
	protected final NativeImageLoadCallback loader;
	
	/**
	 * Initializes the reader.
	 *
	 * @param __in The stream to read from.
	 * @param __loader The image loader to use.
	 * @throws NullPointerException On null arguments.
	 * @since 2019/05/06
	 */
	public JPEGReader(InputStream __in, NativeImageLoadCallback __loader)
		throws NullPointerException
	{
		if (__in == null || __loader == null)
			throw new NullPointerException("NARG");
		
		this.in = new DataInputStream(__in);
		this.loader = __loader;
	}
	
	/**
	 * Parses the JPEG.
	 *
	 * @since 2019/05/06
	 */
	@Override
	public void parse()
	{
		DataInputStream in = this.in;
		NativeImageLoadCallback loader = this.loader;
		
		Debugging.todoNote("Implement JPEG decoding", new Object[] {});
		
		loader.initialize(4, 4,
			false, false);
		loader.addImage(new int[16], 0, 16,
			0, false);
	}
}

