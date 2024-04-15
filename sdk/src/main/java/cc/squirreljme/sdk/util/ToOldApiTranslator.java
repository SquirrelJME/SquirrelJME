// -*- Mode: Java; indent-tabs-mode: t; tab-width: 4 -*-
// ---------------------------------------------------------------------------
// Multi-Phasic Applications: SquirrelJME
//     Copyright (C) Stephanie Gawroriski <xer@multiphasicapps.net>
// ---------------------------------------------------------------------------
// SquirrelJME is under the Mozilla Public License Version 2.0.
// See license.mkd for licensing and copyright information.
// ---------------------------------------------------------------------------

package cc.squirreljme.sdk.util;

import java.lang.ref.WeakReference;
import java.util.Enumeration;
import java.util.Iterator;
import java.util.TimeZone;

/**
 * Translators for old API usage.
 *
 * @since 2024/04/15
 */
public enum ToOldApiTranslator
{
	/** Usage of boxed {@code X.valueOf()} to {@code new X()}. */
	BOXED_VALUE_OF_TO_NEW,
	
	/** Enumerations to simple classes. */
	ENUM_TO_SIMPLE_CLASS,
	
	/** Translates use of {@link Iterator} to {@link Enumeration}. */
	ITERATOR_TO_ENUMERATION,
	
	/** Wrapping of missing collection types. */
	MISSING_COLLECTIONS,
	
	/** Use {@link System#currentTimeMillis()} as basis instead. */
	NANO_TIME,
	
	/** Translates newer collection calls to older collection calls. */
	NEW_TO_OLD_COLLECTIONS,
	
	/** Usage of {@link StringBuilder} to {@link StringBuffer}. */
	STRING_BUILDER_TO_STRING_BUFFER,
	
	/** Removes all usages of {@link Throwable} causes. */
	THROWABLE_DROP_CAUSE,
	
	/** Changes all usages of UTC {@link TimeZone} to GMT. */
	UTC_TO_GMT,
	
	/** Usages of other reference types to {@link WeakReference}. */
	WEAK_REFERENCE,
	
	/* End. */
	;
}
