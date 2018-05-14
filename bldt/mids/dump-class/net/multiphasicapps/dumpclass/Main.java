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
import net.multiphasicapps.classfile.InvalidClassFormatException;
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
	 * @param __out The output.
	 * @param __in The input.
	 * @throws NullPointerException On null arguments.
	 * @since 2018/05/14
	 */
	public static void dumpAnnotation(PrintStream __out,
		AnnotationElement __in)
		throws NullPointerException
	{
		if (__out == null || __in == null)
			throw new NullPointerException("NARG");
		
		__out.printf("Type: %s%n", __in.type());
		
		for (AnnotationValuePair v : __in.valuePairs())
			__out.println(v);
	}
	
	/**
	 * Dumps the class information.
	 *
	 * @param __out The output.
	 * @param __in The input.
	 * @throws NullPointerException On null arguments.
	 * @since 2018/05/14
	 */
	public static void dumpClass(PrintStream __out, ClassFile __in)
		throws NullPointerException
	{
		if (__out == null || __in == null)
			throw new NullPointerException("NARG");
		
		__out.printf("*** %s ***%n", __in.thisName());
		
		__out.printf("Type       : %s%n", __in.type());
		__out.printf("Extends    : %s%n", __in.superName());
		__out.printf("Interfaces : %s%n", __in.interfaceNames());
		__out.printf("Flags      : %s%n", __in.flags());
		__out.printf("Annotations:%n");
		for (AnnotationElement a : __in.annotatedElements())
			__out.printf("    %s%n", a);
		
		throw new todo.TODO();
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
		
		// Dump all of them
		PrintStream out = System.out;
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
								
								Main.dumpClass(out, ClassFile.decode(ent));
							}
							catch (IOException|InvalidClassFormatException e)
							{
								e.printStackTrace();
								continue;
							}
					}
				
				// Treat as class
				else
					Main.dumpClass(out, ClassFile.decode(in));
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

