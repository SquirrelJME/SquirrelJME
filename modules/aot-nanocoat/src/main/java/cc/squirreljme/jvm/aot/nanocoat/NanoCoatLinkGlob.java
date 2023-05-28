// -*- Mode: Java; indent-tabs-mode: t; tab-width: 4 -*-
// ---------------------------------------------------------------------------
// Multi-Phasic Applications: SquirrelJME
//     Copyright (C) Stephanie Gawroriski <xer@multiphasicapps.net>
// ---------------------------------------------------------------------------
// SquirrelJME is under the GNU General Public License v3+, or later.
// See license.mkd for licensing and copyright information.
// ---------------------------------------------------------------------------

package cc.squirreljme.jvm.aot.nanocoat;

import cc.squirreljme.jvm.aot.LinkGlob;
import cc.squirreljme.runtime.cldc.debug.Debugging;
import cc.squirreljme.runtime.cldc.io.PrintStreamWriter;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.io.OutputStreamWriter;
import java.io.PrintStream;
import java.io.UnsupportedEncodingException;

/**
 * Stores the link glob information for NanoCoat.
 *
 * @since 2023/05/28
 */
public class NanoCoatLinkGlob
	implements LinkGlob
{
	/** The name of this glob. */
	protected final String name;
	
	/** The output. */
	protected final CSourceWriter out;
	
	/**
	 * Initializes the link glob.
	 * 
	 * @param __name The name of the glob.
	 * @param __out The final output file.
	 * @throws NullPointerException On null arguments.
	 * @since 2023/05/28
	 */
	public NanoCoatLinkGlob(String __name, OutputStream __out)
		throws NullPointerException
	{
		if (__name == null || __out == null)
			throw new NullPointerException("NARG");
		
		this.name = __name;
		
		// Setup output
		try
		{
			this.out = new CSourceWriter(
				new PrintStream(__out, true, "utf-8"));
		}
		catch (UnsupportedEncodingException __e)
		{
			throw new RuntimeException(__e);
		}
	}
	
	/**
	 * {@inheritDoc}
	 * @since 2023/05/28
	 */
	@Override
	public void close()
		throws IOException
	{
		throw Debugging.todo();
	}
	
	/**
	 * {@inheritDoc}
	 * @since 2023/05/28
	 */
	@Override
	public void finish()
		throws IOException
	{
		throw Debugging.todo();
	}
}
