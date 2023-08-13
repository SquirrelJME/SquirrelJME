// -*- Mode: Java; indent-tabs-mode: t; tab-width: 4 -*-
// ---------------------------------------------------------------------------
// Multi-Phasic Applications: SquirrelJME
//     Copyright (C) Stephanie Gawroriski <xer@multiphasicapps.net>
// ---------------------------------------------------------------------------
// SquirrelJME is under the Mozilla Public License Version 2.0.
// See license.mkd for licensing and copyright information.
// ---------------------------------------------------------------------------

package cc.squirreljme.jvm.aot.nanocoat.table;

import cc.squirreljme.c.CFile;
import cc.squirreljme.c.CFileName;
import cc.squirreljme.c.CIdentifier;
import cc.squirreljme.c.CPPBlock;
import cc.squirreljme.c.CVariable;
import cc.squirreljme.jvm.aot.nanocoat.ArchiveOutputQueue;
import cc.squirreljme.runtime.cldc.debug.Debugging;
import cc.squirreljme.runtime.cldc.util.SortedTreeMap;
import cc.squirreljme.runtime.cldc.util.SortedTreeSet;
import java.io.IOException;
import java.lang.ref.Reference;
import java.util.Map;
import java.util.Set;

/**
 * This represents a static table which will output in a format similar to TSV
 * or CSV which is handled on the NanoCoat side of NanoCoat, it is intended
 * to deduplicate any entries in any given ROM so that every single library
 * accordingly does not need to duplicate the same information over and over.
 * These static tables are essentially used for everything and are ultimately
 * merged in the very end.
 *
 * @param <K> The key type for entries.
 * @param <V> The type of values to store, may be the same as keys. 
 * @since 2023/08/10
 */
public abstract class StaticTable<K, V>
{
	/** Entries within the static table. */
	protected final Map<K, CVariable> keys =
		new SortedTreeMap<>();
	
	/** Variable identifiers, used to check for collisions. */
	protected final Map<CIdentifier, K> identifiers =
		new SortedTreeMap<>();
	
	/** Identifier tracing. */
	protected final Map<CIdentifier, Throwable> identifiersTrace =
		new SortedTreeMap<>();
	
	/** The type of table this is. */
	protected final StaticTableType type;
	
	/** The group which owns this table. */
	private final Reference<StaticTableManager> _manager;
	
	/**
	 * Initializes the static table.
	 *
	 * @param __type The type of table this is.
	 * @throws NullPointerException On null arguments.
	 * @since 2023/08/11
	 */
	StaticTable(Reference<StaticTableManager> __group, StaticTableType __type)
		throws NullPointerException
	{
		if (__group == null || __type == null)
			throw new NullPointerException("NARG");
		
		this._manager = __group;
		this.type = __type;
	}
	
	/**
	 * Returns the identifier to use for the entry.
	 *
	 * @param __key The entry to identify.
	 * @return The identifier to use.
	 * @throws IOException On write errors.
	 * @throws NullPointerException On null arguments.
	 * @since 2023/08/12
	 */
	protected abstract String buildIdentity(K __key)
		throws IOException, NullPointerException;
	
	/**
	 * Writes a table entry to the archive output.
	 *
	 * @param __sourceFile The archive to write to.
	 * @param __fileName The suggested file name.
	 * @param __variable The variable being written.
	 * @param __key The entry to write.
	 * @param __value The value to write.
	 * @throws IOException On write errors.
	 * @throws NullPointerException On null arguments.
	 * @since 2023/08/12
	 */
	protected abstract void writeSource(CFile __sourceFile,
		String __fileName, CVariable __variable, K __key, V __value)
		throws IOException, NullPointerException;
	
	/**
	 * Returns the identifier to use for the entry.
	 *
	 * @param __key The entry to identify.
	 * @return The identifier to use.
	 * @throws NullPointerException On null arguments.
	 * @since 2023/08/13
	 */
	public final CIdentifier identify(K __key)
		throws NullPointerException
	{
		if (__key == null)
			throw new NullPointerException("NARG");
		
		// Use already cached name
		Map<K, CVariable> keys = this.keys;
		if (keys.containsKey(__key))
		{
			CVariable var = keys.get(__key);
			if (var != null)
				return var.name;
		}
		
		// Otherwise build the identity
		try
		{
			return CIdentifier.of(String.format("%s_%s", this.type.prefix,
				this.buildIdentity(__key).toLowerCase()));
		}
		catch (IOException __e)
		{
			throw new IllegalArgumentException(__e);
		}
	}
	
	/**
	 * Puts an entry into the table if it does not exist, and returns the
	 * variable designated for it.
	 *
	 * @param __key The entry to put in.
	 * @return The resultant variable.
	 * @throws IOException On write errors.
	 * @throws NullPointerException On null arguments.
	 * @since 2023/08/12
	 */
	@SuppressWarnings("unchecked")
	public final CVariable put(K __key)
		throws IOException, NullPointerException
	{
		return this.put(__key, (V)__key);
	}
	
	/**
	 * Puts an entry into the table if it does not exist, and returns the
	 * variable designated for it.
	 *
	 * @param __key The entry to put in.
	 * @return The resultant variable.
	 * @throws IOException On write errors.
	 * @throws NullPointerException On null arguments.
	 * @since 2023/08/13
	 */
	public final CVariable put(K __key, V __value)
		throws IOException, NullPointerException
	{
		if (__key == null || __value == null)
			throw new NullPointerException("NARG");
		
		// Already in the map?
		Map<K, CVariable> keys = this.keys;
		if (keys.containsKey(__key))
			return keys.get(__key);
		
		// Identify the key first, to check for collision
		CIdentifier identity = this.identify(__key);
		
		/* {@squirreljme.error NC05 Duplicate identifier. (The identifier)} */
		Map<CIdentifier, K> identifiers = this.identifiers;
		Map<CIdentifier, Throwable> identifiersTrace = this.identifiersTrace;
		if (identifiers.containsKey(identity))
			throw new IllegalStateException(String.format("NC05 %s %s != %s",
				identity, __key, identifiers.get(identity)),
				identifiersTrace.get(identity));
		
		// Record mapping accordingly, for duplication check
		identifiers.put(identity, __key);
		identifiersTrace.put(identity, new Throwable(__key.toString()));
		
		// We need the table manager from this point on
		StaticTableManager manager = this.__manager();
		
		// Setup variable and store into the map
		StaticTableType type = this.type;
		keys.put(__key, null);
		CVariable result = CVariable.of(type.cType, identity);
		keys.put(__key, result);
		
		// Since we just put the key in, generate the needed sources
		String sourceFile = String.format("shared/%s/%s.c",
			type.prefix,
			result.name);
		String headerFile = String.format("shared/%s/include/%s.h",
			type.prefix,
			result.name);
		
		ArchiveOutputQueue archive = manager.archive;
		
		// Header to declare extern
		CFileName headerName = CFileName.of(headerFile);
		try (CFile header = archive.nextCFile(headerName))
		{
			try (CPPBlock ignored = header.headerGuard(headerName))
			{
				header.declare(result.extern());
			}
		}
		
		// Source which actually defines the string
		try (CFile source = archive.nextCFile(sourceFile))
		{
			// Include header
			source.preprocessorInclude(headerName);
			
			// Write source
			this.writeSource(source, sourceFile, result, __key, __value);
		}
		
		// Use the new variable
		return result;
	}
	
	/**
	 * Returns the manager.
	 *
	 * @return The manager.
	 * @throws IllegalStateException If it has been garbage collected.
	 * @since 2023/08/13
	 */
	final StaticTableManager __manager()
		throws IllegalStateException
	{
		StaticTableManager manager = this._manager.get();
		if (manager == null)
			throw new IllegalStateException("GCGC");
		
		return manager;
	}
}
