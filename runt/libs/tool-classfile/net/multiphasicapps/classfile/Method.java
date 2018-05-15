// -*- Mode: Java; indent-tabs-mode: t; tab-width: 4 -*-
// ---------------------------------------------------------------------------
// Multi-Phasic Applications: SquirrelJME
//     Copyright (C) Stephanie Gawroriski <xer@multiphasicapps.net>
//     Copyright (C) Multi-Phasic Applications <multiphasicapps.net>
// ---------------------------------------------------------------------------
// SquirrelJME is under the GNU General Public License v3+, or later.
// See license.mkd for licensing and copyright information.
// ---------------------------------------------------------------------------

package net.multiphasicapps.classfile;

import java.io.DataInputStream;
import java.io.IOException;
import java.lang.ref.Reference;
import java.lang.ref.WeakReference;
import java.util.HashSet;
import java.util.LinkedHashSet;
import java.util.List;
import java.util.Set;
import net.multiphasicapps.collections.UnmodifiableArrayList;

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
	
	/** The code attribute data, which is optional. */
	private final byte[] _rawcodeattr;
	
	/** Annotated values. */
	private final AnnotationElement[] _annotations;
	
	/** The method byte code. */
	private volatile Reference<ByteCode> _bytecode;
	
	/** Name and type reference. */
	private volatile Reference<MethodNameAndType> _nameandtype;
	
	/** The method index. */
	private volatile Reference<MethodHandle> _index;
	
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
	 * @param __avs Annotated method values.
	 * @throws NullPointerException On null arguments except for {@code __mc}.
	 * @since 2017/09/30
	 */
	Method(ClassVersion __ver, ClassFlags __cf, ClassName __tn, Pool __pool,
		MethodFlags __mf, MethodName __mn, MethodDescriptor __mt, byte[] __mc,
		AnnotationElement[] __avs)
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
		this._rawcodeattr = __mc;
		this._annotations = __avs;
	}
	
	/**
	 * {@inheritDoc}
	 * @since 2018/03/06
	 */
	@Override
	public final AnnotationTable annotationTable()
	{
		throw new todo.TODO();
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
		// If there is no code atribute there is no byte code
		byte[] rawcodeattr = this._rawcodeattr;
		if (rawcodeattr == null)
			return null;
		
		// Otherwise load a representation of it
		Reference<ByteCode> ref = this._bytecode;
		ByteCode rv;
		
		if (ref == null || null == (rv = ref.get()))
			this._bytecode = new WeakReference<>((rv = new ByteCode(
				new WeakReference<>(this), this._rawcodeattr)));
		
		return rv;
	}
	
	/**
	 * Returns the flags for this method.
	 *
	 * @return The method flags.
	 * @since 2017/10/11
	 */
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
			this._index = new WeakReference<>(rv = new MethodHandle(
				this.classname, this.methodname, this.methodtype));
		
		return rv;
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
	 * Returns the name and type of the method.
	 *
	 * @return The method name and type.
	 * @since 2017/10/10
	 */
	public final MethodNameAndType nameAndType()
	{
		Reference<MethodNameAndType> ref = this._nameandtype;
		MethodNameAndType rv;
		
		if (ref == null || null == (rv = ref.get()))
			this._nameandtype = new WeakReference<>(
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
		
		// Parse fields
		for (int i = 0; i < nm; i++)
		{
			MethodFlags flags = new MethodFlags(__cf,
				__in.readUnsignedShort());
			MethodName name = new MethodName(
				__pool.<UTFConstantEntry>require(UTFConstantEntry.class,
				__in.readUnsignedShort()).toString());
			MethodDescriptor type = new MethodDescriptor(
				__pool.<UTFConstantEntry>require(UTFConstantEntry.class,
				__in.readUnsignedShort()).toString());
			
			// {@squirreljme.error JC12 A duplicate method exists within the
			// class. (The method name; The method descriptor)}
			if (!dup.add(new NameAndType(name.toString(), type.toString())))
				throw new InvalidClassFormatException(String.format(
					"JC12 %s %s", name, type));
			
			// Annotated values
			Set<AnnotationElement> avs = new LinkedHashSet<>();
			
			// Handle attributes
			int na = __in.readUnsignedShort();
			String[] attr = new String[1];
			int[] alen = new int[1];
			byte[] code = null;
			for (int j = 0; j < na; j++)
				try (DataInputStream ai = ClassFile.__nextAttribute(__in,
					__pool, attr, alen))
				{
					// Parse annotations?
					if (true)
						throw new todo.TODO();
					
					// Only care about the code attribute
					if (!"Code".equals(attr[0]))
						continue;
			
					// {@squirreljme.error JC13 The specified method
					// contains more than one code attribute. (The current
					// class; The method name; The method type)}
					if (code != null)
						throw new InvalidClassFormatException(String.format(
							"JC13 %s %s %s", __tn, name, type));
					
					// Copy bytes
					int rlen = alen[0];
					code = new byte[rlen];
					ai.readFully(code, 0, rlen);
				}
		
			// {@squirreljme.error JC14 The specified method does not have
			// the correct matching for abstract and if code exists or not.
			// (The current
			// class; The method name; The method type; The method flags)}
			if ((code == null) != (flags.isAbstract() | flags.isNative()))
				throw new InvalidClassFormatException(String.format(
					"JC14 %s %s %s", __tn, name, type, flags));
			
			// Create
			rv[i] = new Method(__ver, __cf, __tn, __pool, flags, name, type,
				code,
				avs.<AnnotationElement>toArray(new AnnotationElement[avs.size()]));
		}
		
		// All done!
		return rv;
	}
}

