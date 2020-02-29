// -*- Mode: Java; indent-tabs-mode: t; tab-width: 4 -*-
// ---------------------------------------------------------------------------
// Multi-Phasic Applications: SquirrelJME
//     Copyright (C) Stephanie Gawroriski <xer@multiphasicapps.net>
// ---------------------------------------------------------------------------
// SquirrelJME is under the GNU General Public License v3+, or later.
// See license.mkd for licensing and copyright information.
// ---------------------------------------------------------------------------

package cc.squirreljme.jvm.lib;

import cc.squirreljme.jvm.Assembly;
import cc.squirreljme.jvm.ConfigReader;
import cc.squirreljme.jvm.ConfigRomType;
import cc.squirreljme.jvm.JVMFunction;

/**
 * This contains boot ROM information.
 *
 * @since 2019/06/23
 */
public final class BootRom
{
	/** Boot libraries which have been loaded. */
	public static volatile BootRomLibrary[] BOOT_LIBRARIES;
	
	/** The offset to the jar count. */
	public static final byte ROM_NUMJARS_OFFSET =
		4;
	
	/** Offset to the table of contents offset. */
	public static final byte ROM_TOCOFFSET_OFFSET =
		8;
		
	/** The index of the JAR which should be the boot point. */
	public static final byte ROM_BOOTJARINDEX_OFFSET =
		12;
	
	/** The offset into the packfile where the boot entry is. */
	public static final byte ROM_BOOTJAROFFSET_OFFSET =
		16;
	
	/** The size of the boot jar. */
	public static final byte ROM_BOOTJARSIZE_OFFSET =
		20;
	
	/** Initial class path library indexes. */
	public static final byte ROM_BOOTICPOFFSET_OFFSET =
		24;
	
	/** Initial class path library index count. */
	public static final byte ROM_BOOTICPSIZE_OFFSET =
		28;
	
	/** Initial main class. */
	public static final byte ROM_BOOTMAINCLASS_OFFSET =
		32;
	
	/** Is the boot class a MIDlet? */
	public static final byte ROM_BOOTMAINMIDLET_OFFSET =
		36;
	
	/** Static constant pool offset. */
	public static final byte ROM_STATICPOOLOFF_OFFSET =
		40;
	
	/** Static constant pool size. */
	public static final byte ROM_STATICPOOLSIZE_OFFSET =
		44;
	
	/** Runtime constant pool offset. */
	public static final byte ROM_RUNTIMEPOOLOFF_OFFSET =
		48;
	
	/** Runtime constant pool size. */
	public static final byte ROM_RUNTIMEPOOLSIZE_OFFSET =
		52;
	
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
	
	/**
	 * Returns the boot libraries which make up the initial classpath.
	 *
	 * @param __rombase The ROM base.
	 * @param __config The configuration system to use.
	 * @return The libraries to set for the initial classpath.
	 * @since 2019/06/20
	 */
	public static final ClassLibrary[] initialClasspath(int __rombase,
		ConfigReader __config)
	{
		// Load all libraries
		BootRomLibrary[] bootlibs = BootRom.bootLibraries(__rombase);
		if (bootlibs == null)
			Assembly.breakpoint();
		int numboot = bootlibs.length;
		
		// The initial class path to use
		ClassLibrary[] usecp;
		
		// Use the passed class-path if one was specified.
		String[] usercp = __config.loadStrings(ConfigRomType.CLASS_PATH);
		if (usercp != null)
		{
			// Debug
			todo.DEBUG.note("Using user class path!");
			
			// Scan for libraries
			int n = usercp.length;
			usecp = new ClassLibrary[n];
			for (int i = 0; i < n; i++)
			{
				String libname = usercp[i];
				
				// Find library
				for (int j = 0; j < numboot; j++)
				{
					BootRomLibrary bl = bootlibs[j];
					
					// Is this library?
					if (libname.equals(bl.name))
					{
						usecp[i] = bl;
						break;
					}
				}
			}
		}
		
		// Use class-path built into the ROM
		else
		{
			// Debug
			todo.DEBUG.note("Using firmware class path!");
			
			// Get offset to the table and its length
			int icpoff = __rombase + Assembly.memReadJavaInt(__rombase,
				BootRom.ROM_BOOTICPOFFSET_OFFSET),
				icpsize = Assembly.memReadJavaInt(__rombase,
					BootRom.ROM_BOOTICPSIZE_OFFSET);
			
			// Read all of them
			usecp = new ClassLibrary[icpsize];
			for (int i = 0; i < icpsize; i++)
				usecp[i] = bootlibs[Assembly.memReadJavaInt(icpoff,
					i * 4)];
		}
		
		// Use them!
		return usecp;
	}
	
	/**
	 * Returns if the initial class is a MIDlet.
	 *
	 * @param __rombase The base of the ROM.
	 * @param __config The configuration to use.
	 * @return Is this initial program a MIDlet?
	 * @since 2019/12/14
	 */
	public static final boolean initialIsMidlet(int __rombase,
		ConfigReader __config)
	{
		// Get from configuration first
		int rv = __config.loadInteger(ConfigRomType.IS_MIDLET);
		if (rv != 0)
			return (rv > 0);
		
		return Assembly.memReadJavaInt(
			__rombase, BootRom.ROM_BOOTMAINMIDLET_OFFSET) > 0;
	}
	
	/**
	 * Returns the initial main class.
	 *
	 * @param __rombase The base of the ROM.
	 * @param __config The configuration to use.
	 * @return The initial main class.
	 * @since 2019/06/23
	 */
	public static final String initialMain(int __rombase,
		ConfigReader __config)
	{
		// Use main user class
		String usermain = __config.loadString(ConfigRomType.MAIN_CLASS);
		if (usermain != null)
			return usermain;
		
		// Otherwise read it from the boot ROM
		return JVMFunction.jvmLoadString(__rombase +
			Assembly.memReadJavaInt(__rombase,
				BootRom.ROM_BOOTMAINCLASS_OFFSET));
	}
	
	/**
	 * Returns all of the libraries which are available to the bootstrap.
	 *
	 * @param __rombase The ROM base.
	 * @return The available bootstrap libraries.
	 * @since 2019/06/14
	 */
	public static final BootRomLibrary[] bootLibraries(int __rombase)
	{
		// Already exists?
		BootRomLibrary[] bootlibs = BootRom.BOOT_LIBRARIES;
		if (bootlibs != null)
			return bootlibs;
		
		// Number of JARs in the ROM
		int numjars = Assembly.memReadJavaInt(__rombase,
			BootRom.ROM_NUMJARS_OFFSET);
		
		// Offset to table of contents
		int tocoff = Assembly.memReadJavaInt(__rombase,
			BootRom.ROM_TOCOFFSET_OFFSET);
		
		// Debug
		todo.DEBUG.note("Scanning %d libraries...", numjars);
		
		// Seeker for the table of contents
		int seeker = __rombase + tocoff;
		
		// This is used to contain the constant pool data offset and size which
		// is then used when we initialize classes
		BootRomPoolInfo brpi = new BootRomPoolInfo(
			__rombase + Assembly.memReadJavaInt(__rombase,
				BootRom.ROM_STATICPOOLOFF_OFFSET),
			Assembly.memReadJavaInt(__rombase,
				BootRom.ROM_STATICPOOLSIZE_OFFSET),
			__rombase + Assembly.memReadJavaInt(__rombase,
				BootRom.ROM_RUNTIMEPOOLOFF_OFFSET),
			Assembly.memReadJavaInt(__rombase,
				BootRom.ROM_RUNTIMEPOOLSIZE_OFFSET));
		
		// Load all the JAR informations
		bootlibs = new BootRomLibrary[numjars];
		for (int i = 0; i < numjars; i++)
		{
			// Manifest address is optional
			int ma = Assembly.memReadJavaInt(seeker,
				BootRom.TOC_MANIFEST_LENGTH_OFFSET);
			
			// Load library info
			BootRomLibrary bl = new BootRomLibrary(JVMFunction.jvmLoadString(
				__rombase + Assembly.memReadJavaInt(seeker,
					BootRom.TOC_NAME_OFFSET)),
				__rombase + Assembly.memReadJavaInt(seeker,
					BootRom.TOC_JAR_OFFSET),
				Assembly.memReadJavaInt(seeker, BootRom.TOC_JARLEN_OFFSET),
				(ma == 0 ? 0 : __rombase + ma),
				Assembly.memReadJavaInt(seeker,
					BootRom.TOC_MANIFEST_LENGTH_OFFSET),
				brpi);
			
			// Store it
			bootlibs[i] = bl;
			
			// Go to the next entry
			seeker += BootRom.TOC_ENTRY_SIZE;
		}
		
		// Store for later usage
		BootRom.BOOT_LIBRARIES = bootlibs;
		
		// Debug
		todo.DEBUG.note("Okay.");
		
		// Return the libraries
		return bootlibs;
	}
}

