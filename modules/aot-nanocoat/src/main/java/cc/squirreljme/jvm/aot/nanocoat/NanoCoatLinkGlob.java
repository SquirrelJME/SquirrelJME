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
import net.multiphasicapps.zip.streamwriter.ZipStreamWriter;

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
	
	/** The base name of the source file. */
	protected final String baseName;
	
	/** The name of this output file. */
	protected final String fileName;
	
	/** The wrapped ZIP file. */
	protected final ZipStreamWriter zip;
	
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
		
		// Determine output names
		this.name = __name;
		this.baseName = Utils.dosFileName(__name);
		this.fileName = this.baseName + ".c";
		
		// Setup ZIP output
		ZipStreamWriter zip = new ZipStreamWriter(__out);
		this.zip = zip;
		
		// Setup output
		try
		{
			this.out = new CSourceWriter(
				new PrintStream(zip.nextEntry(this.fileName),
					true, "utf-8"));
		}
		catch (IOException __e)
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
		// Close out ZIP
		this.zip.flush();
		this.zip.close();
	}
	
	/**
	 * {@inheritDoc}
	 * @since 2023/05/28
	 */
	@Override
	public void finish()
		throws IOException
	{
		// Close out the entry
		this.out.flush();
		this.out.close();
	}
	
	/**
	 * {@inheritDoc}
	 * @since 2023/05/28
	 */
	@Override
	public void initialize()
		throws IOException
	{
		CSourceWriter out = this.out;
		
		// If we are compiling source, include ourselves via the header
		out.preprocessorLine("ifndef", "SJME_C_CH");
		
		// Do the actual include of ourselves
		out.preprocessorLine("define", "SJME_C_CH 1");
		out.preprocessorLine("include", "\"sjmejni.h\"");
		out.preprocessorLine("include", "\"%s\"",
			this.fileName);
		out.preprocessorLine("undef", "SJME_C_CH");
		
		// End trickery
		out.preprocessorLine("endif", "");
	}
}
