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
import net.multiphasicapps.squirreljme.jit.ClusterIdentifier;
import net.multiphasicapps.squirreljme.jit.JITConfig;
import net.multiphasicapps.squirreljme.jit.JITException;
import net.multiphasicapps.squirreljme.jit.LinkTable;

/**
 * This class reads the Java class file format and then passes that to the
 * JIT compiler which recompiles the given class.
 *
 * @since 2017/05/29
 */
public final class ClassCompiler
	implements Runnable
{
	/** The magic number of the class file. */
	private static final int _MAGIC_NUMBER =
		0xCAFEBABE;
	
	/** The owning configuration. */
	protected final JITConfig config;
	
	/** The input stream containing the class data. */
	protected final DataInputStream in;
	
	/** The cluster this class is within. */
	protected final ClusterIdentifier cluster;
	
	/** The link table to link into. */
	protected final LinkTable linktable;
	
	/**
	 * Creates an instance of the compiler for the given class file.
	 *
	 * @param __jc The JIT configuration.
	 * @param __is The stream containing the class data to compile.
	 * @param __ci The cluster the class is in.
	 * @param __lt The link table which is given the compiled class data.
	 * @return The compilation task.
	 * @throws NullPointerException On null arguments.
	 * @since 2017/06/02
	 */
	public ClassCompiler(JITConfig __jc, InputStream __is,
		ClusterIdentifier __ci, LinkTable __lt)
		throws NullPointerException
	{
		// Check
		if (__jc == null || __is == null || __ci == null || __lt == null)
			throw new NullPointerException("NARG");
		
		// Set
		this.config = __jc;
		this.in = new DataInputStream(__is);
		this.cluster = __ci;
		this.linktable = __lt;
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
			// {@squirreljme.error JI06 Invalid magic number read from the start
			// of the class file. (The read magic number; The expected magic
			// number)}
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
			
			throw new todo.TODO();
		}
		
		// {@squirreljme.error JI07 Read error reading the class.}
		catch (IOException e)
		{
			throw new JITException("JI07", e);
		}
	}
}

