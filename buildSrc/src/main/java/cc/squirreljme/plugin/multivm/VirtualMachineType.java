// -*- Mode: Java; indent-tabs-mode: t; tab-width: 4 -*-
// ---------------------------------------------------------------------------
// Multi-Phasic Applications: SquirrelJME
//     Copyright (C) Stephanie Gawroriski <xer@multiphasicapps.net>
// ---------------------------------------------------------------------------
// SquirrelJME is under the GNU General Public License v3+, or later.
// See license.mkd for licensing and copyright information.
// ---------------------------------------------------------------------------

package cc.squirreljme.plugin.multivm;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.nio.file.Files;
import org.gradle.api.Project;
import org.gradle.api.tasks.SourceSet;

/**
 * Represents the type of virtual machine to run.
 *
 * @since 2020/08/06
 */
public enum VirtualMachineType
	implements VirtualMachineSpecifier
{
	/** Hosted virtual machine. */
	HOSTED("Hosted", "jar")
	{
		/**
		 * {@inheritDoc}
		 * @since 2020/08/15
		 */
		@Override
		public void processLibrary(InputStream __in, OutputStream __out)
			throws IOException, NullPointerException
		{
			if (__in == null || __out == null)
				throw new NullPointerException("NARG");
			
			// Is just pure copy of the JAR
			MultiVMHelpers.copy(__in, __out);
		}
	},
	
	/** SpringCoat virtual machine. */
	SPRINGCOAT("SpringCoat", "jar")
	{
		/**
		 * {@inheritDoc}
		 * @since 2020/08/15
		 */
		@Override
		public void processLibrary(InputStream __in, OutputStream __out)
			throws IOException, NullPointerException
		{
			if (__in == null || __out == null)
				throw new NullPointerException("NARG");
			
			// Is just pure copy of the JAR
			MultiVMHelpers.copy(__in, __out);
		}
	},
	
	/** SummerCoat virtual machine. */
	SUMMERCOAT("SummerCoat", "sqc")
	{
		/**
		 * {@inheritDoc}
		 * @since 2020/08/15
		 */
		@Override
		public void processLibrary(InputStream __in, OutputStream __out)
			throws IOException, NullPointerException
		{
			if (__in == null || __out == null)
				throw new NullPointerException("NARG");
			
			throw new Error("TODO");
		}
	},
	
	/* End. */
	;
	
	/** The proper name of the VM. */
	public final String properName;
	
	/** The extension for the VM. */
	public final String extension;
	
	/**
	 * Returns the proper name of the virtual machine.
	 * 
	 * @param __properName The proper name of the VM.
	 * @param __extension The library extension.
	 * @throws NullPointerException On null arguments.
	 * @since 2020/08/06
	 */
	VirtualMachineType(String __properName, String __extension)
		throws NullPointerException
	{
		if (__properName == null)
			throw new NullPointerException("NARG");
		
		this.properName = __properName;
		this.extension = __extension;
	}
	
	/**
	 * {@inheritDoc}
	 * @since 2020/08/07
	 */
	@Override
	public String outputLibraryName(Project __project, String __sourceSet)
		throws NullPointerException
	{
		if (__project == null || __sourceSet == null)
			throw new NullPointerException("NARG");
		
		// The main library should never show its original source set
		if (SourceSet.MAIN_SOURCE_SET_NAME.equals(__sourceSet))
			return __project.getName() + "." + this.extension;
		
		// Otherwise include the source sets
		return __project.getName() + __sourceSet + "." + this.extension;
	}
	
	/**
	 * {@inheritDoc}
	 * @since 2020/08/07
	 */
	@Override
	public String vmName(VMNameFormat __format)
		throws NullPointerException
	{
		String properName = this.properName;
		switch (__format)
		{
			case LOWERCASE:
				return properName.toLowerCase();
				
			case CAMEL_CASE:
				return Character.toLowerCase(properName.charAt(0)) +
					properName.substring(1);
				
			case PROPER_NOUN:
			default:
				return properName;
		}
	}
}
