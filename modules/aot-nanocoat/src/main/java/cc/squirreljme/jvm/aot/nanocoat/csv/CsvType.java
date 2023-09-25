// -*- Mode: Java; indent-tabs-mode: t; tab-width: 4 -*-
// ---------------------------------------------------------------------------
// Multi-Phasic Applications: SquirrelJME
//     Copyright (C) Stephanie Gawroriski <xer@multiphasicapps.net>
// ---------------------------------------------------------------------------
// SquirrelJME is under the Mozilla Public License Version 2.0.
// See license.mkd for licensing and copyright information.
// ---------------------------------------------------------------------------

package cc.squirreljme.jvm.aot.nanocoat.csv;

import cc.squirreljme.csv.CsvDeserializerSerializer;
import cc.squirreljme.csv.CsvSerializerResult;
import cc.squirreljme.runtime.cldc.debug.Debugging;
import java.util.Arrays;
import java.util.List;
import java.util.Map;
import net.multiphasicapps.collections.UnmodifiableList;

/**
 * Represents the type of CSV used.
 *
 * @since 2023/09/25
 */
public enum CsvType
	implements CsvDeserializerSerializer<Object>
{
	/** Classes. */
	CLASSES("classes.csv", ClassCsvEntry.class)
	{
		/**
		 * {@inheritDoc}
		 * @since 2023/09/25
		 */
		@Override
		public void serialize(Object __input, CsvSerializerResult __result)
			throws NullPointerException
		{
			if (__input == null || __result == null)
				throw new NullPointerException("NARG");
			
			throw Debugging.todo();
		}
		
		/**
		 * {@inheritDoc}
		 * @since 2023/09/25
		 */
		@Override
		public void serializeHeaders(CsvSerializerResult __result)
			throws NullPointerException
		{
			if (__result == null)
				throw new NullPointerException("NARG");
			
			throw Debugging.todo();
		}
		
		/**
		 * {@inheritDoc}
		 * @since 2023/09/25
		 */
		@Override
		public Object deserialize(Map<String, String> __values)
			throws NullPointerException
		{
			if (__values == null)
				throw new NullPointerException("NARG");
			
			throw Debugging.todo();
		}
	},
	
	/** Modules. */
	MODULE("modules.csv", ModuleCsvEntry.class)
	{
		/**
		 * {@inheritDoc}
		 * @since 2023/09/25
		 */
		@Override
		public ModuleCsvEntry deserialize(Map<String, String> __values)
			throws NullPointerException
		{
			if (__values == null)
				throw new NullPointerException("NARG");
			
			throw Debugging.todo();
		}
		
		/**
		 * {@inheritDoc}
		 * @since 2023/09/25
		 */
		@Override
		public void serialize(Object __input, CsvSerializerResult __result)
			throws NullPointerException
		{
			if (__input == null || __result == null)
				throw new NullPointerException("NARG");
			
			ModuleCsvEntry entry = (ModuleCsvEntry)__input;
			
			throw Debugging.todo();
		}
		
		/**
		 * {@inheritDoc}
		 * @since 2023/09/25
		 */
		@Override
		public void serializeHeaders(CsvSerializerResult __result)
			throws NullPointerException
		{
			if (__result == null)
				throw new NullPointerException("NARG");
			
			throw Debugging.todo();
		}
	},
	
	/** Shared fragments. */
	SHARED("shared.csv", SharedCsvEntry.class)
	{
		/**
		 * {@inheritDoc}
		 * @since 2023/09/25
		 */
		@Override
		public void serialize(Object __input, CsvSerializerResult __result)
			throws NullPointerException
		{
			if (__input == null || __result == null)
				throw new NullPointerException("NARG");
			
			throw Debugging.todo();
		}
		
		/**
		 * {@inheritDoc}
		 * @since 2023/09/25
		 */
		@Override
		public void serializeHeaders(CsvSerializerResult __result)
			throws NullPointerException
		{
			if (__result == null)
				throw new NullPointerException("NARG");
			
			throw Debugging.todo();
		}
		
		/**
		 * {@inheritDoc}
		 * @since 2023/09/25
		 */
		@Override
		public Object deserialize(Map<String, String> __values)
			throws NullPointerException
		{
			if (__values == null)
				throw new NullPointerException("NARG");
			
			throw Debugging.todo();
		}
	},
	
	/** Resources. */
	RESOURCES("resources.csv", ResourceCsvEntry.class)
	{
		/**
		 * {@inheritDoc}
		 * @since 2023/09/25
		 */
		@Override
		public void serialize(Object __input, CsvSerializerResult __result)
			throws NullPointerException
		{
			if (__input == null || __result == null)
				throw new NullPointerException("NARG");
			
			throw Debugging.todo();
		}
		
		/**
		 * {@inheritDoc}
		 * @since 2023/09/25
		 */
		@Override
		public void serializeHeaders(CsvSerializerResult __result)
			throws NullPointerException
		{
			if (__result == null)
				throw new NullPointerException("NARG");
			
			throw Debugging.todo();
		}
		
		/**
		 * {@inheritDoc}
		 * @since 2023/09/25
		 */
		@Override
		public Object deserialize(Map<String, String> __values)
			throws NullPointerException
		{
			if (__values == null)
				throw new NullPointerException("NARG");
			
			throw Debugging.todo();
		}
	},
	
	/* End. */
	;
	
	/** The types of CSVs which are available. */
	public static final List<CsvType> VALUES =
		UnmodifiableList.of(Arrays.asList(CsvType.values()));
	
	/** The file name used. */
	public final String fileName;
	
	/** The file name with the root. */
	public final String rootedFileName;
	
	/** The entry type this serializes to/from. */
	public final Class<?> entryType;
	
	/**
	 * Initializes the CSV type.
	 *
	 * @param __fileName The file name of the type.
	 * @param __entry The entry type this maps to.
	 * @throws NullPointerException On null arguments
	 * @since 2023/09/25
	 */
	CsvType(String __fileName, Class<?> __entry)
		throws NullPointerException
	{
		if (__entry == null)
			throw new NullPointerException("NARG");
		
		this.fileName = __fileName;
		this.rootedFileName = "/" + __fileName;
		this.entryType = __entry;
	}
}
