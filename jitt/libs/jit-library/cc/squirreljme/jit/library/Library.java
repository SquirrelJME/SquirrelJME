// -*- Mode: Java; indent-tabs-mode: t; tab-width: 4 -*-
// ---------------------------------------------------------------------------
// Multi-Phasic Applications: SquirrelJME
//     Copyright (C) Stephanie Gawroriski <xer@multiphasicapps.net>
//     Copyright (C) Multi-Phasic Applications <multiphasicapps.net>
// ---------------------------------------------------------------------------
// SquirrelJME is under the GNU General Public License v3+, or later.
// See license.mkd for licensing and copyright information.
// ---------------------------------------------------------------------------

package cc.squirreljme.jit.library;

import cc.squirreljme.jit.classfile.ClassFile;
import java.io.InputStream;
import java.io.IOException;
import java.lang.ref.Reference;
import java.lang.ref.WeakReference;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;
import java.util.NoSuchElementException;

/**
 * This class represents a library within the JIT which is used to provide
 * access to class files and resources.
 *
 * @since 2018/02/21
 */
public abstract class Library
{
	/** The name of this library. */
	protected final String name;
	
	/** Iterable over classes. */
	private volatile Reference<Iterable<ClassFile>> _classesiterable;
	
	/** Class file cache. */
	private volatile Map<String, Reference<ClassFile>> _classcache =
		new HashMap<>();
	
	/**
	 * Initializes the base library.
	 *
	 * @param __name The name of this library.
	 * @throws NullPointerException On null arguments.
	 * @since 2018/02/24
	 */
	public Library(String __name)
		throws NullPointerException
	{
		if (__name == null)
			throw new NullPointerException("NARG");
		
		this.name = __name;
	}
	
	/**
	 * Iterates over the entries available within this library.
	 *
	 * @return The entries which are available for usage.
	 * @since 2018/02/23
	 */
	public abstract Iterable<String> entries();
	
	/**
	 * Opens the entry with the specified name.
	 *
	 * @param __name The entry to open.
	 * @return The input stream to the entry data or {@code null} if no
	 * such entry exists.
	 * @throws NullPointerException On null arguments.
	 * @since 2018/02/23
	 */
	public abstract InputStream open(String __name)
		throws NullPointerException;
	
	/**
	 * Iterates through the classes which are available in this library.
	 *
	 * @return The input classes available in this library.
	 * @since 2018/02/24
	 */
	public final Iterable<ClassFile> classes()
	{
		Reference<Iterable<ClassFile>> ref = this._classesiterable;
		Iterable<ClassFile> rv;
		
		if (ref == null || null == (rv = ref.get()))
			this._classesiterable = new WeakReference<>((rv =
				new __ClassesIterable__(this.entries())));
		
		return rv;
	}
	
	/**
	 * Loads the class by the specified name.
	 *
	 * @param __name The name of the class to load.
	 * @return The class file for the given class.
	 * @throws NoSuchClassException If the specified class does not exist.
	 * @throws NullPointerException On null arguments.
	 * @since 2018/02/23
	 */
	public final ClassFile loadClass(String __name)
		throws NoSuchClassException, NullPointerException
	{
		if (__name == null)
			throw new NullPointerException("NARG");
		
		Map<String, Reference<ClassFile>> classcache = this._classcache;
		synchronized (classcache)
		{
			Reference<ClassFile> ref = classcache.get(__name);
			ClassFile rv;
			
			if (ref == null || null == (rv = ref.get()))
			{
				try (InputStream is = this.open(__name + ".class"))
				{
					// {@squirreljme.error BP02 No such class exists. (The
					// name of the requested class)}.
					if (is == null)
						throw new NoSuchClassException(String.format(
							"BP02 %s", __name));
					
					rv = ClassFile.decode(is);
				}
				
				// {@squirreljme.error BP01 Could not load the class data.}
				catch (IOException e)
				{
					throw new RuntimeException("BP01", e);
				}
				
				classcache.put(__name, new WeakReference<>(rv));
			}
			
			return rv;
		}
	}
	
	/**
	 * This provides an iterable over class files.
	 *
	 * @return The iterable over class files.
	 * @since 2018/02/24
	 */
	private final class __ClassesIterable__
		implements Iterable<ClassFile>
	{
		/** The source iterator. */
		protected final Iterable<String> entries;
		
		/**
		 * Initializes the iterable classes.
		 *
		 * @param __entries The entries to source from.
		 * @throws NullPointerException On null arguments.
		 * @since 2018/02/24
		 */
		private __ClassesIterable__(Iterable<String> __entries)
			throws NullPointerException
		{
			if (__entries == null)
				throw new NullPointerException("NARG");
			
			this.entries = __entries;
		}
		
		/**
		 * {@inheritDoc}
		 * @since 2018/02/24
		 */
		@Override
		public final Iterator<ClassFile> iterator()
		{
			return new __ClassesIterator__(this.entries.iterator());
		}
	}
	
	/**
	 * This iterates over class files which exist within the library.
	 *
	 * @since 2018/02/24
	 */
	private final class __ClassesIterator__
		implements Iterator<ClassFile>
	{
		/** The iterator of class names to source from. */
		protected final Iterator<String> entries;
		
		/** The next entry to consider. */
		private volatile String _next;
		
		/**
		 * Initializes the classes iterator.
		 *
		 * @param __entries The entries to iterate over.
		 * @throws NullPointerException On null arguments.
		 * @since 2018/02/24
		 */
		private __ClassesIterator__(Iterator<String> __entries)
			throws NullPointerException
		{
			if (__entries == null)
				throw new NullPointerException("NARG");
			
			this.entries = __entries;
		}
		
		/**
		 * {@inheritDoc}
		 * @since 2018/02/24
		 */
		@Override
		public final boolean hasNext()
		{
			// Pre-cached
			if (this._next != null)
				return true;
			
			Iterator<String> entries = this.entries;
			
			// Only consider classes
			for (;;)
			{
				String next;
				try
				{
					next = entries.next();
				}
				catch (NoSuchElementException e)
				{
					return false;
				}
				
				// Ignore non-classes
				if (!next.endsWith(".class"))
					continue;
				
				this._next = next.substring(0, next.length() - 6);
				return true;
			}
		}
		
		/**
		 * {@inheritDoc}
		 * @since 2018/02/24
		 */
		@Override
		public final ClassFile next()
		{
			if (!this.hasNext())
				throw new NoSuchElementException("NSEE");
			
			// Clear cache
			String next = this._next;
			this._next = null;
			
			// Load that class
			return Library.this.loadClass(next);
		}
		
		/**
		 * {@inheritDoc}
		 * @since 2018/02/24
		 */
		@Override
		public final void remove()
		{
			throw new UnsupportedOperationException("RORO");
		}
	}
}

