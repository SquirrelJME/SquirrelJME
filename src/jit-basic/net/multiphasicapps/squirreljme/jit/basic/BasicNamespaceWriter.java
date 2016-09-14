// -*- Mode: Java; indent-tabs-mode: t; tab-width: 4 -*-
// ---------------------------------------------------------------------------
// Multi-Phasic Applications: SquirrelJME
//     Copyright (C) 2013-2016 Steven Gawroriski <steven@multiphasicapps.net>
//     Copyright (C) 2013-2016 Multi-Phasic Applications <multiphasicapps.net>
// ---------------------------------------------------------------------------
// SquirrelJME is under the GNU General Public License v3+, or later.
// For more information see license.mkd.
// ---------------------------------------------------------------------------

package net.multiphasicapps.squirreljme.jit.basic;

import java.io.IOException;
import java.io.OutputStream;
import java.util.ArrayList;
import java.util.List;
import net.multiphasicapps.io.data.DataEndianess;
import net.multiphasicapps.io.data.ExtendedDataOutputStream;
import net.multiphasicapps.squirreljme.java.symbols.ClassNameSymbol;
import net.multiphasicapps.squirreljme.jit.base.JITException;
import net.multiphasicapps.squirreljme.jit.JITClassWriter;
import net.multiphasicapps.squirreljme.jit.JITConfig;
import net.multiphasicapps.squirreljme.jit.JITNamespaceWriter;
import net.multiphasicapps.squirreljme.jit.JITResourceWriter;
import net.multiphasicapps.squirreljme.nativecode.NativeCodeWriterFactory;

/**
 * This is the namespace writer which is used ot write.
 *
 * @since 2016/09/11
 */
public class BasicNamespaceWriter
	implements JITNamespaceWriter
{
	/** The magic number for the namespace file. */
	private static final long _MAGIC_NUMBER =
		0xD3F1F5E9F2F2E5ECL;
	
	/** The namespace name. */
	protected final String name;
	
	/** The configuration. */
	final JITConfig _config;
	
	/** The output executable. */
	final ExtendedDataOutputStream _output;
	
	/** The basic global pool. */
	final BasicConstantPool _pool =
		new BasicConstantPool();
	
	/** The code writer factory. */
	final NativeCodeWriterFactory _codewriter;
	
	/** Classes in the namespace. */
	private final List<__Class__> _classes =
		new ArrayList<>();
	
	/** Resources in the namespace. */
	private final List<__Resource__> _resources =
		new ArrayList<>();
	
	/** Implemented interfaces in the namespace. */
	private final List<__Interface__> _interfaces =
		new ArrayList<>();
	
	/** The methods that exist in the namespace. */
	private final List<__Method__> _methods =
		new ArrayList<>();
	
	/** Native code within a method. */
	private final List<__Code__> _codes =
		new ArrayList<>();
	
	/** The current writer. */
	volatile __BaseWriter__ _current;
	
	/**
	 * Initializes the namespace writer.
	 *
	 * @param __conf The configuration used.
	 * @param __name The name of the namespace.
	 * @param __os The output executable.
	 * @throws NullPointerException On null arguments.
	 * @since 2016/09/11
	 */
	public BasicNamespaceWriter(JITConfig __conf, String __name,
		OutputStream __os)
		throws NullPointerException
	{
		if (__conf == null || __name == null || __os == null)
			throw new NullPointerException("NARG");
		
		// Set
		this._config = __conf;
		this.name = __name;
		
		// {@squirreljme.error BV01 No native code factory was set in the
		// configuration. (The configuration)}
		NativeCodeWriterFactory codewriter = __conf.<NativeCodeWriterFactory>
			getAsClass(BasicOutputFactory.NATIVE_CODE_PROPERTY,
			NativeCodeWriterFactory.class);
		if (codewriter == null)
			throw new JITException(String.format("BV01 %s", __conf));
		this._codewriter = codewriter;
		
		// Initialize output stream
		ExtendedDataOutputStream output;
		this._output = (output = new ExtendedDataOutputStream(__os));
		switch (__conf.triplet().endianess())
		{
			case BIG: output.setEndianess(DataEndianess.BIG); break;
			case LITTLE: output.setEndianess(DataEndianess.LITTLE); break;
			
				// Unknown
			default:
				throw new RuntimeException("OOPS");
		}
		
		// Write magic number
		try
		{
			output.writeLong(_MAGIC_NUMBER);
		}
		
		// {@squirreljme.error BV09 Failed to write the magic number.}
		catch (IOException e)
		{
			throw new JITException("BV09", e);
		}
	}
	
	/**
	 * {@inheritDoc}
	 * @since 2016/09/11
	 */
	@Override
	public JITClassWriter beginClass(ClassNameSymbol __cn)
		throws JITException, NullPointerException
	{
		// Check
		if (__cn == null)
			throw new NullPointerException("NARG");
		
		// {@squirreljme.error BV02 Cannot write a new class when there is
		// another class or resource being written.}
		if (this._current != null)
			throw new JITException("BV02");
		
		// Create class
		__Class__ cl = new __Class__(this._pool.addClassName(__cn));
		BasicClassWriter rv = new BasicClassWriter(this, cl);
		this._classes.add(cl);
		return rv;
	}
	
	/**
	 * {@inheritDoc}
	 * @since 2016/09/11
	 */
	@Override
	public JITResourceWriter beginResource(String __name)
		throws JITException, NullPointerException
	{
		// Check
		if (__name == null)
			throw new NullPointerException("NARG");
		
		// {@squirreljme.error BV03 Cannot write a new resource when there is
		// another class or resource being written.}
		if (this._current != null)
			throw new JITException("BV03");
		
		// Create resource
		__Resource__ rc = new __Resource__(this._pool.addString(__name));
		BasicResourceWriter rv = new BasicResourceWriter(this, rc);
		this._resources.add(rc);
		return rv;
	}
	
	/**
	 * {@inheritDoc}
	 * @since 2016/09/11
	 */
	@Override
	public void close()
		throws JITException
	{
		throw new Error("TODO");
	}
	
	/**
	 * Adds code to the current namespace.
	 *
	 * @param __m The code to add.
	 * @return The index where the code was added.
	 * @throws NullPointerException On null arguments.
	 * @since 2016/09/14
	 */
	int __addCode(__Code__ __c)
	{
		// Check
		if (__c == null)
			throw new NullPointerException("NARG");
		
		// Add to the end
		List<__Code__> codes = this._codes;
		int rv = codes.size();
		codes.add(__c);
		return rv;
	}
	
	/**
	 * Adds a method to the namespace.
	 *
	 * @param __m The method to add.
	 * @return The index where the method was added.
	 * @throws NullPointerException On null arguments.
	 * @since 2016/09/14
	 */
	int __addMethod(__Method__ __m)
		throws NullPointerException
	{
		// Check
		if (__m == null)
			throw new NullPointerException("NARG");
		
		// Add to the end
		List<__Method__> methods = this._methods;
		int rv = methods.size();
		methods.add(__m);
		return rv;
	}
	
	/**
	 * Places the specified interfaces into the interface table and returns
	 * the basic index.
	 *
	 * @param __i The interfaces to load.
	 * @return The index of the starting index in the table.
	 * @throws NullPointerException On null arguments.
	 * @since 2016/09/14
	 */
	int __interfaces(ClassNameSymbol[] __i)
		throws NullPointerException
	{
		// Check
		if (__i == null)
			throw new NullPointerException("NARG");
		
		// Get base index
		List<__Interface__> interfaces = this._interfaces;
		int basedx = interfaces.size();
		
		// Add to the interface table
		BasicConstantPool pool = this._pool;
		int n = __i.length;
		for (int i = 0; i < n; i++)
			interfaces.add(new __Interface__(pool.addClassName(__i[i])));
		
		// Return base index
		return basedx;
	}
}

