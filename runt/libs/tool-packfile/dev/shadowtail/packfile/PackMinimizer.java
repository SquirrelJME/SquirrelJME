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
		
		throw new todo.TODO();
	}
}

