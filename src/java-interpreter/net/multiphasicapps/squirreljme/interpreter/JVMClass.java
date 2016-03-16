// -*- Mode: Java; indent-tabs-mode: t; tab-width: 4 -*-
// ---------------------------------------------------------------------------
// Multi-Phasic Applications: SquirrelJME
//     Copyright (C) 2013-2016 Steven Gawroriski <steven@multiphasicapps.net>
//     Copyright (C) 2013-2016 Multi-Phasic Applications <multiphasicapps.net>
// ---------------------------------------------------------------------------
// SquirrelJME is under the GNU Affero General Public License v3+, or later.
// For more information see license.mkd.
// ---------------------------------------------------------------------------

package net.multiphasicapps.squirreljme.interpreter;

import java.io.DataInputStream;
import java.io.InputStream;
import java.io.IOException;
import java.nio.channels.SeekableByteChannel;
import java.util.HashSet;
import java.util.LinkedHashSet;
import java.util.List;
import java.util.Set;
import net.multiphasicapps.collections.MissingCollections;

/**
 * This represents a single class loaded by the interpreter.
 *
 * @since 2016/03/01
 */
public class JVMClass
{
	/** The class file magic number. */
	public static final int MAGIC_NUMBER =
		0xCAFEBABE;
	
	/** Is invokedynamic supported anyway even thougn it is illegal? */
	static final boolean SUPPORT_INVOKEDYNAMIC_ANYWAY =
		Boolean.valueOf(System.getProperty(
			JVMClass.class.getName() + ".invokedynamic"));
	
	/** The interpreter engine which owns this class. */
	protected final JVMEngine engine;
	
	/** Super classes and interfaces implemented in this class. */
	protected final Set<JVMClass> superclasses;
	
	/** The version of this class file. */
	protected final JVMClassVersion version;
	
	/** The class constant pool. */
	protected final JVMConstantPool constantpool;
	
	/** Class access flags. */
	protected final Set<JVMClassFlag> flags;
	
	/**
	 * Initializes the class data.
	 *
	 * @param __owner The owning interpreter engine.
	 * @param __cdata Class data for parsing.
	 * @throws JVMClassFormatError If the input class data is not
	 * correct.
	 * @throws IOException On read errors.
	 * @throws NullPointerException On null arguments.
	 * @since 2016/03/01
	 */
	JVMClass(JVMEngine __owner, InputStream __cdata)
		throws JVMClassFormatError, IOException, NullPointerException
	{
		// Check
		if (__owner == null || __cdata == null)
			throw new NullPointerException();
		
		// Set
		engine = __owner;
		
		// Read the input class data
		DataInputStream das = new DataInputStream(__cdata);
		
		// Check the magic number
		int clmagic;
		if (MAGIC_NUMBER != (clmagic = das.readInt()))
			throw new JVMClassFormatError(String.format("Expected " +
				"magic number %08x, not %08x", MAGIC_NUMBER, clmagic));
		
		// Read the class version number, this modifies if certain
		// instructions are handled and how they are verified (StackMap vs
		// StackMapTable)
		version = JVMClassVersion.findVersion(
			das.readUnsignedShort() | (das.readUnsignedShort() << 16));
		if (version.compareTo(JVMClassVersion.MAX_VERSION) > 0)
			throw new JVMClassVersionError(String.format("Class " +
				"version " + version + " is newer than " +
				JVMClassVersion.MAX_VERSION + "."));
		
		// Initialize the constant pool
		constantpool = new JVMConstantPool(this, das);
		
		// Read flags
		flags = JVMClass.<JVMClassFlag>__parseFlags(JVMClassFlag.FLAGS,
			das.readUnsignedShort());
		
		// Interface?
		if (isInterface())
		{
			// Must be abstract, cannot have some flags set
			if ((!isAbstract()) ||
				(isFinal() || isSpecialInvokeSpecial() || isEnum()))
				throw new JVMClassFormatError(flags.toString());
		}
		
		// Normal class
		else
		{
			// Cannot be an annotation
			// Cannot be abstract and final
			if (isAnnotation() ||
				(isAbstract() && isFinal()))
				throw new JVMClassFormatError(flags.toString());
		}
		
		throw new Error("TODO");
	}
	
	/**
	 * Locates a method in the class by the given identifier name and method
	 * descriptor.
	 *
	 * @param __name The name of the method.
	 * @param __desc The method descriptor.
	 * @return The method matching the name and descriptor.
	 * @throws NullPointerException On null arguments.
	 * @since 2016/03/01
	 */
	public JVMMethod getMethod(String __name, String __desc)
		throws NullPointerException
	{
		// Check
		if (__name == null || __desc == null)
			throw new NullPointerException();
		
		throw new Error("TODO");
	}
	
	/**
	 * Returns the name of this class as returned by {@link Class#getName()}.
	 *
	 * @return The name of this class.
	 * @since 2016/03/02
	 */
	public String getName()
	{
		throw new Error("TODO");
	}
	
	/**
	 * Is a specific flag set?
	 *
	 * @param __jcf The flag to check if it is set.
	 * @return {@code true} if the flag is set or {@code false} if it is not.
	 * @throws NullPointerException On null arguments.
	 */
	public boolean hasFlag(JVMClassFlag __jcf)
	{
		// Check
		if (__jcf == null)
			throw new NullPointerException();
		
		return flags.contains(__jcf);
	}
	
	/**
	 * Is this class abstract?
	 *
	 * @return {@code true} if it is abstract.
	 * @since 2016/03/15
	 */
	public boolean isAbstract()
	{
		return flags.contains(JVMClassFlag.ABSTRACT);
	}
	
	/**
	 * Is this an annotation?
	 *
	 * @return {@code true} if it is an annotation.
	 * @since 2016/03/15
	 */
	public boolean isAnnotation()
	{
		return flags.contains(JVMClassFlag.ANNOTATION);
	}
	
	/**
	 * Is this an enumeration?
	 *
	 * @return {@code true} if it is an enumeration.
	 * @since 2016/03/15
	 */
	public boolean isEnum()
	{
		return flags.contains(JVMClassFlag.ENUM);
	}
	
	/**
	 * Is this class final?
	 *
	 * @return {@code true} if it is final.
	 * @since 2016/03/15
	 */
	public boolean isFinal()
	{
		return flags.contains(JVMClassFlag.FINAL);
	}
	
	/**
	 * Is this an interface?
	 *
	 * @return {@code true} if an interface.
	 * @since 2016/03/15
	 */
	public boolean isInterface()
	{
		return flags.contains(JVMClassFlag.INTERFACE);
	}
	
	/**
	 * Is this class package private?
	 *
	 * @return {@code true} if it is package private.
	 * @since 2016/03/15
	 */
	public boolean isPackagePrivate()
	{
		return !isPublic();
	}
	
	/**
	 * Is this class public?
	 *
	 * @return {@code true} if it is public.
	 * @since 2016/03/15
	 */
	public boolean isPublic()
	{
		return flags.contains(JVMClassFlag.PUBLIC);
	}
	
	/**
	 * Is there special handling for super-class method calls?
	 *
	 * @return {@code true} if the super-class invocation special flag is set.
	 * @since 2016/03/15
	 */
	public boolean isSpecialInvokeSpecial()
	{
		return flags.contains(JVMClassFlag.SUPER);
	}
	
	/**
	 * Returns the version of the class file which determines if specific
	 * features are supported or not.
	 *
	 * @return The class version number.
	 * @since 2016/03/13
	 */
	public JVMClassVersion version()
	{
		return version;
	}
	
	/**
	 * Parses input flags and returns an unmodifiable set view of them.
	 *
	 * @param <B> The flag type.
	 * @param __ll The input flags which are available.
	 * @param __in The input flag value.
	 * @return The set of flags, it is not modifiable.
	 * @throws JVMClassFormatError If illegal flags are specified.
	 * @throws NullPointerException On null arguments.
	 * @since 2016/03/15
	 */
	static <B extends JVMBitFlag> Set<B> __parseFlags(List<B> __ll, int __in)
		throws JVMClassFormatError, NullPointerException
	{
		// Check
		if (__ll == null)
			throw new NullPointerException();
		
		// Target set
		Set<B> rv = new HashSet<>();
		
		// Parse flags
		int rem = __in;
		for (B b : __ll)
		{
			// Get the mask
			int mm = b.mask();
			
			// If it is set, clear it and add it
			if (0 != (rem & mm))
			{
				// Set
				rv.add(b);
				
				// Clear
				rem &= ~mm;
			}
		}
		
		// If non-zero then extra illegal flags remain
		if (rem != 0)
			throw new JVMClassFormatError("Extra flags " +
				Integer.toHexString(rem) + ".");
		
		// Lock it in
		return MissingCollections.<B>unmodifiableSet(rv);
	}
}

