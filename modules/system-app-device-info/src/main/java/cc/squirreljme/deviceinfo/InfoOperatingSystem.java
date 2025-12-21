// -*- Mode: Java; indent-tabs-mode: t; tab-width: 4 -*-
// ---------------------------------------------------------------------------
// Multi-Phasic Applications: SquirrelJME
//     Copyright (C) Stephanie Gawroriski <xer@multiphasicapps.net>
// ---------------------------------------------------------------------------
// SquirrelJME is under the Mozilla Public License Version 2.0.
// See license.mkd for licensing and copyright information.
// ---------------------------------------------------------------------------

package cc.squirreljme.deviceinfo;

import cc.squirreljme.jvm.mle.RuntimeShelf;
import cc.squirreljme.jvm.mle.constants.VMDescriptionType;
import cc.squirreljme.jvm.mle.exceptions.MLECallError;
import cc.squirreljme.runtime.cldc.debug.Debugging;
import java.util.Objects;

/**
 * Operating system specific values.
 *
 * @since 2025/12/06
 */
public enum InfoOperatingSystem
	implements SpecificField
{
	/** Name. */
	NAME("Name", "os.name"),
	
	/** Version. */
	VERSION("Version", "os.version"),
	
	/** Architecture. */
	ARCHITECTURE("Architecture", "os.arch"),
	
	/** Path separator. */
	PATH_SEPARATOR("Path Separator", "file.separator"),
	
	/** Line ending type. */
	LINE_ENDING("Line Ending", "line.separator")
	{
		/**
		 * {@inheritDoc}
		 * @since 2025/12/06
		 */
		@Override
		public String value()
		{
			String sep = System.getProperty("line.separator");
			
			// Make it more readable
			StringBuilder sb = new StringBuilder();
			for (int n = sep.length(), i = 0; i < n; i++)
			{
				char c = sep.charAt(i);
				
				if (c == '\r')
					sb.append("CR");
				else if (c == '\n')
					sb.append("LF");
				else
					sb.append(String.format("%02x", c & 0xFF));
			}
			
			return sb.toString();
		}
	},
	
	/** Executable. */
	EXECUTABLE_PATH("Executable Path", null)
	{
		/**
		 * {@inheritDoc}
		 * @since 2025/12/06
		 */
		@Override
		public String value()
		{
			try
			{
				return Objects.toString(RuntimeShelf.vmDescription(
					VMDescriptionType.EXECUTABLE_PATH), "unknown");
			}
			catch (MLECallError __e)
			{
				return __e.getMessage();
			}
		}
	},
	
	/* End. */
	;
	
	/** The key for the field. */
	protected final String key;
	
	/** The property for the field. */
	protected final String property;
	
	/**
	 * Initializes the field key.
	 *
	 * @param __key The key.
	 * @param __property The property.
	 * @throws NullPointerException On null arguments.
	 * @since 2025/12/06
	 */
	InfoOperatingSystem(String __key, String __property)
		throws NullPointerException
	{
		if (__key == null)
			throw new NullPointerException("NARG");
		
		this.key = __key;
		this.property = __property;
	}
	
	/**
	 * {@inheritDoc}
	 *
	 * @since 2025/12/06
	 */
	@Override
	public final String key()
	{
		return this.key;
	}
	
	/**
	 * {@inheritDoc}
	 *
	 * @since 2025/12/06
	 */
	@Override
	public String value()
	{
		return System.getProperty(this.property);
	}
}
