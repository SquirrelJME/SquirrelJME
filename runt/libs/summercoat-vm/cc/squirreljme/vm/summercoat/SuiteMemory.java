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
import dev.shadowtail.jarfile.JarMinimizer;
import dev.shadowtail.jarfile.MinimizedJarHeader;
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
	extends AbstractReadableMemory
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
	private volatile ReadableMemory _memory;
	
	/** Header for this JAR. */
	volatile MinimizedJarHeader _jarheader;
	
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
	public int memReadByte(int __addr)
	{
		// Initialize?
		if (!this._didinit)
			try
			{
				this.__init();
			}
			catch (IOException e)
			{
				throw new RuntimeException(e);
			}
		
		// Forward
		return this._memory.memReadByte(__addr);
	}
	
	/**
	 * {@inheritDoc}
	 * @since 2019/04/21
	 */
	@Override
	public void memReadBytes(int __addr, byte[] __b, int __o, int __l)
		throws IndexOutOfBoundsException, NullPointerException
	{
		// Initialize?
		if (!this._didinit)
			try
			{
				this.__init();
			}
			catch (IOException e)
			{
				throw new RuntimeException(e);
			}
		
		// Forward
		this._memory.memReadBytes(__addr, __b, __o, __l);
	}
	
	/**
	 * {@inheritDoc}
	 * @since 2019/04/21
	 */
	@Override
	public int memReadInt(int __addr)
	{
		// Initialize?
		if (!this._didinit)
			try
			{
				this.__init();
			}
			catch (IOException e)
			{
				throw new RuntimeException(e);
			}
		
		// Forward
		return this._memory.memReadInt(__addr);
	}
	
	/**
	 * {@inheritDoc}
	 * @since 2019/04/21
	 */
	@Override
	public int memReadShort(int __addr)
	{
		// Initialize?
		if (!this._didinit)
			try
			{
				this.__init();
			}
			catch (IOException e)
			{
				throw new RuntimeException(e);
			}
		
		// Forward
		return this._memory.memReadShort(__addr);
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
		this._didinit = true;
		
		// Load the class library
		String libname = this.libname;
		VMClassLibrary clib = this.suites.loadLibrary(libname);
		
		// Debug
		todo.DEBUG.note("Initialize suite %s @%08d", libname, this.offset);
		
		// Minimize and format the JAR
		byte[] jf = JarMinimizer.minimize((libname.equals("supervisor") ||
			libname.equals("supervisor.jar")), clib);
		
		// {@squirreljme.error AE09 Suite chunk size limit was exceeded.
		// (The chunk size)}
		if (jf.length > SuitesMemory.SUITE_CHUNK_SIZE)
			throw new RuntimeException("AE09 " + jf.length);
		
		// Set memory using this byte array
		ReadableMemory rm;
		this._memory = (rm = new ByteArrayMemory(this.offset, jf));
		
		// Load the JAR header
		this._jarheader = MinimizedJarHeader.decode(
			new ReadableMemoryInputStream(rm, 0,
				MinimizedJarHeader.HEADER_SIZE_WITH_MAGIC));
	}
}

