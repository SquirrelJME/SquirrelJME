// -*- Mode: Java; indent-tabs-mode: t; tab-width: 4 -*-
// ---------------------------------------------------------------------------
// Multi-Phasic Applications: SquirrelJME
//     Copyright (C) 2013-2016 Steven Gawroriski <steven@multiphasicapps.net>
//     Copyright (C) 2013-2016 Multi-Phasic Applications <multiphasicapps.net>
// ---------------------------------------------------------------------------
// SquirrelJME is under the GNU Affero General Public License v3+, or later.
// For more information see license.mkd.
// ---------------------------------------------------------------------------

package net.multiphasicapps.squirreljme.classpath.jar.fs;

import java.io.IOException;
import java.nio.channels.FileChannel;
import java.nio.channels.SeekableByteChannel;
import java.nio.file.Path;
import java.nio.file.StandardOpenOption;
import net.multiphasicapps.squirreljme.classpath.ClassUnit;
import net.multiphasicapps.squirreljme.classpath.ClassUnitProvider;
import net.multiphasicapps.squirreljme.classpath.jar.JarClassUnit;
import net.multiphasicapps.squirreljme.classpath.jar.JarClassUnitProvider;

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
	 * @since 2016/05/28
	 */
	@Override
	protected SeekableByteChannel obtainChannel()
		throws IOException
	{
		return FileChannel.open(path, StandardOpenOption.READ);
	}
}

