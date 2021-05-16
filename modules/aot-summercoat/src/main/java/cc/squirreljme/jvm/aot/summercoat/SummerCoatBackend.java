// -*- Mode: Java; indent-tabs-mode: t; tab-width: 4 -*-
// ---------------------------------------------------------------------------
// Multi-Phasic Applications: SquirrelJME
//     Copyright (C) Stephanie Gawroriski <xer@multiphasicapps.net>
// ---------------------------------------------------------------------------
// SquirrelJME is under the GNU General Public License v3+, or later.
// See license.mkd for licensing and copyright information.
// ---------------------------------------------------------------------------

package cc.squirreljme.jvm.aot.summercoat;

import cc.squirreljme.jvm.aot.Backend;
import cc.squirreljme.jvm.aot.CompileSettings;
import cc.squirreljme.jvm.aot.LinkGlob;
import cc.squirreljme.jvm.aot.RomSettings;
import cc.squirreljme.jvm.summercoat.constants.JarProperty;
import cc.squirreljme.jvm.summercoat.ld.mem.ByteArrayMemory;
import cc.squirreljme.jvm.summercoat.ld.pack.HeaderStruct;
import cc.squirreljme.jvm.summercoat.ld.pack.JarRom;
import cc.squirreljme.runtime.cldc.io.HexDumpOutputStream;
import cc.squirreljme.vm.SummerCoatJarLibrary;
import cc.squirreljme.vm.VMClassLibrary;
import dev.shadowtail.classfile.mini.DualPoolEncoder;
import dev.shadowtail.classfile.mini.MinimizedClassFile;
import dev.shadowtail.classfile.mini.MinimizedClassHeader;
import dev.shadowtail.classfile.mini.MinimizedField;
import dev.shadowtail.classfile.pool.DualClassRuntimePool;
import dev.shadowtail.packfile.PackMinimizer;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.io.PrintStream;
import java.util.Arrays;

/**
 * This is the backend for SummerCoat.
 *
 * @since 2020/11/22
 */
public class SummerCoatBackend
	implements Backend
{
	/**
	 * {@inheritDoc}
	 * @since 2020/11/22
	 */
	@Override
	public void compileClass(CompileSettings __settings, LinkGlob __glob,
		String __name, InputStream __in, OutputStream __out)
		throws IOException, NullPointerException
	{
		if (__settings == null || __glob == null || __name == null ||
			__in == null || __out == null)
			throw new NullPointerException("NARG");
		
		// Copy from the source to the destination
		byte[] buf = new byte[4096];
		for (;;)
		{
			int rc = __in.read(buf);
			
			if (rc < 0)
				break;
			
			__out.write(buf, 0, rc);
		}
	}
	
	/**
	 * {@inheritDoc}
	 * @since 2021/05/16
	 */
	@Override
	public void dumpGlob(byte[] __inGlob, String __name, PrintStream __out)
		throws IOException, NullPointerException
	{
		// Load JAR
		JarRom jar = new JarRom(0, __name,
			new ByteArrayMemory(0, __inGlob, false));
		
		// Decode the dual pool
		HeaderStruct header = jar.header();
		DualClassRuntimePool dualPool = DualPoolEncoder.decode(__inGlob,
			header.getProperty(JarProperty.OFFSET_STATIC_POOL),
				header.getProperty(JarProperty.SIZE_STATIC_POOL),
			header.getProperty(JarProperty.OFFSET_RUNTIME_POOL),
				header.getProperty(JarProperty.SIZE_RUNTIME_POOL));
		
		// Dump each individual resource
		String[] entries = jar.entries();
		for (int i = 0, n = entries.length; i < n; i++)
			try (InputStream entryIn = jar.openResourceAsStream(i)) 
			{
				// Ignore directories!
				if (entries[i].endsWith("/"))
					continue;
				
				// Header
				__out.printf("****** %s ******%n", entries[i]);
				
				// If not a class file, dump raw byte data
				if (!entries[i].endsWith(".class"))
					HexDumpOutputStream.dump(__out, entryIn);
				
				// Otherwise decode as a class
				else
					this.__dumpClass(__out,
						MinimizedClassFile.decode(entryIn, dualPool));
				
				// Spacer
				__out.println();
			}
	}
	
	/**
	 * {@inheritDoc}
	 * @since 2020/11/22
	 */
	@Override
	public LinkGlob linkGlob(CompileSettings __settings, String __name,
		OutputStream __out)
		throws IOException, NullPointerException
	{
		if (__settings == null || __name == null || __out == null)
			throw new NullPointerException("NARG");
		
		return new SummerCoatLinkGlob(__settings, __name, __out);
	}
	
	/**
	 * {@inheritDoc}
	 * @since 2020/11/22
	 */
	@Override
	public String name()
	{
		return "summercoat";
	}
	
	/**
	 * {@inheritDoc}
	 * @since 2020/11/27
	 */
	@Override
	public void rom(RomSettings __settings, OutputStream __out,
		VMClassLibrary... __libs)
		throws IOException, NullPointerException
	{
		if (__settings == null || __out == null || __libs == null)
			throw new NullPointerException("NARG");
		
		// Try to find the boot library
		String bootLib = null;
		for (VMClassLibrary lib : __libs)
		{
			String name = lib.name();
			
			// Determine the base name
			int lastPath = Math.max(name.lastIndexOf('/'),
				name.lastIndexOf('\\'));
			name = (lastPath >= 0 ? name.substring(lastPath + 1) : name);
			
			// Clip off extension
			if (SummerCoatJarLibrary.isSqc(name) ||
				name.endsWith(".jar") || name.endsWith(".JAR"))
				name = name.substring(0, name.length() - 4);
			
			// Is this cldc-compact?
			if (name.equals("cldc-compact"))
				bootLib = lib.name();
		}
		
		// {@squirreljme.error AA01 Could not find the boot library.
		// (The available libraries)}
		if (bootLib == null)
			throw new IllegalArgumentException(
				"AA01 " + Arrays.asList(__libs));
		
		// Call the minimizer directly, just use a base library for the main
		// boot since it does not matter as the bootstrap should find the
		// launcher or the correct program to load rather than having it
		// baked into the ROM
		PackMinimizer.minimize(__out, bootLib, __libs);
	}
	
	/**
	 * Dumps the given class.
	 * 
	 * @param __out The output stream.
	 * @param __class The class to dump.
	 * @since 2021/05/16
	 */
	private void __dumpClass(PrintStream __out, MinimizedClassFile __class)
	{
		// Print some basic class details
		__out.printf("thisName      : %s%n", __class.thisName());
		__out.printf("superName     : %s%n", __class.superName());
		__out.printf("interfaceNames: %s%n", __class.interfaceNames());
		__out.printf("flags:          %s%n", __class.flags());
		__out.println();
		
		// Dump header
		this.__dumpHeader(__class.header, __out);
		
		// Dump fields
		for (MinimizedField f : __class.fields(true))
			this.__dumpField(f, __out);
		for (MinimizedField f : __class.fields(false))
			this.__dumpField(f, __out);
		
		// Dump methods
		/*for (MinimizedMethod m : __class.methods(true))
			this.__dumpMethod(m, __out);
		for (MinimizedMethod m : __class.methods(false))
			this.__dumpMethod(m, __out);*/
		__out.println();
		
		// End of class
		__out.println();
	}
	
	/**
	 * Dumps the given field.
	 * 
	 * @param __f The field to dump.
	 * @param __out The output.
	 * @since 2021/05/16
	 */
	private void __dumpField(MinimizedField __f, PrintStream __out)
	{
		__out.printf("Field %s:%n", __f.nameAndType());
		__out.printf("    flags : %s%n", __f.flags());
		__out.printf("    type  : %s%n", __f.datatype);
		__out.printf("    value : %s%n", __f.value);
		__out.printf("    size  : %s%n", __f.size);
		__out.printf("    offset: %s%n", __f.offset);
		__out.println();
	}
	
	/**
	 * Dumps the class file header.
	 * 
	 * @param __header The header.
	 * @param __out Where to dump to.
	 * @since 2021/05/16
	 */
	private void __dumpHeader(MinimizedClassHeader __header, PrintStream __out)
	{
		__out.println("Class Properties:");
		for (int i = 0, n = __header.numProperties(); i < n; i++)
			__out.printf("    %2d %-26s: 0x%08x / %d%n",
				i, __Utils__.classPropertyToString(i),
				__header.get(i), __header.get(i));
		
		// Spacer
		__out.println();
	}
}
