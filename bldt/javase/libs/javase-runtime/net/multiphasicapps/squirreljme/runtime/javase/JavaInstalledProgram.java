// -*- Mode: Java; indent-tabs-mode: t; tab-width: 4 -*-
// ---------------------------------------------------------------------------
// Multi-Phasic Applications: SquirrelJME
//     Copyright (C) Stephanie Gawroriski <xer@multiphasicapps.net>
//     Copyright (C) Multi-Phasic Applications <multiphasicapps.net>
// ---------------------------------------------------------------------------
// SquirrelJME is under the GNU General Public License v3+, or later.
// See license.mkd for licensing and copyright information.
// ---------------------------------------------------------------------------

package net.multiphasicapps.squirreljme.runtime.javase;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.InputStream;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.StandardOpenOption;
import net.multiphasicapps.squirreljme.runtime.cldc.SystemProgramType;
import net.multiphasicapps.squirreljme.runtime.kernel.KernelProgram;
import net.multiphasicapps.tool.manifest.JavaManifest;
import net.multiphasicapps.tool.manifest.JavaManifestAttributes;
import net.multiphasicapps.tool.manifest.JavaManifestKey;
import net.multiphasicapps.zip.blockreader.FileChannelBlockAccessor;
import net.multiphasicapps.zip.blockreader.ZipBlockEntry;
import net.multiphasicapps.zip.blockreader.ZipBlockReader;
import net.multiphasicapps.zip.blockreader.ZipEntryNotFoundException;

/**
 * This represents an installed Java program.
 *
 * @since 2017/12/31
 */
public final class JavaInstalledProgram
	extends KernelProgram
{
	/** The path to the JAR. */
	protected final Path path;
	
	/** The control manifest. */
	protected final Path control;
	
	/**
	 * Initializes the installed program.
	 *
	 * @param __p The path to the installed JAR.
	 * @throws NullPointerException On null arguments.
	 * @since 2017/12/31
	 */
	public JavaInstalledProgram(Path __p)
		throws NullPointerException
	{
		super(__extractIndex(__p));
		
		if (__p == null)
			throw new NullPointerException("NARG");
		
		this.path = __p;
		this.control = __p.resolveSibling(__p.getFileName() + ".MF");
	}
	
	/**
	 * {@inheritDoc}
	 * @since 2017/12/31
	 */
	@Override
	protected String accessControlGet(String __k)
		throws NullPointerException
	{
		if (__k == null)
			throw new NullPointerException("NARG");
		
		try (InputStream in = Files.newInputStream(this.control,
				StandardOpenOption.READ))
		{
			JavaManifest man = new JavaManifest(in);
			return man.getMainAttributes().get(new JavaManifestKey(__k));
		}
		
		// Could not read, treat as null always
		catch (IOException e)
		{
			return null;
		}
	}
	
	/**
	 * {@inheritDoc}
	 * @since 2017/12/31
	 */
	@Override
	protected InputStream accessLoadResource(String __name)
		throws NullPointerException
	{
		if (__name == null)
			throw new NullPointerException("NARG");
		
		// Read from the input
		try (FileChannelBlockAccessor fcn =
				new FileChannelBlockAccessor(this.path);
			ZipBlockReader zip = new ZipBlockReader(fcn);
			InputStream in = zip.open(__name))
		{
			try (ByteArrayOutputStream baos = new ByteArrayOutputStream())
			{
				byte[] buf = new byte[512];
				for (;;)
				{
					int rc = in.read(buf);
					
					if (rc < 0)
						break;
					
					baos.write(buf, 0, rc);
				}
				
				baos.flush();
				return new ByteArrayInputStream(baos.toByteArray());
			}
		}
		
		// No entry exists
		catch (ZipEntryNotFoundException e)
		{
			return null;
		}
		
		// {@squirreljme.error AF04 Could not read ZIP file contents.}
		catch (IOException e)
		{
			throw new RuntimeException("AF04", e);
		}
	}
	
	/**
	 * Extracts the index from the path.
	 *
	 * @param __p The path to extract the index from.
	 * @return The index of the program.
	 * @throws NullPointerException On null arguments.
	 * @since 2017/12/31
	 */
	private static final int __extractIndex(Path __p)
		throws NullPointerException
	{
		if (__p == null)
			throw new NullPointerException("NARG");
		
		// Get base component and assume it ends in JAR
		String base = __p.getFileName().toString();
		return Integer.parseInt(base.substring(0, base.length() - 4));
	}
}

