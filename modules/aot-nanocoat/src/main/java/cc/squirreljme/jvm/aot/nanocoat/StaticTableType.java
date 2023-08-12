// -*- Mode: Java; indent-tabs-mode: t; tab-width: 4 -*-
// ---------------------------------------------------------------------------
// Multi-Phasic Applications: SquirrelJME
//     Copyright (C) Stephanie Gawroriski <xer@multiphasicapps.net>
// ---------------------------------------------------------------------------
// SquirrelJME is under the Mozilla Public License Version 2.0.
// See license.mkd for licensing and copyright information.
// ---------------------------------------------------------------------------

package cc.squirreljme.jvm.aot.nanocoat;

import cc.squirreljme.jvm.aot.nanocoat.linkage.ClassObjectLinkage;
import cc.squirreljme.jvm.aot.nanocoat.linkage.FieldAccessLinkage;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;

/**
 * Represents the type of static table this is.
 *
 * @since 2023/08/11
 */
public enum StaticTableType
{
	/** Strings. */
	STRINGS("char", void.class),
	
	/** Method code. */
	CODE("code", CodeFingerprint.class),
	
	/** Class interfaces. */
	CLASS_INTERFACES("ints", void.class),
	
	/** Class object linkages. */
	LINKAGE_CLASS("lncl", ClassObjectLinkage.class),
	
	/** Field access linkages. */
	LINKAGE_FIELD_ACCESS("lnfa", FieldAccessLinkage.class),
	
	/** Method invocation linkages. */
	LINKAGE_METHOD_INVOKE("lnmi", void.class),
	
	/** String reference linkages. */
	LINKAGE_STRING("lnst", void.class),
	
	/** Integer value linkages. */
	LINKAGE_INTEGER("lnvi", void.class),
	
	/** Long value linkages. */
	LINKAGE_LONG("lnvj", void.class),
	
	/** Float value linkages. */
	LINKAGE_FLOAT("lnvf", void.class),
	
	/** Double value linkages. */
	LINKAGE_DOUBLE("lnvd", void.class),
	
	/** Library resource. */
	RESOURCE("rsrc", void.class),
	
	/** Field type information. */
	FIELD_TYPE("tyme", void.class),
	
	/** Method type information. */
	METHOD_TYPE("tyme", void.class),
	
	/** Locals/stack variable limit information. */
	LOCALS_STACK_LIMITS("valn", VariableLimits.class),
	
	/* End. */
	;
	
	/** The static table types available. */
	public static final List<StaticTableType> TYPES =
		Collections.unmodifiableList(Arrays.asList(StaticTableType.values()));
	
	/** The number of types available. */
	public static final int NUM_TYPES =
		StaticTableType.TYPES.size();
	
	/** The table prefix. */
	public final String prefix;
	
	/** The type of elements the table stores. */
	public final Class<?> elementType;
	
	/**
	 * Initializes the table type.
	 *
	 * @param __prefix The prefix used.
	 * @param __elementType The element type.
	 * @throws NullPointerException On null arguments.
	 * @since 2023/08/11
	 */
	StaticTableType(String __prefix, Class<?> __elementType)
		throws NullPointerException
	{
		if (__prefix == null || __elementType == null)
			throw new NullPointerException("NARG");
		
		this.prefix = __prefix;
		this.elementType = __elementType;
	}
}
