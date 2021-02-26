// -*- Mode: Java; indent-tabs-mode: t; tab-width: 4 -*-
// ---------------------------------------------------------------------------
// Multi-Phasic Applications: SquirrelJME
//     Copyright (C) Stephanie Gawroriski <xer@multiphasicapps.net>
// ---------------------------------------------------------------------------
// SquirrelJME is under the GNU General Public License v3+, or later.
// See license.mkd for licensing and copyright information.
// ---------------------------------------------------------------------------

package cc.squirreljme.emulator;

import cc.squirreljme.jvm.mle.brackets.JarPackageBracket;
import cc.squirreljme.jvm.mle.exceptions.MLECallError;
import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.nio.file.Path;
import net.multiphasicapps.zip.blockreader.FileChannelBlockAccessor;
import net.multiphasicapps.zip.blockreader.ZipBlockEntry;
import net.multiphasicapps.zip.blockreader.ZipBlockReader;
import net.multiphasicapps.zip.blockreader.ZipEntryNotFoundException;

/**
 * This is an emulation of {@link JarPackageBracket} so that it can access
 * the path and such accordingly.
 *
 * @since 2020/10/31
 */
public class EmulatedJarPackageBracket
	implements JarPackageBracket
{
	/** The JAR path to use. */
	public final Path path;
	
	/**
	 * Initializes the emulated bracket.
	 * 
	 * @param __path The path to target.
	 * @throws NullPointerException On null arguments.
	 * @since 2020/10/31
	 */
	public EmulatedJarPackageBracket(Path __path)
		throws NullPointerException
	{
		if (__path == null)
			throw new NullPointerException("NARG");
		
		this.path = __path;
	}
	
	/**
	 * Opens the specified resource.
	 * 
	 * @param __rc The resource to open.
	 * @return The stream to the resource data.
	 * @throws NullPointerException On null arguments.
	 * @since 2020/10/31
	 */
	public InputStream openResource(String __rc)
		throws NullPointerException
	{
		if (__rc == null)
			throw new NullPointerException("NARG");
		
		// Directly access the ZIP file using our own ZIP code
		try (ZipBlockReader zip = new ZipBlockReader(
			new FileChannelBlockAccessor(this.path)))
		{
			ZipBlockEntry entry = zip.get(__rc);
			
			// Copy contents of the entry into memory
			ByteArrayOutputStream baos = new ByteArrayOutputStream();
			try (InputStream in = entry.open())
			{
				byte[] buf = new byte[4096];
				for (;;)
				{
					int rc = in.read(buf);
					
					if (rc < 0)
						break;
					
					baos.write(buf, 0, rc);
				}
			}
			
			return new ByteArrayInputStream(baos.toByteArray());
		}
		catch (ZipEntryNotFoundException ignored)
		{
			return null;
		}
		catch (IOException e)
		{
			throw new MLECallError("I/O Exception.", e);
		}
	}
}
