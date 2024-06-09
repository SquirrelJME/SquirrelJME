// -*- Mode: Java; indent-tabs-mode: t; tab-width: 4 -*-
// ---------------------------------------------------------------------------
// SquirrelJME
//     Copyright (C) Stephanie Gawroriski <xer@multiphasicapps.net>
// ---------------------------------------------------------------------------
// SquirrelJME is under the Mozilla Public License Version 2.0.
// See license.mkd for licensing and copyright information.
// ---------------------------------------------------------------------------

package net.multiphasicapps.classfile;

import java.io.DataInputStream;
import java.io.IOException;
import java.lang.ref.Reference;
import java.lang.ref.SoftReference;
import java.lang.ref.WeakReference;
import java.util.HashSet;
import java.util.Set;

/**
 * This represents a method which is used to execute byte code.
 *
 * @since 2017/09/30
 */
public final class Method
	extends Member
	implements Annotated
{
	/** The version of the class. */
	protected final ClassVersion version;
	
	/** The flags for class. */
	protected final ClassFlags classflags;
	
	/** The name of the current class. */
	protected final ClassName classname;
	
	/** The constant pool. */
	protected final Pool pool;
	
	/** The flags for the current method. */
	protected final MethodFlags methodflags;
	
	/** The name of the method. */
	protected final MethodName methodname;
	
	/** The type of the method. */
	protected final MethodDescriptor methodtype;
	
	/** Does this method have code? */
	protected final boolean hascode;
	
	/** The method index. */
	protected final int methodIndex;
	
	/** Annotated values. */
	protected final AnnotationTable annotations;
	
	/** The code attribute data, which is optional. */
	private final byte[] _rawcodeattr;
	
	/** The method byte code. */
	private Reference<ByteCode> _bytecode;
	
	/** Name and type reference. */
	private Reference<MethodNameAndType> _nameandtype;
	
	/** The method index. */
	private Reference<MethodHandle> _index;
	
	/**
	 * Initializes the method.
	 *
	 * @param __ver The class version.
	 * @param __cf The class flags.
	 * @param __tn The name of the class.
	 * @param __pool The constant pool.
	 * @param __mf The method flags.
	 * @param __mn The name of the method.
	 * @param __mt The method type.
	 * @param __mc An optional byte array representing the code attribute, the
	 * value is used directly.
	 * @param __avs Annotations associated with this method.
	 * @param __index The method index.
	 * @throws NullPointerException On null arguments except for {@code __mc}.
	 * @since 2017/09/30
	 */
	Method(ClassVersion __ver, ClassFlags __cf, ClassName __tn, Pool __pool,
		MethodFlags __mf, MethodName __mn, MethodDescriptor __mt, byte[] __mc,
		AnnotationTable __avs, int __index)
		throws NullPointerException
	{
		if (__ver == null || __cf == null || __tn == null || __pool == null ||
			__mf == null || __mn == null || __mt == null || __avs == null)
			throw new NullPointerException("NARG");
		
		// Set
		this.version = __ver;
		this.classflags = __cf;
		this.classname = __tn;
		this.pool = __pool;
		this.methodflags = __mf;
		this.methodname = __mn;
		this.methodtype = __mt;
		this.annotations = __avs;
		this._rawcodeattr = __mc;
		this.hascode = !__mf.isNative() && !__mf.isAbstract();
		this.methodIndex = __index;
	}
	
	/**
	 * {@inheritDoc}
	 * @since 2018/03/06
	 */
	@Override
	public final AnnotationTable annotationTable()
	{
		return this.annotations;
	}
	
	/**
	 * Returns the number of slots used for the method call.
	 * 
	 * @return The argument slot count for this method, handles static or
	 * instance methods.
	 * @since 2023/05/31
	 */
	public int argumentSlotCount()
	{
		return (this.methodflags.isStatic() ? 0 : 1) +
			this.methodtype.argumentSlotCount();
	}
	
	/**
	 * Returns the byte code for this method.
	 *
	 * @return The byte code for this method or {@code null} if there is none.
	 * @throws InvalidClassFormatException If the byte code is not valid.
	 * @since 2017/10/09
	 */
	public final ByteCode byteCode()
		throws InvalidClassFormatException
	{
		// If there is no code attribute there is no byte code
		byte[] rawcodeattr = this._rawcodeattr;
		if (!this.hascode)
			return null;
		
		// Otherwise, load a representation of it
		Reference<ByteCode> ref = this._bytecode;
		ByteCode rv;
		
		if (ref == null || null == (rv = ref.get()))
			this._bytecode = new SoftReference<>((rv = new ByteCode(
				new WeakReference<>(this), this._rawcodeattr,
				this.classname, this.methodflags)));
		
		return rv;
	}
	
	/**
	 * {@inheritDoc}
	 * @since 2017/10/11
	 */
	@Override
	public final MethodFlags flags()
	{
		return this.methodflags;
	}
	
	/**
	 * Returns the index of the method.
	 *
	 * @return The method index.
	 * @since 2017/10/14
	 */
	public final MethodHandle handle()
	{
		Reference<MethodHandle> ref = this._index;
		MethodHandle rv;
		
		if (ref == null || null == (rv = ref.get()))
			this._index = new SoftReference<>(rv = new MethodHandle(
				this.classname, this.methodname, this.methodtype));
		
		return rv;
	}
	
	/**
	 * Returns the class this method is in.
	 * 
	 * @return The class this method is in.
	 * @since 2023/05/28
	 */
	public ClassName inClass()
	{
		return this.classname;
	}
	
	/**
	 * Returns if this method is effectively final, meaning that it cannot be
	 * replaced.
	 *
	 * @return If this method is effectively final or not.
	 * @since 2017/10/16
	 */
	public boolean isEffectivelyFinal()
	{
		return this.methodflags.isFinal() || this.classflags.isFinal();
	}
	
	/**
	 * Returns whether this is a constructor or not.
	 *
	 * @return Whether this is a constructor or not.
	 * @since 2018/09/03
	 */
	public final boolean isInstanceInitializer()
	{
		return this.methodname.isInstanceInitializer();
	}
	
	/**
	 * Returns whether this is a static initializer or not.
	 *
	 * @return Whether this is a static initializer or not.
	 * @since 2018/09/03
	 */
	public final boolean isStaticInitializer()
	{
		return this.methodname.isStaticInitializer();
	}
	
	/**
	 * The method index.
	 * 
	 * @return The method index.
	 * @since 2021/07/06
	 */
	public final int methodIndex()
	{
		return this.methodIndex;
	}
	
	/**
	 * Returns the name of the method.
	 *
	 * @return The method name.
	 * @since 2017/10/12
	 */
	public final MethodName name()
	{
		return this.methodname;
	}
	
	/**
	 * {@inheritDoc}
	 * @since 2017/10/10
	 */
	@Override
	public final MethodNameAndType nameAndType()
	{
		Reference<MethodNameAndType> ref = this._nameandtype;
		MethodNameAndType rv;
		
		if (ref == null || null == (rv = ref.get()))
			this._nameandtype = new SoftReference<>(
				rv = new MethodNameAndType(this.methodname, this.methodtype));
		
		return rv;
	}
	
	/**
	 * Returns the constant pool this method uses.
	 *
	 * @return The constant pool which is used.
	 * @since 2017/10/09
	 */
	public final Pool pool()
	{
		return this.pool;
	}
	
	/**
	 * Returns the method's type.
	 *
	 * @return The type of the method.
	 * @since 2017/10/16
	 */
	public final MethodDescriptor type()
	{
		return this.methodtype;
	}
	
	/**
	 * Decodes all methods from the input class data.
	 *
	 * @param __ver The version of the class.
	 * @param __tn The name of the owning class.
	 * @param __cf The flags for the owning class.
	 * @param __pool The constant pool for the class.
	 * @throws InvalidClassFormatException If the class is not formatted
	 * correctly.
	 * @throws IOException On read errors.
	 * @throws NullPointerException On null arguments.
	 * @since 2017/09/30
	 */
	public static Method[] decode(ClassVersion __ver, ClassName __tn,
		ClassFlags __cf, Pool __pool, DataInputStream __in)
		throws InvalidClassFormatException, IOException, NullPointerException
	{
		if (__ver == null || __tn == null || __cf == null || __pool == null ||
			__in == null)
			throw new NullPointerException("NARG");
		
		int nm = __in.readUnsignedShort();
		Method[] rv = new Method[nm];
		Set<NameAndType> dup = new HashSet<>();
		
		// Parse methods
		for (int i = 0; i < nm; i++)
		{
			// Read the flags but do not initialize them yet
			int rawflags = __in.readUnsignedShort();
			
			// Parse name, this is needed to see if it is a constructor
			MethodName name;
			int nameDx = __in.readUnsignedShort();
			try
			{
				name = new MethodName(
					__pool.<UTFConstantEntry>require(UTFConstantEntry.class,
						nameDx).toString());
			}
			catch (InvalidClassFormatException __e)
			{
				/* {@squirreljme.error JC5a. */
				throw new InvalidClassFormatException(String.format(
					"JC5a %d %d %s", nm, nameDx,
					__pool.get(Object.class, nameDx)), __e);
			}
			
			// Initialize the flags now that we know the class name, this way
			// we can determine if this is a constructor or not
			MethodFlags flags = new MethodFlags(__cf, name, rawflags);
			
			// Continue reading the type
			MethodDescriptor type = new MethodDescriptor(
				__pool.<UTFConstantEntry>require(UTFConstantEntry.class,
				__in.readUnsignedShort()).toString());
			
			/* {@squirreljme.error JC3f A duplicate method exists within the
			class. (The method name; The method descriptor)} */
			if (!dup.add(new NameAndType(name.toString(), type.toString())))
				throw new InvalidClassFormatException(String.format(
					"JC3f %s %s", name, type));
			
			// Handle attributes
			AttributeTable attrs = AttributeTable.parse(__pool, __in);
			
			// Parse annotations
			AnnotationTable annotations = AnnotationTable.parse(__pool, attrs);
			
			// Get copy of the code attribute
			Attribute maybecode = attrs.get("Code");
			byte[] code = (maybecode == null ? null : maybecode.bytes());
			
			/* {@squirreljme.error JC3g The specified method does not have
			the correct matching for abstract and if code exists or not.
			(The current
			class; The method name; The method type; The method flags)} */
			if ((code == null) != (flags.isAbstract() | flags.isNative()))
				throw new InvalidClassFormatException(String.format(
					"JC3g %s %s %s %s", __tn, name, type, flags));
			
			// Create
			rv[i] = new Method(__ver, __cf, __tn, __pool, flags, name, type,
				code, annotations, i);
		}
		
		// All done!
		return rv;
	}
}

