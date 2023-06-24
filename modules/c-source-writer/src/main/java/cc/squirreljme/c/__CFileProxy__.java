// -*- Mode: Java; indent-tabs-mode: t; tab-width: 4 -*-
// ---------------------------------------------------------------------------
// Multi-Phasic Applications: SquirrelJME
//     Copyright (C) Stephanie Gawroriski <xer@multiphasicapps.net>
// ---------------------------------------------------------------------------
// SquirrelJME is under the Mozilla Public License Version 2.0.
// See license.mkd for licensing and copyright information.
// ---------------------------------------------------------------------------

package cc.squirreljme.c;

import cc.squirreljme.c.std.CFunctionProvider;
import cc.squirreljme.runtime.cldc.debug.Debugging;
import java.io.IOException;
import java.lang.ref.Reference;
import java.util.List;

/**
 * Proxy for {@link CFile}.
 *
 * @since 2023/06/04
 */
@SuppressWarnings("resource")
abstract class __CFileProxy__
	implements CSourceWriter
{
	/** The file to proxy writes to. */
	final Reference<? extends CFile> _fileRef;
	
	/**
	 * Initializes the proxy.
	 * 
	 * @param __writer The file to proxy to.
	 * @throws NullPointerException On null arguments.
	 * @since 2023/06/04
	 */
	__CFileProxy__(CSourceWriter __writer)
		throws NullPointerException
	{
		if (__writer == null)
			throw new NullPointerException("NARG");
		
		// Try to get the original file
		if (__writer instanceof CFile)
			this._fileRef = ((CFile)__writer)._fileRef;
		else if (__writer instanceof __CFileProxy__)
			this._fileRef = ((__CFileProxy__)__writer)._fileRef;
		else
			throw new ClassCastException("CCEE");
	}
	
	/**
	 * {@inheritDoc}
	 * @since 2023/06/04
	 */
	@Override
	public CSourceWriter array(Object... __values)
		throws IOException
	{
		return this.__file().array(__values);
	}
	
	/**
	 * {@inheritDoc}
	 * @since 2023/06/04
	 */
	@Override
	public CSourceWriter array(boolean... __values)
		throws IOException
	{
		return this.__file().array(__values);
	}
	
	/**
	 * {@inheritDoc}
	 * @since 2023/06/04
	 */
	@Override
	public CSourceWriter array(byte... __values)
		throws IOException
	{
		return this.__file().array(__values);
	}
	
	/**
	 * {@inheritDoc}
	 * @since 2023/06/04
	 */
	@Override
	public CSourceWriter array(short... __values)
		throws IOException
	{
		return this.__file().array(__values);
	}
	
	/**
	 * {@inheritDoc}
	 * @since 2023/06/04
	 */
	@Override
	public CSourceWriter array(char... __values)
		throws IOException
	{
		return this.__file().array(__values);
	}
	
	/**
	 * {@inheritDoc}
	 * @since 2023/06/04
	 */
	@Override
	public CSourceWriter array(int... __values)
		throws IOException
	{
		return this.__file().array(__values);
	}
	
	/**
	 * {@inheritDoc}
	 * @since 2023/06/04
	 */
	@Override
	public CSourceWriter array(long... __values)
		throws IOException
	{
		return this.__file().array(__values);
	}
	
	/**
	 * {@inheritDoc}
	 * @since 2023/06/04
	 */
	@Override
	public CSourceWriter array(float... __values)
		throws IOException
	{
		return this.__file().array(__values);
	}
	
	/**
	 * {@inheritDoc}
	 * @since 2023/06/04
	 */
	@Override
	public CSourceWriter array(double... __values)
		throws IOException
	{
		return this.__file().array(__values);
	}
	
	/**
	 * {@inheritDoc}
	 * @since 2023/06/04
	 */
	@Override
	public CSourceWriter array(List<?> __values)
		throws IOException
	{
		return this.__file().array(__values);
	}
	
	/**
	 * {@inheritDoc}
	 * @since 2023/06/04
	 */
	@Override
	public CSourceWriter character(char __c)
		throws IOException
	{
		return this.__file().character(__c);
	}
	
	/**
	 * {@inheritDoc}
	 * @since 2023/06/04
	 */
	@Override
	public CBlock curly()
		throws IOException
	{
		return this.__file().curly();
	}
	
	/**
	 * {@inheritDoc}
	 *
	 * @since 2023/06/04
	 */
	@Override
	public CSourceWriter declare(CFunctionType __function)
		throws IOException, NullPointerException
	{
		return this.__file().declare(__function);
	}
	
	/**
	 * {@inheritDoc}
	 * @since 2023/06/19
	 */
	@Override
	public CSourceWriter declare(CVariable __var)
		throws IOException, NullPointerException
	{
		return this.__file().declare(__var);
	}
	
	/**
	 * {@inheritDoc}
	 * @since 2023/06/19
	 */
	@Override
	public <B extends CBlock> B declare(Class<B> __blockType, CVariable __var)
		throws IOException, NullPointerException
	{
		return this.__file().declare(__blockType, __var);
	}
	
	/**
	 * {@inheritDoc}
	 * @since 2023/06/04
	 */
	@Override
	public CSourceWriter define(CStructType __what)
		throws IOException, NullPointerException
	{
		return this.__file().define(__what);
	}
	
	/**
	 * {@inheritDoc}
	 * @since 2023/06/24
	 */
	@Override
	public CSourceWriter expression(CExpression __expression)
		throws IOException, NullPointerException
	{
		return this.__file().expression(__expression);
	}
	
	/**
	 * {@inheritDoc}
	 * @since 2023/06/04
	 */
	@Override
	public CSourceWriter freshLine()
		throws IOException
	{
		return this.__file().freshLine();
	}
	
	/**
	 * {@inheritDoc}
	 * @since 2023/06/04
	 */
	@Override
	public CSourceWriter functionCall(
		CFunctionType __function, CExpression... __args)
		throws IllegalArgumentException, IOException, NullPointerException
	{
		return this.__file().functionCall(__function, __args);
	}
	
	/**
	 * {@inheritDoc}
	 * @since 2023/06/24
	 */
	@Override
	public CSourceWriter functionCall(CFunctionProvider __function,
		CExpression... __args)
		throws IOException, NullPointerException
	{
		return this.__file().functionCall(__function.function(), __args);
	}
	
	/**
	 * {@inheritDoc}
	 * @since 2023/06/04
	 */
	@Override
	public CFunctionBlock define(CFunctionType __function)
		throws IOException, NullPointerException
	{
		return this.__file().define(__function);
	}
	
	/**
	 * {@inheritDoc}
	 * @since 2023/06/04
	 */
	@Override
	public CSourceWriter number(Number __number)
		throws IOException, NullPointerException
	{
		return this.__file().number(__number);
	}
	
	/**
	 * {@inheritDoc}
	 * @since 2023/06/04
	 */
	@Override
	public CSourceWriter number(CNumberType __type, Number __number)
		throws IOException, NullPointerException
	{
		return this.__file().number(__type, __number);
	}
	
	/**
	 * {@inheritDoc}
	 * @since 2023/06/04
	 */
	@Override
	public CSourceWriter preprocessorDefine(CIdentifier __symbol,
		CExpression __expression)
		throws IOException, NullPointerException
	{
		return this.__file().preprocessorDefine(__symbol, __expression);
	}
	
	/**
	 * {@inheritDoc}
	 * @since 2023/06/04
	 */
	@Override
	public CPPBlock preprocessorIf(CExpression __expression)
		throws IOException, NullPointerException
	{
		return this.__file().preprocessorIf(__expression);
	}
	
	/**
	 * {@inheritDoc}
	 * @since 2023/06/04
	 */
	@Override
	public CSourceWriter preprocessorInclude(CFileName __fileName)
		throws IOException, NullPointerException
	{
		return this.__file().preprocessorInclude(__fileName);
	}
	
	/**
	 * {@inheritDoc}
	 * @since 2023/06/04
	 */
	@Override
	public CSourceWriter preprocessorLine(CPPDirective __directive,
		Object... __tokens)
		throws IOException, NullPointerException
	{
		return this.__file().preprocessorLine(__directive, __tokens);
	}
	
	/**
	 * {@inheritDoc}
	 * @since 2023/06/04
	 */
	@Override
	public CSourceWriter preprocessorUndefine(CIdentifier __symbol)
		throws IOException, NullPointerException
	{
		return this.__file().preprocessorUndefine(__symbol);
	}
	
	/**
	 * {@inheritDoc}
	 * @since 2023/06/04
	 */
	@Override
	public CSourceWriter returnValue(CExpression __expression)
		throws IOException
	{
		return this.__file().returnValue(__expression);
	}
	
	/**
	 * {@inheritDoc}
	 * @since 2023/06/04
	 */
	@Override
	public CSourceWriter surround(String __prefix, Object... __tokens)
		throws IOException
	{
		return this.__file().surround(__prefix, __tokens);
	}
	
	/**
	 * {@inheritDoc}
	 * @since 2023/06/04
	 */
	@Override
	public CSourceWriter surroundDelimited(String __prefix, String __delim,
		Object... __tokens)
		throws IOException, NullPointerException
	{
		return this.__file().surroundDelimited(__prefix, __delim, __tokens);
	}
	
	/**
	 * {@inheritDoc}
	 * @since 2023/06/04
	 */
	@Override
	public CSourceWriter token(CharSequence __token)
		throws IllegalArgumentException, IOException, NullPointerException
	{
		return this.__file().token(__token);
	}
	
	/**
	 * {@inheritDoc}
	 * @since 2023/06/23
	 */
	@Override
	public CSourceWriter token(CharSequence __token, boolean __forceNewline)
		throws IllegalArgumentException, IOException, NullPointerException
	{
		return this.__file().token(__token, __forceNewline);
	}
	
	/**
	 * {@inheritDoc}
	 * @since 2023/06/04
	 */
	@Override
	public CSourceWriter token(Object __token)
		throws IllegalArgumentException, IOException
	{
		return this.__file().token(__token);
	}
	
	/**
	 * {@inheritDoc}
	 * @since 2023/06/04
	 */
	@Override
	public CSourceWriter tokens(Object... __tokens)
		throws IOException
	{
		return this.__file().tokens(__tokens);
	}
	
	/**
	 * {@inheritDoc}
	 * @since 2023/06/19
	 */
	@Override
	public CSourceWriter variableSet(CVariable __var, CExpression __value)
		throws IllegalArgumentException, IOException, NullPointerException
	{
		return this.__file().variableSet(__var, __value);
	}
	
	/**
	 * Returns the used source writer, for inlining.
	 * 
	 * @return The source writer user.
	 * @throws IllegalStateException If the writer was garbage collected.
	 * @since 2023/05/29
	 */
	final CFile __file()
		throws IllegalStateException
	{
		CFile rv = this._fileRef.get();
		if (rv == null)
			throw new IllegalStateException("GCGC");
		return rv;
	}
}
