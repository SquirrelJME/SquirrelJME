// -*- Mode: Java; indent-tabs-mode: t; tab-width: 4 -*-
// ---------------------------------------------------------------------------
// Multi-Phasic Applications: SquirrelJME
//     Copyright (C) Stephanie Gawroriski <xer@multiphasicapps.net>
// ---------------------------------------------------------------------------
// SquirrelJME is under the GNU General Public License v3+, or later.
// See license.mkd for licensing and copyright information.
// ---------------------------------------------------------------------------

package dev.shadowtail.packfile;

import cc.squirreljme.vm.VMClassLibrary;
import dev.shadowtail.classfile.mini.DualPoolEncoder;
import dev.shadowtail.classfile.mini.DualPoolEncodeResult;
import dev.shadowtail.classfile.pool.DualClassRuntimePoolBuilder;
import dev.shadowtail.jarfile.JarMinimizer;
import dev.shadowtail.jarfile.MinimizedJarHeader;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.OutputStream;
import net.multiphasicapps.classfile.InvalidClassFormatException;
import net.multiphasicapps.io.TableSectionOutputStream;

/**
 * This class is used to pack multiple JAR files into a single packed ROM, so
 * that it is all contained within a single unit.
 *
 * @since 2019/05/29
 */
public class PackMinimizer
{
	/**
	 * Minimizes the class library.
	 *
	 * @param __boot The boot class used for the entry point.
	 * @param __initcp The initial classpath, if any.
	 * @param __mainbc Main boot class.
	 * @param __libs The libraries to minimize.
	 * @return The resulting minimized pack file.
	 * @throws IOException On read/write errors.
	 * @throws NullPointerException On null arguments.
	 * @since 2019/05/29
	 */
	public static final byte[] minimize(String __boot,
		String[] __initcp, String __mainbc, boolean __ismid,
		VMClassLibrary... __libs)
		throws IOException, NullPointerException
	{
		if (__libs == null)
			throw new NullPointerException("NARG");
		
		// Write into resulting array
		try (ByteArrayOutputStream baos = new ByteArrayOutputStream(1048576))
		{
			// Minimize
			PackMinimizer.minimize(baos, __boot, __initcp, __mainbc, __ismid,
				__libs);
			
			// Return result
			return baos.toByteArray();
		}
	}
	
	/**
	 * Minimizes the class library.
	 *
	 * @param __os The stream to write the minimized file to.
	 * @param __boot The boot class used for the entry point.
	 * @param __initcp Initial classpath.
	 * @param __mainbc Main boot class.
	 * @param __ismid Is this a MIDlet?
	 * @param __libs The libraries to minimize.
	 * @throws IOException On read/write errors.
	 * @throws NullPointerException On null arguments.
	 * @since 2019/05/29
	 */
	public static final void minimize(OutputStream __os, String __boot,
		String[] __initcp, String __mainbc, boolean __ismid,
		VMClassLibrary... __libs)
		throws IOException, NullPointerException
	{
		if (__os == null || __libs == null ||
			(__boot != null && (__initcp == null || __mainbc == null)))
			throw new NullPointerException("NARG");
		
		// Defensive copy and check for nulls
		__initcp = (__initcp == null ? new String[0] : __initcp.clone());
		for (int i = 0, n = __initcp.length; i < n; i++)
		{
			String vx = __initcp[i];
			
			if (vx == null)
				throw new NullPointerException("NARG");
			
			// Append JAR always
			if (!vx.endsWith(".jar"))
				__initcp[i] = vx + ".jar";
		}
		
		// Make sure it ends in JAR
		if (!__boot.endsWith(".jar"))
			__boot = __boot + ".jar";
		
		// Write ROM sections
		TableSectionOutputStream out = new TableSectionOutputStream();
		
		// Number of libraries to process
		int numlibs = __libs.length;
		
		// Initialize classpath indexes
		int numinitcp = __initcp.length;
		int[] cpdx = new int[numinitcp];
		
		// Header and table of contents sections
		TableSectionOutputStream.Section header = out.addSection(
			MinimizedPackHeader.HEADER_SIZE_WITH_MAGIC, 4);
		TableSectionOutputStream.Section toc = out.addSection(
			MinimizedPackHeader.TOC_ENTRY_SIZE * numlibs, 4);
		
		// Setup dual-pool where all combined values are stored as needed
		DualClassRuntimePoolBuilder dualpool =
			new DualClassRuntimePoolBuilder();
		
		// Section of the boot JAR
		int bootjarindex = -1;
		TableSectionOutputStream.Section bootjarsection = null;
		
		// Go through each library, minimize and write!
		for (int i = 0; i < numlibs; i++)
		{
			VMClassLibrary lib = __libs[i];
			String name = lib.name();
			
			// Normalize extension
			if (!name.endsWith(".jar"))
				name = name + ".jar";
			
			// Find library used in the initial classpath
			for (int j = 0; j < numinitcp; j++)
				if (name.equals(__initcp[j]))
				{
					cpdx[j] = i;
					break;
				}
			
			// Write name of JAR
			TableSectionOutputStream.Section jname = out.addSection(
				TableSectionOutputStream.VARIABLE_SIZE, 4);
			jname.writeUTF(name);
			
			// Output JAR data
			TableSectionOutputStream.Section jdata = out.addSection(
				TableSectionOutputStream.VARIABLE_SIZE, 4);
			
			// Is this a boot library?
			boolean isboot;
			if ((isboot = name.equals(__boot)))
			{
				bootjarindex = i;
				bootjarsection = jdata;
			}
			
			// Writing could fail however, so this makes it easier to find
			// the location of that failure
			MinimizedJarHeader mjh;
			try
			{
				// Used to get the header
				MinimizedJarHeader[] mjha = new MinimizedJarHeader[1];
				
				// The boot JAR is completely stand-alone, so do not use
				// a global JAR pool for it.
				JarMinimizer.minimize((isboot ? null : dualpool), isboot, lib,
					jdata, mjha);
				
				// Get the generated header
				mjh = mjha[0];
			}
			
			// {@squirreljme.error BI01 Could not minimize the JAR due to
			// an invalid class file. (The name)}
			catch (InvalidClassFormatException e)
			{
				throw new InvalidClassFormatException("BI01 " + name, e);
			}
			
			// Write TOC details
			toc.writeSectionAddressInt(jname);
			toc.writeSectionAddressInt(jdata);
			toc.writeSectionSizeInt(jdata);
			
			// Write manifest details if it is valid
			if (mjh.manifestlen > 0)
			{
				toc.writeSectionAddressInt(jdata, mjh.manifestoff);
				toc.writeInt(mjh.manifestlen);
			}
			else
			{
				toc.writeInt(0);
				toc.writeInt(0);
			}
		}
		
		// Write header details
		header.writeInt(MinimizedPackHeader.MAGIC_NUMBER);
		header.writeInt(numlibs);
		header.writeInt(MinimizedPackHeader.HEADER_SIZE_WITH_MAGIC);
		
		// Optional BootJAR information
		header.writeInt(bootjarindex);
		if (bootjarsection != null)
		{
			header.writeSectionAddressInt(bootjarsection);
			header.writeSectionSizeInt(bootjarsection);
		}
		else
		{
			header.writeInt(0);
			header.writeInt(0);
		}
		
		// Write initial classpath
		TableSectionOutputStream.Section icp = out.addSection(
			TableSectionOutputStream.VARIABLE_SIZE, 4);
		for (int i = 0; i < numinitcp; i++)
			icp.writeInt(cpdx[i]);
		
		// More boot information
		header.writeSectionAddressInt(icp);
		header.writeInt(numinitcp);
		
		// Main entry point name
		if (__mainbc == null)
		{
			header.writeInt(0);
			header.writeInt(0);
		}
		else
		{
			TableSectionOutputStream.Section mcl = out.addSection(
				TableSectionOutputStream.VARIABLE_SIZE, 4);
			
			// Main class
			mcl.writeUTF(__mainbc.replace('.', '/'));
			header.writeSectionAddressInt(mcl);
			
			// Is this a MIDlet?
			header.writeInt((__ismid ? 1 : -1));
		}
		
		// Encode the constant pools
		TableSectionOutputStream.Section lpd = out.addSection(
			TableSectionOutputStream.VARIABLE_SIZE, 4);
		DualPoolEncodeResult der = DualPoolEncoder.encode(dualpool, lpd);
		
		// Static pool
		header.writeSectionAddressInt(lpd, der.staticpooloff);
		header.writeInt(der.staticpoolsize);
		
		// Run-time pool
		header.writeSectionAddressInt(lpd, der.runtimepooloff);
		header.writeInt(der.runtimepoolsize);
		
		// Finish off
		out.writeTo(__os);
	}
}

