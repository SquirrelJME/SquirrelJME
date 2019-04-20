// -*- Mode: Java; indent-tabs-mode: t; tab-width: 4 -*-
// ---------------------------------------------------------------------------
// Multi-Phasic Applications: SquirrelJME
//     Copyright (C) Stephanie Gawroriski <xer@multiphasicapps.net>
//     Copyright (C) Multi-Phasic Applications <multiphasicapps.net>
// ---------------------------------------------------------------------------
// SquirrelJME is under the GNU General Public License v3+, or later.
// See license.mkd for licensing and copyright information.
// ---------------------------------------------------------------------------

package dev.shadowtail.classfile.mini;

import dev.shadowtail.classfile.nncc.ArgumentFormat;
import dev.shadowtail.classfile.nncc.NativeCode;
import dev.shadowtail.classfile.nncc.NativeInstruction;
import dev.shadowtail.classfile.nncc.NativeInstructionType;
import dev.shadowtail.classfile.nncc.RegisterList;
import dev.shadowtail.classfile.xlate.CompareType;
import dev.shadowtail.classfile.xlate.DataType;
import dev.shadowtail.classfile.xlate.MathType;
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
import net.multiphasicapps.classfile.ClassName;
import net.multiphasicapps.classfile.ConstantValue;
import net.multiphasicapps.classfile.Field;
import net.multiphasicapps.classfile.FieldDescriptor;
import net.multiphasicapps.classfile.FieldFlags;
import net.multiphasicapps.classfile.FieldName;
import net.multiphasicapps.classfile.InstructionJumpTarget;
import net.multiphasicapps.classfile.InvalidClassFormatException;
import net.multiphasicapps.classfile.Method;
import net.multiphasicapps.classfile.MethodDescriptor;
import net.multiphasicapps.classfile.MethodFlags;
import net.multiphasicapps.classfile.MethodName;
import net.multiphasicapps.classfile.PrimitiveType;

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
		__dos.writeInt(MinimizedClassHeader.MAGIC_NUMBER);
		
		// Unused, may be used later when needed
		__dos.writeShort(0);
		__dos.writeShort(0);
		
		// Process all fields
		__TempFields__[] fields = this.__doFields(); 
		
		// Process all methods
		__TempMethods__[] methods = this.__doMethods();
		
		// The pool
		MinimizedPoolBuilder pool = this.pool;
		
		// This may be null for Object
		ClassName supernamecn = input.superName();
		
		// Class names
		int thisname = pool.add(input.thisName()),
			supername = (supernamecn == null ? 0 : pool.add(supernamecn)),
			inames = pool.add(input.interfaceNames());
		
		// The source file name
		String sfn = input.sourceFile();
		int snfid = (sfn == null ? 0 : Minimizer.__checkUShort(pool.add(sfn)));
		
		// Store the pool size
		__dos.writeShort(Minimizer.__checkUShort(pool.size()));
		
		// Store class information, such as the flags, name, superclass,
		// interfaces, class type, and version
		ClassFile input = this.input;
		__dos.writeInt(input.flags().toJavaBits());
		__dos.writeShort(Minimizer.__checkUShort(thisname));
		__dos.writeShort(Minimizer.__checkUShort(supername));
		__dos.writeShort(Minimizer.__checkUShort(inames));
		__dos.writeByte(input.type().ordinal());
		__dos.writeByte(input.version().ordinal());
		
		// Needed for debugging to figure out what file the class is in,
		// will be very useful
		__dos.writeShort(snfid);
		
		// Write static and instance field counts
		for (int i = 0; i < 2; i++)
		{
			__TempFields__ tf = fields[i];
			
			__dos.writeShort(Minimizer.__checkUShort(tf._count));
			__dos.writeShort(Minimizer.__checkUShort(tf._bytes));
			__dos.writeShort(Minimizer.__checkUShort(tf._objects));
		}
		
		// Write static and instance method counts
		for (int i = 0; i < 2; i++)
		{
			__TempMethods__ tm = methods[i];
			
			__dos.writeShort(Minimizer.__checkUShort(tm._count));
		}
		
		// Relative offset where all the data will end up being, starts at
		// the constant pool address. Size is calculated as:
		// written + pooloff/len + fieldoff/len + methodoff/len + fileoff/len
		int reloff = __dos.size() + 8 + 16 + 16 + 8,
			baserel = reloff;
		
		// Base round to pool data
		reloff = Minimizer.__relAdd(reloff, 0);
		byte[] pooldata = pool.getBytes();
		
		// Constant pool locator
		__dos.writeInt(reloff);
		__dos.writeInt(pooldata.length);
		
		// Round
		reloff = Minimizer.__relAdd(reloff, pooldata.length);
		
		// Field locator
		byte[][] fielddata = new byte[2][];
		for (int i = 0; i < 2; i++)
		{
			__TempFields__ tf = fields[i];
			
			// Get bytes
			byte[] data = tf.getBytes(pool);
			fielddata[i] = data;
			
			// Offset and size
			__dos.writeInt(reloff);
			__dos.writeInt(data.length);
			
			// Round
			reloff = Minimizer.__relAdd(reloff, data.length);
		}
		
		// Method locator
		byte[][] methoddata = new byte[2][];
		for (int i = 0; i < 2; i++)
		{
			__TempMethods__ tm = methods[i];
			
			// Get bytes
			byte[] data = tm.getBytes(pool);
			methoddata[i] = data;
			
			// Offset and size
			__dos.writeInt(reloff);
			__dos.writeInt(data.length);
			
			// Round
			reloff = Minimizer.__relAdd(reloff, data.length);
		}
		
		// Write absolute file size! This saves time in calculating how big
		// a file we have and we can just read that many bytes for all the
		// data areas or similar if needed
		__dos.writeInt(reloff + 4);
		__dos.writeInt((reloff - baserel) + 4);
		
		// Constant pool is rounded
		Minimizer.__dosRound(__dos);
		
		// Write constant pool
		__dos.write(pooldata);
		Minimizer.__dosRound(__dos);
		
		// Write field data
		for (int i = 0; i < 2; i++)
		{
			__dos.write(fielddata[i]);
			Minimizer.__dosRound(__dos);
		}
		
		// Write method data
		for (int i = 0; i < 2; i++)
		{
			__dos.write(methoddata[i]);
			Minimizer.__dosRound(__dos);
		}
		
		// Write end magic number
		__dos.writeInt(MinimizedClassHeader.END_MAGIC_NUMBER);
	}
	
	/**
	 * Process fields.
	 *
	 * @return The resulting fields, static and instance split into each.
	 * @since 2019/03/11
	 */
	private final __TempFields__[] __doFields()
	{
		MinimizedPoolBuilder pool = this.pool;
		
		// Static and instance fields are split because they are stored
		// in different places
		__TempFields__[] rv = new __TempFields__[]{
			new __TempFields__(), new __TempFields__()};
		
		// Check if this is object or an array, so that special fields are
		// added
		boolean isobject = this.input.thisName().toString().
				equals("java/lang/Object"),
			isarray = this.input.thisName().isArray();
		
		// Perform some sorting to optimize slightly and make the layout a
		// bit friendlier
		List<Field> sorted = new ArrayList<>(this.input.fields());
		Collections.sort(sorted, new __MinimizerFieldSort__());
		
		// If this is an object, add the special class type (internal class
		// pointer) and the reference count
		if (isobject)
		{
			// Synthetic + Transient + Final
			sorted.add(0, new Field(new FieldFlags(0x1090),
				new FieldName("_class"),
				FieldDescriptor.INTEGER, null, null));
			
			// Synthetic + Transient + Volatile
			sorted.add(1, new Field(new FieldFlags(0x10c0),
				new FieldName("_refcount"),
				FieldDescriptor.INTEGER, null, null));
		}
		
		// If an array, add the length of the array
		if (isarray)
		{
			// Synthetic + Transient + Final
			sorted.add(0, new Field(new FieldFlags(0x1090),
				new FieldName("_length"),
				FieldDescriptor.INTEGER, null, null));
		}
		
		// Process each field
		for (Field f : sorted)
		{
			// These are stored in their own rows
			__TempFields__ temp = rv[(f.flags().isStatic() ? 0 : 1)];
			
			// Determine the size of this entry (and its alignment)
			PrimitiveType pt = f.type().primitiveType();
			int fsz = (pt == null ? 4 : pt.bytes());
			
			// If this is an object increase the object count, this is needed
			// by the garbage collector to determine the addresses to scan
			// However, the object class always has no objects in it
			if (!isobject && pt == null)
				temp._objects++;
			
			// Determine the base position and check if any alignment is needed
			// assuming types of a given size are always aligned
			int basep = (temp._bytes + (fsz - 1)) & ~(fsz - 1);
			
			// Constant value may be null, but if it is not then add it
			// to the pool
			ConstantValue cval = f.constantValue();
			
			// Build field information
			MinimizedField q;
			temp._fields.add((q = new MinimizedField(
				f.flags().toJavaBits(),
				basep,
				fsz,
				new FieldName(pool.<String>addSelf(f.name().toString())),
				pool.<ClassName>addSelf(f.type().className()).field(),
				(cval == null ? null :
					pool.<Object>addSelf(cval.boxedValue())))));
			
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
		MinimizedPoolBuilder pool = this.pool;
		
		// Split static and instance methods to make them easier to locate
		// accordingly
		__TempMethods__[] rv = new __TempMethods__[]{
			new __TempMethods__(), new __TempMethods__()};
		
		// Process each method
		for (Method m : this.input.methods())
		{
			// These are stored in their own rows
			__TempMethods__ temp = rv[(m.flags().isStatic() ? 0 : 1)];
			
			// Need to translate and serialize the byte code into a register
			// form and remap any used references.
			MethodFlags mf = m.flags();
			byte[] transcode = null;
			byte[] lnt = null;
			if (!mf.isAbstract() && !mf.isNative())
			{
				// The minified classes use register code since it is easier
				// to handle by the VM
				NativeCode rc = m.nativeCode();
				
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
				temp._count,
				new MethodName(pool.<String>addSelf(m.name().toString())),
				pool.<MethodDescriptor>addSelf(m.type()),
				transcode,
				lnt)));
			
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
	private final byte[] __translateCode(NativeCode __rc)
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
			NativeInstruction i = __rc.get(cdx);
			
			// Record that the instruction is at this position
			indexpos[cdx] = dos.size();
			
			// Write operation
			int op = i.operation();
			dos.write(op);
			
			// Encode arguments
			ArgumentFormat[] format = i.argumentFormat();
			for (int a = 0, an = i.argumentCount(),
				afn = format.length; a < an; a++)
			{
				// Read argument
				Object v = i.argument(a);
				
				// Write the format
				switch (format[a])
				{
						// Variable 16-bit unsigned integer
					case VUINT:
					case VPOOL:
					case VJUMP:
						// Remap value
						int vm = 0;
						switch (format[a])
						{
							case VPOOL:
								vm = pool.add(v);
								break;
								
							case VJUMP:
								jumpreps.put(dos.size(),
									(InstructionJumpTarget)v);
								
								// Do not know if the full address can fit
								vm = 32767;
								break;
							
							case VUINT:
								vm = ((Number)v).intValue();
								break;
						}
						
						// Fits in 7-bit value
						if (vm >= 0 && vm <= 127)
							dos.write(vm);
						
						// Fits in 15-bit value
						else if (vm >= 128 && vm <= 32767)
						{
							dos.write(0x80 | (vm >> 8));
							dos.write(vm & 0xFF);
						}
						
						// {@squirreljme.error JC3r 15-bit integer out of
						// range. (The value)}
						else
							throw new InvalidClassFormatException(
								"JC3r " + vm);
						break;
						
						// Register List
					case REGLIST:
						{
							// Scan register list to see if it is "wide", if
							// it is then the wide format is used
							RegisterList rl = (RegisterList)v;
							int rn = rl.size();
							boolean wide = (rn > 127);
							for (int r = 0; r < rn; r++)
							{
								int rv = rl.get(r);
								if (rv < 0 || rv > 127)
								{
									wide = true;
									break;
								}
							}
							
							// Wide
							if (wide)
							{
								// Count
								dos.write(0x80 | (rn >> 8));
								dos.write(rn & 0xFF);
								
								// Write as shorts
								for (int r = 0; r < rn; r++)
									dos.writeShort(rl.get(r));
							}
							
							// Narrow
							else
							{
								// Count
								dos.write(rn);
								
								// Write as bytes
								for (int r = 0; r < rn; r++)
									dos.write(rl.get(r));
							}
						}
						break;
						
						// 32-bit int
					case INT32:
						dos.writeInt(((Number)v).intValue());
						break;
					
						// 64-bit long
					case INT64:
						dos.writeLong(((Number)v).longValue());
						break;
					
						// 32-bit float
					case FLOAT32:
						dos.writeInt(Float.floatToRawIntBits(
							((Number)v).floatValue()));
						break;
					
						// 64-bit double
					case FLOAT64:
						dos.writeLong(Double.doubleToRawLongBits(
							((Number)v).doubleValue()));
						break;
					
						// Unknown
					default:
						throw new todo.OOPS();
				}
			}
		}
		
		// Generate array
		byte[] rv = baos.toByteArray();
		
		// Replace jumps
		for (Map.Entry<Integer, InstructionJumpTarget> e : jumpreps.entrySet())
		{
			int ai = e.getKey(),
				jt = e.getValue().target();
			
			// Wide
			if ((rv[ai] & 0x80) != 0)
			{
				rv[ai] = (byte)(0x80 | (jt >> 8));
				rv[ai + 1] = (byte)(jt & 0xFF);
			}
			
			// Narrow
			else
				rv[ai] = (byte)jt;
		}
		
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
	
	/**
	 * Writes variable size unsigned short, only 15-bits are possible to be
	 * written.
	 *
	 * @param __dos The stream to write to.
	 * @param __v The value to write.
	 * @throws IOException ON write errors.
	 * @throws NullPointerException On null arguments.
	 * @since 2019/04/14
	 */
	public static final void writeVUShort(DataOutputStream __dos, int __v)
		throws IOException, NullPointerException
	{
		if (__dos == null)
			throw new NullPointerException("NARG");
		
		// Single byte form (7-bit)
		if (__v >= 0 && __v < 127)
			__dos.writeByte(__v);
		
		// Double byte form (15-bits)
		else if (__v >= 128 && __v < 32768)
		{
			__dos.writeByte(0x80 | (__v >>> 8));
			__dos.writeByte(__v & 0xFF);
		}
		
		// {@squirreljme.error JC3q Variable unsigned short out of range.
		// (The value)}
		else
			throw new InvalidClassFormatException("JC3q " + __v);
	}
	
	/**
	 * Checks that the unsigned short is in range.
	 *
	 * @param __v The value to check.
	 * @return {@code __v}.
	 * @throws InvalidClassFormatException If the short is out of range.
	 * @since 2019/04/14
	 */
	static final int __checkUShort(int __v)
		throws InvalidClassFormatException
	{
		// {@squirreljme.error JC3n Unsigned short out of range. (The value)}
		if (__v < 0 || __v > 65535)
			throw new InvalidClassFormatException("JC3n " + __v);
		return __v;
	}
	
	/**
	 * Pads the output stream.
	 *
	 * @param __dos The stream to pad.
	 * @throws IOException On write errors.
	 * @throws NullPointerException On null arguments.
	 * @since 2019/04/14
	 */
	static final void __dosRound(DataOutputStream __dos)
		throws IOException, NullPointerException
	{
		if (__dos == null)
			throw new NullPointerException("NARG");
		
		// Add padding
		while ((__dos.size() & 3) != 0)
			__dos.write(0);
	}
	
	/**
	 * Adds length data to the relative offset.
	 *
	 * @param __rel Current relative offset.
	 * @param __v The offset to add.
	 * @since 2019/04/14
	 */
	static final int __relAdd(int __rel, int __v)
	{
		__rel += __v;
		return (__rel + 3) & (~3);
	}
}

