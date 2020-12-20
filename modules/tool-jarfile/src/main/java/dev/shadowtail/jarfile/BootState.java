// -*- Mode: Java; indent-tabs-mode: t; tab-width: 4 -*-
// ---------------------------------------------------------------------------
// Multi-Phasic Applications: SquirrelJME
//     Copyright (C) Stephanie Gawroriski <xer@multiphasicapps.net>
// ---------------------------------------------------------------------------
// SquirrelJME is under the GNU General Public License v3+, or later.
// See license.mkd for licensing and copyright information.
// ---------------------------------------------------------------------------

package dev.shadowtail.jarfile;

import cc.squirreljme.runtime.cldc.debug.Debugging;
import dev.shadowtail.classfile.mini.MinimizedClassFile;
import dev.shadowtail.classfile.pool.BasicPool;
import dev.shadowtail.classfile.pool.BasicPoolEntry;
import dev.shadowtail.classfile.pool.DualClassRuntimePool;
import java.io.IOException;
import java.io.InputStream;
import java.util.HashMap;
import java.util.Map;
import net.multiphasicapps.classfile.ClassName;
import net.multiphasicapps.io.ChunkSection;

/**
 * The boot state of the system.
 *
 * @since 2020/12/13
 */
public final class BootState
{
	/** The class data used. */
	private final Map<ClassName, ChunkSection> _rawChunks =
		new HashMap<>();
	
	/** Classes which have been read. */
	private final Map<ClassName, MinimizedClassFile> _readClasses =
		new HashMap<>();
	
	/** The state of all classes. */
	private final Map<ClassName, ClassState> _classStates =
		new HashMap<>();
	
	/** Memory handles for the boot state, to be written accordingly. */
	private final MemHandles _memHandles =
		new MemHandles();
		
	/** The name of the boot class. */
	private ClassName _bootClass;
	
	/** The pool references to use for booting. */
	private DualClassRuntimePool _pool;
	
	/**
	 * Adds the specified class to be loaded and handled later.
	 * 
	 * @param __class The class name of this resource.
	 * @param __in The chunk to read from.
	 * @param __isBootClass Is this the boot class?
	 * @throws IOException On read errors.
	 * @throws NullPointerException On null arguments.
	 * @since 2020/12/13
	 */
	public void addClass(ClassName __class, ChunkSection __in,
		boolean __isBootClass)
		throws IOException, NullPointerException
	{
		if (__class == null || __in == null)
			throw new NullPointerException("NARG");
		
		// Store the raw chunk reference
		this._rawChunks.put(__class, __in);
		
		// Is this the entry class?
		if (__isBootClass)
			this._bootClass = __class;
	}
	
	/**
	 * Performs the boot process for the system.
	 * 
	 * @param __pool The pool to use for loading.
	 * @param __outData The output data for the bootstrap.
	 * @param __startPoolHandleId The target handle ID for the pool.
	 * @throws IOException On read errors.
	 * @throws NullPointerException On null arguments.
	 * @since 2020/12/16
	 */
	public void boot(DualClassRuntimePool __pool, ChunkSection __outData,
		int[] __startPoolHandleId)
		throws IOException, NullPointerException
	{
		if (__pool == null || __outData == null ||
			__startPoolHandleId == null)
			throw new NullPointerException("NARG");
		
		// Set the boot pool because we need everything that is inside
		this._pool = __pool;
		
		// Recursively load the boot class and any dependent class
		ClassState boot = this.__loadClass(this._bootClass);
		
		// Determine the starting memory handle ID
		__startPoolHandleId[0] = boot.poolMemHandle.id;
		
		// Write the memory handles into boot memory
		MemHandles memHandles = this._memHandles;
		throw Debugging.todo();
	}
	
	/**
	 * Reads a minimized class file for the given class.
	 * 
	 * @param __class The class to boot.
	 * @return The read class data.
	 * @throws IOException On read errors.
	 * @throws NullPointerException On null arguments.
	 * @since 2020/12/16
	 */
	private MinimizedClassFile readClass(ClassName __class)
		throws IOException, NullPointerException
	{
		if (__class == null)
			throw new NullPointerException("NARG");
		
		// Did we already read this class?
		Map<ClassName, MinimizedClassFile> readClasses = this._readClasses;
		MinimizedClassFile rv = readClasses.get(__class);
		if (rv != null)
			return rv;
		
		// Load in class data
		try (InputStream in = this._rawChunks.get(__class).currentStream())
		{
			rv = MinimizedClassFile.decode(in, this._pool);
			
			// Debug
			Debugging.debugNote("Read %s...", rv.thisName());
		}
		
		// Cache it and use it
		readClasses.put(__class, rv);
		return rv;
	}
	
	/**
	 * Loads the specified class.
	 * 
	 * @param __cl The class to load.
	 * @return The class state for the class.
	 * @throws IOException On read errors.
	 * @throws NullPointerException On null arguments.
	 * @since 2020/12/16
	 */
	final ClassState __loadClass(ClassName __cl)
		throws IOException, NullPointerException
	{
		if (__cl == null)
			throw new NullPointerException("NARG");
		
		// Has this class already been loaded?
		Map<ClassName, ClassState> classStates = this._classStates;
		ClassState rv = classStates.get(__cl);
		if (rv != null)
			return rv;
		
		// Debug
		Debugging.debugNote("Loading %s...", __cl);
		
		// Read the class data as fast as possible and store into the map so
		// we can recursive and recycle classes.
		MinimizedClassFile classFile = this.readClass(this._bootClass);
		rv = new ClassState(classFile.thisName(), classFile);
		classStates.put(__cl, rv);
		
		// Everything is based on the run-time pool, so we need to load
		// everything inside
		BasicPool rtPool = classFile.pool.runtimePool();
		int rtPoolSz = rtPool.size();
		
		// Allocate storage for the constant pool
		MemHandles memHandles = this._memHandles;
		PoolHandle pool = memHandles.allocPool(rtPoolSz);
		rv.poolMemHandle = pool;
		
		// Set all of the entries within the pool
		for (int i = 0; i < rtPoolSz; i++)
			pool.set(i, this.loadPool(rtPool.byIndex(i)));
		
		throw Debugging.todo();
		//Map<ClassName, __ClassState__> _classStates
	}
	
	/**
	 * Loads the specified pool entry into memory and returns the handle.
	 * 
	 * @param __entry The entry to load.
	 * @return The memory handle for the entry data.
	 * @throws NullPointerException On null arguments.
	 * @since 2020/12/29
	 */
	private MemHandle loadPool(BasicPoolEntry __entry)
		throws NullPointerException
	{
		if (__entry == null)
			throw new NullPointerException("NARG");
		
		throw Debugging.todo();
	}
}
