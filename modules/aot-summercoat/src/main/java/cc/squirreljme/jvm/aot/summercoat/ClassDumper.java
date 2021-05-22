// -*- Mode: Java; indent-tabs-mode: t; tab-width: 4 -*-
// ---------------------------------------------------------------------------
// Multi-Phasic Applications: SquirrelJME
//     Copyright (C) Stephanie Gawroriski <xer@multiphasicapps.net>
// ---------------------------------------------------------------------------
// SquirrelJME is under the GNU General Public License v3+, or later.
// See license.mkd for licensing and copyright information.
// ---------------------------------------------------------------------------

package cc.squirreljme.jvm.aot.summercoat;

import cc.squirreljme.jvm.summercoat.constants.JarProperty;
import cc.squirreljme.jvm.summercoat.ld.pack.HeaderStruct;
import cc.squirreljme.jvm.summercoat.ld.pack.JarRom;
import cc.squirreljme.runtime.cldc.io.HexDumpOutputStream;
import dev.shadowtail.classfile.mini.DualPoolEncoder;
import dev.shadowtail.classfile.mini.MinimizedClassFile;
import dev.shadowtail.classfile.mini.MinimizedClassHeader;
import dev.shadowtail.classfile.mini.MinimizedField;
import dev.shadowtail.classfile.mini.MinimizedMethod;
import dev.shadowtail.classfile.pool.DualClassRuntimePool;
import java.io.IOException;
import java.io.InputStream;
import java.io.PrintStream;
import net.multiphasicapps.io.Base64Alphabet;
import net.multiphasicapps.io.Base64Encoder;

/**
 * This is a dumper for SummerCoat's minimized class file format.
 *
 * @since 2021/05/21
 */
public final class ClassDumper
{
	/** The size of each line, used for base64 representation. */
	private static final int _LINE_SIZE =
		78;
	
	/** The size of tabs. */
	private static final int _TAB_SIZE =
		4;
	
	/** Minimum line size. */
	private static final int _MIN_LINE_SIZE =
		32;
	
	/** The JAR Rom. */
	protected final JarRom jarRom;
	
	/** The name of the JAR. */
	protected final String jarName;
	
	/** The stream to dump to. */
	protected final PrintStream out;
	
	/**
	 * Initializes the class dumper.
	 * 
	 * @param __jarRom The ROM to be dumped.
	 * @param __name The name of the JAR.
	 * @param __out Where to dump to.
	 * @since 2021/05/21
	 */
	public ClassDumper(JarRom __jarRom, String __name, PrintStream __out)
		throws NullPointerException
	{
		if (__jarRom == null)
			throw new NullPointerException("NARG");
		
		this.jarRom = __jarRom;
		this.jarName = __name;
		this.out = __out;
	}
	
	/**
	 * Dumps the given JAR.
	 * 
	 * @param __inGlob The raw ROM data.
	 * @throws IOException If the JAR could not be read.
	 * @since 2021/05/21
	 */
	public void dump(byte[] __inGlob)
		throws IOException
	{
		// Basic header
		this.__print(0, "# SquirrelJME Compiled JarROM Dump");
		this.__print(0, "dumpVersion", "%d", 1);
		
		// What is this JAR called?
		this.__print(0, "jarName", this.jarName);
		
		// The JAR to be dumped
		JarRom jar = this.jarRom;
		
		// Dump the JAR header
		HeaderStruct header = jar.header();
		this.__dumpJarHeader(header);
		
		// Decode the dual pool
		DualClassRuntimePool dualPool = DualPoolEncoder.decode(__inGlob,
			header.getProperty(JarProperty.OFFSET_STATIC_POOL),
				header.getProperty(JarProperty.SIZE_STATIC_POOL),
			header.getProperty(JarProperty.OFFSET_RUNTIME_POOL),
				header.getProperty(JarProperty.SIZE_RUNTIME_POOL));
		
		// Write out entries
		this.__print(0, "entries", "");
		
		// Dump each individual resource
		String[] entries = jar.entries();
		for (int i = 0, n = entries.length; i < n; i++)
			try (InputStream entryIn = jar.openResourceAsStream(i)) 
			{
				// Ignore directories!
				if (entries[i].endsWith("/"))
					continue;
				
				// Header key
				this.__print(1, "- " + entries[i], "");
				
				// If not a class file, dump raw byte data
				if (!entries[i].endsWith(".class"))
					this.__dumpResource(2, entryIn);
				
				// Otherwise decode as a class
				else
					this.__dumpClass(2,
						MinimizedClassFile.decode(entryIn, dualPool));
			}
	}
	
	/**
	 * Dumps the given resource.
	 * 
	 * @param __indent The indentation.
	 * @param __entryIn The entry data to write.
	 * @throws IOException On read errors.
	 * @since 2021/05/22
	 */
	private void __dumpResource(int __indent, InputStream __entryIn)
		throws IOException
	{
		this.__printBinary(__indent + 1, "data", __entryIn);
	}
	
	/**
	 * Dumps the given class.
	 * 
	 * @param __indent The indentation.
	 * @param __class The class to dump.
	 * @since 2021/05/16
	 */
	private void __dumpClass(int __indent, MinimizedClassFile __class)
	{
		PrintStream __out = this.out;
		
		// Print some basic class details
		__out.printf("thisName      : %s%n", __class.thisName());
		__out.printf("superName     : %s%n", __class.superName());
		__out.printf("interfaceNames: %s%n", __class.interfaceNames());
		__out.printf("flags:          %s%n", __class.flags());
		__out.println();
		
		// Dump header
		this.__dumpHeader(__class.header, __out);
		
		// Dump fields
		for (MinimizedField f : __class.fields(true))
			this.__dumpField(f, __out);
		for (MinimizedField f : __class.fields(false))
			this.__dumpField(f, __out);
		
		// Dump methods
		for (MinimizedMethod m : __class.methods(true))
			this.__dumpMethod(m, __out);
		for (MinimizedMethod m : __class.methods(false))
			this.__dumpMethod(m, __out);
		
		// End of class
		__out.println();
	}
	
	/**
	 * Dumps the given field.
	 * 
	 * @param __f The field to dump.
	 * @param __out The output.
	 * @since 2021/05/16
	 */
	private void __dumpField(MinimizedField __f, PrintStream __out)
	{
		__out.printf("Field %s:%n", __f.nameAndType());
		__out.printf("    flags : %s%n", __f.flags());
		__out.printf("    type  : %s%n", __f.datatype);
		__out.printf("    value : %s%n", __f.value);
		__out.printf("    size  : %s%n", __f.size);
		__out.printf("    offset: %s%n", __f.offset);
		__out.println();
	}
	
	/**
	 * Dumps the class file header.
	 * 
	 * @param __header The header.
	 * @param __out Where to dump to.
	 * @since 2021/05/16
	 */
	private void __dumpHeader(MinimizedClassHeader __header, PrintStream __out)
	{
		__out.println("Class Properties:");
		for (int i = 0, n = __header.numProperties(); i < n; i++)
			__out.printf("    %2d %-26s: 0x%08x / %d%n",
				i, __Utils__.classPropertyToString(i),
				__header.get(i), __header.get(i));
		
		// Spacer
		__out.println();
	}
	
	/**
	 * Dumps the JAR header.
	 * 
	 * @param __header The header to dump.
	 * @since 2021/05/16
	 */
	private void __dumpJarHeader(HeaderStruct __header)
	{
		this.__print(0, "jarProperties", "");
		for (int i = 0, n = __header.numProperties(); i < n; i++)
			this.__print(1, __Utils__.jarPropertyToString(i),
			"%08x", __header.getProperty(i));
	}
	
	/**
	 * Dumps the method code.
	 * 
	 * @param __m The method to dump.
	 * @param __out The output.
	 * @since 2021/05/16
	 */
	private void __dumpMethod(MinimizedMethod __m, PrintStream __out)
	{
		__out.printf("Method %s:%n", __m.nameAndType());
		__out.printf("    flags : %s%n", __m.flags());
		__out.printf("    index : %s%n", __m.index);
		
		// Is there code to be dumped?
		byte[] code = __m.code();
		if (code != null && code.length > 0)
		{
			__out.printf("    code  :%n");
			HexDumpOutputStream.dump(__out, __m.code());
		}
		
		__out.println();
	}
	
	/**
	 * Prints a line to the output.
	 * 
	 * @param __indent The indentation level.
	 * @param __key The key to print.
	 * @since 2021/05/22
	 */
	private void __print(int __indent, String __key)
	{
		this.__print(__indent, __key, null, (Object[])null);
	}
	
	/**
	 * Prints a line to the output.
	 * 
	 * @param __indent The indentation level.
	 * @param __key The key to print.
	 * @param __value The format string.
	 * @since 2021/05/21
	 */
	private void __print(int __indent, String __key, String __value)
	{
		this.__print(__indent, __key, __value, (Object[])null);
	}
	
	/**
	 * Prints a line to the output.
	 * 
	 * @param __indent The indentation level.
	 * @param __key The key to print.
	 * @param __value The format string.
	 * @param __args The arguments to the format.
	 * @since 2021/05/21
	 */
	private void __print(int __indent, String __key, String __value,
		Object... __args)
	{
		this.__printPrefix(__indent, __key, __value != null,
			__value != null && !__value.isEmpty());
		
		// Print value line
		PrintStream out = this.out;
		if (__value != null && !__value.isEmpty())
		{
			if (__args == null || __args.length == 0)
				out.print(__value);
			else
				out.printf(__value, __args);
		}
		
		// End the line
		out.println();
	}
	
	/**
	 * Prints binary data.
	 * 
	 * @param __indent The indentation.
	 * @param __key The key to print.
	 * @param __in The data to print.
	 * @throws IOException On read errors.
	 * @since 2021/05/22
	 */
	private void __printBinary(int __indent, String __key, InputStream __in)
		throws IOException
	{
		this.__printPrefix(__indent, __key,
			true, true);
		
		// Determine the line size to use, always keep a minimum!
		int lineSize = Math.max(ClassDumper._MIN_LINE_SIZE,
			(ClassDumper._LINE_SIZE - (__indent * ClassDumper._TAB_SIZE)) + 1);
			
		// Print encoded binary data
		PrintStream out = this.out;
		try (Base64Encoder in = new Base64Encoder(__in, Base64Alphabet.BASIC))
		{
			// Print binary coding prefix
			out.print("!binary |");
			out.println();
			
			// Write encoded characters
			int col = 0;
			for (;;)
			{
				// Which character is next?
				int c = in.read();
				if (c < 0)
					break;
				
				// Print indentation first?
				if ((col++) == 0)
					this.__printPrefix(__indent, " ",
						false, false);
				
				// Print this character
				out.print((char)c);
				
				// Go to next row?
				if (col >= lineSize)
				{
					out.println();
					col = 0;
				}
			}
		}
		
		// End the line
		out.println();
	}
	
	/**
	 * Prints the prefix.
	 * 
	 * @param __indent The indentation.
	 * @param __key The key to print.
	 * @param __hasValue Does this have a value?
	 * @param __valueNotEmpty Is this value not empty?
	 * @since 2021/05/22
	 */
	private void __printPrefix(int __indent, String __key, boolean __hasValue,
		boolean __valueNotEmpty)
	{
		// Print indentation
		PrintStream out = this.out;
		for (int i = 0; i < __indent; i++)
			out.print('\t');
		
		// Print the key?
		if (__key != null)
		{
			out.print(__key);
			
			// If there is also a value, there needs to be a divider
			if (__hasValue)
			{
				out.print(':');
				if (__valueNotEmpty)
					out.print(' ');
			}
		}
	}
}
