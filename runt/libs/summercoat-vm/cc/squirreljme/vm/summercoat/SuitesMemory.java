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
import java.io.ByteArrayOutputStream;
import java.io.DataOutputStream;
import java.io.IOException;
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
	
	/** The suite configuration table (addresses of suites). */
	protected final ReadableMemory configtable;
	
	/** The individual regions of suite memory. */
	private final SuiteMemory[] _suitemem;
	
	/** This is the mapping of suite names to memory. */
	private final Map<String, SuiteMemory> _suitemap;
	
	/** Was the config table initialized? */
	private volatile boolean _didconfiginit;
	
	/** Boot JAR header. */
	volatile MinimizedJarHeader _bootjarheader;
	
	/** The offset to the boot JAR. */
	volatile int _bootjaroff;
	
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
		
		// Output stream for table of contents
		ByteArrayOutputStream tbaos = new ByteArrayOutputStream(4096);
		DataOutputStream tdos = new DataOutputStream(tbaos);
		
		// Jar name fills
		ByteArrayOutputStream sbaos = new ByteArrayOutputStream(4096);
		DataOutputStream sdos = new DataOutputStream(sbaos);
		
		// Relative offset for the string table
		int reloff = (n * 8) + 8;
		
		// Setup suite memory area
		SuiteMemory[] suitemem = new SuiteMemory[n];
		Map<String, SuiteMemory> suitemap = new LinkedHashMap<>();
		
		// Write table of contents and memory map suites
		int off = CONFIG_TABLE_SIZE;
		try
		{
			// Setup memory regions for the various suites
			for (int i = 0; i < n; i++, off += SUITE_CHUNK_SIZE)
			{
				// Set
				String ln = libnames[i];
				SuiteMemory sm;
				suitemem[i] = (sm = new SuiteMemory(off, __sm, ln));
				
				// Also use map for quick access
				suitemap.put(ln, sm);
				
				// Write offset to suite ROM
				tdos.writeInt(off);
				
				// Write name of suite
				tdos.writeInt(reloff + sdos.size());
				sdos.writeUTF(ln);
				
				// Round table data
				while ((sdos.size() & 1) != 0)
					sdos.write(0);
				
				// Debug
				todo.DEBUG.note("MMap Suite %s -> %08x", ln, __off + off);
			}
			
			// End of table
			tdos.writeInt(-1);
			tdos.writeInt(-1);
			
			// Build configuration space
			sbaos.writeTo(tdos);
		}
		
		// {@squirreljme.error AE04 Could not build the table of contents.}
		catch (IOException e)
		{
			throw new RuntimeException("AE04", e);
		}
		
		// Store memory
		ReadableMemory configtable = new ByteArrayMemory(__off,
			tbaos.toByteArray());
		this.configtable = configtable;
		
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
			return this.configtable.memReadByte(__addr);
		
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
		SuiteMemory cldc = this._suitemap.get("supervisor.jar");
		try
		{
			cldc.__init();
		}
		
		// {@squirreljme.error AE0t Could not initialize the supervisor.}
		catch (IOException e)
		{
			throw new RuntimeException("AE0t", e);
		}
		
		// Get and initialize boot JAR header
		this._bootjarheader = cldc._jarheader;
		this._bootjaroff = cldc.offset;
	}
}

