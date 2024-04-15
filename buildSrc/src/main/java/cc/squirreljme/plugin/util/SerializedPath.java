// -*- Mode: Java; indent-tabs-mode: t; tab-width: 4 -*-
// ---------------------------------------------------------------------------
// Multi-Phasic Applications: SquirrelJME
//     Copyright (C) Stephanie Gawroriski <xer@multiphasicapps.net>
// ---------------------------------------------------------------------------
// SquirrelJME is under the Mozilla Public License Version 2.0.
// See license.mkd for licensing and copyright information.
// ---------------------------------------------------------------------------

package cc.squirreljme.plugin.util;

import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.io.Serializable;
import java.lang.reflect.Field;
import java.nio.file.Path;
import java.nio.file.Paths;

/**
 * A serialized path for use with Gradle.
 *
 * @since 2022/09/11
 */
@SuppressWarnings("UseOfClone")
public final class SerializedPath
	implements Comparable<SerializedPath>, Cloneable, Serializable
{
	/** Serialization ID. */
	private static final long serialVersionUID =
		12893674786663984L;
	
	/** The represented path. */
	public final Path path;
	
	/**
	 * Initializes the serialized path.
	 * 
	 * @param __path The path to use.
	 * @throws NullPointerException On null arguments.
	 * @since 2022/09/11
	 */
	public SerializedPath(Path __path)
		throws NullPointerException
	{
		if (__path == null)
			throw new NullPointerException("NARG");
		
		this.path = __path;
	}
	
	/**
	 * {@inheritDoc}
	 * @since 2022/09/11
	 */
	@Override
	protected SerializedPath clone()
		throws CloneNotSupportedException
	{
		return new SerializedPath(this.path);
	}
	
	/**
	 * {@inheritDoc}
	 * @since 2022/09/11
	 */
	@Override
	public int compareTo(SerializedPath __o)
	{
		return this.path.compareTo(__o.path);
	}
	
	/**
	 * {@inheritDoc}
	 * @since 2022/09/11
	 */
	@Override
	public int hashCode()
	{
		return this.path.hashCode();
	}
	
	/**
	 * {@inheritDoc}
	 * @since 2022/09/11
	 */
	@Override
	public boolean equals(Object __o)
	{
		if (this == __o)
			return true;
		if (!(__o instanceof SerializedPath))
			return false;
		
		return this.path.equals(((SerializedPath)__o).path);
	}
	
	/**
	 * Reads the serialization object.
	 *
	 * @param __in The input object.
	 * @throws NoSuchFieldException If the field does not exist.
	 * @throws IOException On read errors.
	 * @throws ClassNotFoundException If our class is not valid?
	 * @throws IllegalAccessException If we cannot write a final field.
	 * @since 2022/09/11
	 */
	private void readObject(ObjectInputStream __in)
		throws NoSuchFieldException, IOException, ClassNotFoundException,
		IllegalAccessException
	{
		Field path = this.getClass().getDeclaredField("path");
		path.setAccessible(true);
		path.set(this, Paths.get(__in.readObject().toString()));
	}
	
	/**
	 * {@inheritDoc}
	 * @since 2022/09/11
	 */
	@Override
	public String toString()
	{
		return this.path.toString();
	}
	
	/**
	 * Serializes the path.
	 *
	 * @param __out The stream to write to.
	 * @throws IOException On write errors.
	 * @since 2022/09/11
	 */
	private void writeObject(ObjectOutputStream __out)
		throws IOException
	{
		__out.writeObject(this.path.toString());
	}
	
	/**
	 * Boxes all the paths.
	 *
	 * @param __paths The paths to box.
	 * @return The boxed paths.
	 * @since 2022/09/11
	 */
	public static SerializedPath[] boxPaths(Path... __paths)
		throws NullPointerException
	{
		if (__paths == null)
			throw new NullPointerException("NARG");
		
		// Process each one
		int count = __paths.length;
		SerializedPath[] result = new SerializedPath[count];
		for (int i = 0; i < count; i++)
			result[i] = new SerializedPath(__paths[i]);
		
		return result;
	}
	
	/**
	 * Unboxes all the paths.
	 * 
	 * @param __paths The paths to unbox.
	 * @return The unboxed paths.
	 * @since 2022/09/11
	 */
	public static Path[] unboxPaths(SerializedPath... __paths)
		throws NullPointerException
	{
		if (__paths == null)
			throw new NullPointerException("NARG");
		
		// Process each one
		int count = __paths.length;
		Path[] result = new Path[count];
		for (int i = 0; i < count; i++)
			result[i] = __paths[i].path;
		
		return result;
	}
}
