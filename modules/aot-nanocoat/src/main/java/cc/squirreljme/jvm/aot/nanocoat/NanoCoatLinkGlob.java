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
import cc.squirreljme.runtime.cldc.util.SortedTreeSet;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.io.PrintStream;
import java.util.Set;
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
	
	/** The name of this root source output file. */
	protected final CFileName rootSourceFileName;
	
	/** The name of this output file. */
	protected final CFileName headerFileName;
	
	/** The wrapped ZIP file. */
	protected final ZipStreamWriter zip;
	
	/** Raw header output data. */
	protected final ByteArrayOutputStream rawHeaderOut;
	
	/** The output for the C header. */
	protected final CFile headerOut;
	
	/** Raw root source output data. */
	protected final ByteArrayOutputStream rawRootSourceOut;
	
	/** The output for the C source root. */
	protected final CFile rootSourceOut;
	
	/** Files which have been output. */
	protected final Set<String> outputFiles =
		new SortedTreeSet<>();
	
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
		this.rootSourceFileName = CFileName.of(this.baseName + ".c");
		
		// Setup ZIP output
		ZipStreamWriter zip = new ZipStreamWriter(__headerOut);
		this.zip = zip;
		
		// Setup output
		try
		{
			// Setup header writing
			this.rawHeaderOut = new ByteArrayOutputStream();
			this.headerOut = Utils.cFile(this.rawHeaderOut);
			
			// Setup source root writing
			this.rawRootSourceOut = new ByteArrayOutputStream();
			this.rootSourceOut = Utils.cFile(this.rawRootSourceOut);
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
		ZipStreamWriter zip = this.zip;
		try (OutputStream out = zip.nextEntry(
			this.inDirectory(this.headerFileName)))
		{
			// Close out the header entry before we write it fully
			this.headerOut.flush();
			this.headerOut.close();
			
			// Copy to the ZIP
			out.write(this.rawHeaderOut.toByteArray());
			out.flush();
		}
		
		// Write root source as well
		try (OutputStream out = zip.nextEntry(
			this.inDirectory(this.rootSourceFileName)))
		{
			// Close out the header entry before we write it fully
			this.rootSourceOut.flush();
			this.rootSourceOut.close();
			
			// Copy to the ZIP
			out.write(this.rawRootSourceOut.toByteArray());
			out.flush();
		}
		
		// Output the CMake file accordingly
		try (OutputStream rawCMake = zip.nextEntry(
			this.inDirectory("CMakeLists.txt"));
			PrintStream cmake = new PrintStream(rawCMake, true,
				"utf-8"))
		{
			// Start with header
			Utils.headerCMake(cmake);
			
			// Include functions and macros header
			cmake.println("include(\"${CMAKE_SOURCE_DIR}/cmake/" +
				"rom-macros-and-functions.cmake\"\n\tNO_POLICY_SCOPE)");
			cmake.println();
			
			// Generate list of files
			cmake.printf("set(%sFiles", this.baseName);
			cmake.println();
			for (String outputFile : this.outputFiles)
			{
				cmake.printf("\t\"%s\"", outputFile);
				cmake.println();
			}
			cmake.println("\t)");
			cmake.println();
			
			// Use macro for all the files
			cmake.printf("squirreljme_romLibrary(%s \"${%sFiles}\")",
				this.baseName, this.baseName);
			cmake.println();
			cmake.println();
			
			// Make sure ZIP entry is written
			cmake.flush();
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
		CSourceWriter headerOut = this.headerOut;
		CSourceWriter rootSourceOut = this.rootSourceOut;
		
		// Write headers
		Utils.headerC(headerOut);
		Utils.headerC(rootSourceOut);
		
		// Make sure the output files has the actual root source
		this.outputFiles.add(this.rootSourceFileName.toString());
		
		// If we are compiling source, include ourselves via the header
		try (CPPBlock block = headerOut.preprocessorIf(
				CExpressionBuilder.builder()
					.not()
					.preprocessorDefined(Constants.CODE_GUARD)
					.build()))
		{
			// Do not do this again
			headerOut.preprocessorDefine(Constants.CODE_GUARD,
				CExpressionBuilder.builder()
					.number(1)
				.build());
			
			// Do the actual include of ourselves
			headerOut.preprocessorInclude(Constants.SJME_JNI_HEADER);
			headerOut.preprocessorInclude(this.headerFileName);
			
			// Stop doing this, so we can continue back to normal source code
			headerOut.preprocessorUndefine(Constants.CODE_GUARD);
		}
	}
}
