// -*- Mode: Java; indent-tabs-mode: t; tab-width: 4 -*-
// ---------------------------------------------------------------------------
// Multi-Phasic Applications: SquirrelJME
//     Copyright (C) Stephanie Gawroriski <xer@multiphasicapps.net>
//     Copyright (C) Multi-Phasic Applications <multiphasicapps.net>
// ---------------------------------------------------------------------------
// SquirrelJME is under the GNU General Public License v3+, or later.
// See license.mkd for licensing and copyright information.
// ---------------------------------------------------------------------------

package net.multiphasicapps.classfile.mini;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.DataOutputStream;
import java.io.InputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import net.multiphasicapps.classfile.ClassFile;
import net.multiphasicapps.classfile.Field;
import net.multiphasicapps.classfile.InvalidClassFormatException;
import net.multiphasicapps.classfile.Method;
import net.multiphasicapps.classfile.MethodFlags;
import net.multiphasicapps.classfile.PrimitiveType;
import net.multiphasicapps.classfile.register.RegisterCode;
import net.multiphasicapps.classfile.register.RegisterInstruction;
import net.multiphasicapps.classfile.register.RegisterOperationMnemonics;
import net.multiphasicapps.classfile.register.RegisterOperationType;

/**
 * This takes an input class file and translates it to the minimized format
 * which is easier to use.
 *
 * @since 2019/03/10
 */
public final class Minimizer
{
	/** The class file to minimize. */
	protected final ClassFile input;
	
	/** The constant pool builder to use. */
	protected final MinimizedPoolBuilder pool =
		new MinimizedPoolBuilder();
	
	/**
	 * Initializes the minimizer.
	 *
	 * @param __cf The class to minimize.
	 * @throws NullPointerException On null arguments.
	 * @since 2019/03/10
	 */
	private Minimizer(ClassFile __cf)
		throws NullPointerException
	{
		if (__cf == null)
			throw new NullPointerException("NARG");
		
		this.input = __cf;
	}
	
	/**
	 * Performs the minimization process.
	 *
	 * @param __dos The stream to write the result to.
	 * @throws IOException On write errors.
	 * @throws NullPointerException On null arguments.
	 * @since 2019/03/10
	 */
	private final void __run(DataOutputStream __dos)
		throws IOException, NullPointerException
	{
		if (__dos == null)
			throw new NullPointerException("NARG");
		
		// Write magic number to specify this format
		__dos.writeInt(MinimizedClassFile.MAGIC_NUMBER);
		
		// Process all fields
		__TempFields__[] fields = this.__doFields(); 
		
		// Process all methods
		__TempMethods__[] methods = this.__doMethods();
		
		throw new todo.TODO();
	}
	
	/**
	 * Process fields.
	 *
	 * @return The resulting fields, static and instance split into each.
	 * @since 2019/03/11
	 */
	private final __TempFields__[] __doFields()
	{
		// Static and instance fields are split because they are stored
		// in different places
		__TempFields__[] rv = new __TempFields__[]{
			new __TempFields__(), new __TempFields__()};
		
		// Perform some sorting to optimize slightly and make the layout a
		// bit friendlier
		List<Field> sorted = new ArrayList<>(this.input.fields());
		Collections.sort(sorted, new __MinimizerFieldSort__());
		
		// Process each field
		for (Field f : sorted)
		{
			// These are stored in their own rows
			__TempFields__ temp = rv[(f.flags().isStatic() ? 1 : 0)];
			
			// Determine the size of this entry (and its alignment)
			PrimitiveType pt = f.type().primitiveType();
			int fsz = (pt == null ? 4 : pt.bytes());
			
			// Determine the base position and check if any alignment is needed
			// assuming types of a given size are always aligned
			int basep = (temp._bytes + (fsz - 1)) & ~(fsz - 1);
			
			// Build field information
			MinimizedField q;
			temp._fields.add((q = new MinimizedField(
				f.flags().toJavaBits(),
				basep,
				fsz,
				f.name(),
				f.type(),
				f.constantValue())));
			
			// Debug
			todo.DEBUG.note("Add field %s", q);
			
			// Handle table sizes
			temp._bytes = basep + fsz;
			temp._count++;
		}
		
		// Return static and instance fields
		return rv;
	}
	
	/**
	 * Processes all methods within the class.
	 *
	 * @return The processed static and instance methods.
	 * @since 2019/03/13
	 */
	private final __TempMethods__[] __doMethods()
	{
		// Split static and instance methods to make them easier to locate
		// accordingly
		__TempMethods__[] rv = new __TempMethods__[]{
			new __TempMethods__(), new __TempMethods__()};
		
		// Process each method
		for (Method m : this.input.methods())
		{
			// These are stored in their own rows
			__TempMethods__ temp = rv[(m.flags().isStatic() ? 1 : 0)];
			
			// Need to translate and serialize the byte code into a register
			// form and remap any used references.
			MethodFlags mf = m.flags();
			byte[] transcode = null;
			byte[] lnt = null;
			if (!mf.isAbstract() && !mf.isNative())
			{
				// The minified classes use register code since it is easier
				// to handle by the VM
				RegisterCode rc = m.registerCode();
				
				// Encode to bytes
				try (ByteArrayOutputStream baos = new ByteArrayOutputStream();
					ByteArrayOutputStream lnb = new ByteArrayOutputStream();
					DataOutputStream dos = new DataOutputStream(baos);
					DataOutputStream lbd = new DataOutputStream(lnb))
				{
					// Translate code
					this.__translateCode(rc, dos);
					transcode = baos.toByteArray();
					
					// Translate lines
					this.__translateLines(rc.lines(), lbd);
					lnt = lnb.toByteArray();
				}
				catch (IOException e)
				{
					throw new RuntimeException(e);
				}
			}
			
			// Add method
			MinimizedMethod q;
			temp._methods.add((q = new MinimizedMethod(mf.toJavaBits(),
				temp._count, m.name(), m.type(), transcode, lnt)));
			
			// Debug
			todo.DEBUG.note("Add method %s", q);
			
			// Quick count for used methods
			temp._count++;
		}
		
		return rv;
	}
	
	/**
	 * Translates code.
	 *
	 * @param __rc The register code used.
	 * @param __dos The stream to write to.
	 * @throws IOException On write errors.
	 * @throws NullPointerException On null arguments.
	 * @since 2019/03/23
	 */
	private final void __translateCode(RegisterCode __rc,
		DataOutputStream __dos)
		throws IOException, NullPointerException
	{
		if (__rc == null || __dos == null)
			throw new NullPointerException("NARG");
		
		// Operations will reference this constant pool
		MinimizedPoolBuilder pool = this.pool;
		
		// Go through each instruction
		for (RegisterInstruction i : __rc)
		{
			// Write operation
			int op = i.operation();
			__dos.write(op);
			
			// All instructions use a pre-defined format, so doing it this way
			// simplifies handling various instructions as they will just use
			// a common layout.
			InstructionFormat ef = InstructionFormat.of(op);
			switch (ef)
			{
					// Plain format, just the opcode
				case PLAIN:
					break;
				
					// Constant pool reference
				case POOL16:
					__dos.writeShort(pool.add(i.argument(0)));
					break;
				
					// Type + 32-bit integer
				case TI32:
					{
						// Integer
						Number v = i.<Number>argument(0, Number.class);
						if (v instanceof Integer)
						{
							__dos.write('I');
							__dos.writeInt((Integer)v);
						}
						
						// Float
						else if (v instanceof Float)
						{
							__dos.write('F');
							__dos.writeInt(Float.floatToRawIntBits((Float)v));
						}
						
						// Unknown
						else
							throw new todo.OOPS(v.toString());
					}
					break;
					
					// Unsigned 16-bit integer
				case U16:
					__dos.writeShort(i.<Number>argument(0, Number.class).
						shortValue());
					break;
					
				default:
					throw new todo.OOPS(ef.toString());
			}
		}
	}
	
	/**
	 * Encodes the line number table for operations. This compacts the line
	 * number table to a more compact format.
	 *
	 * @param __ln The input table.
	 * @param __dos The output stream.
	 * @throws IOException On write errors.
	 * @throws NullPointerException On null arguments.
	 * @since 2019/03/24
	 */
	private final void __translateLines(short[] __ln, DataOutputStream __dos)
		throws IOException, NullPointerException
	{
		if (__ln == null || __dos == null)
			throw new NullPointerException("NARG");
		
		// Go through every entry, compacting it accordingly
		int lastline = -1,
			lastpc = 0;
		for (int i = 0, n = __ln.length; i < n; i++)
		{
			int nowline = __ln[i];
			
			// If there is a really long stretch of instructions which point
			// to the same exact line, force it to be reset so that way the
			// number table after this point is not complete garbage
			boolean force = ((i - lastpc) >= 255);
			
			// Line number has changed, need to encode the information
			if (force || nowline != lastline)
			{
				// Since multiple instruction addresses can share line info,
				// to reduce the size of the table just record an offset from
				// the last PC address
				// Just a single byte is used for the difference since in
				// general these ranges will be small
				int diff = i - lastpc;
				__dos.write(diff);
				
				// Write line position here, just as a 16-bit value without
				// any different encoding since values can jump all over the
				// place
				__dos.writeShort(nowline);
				
				// For next time
				lastline = nowline;
				lastpc = i;
			}
		}
	}
	
	/**
	 * Minimizes the given class and returns the minimized version of it.
	 *
	 * @param __cf The class to minimize.
	 * @return The resulting minimized class.
	 * @throws InvalidClassFormatException If the class is not formatted
	 * correctly.
	 * @throws NullPointerException On null arguments.
	 * @since 2019/03/10
	 */
	public static final MinimizedClassFile minimize(ClassFile __cf)
		throws InvalidClassFormatException, NullPointerException
	{
		if (__cf == null)
			throw new NullPointerException("NARG");
		
		// Minimization is straight to a byte format so just read that in
		// again
		try (ByteArrayOutputStream baos = new ByteArrayOutputStream(8192))
		{
			// Output minimized code to the byte array
			Minimizer.minimize(__cf, baos);
			
			// Just feed this back into the decoder for reading
			try (InputStream is = new ByteArrayInputStream(baos.toByteArray()))
			{
				return MinimizedClassFile.decode(is);
			}
		}
		
		// {@squirreljme.error JC2k Could not minimize class due to a read
		// or write error.}
		catch (IOException e)
		{
			throw new RuntimeException("JC2k", e);
		}
	}
	
	/**
	 * Minimizes the class file so that it is in a more compact format as
	 * needed.
	 *
	 * @param __cf The class file to minimize.
	 * @param __os The stream to write the minimized format to.
	 * @throws InvalidClassFormatException If the class format is not valid.
	 * @throws IOException On write errors.
	 * @throws NullPointerException On null arguments.
	 * @since 2019/03/10
	 */
	public static final void minimize(ClassFile __cf, OutputStream __os)
		throws InvalidClassFormatException, IOException, NullPointerException
	{
		if (__cf == null || __os == null)
			throw new NullPointerException("NARG");
		
		new Minimizer(__cf).__run(new DataOutputStream(__os));
	}
}

