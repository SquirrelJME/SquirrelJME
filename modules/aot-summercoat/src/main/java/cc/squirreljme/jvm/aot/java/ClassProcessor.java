// -*- Mode: Java; indent-tabs-mode: t; tab-width: 4 -*-
// ---------------------------------------------------------------------------
// Multi-Phasic Applications: SquirrelJME
//     Copyright (C) Stephanie Gawroriski <xer@multiphasicapps.net>
// ---------------------------------------------------------------------------
// SquirrelJME is under the GNU General Public License v3+, or later.
// See license.mkd for licensing and copyright information.
// ---------------------------------------------------------------------------

package cc.squirreljme.jvm.aot.java;

import cc.squirreljme.jvm.aot.CompilationException;
import cc.squirreljme.runtime.cldc.debug.Debugging;
import java.io.DataInputStream;
import java.io.IOException;
import java.io.InputStream;
import net.multiphasicapps.classfile.ClassFile;
import net.multiphasicapps.classfile.InvalidClassFormatException;

/**
 * Processes a single Java class.
 *
 * @since 2022/08/04
 */
public class ClassProcessor
{
	/** The handler for classes. */
	protected final ClassHandler handler;
	
	/**
	 * Initializes the class processor.
	 * 
	 * @param __handler The handler used.
	 * @throws NullPointerException On null arguments.
	 * @since 2022/09/07
	 */
	public ClassProcessor(ClassHandler __handler)
		throws NullPointerException
	{
		if (__handler == null)
			throw new NullPointerException("NARG");
		
		this.handler = __handler;
	}
	
	/**
	 * Processes the given class.
	 * 
	 * @param __inClass The input class to process.
	 * @throws IOException On read errors.
	 * @throws NullPointerException On null arguments.
	 * @since 2022/09/07
	 */
	public void process(InputStream __inClass)
		throws IOException, NullPointerException
	{
		if (__inClass == null)
			throw new NullPointerException("NARG");
		
		// This could fail
		try
		{
			// Decode the class file before we start processing it
			ClassFile classFile = ClassFile.decode(__inClass);
			
			throw Debugging.todo();
		}
		
		// Wrap this exception
		catch (InvalidClassFormatException e)
		{
			throw new CompilationException(e);
		}
	}
}
