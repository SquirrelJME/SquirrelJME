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
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import net.multiphasicapps.classfile.ClassFile;
import net.multiphasicapps.classfile.ClassName;
import net.multiphasicapps.classfile.InvalidClassFormatException;
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
	/** The class loader. */
	protected final SpringClassLoader classloader;
	
	/** Threads which are available. */
	private final List<SpringThread> _threads =
		new ArrayList<>();
	
	/** The next thread ID to use. */
	private volatile int _nextthreadid;
	
	/**
	 * Initializes the virtual machine.
	 *
	 * @param __cl The class loader.
	 * @throws NullPointerException On null arguments.
	 * @since 2018/09/03
	 */
	public SpringMachine(SpringClassLoader __cl)
		throws NullPointerException
	{
		if (__cl == null)
			throw new NullPointerException("NARG");
		
		this.classloader = __cl;
	}
	
	/**
	 * Creates a new thread within the virtual machine.
	 *
	 * @param __n The name of the thread.
	 * @return The newly created thread.
	 * @throws NullPointerException On null arguments.
	 * @since 2018/09/01
	 */
	public final SpringThread createThread(String __n)
		throws NullPointerException
	{
		if (__n == null)
			throw new NullPointerException("NARG");
		
		// Store thread
		List<SpringThread> threads = this._threads;
		synchronized (threads)
		{
			// Initialize new thread
			SpringThread rv = new SpringThread(++this._nextthreadid, __n);
			
			// Store thread
			threads.add(rv);
			return rv;
		}
	}
}

