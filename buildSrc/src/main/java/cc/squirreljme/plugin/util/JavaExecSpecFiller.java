// -*- Mode: Java; indent-tabs-mode: t; tab-width: 4 -*-
// ---------------------------------------------------------------------------
// SquirrelJME
//     Copyright (C) Stephanie Gawroriski <xer@multiphasicapps.net>
// ---------------------------------------------------------------------------
// SquirrelJME is under the Mozilla Public License Version 2.0.
// See license.mkd for licensing and copyright information.
// ---------------------------------------------------------------------------

package cc.squirreljme.plugin.util;

import java.util.Collection;
import java.util.Map;

/**
 * Execution specification that is self implemented and capable of generateing
 * arguments accordingly.
 *
 * @since 2020/12/26
 */
public interface JavaExecSpecFiller
{
	void classpath(Collection<Object> __classPath);
	
	Iterable<String> getCommandLine();
	
	void setMain(String __mainClass);
	
	void setArgs(Collection<String> __args);
	
	void setJvmArgs(Collection<String> __args);
	
	void systemProperties(Map<String, String> __sysProps);
}
