// -*- Mode: Java; indent-tabs-mode: t; tab-width: 4 -*-
// ---------------------------------------------------------------------------
// Multi-Phasic Applications: SquirrelJME
//     Copyright (C) Stephanie Gawroriski <xer@multiphasicapps.net>
// ---------------------------------------------------------------------------
// SquirrelJME is under the Mozilla Public License Version 2.0.
// See license.mkd for licensing and copyright information.
// ---------------------------------------------------------------------------

package cc.squirreljme.runtime.cldc.full;

import cc.squirreljme.runtime.cldc.debug.Debugging;
import java.lang.ref.Reference;
import java.lang.ref.WeakReference;
import java.nio.file.FileSystem;
import java.nio.file.InvalidPathException;
import java.nio.file.Path;

/**
 * Base class for implementations of {@link Path}.
 * 
 * An implementation of the class only needs to implement the core operations
 * of a path that everything else is able to use, this keeps the individual
 * implementations much simpler.
 *
 * @param <FS> The filesystem type.
 * @since 2023/08/20
 */
public abstract class AbstractPath<FS extends AbstractFileSystem>
	implements Path
{
	/** The owning filesystem. */
	protected final FS fileSystem; 
	
	/** The cached root. */
	private volatile Reference<Path> _root;
	
	/** Cached name lists. */
	private volatile Reference<Path>[] _names;
	
	/** Cached parent. */
	private volatile Reference<Path> _parent;
	
	/** Is the root known to be null? */
	private volatile boolean _isNullRoot;
	
	/** Parent is known to be null? */
	private volatile boolean _isNullParent;
	
	/** The cached name count. */
	private volatile int _nameCount =
		-1;
	
	/** The cached hash code of this path. */
	private volatile int _hashCode;
	
	/** The cached string representation. */
	private volatile Reference<String> _string;
	
	/**
	 * Initializes the base abstract path.
	 *
	 * @param __fileSystem The owning filesystem.
	 * @throws NullPointerException On null arguments.
	 * @since 2023/08/21
	 */
	protected AbstractPath(FS __fileSystem)
		throws NullPointerException
	{
		if (__fileSystem == null)
			throw new NullPointerException("NARG");
		
		this.fileSystem = __fileSystem;
	}
	
	/**
	 * Returns the name at the given index. 
	 *
	 * @param __dx The index to get the name from.
	 * @return The name at the index.
	 * @throws IllegalArgumentException If the index is not valid.
	 * @since 2023/08/20
	 */
	protected abstract Path getInternalName(int __dx)
		throws IllegalArgumentException;
	
	/**
	 * Returns the name count of the path.
	 *
	 * @return The path name count.
	 * @since 2023/08/20
	 */
	protected abstract int getInternalNameCount();
	
	/**
	 * Returns the root of this path.
	 *
	 * @return The root of this path, may be {@code null} if there is none.
	 * @since 2023/08/20
	 */
	protected abstract Path getInternalRoot();
	
	/**
	 * {@inheritDoc}
	 * @since 2023/08/21
	 */
	@Override
	public final int compareTo(Path __b)
	{
		/* {@squirreljme.error ZY06 The two paths belong to different
		file system implementations.} */
		FS fileSystem = this.fileSystem;
		if (fileSystem.getClass() != __b.getFileSystem().getClass())
			throw new ClassCastException("ZY06");
		
		// Compare roots first, no roots are before roots
		Path aRoot = this.getRoot();
		Path bRoot = __b.getRoot();
		if ((aRoot == null) != (bRoot == null))
			return (aRoot == null ? -1 : 1);
		else if (aRoot != null)
		{
			// Compare the root components
			int compare = fileSystem.compare(aRoot, bRoot);
			if (compare != 0)
				return compare;
		}
		
		// Get counts, determines which path is longer/shorter
		int aCount = this.getNameCount();
		int bCount = __b.getNameCount();
		
		// Count each group
		int mCount = Math.max(aCount, bCount);
		for (int i = 0; i < mCount; i++)
		{
			// Get both components
			Path aComp = this.getName(i);
			Path bComp = __b.getName(i);
			
			// Are these two components different?
			int compare = fileSystem.compare(aComp, bComp);
			if (compare != 0)
				return compare;
		}
		
		// The longer path
		return aCount - bCount;
	}
	
	/**
	 * {@inheritDoc}
	 * @since 2023/08/21
	 */
	@Override
	public boolean equals(Object __o)
	{
		if (this == __o)
			return true;
		else if (!(__o instanceof Path))
			return false;
		
		// Use standard comparison, because some filesystems might be
		// case insensitive to where "a" == "A" whereas others this might be
		// false instead of true
		return this.compareTo((Path)__o) == 0;
	}
	
	/**
	 * {@inheritDoc}
	 * @since 2023/08/20
	 */
	@Override
	public final Path getFileName()
	{
		int count = this.getNameCount();
		if (count == 0)
			return null;
		return this.getName(count - 1);
	}
	
	/**
	 * {@inheritDoc}
	 * @since 2023/08/21
	 */
	@Override
	public final FileSystem getFileSystem()
	{
		return this.fileSystem;
	}
	
	/**
	 * {@inheritDoc}
	 * @since 2023/08/20
	 */
	@SuppressWarnings("unchecked")
	@Override
	public final Path getName(int __dx)
		throws IllegalArgumentException
	{
		/* {@squirreljme.error ZY05 Path index is out of bounds.}. */
		int count = this.getNameCount();
		if (__dx < 0 || __dx >= count)
			throw new IllegalArgumentException("ZY05");
		
		// Need to create the name cache?
		Reference<Path>[] names = this._names;
		if (names == null)
		{
			names = (Reference<Path>[])new Reference[count];
			this._names = names;
		}
		
		Reference<Path> ref = names[__dx];
		Path result;
		
		// If not cached, cache it
		if (ref == null || (result = ref.get()) == null)
		{
			result = this.getInternalName(__dx);
			names[__dx] = new WeakReference<>(result);
		}
		
		// Use result
		return result;
	}
	
	/**
	 * {@inheritDoc}
	 * @since 2023/08/20
	 */
	@Override
	public final int getNameCount()
	{
		// Check for cached value
		int result = this._nameCount;
		if (result < 0)
		{
			result = this.getInternalNameCount();
			this._nameCount = result;
		}
		
		return result;
	}
	
	/**
	 * {@inheritDoc}
	 * @since 2023/08/20
	 */
	@Override
	public final Path getParent()
	{
		// Parent known to be null?
		if (this._isNullParent)
			return null;
		
		// Need to determine this path?
		Reference<Path> ref = this._parent;
		Path rv;
		if (ref == null || (rv = ref.get()) == null)
		{
			int count = this.getNameCount();
			
			// If this is an empty path, there is no parent
			if (count == 0)
				rv = null;
			
			// If this is just a single component left, then the parent is
			// the root component
			else if (count == 1)
				rv = this.getRoot();
			
			// Otherwise, we need to calculate it
			else
			{
				throw Debugging.todo();
			}
			
			// If there is no parent, set as such, otherwise cache it
			if (rv == null)
				this._isNullParent = true;
			else
				this._parent = new WeakReference<>(rv);
		}
		
		return rv;
	}
	
	/**
	 * {@inheritDoc}
	 * @since 2023/08/20
	 */
	@Override
	public final Path getRoot()
	{
		// Known to be null already?
		if (this._isNullRoot)
			return null;
		
		Reference<Path> ref = this._root;
		Path rv;
		
		// Determine the root of the path
		if (ref == null || (rv = ref.get()) == null)
		{
			rv = this.getInternalRoot();
			
			// Either flag it as null or cache it
			if (rv == null)
				this._isNullRoot = true;
			else
				this._root = new WeakReference<>(rv);
		}
		
		return rv;
	}
	
	/**
	 * {@inheritDoc}
	 * @since 2023/08/21
	 */
	@Override
	public int hashCode()
	{
		// Already determined the hash?
		int rv = this._hashCode;
		if (rv != 0)
			return rv;
		
		// Just use the string representation
		rv = this.toString().hashCode();
		
		// Cache and return it
		this._hashCode = rv;
		return rv;
	}
	
	/**
	 * {@inheritDoc}
	 * @since 2023/08/21
	 */
	@Override
	public final boolean isAbsolute()
	{
		// This is simple because if there is a root, then the path
		// is absolute
		return this.getRoot() != null;
	}
	
	/**
	 * {@inheritDoc}
	 * @since 2023/08/20
	 */
	@Override
	public final Path resolve(Path __target)
		throws InvalidPathException, NullPointerException
	{
		if (__target == null)
			throw new NullPointerException("NARG");
		
		// If the target is absolute, use that
		if (__target.isAbsolute())
			return __target;
		
		// If the target is an empty path, then use this one
		if (__target.getNameCount() == 0)
			return this;
		
		// Root component, which determines name resolution 
		Path thisRoot = this.getRoot();
		int thisCount = -1;
		
		throw Debugging.todo();
	}
	
	/**
	 * {@inheritDoc}
	 * @since 2023/08/21
	 */
	@Override
	public final Path resolve(String __target)
		throws InvalidPathException, NullPointerException
	{
		if (__target == null)
			throw new NullPointerException("NARG");
		
		return this.resolve(this.fileSystem.getPath(__target));
	}
	
	/**
	 * {@inheritDoc}
	 * @since 2023/08/21
	 */
	@Override
	public final String toString()
	{
		Reference<String> ref = this._string;
		String rv;
		
		if (ref == null || (rv = ref.get()) == null)
		{
			throw Debugging.todo();
		}
		
		return rv;
	}
}
