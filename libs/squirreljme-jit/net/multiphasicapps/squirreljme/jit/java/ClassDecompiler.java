// -*- Mode: Java; indent-tabs-mode: t; tab-width: 4 -*-
// ---------------------------------------------------------------------------
// Multi-Phasic Applications: SquirrelJME
//     Copyright (C) Steven Gawroriski <steven@multiphasicapps.net>
//     Copyright (C) Multi-Phasic Applications <multiphasicapps.net>
// ---------------------------------------------------------------------------
// SquirrelJME is under the GNU General Public License v3+, or later.
// See license.mkd for licensing and copyright information.
// ---------------------------------------------------------------------------

package net.multiphasicapps.squirreljme.jit.java;

import java.io.DataInputStream;
import java.io.InputStream;
import java.io.IOException;
import net.multiphasicapps.squirreljme.jit.bin.ClassName;
import net.multiphasicapps.squirreljme.jit.bin.LinkerState;
import net.multiphasicapps.squirreljme.jit.JITConfig;
import net.multiphasicapps.squirreljme.jit.JITException;

/**
 * This class reads the Java class file format and then passes that to the
 * JIT compiler which recompiles the given class.
 *
 * @since 2017/05/29
 */
public final class ClassDecompiler
	implements Runnable
{
	/** The magic number of the class file. */
	private static final int _MAGIC_NUMBER =
		0xCAFEBABE;
	
	/** The owning configuration. */
	protected final JITConfig config;
	
	/** The input stream containing the class data. */
	protected final DataInputStream in;
	
	/** The linker state to write into. */
	protected final LinkerState linkerstate;
	
	/**
	 * Creates an instance of the compiler for the given class file.
	 *
	 * @param __ls The linker state which contains all of the output executable
	 * information.
	 * @param __is The stream containing the class data to compile.
	 * @return The compilation task.
	 * @throws NullPointerException On null arguments.
	 * @since 2017/06/02
	 */
	public ClassDecompiler(LinkerState __ls, InputStream __is)
		throws NullPointerException
	{
		// Check
		if (__is == null || __ls == null)
			throw new NullPointerException("NARG");
		
		// Set
		this.config = __ls.config();
		this.in = new DataInputStream(__is);
		this.linkerstate = __ls;
	}
	
	/**
	 * {@inheritDoc}
	 * @since 2017/06/02
	 */
	@Override
	public void run()
	{
		try
		{
			// {@squirreljme.error JI06 Invalid magic number read from the
			// start of the class file. (The read magic number; The expected
			// magic number)}
			DataInputStream in = this.in;
			int mnum;
			if ((mnum = in.readInt()) != _MAGIC_NUMBER)
				throw new JITException(String.format("JI06 %08x %08x", mnum,
					_MAGIC_NUMBER));
			
			// {@squirreljme.error JI08 The version number of the input class
			// file is not valid. (The version number)}
			int cver = in.readShort() | (in.readShort() << 16);
			ClassVersion version = ClassVersion.findVersion(cver);
			if (version == null)
				throw new JITException(String.format("JI08 %d.%d",
					cver >>> 16, (cver & 0xFFFF)));
			
			// Decode the class
			Pool pool = new Pool(in);
			
			// Decode flags
			ClassFlags classflags = new ClassFlags(in.readUnsignedShort());
			
			// Name of the current class
			ClassName thisname = pool.<ClassName>require(ClassName.class,
				in.readUnsignedShort());
			
			// Super class name
			ClassName supername = pool.<ClassName>get(ClassName.class,
				in.readUnsignedShort());
			
			// {@squirreljme.error JI0s Either Object has a superclass which it
			// cannot extend any class or any other class does not have a super
			// class. (The current class name; The super class name)}
			if (thisname.equals(new ClassName("java/lang/Object")) !=
				(supername == null))
				throw new JITException(String.format("JI0s %s %s", thisname,
					supername));
			
			// Read interfaces
			int icount = in.readUnsignedShort();
			ClassName[] interfaces = new ClassName[icount];
			for (int i = 0; i < icount; i++)
				interfaces[i] = pool.<ClassName>require(ClassName.class,
					in.readUnsignedShort());
			
			// Read in fields
			int nf = in.readUnsignedShort();
			for (int i = 0; i < nf; i++)
				throw new todo.TODO();
			
			// Read in methods
			int nm = in.readUnsignedShort();
			for (int i = 0; i < nm; i++)
				throw new todo.TODO();
			
			// Handle attributes
			if (true)
				throw new todo.TODO();
			
			// {@squirreljme.error JI12 Expected end of the class to follow the
			// attributes in the class. (The name of this class)}
			if (in.read() >= 0)
				throw new JITException(String.format("JI12 %s", thisname));
			
			throw new todo.TODO();
		}
		
		// {@squirreljme.error JI07 Read error reading the class.}
		catch (IOException e)
		{
			throw new JITException("JI07", e);
		}
	}
}

