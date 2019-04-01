// -*- Mode: Java; indent-tabs-mode: t; tab-width: 4 -*-
// ---------------------------------------------------------------------------
// Multi-Phasic Applications: SquirrelJME
//     Copyright (C) Stephanie Gawroriski <xer@multiphasicapps.net>
// ---------------------------------------------------------------------------
// SquirrelJME is under the GNU General Public License v3+, or later.
// See license.mkd for licensing and copyright information.
// ---------------------------------------------------------------------------

package dev.shadowtail.javapopcount;

import cc.squirreljme.builder.support.Binary;
import cc.squirreljme.builder.support.ProjectManager;
import cc.squirreljme.builder.support.TimeSpaceType;
import java.io.PrintStream;
import java.util.LinkedHashMap;
import java.util.Map;
import net.multiphasicapps.classfile.ByteCode;
import net.multiphasicapps.classfile.ClassFile;
import net.multiphasicapps.classfile.Instruction;
import net.multiphasicapps.classfile.Method;
import net.multiphasicapps.zip.streamreader.ZipStreamEntry;
import net.multiphasicapps.zip.streamreader.ZipStreamReader;

/**
 * Main entry point.
 *
 * @since 2019/03/31
 */
public class Main
{
	/**
	 * Counts all of the instructions in the byte code.
	 *
	 * @param __bc The byte code to go through.
	 * @param __c The output count.
	 * @return
	 * @since 2019/03/31
	 */
	public static int countByteCode(ByteCode __bc, Map<Integer, Counter> __c)
		throws NullPointerException
	{
		if (__bc == null || __c == null)
			throw new NullPointerException("NARG");
		
		// Total number of instructions
		int rv = 0;
		
		// Go through every instruction
		for (Instruction i : __bc)
		{
			int op = i.operation();
			
			// Get counter
			Counter c = __c.get(op);
			if (c == null)
				__c.put(op, (c = new Counter()));
			
			// Increase the counters
			c.count++;
			rv++;
		}
		
		// Return the total!
		return rv;
	}
	
	/**
	 * Runs the program.
	 *
	 * @param __args Program arguments.
	 * @throws Throwable On any throwable.
	 * @since 2019/03/31
	 */
	public static void main(String... __args)
		throws Throwable
	{
		// Instruction Counts
		Map<Integer, Counter> counts = new LinkedHashMap<>();
		long total = 0;
		
		// Load project manager
		ProjectManager pm = ProjectManager.fromArguments(__args);
		
		// For a progress indicator
		PrintStream ps = System.err;
		
		// Go through every binary that exists
		for (Binary b : pm.binaryManager(TimeSpaceType.BUILD))
		{
			// Compile the binary to make sure it will "run"
			Binary[] deps = pm.build(TimeSpaceType.BUILD, b.toString());
			
			// Go through the entire ZIP
			try (ZipStreamReader zsr = b.zipStream())
			{
				for (;;)
					try (ZipStreamEntry ent = zsr.nextEntry())
					{
						if (ent == null)
							break;
						
						// Only consider class files
						if (!ent.name().endsWith(".class"))
						{
							ps.print('.');
							continue;
						}
						
						// Decode class file
						ClassFile cf = ClassFile.decode(ent);
						
						// Show a hit
						ps.print('+');
						
						// Go through methods and count instructions
						for (Method m : cf.methods())
						{
							ByteCode bc = m.byteCode();
							if (bc == null)
							{
								ps.print('-');
								continue;
							}
							
							// Show method being processed
							ps.print('!');
							
							// Count it
							total += Main.countByteCode(bc, counts);
						}
					}
			}
		}
		
		// End indicator
		ps.println();
		
		// Show totals
		todo.DEBUG.note("Total %d", total);
		todo.DEBUG.note("Counts %s", counts);
		
		throw new todo.TODO();
	}
	
	/**
	 * It is faster to use a counter than it is to create integers for
	 * incrementing.
	 *
	 * @since 2019/03/31
	 */
	public static final class Counter
	{
		/** Count. */
		public int count;
		
		/**
		 * {@inheritDoc}
		 * @since 2019/03/31
		 */
		@Override
		public final String toString()
		{
			return Integer.toString(count);
		}
	}
}

