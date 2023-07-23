// -*- Mode: Java; indent-tabs-mode: t; tab-width: 4 -*-
// ---------------------------------------------------------------------------
// Multi-Phasic Applications: SquirrelJME
//     Copyright (C) Stephanie Gawroriski <xer@multiphasicapps.net>
// ---------------------------------------------------------------------------
// SquirrelJME is under the Mozilla Public License Version 2.0.
// See license.mkd for licensing and copyright information.
// ---------------------------------------------------------------------------

package cc.squirreljme.jvm.aot.nanocoat;

import cc.squirreljme.c.CExpressionBuilder;
import cc.squirreljme.c.CFile;
import cc.squirreljme.c.CFileName;
import cc.squirreljme.c.CPPBlock;
import cc.squirreljme.c.CSourceWriter;
import cc.squirreljme.jvm.aot.LinkGlob;
import cc.squirreljme.jvm.aot.nanocoat.common.Constants;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.OutputStream;
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
	protected final CFileName headerFileName;
	
	/** The wrapped ZIP file. */
	protected final ZipStreamWriter zip;
	
	/** Raw header output data. */
	protected final ByteArrayOutputStream rawHeaderOut;
	
	/** The output. */
	protected final CFile headerOut;
	
	/**
	 * Initializes the link glob.
	 * 
	 * @param __name The name of the glob.
	 * @param __headerOut The final output file.
	 * @throws NullPointerException On null arguments.
	 * @since 2023/05/28
	 */
	public NanoCoatLinkGlob(String __name, OutputStream __headerOut)
		throws NullPointerException
	{
		if (__name == null || __headerOut == null)
			throw new NullPointerException("NARG");
		
		// Determine output names
		this.name = __name;
		this.baseName = Utils.basicFileName(__name);
		this.headerFileName = CFileName.of(this.baseName + ".h");
		
		// Setup ZIP output
		ZipStreamWriter zip = new ZipStreamWriter(__headerOut);
		this.zip = zip;
		
		// Setup output
		try
		{
			this.rawHeaderOut = new ByteArrayOutputStream();
			this.headerOut = Utils.cFile(this.rawHeaderOut);
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
		// Need to write the raw header output for this class
		try (OutputStream out = this.zip.nextEntry(
			this.inDirectory(this.headerFileName)))
		{
			// Close out the header entry before we write it fully
			this.headerOut.flush();
			this.headerOut.close();
			
			// Copy to the ZIP
			out.write(this.rawHeaderOut.toByteArray());
			out.flush();
		}
	}
	
	/**
	 * Returns the file name in the directory.
	 *
	 * @param __file The file to place.
	 * @return The file in the directory.
	 * @throws NullPointerException On null arguments.
	 * @since 2023/07/17
	 */
	public String inDirectory(CFileName __file)
		throws NullPointerException
	{
		if (__file == null)
			throw new NullPointerException("NARG");
		
		return this.inDirectory(__file.toString());
	}
	
	/**
	 * Returns the file name in the directory.
	 *
	 * @param __file The file to place.
	 * @return The file in the directory.
	 * @throws NullPointerException On null arguments.
	 * @since 2023/07/17
	 */
	public String inDirectory(String __file)
		throws NullPointerException
	{
		if (__file == null)
			throw new NullPointerException("NARG");
		
		return String.format("%s/%s", this.baseName, __file);
	}
	
	/**
	 * {@inheritDoc}
	 * @since 2023/05/28
	 */
	@Override
	public void initialize()
		throws IOException
	{
		CSourceWriter out = this.headerOut;
		
		// Write header
		Utils.header(out);
		
		// If we are compiling source, include ourselves via the header
		try (CPPBlock block = out.preprocessorIf(CExpressionBuilder.builder()
				.not()
				.preprocessorDefined(Constants.CODE_GUARD)
				.build()))
		{
			// Do not do this again
			out.preprocessorDefine(Constants.CODE_GUARD,
				CExpressionBuilder.builder()
					.number(1)
				.build());
			
			// Do the actual include of ourselves
			out.preprocessorInclude(Constants.SJME_JNI_HEADER);
			out.preprocessorInclude(this.headerFileName);
			
			// Stop doing this, so we can continue back to normal source code
			out.preprocessorUndefine(Constants.CODE_GUARD);
		}
	}
}
