// -*- Mode: Java; indent-tabs-mode: t; tab-width: 4 -*-
// ---------------------------------------------------------------------------
// Multi-Phasic Applications: SquirrelJME
//     Copyright (C) Stephanie Gawroriski <xer@multiphasicapps.net>
// ---------------------------------------------------------------------------
// SquirrelJME is under the GNU General Public License v3+, or later.
// See license.mkd for licensing and copyright information.
// ---------------------------------------------------------------------------

package cc.squirreljme.jvm.aot.test;

import cc.squirreljme.jvm.aot.Backend;
import cc.squirreljme.jvm.aot.LinkGlob;
import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;

/**
 * Base class for testing compilation of code.
 *
 * @since 2022/08/14
 */
public abstract class BaseCompilation
	extends BaseBackend
{
	/**
	 * Initializes the base compilation.
	 *
	 * @param __backend The backend to use.
	 * @throws NullPointerException On null arguments.
	 * @since 2022/08/14
	 */
	public BaseCompilation(Backend __backend)
		throws NullPointerException
	{
		super(__backend);
	}
	
	/**
	 * Compiles the given class and returns the resulting bytes of that
	 * compilation.
	 * 
	 * @param __name The name of the class to compile.
	 * @param __in The class byte data.
	 * @return The compiled bytes for the class.
	 * @throws IOException On read/write errors.
	 * @throws NullPointerException On null arguments.
	 * @since 2022/08/14
	 */
	public final byte[] compileClass(String __name, InputStream __in)
		throws IOException, NullPointerException
	{
		try (ByteArrayOutputStream baos = new ByteArrayOutputStream())
		{
			// This compiles to a byte array output
			this.backend.compileClass(this.situationParameters().linkGlob,
				__name, __in, baos);
			
			return baos.toByteArray();
		} 
	}
	
	
	/**
	 * Compiles the given class and returns the resulting bytes of that
	 * compilation.
	 * 
	 * @param __example The example class to compile.
	 * @return The compiled bytes for the class.
	 * @throws IOException On read/write errors.
	 * @throws NullPointerException On null arguments.
	 * @since 2022/08/14
	 */
	public final byte[] compileClass(ExampleClass __example)
		throws IOException, NullPointerException
	{
		if (__example == null)
			throw new NullPointerException("NARG");
			
		try (InputStream in = __example.load())
		{
			return this.compileClass(__example.className(), in);
		}
	}
	
	/**
	 * Compiles the given class and then joins it into the {@link LinkGlob}
	 * so that it is a part of the output.
	 * 
	 * @param __name The name of the class to compile.
	 * @param __in The class byte data.
	 * @throws IOException On read/write errors.
	 * @throws NullPointerException On null arguments.
	 * @since 2022/08/14
	 */
	public final void compileClassAndJoin(String __name, InputStream __in)
		throws IOException, NullPointerException
	{
		try (ByteArrayInputStream in = new ByteArrayInputStream(
			this.compileClass(__name, __in)))
		{
			this.situationParameters().linkGlob.join(__name, false, in);
		}
	}
	
	/**
	 * Compiles the given example and then joins it into the {@link LinkGlob}
	 * so that it is a part of the output.
	 * 
	 * @throws IOException On read/write errors.
	 * @throws NullPointerException On null arguments.
	 * @since 2022/08/14
	 */
	public final void compileClassAndJoin(ExampleClass __example)
		throws IOException, NullPointerException
	{
		if (__example == null)
			throw new NullPointerException("NARG");
			
		try (InputStream in = __example.load())
		{
			this.compileClassAndJoin(__example.className(), in);
		}
	}
}
