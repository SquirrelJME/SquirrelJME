// -*- Mode: Java; indent-tabs-mode: t; tab-width: 4 -*-
// ---------------------------------------------------------------------------
// Multi-Phasic Applications: SquirrelJME
//     Copyright (C) Stephanie Gawroriski <xer@multiphasicapps.net>
// ---------------------------------------------------------------------------
// SquirrelJME is under the GNU General Public License v3+, or later.
// See license.mkd for licensing and copyright information.
// ---------------------------------------------------------------------------

package cc.squirreljme.vm.summercoat;

import cc.squirreljme.vm.VMClassLibrary;
import cc.squirreljme.vm.VMException;
import cc.squirreljme.vm.VMSuiteManager;
import dev.shadowtail.jarfile.MinimizedJarHeader;
import dev.shadowtail.packfile.MinimizedPackHeader;
import java.io.ByteArrayOutputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.util.Arrays;
import java.util.LinkedHashMap;
import java.util.Map;
import net.multiphasicapps.classfile.MethodFlags;

/**
 * This class contains the memory information for every single suite which
 * exists within the VM.
 *
 * @since 2019/04/21
 */
public final class SuitesMemory
	extends AbstractReadableMemory
	implements ReadableMemory
{
	/** Configuration and table space size. */
	public static final int CONFIG_TABLE_SIZE =
		1048576;
	
	/** The suite chunk size. */
	public static final int SUITE_CHUNK_SIZE =
		4194304;
	
	/** The suite manage to base from. */
	protected final VMSuiteManager suites;
	
	/** Offset. */
	protected final int offset;
	
	/** The size of this memory region. */
	protected final int size;
	
	/** The individual regions of suite memory. */
	private final SuiteMemory[] _suitemem;
	
	/** This is the mapping of suite names to memory. */
	private final Map<String, SuiteMemory> _suitemap;
	
	/** The suite configuration table (addresses of suites). */
	private volatile ReadableMemory _configtable;
	
	/** Was the config table initialized? */
	private volatile boolean _didconfiginit;
	
	/**
	 * Initializes the suites memory.
	 *
	 * @param __off The offset of suite memory.
	 * @param __sm The suite manager.
	 * @throws NullPointerException On null arguments.
	 * @since 2019/04/21
	 */
	public SuitesMemory(int __off, VMSuiteManager __sm)
		throws NullPointerException
	{
		if (__sm == null)
			throw new NullPointerException("NARG");
		
		// Set suites
		this.suites = __sm;
		
		// All the libraries which are available for usage
		String[] libnames = __sm.listLibraryNames();
		int n = libnames.length;
		
		// Setup suite memory area
		SuiteMemory[] suitemem = new SuiteMemory[n];
		Map<String, SuiteMemory> suitemap = new LinkedHashMap<>();
		
		// Setup memory regions for the various suites
		int off = CONFIG_TABLE_SIZE;
		for (int i = 0; i < n; i++, off += SUITE_CHUNK_SIZE)
		{
			// Need the suite name for later lookup on init
			String libname = libnames[i];
			
			// Map suite
			SuiteMemory sm;
			suitemem[i] = (sm = new SuiteMemory(off, __sm, libname));
			
			// Also use map for quick access
			suitemap.put(libname, sm);
		}
		
		// Store all the various suite memories
		this._suitemem = suitemem;
		this._suitemap = suitemap;
		
		// Store final memory parameters
		this.offset = __off;
		this.size = off;
	}
	
	/**
	 * {@inheritDoc}
	 * @since 2019/04/21
	 */
	@Override
	public int memReadByte(int __addr)
	{
		// Needs to be initialized?
		if (!this._didconfiginit)
			this.__init();
		
		// Reading from the config table?
		if (__addr < CONFIG_TABLE_SIZE)
			return this._configtable.memReadByte(__addr);
		
		// Determine the suite index we are wanting to look in memory
		int si = (__addr - CONFIG_TABLE_SIZE) / SUITE_CHUNK_SIZE;
		
		// Instead of failing, return some invalid values
		SuiteMemory[] suitemem = this._suitemem;
		if (si < 0 || si >= suitemem.length)
			return 0xFF;
		
		// Read from suite memory
		return suitemem[si].memReadByte(__addr - suitemem[si].offset);
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
		return this.size;
	}
	
	/**
	 * Initializes the configuration space.
	 *
	 * @since 2019/04/21
	 */
	final void __init()
	{
		// Do not initialize twice!
		if (this._didconfiginit)
			return;
		this._didconfiginit = true;
		
		// Initialize the bootstrap
		SuiteMemory superv = this._suitemap.get("supervisor.jar");
		try
		{
			superv.__init();
		}
		
		// {@squirreljme.error AE0t Could not initialize the supervisor.}
		catch (IOException e)
		{
			throw new RuntimeException("AE0t", e);
		}
		
		// Get suites and the number of them for processing
		SuiteMemory[] suitemem = this._suitemem;
		int numsuites = suitemem.length;
		
		// Build a virtualized pack header which works with SummerCoat and
		// matches the ROM format (just appears as a larger ROM)
		int packoffset = this.offset;
		try (ByteArrayOutputStream pbaos = new ByteArrayOutputStream(4096);
			DataOutputStream dos = new DataOutputStream(pbaos))
		{
			// Relative offset for names
			int reloff = MinimizedPackHeader.HEADER_SIZE_WITH_MAGIC +
				(MinimizedPackHeader.TOC_ENTRY_SIZE * numsuites);
			
			// Write pack header
			dos.writeInt(MinimizedPackHeader.MAGIC_NUMBER);
			dos.writeInt(numsuites);
			dos.writeInt(Arrays.asList(suitemem).indexOf(superv));
			dos.writeInt(superv.offset);
			dos.writeInt(SUITE_CHUNK_SIZE);
			dos.writeInt(MinimizedPackHeader.HEADER_SIZE_WITH_MAGIC);
			
			// Name table output
			ByteArrayOutputStream nbaos = new ByteArrayOutputStream(4096);
			DataOutputStream ndos = new DataOutputStream(nbaos);
			
			// Write TOC
			for (int i = 0; i < numsuites; i++)
			{
				SuiteMemory suite = suitemem[i];
				
				// Align name
				while (((reloff + ndos.size()) & 1) != 0)
					ndos.write(0);
				
				// Name position
				dos.writeInt(reloff + ndos.size());
				
				// Write name
				ndos.writeUTF(suite.libname);
				
				// Offset and size of the chunk
				dos.writeInt(suite.offset);
				dos.writeInt(SUITE_CHUNK_SIZE);
				
				// The manifest is not known, must be searched
				dos.writeInt(0);
				dos.writeInt(0);
			}
			
			// Write name table
			nbaos.writeTo(dos);
			
			// Store written configuration table
			ReadableMemory configtable = new ByteArrayMemory(packoffset,
				pbaos.toByteArray());
			this._configtable = configtable;
		}
		
		// {@squirreljme.error AE07 Could not write the virtual packfile.}
		catch (IOException e)
		{
			throw new RuntimeException("AE07", e);
		}
	}
}

