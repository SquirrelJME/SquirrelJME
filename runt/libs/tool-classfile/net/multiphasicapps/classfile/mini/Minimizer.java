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
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import net.multiphasicapps.classfile.ClassFile;
import net.multiphasicapps.classfile.Field;
import net.multiphasicapps.classfile.InstructionJumpTarget;
import net.multiphasicapps.classfile.InvalidClassFormatException;
import net.multiphasicapps.classfile.Method;
import net.multiphasicapps.classfile.MethodFlags;
import net.multiphasicapps.classfile.PrimitiveType;
import net.multiphasicapps.classfile.register.CompareType;
import net.multiphasicapps.classfile.register.DataType;
import net.multiphasicapps.classfile.register.RegisterCode;
import net.multiphasicapps.classfile.register.RegisterInstruction;
import net.multiphasicapps.classfile.register.RegisterList;
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
			
			// If this is an object increase the object count, this is needed
			// by the garbage collector to determine the addresses to scan
			if (pt == null)
				temp._objects++;
			
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
		
		// Always align the field area to 8 bytes so that way if there are
		// any long/double in the fields for an object following this, it will
		// properly be aligned
		for (__TempFields__ temp : rv)
			temp._bytes = (temp._bytes + 7) & ~7;
		
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
				try (ByteArrayOutputStream lnb = new ByteArrayOutputStream();
					DataOutputStream lbd = new DataOutputStream(lnb))
				{
					// Translate code
					transcode = this.__translateCode(rc);
					
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
	 * @return The resulting stream.
	 * @throws IOException On write errors.
	 * @throws NullPointerException On null arguments.
	 * @since 2019/03/23
	 */
	private final byte[] __translateCode(RegisterCode __rc)
		throws IOException, NullPointerException
	{
		if (__rc == null)
			throw new NullPointerException("NARG");
		
		// Where stuff gets written to
		ByteArrayOutputStream baos = new ByteArrayOutputStream(256);
		DataOutputStream dos = new DataOutputStream(baos);
		
		// Positions where all the instructions are in the byte array
		int cdn = __rc.length();
		int[] indexpos = new int[cdn];
		
		// Locations which have jump targets to be replaced
		Map<Integer, InstructionJumpTarget> jumpreps = new HashMap<>();
		
		// Operations will reference this constant pool
		MinimizedPoolBuilder pool = this.pool;
		
		// Go through each instruction
		for (int cdx = 0; cdx < cdn; cdx++)
		{
			// Get instruction here
			RegisterInstruction i = __rc.get(cdx);
			
			// Record that the instruction is at this position
			indexpos[cdx] = dos.size();
			
			// Write operation
			int op = i.operation();
			dos.write(op);
			
			// Most instructions use a pre-defined format, so doing it this way
			// simplifies handling various instructions as they will just use
			// a common layout.
			switch (i.encoding())
			{
				case RegisterOperationType.NOP:
				case RegisterOperationType.RETURN:
					break;
				
				case RegisterOperationType.X32_FIELD_LOAD:
				case RegisterOperationType.X64_FIELD_LOAD:
				case RegisterOperationType.OBJECT_FIELD_LOAD:
				case RegisterOperationType.ENCODING_U16:
					dos.writeShort(i.shortArgument(0));
					break;
					
				case RegisterOperationType.ENCODING_J16:
					jumpreps.put(dos.size(), i.<InstructionJumpTarget>
						argument(0, InstructionJumpTarget.class));
					dos.writeShort(0);
					break;
					
				case RegisterOperationType.ENCODING_U16_J16:
					dos.writeShort(i.shortArgument(0));
					
					jumpreps.put(dos.size(), i.<InstructionJumpTarget>
						argument(1, InstructionJumpTarget.class));
					dos.writeShort(0);
					break;
					
				case RegisterOperationType.ENCODING_POOL16_U16:
					dos.writeShort(pool.add(i.argument(0)));
					dos.writeShort(i.shortArgument(1));
					break;
					
				case RegisterOperationType.ENCODING_U16_U16:
				case RegisterOperationType.ENCODING_U16_U16_2:
					dos.writeShort(i.shortArgument(0));
					dos.writeShort(i.shortArgument(1));
					break;
					
				case RegisterOperationType.ENCODING_U16_U16_J16:
					dos.writeShort(i.shortArgument(0));
					dos.writeShort(i.shortArgument(1));
					
					jumpreps.put(dos.size(), i.<InstructionJumpTarget>
						argument(2, InstructionJumpTarget.class));
					dos.writeShort(0);
					break;
					
				case RegisterOperationType.ENCODING_POOL16_U16_U16:
					dos.writeShort(pool.add(i.argument(0)));
					dos.writeShort(i.shortArgument(1));
					dos.writeShort(i.shortArgument(2));
					break;
					
				case RegisterOperationType.ENCODING_U16_U16_U16:
				case RegisterOperationType.ENCODING_U16_U16_U16_2:
				case RegisterOperationType.ENCODING_U16_U16_U16_3:
					dos.writeShort(i.shortArgument(0));
					dos.writeShort(i.shortArgument(1));
					dos.writeShort(i.shortArgument(2));
					break;
					
				case RegisterOperationType.INVOKE_METHOD:
					{
						dos.writeShort(pool.add(i.argument(0)));
						
						// Write register list
						RegisterList rl = i.<RegisterList>argument(1,
							RegisterList.class);
						int rn = rl.size();
						dos.writeShort(rn);
						for (int r = 0; r < rn; r++)
							dos.writeShort(rl.get(r));
					}
					break;
					
				case RegisterOperationType.JUMP_IF_INSTANCE:
					dos.writeShort(pool.add(i.argument(0)));
					dos.writeShort(i.shortArgument(1));
					
					jumpreps.put(dos.size(), i.<InstructionJumpTarget>
						argument(2, InstructionJumpTarget.class));
					dos.writeShort(0);
					break;
					
				case RegisterOperationType.JUMP_IF_INSTANCE_GET_EXCEPTION:
					dos.writeShort(pool.add(i.argument(0)));
					dos.writeShort(i.shortArgument(1));
					
					jumpreps.put(dos.size(), i.<InstructionJumpTarget>
						argument(2, InstructionJumpTarget.class));
					dos.writeShort(0);
					
					dos.writeShort(i.shortArgument(3));
					break;
					
				case RegisterOperationType.LOOKUPSWITCH:
					throw new todo.TODO();
				
				case RegisterOperationType.X32_CONST:
					{
						// Integer
						Number v = i.<Number>argument(0, Number.class);
						if (v instanceof Integer)
							dos.writeInt((Integer)v);
						
						// Float
						else if (v instanceof Float)
							dos.writeInt(Float.floatToRawIntBits((Float)v));
						
						// Unknown
						else
							throw new todo.OOPS(v.toString());
					}
					break;
					
				case RegisterOperationType.X64_CONST:
					{
						// Long
						Number v = i.<Number>argument(0, Number.class);
						if (v instanceof Long)
							dos.writeLong((Long)v);
						
						// Double
						else if (v instanceof Double)
							dos.writeLong(
								Double.doubleToRawLongBits((Double)v));
						
						// Unknown
						else
							throw new todo.OOPS(v.toString());
					}
					break;
					
				case RegisterOperationType.TABLESWITCH:
					throw new todo.TODO();
				
				default:
					throw new todo.OOPS();
			}
		}
		
		// Generate array
		byte[] rv = baos.toByteArray();
		
		// Replace jumps
		for (Map.Entry<Integer, InstructionJumpTarget> e : jumpreps.entrySet())
		{
			int ai = e.getKey(),
				jt = e.getValue().target();
			
			// Remember that values are big endian
			rv[ai + 0] = (byte)(jt >>> 8);
			rv[ai + 1] = (byte)(jt);
		}
		
		// Debug
		todo.DEBUG.note("Code hexdump:");
		net.multiphasicapps.io.HexDumpOutputStream.dump(System.err, rv);
		
		// Return array
		return rv;
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

