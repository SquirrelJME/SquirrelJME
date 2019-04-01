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
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import net.multiphasicapps.classfile.ByteCode;
import net.multiphasicapps.classfile.ClassFile;
import net.multiphasicapps.classfile.Instruction;
import net.multiphasicapps.classfile.InstructionMnemonics;
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
		
		// Load project manager
		ProjectManager pm = ProjectManager.fromArguments(__args);
		
		// For a progress indicator
		PrintStream ps = System.err;
		
		// Totals, which might be useful
		long totalclass = 0,
			totalmeths = 0,
			totalnamth = 0,
			totalinsts = 0;
		
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
							continue;
						
						// Print the class
						//ps.printf("Class %s%n", ent.name());
						
						// Decode class file
						ClassFile cf = ClassFile.decode(ent);
						totalclass++;
						
						// Go through methods and count instructions
						for (Method m : cf.methods())
						{
							// Print method
							//ps.printf("\tMethod %s%n", m.nameAndType());
							
							// Count
							totalmeths++;
							
							// Must have code
							ByteCode bc = m.byteCode();
							if (bc == null)
								continue;
							
							// Progress indicator
							ps.print('.');
							
							// Count methods with code
							totalnamth++;
							
							// Count it
							totalinsts += Main.countByteCode(bc, counts);
						}
					}
			}
		}
		
		// End progress indicator
		ps.println();
		
		// Build reverses
		List<Reverse> revs = new ArrayList<>(counts.size());
		for (Map.Entry<Integer, Counter> e : counts.entrySet())
			revs.add(new Reverse(e.getKey(), e.getValue().count));
		
		// Sort and reverse so higher values are first
		Collections.sort(revs, new CompareReverse());
		Collections.reverse(revs);
		
		// Switch to output
		ps = System.out;
		
		// Print total then every instruction
		ps.printf("Total Classes     : %d%n", totalclass);
		ps.printf("Total Methods     : %d%n", totalmeths);
		ps.printf("Total Methods+Code: %d%n", totalnamth);
		ps.printf("Total Instructions: %d%n", totalinsts);
		for (Reverse r : revs)
			ps.printf("%15s: %d%n",
				InstructionMnemonics.toString(r.op), r.count);
	}
	
	/**
	 * Comparator for reverse operations.
	 *
	 * @since 2019/04/01
	 */
	public static final class CompareReverse
		implements Comparator<Reverse>
	{
		/**
		 * {@inheritDoc}
		 * @since 2019/04/01
		 */
		@Override
		public final int compare(Reverse __a, Reverse __b)
		{
			// Compare counts first to group them together
			long lc = __b.count - __a.count;
			
			// Then just compare the opcode mnemonic so the output list
			// appears more stable
			return InstructionMnemonics.toString(__a.op).compareTo(
				InstructionMnemonics.toString(__b.op));
		}
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
		public long count;
		
		/**
		 * {@inheritDoc}
		 * @since 2019/03/31
		 */
		@Override
		public final String toString()
		{
			return Long.toString(count);
		}
	}
	
	/**
	 * Contains reverse instruction information.
	 *
	 * @since 2019/03/31
	 */
	public static final class Reverse
		implements Comparable<Reverse>
	{
		/** The operation. */
		protected final int op;
		
		/** The total count. */
		protected final long count;
		
		/**
		 * Initializes the reverse.
		 *
		 * @param __o The operation.
		 * @param __c The count.
		 * @since 2019/03/31
		 */
		public Reverse(int __o, long __c)
		{
			this.op = __o;
			this.count = __c;
		}
		
		/**
		 * {@inheritDoc}
		 * @since 2019/03/31
		 */
		@Override
		public final int compareTo(Reverse __b)
		{
			return (int)Math.min(Integer.MAX_VALUE,
				Math.max(Integer.MIN_VALUE, this.count - __b.count));
		}
	}
}

