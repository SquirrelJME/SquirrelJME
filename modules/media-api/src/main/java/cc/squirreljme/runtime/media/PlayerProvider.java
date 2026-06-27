// -*- Mode: Java; indent-tabs-mode: t; tab-width: 4 -*-
// ---------------------------------------------------------------------------
// Multi-Phasic Applications: SquirrelJME
//     Copyright (C) Stephanie Gawroriski <xer@multiphasicapps.net>
// ---------------------------------------------------------------------------
// SquirrelJME is under the Mozilla Public License Version 2.0.
// See license.mkd for licensing and copyright information.
// ---------------------------------------------------------------------------

package cc.squirreljme.runtime.media;

import cc.squirreljme.runtime.cldc.annotation.SquirrelJMEVendorApi;
import java.io.IOException;
import java.util.ServiceLoader;
import javax.microedition.io.InputConnection;
import javax.microedition.media.MediaException;
import javax.microedition.media.Player;
import org.intellij.lang.annotations.Language;

/**
 * This is used to create instances of players which may be optionally
 * included or otherwise.
 * 
 * This is to be used with {@link ServiceLoader}.
 *
 * @since 2026/06/26
 */
@SquirrelJMEVendorApi
public interface PlayerProvider
{
	/**
	 * Does this player accept the given content type?
	 *
	 * @param __contentType The content type.
	 * @return If this accepts the given content type.
	 * @throws NullPointerException On null arguments.
	 * @since 2026/06/27
	 */
	@SquirrelJMEVendorApi
	boolean acceptsContentType(
		@Language("mime-type-reference") String __contentType)
		throws NullPointerException;
	
	/**
	 * Returns the content types which are supported by this provider.
	 *
	 * @return The supported content types for this player.
	 * @since 2026/06/27
	 */
	@Language("mime-type-reference")
	@SquirrelJMEVendorApi
	String[] acceptsContentTypes();
	
	/**
	 * Does this player type accept input connections?
	 *
	 * @return If this accepts player connections.
	 * @since 2026/06/27
	 */
	@SquirrelJMEVendorApi
	boolean acceptsInputConnection();
	
	/**
	 * Creates a player for the given media through the given input
	 * connection. 
	 *
	 * @param __in The connection to read from.
	 * @param __contentType The content type used.
	 * @return The player for the given media.
	 * @throws IOException On read/write errors.
	 * @throws MediaException If the media could not be opened.
	 * @throws NullPointerException On null arguments.
	 * @since 2026/06/27
	 */
	@SquirrelJMEVendorApi
	Player viaInputConnection(InputConnection __in,
		String __contentType)
		throws IOException, MediaException, NullPointerException;
}
