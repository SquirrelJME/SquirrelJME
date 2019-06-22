// -*- Mode: Java; indent-tabs-mode: t; tab-width: 4 -*-
// ---------------------------------------------------------------------------
// Multi-Phasic Applications: SquirrelJME
//     Copyright (C) Stephanie Gawroriski <xer@multiphasicapps.net>
// ---------------------------------------------------------------------------
// SquirrelJME is under the GNU General Public License v3+, or later.
// See license.mkd for licensing and copyright information.
// ---------------------------------------------------------------------------

package cc.squirreljme.jvm;

/**
 * This represents a single boot library.
 *
 * @since 2019/06/14
 */
public final class BootLibrary
{
	/** Boot libraries which have been loaded. */
	public static volatile BootLibrary[] BOOT_LIBRARIES;
	
	/** The offset to the jar count. */
	public static final byte ROM_NUMJARS_OFFSET =
		4;
		
	/** The index of the JAR which should be the boot point. */
	public static final byte ROM_BOOTJARINDEX_OFFSET =
		8;
	
	/** The offset into the packfile where the boot entry is. */
	public static final byte ROM_BOOTJAROFFSET_OFFSET =
		12;
	
	/** The size of the boot jar. */
	public static final byte ROM_BOOTJARSIZE_OFFSET =
		16;
	
	/** Initial class path library indexes. */
	public static final byte ROM_BOOTICPOFFSET_OFFSET =
		20;
	
	/** Initial clsas path library index count. */
	public static final byte ROM_BOOTICPSIZE_OFFSET =
		24;
	
	/** Offset to the table of contents offset. */
	public static final byte ROM_TOCOFFSET_OFFSET =
		28;
	
	/** Table of contents size. */
	public static final byte TOC_ENTRY_SIZE =
		20;
	
	/** Table of contents name offset. */
	public static final byte TOC_NAME_OFFSET =
		0;
	
	/** Table of contents JAR data offset. */
	public static final byte TOC_JAR_OFFSET =
		4;
	
	/** Table of contents size of the JAR. */
	public static final byte TOC_JARLEN_OFFSET =
		8;
	
	/** Table of contents manifest offset. */
	public static final byte TOC_MANIFEST_OFFSET =
		12;
	
	/** Table of contents length of manifest. */
	public static final byte TOC_MANIFEST_LENGTH_OFFSET =
		16;
	
	/** The name of this library. */
	protected final String name;
	
	/** The absolute address of the JAR. */
	protected final int address;
	
	/** The length of the JAR. */
	protected final int length;
	
	/** Manifest address. */
	protected final int manifestaddress;
	
	/** Manifest length. */
	protected final int manifestlength;
	
	/**
	 * Initializes the boot library.
	 *
	 * @param __name The name of the library.
	 * @param __addr The JAR address.
	 * @param __len The JAR length.
	 * @param __maddr The manifest address.
	 * @param __mlen The manifest length.
	 * @throws NullPointerException On null arguments.
	 * @since 2019/06/19
	 */
	public BootLibrary(String __name, int __addr, int __len, int __maddr,
		int __mlen)
		throws NullPointerException
	{
		if (__name == null)
			throw new NullPointerException("NARG");
		
		this.name = __name;
		this.address = __addr;
		this.length = __len;
		this.manifestaddress = __maddr;
		this.manifestlength = __mlen;
	}
	
	/**
	 * Returns the boot libraries which make up the initial classpath.
	 *
	 * @param __rombase The ROM base.
	 * @param __confbase The configuration ROM base.
	 * @return The libraries to set for the initial classpath.
	 * @since 2019/06/20
	 */
	public static final BootLibrary[] initialClasspath(int __rombase,
		int __confbase)
	{
		// Load all libraries
		BootLibrary[] bootlibs = BootLibrary.bootLibraries(__rombase);
		if (bootlibs == null)
			Assembly.breakpoint();
		
		// The initial class path to use
		BootLibrary[] usecp;
		
		// Use the passed class-path if one was specified.
		int cp = Bootstrap.configSearch(__confbase, ConfigRomType.CLASS_PATH);
		if (cp != 0)
		{
			// Debug
			todo.DEBUG.note("Using user class path!");
			
			// Read string count
			int numcp = Assembly.memReadJavaShort(cp, 0) & 0xFFFF;
			cp += 2;
			
			// Build resulting array
			usecp = new BootLibrary[numcp];
			for (int i = 0; i < numcp; i++)
			{
				// Need to read the string length for skipping
				int strlen = Assembly.memReadJavaShort(cp, 0) & 0xFFFF;
				
				// Decode name of library
				String libname = JVMFunction.jvmLoadString(cp);
				
				// Find library for it
				for (int j = 0, jn = bootlibs.length; j < jn; j++)
				{
					BootLibrary bl = bootlibs[j];
					
					// Is this library?
					if (libname.equals(bl.name))
					{
						usecp[i] = bl;
						break;
					}
				}
				
				// Skip
				cp += strlen + 2;
			}
		}
		
		// Use class-path built into the ROM
		else
		{
			// Debug
			todo.DEBUG.note("Using firmware class path!");
			
			// Get offset to the table and its length
			int icpoff = __rombase + Assembly.memReadJavaInt(__rombase,
					ROM_BOOTICPOFFSET_OFFSET),
				icpsize = Assembly.memReadJavaInt(__rombase,
					ROM_BOOTICPSIZE_OFFSET);
			
			// Read all of them
			usecp = new BootLibrary[icpsize];
			for (int i = 0; i < icpsize; i++)
				usecp[i] = bootlibs[Assembly.memReadJavaInt(icpoff,
					i * 4)];
		}
		
		// Use them!
		return usecp;
	}
	
	/**
	 * Returns all of the libraries which are available to the bootstrap.
	 *
	 * @param __rombase The ROM base.
	 * @return The available bootstrap libraries.
	 * @since 2019/06/14
	 */
	public static final BootLibrary[] bootLibraries(int __rombase)
	{
		// Already exists?
		BootLibrary[] bootlibs = BOOT_LIBRARIES;
		if (bootlibs != null)
			return bootlibs;
		
		// Number of JARs in the ROM
		int numjars = Assembly.memReadJavaInt(__rombase, ROM_NUMJARS_OFFSET);
		
		// Offset to table of contents
		int tocoff = Assembly.memReadJavaInt(__rombase, ROM_TOCOFFSET_OFFSET);
		
		// Debug
		todo.DEBUG.note("Scanning %d libraries...", numjars);
		
		// Seeker for the table of contents
		int seeker = __rombase + tocoff;
		
		// Load all the JAR informations
		bootlibs = new BootLibrary[numjars];
		for (int i = 0; i < numjars; i++)
		{
			// Manifest address is optional
			int ma = Assembly.memReadJavaInt(seeker,
				TOC_MANIFEST_LENGTH_OFFSET);
			
			// Load library info
			BootLibrary bl = new BootLibrary(JVMFunction.jvmLoadString(
				__rombase + Assembly.memReadJavaInt(seeker, TOC_NAME_OFFSET)),
				__rombase + Assembly.memReadJavaInt(seeker, TOC_JAR_OFFSET),
				Assembly.memReadJavaInt(seeker, TOC_JARLEN_OFFSET),
				(ma == 0 ? 0 : __rombase + ma),
				Assembly.memReadJavaInt(seeker, TOC_MANIFEST_LENGTH_OFFSET));
			
			// Store it
			bootlibs[i] = bl;
			
			// Go to the next entry
			seeker += TOC_ENTRY_SIZE;
		}
		
		// Store for later usage
		BOOT_LIBRARIES = bootlibs;
		
		// Debug
		todo.DEBUG.note("Okay.");
		
		// Return the libraries
		return bootlibs;
	}
}

