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
	/**
	 * Sets the classpath to use.
	 *
	 * @param __classPath The classpath to use.
	 * @since 2025/02/14
	 */
	void classpath(Collection<Object> __classPath);
	
	/**
	 * Returns the target command line. 
	 *
	 * @return The target command line.
	 * @since 2025/02/14
	 */
	Iterable<String> getCommandLine();
	
	/**
	 * Sets the main Java class.
	 *
	 * @param __mainClass The main Java class to execute.
	 * @since 2025/02/14
	 */
	void setMain(String __mainClass);
	
	/**
	 * Sets the main arguments. 
	 *
	 * @param __args The main arguments to use.
	 * @since 2025/02/14
	 */
	void setArgs(Collection<String> __args);
	
	/**
	 * Sets the JVM arguments.
	 *
	 * @param __args The JVM arguments.
	 * @since 2025/02/14
	 */
	void setJvmArgs(Collection<String> __args);
	
	/**
	 * Sets system properties to define.
	 *
	 * @param __sysProps System properties to define.
	 * @since 2025/02/14
	 */
	void systemProperties(Map<String, String> __sysProps);
}
