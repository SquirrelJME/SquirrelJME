// -*- Mode: Java; indent-tabs-mode: t; tab-width: 4 -*-
// ---------------------------------------------------------------------------
// Multi-Phasic Applications: SquirrelJME
//     Copyright (C) Stephanie Gawroriski <xer@multiphasicapps.net>
// ---------------------------------------------------------------------------
// SquirrelJME is under the GNU General Public License v3+, or later.
// See license.mkd for licensing and copyright information.
// ---------------------------------------------------------------------------

package dev.shadowtail.jarfile;

import cc.squirreljme.jvm.summercoat.constants.ClassProperty;
import cc.squirreljme.jvm.summercoat.constants.MemHandleKind;
import cc.squirreljme.jvm.summercoat.constants.StaticClassProperty;
import cc.squirreljme.runtime.cldc.debug.Debugging;
import cc.squirreljme.runtime.cldc.util.SortedTreeSet;
import dev.shadowtail.classfile.mini.MinimizedClassFile;
import dev.shadowtail.classfile.mini.MinimizedClassHeader;
import dev.shadowtail.classfile.mini.MinimizedField;
import dev.shadowtail.classfile.mini.Minimizer;
import dev.shadowtail.classfile.pool.BasicPool;
import dev.shadowtail.classfile.pool.BasicPoolEntry;
import dev.shadowtail.classfile.pool.DualClassRuntimePool;
import java.io.IOException;
import java.io.InputStream;
import java.util.Deque;
import java.util.HashMap;
import java.util.HashSet;
import java.util.LinkedHashMap;
import java.util.LinkedList;
import java.util.Map;
import java.util.Set;
import net.multiphasicapps.classfile.ClassFile;
import net.multiphasicapps.classfile.ClassName;
import net.multiphasicapps.classfile.ClassNames;
import net.multiphasicapps.classfile.FieldNameAndType;
import net.multiphasicapps.classfile.PrimitiveType;
import net.multiphasicapps.io.ChunkFuture;
import net.multiphasicapps.io.ChunkSection;

/**
 * The boot state of the system.
 *
 * @since 2020/12/13
 */
public final class BootState
{
	/** The length of an array. */
	private static final FieldNameAndType _ARRAY_LENGTH =
		new FieldNameAndType("_length", "I");
	
	/** The object class. */
	private static final ClassName _OBJECT_CLASS =
		new ClassName("java/lang/Object");
	
	/** The class for {@link Class}. */
	private static final ClassName _CLASS_CLASS =
		new ClassName("java/lang/Class");
	
	/** Object class info. */
	private static final FieldNameAndType _OBJECT_CLASS_INFO =
		new FieldNameAndType("_classInfo",
		Minimizer.CLASS_INFO_FIELD_DESC);
	
	/** The name of the string class. */
	private static final ClassName _STRING_CLASS =
		new ClassName("java/lang/String");
	
	/** The class data used. */
	private final Map<ClassName, ChunkSection> _rawChunks =
		new HashMap<>();
	
	/** Classes which have been read. */
	private final Map<ClassName, MinimizedClassFile> _readClasses =
		new HashMap<>();
	
	/** The state of all classes. */
	private final Map<ClassName, ClassState> _classStates =
		new HashMap<>();
	
	/** Actions performed on memory. */
	private final MemActions _memActions =
		new MemActions();
	
	/** Memory handles for the boot state, to be written accordingly. */
	private final MemHandles _memHandles =
		new MemHandles(this._memActions);
	
	/** Internal strings. */
	private final Map<String, MemHandle> _internStrings =
		new LinkedHashMap<>();
		
	/** The name of the boot class. */
	private ClassName _bootClass;
	
	/** The pool references to use for booting. */
	private DualClassRuntimePool _pool;
	
	/** Base array size. */
	int _baseArraySize =
		-1;
	
	/** The pool chunk. */
	private ChunkSection _poolChunk;
	
	/**
	 * Adds the specified class to be loaded and handled later.
	 * 
	 * @param __class The class name of this resource.
	 * @param __in The chunk to read from.
	 * @param __isBootClass Is this the boot class?
	 * @throws IOException On read errors.
	 * @throws NullPointerException On null arguments.
	 * @since 2020/12/13
	 */
	public void addClass(ClassName __class, ChunkSection __in,
		boolean __isBootClass)
		throws IOException, NullPointerException
	{
		if (__class == null || __in == null)
			throw new NullPointerException("NARG");
		
		// Store the raw chunk reference
		this._rawChunks.put(__class, __in);
		
		// Is this the entry class?
		if (__isBootClass)
			this._bootClass = __class;
	}
	
	/**
	 * Returns all of the interfaces that are implemented by the given class.
	 * 
	 * @param __cl The class to get for.
	 * @return All of the interfaces that the class implements.
	 * @throws IOException On read errors.
	 * @throws NullPointerException On null arguments.
	 * @since 2020/12/21
	 */
	public ClassNames allInterfaces(ClassName __cl)
		throws IOException, NullPointerException
	{
		if (__cl == null)
			throw new NullPointerException("NARG");
		
		// All types that are interfaces
		Set<ClassName> result = new SortedTreeSet<>();
		
		// Seed initial queue with the current class
		Deque<ClassName> queue = new LinkedList<>();
		queue.add(__cl);
		
		// Drain the queue and visit all the various classes
		Set<ClassName> visited = new HashSet<>();
		while (!queue.isEmpty())
		{
			// Only visit classes once
			ClassName visit = queue.remove();
			if (visited.contains(visit))
				continue;
			
			// Do not visit again
			visited.add(visit);
			
			// If this is an interface, use that
			ClassState state = this.loadClass(visit);
			if (state.classFile.flags().isInterface())
				result.add(visit);
			
			// Add the interfaces to the queue for processing 
			queue.addAll(state.classFile.interfaceNames());
			
			// Add the super class to go up if there is one
			ClassName parent = state.classFile.superName();
			if (parent != null)
				queue.add(parent);
		}
		
		return new ClassNames(result);
	}
	
	/**
	 * Performs the boot process for the system.
	 * 
	 * @param __pool The pool to use for loading.
	 * @param __lpd The local dual pool section.
	 * @param __outData The output data for the bootstrap.
	 * @param __startPoolHandleId The target handle ID for the pool.
	 * @throws IOException On read errors.
	 * @throws NullPointerException On null arguments.
	 * @since 2020/12/16
	 */
	public void boot(DualClassRuntimePool __pool, ChunkSection __lpd,
		ChunkSection __outData,
		int[] __startPoolHandleId)
		throws IOException, NullPointerException
	{
		if (__pool == null || __lpd == null || __outData == null ||
			__startPoolHandleId == null)
			throw new NullPointerException("NARG");
		
		// Set the boot pool because we need everything that is inside
		this._pool = __pool;
		this._poolChunk = __lpd;
		
		// Recursively load the boot class and any dependent class
		ClassState boot = this.loadClass(this._bootClass);
		
		// Chain link all the internal strings needed by the bootstrap so that
		// they are consistent. This also needs to handle weak references so
		// these strings can be GCed.
		Debugging.todoNote("Chainlink intern strings.");
		if (false)
			throw Debugging.todo();
		// this._internStrings
		
		// Determine the starting memory handle ID
		__startPoolHandleId[0] = boot._poolMemHandle.id;
		
		// Write the memory handles (and actions) into boot memory
		this._memHandles.chunkOut(__outData);
	}
	
	/**
	 * Loads a character array.
	 * 
	 * @param __v The values to store.
	 * @return The loaded character array.
	 * @throws IOException On read errors.
	 * @throws NullPointerException On null arguments.
	 * @since 2021/01/10
	 */
	public final ChunkMemHandle loadArrayChar(char... __v)
		throws IOException, NullPointerException
	{
		if (__v == null)
			throw new NullPointerException("NARG");
		
		// The class used
		ClassName type = PrimitiveType.CHARACTER
			.toClassName().addDimensions(1);
		
		// Prepare array to store the character data
		int n = __v.length;
		ChunkMemHandle rv = this.prepareArray(type, n);
		
		// Determine the base offset to write to
		int baseOff = this._baseArraySize;
		
		// Write all the elements to it
		for (int i = 0, off = baseOff; i < n; i++, off += 2)
			rv.write(off, MemoryType.SHORT, (int)__v[i]);
		
		return rv;
	}
	
	/**
	 * Loads the specified class.
	 * 
	 * @param __cl The class to load.
	 * @return The class state for the class.
	 * @throws IOException On read errors.
	 * @throws NullPointerException On null arguments.
	 * @since 2020/12/16
	 */
	public final ClassState loadClass(ClassName __cl)
		throws IOException, NullPointerException
	{
		if (__cl == null)
			throw new NullPointerException("NARG");
		
		// Has this class already been loaded?
		Map<ClassName, ClassState> classStates = this._classStates;
		ClassState rv = classStates.get(__cl);
		if (rv != null)
			return rv;
		
		// Debug
		Debugging.debugNote("Loading %s...", __cl);
		
		// Read the class data as fast as possible and store into the map so
		// we can recursive and recycle classes.
		MinimizedClassFile classFile = this.readClass(__cl);
		rv = new ClassState(classFile.thisName(), classFile);
		classStates.put(__cl, rv);
		
		// Header information to extract properties from
		MinimizedClassHeader header = classFile.header;
		
		// Allocate storage for the class information
		MemHandles memHandles = this._memHandles;
		ClassInfoHandle classInfo = memHandles.allocClassInfo(rv);
		rv._classInfoHandle = classInfo;
		
		// Copy all of the static properties since they would not normally
		// be copied
		for (int i = 0; i < StaticClassProperty.NUM_STATIC_PROPERTIES; i++)
			classInfo.set(i, header.get(i));
		
		// Everything is based on the run-time pool, so we need to load
		// everything inside
		BasicPool rtPool = classFile.pool.runtimePool();
		int rtPoolSz = rtPool.size();
		
		// Allocate storage for the constant pool
		PoolHandle pool = memHandles.allocPool(rtPoolSz);
		rv._poolMemHandle = pool;
		
		// The class pool is here
		classInfo.set(ClassProperty.POOLHANDLE_POOL, pool);
		
		// The class static fields have to go somewhere
		MemHandle staticFieldData = memHandles.allocFields(header.get(
			StaticClassProperty.SIZE_STATIC_FIELD_DATA));
		classInfo.set(ClassProperty.MEMHANDLEBASE_STATIC_FIELDS,
			staticFieldData);
		
		// Store the pointer to where the Class ROM exists in memory, but this
		// is only valid for non-special classes
		if (!__cl.isArray() && !__cl.isPrimitive())
			classInfo.set(ClassProperty.MEMPTR_ROM_CLASS,
				this._rawChunks.get(classFile.thisName()).futureAddress());
		
		// Need to determine if we are Object or our super class is Object
		// that way there can be shortcuts on resolution
		ClassName superClass = classFile.superName();
		boolean isThisObject = classFile.thisName().isObject();
		
		// This should never happen, hopefully!
		if (!__cl.isPrimitive() && !isThisObject && superClass == null)
			throw Debugging.oops(classFile.thisName(), superClass);
		
		// If this is the object class, then it is well known what the size
		// of the various classes are
		if (isThisObject)
		{
			classInfo.set(ClassProperty.OFFSETBASE_INSTANCE_FIELDS, 0);
			classInfo.set(ClassProperty.SIZE_ALLOCATION,
				header.get(StaticClassProperty.SIZE_INSTANCE_FIELD_DATA));
			
			// There is no depth to this class
			classInfo.set(ClassProperty.INT_CLASSDEPTH, 0);
		}
		
		// Primitive types are special cases
		else if (__cl.isPrimitive())
		{
			classInfo.set(ClassProperty.SIZE_ALLOCATION,
				__cl.primitiveType().bytes());
			
			// These do not make sense
			classInfo.set(ClassProperty.OFFSETBASE_INSTANCE_FIELDS, 0);
			classInfo.set(ClassProperty.INT_CLASSDEPTH, 0);
		}
		
		// Is there a super class for this class?
		ClassState superClassState = (superClass == null ? null :
			this.loadClass(superClass));
		if (superClass != null)
			classInfo.set(ClassProperty.CLASSINFO_SUPER,
				superClassState._classInfoHandle);
		
		// Store for later
		rv._superClass = superClassState;
		
		// Determine the allocation size of this class, we need to know this
		// as soon as possible but we can only determine this once object
		// is handled.
		if (!isThisObject && !__cl.isPrimitive())
		{
			// The base of the class is the allocation size of the super
			// class
			int base = superClassState._classInfoHandle
				.getInteger(ClassProperty.SIZE_ALLOCATION);
			classInfo.set(ClassProperty.OFFSETBASE_INSTANCE_FIELDS, base);
			
			// The allocation size of this class is the combined base and
			// our storage for fields
			classInfo.set(ClassProperty.SIZE_ALLOCATION, base +
				header.get(StaticClassProperty.SIZE_INSTANCE_FIELD_DATA));
			
			// This is one deeper than the super class
			classInfo.set(ClassProperty.INT_CLASSDEPTH,
				superClassState._classInfoHandle
					.getInteger(ClassProperty.INT_CLASSDEPTH) + 1);
		}
		
		// Fill in any interfaces the class implements
		ClassNames interfaces = classFile.interfaceNames();
		classInfo.set(ClassProperty.CLASSINFOS_INTERFACECLASSES,
			memHandles.allocClassInfos(
				this.loadClasses(interfaces.toArray())));
		
		// The name of the current class
		classInfo.set(ClassProperty.MEMHANDLE_THISNAME_DESC,
			this.loadString(__cl.toString()));
		classInfo.set(ClassProperty.MEMHANDLE_THISNAME_CLASSGETNAME,
			this.loadString(__cl.toRuntimeString()));
		
		// Store in constant values for fields
		for (MinimizedField staticField : classFile.fields(true))
		{
			Debugging.todoNote("Static field.");
		}
		
		// Determine the VTable for all non-interface instance methods
		if (true)
			Debugging.todoNote("Virtual VTable");
		
		// Determine all of the interfaces that this class possibly implements.
		// This will be used by instance checks and eventual interface VTable
		// lookup
		ClassNames allInterfaces = this.allInterfaces(__cl);
		classInfo.set(ClassProperty.CLASSINFOS_ALL_INTERFACECLASSES, 
			memHandles.allocClassInfos(
				this.loadClasses(allInterfaces.toArray())));
		
		// Pre-build all of the interface VTables for every interface that
		// this class implements, a class gets a VTable for all interfaces
		// and inherited interfaces it implements.
		// ex: ArrayList gets VTables for Cloneable, Collection<E>, List<E>,
		// RandomAccess
		if (true)
			Debugging.todoNote("Interface VTables");
		
		// Load the information for the Class<?> instance
		classInfo.set(ClassProperty.MEMHANDLE_LANGCLASS_INSTANCE,
			this.loadLangClass(__cl));
		
		// Set and initialize all of the entries within the pool
		for (int i = 0; i < rtPoolSz; i++)
		{
			Object pv = this.__loadPool(rtPool, rtPool.byIndex(i));
			
			// Depends on the type of value returned
			if (pv instanceof Integer)
				pool.set(i, (int)pv);
			else if (pv instanceof MemHandle)
				pool.set(i, (MemHandle)pv);
			else if (pv instanceof ChunkFuture)
				pool.set(i, (ChunkFuture)pv);
			else if (pv instanceof BootJarPointer)
				pool.set(i, (BootJarPointer)pv);
			
			// Do not know what to do with this
			else
				throw Debugging.oops(pv);
		}
		
		// The component class, if this is an array
		if (__cl.isArray())
			classInfo.set(ClassProperty.CLASSINFO_COMPONENTCLASS,
				this.loadClass(__cl.componentType())._classInfoHandle);
		
		// Determine the function pointer to the default new instance
		if (true)
			Debugging.todoNote("FuncPtr New");
		// FUNCPTR_DEFAULT_NEW
		
		// Loading the class is complete!
		return rv;
	}
	
	/**
	 * Loads multiple classes.
	 * 
	 * @param __names The names of the classes to load.
	 * @return The loaded classes.
	 * @throws IOException On read errors.
	 * @throws NullPointerException On null arguments.
	 * @since 2020/12/21
	 */
	public final ClassState[] loadClasses(ClassName... __names)
		throws IOException, NullPointerException
	{
		if (__names == null)
			throw new NullPointerException("NARG");
		
		int n = __names.length;
		ClassState[] rv = new ClassState[n];
		
		for (int i = 0; i < n; i++)
			rv[i] = this.loadClass(__names[i]);
		
		return rv;
	}
	
	/**
	 * Loads an instance of {@link Class} for a given object.
	 * 
	 * @param __cl The class to load for.
	 * @return The {@link Class} instance.
	 * @throws IOException On read errors.
	 * @throws NullPointerException On null arguments.
	 * @since 2020/12/21
	 */
	public MemHandle loadLangClass(ClassName __cl)
		throws IOException, NullPointerException
	{
		if (__cl == null)
			throw new NullPointerException("NARG");
			
		// Load the string class and prepare an object that we will be writing
		// into as required
		ChunkMemHandle object = this.prepareObject(BootState._CLASS_CLASS);
		
		// There just needs to be a handle to the class information, note
		// that class information is a kind of TypeBracket at least on
		// SummerCoat.
		this.objectFieldSet(object,
			new FieldNameAndType("_type",
				"Lcc/squirreljme/jvm/mle/brackets/TypeBracket;"),
			this.loadClass(__cl)._classInfoHandle);
		
		return object;
	}
	
	/**
	 * Loads a string into memory and returns the mem handle of where it is
	 * allocated.
	 * 
	 * @param __s The string to load.
	 * @return The memory handle of the string.
	 * @throws IOException On read errors.
	 * @throws NullPointerException On null arguments.
	 * @since 2020/12/21
	 */
	public final MemHandle loadString(String __s)
		throws IOException, NullPointerException
	{
		if (__s == null)
			throw new NullPointerException("NARG");
		
		// Has this string already been loaded? If so then we need to use
		// the pre-existing string here
		Map<String, MemHandle> intern = this._internStrings;
		MemHandle rv = intern.get(__s);
		if (rv != null)
			return rv;
		
		// Load the string class and prepare an object that we will be writing
		// into as required
		ChunkMemHandle object = this.prepareObject(BootState._STRING_CLASS);
		
		// The only part of string that needs to be set is the character data
		this.objectFieldSet(object,
			new FieldNameAndType("_chars", "[C"),
			this.loadArrayChar(__s.toCharArray()));
		
		return object;
	}
	
	/**
	 * Gets the value for the given instance field.
	 * 
	 * @param <V> The type of value requested.
	 * @param __cl The type of vlaue requested.
	 * @param __object The object to get.
	 * @param __nat The name and type of the field.
	 * @throws IllegalArgumentException If the class does not have that field.
	 * @throws IOException On read/write errors.
	 * @throws NullPointerException On null arguments.
	 * @since 2021/01/12
	 */
	public  <V> V objectFieldGet(Class<V> __cl, ChunkMemHandle __object,
		FieldNameAndType __nat)
		throws IllegalArgumentException, IOException, NullPointerException
	{
		if (__object == null || __nat == null)
			throw new NullPointerException("NARG");
		
		// Determine the starting point for the object scan
		ClassState start;
		if (__nat.equals(BootState._OBJECT_CLASS_INFO))
			start = this.loadClass(BootState._OBJECT_CLASS);
		else
			start = this.<ClassInfoHandle>objectFieldGet(ClassInfoHandle.class,
				__object, BootState._OBJECT_CLASS_INFO).classState();
		
		// Determine where in the object we will be reading the value
		for (ClassState at = start; at != null; at = at._superClass)
			for (MinimizedField f : at.classFile.fields(false))
				if (__nat.equals(f.nameAndType()))
				{
					// Where is this field located?
					int offset = f.offset + at._classInfoHandle.getInteger(
						ClassProperty.OFFSETBASE_INSTANCE_FIELDS);
					
					// Set value here
					return __object.<V>read(__cl, offset,
						MemoryType.of(__nat.type().dataType()));
				}
		
		// {@squirreljme.error BC0k Class does not contain the given field.
		// (The class; The field)}
		throw new IllegalArgumentException(String.format("BC0k %s %s",
			start.thisName, __nat));
	}
	
	/**
	 * Sets the given field for the object instance.
	 * 
	 * @param __object The object to set.
	 * @param __nat The name and type of the field.
	 * @param __v The value to store.
	 * @throws IllegalArgumentException If the class does not have that field.
	 * @throws IOException On read/write errors.
	 * @throws NullPointerException On null arguments.
	 * @since 2021/01/10
	 */
	public void objectFieldSet(ChunkMemHandle __object, FieldNameAndType __nat,
		Object __v)
		throws IllegalArgumentException, IOException, NullPointerException
	{
		if (__object == null || __nat == null || __v == null)
			throw new NullPointerException("NARG");
		
		// Determine the starting point for the object scan
		ClassState start;
		if (__nat.equals(BootState._OBJECT_CLASS_INFO))
			start = this.loadClass(BootState._OBJECT_CLASS);
		else
			start = this.<ClassInfoHandle>objectFieldGet(ClassInfoHandle.class,
				__object, BootState._OBJECT_CLASS_INFO).classState();
		
		// Determine where in the object we will be writing the value
		for (ClassState at = start; at != null; at = at._superClass)
			for (MinimizedField f : at.classFile.fields(false))
				if (__nat.equals(f.nameAndType()))
				{
					// Where is this field located?
					int offset = f.offset + at._classInfoHandle.getInteger(
						ClassProperty.OFFSETBASE_INSTANCE_FIELDS);
					
					// Set value here
					__object.write(offset,
						MemoryType.of(__nat.type().dataType()), __v);
					
					// Stop processing, since we set some value
					return;
				}
		
		// {@squirreljme.error BC0j Class does not contain the given field.
		// (The class; The field)}
		throw new IllegalArgumentException(String.format("BC0j %s %s",
			start.thisName, __nat));
	}
	
	/**
	 * Prepares an array instance.
	 * 
	 * @param __cl The class type to use for the array.
	 * @param __len The length of the array.
	 * @return The handle for the array data.
	 * @throws IllegalArgumentException If the class is not an array or the
	 * array length is negative.
	 * @throws IOException On read errors.
	 * @throws NullPointerException On null arguments.
	 * @since 2021/01/10
	 */
	public ChunkMemHandle prepareArray(ClassName __cl, int __len)
		throws IllegalArgumentException, IOException, NullPointerException
	{
		if (__cl == null)
			throw new NullPointerException("NARG");
		if (__len < 0)
			throw new IllegalArgumentException("NEGV " + __len);
		
		// {@squirreljme.error BC0f Class is not an array. (The class)}
		if (!__cl.isArray())
			throw new IllegalArgumentException("BC0f" + __cl);
		
		// We need to know about the class to work with it
		ClassState state = this.loadClass(__cl);
		
		// Determine the memory handle kind used
		int handleKind;
		PrimitiveType pType = __cl.componentType().primitiveType();
		if (pType == null)
			handleKind = MemHandleKind.OBJECT_ARRAY;
		else
			switch (pType)
			{
				case BOOLEAN:
				case BYTE:
					handleKind = MemHandleKind.BYTE_ARRAY;
					break;
				
				case CHARACTER:
					handleKind = MemHandleKind.CHARACTER_ARRAY;
					break;
					
				case DOUBLE:
					handleKind = MemHandleKind.DOUBLE_ARRAY;
					break;
					
				case FLOAT:
					handleKind = MemHandleKind.FLOAT_ARRAY;
					break;
					
				case INTEGER:
					handleKind = MemHandleKind.INTEGER_ARRAY;
					break;
					
				case LONG:
					handleKind = MemHandleKind.LONG_ARRAY;
					break;
					
				case SHORT:
					handleKind = MemHandleKind.SHORT_ARRAY;
					break;
					
				default:
					throw Debugging.oops();
			}
		
		// Calculate the base size for arrays
		int baseArraySize = this._baseArraySize;
		if (baseArraySize < 0)
		{
			baseArraySize = state._classInfoHandle
				.getInteger(ClassProperty.SIZE_ALLOCATION);
			this._baseArraySize = baseArraySize;
		}
		
		// Allocate memory needed to store the array handle, this includes
		// room for all of the elements accordingly
		ChunkMemHandle rv = this._memHandles.alloc(
			handleKind, baseArraySize +
				(__len * __cl.componentType().field().dataType().size()));
		
		// Store the array size as a hint
		rv._arraySize = __len;
		
		// Set array field information
		this.objectFieldSet(rv, BootState._OBJECT_CLASS_INFO,
			state._classInfoHandle);
		this.objectFieldSet(rv, BootState._ARRAY_LENGTH,
			__len);
		
		return rv;
	}
	
	/**
	 * Prepares memory that contains an instance for the given class.
	 * 
	 * @param __cl The class to load.
	 * @return The handle to the class data.
	 * @throws IOException On read errors.
	 * @throws IllegalArgumentException If an object is attempted to be
	 * allocated.
	 * @throws NullPointerException On null arguments.
	 * @since 2021/01/10
	 */
	public ChunkMemHandle prepareObject(ClassName __cl)
		throws IllegalArgumentException, IOException, NullPointerException
	{
		if (__cl == null)
			throw new NullPointerException("NARG");
		
		// {@squirreljme.error BC0i Cannot use this method to initialize a
		// primitive or array type. (The class name)}
		if (__cl.isPrimitive() || __cl.isArray())
			throw new IllegalArgumentException("BC0i " + __cl);
		
		// We need to know about the class to work with it
		ClassState state = this.loadClass(__cl);
		
		// Allocate memory needed to store the object handle
		ChunkMemHandle rv = this._memHandles.allocObject(
			state._classInfoHandle.getInteger(ClassProperty.SIZE_ALLOCATION));
		
		// Set object field information
		this.objectFieldSet(rv, BootState._OBJECT_CLASS_INFO,
			state._classInfoHandle);
		
		return rv;
	}
	
	/**
	 * Reads a minimized class file for the given class.
	 * 
	 * @param __class The class to boot.
	 * @return The read class data.
	 * @throws IOException On read errors.
	 * @throws NullPointerException On null arguments.
	 * @since 2020/12/16
	 */
	public MinimizedClassFile readClass(ClassName __class)
		throws IOException, NullPointerException
	{
		if (__class == null)
			throw new NullPointerException("NARG");
		
		// Did we already read this class?
		Map<ClassName, MinimizedClassFile> readClasses = this._readClasses;
		MinimizedClassFile rv = readClasses.get(__class);
		if (rv != null)
			return rv;
		
		// Arrays are special and are dynamically created accordingly
		if (__class.isArray() || __class.isPrimitive())
			rv = MinimizedClassFile.decode(Minimizer.minimize(
				ClassFile.special(__class.field())));
		
		// Load in class data
		else
			try (InputStream in = this._rawChunks.get(__class).currentStream())
			{
				rv = MinimizedClassFile.decode(in, this._pool);
				
				// Debug
				Debugging.debugNote("Read %s...", rv.thisName());
			}
		
		// Cache it and use it
		readClasses.put(__class, rv);
		return rv;
	}
	
	/**
	 * Loads the specified pool entry into memory and returns the handle.
	 *
	 * @param __clPool The class runtime pool;
	 * @param __entry The entry to load.
	 * @return The loaded pool value.
	 * @throws NullPointerException On null arguments.
	 * @since 2020/12/29
	 */
	private Object __loadPool(BasicPool __clPool, BasicPoolEntry __entry)
		throws NullPointerException
	{
		if (__clPool == null || __entry == null)
			throw new NullPointerException("NARG");
		
		// Needed pool information
		DualClassRuntimePool dualPool = this._pool;
		BasicPool staticDualPool = dualPool.classPool();
		ChunkSection poolChunk = this._poolChunk;
		
		// Determine where the pool is located
		int poolOff = poolChunk.futureAddress().get();
		
		// Debugging
		Debugging.debugNote("Load pool entry: %s", __entry);
		
		// Depends on the type used
		switch (__entry.type())
		{
				// A noted string for debugging purposes, this directly points
				// to the ROM for String data
			case NOTED_STRING:
				BasicPoolEntry poolStr =
					staticDualPool.byIndex(__entry.part(0));
				
				// These point to STRINGs that prefix with [hash$16 len$16]
				return new BootJarPointer(poolOff + poolStr.offset + 4);
		}
		
		if (false)
			throw Debugging.todo();
		Debugging.todoNote("Handle pool entry: %s", __entry);
		return 0;//this._memHandles.allocObject(0);
	}
}
