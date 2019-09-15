// -*- Mode: Java; indent-tabs-mode: t; tab-width: 4 -*-
// ---------------------------------------------------------------------------
// Multi-Phasic Applications: SquirrelJME
//     Copyright (C) Stephanie Gawroriski <xer@multiphasicapps.net>
// ---------------------------------------------------------------------------
// SquirrelJME is under the GNU General Public License v3+, or later.
// See license.mkd for licensing and copyright information.
// ---------------------------------------------------------------------------

package dev.shadowtail.classfile.mini;

import dev.shadowtail.classfile.pool.AccessedField;
import dev.shadowtail.classfile.pool.BasicPool;
import dev.shadowtail.classfile.pool.BasicPoolBuilder;
import dev.shadowtail.classfile.pool.BasicPoolEntry;
import dev.shadowtail.classfile.pool.ClassInfoPointer;
import dev.shadowtail.classfile.pool.ClassPool;
import dev.shadowtail.classfile.pool.DualClassRuntimePool;
import dev.shadowtail.classfile.pool.DualClassRuntimePoolBuilder;
import dev.shadowtail.classfile.pool.FieldAccessTime;
import dev.shadowtail.classfile.pool.FieldAccessType;
import dev.shadowtail.classfile.pool.InvokeType;
import dev.shadowtail.classfile.pool.InvokedMethod;
import dev.shadowtail.classfile.pool.MethodIndex;
import dev.shadowtail.classfile.pool.NotedString;
import dev.shadowtail.classfile.pool.UsedString;
import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.util.ArrayList;
import java.util.List;
import net.multiphasicapps.classfile.ClassName;
import net.multiphasicapps.classfile.ClassNames;
import net.multiphasicapps.classfile.InvalidClassFormatException;
import net.multiphasicapps.classfile.MethodDescriptor;
import net.multiphasicapps.classfile.MethodHandle;
import net.multiphasicapps.classfile.MethodName;
import net.multiphasicapps.io.TableSectionOutputStream;

/**
 * This contains the encoder and decoder for dual pools.
 *
 * @since 2019/07/17
 */
public final class DualPoolEncoder
{
	/** The size of the table entries. */
	public static final int TABLE_ENTRY_SIZE =
		8;
	
	/**
	 * Not used.
	 *
	 * @since 2019/07/17
	 */
	private DualPoolEncoder()
	{
	}
	
	/**
	 * Decodes the specified pool.
	 *
	 * @param __b The input byte array.
	 * @param __co The class pool offset.
	 * @param __cl The class pool length.
	 * @param __ro The run-time pool offset.
	 * @param __rl The run-time pool length.
	 * @return The resulting dual pool.
	 * @throws InvalidClassFormatException If the pool is not valid.
	 * @throws IOException If the pool could not be read.
	 * @throws NullPointerException On null arguments.
	 * @since 2019/09/07
	 */
	public static final DualClassRuntimePool decode(byte[] __b,
		int __co, int __cl, int __ro, int __rl)
		throws InvalidClassFormatException, IOException, NullPointerException
	{
		if (__b == null)
			throw new NullPointerException("NARG");
		
		// The resulting pool
		BasicPool classpool = null,
			runpool = null;
			
		// Read of both pools
		for (boolean isruntime = false;; isruntime = true)
		{
			// The base offset and length used, used to read entry data
			int baseoff = (isruntime ? __ro : __co);
			
			// Which stream to read main entries from?
			DataInputStream dis = new DataInputStream(
				new ByteArrayInputStream(__b, baseoff,
					(isruntime ? __rl : __cl)));
			
			// Read the entry count (first null entry), ignore padding
			dis.readByte();
			dis.readByte();
			dis.readShort();
			int poolsize = dis.readInt();
			
			// Target entries
			List<BasicPoolEntry> entries = new ArrayList<>(poolsize);
			
			// Initialize null entry
			entries.add(new BasicPoolEntry(0, null, new int[0]));
			
			// Read in every entry
			for (int i = 1; i < poolsize; i++)
			{
				// Read entry properties
				int tag = dis.readByte() & 0xFF,
					numparts = dis.readByte(),
					elen = dis.readUnsignedShort(),
					eoff = dis.readInt();
				
				// Read type information
				MinimizedPoolEntryType etype;
				try
				{
					etype = MinimizedPoolEntryType.of(tag);
				}
				catch (IllegalArgumentException e)
				{
					// {@squirreljme.error JC4i Invalid constant pool entry.}
					throw new InvalidClassFormatException("JC4i", e);
				}
				
				// {@squirreljme.error JC4j The specified type cannot be within
				// the current pool section. (The type; If this is the run-time
				// pool)}
				if (etype.isRuntime() != isruntime)
					throw new InvalidClassFormatException("JC4j " + etype +
						" " + isruntime);
				
				// Get reader for the entry data
				DataInputStream xd = new DataInputStream(
					new ByteArrayInputStream(__b, baseoff + eoff, elen));
				
				// Is this wide?
				boolean iswide = (numparts < 0);
				if (iswide)
					numparts = -numparts;
				
				// Resulting parts and value
				Object value;
				int[] parts = new int[numparts];
				
				// Depends on the type
				switch (etype)
				{
						// String
					case STRING:
						// Read hash code and length
						parts[0] = xd.readUnsignedShort();
						parts[1] = xd.readUnsignedShort();
						
						// Decode actual string
						value = xd.readUTF();
						break;
						
						// Everything else just consists of parts which are
						// either values to other indexes or an ordinal
					case ACCESSED_FIELD:
					case CLASS_INFO_POINTER:
					case CLASS_NAME:
					case CLASS_NAMES:
					case CLASS_POOL:
					case INTEGER:
					case FLOAT:
					case LONG:
					case DOUBLE:
					case INVOKED_METHOD:
					case METHOD_DESCRIPTOR:
					case METHOD_INDEX:
					case NOTED_STRING:
					case USED_STRING:
						// Read parts
						if (iswide)
							for (int p = 0; p < numparts; p++)
								parts[p] = xd.readUnsignedShort();
						else
							for (int p = 0; p < numparts; p++)
								parts[p] = xd.readByte() & 0xFF;
						
						// Build value depending on the parts used
						switch (etype)
						{
								// Name of class, has extra meta-data
							case CLASS_NAME:
								value = new ClassName(entries.get(parts[0]).
									<String>value(String.class));
								break;
							
								// A list of class names
							case CLASS_NAMES:
								ClassName[] cnn = new ClassName[numparts];
								
								for (int j = 0; j < numparts; j++)
									cnn[j] = entries.get(parts[j]).
										<ClassName>value(ClassName.class);
								
								value = new ClassNames(cnn);
								break;
								
								// Raw integer value
							case INTEGER:
								value = ((parts[0] & 0xFFFF) << 16) |
									(parts[1] & 0xFFFF);
								break;
								
								// Raw float value
							case FLOAT:
								value = Float.intBitsToFloat(
									((parts[0] & 0xFFFF) << 16) |
									(parts[1] & 0xFFFF));
								break;
								
								// Raw long value
							case LONG:
								value = (((long)(((parts[0] & 0xFFFF) << 16) |
									((parts[1] & 0xFFFF))) &
										0xFFFFFFFFL) << 32L) |
									((long)(((parts[2] & 0xFFFF) << 16) |
									((parts[3] & 0xFFFF))) & 0xFFFFFFFFL);
								break;
								
								// Raw double value
							case DOUBLE:
								value = Double.longBitsToDouble(
									(((long)(((parts[0] & 0xFFFF) << 16) |
									((parts[1] & 0xFFFF))) &
										0xFFFFFFFFL) << 32L) |
									((long)(((parts[2] & 0xFFFF) << 16) |
									((parts[3] & 0xFFFF))) & 0xFFFFFFFFL));
								break;
								
								// Method descriptor, has extra meta-data
							case METHOD_DESCRIPTOR:
								value = new MethodDescriptor(
									entries.get(parts[0]).
									<String>value(String.class));
								break;
								
								// A field which has been accessed
							case ACCESSED_FIELD:
								value = new AccessedField(
									FieldAccessTime.of(parts[0]),
									FieldAccessType.of(parts[1]),
									classpool.<ClassName>byIndex(
										ClassName.class, parts[2]),
									classpool.<String>byIndex(
										String.class, parts[3]),
									classpool.<ClassName>byIndex(
										ClassName.class, parts[4]).field());
								break;
							
								// Class information point
							case CLASS_INFO_POINTER:
								value = new ClassInfoPointer(
									classpool.byIndex(parts[0]).
									<ClassName>value(ClassName.class));
								break;
								
								// Class run-time pool reference
							case CLASS_POOL:
								value = new ClassPool(
									classpool.byIndex(parts[0]).
									<ClassName>value(ClassName.class));
								break;
								
								// A method which has been invoked
							case INVOKED_METHOD:
								value = new InvokedMethod(
									InvokeType.of(parts[0]),
									classpool.<ClassName>byIndex(
										ClassName.class, parts[1]),
									classpool.<String>byIndex(String.class,
										parts[2]),
									classpool.<MethodDescriptor>byIndex(
										MethodDescriptor.class, parts[3]));
								break;
								
								// Method index in vtable
							case METHOD_INDEX:
								value = new MethodIndex(
									classpool.<ClassName>byIndex(
										ClassName.class, parts[1]),
									classpool.<String>byIndex(
										String.class, parts[2]),
									classpool.<MethodDescriptor>byIndex(
										MethodDescriptor.class, parts[3]));
								break;
								
								// Noted string
							case NOTED_STRING:
								value = new NotedString(
									classpool.byIndex(parts[0]).
									<String>value(String.class));
								break;
								
								// Used string
							case USED_STRING:
								value = new UsedString(
									classpool.byIndex(parts[0]).
									<String>value(String.class));
								break;
								
								// Unknown
							default:
								throw new todo.OOPS(etype.name());
						}
						break;
						
						// Unknown
					default:
						throw new todo.OOPS(etype.name());
				}
				
				// Record entry
				entries.add(new BasicPoolEntry(i, value, parts, eoff));
			}
			
			// Build pool
			BasicPool result = new BasicPool(entries);
			
			// Store pool in the right spot
			if (isruntime)
				runpool = result;
			else
				classpool = result;
			
			// Stop processing after the run-time is done
			if (isruntime)
				break;
		}
		
		// Finalize pool
		return new DualClassRuntimePool(classpool, runpool);
	}
	
	/**
	 * Encodes the dual pool to the given output stream and returns the
	 * result.
	 *
	 * @param __dp The dual-pool to encode.
	 * @param __out The stream to write to.
	 * @return The result with size information.
	 * @throws IOException On write errors.
	 * @throws NullPointerException On null arguments.
	 * @since 2019/07/17
	 */
	public static final DualPoolEncodeResult encode(
		DualClassRuntimePoolBuilder __dp, OutputStream __out)
		throws IOException, NullPointerException
	{
		if (__dp == null || __out == null)
			throw new NullPointerException("NARG");
		
		// Table sections are used for output
		TableSectionOutputStream output = new TableSectionOutputStream();
		
		// Build sections for output
		TableSectionOutputStream.Section clssection = output.addSection(
			TableSectionOutputStream.VARIABLE_SIZE, 4);
		TableSectionOutputStream.Section runsection = output.addSection(
			TableSectionOutputStream.VARIABLE_SIZE, 4);
		
		// Write both of the pools
		for (boolean isruntime = false;; isruntime = true)
		{
			// Sub output which writes to the section
			TableSectionOutputStream sub = new TableSectionOutputStream();
			
			// Section containing the table of contents
			TableSectionOutputStream.Section toc = sub.addSection(
				TableSectionOutputStream.VARIABLE_SIZE, 4);
			
			// Are we encoding the static or run-time pool?
			BasicPoolBuilder pool = (isruntime ? __dp.runtimePool() :
				__dp.classPool());
			
			// The size of the pool, used for the first entry
			int poolsize = pool.size();
			
			// Write individual pool entries
			for (BasicPoolEntry e : pool)
			{
				// The first is always null and just contains some pool
				// information
				Object ev = e.value;
				if (e.index == 0)
				{
					// No tag, no parts, no size, entry count (offset)
					toc.write(0);
					toc.write(0);
					toc.writeShort(0);
					toc.writeInt(poolsize);
					
					// Skip
					continue;
				}
				
				// Determine the type of entry this is
				MinimizedPoolEntryType et =
					MinimizedPoolEntryType.ofClass(ev.getClass());
				
				// {@squirreljme.error JC4d Cannot store the given entry
				// because it not compatible with the static/run-time
				// state. (The pool type; The value type; Is the run-time
				// pool being processed?)}
				if (isruntime != et.isRuntime())
					throw new IllegalStateException("JC4d " +
						et + " " + ev + " " + isruntime);
				
				// Determine if the parts need to be wide
				short[] ep = e.parts();
				int epn = ep.length;
				boolean iswide = false;
				for (int j = 0; j < epn; j++)
				{
					int v = ep[j];
					if (v < 0 || v > 127)
						iswide = true;
				}
				
				// Encode the data into a section somewhere in this
				// sub-section
				TableSectionOutputStream.Section data = sub.addSection(
					TableSectionOutputStream.VARIABLE_SIZE, 4);
				data.write(DualPoolEncoder.encodeValue(et, ep, iswide, ev));
				
				// Write pool table entry
				toc.writeByte(et.ordinal());
				toc.writeByte((iswide ? -epn : epn));
				toc.writeSectionSizeShort(data);
				toc.writeSectionAddressInt(data);
			}
			
			// Write entire sub-section data to the target
			sub.writeTo((isruntime ? runsection : clssection));
			
			// Stop processing after the run-time is done
			if (isruntime)
				break;
		}
		
		// Write to the target stream
		output.writeTo(__out);
		
		// Return the location of the data, note that the addresses and
		// sizes would have been realized already
		return new DualPoolEncodeResult(
			output.sectionAddress(clssection), output.sectionSize(clssection),
			output.sectionAddress(runsection), output.sectionSize(runsection));
	}
	
	/**
	 * Encodes a layered pool from one class to another.
	 *
	 * @param __src The source pool.
	 * @param __onto The pool to layer on top of.
	 * @param __out The stream to write to.
	 * @return The result of the encoded pool data.
	 * @throws IOException On write errors.
	 * @throws NullPointerException On null arguments.
	 * @since 2019/08/25
	 */
	public static final DualPoolEncodeResult encodeLayered(
		DualClassRuntimePoolBuilder __src, DualClassRuntimePoolBuilder __onto,
		OutputStream __out)
		throws IOException, NullPointerException
	{
		if (__src == null || __onto == null || __out == null)
			throw new NullPointerException("NARG");
		
		// The resulting table
		TableSectionOutputStream table = new TableSectionOutputStream();
		
		// Process static entries
		TableSectionOutputStream.Section sl = table.addSection(
			TableSectionOutputStream.VARIABLE_SIZE, 4);
		for (BasicPoolEntry e : __src.classPool())
		{
			Object v = e.value;
			if (v == null)
				sl.writeInt(__src.classPool().size());
			else
				sl.writeInt(__onto.add(false, v).index);
		}
		
		// Process run-time entries
		TableSectionOutputStream.Section rl = table.addSection(
			TableSectionOutputStream.VARIABLE_SIZE, 4);
		for (BasicPoolEntry e : __src.runtimePool())
		{
			Object v = e.value;
			if (v == null)
				sl.writeInt(__src.runtimePool().size());
			else
				sl.writeInt(__onto.add(true, v).index);
		}
		
		// Write the encoded pool
		table.writeTo(__out);
		
		// Return the positions of everything
		return new DualPoolEncodeResult(
			table.sectionAddress(sl), -table.sectionSize(sl),
			table.sectionAddress(rl), -table.sectionSize(rl));
	}
	
	/**
	 * Encodes the pool value.
	 *
	 * @param __t The type to encode.
	 * @param __p The parts.
	 * @param __wide Are the parts wide?
	 * @param __v The value.
	 * @return The encoded value data.
	 * @throws IOException On write errors.
	 * @throws NullPointerException On null arguments.
	 * @since 2019/07/20
	 */
	public static final byte[] encodeValue(MinimizedPoolEntryType __t,
		short[] __p, boolean __wide, Object __v)
		throws IOException, NullPointerException
	{
		if (__t == null || __p == null || __v == null)
			throw new NullPointerException("NARG");
		
		// Output for this data
		TableSectionOutputStream output = new TableSectionOutputStream();
		TableSectionOutputStream.Section dos = output.addSection();
		
		// Depends on the type
		switch (__t)
		{
				// Encode strings into the pool
			case STRING:
				{
					// Record hashCode and the String size as simple
					// fields to read. Note that even though there is
					// the UTF length, the length of the actual string
					// could be useful. But only keep the lowest part
					// as that will be "good enough"
					dos.writeShort((short)__p[0]);
					dos.writeUnsignedShortChecked(__p[1]);
					
					// Write string UTF data
					dos.writeUTF((String)__v);
					
					// Write NUL terminator so it can be directly used
					// as a UTF-8 C pointer
					dos.writeByte(0);
				}
				break;
				
				// Everything else just consists of parts which are
				// either values to other indexes or an ordinal
			case ACCESSED_FIELD:
			case CLASS_INFO_POINTER:
			case CLASS_NAME:
			case CLASS_NAMES:
			case CLASS_POOL:
			case INTEGER:
			case FLOAT:
			case DOUBLE:
			case LONG:
			case INVOKED_METHOD:
			case METHOD_DESCRIPTOR:
			case METHOD_INDEX:
			case NOTED_STRING:
			case USED_STRING:
				if (__wide)
					for (int i = 0, n = __p.length; i < n; i++)
						dos.writeShortChecked(__p[i]);
				else
					for (int i = 0, n = __p.length; i < n; i++)
						dos.writeByte(__p[i]);
				break;
			
			default:
				throw new todo.OOPS(__t.name());
		}
		
		return output.toByteArray();
	}
}

