// -*- Mode: Java; indent-tabs-mode: t; tab-width: 4 -*-
// ---------------------------------------------------------------------------
// SquirrelJME
//     Copyright (C) Stephanie Gawroriski <xer@multiphasicapps.net>
// ---------------------------------------------------------------------------
// SquirrelJME is under the Mozilla Public License Version 2.0.
// See license.mkd for licensing and copyright information.
// ---------------------------------------------------------------------------

package com.oracle.json.stream;

import cc.squirreljme.runtime.cldc.annotation.Api;

/**
 * Provides location information on the source of the JSON data when an
 * event occurs. All information provided here is optional, and where any
 * values which are not known {@code -1} shall be returned.
 *
 * @since 2014/07/25
 */
@Api
public interface JsonLocation
{
	/**
	 * Returns the column number for the current event, or {@code -1} if
	 * unknown.
	 *
	 * @return Column number or {@code -1}.
	 * @since 2014/07/25
	 */
	@Api
	long getColumnNumber();
	
	/**
	 * Returns the line number for the current event, or {@code -1} if
	 * unknown.
	 *
	 * @return Line number or {@code -1}.
	 * @since 2014/07/25
	 */
	@Api
	long getLineNumber();
	
	/**
	 * Returns the stream offset for the current event, or {@code -1} if
	 * unknown.
	 *
	 * @return Stream offset or {@code -1}.
	 * @since 2014/07/25
	 */
	@Api
	long getStreamOffset();
}

