// -*- Mode: Java; indent-tabs-mode: t; tab-width: 4 -*-
// ---------------------------------------------------------------------------
// Multi-Phasic Applications: SquirrelJME
//     Copyright (C) Stephanie Gawroriski <xer@multiphasicapps.net>
//     Copyright (C) Multi-Phasic Applications <multiphasicapps.net>
// ---------------------------------------------------------------------------
// SquirrelJME is under the GNU General Public License v3+, or later.
// See license.mkd for licensing and copyright information.
// ---------------------------------------------------------------------------

package cc.squirreljme.springcoat.vm;

import cc.squirreljme.builder.support.Binary;
import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.InputStream;
import java.io.IOException;
import net.multiphasicapps.classfile.ClassFile;
import net.multiphasicapps.classfile.ClassName;
import net.multiphasicapps.classfile.InvalidClassFormatException;
import net.multiphasicapps.zip.blockreader.ZipBlockReader;
import net.multiphasicapps.zip.blockreader.ZipBlockEntry;
import net.multiphasicapps.zip.blockreader.ZipEntryNotFoundException;

/**
 * This class acts as the equivalent to {@code ClassLoader} in that it manages
 * the class path and the eventual loading of classes.
 *
 * @since 2018/09/01
 */
public final class SpringClassLoader
{
	/** The class path for the machine. */
	private final Binary[] _classpath;
	
	/**
	 * Initializes the class loader.
	 *
	 * @param __classpath The classpath.
	 * @throws NullPointerException On null arguments.
	 * @since 2018/09/01
	 */
	public SpringClassLoader(Binary... __classpath)
		throws NullPointerException
	{
		for (Binary b : __classpath = (__classpath == null ? new Binary[0] :
			__classpath.clone()))
			if (b == null)
				throw new NullPointerException("NARG");
		this._classpath = __classpath;
	}
	
	/**
	 * This goes through the classpath and loads the specified class file for
	 * the given class.
	 *
	 * @param __cn The class to load.
	 * @return The loaded class file data.
	 * @throws NullPointerException On null arguments.
	 * @throws SpringClassFormatException If the class is not formatted
	 * properly.
	 * @throws SpringClassNotFoundException If the class was not found.
	 * @since 2018/09/01
	 */
	public final ClassFile loadClassFile(ClassName __cn)
		throws NullPointerException, SpringClassFormatException,
			SpringClassNotFoundException
	{
		if (__cn == null)
			throw new NullPointerException("NARG");
		
		// Debug
		todo.DEBUG.note("Loading `%s`...", __cn);
		
		// This is the class that is read, in binary form
		String fileform = __cn.toString() + ".class";
		
		// Otherwise we need to go through every single binary to find
		// the class we want, which can take awhile
		byte[] data = null;
		for (Binary b : this._classpath)
			try (ZipBlockReader zip = b.zipBlock())
			{
				// Does not exist?
				ZipBlockEntry entry = zip.get(fileform);
				if (entry == null)
					continue;
				
				// Read in the data
				byte[] buf = new byte[512];
				try (ByteArrayOutputStream baos =
					new ByteArrayOutputStream(1024);
					InputStream in = entry.open())
				{
					for (;;)
					{
						int rc = in.read(buf);
						
						if (rc < 0)
						{
							baos.flush();
							data = baos.toByteArray();
							break;
						}
						
						baos.write(buf, 0, rc);
					}
				}
				
				break;
			}
			catch (ZipEntryNotFoundException e)
			{
			}
			catch (IOException e)
			{
				// {@squirreljme.error BK03 Failed to read from the class
				// path.}
				throw new SpringException("BK03", e);
			}
		
		// {@squirreljme.error BK02 Could not locate the specified class.
		// (The class which was not found; The class file which was
		// attempted to be located)}
		if (data == null)
			throw new SpringClassNotFoundException(__cn, String.format(
				"BK02 %s %s", __cn, fileform));
		
		// Decode class file
		ClassFile cf;
		try (ByteArrayInputStream bais = new ByteArrayInputStream(data))
		{
			return ClassFile.decode(bais);
		}
		catch (IOException e)
		{
			// {@squirreljme.error BK05 Could not read from the source
			// class file. (The class being read)}
			throw new SpringVirtualMachineException(String.format(
				"BK05 %s", __cn), e);
		}
		catch (InvalidClassFormatException e)
		{
			// {@squirreljme.error BK04 The class is not formatted
			// correctly. (The class being read)}
			throw new SpringClassFormatException(__cn, String.format(
				"BK04 %s", __cn), e);
		}
	}
}

