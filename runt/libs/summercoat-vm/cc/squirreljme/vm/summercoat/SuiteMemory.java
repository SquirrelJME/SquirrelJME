// -*- Mode: Java; indent-tabs-mode: t; tab-width: 4 -*-
// ---------------------------------------------------------------------------
// Multi-Phasic Applications: SquirrelJME
//     Copyright (C) Stephanie Gawroriski <xer@multiphasicapps.net>
// ---------------------------------------------------------------------------
// SquirrelJME is under the GNU General Public License v3+, or later.
// See license.mkd for licensing and copyright information.
// ---------------------------------------------------------------------------

package cc.squirreljme.vm.summercoat;

import dev.shadowtail.classfile.mini.Minimizer;
import java.io.ByteArrayOutputStream;
import java.io.DataOutputStream;
import java.io.InputStream;
import java.io.IOException;
import cc.squirreljme.vm.VMClassLibrary;
import cc.squirreljme.vm.VMException;
import cc.squirreljme.vm.VMSuiteManager;
import net.multiphasicapps.classfile.ClassFile;

/**
 * This represents the single virtual memory space for suite memory.
 *
 * @since 2019/04/21
 */
public final class SuiteMemory
	implements ReadableMemory
{
	/** The suite manager. */
	protected final VMSuiteManager suites;
	
	/** The library this uses. */
	protected final String libname;
	
	/** The offset to this memory region. */
	protected final int offset;
	
	/** Was this initialized? */
	private volatile boolean _didinit;
	
	/** Memory for this suite. */
	private volatile ByteArrayMemory _memory;
	
	/**
	 * Initializes the suite memory.
	 *
	 * @param __off The memory offset for this suite.
	 * @param __sm The suite manager.
	 * @param __ln The library name.
	 * @throws NullPointerException On null arguments.
	 */
	public SuiteMemory(int __off, VMSuiteManager __sm, String __ln)
		throws NullPointerException
	{
		if (__sm == null || __ln == null)
			throw new NullPointerException("NARG");
		
		this.suites = __sm;
		this.libname = __ln;
		this.offset = __off;
	}
	
	/**
	 * {@inheritDoc}
	 * @since 2019/04/21
	 */
	@Override
	public int memReadInt(int __addr)
	{
		throw new todo.TODO();
	}
	
	/**
	 * {@inheritDoc}
	 * @since 2019/04/21
	 */
	@Override
	public int memRegionOffset()
	{
		return this.offset;
	}
	
	/**
	 * {@inheritDoc}
	 * @since 2019/04/21
	 */
	@Override
	public final int memRegionSize()
	{
		return SuitesMemory.SUITE_CHUNK_SIZE;
	}
	
	/**
	 * Initializes all the memory and suites here.
	 *
	 * @throws IOException On read/write errors.
	 * @since 2019/04/21
	 */
	final void __init()
		throws IOException
	{
		// Do not initialize twice!
		if (this._didinit)
			return;
		
		// Load the class library
		String libname = this.libname;
		VMClassLibrary clib = this.suites.loadLibrary(libname);
		
		// Need to build a resource index
		String[] lsr = clib.listResources();
		int rn = lsr.length;
		
		// Resource table
		ByteArrayOutputStream ibytes = new ByteArrayOutputStream();
		DataOutputStream idos = new DataOutputStream(ibytes);
		
		// Data table containing minified classes and such
		ByteArrayOutputStream dbytes = new ByteArrayOutputStream();
		DataOutputStream ddos = new DataOutputStream(dbytes);
		
		// Relative offset to the stream data, just to fit the resource table
		int reloff = (rn * 12) + 12;
		
		// Build all table regions
		for (int i = 0; i < rn; i++)
		{
			String en = lsr[i];
			
			// Base string start position
			int srs = ddos.size();
			
			// Write null terminated entry name, ignore Unicode
			for (int s = 0, sn = en.length(); s < sn; s++)
				ddos.write(en.charAt(s));
			ddos.write(0);
			
			// Write padding
			while ((ddos.size() & 3) != 0)
				ddos.write(0);
			
			// Base data start position
			int drs = ddos.size();
			
			// Copy resource data or convert class file
			try (InputStream in = clib.resourceAsStream(en))
			{
				// Debug
				todo.DEBUG.note("Loading %s:%s", libname, en);
				
				// Load, compile, and minimize class
				if (en.endsWith(".class"))
				{
					Minimizer.minimize(ClassFile.decode(in), ddos);
				}
				
				// Copy resource
				else
				{
					byte[] buf = new byte[512];
					for (;;)
					{
						int rc = in.read(buf);
						
						if (rc < 0)
							break;
						
						ddos.write(buf, 0, rc);
					}
				}
			}
			
			// Write table information
			idos.writeInt(reloff + srs);
			idos.writeInt(reloff + drs);
			idos.writeInt(ddos.size() - drs);
			
			// Write padding
			while ((ddos.size() & 3) != 0)
				ddos.write(0);
		}
		
		// End padding
		idos.writeInt(0xFFFFFFFF);
		idos.writeInt(0xFFFFFFFF);
		idos.writeInt(0xFFFFFFFF);
		
		// Append data area to the index area and build the memory region
		idos.write(dbytes.toByteArray());
		this._memory = new ByteArrayMemory(this.offset, ibytes.toByteArray());
		
		// Was initialized
		this._didinit = true;
	}
}

