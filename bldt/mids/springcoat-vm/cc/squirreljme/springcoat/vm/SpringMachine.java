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
import java.io.ByteArrayOutputStream;
import java.io.InputStream;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;
import net.multiphasicapps.classfile.ClassName;
import net.multiphasicapps.zip.blockreader.ZipBlockReader;
import net.multiphasicapps.zip.blockreader.ZipBlockEntry;
import net.multiphasicapps.zip.blockreader.ZipEntryNotFoundException;

/**
 * This class contains the instance of the SpringCoat virtual machine and has
 * a classpath along with all the needed storage for variables and such.
 *
 * @since 2018/07/29
 */
public final class SpringMachine
{
	/** The class path for the machine. */
	private final Binary[] _classpath;
	
	/** Classes which have been loaded. */
	private final Map<ClassName, SpringClass> _classes =
		new HashMap<>();
	
	/**
	 * Initializes the machine.
	 *
	 * @param __classpath The classpath.
	 * @throws NullPointerException On null arguments.
	 * @since 2018/08/04
	 */
	public SpringMachine(Binary... __classpath)
		throws NullPointerException
	{
		for (Binary b : __classpath = (__classpath == null ? new Binary[0] :
			__classpath.clone()))
			if (b == null)
				throw new NullPointerException("NARG");
		this._classpath = __classpath;
	}
	
	/**
	 * Locates the specified class in the machine.
	 *
	 * @param __cn The class to load.
	 * @return The found class.
	 * @throws NullPointerException On null arguments.
	 * @throws SpringClassNotFoundException If the class was not found.
	 * @since 2018/08/05
	 */
	public final SpringClass locateClass(ClassName __cn)
		throws NullPointerException
	{
		if (__cn == null)
			throw new NullPointerException("NARG");
		
		System.err.printf("DEBUG -- locateClass(%s)%n", __cn);
		
		// Lock on classes
		Map<ClassName, SpringClass> classes = this._classes;
		synchronized (classes)
		{
			// If the class has already been obtained then use that
			SpringClass rv = classes.get(__cn);
			if (rv != null)
				return rv;
			
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
						int rc = in.read(buf);
						
						if (rc < 0)
						{
							baos.flush();
							data = baos.toByteArray();
							break;
						}
						
						baos.write(buf, 0, rc);
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
			
			// {@squirreljme.error BK01 Could not locate the specified class.
			// (The class which was not found; The class file which was
			// attempted to be located)}
			if (data == null)
				throw new SpringClassNotFoundException(__cn, String.format(
					"BK01 %s %s", __cn, fileform));
			
			throw new todo.TODO();
		}
	}
	
	/**
	 * Locates the specified class in the machine.
	 *
	 * @param __cn The class to load.
	 * @return The found class.
	 * @throws NullPointerException On null arguments.
	 * @throws SpringClassNotFoundException If the class was not found.
	 * @since 2018/08/05
	 */
	public final SpringClass locateClass(String __cn)
		throws NullPointerException
	{
		if (__cn == null)
			throw new NullPointerException("NARG");
		
		return this.locateClass(new ClassName(__cn));
	}
}

