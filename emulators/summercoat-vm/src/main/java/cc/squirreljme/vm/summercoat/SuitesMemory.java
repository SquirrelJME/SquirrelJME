// -*- Mode: Java; indent-tabs-mode: t; tab-width: 4 -*-
// ---------------------------------------------------------------------------
// Multi-Phasic Applications: SquirrelJME
//     Copyright (C) Stephanie Gawroriski <xer@multiphasicapps.net>
// ---------------------------------------------------------------------------
// SquirrelJME is under the GNU General Public License v3+, or later.
// See license.mkd for licensing and copyright information.
// ---------------------------------------------------------------------------

package cc.squirreljme.vm.summercoat;

import cc.squirreljme.emulator.vm.VMException;
import cc.squirreljme.emulator.vm.VMSuiteManager;
import dev.shadowtail.packfile.MinimizedPackHeader;
import java.io.ByteArrayOutputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.util.Arrays;
import java.util.LinkedHashMap;
import java.util.Map;

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
		25_165_824;
	
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
		int off = SuitesMemory.CONFIG_TABLE_SIZE;
		for (int i = 0; i < n; i++, off += SuitesMemory.SUITE_CHUNK_SIZE)
		{
			// Need the suite name for later lookup on init
			String libname = libnames[i];
			
			// Normalize and add JAR
			if (!libname.endsWith(".jar"))
				libname = libname + ".jar";
			
			// Map suite
			SuiteMemory sm;
			suitemem[i] = (sm = new SuiteMemory(off, __sm, libname));
			
			// Also use map for quick access
			suitemap.put(libname, sm);
		}
		
		// {@squirreljme.error AE0q Suite space has exceeded size limit of
		// 2GiB. (The current size; The amount of bytes over)}
		if (off < 0)
			throw new VMException("AE0q " + (((long)off) & 0xFFFFFFFFL) + " " +
				(off - 0x7FFFFFFF));
		
		// Store all the various suite memories
		this._suitemem = suitemem;
		this._suitemap = suitemap;
		
		// Store final memory parameters
		this.offset = __off;
		this.size = off;
	}
	
	/**
	 * Finds the specified library.
	 *
	 * @param __name The library to locate.
	 * @return The located library.
	 * @throws NullPointerException On null arguments.
	 * @since 2020/03/01
	 */
	public final SuiteMemory findLibrary(String __name)
		throws NullPointerException
	{
		if (__name == null)
			throw new NullPointerException("NARG");
		
		Map<String, SuiteMemory> suitemap = this._suitemap;
		
		// Direct name match?
		SuiteMemory rv = suitemap.get(__name);
		if (rv != null)
			return rv;
		
		// With JAR attached?
		rv = suitemap.get(__name + ".jar");
		if (rv != null)
			return rv;
		
		// Try one last time with JAR removed
		if (__name.endsWith(".jar"))
			return suitemap.get(__name.substring(
				0, __name.length() - ".jar".length()));
		
		// Not found, give up
		return null;
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
		if (__addr < SuitesMemory.CONFIG_TABLE_SIZE)
			return this._configtable.memReadByte(__addr);
		
		// Determine the suite index we are wanting to look in memory
		int si = (__addr - SuitesMemory.CONFIG_TABLE_SIZE) /
			SuitesMemory.SUITE_CHUNK_SIZE;
		
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
		SuiteMemory superv = this.findLibrary("cldc-compact");
		try
		{
			superv.__init();
		}
		
		// {@squirreljme.error AE0a Could not initialize the supervisor.}
		catch (IOException e)
		{
			throw new RuntimeException("AE0a", e);
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
			
			// Count and table of contents position
			dos.writeInt(numsuites);
			dos.writeInt(MinimizedPackHeader.HEADER_SIZE_WITH_MAGIC);
			
			// Boot properties
			dos.writeInt(Arrays.asList(suitemem).indexOf(superv));
			dos.writeInt(superv.offset);
			dos.writeInt(SuitesMemory.SUITE_CHUNK_SIZE);
			dos.writeInt(0);
			dos.writeInt(0);
			dos.writeInt(0);
			dos.writeInt(0);
			
			// Class and run-time constant pools
			dos.writeInt(0);
			dos.writeInt(0);
			dos.writeInt(0);
			dos.writeInt(0);
			
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
				dos.writeInt(SuitesMemory.SUITE_CHUNK_SIZE);
				
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
		
		// {@squirreljme.error AE0b Could not write the virtual packfile.}
		catch (IOException e)
		{
			throw new RuntimeException("AE0b", e);
		}
	}
}

