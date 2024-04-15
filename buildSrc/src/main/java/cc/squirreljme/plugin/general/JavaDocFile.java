// -*- Mode: Java; indent-tabs-mode: t; tab-width: 4 -*-
// ---------------------------------------------------------------------------
// Multi-Phasic Applications: SquirrelJME
//     Copyright (C) Stephanie Gawroriski <xer@multiphasicapps.net>
// ---------------------------------------------------------------------------
// SquirrelJME is under the Mozilla Public License Version 2.0.
// See license.mkd for licensing and copyright information.
// ---------------------------------------------------------------------------

package cc.squirreljme.plugin.general;

import java.io.IOException;
import java.io.InputStream;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;

/**
 * Represents the JavaDoc file.
 *
 * @since 2022/08/29
 */
public final class JavaDocFile
	implements Comparable<JavaDocFile>
{
	/** The name of the file. */
	public final String name;
	
	/** The data supplier. */
	public final JavaDocFileSupplier supplier;
	
	/** The checksum. */
	private volatile byte[] _checksum;
	
	/**
	 * Initializes the file reference.
	 * 
	 * @param __name The name of the file.
	 * @param __supplier The supplier for the file data.
	 * @throws NullPointerException On null arguments.
	 * @since 2022/08/29
	 */
	public JavaDocFile(String __name, JavaDocFileSupplier __supplier)
		throws NullPointerException
	{
		if (__name == null || __supplier == null)
			throw new NullPointerException("NARG");
		
		this.name = __name;
		this.supplier = __supplier;
	}
	
	/**
	 * Returns the checksum of the file.
	 * 
	 * @return The file checksum.
	 * @since 2022/08/29
	 */
	public byte[] checkSum()
	{
		// Has this already been calculated?
		byte[] checksum = this._checksum;
		if (checksum != null)
			return checksum.clone();
		
		// Calculate it
		try (InputStream in = this.supplier.open())
		{
			MessageDigest digest = MessageDigest.getInstance("sha-1");
			
			// Calculate checksum with bytes
			byte[] buf = new byte[16384];
			for (;;)
			{
				int rc = in.read(buf);
				
				if (rc < 0)
					break;
				
				digest.update(buf, 0, rc);
			}
			
			checksum = digest.digest();
			this._checksum = checksum;
			return checksum.clone();
		}
		catch (IOException|NoSuchAlgorithmException e)
		{
			throw new RuntimeException(e);
		}
	}
	
	/**
	 * {@inheritDoc}
	 * @since 2022/08/29
	 */
	@Override
	public int compareTo(JavaDocFile __b)
	{
		return this.name.compareTo(__b.name);
	}
	
	/**
	 * {@inheritDoc}
	 * @since 2022/08/29
	 */
	@Override
	public String toString()
	{
		return this.name;
	}
}
