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
	/** The actual file. */
	final CFile _file;
	
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
			this._file = ((CFile)__writer);
		else if (__writer instanceof __CFileProxy__)
			this._file = ((__CFileProxy__)__writer)._file;
		else
			throw new ClassCastException("CCEE");
		
		// This should not occur, hopefully...
		if (this._file == null)
			throw new NullPointerException("GCGC");
	}
	
	/**
	 * {@inheritDoc}
	 * @since 2023/06/04
	 */
	@Override
	public CSourceWriter array(Object... __values)
		throws IOException
	{
		this.__file().array(__values);
		return this;
	}
	
	/**
	 * {@inheritDoc}
	 * @since 2023/06/04
	 */
	@Override
	public CSourceWriter array(boolean... __values)
		throws IOException
	{
		this.__file().array(__values);
		return this;
	}
	
	/**
	 * {@inheritDoc}
	 * @since 2023/06/04
	 */
	@Override
	public CSourceWriter array(byte... __values)
		throws IOException
	{
		this.__file().array(__values);
		return this;
	}
	
	/**
	 * {@inheritDoc}
	 * @since 2023/06/04
	 */
	@Override
	public CSourceWriter array(short... __values)
		throws IOException
	{
		this.__file().array(__values);
		return this;
	}
	
	/**
	 * {@inheritDoc}
	 * @since 2023/06/04
	 */
	@Override
	public CSourceWriter array(char... __values)
		throws IOException
	{
		this.__file().array(__values);
		return this;
	}
	
	/**
	 * {@inheritDoc}
	 * @since 2023/06/04
	 */
	@Override
	public CSourceWriter array(int... __values)
		throws IOException
	{
		this.__file().array(__values);
		return this;
	}
	
	/**
	 * {@inheritDoc}
	 * @since 2023/06/04
	 */
	@Override
	public CSourceWriter array(long... __values)
		throws IOException
	{
		this.__file().array(__values);
		return this;
	}
	
	/**
	 * {@inheritDoc}
	 * @since 2023/06/04
	 */
	@Override
	public CSourceWriter array(float... __values)
		throws IOException
	{
		this.__file().array(__values);
		return this;
	}
	
	/**
	 * {@inheritDoc}
	 * @since 2023/06/04
	 */
	@Override
	public CSourceWriter array(double... __values)
		throws IOException
	{
		this.__file().array(__values);
		return this;
	}
	
	/**
	 * {@inheritDoc}
	 * @since 2023/06/04
	 */
	@Override
	public CSourceWriter array(List<?> __values)
		throws IOException
	{
		this.__file().array(__values);
		return this;
	}
	
	/**
	 * {@inheritDoc}
	 * @since 2023/06/04
	 */
	@Override
	public CSourceWriter character(char __c)
		throws IOException
	{
		this.__file().character(__c);
		return this;
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
		this.__file().declare(__function);
		return this;
	}
	
	/**
	 * {@inheritDoc}
	 * @since 2023/06/19
	 */
	@Override
	public CSourceWriter declare(CVariable __var)
		throws IOException, NullPointerException
	{
		this.__file().declare(__var);
		return this;
	}
	
	/**
	 * {@inheritDoc}
	 * @since 2023/06/19
	 */
	@Override
	public <B extends CBlock> B define(Class<B> __blockType, CVariable __var)
		throws IOException, NullPointerException
	{
		return this.__file().define(__blockType, __var);
	}
	
	/**
	 * {@inheritDoc}
	 * @since 2023/06/04
	 */
	@Override
	public CSourceWriter define(CStructType __what)
		throws IOException, NullPointerException
	{
		this.__file().define(__what);
		return this;
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
	 * @since 2023/06/24
	 */
	@Override
	public CSourceWriter expression(CExpression __expression)
		throws IOException, NullPointerException
	{
		this.__file().expression(__expression);
		return this;
	}
	
	/**
	 * {@inheritDoc}
	 * @since 2023/06/04
	 */
	@Override
	public CSourceWriter freshLine()
		throws IOException
	{
		this.__file().freshLine();
		return this;
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
		this.__file().functionCall(__function, __args);
		return this;
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
		this.__file().functionCall(__function.function(), __args);
		return this;
	}
	
	/**
	 * {@inheritDoc}
	 * @since 2023/07/15
	 */
	@Override
	public CSourceWriter gotoLabel(String __target)
		throws IOException, NullPointerException
	{
		this.__file().gotoLabel(__target);
		return this;
	}
	
	/**
	 * {@inheritDoc}
	 * @since 2023/07/15
	 */
	@Override
	public CSourceWriter gotoLabel(CIdentifier __target)
		throws IOException, NullPointerException
	{
		this.__file().gotoLabel(__target);
		return this;
	}
	
	/**
	 * {@inheritDoc}
	 * @since 2023/07/15
	 */
	@Override
	public CSourceWriter indent(int __by)
		throws IOException
	{
		this.__file().indent(__by);
		return this;
	}
	
	/**
	 * {@inheritDoc}
	 * @since 2023/07/15
	 */
	@Override
	public CSourceWriter label(String __label)
		throws IOException, NullPointerException
	{
		this.__file().label(__label);
		return this;
	}
	
	/**
	 * {@inheritDoc}
	 * @since 2023/07/15
	 */
	@Override
	public CSourceWriter label(CIdentifier __label)
		throws IOException, NullPointerException
	{
		this.__file().label(__label);
		return this;
	}
	
	/**
	 * {@inheritDoc}
	 * @since 2023/06/04
	 */
	@Override
	public CSourceWriter number(Number __number)
		throws IOException, NullPointerException
	{
		this.__file().number(__number);
		return this;
	}
	
	/**
	 * {@inheritDoc}
	 * @since 2023/06/04
	 */
	@Override
	public CSourceWriter number(CNumberType __type, Number __number)
		throws IOException, NullPointerException
	{
		this.__file().number(__type, __number);
		return this;
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
		this.__file().preprocessorDefine(__symbol, __expression);
		return this;
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
		this.__file().preprocessorInclude(__fileName);
		return this;
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
		this.__file().preprocessorLine(__directive, __tokens);
		return this;
	}
	
	/**
	 * {@inheritDoc}
	 * @since 2023/06/04
	 */
	@Override
	public CSourceWriter preprocessorUndefine(CIdentifier __symbol)
		throws IOException, NullPointerException
	{
		this.__file().preprocessorUndefine(__symbol);
		return this;
	}
	
	/**
	 * {@inheritDoc}
	 * @since 2023/06/04
	 */
	@Override
	public CSourceWriter returnValue(CExpression __expression)
		throws IOException
	{
		this.__file().returnValue(__expression);
		return this;
	}
	
	/**
	 * {@inheritDoc}
	 * @since 2023/06/04
	 */
	@Override
	public CSourceWriter surround(String __prefix, Object... __tokens)
		throws IOException
	{
		this.__file().surround(__prefix, __tokens);
		return this;
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
		this.__file().surroundDelimited(__prefix, __delim, __tokens);
		return this;
	}
	
	/**
	 * {@inheritDoc}
	 * @since 2023/06/04
	 */
	@Override
	public CSourceWriter token(CharSequence __token)
		throws IllegalArgumentException, IOException, NullPointerException
	{
		this.__file().token(__token);
		return this;
	}
	
	/**
	 * {@inheritDoc}
	 * @since 2023/06/23
	 */
	@Override
	public CSourceWriter token(CharSequence __token, boolean __forceNewline)
		throws IllegalArgumentException, IOException, NullPointerException
	{
		this.__file().token(__token, __forceNewline);
		return this;
	}
	
	/**
	 * {@inheritDoc}
	 * @since 2023/06/04
	 */
	@Override
	public CSourceWriter token(Object __token)
		throws IllegalArgumentException, IOException
	{
		this.__file().token(__token);
		return this;
	}
	
	/**
	 * {@inheritDoc}
	 * @since 2023/06/04
	 */
	@Override
	public CSourceWriter tokens(Object... __tokens)
		throws IOException
	{
		this.__file().tokens(__tokens);
		return this;
	}
	
	/**
	 * {@inheritDoc}
	 * @since 2023/06/19
	 */
	@Override
	public CSourceWriter variableSet(CVariable __var, CExpression __value)
		throws IllegalArgumentException, IOException, NullPointerException
	{
		this.__file().variableSet(__var, __value);
		return this;
	}
	
	/**
	 * {@inheritDoc}
	 * @since 2023/07/03
	 */
	@Override
	public CSourceWriter variableSet(CExpression __var, CExpression __value)
		throws IllegalArgumentException, IOException, NullPointerException
	{
		this.__file().variableSet(__var, __value);
		return this;
	}
	
	/**
	 * {@inheritDoc}
	 * @since 2023/07/04
	 */
	@Override
	public CSourceWriter variableSetViaFunction(CExpression __var,
		CFunctionType __function, CExpression... __args)
		throws IllegalArgumentException, IOException, NullPointerException
	{
		this.__file().variableSetViaFunction(__var, __function, __args);
		return this;
	}
	
	/**
	 * {@inheritDoc}
	 * @since 2023/07/04
	 */
	@Override
	public CSourceWriter variableSetViaFunction(CExpression __var,
		CFunctionProvider __function, CExpression... __args)
		throws IllegalArgumentException, IOException, NullPointerException
	{
		this.__file().variableSetViaFunction(__var, __function, __args);
		return this;
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
		return this._file;
	}
}
