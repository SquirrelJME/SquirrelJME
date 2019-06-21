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
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;
import net.multiphasicapps.classfile.ByteCode;
import net.multiphasicapps.classfile.ClassFile;
import net.multiphasicapps.classfile.InstructionMnemonics;
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
	/** The maximum column size. */
	public static final int COLUMN_SIZE =
		44;
	
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
		__ps.printf("****** %s ******", __m.nameAndType());
		__ps.println();
		
		// Line population count, to track how many times it changed to them
		Map<Integer, Integer> lnpopcount = new HashMap<>();
		
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
		
		// Fragment part builder
		StringBuilder sb = new StringBuilder();
		
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
				nijop = ni.intArgument(1);
				nijpc = ni.intArgument(2);
			}
			
			// Change of source line?
			if (nijln != lljln)
			{
				// Get population of this line
				Integer popcount = lnpopcount.get(nijln);
				if (popcount == null)
					popcount = Integer.valueOf(1);
				lnpopcount.put(nijln, popcount + 1);
				
				// Get the line, turn tabs to spaces to save room
				String ln = (nijln > 0 && nijln <= __srclines.size() ?
					__srclines.get(nijln - 1) : "<INVALID LINE?>").
					replace('\t', ' ');
				
				// Print the line text
				__ps.printf("    L%4d%3s: %s", nijln, (popcount > 1 ?
					String.format("+%-2d", popcount) : "   "), ln);
				__ps.println();
				
				// Set new last line
				lljln = nijln;
			}
			
			// Change of Java instruction? Get its string form
			String jis = "";
			boolean chjpc;
			if ((chjpc = (nijpc != lljpc)))
			{
				// Which string to put here?
				Object jstr = (bc.isValidAddress(nijpc) ?
					bc.getByAddress(nijpc) :
					InstructionMnemonics.toString(nijop));
				
				// Get form
				jis = String.format("J@%3d: %s", nijpc, jstr);
				
				// Set new last address
				lljpc = nijpc;
			}
			
			// Get native instruction form
			String nis = String.format("N@%3d: %s",
				nidx, ni);
			
			// Get string lengths
			int nisl = nis.length(),
				jisl = jis.length();
			
			// Determine row count
			int nirl = Math.max(1, (nisl + COLUMN_SIZE) / COLUMN_SIZE),
				jirl = Math.max(1, (jisl + COLUMN_SIZE) / COLUMN_SIZE),
				mxrl = Math.max(nirl, jirl);
			
			// Print all rows
			for (int i = 0; i < mxrl; i++)
			{
				// Print indent space
				__ps.print("    ");
				
				// New line or continuation?
				__ps.print((i == 0 ? '>' : ' '));
				
				// Determine fragment start and ends
				int nfs = Math.min(nisl, COLUMN_SIZE * i),
					nfe = Math.min(nisl, nfs + COLUMN_SIZE),
					jfs = Math.min(jisl, COLUMN_SIZE * i),
					jfe = Math.min(jisl, jfs + COLUMN_SIZE);
				
				// Print native fragment
				sb.setLength(0);
				sb.append(nis.substring(nfs, nfe));
				while (sb.length() < COLUMN_SIZE)
					sb.append(' ');
				__ps.print(sb);
				
				// Print left splitter
				__ps.print((i == 0 ? '<' : ' '));
				
				// Splitter if a start match
				__ps.print((i == 0 && chjpc ? '=' : '|'));
				
				// Print right splitter?
				__ps.print((chjpc ? '>' : ' '));
				
				// Print Java fragment
				sb.setLength(0);
				sb.append(jis.substring(jfs, jfe));
				while (sb.length() < COLUMN_SIZE)
					sb.append(' ');
				__ps.print(sb);
				
				// New line or continuation?
				__ps.print((chjpc ? '<' : ' '));
				
				// End line
				__ps.println();
			}
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
		SourceName projectname = new SourceName(
			(__args.length > 0 ? __args[0] : "cldc-compact"));
		
		// Get the source code for line lookup
		Source psrc = sm.get(projectname);
		
		// Get up to date binary
		bm.compile(bm.get(projectname));
		Binary pbin = bm.get(projectname);
		
		// Get the class we want to look at, make sure dots are slashes!
		String wantclass = (__args.length > 1 ? __args[1] :
			"java/lang/Object").replace('.', '/');
		
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
					System.err.println("Reading lines...");
					
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
					System.err.printf("Read %d lines!%n", lines.size());
				}
			}
			catch (NoSuchInputException e)
			{
				// Ignore
			}
		
		// Want a specific method by name?
		String onemethod = (__args.length > 2 ? __args[2] : "");
		if (onemethod.isEmpty())
			onemethod = null;
		
		// Scan through methods
		for (Method m : classfile.methods())
		{
			// Ignore abstracts/native
			if (m.flags().isAbstract() || m.flags().isNative())
				continue;
			
			// Wanted just one method and it did not match?
			if (onemethod != null && !m.name().toString().equals(onemethod))
				continue;
			
			// Dump it
			Main.dump(System.out, lines, m);
		}
	}
}

