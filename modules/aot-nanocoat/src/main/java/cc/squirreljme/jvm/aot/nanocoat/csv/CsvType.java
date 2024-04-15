// -*- Mode: Java; indent-tabs-mode: t; tab-width: 4 -*-
// ---------------------------------------------------------------------------
// Multi-Phasic Applications: SquirrelJME
//     Copyright (C) Stephanie Gawroriski <xer@multiphasicapps.net>
// ---------------------------------------------------------------------------
// SquirrelJME is under the Mozilla Public License Version 2.0.
// See license.mkd for licensing and copyright information.
// ---------------------------------------------------------------------------

package cc.squirreljme.jvm.aot.nanocoat.csv;

import cc.squirreljme.c.CFile;
import cc.squirreljme.c.CFileName;
import cc.squirreljme.c.CIdentifier;
import cc.squirreljme.csv.CsvDeserializerSerializer;
import cc.squirreljme.csv.CsvSerializer;
import cc.squirreljme.csv.CsvSerializerResult;
import cc.squirreljme.runtime.cldc.debug.Debugging;
import java.util.Arrays;
import java.util.List;
import java.util.Map;
import java.util.Set;
import net.multiphasicapps.classfile.ClassName;
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
		public ClassCsvEntry deserialize(Map<String, String> __values)
			throws NullPointerException
		{
			if (__values == null)
				throw new NullPointerException("NARG");
			
			return new ClassCsvEntry(
				new ClassName(__values.get("class")),
				CIdentifier.of(__values.get("identifier")),
				CFileName.of(__values.get("header")),
				CFileName.of(__values.get("source")));
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
			
			ClassCsvEntry entry = (ClassCsvEntry)__input;
			
			__result.value("thisName",
				entry.thisName.toString());
			__result.value("identifier",
				entry.identifier.toString());
			__result.value("header",
				entry.header.toString());
			__result.value("source",
				entry.source.toString());
			
			// End of row
			__result.endRow();
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
			
			__result.headers("class",
				"identifier",
				"header",
				"source");
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
			
			return new ModuleCsvEntry(
				__values.get("name"),
				CIdentifier.of(__values.get("identifier")),
				CFileName.of(__values.get("header")),
				CFileName.of(__values.get("source")));
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
			
			__result.value("name",
				entry.name);
			__result.value("identifier",
				entry.identifier.toString());
			__result.value("header",
				entry.header.toString());
			__result.value("source",
				entry.source.toString());
			
			__result.endRow();
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
			
			__result.headers("name",
				"identifier",
				"header",
				"source");
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
		public SharedCsvEntry deserialize(Map<String, String> __values)
			throws NullPointerException
		{
			if (__values == null)
				throw new NullPointerException("NARG");
			
			return new SharedCsvEntry(
				__values.get("prefix"),
				CIdentifier.of(__values.get("identifier")),
				CFileName.of(__values.get("header")),
				CFileName.of(__values.get("source")));
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
			
			SharedCsvEntry input = (SharedCsvEntry)__input;
			
			__result.value("prefix",
				input.prefix);
			__result.value("identifier",
				input.identifier.toString());
			__result.value("header",
				input.header.toString());
			__result.value("source",
				input.source.toString());
			
			__result.endRow();
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
			
			__result.headers("prefix",
				"identifier",
				"header",
				"source");
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
		public ResourceCsvEntry deserialize(Map<String, String> __values)
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
			
			__result.headers("path",
				"identifier",
				"header",
				"source");
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
	
	/**
	 * Casts the given set.
	 *
	 * @param <T> The type of set to cast to.
	 * @param __type The type of set to cast to.
	 * @param __in The input set.
	 * @return The cast type.
	 * @throws ClassCastException If the type is not valid for this type.
	 * @throws NullPointerException On null arguments.
	 * @since 2023/09/25
	 */
	@SuppressWarnings("unchecked")
	public final <T> Set<T> cast(Class<T> __type, Set<?> __in)
		throws ClassCastException, NullPointerException
	{
		if (__in == null)
			throw new NullPointerException("NARG");
		
		if (!__type.isAssignableFrom(this.entryType))
			throw new ClassCastException("CLCL");
		
		return (Set<T>)((Object)__in);
	}
	
	/**
	 * Casts the serializer accordingly to the given type.
	 *
	 * @param <T> The type to cast to.
	 * @param __type The type to cast to.
	 * @return The cast type.
	 * @throws ClassCastException If the type is not valid.
	 * @throws NullPointerException On null arguments.
	 * @since 2023/10/15
	 */
	@SuppressWarnings("unchecked")
	public final <T> CsvSerializer<T> serializer(Class<T> __type)
		throws ClassCastException, NullPointerException
	{
		if (!__type.isAssignableFrom(this.entryType))
			throw new ClassCastException("CAST");
		
		return (CsvSerializer<T>)((Object)this);
	}
}
