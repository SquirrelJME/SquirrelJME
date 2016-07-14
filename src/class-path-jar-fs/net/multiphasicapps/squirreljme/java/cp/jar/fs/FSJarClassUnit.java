// -*- Mode: Java; indent-tabs-mode: t; tab-width: 4 -*-
// ---------------------------------------------------------------------------
// Multi-Phasic Applications: SquirrelJME
//     Copyright (C) 2013-2016 Steven Gawroriski <steven@multiphasicapps.net>
//     Copyright (C) 2013-2016 Multi-Phasic Applications <multiphasicapps.net>
// ---------------------------------------------------------------------------
// SquirrelJME is under the GNU General Public License v3+, or later.
// For more information see license.mkd.
// ---------------------------------------------------------------------------

package net.multiphasicapps.squirreljme.java.cp.jar.fs;

import java.io.IOException;
import java.nio.channels.FileChannel;
import java.nio.channels.SeekableByteChannel;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardOpenOption;
import net.multiphasicapps.squirreljme.java.cp.ClassUnit;
import net.multiphasicapps.squirreljme.java.cp.ClassUnitProvider;
import net.multiphasicapps.squirreljme.java.cp.jar.JarClassUnit;
import net.multiphasicapps.squirreljme.java.cp.jar.JarClassUnitProvider;

/**
 * This contains a provider for JAR based class units.
 *
 * @since 2016/05/27
 */
public class FSJarClassUnit
	extends JarClassUnit
{
	/** The path to the file system JAR unit. */
	protected final Path path;
	
	/**
	 * Initializes the filesystem backed JAR unit.
	 *
	 * @param __p The path to the JAR file.
	 * @throws NullPointerException On null arguments.
	 * @since 2016/05/27
	 */
	public FSJarClassUnit(Path __p)
		throws NullPointerException
	{
		super(__p.getFileName().toString());
		
		// Set
		this.path = __p;
	}
	
	/**
	 * {@inheritDoc}
	 * @since 2016/05/29
	 */
	@Override
	public final int compareTo(String __s)
		throws NullPointerException
	{
		// Check
		if (__s == null)
			throw new NullPointerException("NARG");
		
		// Get both paths
		Path a = this.path;
		Path b = Paths.get(__s);
		
		// If A is absolute and B is relative, relativize A
		if (a.isAbsolute() && !b.isAbsolute())
			b = b.toAbsolutePath().normalize();
		
		// If A is relative and B is absolute, make A absolute
		else if (!a.isAbsolute() && b.isAbsolute())
			a = a.toAbsolutePath().normalize();
		
		// Represents the same path? return 0
		if (a.equals(b))
			return 0;
		
		// Use the filesystem to compare against, because these class units
		// could be on case insensitive and locale specific filesystems.
		return a.compareTo(b);
	}
	
	/**
	 * {@inheritDoc}
	 * @since 2016/05/28
	 */
	@Override
	protected SeekableByteChannel obtainChannel()
		throws IOException
	{
		return FileChannel.open(path, StandardOpenOption.READ);
	}
}

