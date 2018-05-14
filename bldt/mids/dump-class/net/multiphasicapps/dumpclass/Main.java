// -*- Mode: Java; indent-tabs-mode: t; tab-width: 4 -*-
// ---------------------------------------------------------------------------
// Multi-Phasic Applications: SquirrelJME
//     Copyright (C) Stephanie Gawroriski <xer@multiphasicapps.net>
//     Copyright (C) Multi-Phasic Applications <multiphasicapps.net>
// ---------------------------------------------------------------------------
// SquirrelJME is under the GNU General Public License v3+, or later.
// See license.mkd for licensing and copyright information.
// ---------------------------------------------------------------------------

package net.multiphasicapps.dumpclass;

import java.io.IOException;
import java.io.InputStream;
import java.io.PrintStream;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.nio.file.StandardOpenOption;
import net.multiphasicapps.classfile.AnnotationElement;
import net.multiphasicapps.classfile.AnnotationValuePair;
import net.multiphasicapps.classfile.ClassFile;
import net.multiphasicapps.classfile.ClassFlag;
import net.multiphasicapps.classfile.ClassName;
import net.multiphasicapps.classfile.InvalidClassFormatException;
import net.multiphasicapps.io.IndentedOutputStream;
import net.multiphasicapps.zip.streamreader.ZipStreamEntry;
import net.multiphasicapps.zip.streamreader.ZipStreamReader;

/**
 * Main entry class for the dumper.
 *
 * @since 2018/05/14
 */
public class Main
{
	/**
	 * Dumps a single annotation.
	 *
	 * @param __i The controller for indenting.
	 * @param __out The output.
	 * @param __in The input.
	 * @throws NullPointerException On null arguments.
	 * @since 2018/05/14
	 */
	public static void dumpAnnotation(IndentedOutputStream __i,
		PrintStream __out, AnnotationElement __in)
		throws NullPointerException
	{
		if (__out == null || __in == null)
			throw new NullPointerException("NARG");
		
		__out.printf("Type: %s%n", __in.type());
		
		// Print the values of the annotations
		__i.increment();
		for (AnnotationValuePair v : __in.valuePairs())
			__out.println(v);
		__i.decrement();
	}
	
	/**
	 * Dumps the class information.
	 *
	 * @param __i The controller for indenting.
	 * @param __out The output.
	 * @param __in The input.
	 * @throws NullPointerException On null arguments.
	 * @since 2018/05/14
	 */
	public static void dumpClass(IndentedOutputStream __i, PrintStream __out,
		ClassFile __in)
		throws NullPointerException
	{
		if (__out == null || __in == null)
			throw new NullPointerException("NARG");
		
		// Base indentation level
		__i.setLevel(0);
		
		__out.printf("*** %s ***%n", __in.thisName());
		
		__out.printf("Type       : %s%n", __in.type());
		__out.printf("Extends    : %s%n", __in.superName());
		
		__out.println("Interfaces");
		__i.increment();
		for (ClassName n : __in.interfaceNames())
			__out.println(n);
		__i.decrement();
		
		// Print flags
		__out.println("Flags");
		__i.increment();
		for (ClassFlag f : __in.flags())
			__out.println(f);
		__i.decrement();
		
		// Print annotations
		__out.println("Annotations");
		__i.increment();
		for (AnnotationElement a : __in.annotatedElements())
			Main.dumpAnnotation(__i, __out, a);
		__i.decrement();
		
		if (true)
			throw new todo.TODO();
		
		// Flush
		__out.flush();
	}
	
	/**
	 * Main entry point.
	 *
	 * @param __args Program arguments.
	 * @since 2018/05/14
	 */
	public static void main(String... __args)
	{
		// Force to exist
		if (__args == null)
			__args = new String[0];
		
		// Indent to make the output nice
		IndentedOutputStream i = new IndentedOutputStream(System.out);
		PrintStream out = new PrintStream(i, true);
		
		// Dump all of them
		for (String a : __args)
		{
			if (a == null)
				continue;
			
			// Parse the class
			try (InputStream in = Files.newInputStream(Paths.get(a),
				StandardOpenOption.READ))
			{
				// Dump everything in the JAR/ZIP
				if (a.endsWith(".jar") || a.endsWith(".zip"))
					try (ZipStreamReader zip = new ZipStreamReader(in))
					{
						for (;;)
							try (ZipStreamEntry ent = zip.nextEntry())
							{
								if (ent == null)
									break;
								
								if (!ent.name().endsWith(".class"))
									continue;
								
								Main.dumpClass(i, out, ClassFile.decode(ent));
							}
							catch (IOException|InvalidClassFormatException e)
							{
								e.printStackTrace();
								continue;
							}
					}
				
				// Treat as class
				else
					Main.dumpClass(i, out, ClassFile.decode(in));
			}
			
			// Note it
			catch (IOException|InvalidClassFormatException e)
			{
				e.printStackTrace();
				continue;
			}
		}
	}
}

