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

import java.io.InputStream;
import java.io.IOException;
import java.io.PrintStream;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.nio.file.StandardOpenOption;
import java.util.Map;
import net.multiphasicapps.classfile.Annotation;
import net.multiphasicapps.classfile.AnnotationTable;
import net.multiphasicapps.classfile.AnnotationValue;
import net.multiphasicapps.classfile.ClassFile;
import net.multiphasicapps.classfile.ClassFlag;
import net.multiphasicapps.classfile.ClassName;
import net.multiphasicapps.classfile.Field;
import net.multiphasicapps.classfile.FieldFlag;
import net.multiphasicapps.classfile.InnerClass;
import net.multiphasicapps.classfile.InnerClasses;
import net.multiphasicapps.classfile.InvalidClassFormatException;
import net.multiphasicapps.classfile.Method;
import net.multiphasicapps.classfile.MethodFlag;
import net.multiphasicapps.classfile.MethodName;
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
		PrintStream __out, Annotation __in)
		throws NullPointerException
	{
		if (__i == null || __out == null || __in == null)
			throw new NullPointerException("NARG");
		
		__out.printf("Type: %s%n", __in.type());
		
		// Print the values of the annotations
		__i.increment();
		for (Map.Entry<MethodName, AnnotationValue> v : __in.entrySet())
			__out.printf("%s=%s%n", v.getKey(), v.getValue());
		__i.decrement();
	}
	
	/**
	 * Dumps the annotation table.
	 *
	 * @param __i The controller for indenting.
	 * @param __out The output.
	 * @param __in The input.
	 * @throws NullPointerException On null arguments.
	 * @since 2018/05/15
	 */
	public static void dumpAnnotationTable(IndentedOutputStream __i,
		PrintStream __out, AnnotationTable __in)
		throws NullPointerException
	{
		if (__i == null || __out == null || __in == null)
			throw new NullPointerException("NARG");
		
		for (Annotation e : __in)
			Main.dumpAnnotation(__i, __out, e);
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
		if (__i == null || __out == null || __in == null)
			throw new NullPointerException("NARG");
		
		// Base indentation level
		__i.setLevel(0);
		
		__out.printf("*** Class %s ***%n", __in.thisName());
		
		__out.printf("Type:        %s%n", __in.type());
		__out.printf("Extends:     %s%n", __in.superName());
		
		__out.println("Interfaces:");
		__i.increment();
		for (ClassName n : __in.interfaceNames())
			__out.println(n);
		__i.decrement();
		
		// Print flags
		__out.println("Flags:");
		__i.increment();
		for (ClassFlag f : __in.flags())
			__out.println(f);
		__i.decrement();
		
		// Print annotations
		__out.println("Annotations:");
		__i.increment();
		Main.dumpAnnotationTable(__i, __out, __in.annotationTable());
		__i.decrement();
	
		// Inner classes
		__out.println("Inner Classes:");
		__i.increment();
		Main.dumpInnerClasses(__i, __out, __in.innerClasses());
		__i.decrement();
		
		// Print fields
		__out.println("Fields:");
		__i.increment();
		for (Field m : __in.fields())
			Main.dumpField(__i, __out, m);
		__i.decrement();
		
		// Print fields
		__out.println("Methods:");
		__i.increment();
		for (Method m : __in.methods())
			Main.dumpMethod(__i, __out, m);
		__i.decrement();
		
		// Flush
		__out.println();
		__out.flush();
	}
	
	/**
	 * Dumps field information.
	 *
	 * @param __i The controller for indenting.
	 * @param __out The output.
	 * @param __in The input.
	 * @throws NullPointerException On null arguments.
	 * @since 2018/05/14
	 */
	public static void dumpField(IndentedOutputStream __i, PrintStream __out,
		Field __in)
		throws NullPointerException
	{
		if (__i == null || __out == null || __in == null)
			throw new NullPointerException("NARG");
		
		__out.printf("--- Field %s ---%n", __in.nameAndType());
		
		__out.printf ("Value:       %s%n", __in.constantValue());
		
		// Print flags
		__out.println("Flags:");
		__i.increment();
		for (FieldFlag f : __in.flags())
			__out.println(f);
		__i.decrement();
		
		// Print annotations
		__out.println("Annotations:");
		__i.increment();
		Main.dumpAnnotationTable(__i, __out, __in.annotationTable());
		__i.decrement();
	}
	
	/**
	 * Dumps the inner classes.
	 *
	 * @param __i The controller for indenting.
	 * @param __out The output.
	 * @param __in The input.
	 * @throws NullPointerException On null arguments.
	 * @since 2018/06/16
	 */
	public static void dumpInnerClasses(IndentedOutputStream __i,
		PrintStream __out, InnerClasses __in)
		throws NullPointerException
	{
		if (__i == null || __out == null || __in == null)
			throw new NullPointerException("NARG");
		
		// Go through each one.
		for (InnerClass ic : __in)
		{
			__out.printf("Class %s%n", ic.name());
			
			__i.increment();
			
			__out.printf("Outer Class: %s%n", ic.outerClass());
			__out.printf("Simple Name: %s%n", ic.simpleName());
			__out.printf("Flags:       %s%n", ic.flags());
			
			__i.decrement();
		}
	}
	
	/**
	 * Dumps method information.
	 *
	 * @param __i The controller for indenting.
	 * @param __out The output.
	 * @param __in The input.
	 * @throws NullPointerException On null arguments.
	 * @since 2018/05/14
	 */
	public static void dumpMethod(IndentedOutputStream __i, PrintStream __out,
		Method __in)
		throws NullPointerException
	{
		if (__i == null || __out == null || __in == null)
			throw new NullPointerException("NARG");
		
		__out.printf("--- Method %s ---%n", __in.nameAndType());
		
		// Print flags
		__out.println("Flags:");
		__i.increment();
		for (MethodFlag f : __in.flags())
			__out.println(f);
		__i.decrement();
		
		// Print annotations
		__out.println("Annotations:");
		__i.increment();
		Main.dumpAnnotationTable(__i, __out, __in.annotationTable());
		__i.decrement();
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
								
								out.printf(">>> %s <<<%n", ent.name());
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
				{
					out.printf(">>> %s <<<%n", a);
					Main.dumpClass(i, out, ClassFile.decode(in));
				}
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

