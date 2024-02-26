// -*- Mode: Java; indent-tabs-mode: t; tab-width: 4 -*-
// ---------------------------------------------------------------------------
// Multi-Phasic Applications: SquirrelJME
//     Copyright (C) Stephanie Gawroriski <xer@multiphasicapps.net>
// ---------------------------------------------------------------------------
// SquirrelJME is under the Mozilla Public License Version 2.0.
// See license.mkd for licensing and copyright information.
// ---------------------------------------------------------------------------

package cc.squirreljme.debugger;

import java.nio.file.Path;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.List;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.NonNull;

/**
 * Preferences for the debugger.
 *
 * @since 2024/01/27
 */
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class Preferences
{
	/** Default value for only sourcing local classes. */
	public static final boolean DEFAULT_LOCAL_CLASS_ONLY =
		false;
	
	/** Default value for resume on connect. */
	public static final boolean DEFAULT_RESUME_ON_CONNECT =
		true;
	
	/** Default value for the last address. */
	public static final String DEFAULT_LAST_ADDRESS =
		":5005";
	
	/** The search path for class path entries if missing from remote VM. */
	protected final List<Path> classSearchPath =
		Collections.synchronizedList(new ArrayList<>());
	
	/** Automatically resume on connect? */
	@Builder.Default
	protected volatile boolean resumeOnConnect =
		Preferences.DEFAULT_RESUME_ON_CONNECT;
	
	/** Only use local classes? */
	@Builder.Default
	protected volatile boolean localClassOnly =
		Preferences.DEFAULT_LOCAL_CLASS_ONLY;
	
	/** The last address used in the argument-less start dialog. */
	@Builder.Default
	@NonNull
	protected volatile String lastAddress =
		Preferences.DEFAULT_LAST_ADDRESS;
}
