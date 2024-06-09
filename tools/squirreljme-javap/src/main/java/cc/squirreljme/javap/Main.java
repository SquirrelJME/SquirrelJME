// -*- Mode: Java; indent-tabs-mode: t; tab-width: 4 -*-
// ---------------------------------------------------------------------------
// Multi-Phasic Applications: SquirrelJME
//     Copyright (C) Stephanie Gawroriski <xer@multiphasicapps.net>
// ---------------------------------------------------------------------------
// SquirrelJME is under the Mozilla Public License Version 2.0.
// See license.mkd for licensing and copyright information.
// ---------------------------------------------------------------------------

package cc.squirreljme.javap;

import java.io.IOException;
import java.io.InputStream;
import java.io.PrintStream;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardOpenOption;
import java.util.ArrayList;
import java.util.List;
import net.multiphasicapps.classfile.ClassFile;
import net.multiphasicapps.classfile.ConstantValue;
import net.multiphasicapps.classfile.Contexual;
import net.multiphasicapps.classfile.InvalidClassFormatException;
import net.multiphasicapps.classfile.Pool;
import net.multiphasicapps.markdownwriter.MarkdownWriter;

/**
 * Main entry point.
 *
 * @since 2024/06/09
 */
public class Main
{
	/**
	 * Dumps the given class.
	 *
	 * @param __w Where to write to.
	 * @param __classy The class to dump.
	 * @throws IOException On read/write errors.
	 * @throws NullPointerException On null arguments.
	 * @since 2024/06/09
	 */
	public static void dumpClass(MarkdownWriter __w, ClassFile __classy)
		throws IOException, NullPointerException
	{
		if (__w == null || __classy == null)
			throw new NullPointerException("NARG");
		
		throw cc.squirreljme.runtime.cldc.debug.Debugging.todo();
	}
	
	/**
	 * Dumps the constant value.
	 *
	 * @param __w Where to write to.
	 * @param __value The constant value to dump.
	 * @throws IOException On read/write errors.
	 * @throws NullPointerException On null arguments.
	 * @since 2024/06/09
	 */
	private static void dumpConstantValue(MarkdownWriter __w,
		ConstantValue __value)
		throws IOException, NullPointerException
	{
		if (__w == null || __value == null)
			throw new NullPointerException("NARG");
		
		__w.printf("Constant value %s: %s",
			__value.type(), __value.boxedValue());
	}
	
	/**
	 * Dumps the contextual.
	 *
	 * @param __w Where to write to.
	 * @param __contexual The contextual to dump.
	 * @throws IOException On read/write errors.
	 * @throws NullPointerException On null arguments.
	 * @since 2024/06/09
	 */
	public static void dumpContextual(MarkdownWriter __w,
		Contexual __contexual)
		throws IOException, NullPointerException
	{
		if (__w == null || __contexual == null)
			throw new NullPointerException("NARG");
		
		if (__contexual instanceof ClassFile)
			Main.dumpClass(__w, (ClassFile)__contexual);
		else if (__contexual instanceof Pool)
			Main.dumpPool(__w, (Pool)__contexual);
		else if (__contexual instanceof ConstantValue)
			Main.dumpConstantValue(__w, (ConstantValue)__contexual);
		
		// Unknown
		else
			__w.printf("Unknown contextual: %s%n",
				__contexual);
	}
	
	/**
	 * Dumps the object.
	 *
	 * @param __w Where to write to.
	 * @param __object The object to dump.
	 * @throws IOException On read/write errors.
	 * @throws NullPointerException On null arguments.
	 * @since 2024/06/09
	 */
	public static void dumpObject(MarkdownWriter __w, Object __object)
		throws IOException, NullPointerException
	{
		if (__w == null)
			throw new NullPointerException("NARG");
		
		if (__object == null)
			__w.println("null");
		else if (__object instanceof Contexual)
			Main.dumpContextual(__w, (Contexual)__object);
		else
			__w.println(__object);
	}
	
	/**
	 * Dumps the pool.
	 *
	 * @param __w Where to write to.
	 * @param __pool The pool to dump.
	 * @throws IOException On read/write errors.
	 * @throws NullPointerException On null arguments.
	 * @since 2024/06/09
	 */
	public static void dumpPool(MarkdownWriter __w, Pool __pool)
		throws IOException, NullPointerException
	{
		if (__w == null || __pool == null)
			throw new NullPointerException("NARG");
		
		// Header
		__w.paragraph();
		__w.println("Constant pool");
		
		// Start list
		__w.listStart();
		
		// Print properties
		__w.printf("Size: %d",
			__pool.size());
		__w.listNext();
		
		// Each individual entry
		for (int i = 0, n = __pool.size(); i < n; i++)
		{
			// Dump it
			__w.printf("%d: ", i);
			Main.dumpObject(__w, __pool.get(Object.class, i));
			
			// Go to next item
			__w.listNext();
		}
		
		// End list
		__w.listEnd();
	}
	
	/**
	 * Main entry point. 
	 *
	 * @param __args Main arguments.
	 * @throws IOException On read errors.
	 * @since 2024/06/09
	 */
	public static void main(String... __args)
		throws IOException
	{
		// Failures
		List<Throwable> failures = new ArrayList<>();
		
		// Everything goes here
		PrintStream ps = System.out;
		MarkdownWriter w = new MarkdownWriter(ps);
		
		// Treat everything as an argument
		for (String arg : __args)
		{
			if (arg == null)
				throw new NullPointerException("NARG");
			
			// Read in class
			Path path = Paths.get(arg);
			try (InputStream in = Files.newInputStream(path,
				StandardOpenOption.READ))
			{
				// Attempt to decode the class
				ClassFile classy = ClassFile.decode(in);
				
				// Dump it
				Main.dumpClass(w, classy);
			}
			catch (Throwable t)
			{
				// Store in exception
				failures.add(new IOException("Failed to parse " + path, t));
				
				// Handle any contextuals, if applicable
				Main.__contextuals(w, t);
			}
		}
		
		// Were there failures?
		if (!failures.isEmpty())
		{
			IOException toss = new IOException("A class failed to parse.");
			
			for (Throwable t : failures)
				toss.addSuppressed(t);
			
			throw toss;
		}
	}
	
	/**
	 * Handles any contextuals.
	 *
	 * @param __w Where to write to.
	 * @param __t The thrown exception.
	 * @throws IOException On read/write errors.
	 * @since 2024/06/09
	 */
	private static void __contextuals(MarkdownWriter __w, Throwable __t)
		throws IOException
	{
		if (__t == null)
			return;
		
		// Handle main exception
		if (__t instanceof InvalidClassFormatException)
		{
			InvalidClassFormatException e = (InvalidClassFormatException)__t;
			for (Contexual contexual : e.context())
				Main.dumpContextual(__w, contexual);
		}
		
		// Recurse into causes
		Main.__contextuals(__w, __t.getCause());
		
		// Recurse into suppressed
		Throwable[] suppressed = __t.getSuppressed();
		if (suppressed != null)
			for (Throwable t : suppressed)
				Main.__contextuals(__w, t);
	}
}
