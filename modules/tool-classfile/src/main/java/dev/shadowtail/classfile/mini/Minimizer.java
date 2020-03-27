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
import dev.shadowtail.classfile.nncc.RegisterList;
import dev.shadowtail.classfile.pool.DualClassRuntimePoolBuilder;
import dev.shadowtail.classfile.xlate.DataType;
import java.io.ByteArrayOutputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.util.ArrayList;
import java.util.Collections;
import java.util.LinkedList;
import java.util.List;
import java.util.Random;
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
import net.multiphasicapps.classfile.MethodFlags;
import net.multiphasicapps.classfile.PrimitiveType;
import net.multiphasicapps.io.TableSectionOutputStream;

/**
 * This takes an input class file and translates it to the minimized format
 * which is easier to use.
 *
 * @since 2019/03/10
 */
public final class Minimizer
{
	/** Counter for UUIDs. */
	private static volatile int _UUID_COUNTER =
		17;
	
	/** The class file to minimize. */
	protected final ClassFile input;
	
	/** The Jar or ROM backed pool, is optional. */
	protected final DualClassRuntimePoolBuilder jarpool;
	
	/** The local constant pool. */
	protected final DualClassRuntimePoolBuilder localpool =
		new DualClassRuntimePoolBuilder();
	
	/**
	 * Initializes the minimizer.
	 *
	 * @param __dp The dual constant pool to use, may be null.
	 * @param __cf The class to minimize.
	 * @throws NullPointerException On null arguments.
	 * @since 2019/03/10
	 */
	private Minimizer(DualClassRuntimePoolBuilder __dp, ClassFile __cf)
		throws NullPointerException
	{
		if (__cf == null)
			throw new NullPointerException("NARG");
		
		this.input = __cf;
		
		// This is the packing JAR/ROM pool, this may be null
		this.jarpool = __dp;
	}
	
	/**
	 * Performs the minimization process.
	 *
	 * @param __os The stream to write the result to.
	 * @throws IOException On write errors.
	 * @throws NullPointerException On null arguments.
	 * @since 2019/03/10
	 */
	private void __run(OutputStream __os)
		throws IOException, NullPointerException
	{
		if (__os == null)
			throw new NullPointerException("NARG");
			
		// The input class
		ClassFile input = this.input;
		
		// The output section
		TableSectionOutputStream output = new TableSectionOutputStream();
		
		// This is the relative pool that the class file uses
		DualClassRuntimePoolBuilder localpool = this.localpool;
		
		// Process all fields and methods
		__TempFields__[] fields = this.__doFields();
		__TempMethods__[] methods = this.__doMethods();
		
		// Initialize header section
		TableSectionOutputStream.Section header =
			output.addSection(MinimizedClassHeader.HEADER_SIZE_WITH_MAGIC);
		
		// Write magic number to specify this format
		header.writeInt(MinimizedClassHeader.MAGIC_NUMBER);
		
		// Unused, may be used later when needed
		header.writeShort(0);
		
		// The index of the instance method named __start
		header.writeByte(methods[1].findMethodIndex("__start"));
		
		// Data type of the class
		header.writeByte(DataType.of(input.thisName().field()).ordinal());
		
		// This may be null for Object
		ClassName supernamecn = input.superName();
		
		// Unused
		header.writeShort(0);
		
		// Store class information, such as the flags, name, superclass,
		// interfaces, class type, and version
		header.writeInt(input.flags().toJavaBits());
		header.writeUnsignedShortChecked(
			localpool.add(false, input.thisName()).index);
		header.writeUnsignedShortChecked((supernamecn == null ? 0 :
			localpool.add(false, supernamecn).index));
		header.writeUnsignedShortChecked(
			localpool.add(false, input.interfaceNames()).index);
		header.writeByte(input.type().ordinal());
		header.writeByte(input.version().ordinal());
		
		// Needed for debugging to figure out what file the class is in,
		// will be very useful
		String sfn = input.sourceFile();
		header.writeUnsignedShortChecked((sfn == null ? 0 :
			localpool.add(false, sfn).index));
		
		// Write static and instance field counts
		for (int i = 0; i < 2; i++)
		{
			__TempFields__ tf = fields[i];
			
			header.writeUnsignedShortChecked(tf._count);
			header.writeUnsignedShortChecked((tf._bytes + 3) & (~3));
			header.writeUnsignedShortChecked(tf._objects);
		}
		
		// Write static and instance method counts
		for (int i = 0; i < 2; i++)
		{
			__TempMethods__ tm = methods[i];
			
			header.writeUnsignedShortChecked(tm._count);
		}
		
		// Unused, the pool was here
		header.writeInt(0);
		header.writeInt(0);
		
		// Field locator
		for (int i = 0; i < 2; i++)
		{
			// Generate section
			TableSectionOutputStream.Section subsection =
				output.addSection(fields[i].getBytes(localpool), 4);
			
			// Write section details
			header.writeSectionAddressInt(subsection);
			header.writeSectionSizeInt(subsection);
		}
		
		// Method locator
		for (int i = 0; i < 2; i++)
		{
			// Generate section
			TableSectionOutputStream.Section subsection =
				output.addSection(methods[i].getBytes(localpool), 4);
			
			// Write section details
			header.writeSectionAddressInt(subsection);
			header.writeSectionSizeInt(subsection);
		}
		
		// Generate a UUID and write it
		long uuid = Minimizer.generateUUID();
		header.writeInt((int)(uuid >>> 32));
		header.writeInt((int)uuid);
		
		// Write absolute file size! This saves time in calculating how big
		// a file we have and we can just read that many bytes for all the
		// data areas or similar if needed
		header.writeFileSizeInt();
		
		// Not used anymore
		header.writeInt(0);
		
		// Where our pools are going
		TableSectionOutputStream.Section lpd = output.addSection();
		
		// Encode the local pool or the local pool on top of the JAR pool
		DualClassRuntimePoolBuilder jarpool = this.jarpool;
		DualPoolEncodeResult der = (jarpool == null ?
			DualPoolEncoder.encode(localpool, lpd) :
			DualPoolEncoder.encodeLayered(localpool, jarpool, lpd));
		
		// Static pool
		header.writeSectionAddressInt(lpd, der.staticpooloff);
		header.writeInt(der.staticpoolsize);
		
		// Run-time pool
		header.writeSectionAddressInt(lpd, der.runtimepooloff);
		header.writeInt(der.runtimepoolsize);
		
		// Write end magic number, which is at the end of the file
		TableSectionOutputStream.Section eofmagic = output.addSection(4);
		eofmagic.writeInt(MinimizedClassHeader.END_MAGIC_NUMBER);
		
		// Write resulting file
		output.writeTo(__os);
	}
	
	/**
	 * Process fields.
	 *
	 * @return The resulting fields, static and instance split into each.
	 * @since 2019/03/11
	 */
	private __TempFields__[] __doFields()
	{
		DualClassRuntimePoolBuilder localpool = this.localpool;
		
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
		// pointer) and the reference count. These are always in a fixed
		// order.
		if (isobject)
		{
			// (ClassInfo) Synthetic + Transient + Final
			sorted.add(0, new Field(new FieldFlags(0x1090),
				new FieldName("_class"),
				FieldDescriptor.INTEGER, null, null));
			
			// (Reference count) Synthetic + Transient + Volatile
			sorted.add(1, new Field(new FieldFlags(0x10c0),
				new FieldName("_refcount"),
				FieldDescriptor.INTEGER, null, null));
			
			// (monitor owning thread) Synthetic + Transient + Volatile
			sorted.add(2, new Field(new FieldFlags(0x10c0),
				new FieldName("_monitor"),
				FieldDescriptor.INTEGER, null, null));
			
			// (monitor enter count) Synthetic + Transient + Volatile
			sorted.add(3, new Field(new FieldFlags(0x10c0),
				new FieldName("_moncount"),
				FieldDescriptor.INTEGER, null, null));
		}
		
		// If an array, add the length of the array
		else if (isarray)
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
			int basep = (temp._bytes + (fsz - 1)) & -fsz;
			
			// Constant value may be null, but if it is not then add it
			// to the pool
			ConstantValue cval = f.constantValue();
			
			// Add field properties
			localpool.add(false, f.name().toString());
			localpool.add(false, f.type().className());
			
			// Boxed value, if used
			if (cval != null)
				localpool.add(false, cval.boxedValue());
			
			// Build field information
			MinimizedField q;
			temp._fields.add((q = new MinimizedField(
				f.flags().toJavaBits(),
				basep,
				fsz,
				f.name(),
				f.type(),
				(cval == null ? null : cval.boxedValue()))));
			
			// Handle table sizes
			temp._bytes = basep + fsz;
			temp._count++;
		}
		
		// Align fields sizes to 4-bytes always so that the next class on
		// top of this has aligned field data
		for (__TempFields__ temp : rv)
			temp._bytes = (temp._bytes + 3) & ~3;
		
		// Return static and instance fields
		return rv;
	}
	
	/**
	 * Processes all methods within the class.
	 *
	 * @return The processed static and instance methods.
	 * @since 2019/03/13
	 */
	private __TempMethods__[] __doMethods()
	{
		DualClassRuntimePoolBuilder localpool = this.localpool;
		ClassFile input = this.input;
		
		// Split static and instance methods to make them easier to locate
		// accordingly
		__TempMethods__[] rv = new __TempMethods__[]{
			new __TempMethods__(input.thisName()),
			new __TempMethods__(input.thisName())};
		
		// Process each method
		for (Method m : input.methods())
		{
			// These are stored in their own rows
			__TempMethods__ temp = rv[(m.flags().isStatic() ? 0 : 1)];
			
			// Need to translate and serialize the byte code into a register
			// form and remap any used references.
			MethodFlags mf = m.flags();
			byte[] transcode = null;
			if (!mf.isAbstract() && !mf.isNative())
			{
				// The minified classes use register code since it is easier
				// to handle by the VM
				NativeCode rc = m.nativeCode();
				
				// Encode data to bytes
				try
				{
					// Translate code
					transcode = this.__translateCode(rc);
				}
				
				// {@squirreljme.error JC0j Error during translation of the
				// specified method. (The current class; The method)}
				catch (InvalidClassFormatException e)
				{
					throw new InvalidClassFormatException("JC4s " +
						input.thisName() + " " + m.nameAndType(), e);
				}
				
				// {@squirreljme.error JC0k IOException translating code.}
				catch (IOException e)
				{
					throw new RuntimeException("JC0k", e);
				}
			}
			
			// Add name and type to the pool
			localpool.add(false, m.name().toString());
			localpool.add(false, m.type());
			
			// Add method
			MinimizedMethod q;
			temp._methods.add((q = new MinimizedMethod(mf.toJavaBits(),
				temp._count,
				m.name(),
				m.type(),
				transcode)));
			
			// Quick count for used methods
			temp._count++;
		}
		
		return rv;
	}
	
	/**
	 * Translates code.
	 *
	 * @param __rc The register code used.
	 * @return The resulting stream.
	 * @throws IOException On write errors.
	 * @throws NullPointerException On null arguments.
	 * @since 2019/03/23
	 */
	private byte[] __translateCode(NativeCode __rc)
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
		List<__Jump__> jumpreps = new LinkedList<>();
		
		// Operations will reference this constant pool
		DualClassRuntimePoolBuilder localpool = this.localpool;
		
		// Go through each instruction
		for (int cdx = 0; cdx < cdn; cdx++)
		{
			// Get instruction here
			NativeInstruction i = __rc.get(cdx);
			
			// Record that the instruction is at this position
			int baseaddr;
			indexpos[cdx] = (baseaddr = dos.size());
			
			// Operation to handle
			int op = i.operation();
			
			// Write operation
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
					case VUREG:
					case VPOOL:
					case VJUMP:
						// Remap value
						int vm = 0;
						switch (format[a])
						{
							case VPOOL:
								try
								{
									vm = localpool.add(true, v).index;
								}
								catch (IllegalArgumentException e)
								{
									// {@squirreljme.error JC4g Could not add
									// pool entry because it is not valid.
									// (The current instruction)}
									throw new InvalidClassFormatException(
										"JC4g " + i, e);
								}
								break;
								
							case VJUMP:
								{
									// Store for later modification
									jumpreps.add(new __Jump__(cdx, dos.size(),
										((InstructionJumpTarget)v).target()));
									
									// Do not know if the full address can fit
									vm = 32767;
								}
								break;
							
							case VUINT:
							case VUREG:
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
						
						// {@squirreljme.error JC0l 15-bit integer out of
						// range. (The value; The instruction index; The
						// invalid instruction)}
						else
							throw new InvalidClassFormatException(
								"JC0l " + vm + " " + cdx + " " + i);
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
						throw new todo.OOPS(i.toString());
				}
			}
		}
		
		// Generate code array
		byte[] rv = baos.toByteArray();
		int codesize = rv.length;
		
		// Replace jumps
		for (__Jump__ j : jumpreps)
		{
			// Calculate target
			int cdx = j.cdx,
				ai = j.dss,
				origjt = j.jt,
				jt = indexpos[origjt] - indexpos[cdx];
			
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
	 * Generates a UUID to identify the minimized class.
	 *
	 * @return The generated UUID.
	 * @since 2019/04/27
	 */
	public static final long generateUUID()
	{
		// Hopefully this seed is good enough?
		Random rand = new Random(System.nanoTime() +
			(System.currentTimeMillis() << 24) + (++Minimizer._UUID_COUNTER));
		
		// Skip a random amount of values to run it for a bit
		for (int i = 0, n = rand.nextInt(32 + rand.nextInt(32)); i < n; i++)
			rand.nextInt();
		
		// Just use the next long value
		return rand.nextLong();
	}
	
	/**
	 * Minimizes the given class and returns the minimized version of it.
	 *
	 * @param __cf The class to minimize.
	 * @return The resulting minimized class as a byte array.
	 * @throws InvalidClassFormatException If the class is not formatted
	 * correctly.
	 * @throws NullPointerException On null arguments.
	 * @since 2019/07/17
	 */
	public static final byte[] minimize(ClassFile __cf)
		throws InvalidClassFormatException, IOException, NullPointerException
	{
		return Minimizer.minimize(null, __cf);
	}
	
	/**
	 * Minimizes the given class and returns the minimized version of it.
	 *
	 * @param __dp Dual constant pool, this may be {@code null}.
	 * @param __cf The class to minimize.
	 * @return The resulting minimized class as a byte array.
	 * @throws InvalidClassFormatException If the class is not formatted
	 * correctly.
	 * @throws NullPointerException On null arguments.
	 * @since 2019/03/10
	 */
	public static final byte[] minimize(DualClassRuntimePoolBuilder __dp,
		ClassFile __cf)
		throws InvalidClassFormatException, IOException, NullPointerException
	{
		if (__cf == null)
			throw new NullPointerException("NARG");
		
		// Minimization is straight to a byte format so just read that in
		// again
		try (ByteArrayOutputStream baos = new ByteArrayOutputStream(1048576))
		{
			// Output minimized code to the byte array
			Minimizer.minimize(__dp, __cf, baos);
			
			// The class data is in the resulting byte array
			return baos.toByteArray();
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
		Minimizer.minimize(null, __cf, __os);
	}
	
	/**
	 * Minimizes the class file so that it is in a more compact format as
	 * needed.
	 *
	 * @param __dp Dual constant pool, may be {@code null}.
	 * @param __cf The class file to minimize.
	 * @param __os The stream to write the minimized format to.
	 * @throws InvalidClassFormatException If the class format is not valid.
	 * @throws IOException On write errors.
	 * @throws NullPointerException On null arguments.
	 * @since 2019/03/10
	 */
	public static final void minimize(DualClassRuntimePoolBuilder __dp,
		ClassFile __cf, OutputStream __os)
		throws InvalidClassFormatException, IOException, NullPointerException
	{
		if (__cf == null || __os == null)
			throw new NullPointerException("NARG");
		
		new Minimizer(__dp, __cf).__run(__os);
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
	public static final MinimizedClassFile minimizeAndDecode(ClassFile __cf)
		throws InvalidClassFormatException, NullPointerException
	{
		if (__cf == null)
			throw new NullPointerException("NARG");
		
		// Minimize raw byte array
		try
		{
			return MinimizedClassFile.decode(Minimizer.minimize(__cf));
		}
		
		// {@squirreljme.error JC0m Could not minimize class due to a read
		// or write error.}
		catch (IOException e)
		{
			throw new RuntimeException("JC0m", e);
		}
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
		
		// {@squirreljme.error JC0n Variable unsigned short out of range.
		// (The value)}
		else
			throw new InvalidClassFormatException("JC0n " + __v);
	}
	
	/**
	 * Checks that the unsigned short is in range.
	 *
	 * @param __v The value to check.
	 * @return {@code __v}.
	 * @throws InvalidClassFormatException If the short is out of range.
	 * @since 2019/04/14
	 */
	@Deprecated
	static final int __checkUShort(int __v)
		throws InvalidClassFormatException
	{
		// {@squirreljme.error JC0o Unsigned short out of range. (The value)}
		if (__v < 0 || __v > 65535)
			throw new InvalidClassFormatException("JC0o " + __v);
		return __v;
	}
	
	/**
	 * Compacts a table of shorts or bytes to a run-length encoded form.
	 *
	 * @param __st The input table.
	 * @param __bt The byte table.
	 * @return The resulting RLE encoded byte data.
	 * @throws IOException On write errors.
	 * @throws NullPointerException On null arguments.
	 * @since 2019/03/24
	 */
	@Deprecated
	private static byte[] __compact(short[] __st, byte[] __bt)
		throws IOException, NullPointerException
	{
		if (__st == null && __bt == null)
			throw new NullPointerException("NARG");
		
		// Using shorts or bytes
		boolean shorts = (__st != null);
		int len = (shorts ? __st.length : __bt.length);
		
		// Target stream
		ByteArrayOutputStream baos = new ByteArrayOutputStream(len);
		DataOutputStream dos = new DataOutputStream(baos);
		
		// Go through every entry, compacting it accordingly
		int lastline = -1,
			lastpc = 0;
		for (int i = 0, n = len; i < n; i++)
		{
			int nowline = (shorts ? __st[i] : __bt[i]);
			
			// If there is a really long stretch of instructions which point
			// to the same exact data, force it to be reset so that way the
			// number table after this point is not complete garbage
			// Note that 255 is the end of data marker
			boolean force = ((i == 0) || ((i - lastpc) >= 254));
			
			// Line number has changed, need to encode the information
			if (force || nowline != lastline)
			{
				// Since multiple instruction addresses can share data info,
				// to reduce the size of the table just record an offset from
				// the last PC address
				// Just a single byte is used for the difference since in
				// general these ranges will be small
				int diff = i - lastpc;
				dos.write(diff);
				
				// Write data position here, just as a 16-bit value without
				// any different encoding since values can jump all over the
				// place
				if (shorts)
					dos.writeShort(nowline);
				else
					dos.writeByte(nowline);
				
				// For next time
				lastline = nowline;
				lastpc = i;
			}
		}
		
		// A difference of 255 means end of data
		dos.write(0xFF);
		
		// Return result
		return baos.toByteArray();
	}
	
	/**
	 * Pads the output stream.
	 *
	 * @param __dos The stream to pad.
	 * @return The current size of the output stream.
	 * @throws IOException On write errors.
	 * @throws NullPointerException On null arguments.
	 * @since 2019/04/14
	 */
	@Deprecated
	static final int __dosRound(DataOutputStream __dos)
		throws IOException, NullPointerException
	{
		if (__dos == null)
			throw new NullPointerException("NARG");
		
		// Add padding
		while ((__dos.size() & 3) != 0)
			__dos.write(0);
		return __dos.size();
	}
	
	/**
	 * Adds length data to the relative offset.
	 *
	 * @param __rel Current relative offset.
	 * @param __v The offset to add.
	 * @since 2019/04/14
	 */
	@Deprecated
	static final int __relAdd(int __rel, int __v)
	{
		__rel += __v;
		return (__rel + 3) & (~3);
	}
}

