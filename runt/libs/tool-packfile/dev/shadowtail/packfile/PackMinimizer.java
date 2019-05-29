// -*- Mode: Java; indent-tabs-mode: t; tab-width: 4 -*-
// ---------------------------------------------------------------------------
// Multi-Phasic Applications: SquirrelJME
//     Copyright (C) Stephanie Gawroriski <xer@multiphasicapps.net>
// ---------------------------------------------------------------------------
// SquirrelJME is under the GNU General Public License v3+, or later.
// See license.mkd for licensing and copyright information.
// ---------------------------------------------------------------------------

package dev.shadowtail.packfile;

import cc.squirreljme.jvm.Constants;
import cc.squirreljme.vm.VMClassLibrary;
import dev.shadowtail.jarfile.JarMinimizer;
import dev.shadowtail.jarfile.MinimizedJarHeader;
import java.io.ByteArrayOutputStream;
import java.io.DataOutputStream;
import java.io.InputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.util.ArrayDeque;
import java.util.ArrayList;
import java.util.Deque;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import net.multiphasicapps.classfile.InvalidClassFormatException;

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
	 * @param __libs The libraries to minimize.
	 * @return The resulting minimized pack file.
	 * @throws IOException On read/write errors.
	 * @throws NullPointerException On null arguments.
	 * @since 2019/05/29
	 */
	public static final byte[] minimize(String __boot,
		VMClassLibrary... __libs)
		throws IOException, NullPointerException
	{
		if (__libs == null)
			throw new NullPointerException("NARG");
		
		// Write into resulting array
		try (ByteArrayOutputStream baos = new ByteArrayOutputStream(1048576))
		{
			// Minimize
			PackMinimizer.minimize(baos, __boot, __libs);
			
			// Return result
			return baos.toByteArray();
		}
	}
	
	/**
	 * Minimizes the class library.
	 *
	 * @param __os The stream to write the minimized file to.
	 * @param __boot The boot class used for the entry point.
	 * @param __libs The libraries to minimize.
	 * @throws IOException On read/write errors.
	 * @throws NullPointerException On null arguments.
	 * @since 2019/05/29
	 */
	public static final void minimize(OutputStream __os, String __boot,
		VMClassLibrary... __libs)
		throws IOException, NullPointerException
	{
		if (__os == null || __libs == null)
			throw new NullPointerException("NARG");
		
		// Formatted data is used
		DataOutputStream dos = new DataOutputStream(__os);
		
		// Calculate relative offset to the JAR data
		int numlibs = __libs.length;
		int reloff = MinimizedPackHeader.HEADER_SIZE_WITH_MAGIC +
			(12 * numlibs);
		
		// Output table of contents
		ByteArrayOutputStream taos = new ByteArrayOutputStream(4096);
		DataOutputStream tdos = new DataOutputStream(taos);
		
		// Jar data
		ByteArrayOutputStream jaos = new ByteArrayOutputStream(1048576);
		DataOutputStream jdos = new DataOutputStream(jaos);
		
		// Boot positions
		int bji = 0,
			bjo = 0;
		
		// Go through each library, minimize and write!
		for (int i = 0; i < numlibs; i++)
		{
			VMClassLibrary lib = __libs[i];
			String name = lib.name();
			
			// Is this a boot library?
			boolean isboot;
			if ((isboot = name.equals(__boot)))
				bji = i;
			
			// Pad where the string would be
			while (((reloff + jdos.size()) & 1) != 0)
				jdos.write(0);
			
			// Write location of string and name of it
			tdos.writeInt(reloff + jdos.size());
			jdos.writeUTF(name);
			
			// Pad where the JAR would be
			while (((reloff + jdos.size()) & 7) != 0)
				jdos.write(0);
			
			// The boot JAR is located here
			if (isboot)
				bjo = reloff + jdos.size();
			
			// Write location of JAR and its minimized data
			int baseat;
			tdos.writeInt(reloff + (baseat = jdos.size()));
			
			// Writing could fail however, so this makes it easier to find
			// the location of that failure
			try
			{
				JarMinimizer.minimize(isboot, lib, jdos);
			}
			
			// {@squirreljme.error BI01 Could not minimize the JAR due to
			// an invalid class file. (The name)}
			catch (InvalidClassFormatException e)
			{
				throw new InvalidClassFormatException("BI01 " + name, e);
			}
			
			// Write size of the data
			tdos.writeInt(jdos.size() - baseat);
		}
		
		// Write pack header
		dos.writeInt(MinimizedPackHeader.MAGIC_NUMBER);
		dos.writeInt(numlibs);
		dos.writeInt(bji);
		dos.writeInt(bjo);
		dos.writeInt(MinimizedPackHeader.HEADER_SIZE_WITH_MAGIC);
		
		// Write TOC and JAR data
		taos.writeTo(dos);
		jaos.writeTo(dos);
	}
}

