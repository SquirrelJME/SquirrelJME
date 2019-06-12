// -*- Mode: Java; indent-tabs-mode: t; tab-width: 4 -*-
// ---------------------------------------------------------------------------
// Multi-Phasic Applications: SquirrelJME
//     Copyright (C) Stephanie Gawroriski <xer@multiphasicapps.net>
// ---------------------------------------------------------------------------
// SquirrelJME is under the GNU General Public License v3+, or later.
// See license.mkd for licensing and copyright information.
// ---------------------------------------------------------------------------

package dev.shadowtail.sxs;

import cc.squirreljme.builder.support.Binary;
import cc.squirreljme.builder.support.BinaryManager;
import cc.squirreljme.builder.support.ProjectManager;
import cc.squirreljme.builder.support.Source;
import cc.squirreljme.builder.support.SourceManager;
import cc.squirreljme.builder.support.SourceName;
import cc.squirreljme.builder.support.SourcePathSetType;
import cc.squirreljme.builder.support.TimeSpaceType;
import dev.shadowtail.classfile.nncc.NativeInstruction;
import dev.shadowtail.classfile.nncc.NativeInstructionType;
import dev.shadowtail.classfile.nncc.NativeCode;
import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.PrintStream;
import java.util.ArrayList;
import java.util.List;
import net.multiphasicapps.classfile.ByteCode;
import net.multiphasicapps.classfile.ClassFile;
import net.multiphasicapps.classfile.Method;
import net.multiphasicapps.javac.CompilerPathSet;
import net.multiphasicapps.javac.NoSuchInputException;

/**
 * Main entry point and dumber for the class code side by side
 *
 * @since 2019/06/12
 */
public class Main
{
	/**
	 * Dumps the given method.
	 *
	 * @param __ps The stream to write to.
	 * @param __srclines The source lines.
	 * @param __m The method to dump.
	 * @throws NullPointerException On null arguments.
	 * @since 2019/06/12
	 */
	public static final void dump(PrintStream __ps,
		List<String> __srclines, Method __m)
		throws NullPointerException
	{
		if (__ps == null || __srclines == null || __m == null)
			throw new NullPointerException("NARG");
		
		// Note what is being dumped
		__ps.printf("****** %s ******%n", __m.nameAndType());
		
		// Get byte code and native code
		ByteCode bc = __m.byteCode();
		NativeCode nc = __m.nativeCode();
		
		// Native debug information
		int nijln = -1,
			nijop = -1,
			nijpc = -1;
		
		// Last position information, to detect changes
		int lljln = nijln,
			lljop = nijop,
			lljpc = nijpc;
		
		// Major order is the native code
		for (int nidx = 0, numni = nc.length(); nidx < numni; nidx++)
		{
			// Get native instruction details
			NativeInstruction ni = nc.get(nidx);
			int niop = ni.operation();
			
			// Update location information?
			if (niop == NativeInstructionType.DEBUG_POINT)
			{
				nijln = ni.intArgument(0);
				nijop = (ni.intArgument(1) & 0xFF);
				nijpc = ni.intArgument(2);
			}
			
			// Change of source line?
			if (nijln != lljln)
			{
				// Get the line, turn tabs to spaces to save room
				String ln = (nijln > 0 && nijln <= __srclines.size() ?
					__srclines.get(nijln - 1) : "<INVALID LINE?>").
					replace('\t', ' ');
				
				// Print the line text
				__ps.printf("    L%03d: %s%n", nijln, ln);
				
				// Set new last line
				lljln = nijln;
			}
			
			//__ps.println(ni);
		}
		
		// Spacing
		__ps.println();
	}
	
	/**
	 * Main entry points.
	 *
	 * @param __args Arguments.
	 * @throws Throwable On any exception.
	 * @since 2019/06/12
	 */
	public static void main(String... __args)
		throws Throwable
	{
		// Must exist
		if (__args == null)
			__args = new String[0];
		
		// Load project manager
		ProjectManager pm = ProjectManager.fromArguments(__args);
		
		// Get source and binary manager
		SourceManager sm = pm.sourceManager(TimeSpaceType.BUILD);
		BinaryManager bm = pm.binaryManager(TimeSpaceType.BUILD);
		
		// Get project to look in
		SourceName projectname = new SourceName(__args[0]);
		
		// Get both the source and the binary for this project
		Source psrc = sm.get(projectname);
		Binary pbin = bm.get(projectname);
		
		// Get the class we want to look at, make sure dots are slashes!
		String wantclass = __args[1].replace('.', '/');
		
		// Load the class file itself
		ClassFile classfile;
		try (CompilerPathSet cps = pbin.pathSet())
		{
			// Open source for parsing
			try (InputStream in = cps.input(wantclass + ".class").open())
			{
				classfile = ClassFile.decode(in);
			}
		}
		
		// Source file line information
		List<String> lines = new ArrayList<>();
		
		// If a source file is set, read all of it!
		String sfn = classfile.sourceFile();
		if (sfn != null)
			try (CompilerPathSet cps = psrc.pathSet(SourcePathSetType.SOURCE))
			{
				// Read
				try (BufferedReader br =
					new BufferedReader(new InputStreamReader(
						cps.input(wantclass + ".java").open())))
				{
					// Debug
					todo.DEBUG.note("Reading lines...");
					
					// Note
					for (;;)
					{
						String ln = br.readLine();
						
						if (ln == null)
							break;
						
						// Add line
						lines.add(ln);
					}
					
					// Debug
					todo.DEBUG.note("Read %d lines!", lines.size());
				}
			}
			catch (NoSuchInputException e)
			{
				// Ignore
			}
		
		// Process each method
		for (Method m : classfile.methods())
		{
			// Ignore abstracts/native
			if (m.flags().isAbstract() || m.flags().isNative())
				continue;
			
			// Dump it
			Main.dump(System.out, lines, m);
		}
	}
}

