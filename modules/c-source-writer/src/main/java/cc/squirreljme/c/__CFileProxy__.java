// -*- Mode: Java; indent-tabs-mode: t; tab-width: 4 -*-
// ---------------------------------------------------------------------------
// Multi-Phasic Applications: SquirrelJME
//     Copyright (C) Stephanie Gawroriski <xer@multiphasicapps.net>
// ---------------------------------------------------------------------------
// SquirrelJME is under the GNU General Public License v3+, or later.
// See license.mkd for licensing and copyright information.
// ---------------------------------------------------------------------------

package cc.squirreljme.c;

import java.io.IOException;
import java.lang.ref.Reference;
import java.lang.ref.WeakReference;
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
	private final Reference<CFile> _file;
	
	/**
	 * Initializes the proxy.
	 * 
	 * @param __file The file to proxy to.
	 * @throws NullPointerException On null arguments.
	 * @since 2023/06/04
	 */
	__CFileProxy__(CFile __file)
		throws NullPointerException
	{
		if (__file == null)
			throw new NullPointerException("NARG");
		
		this._file = new WeakReference<>(__file);
	}
	
	/**
	 * Initializes the proxy.
	 * 
	 * @param __file The file to proxy to.
	 * @throws NullPointerException On null arguments.
	 * @since 2023/06/04
	 */
	__CFileProxy__(Reference<CFile> __file)
		throws NullPointerException
	{
		if (__file == null || __file.get() == null)
			throw new NullPointerException("NARG");
		
		this._file = __file;
	}
	
	/**
	 * {@inheritDoc}
	 * @since 2023/06/04
	 */
	@Override
	public CSourceWriter array(Object... __values)
		throws IOException
	{
		return this.writer().array(__values);
	}
	
	/**
	 * {@inheritDoc}
	 * @since 2023/06/04
	 */
	@Override
	public CSourceWriter array(boolean... __values)
		throws IOException
	{
		return this.writer().array(__values);
	}
	
	/**
	 * {@inheritDoc}
	 * @since 2023/06/04
	 */
	@Override
	public CSourceWriter array(byte... __values)
		throws IOException
	{
		return this.writer().array(__values);
	}
	
	/**
	 * {@inheritDoc}
	 * @since 2023/06/04
	 */
	@Override
	public CSourceWriter array(short... __values)
		throws IOException
	{
		return this.writer().array(__values);
	}
	
	/**
	 * {@inheritDoc}
	 * @since 2023/06/04
	 */
	@Override
	public CSourceWriter array(char... __values)
		throws IOException
	{
		return this.writer().array(__values);
	}
	
	/**
	 * {@inheritDoc}
	 * @since 2023/06/04
	 */
	@Override
	public CSourceWriter array(int... __values)
		throws IOException
	{
		return this.writer().array(__values);
	}
	
	/**
	 * {@inheritDoc}
	 * @since 2023/06/04
	 */
	@Override
	public CSourceWriter array(long... __values)
		throws IOException
	{
		return this.writer().array(__values);
	}
	
	/**
	 * {@inheritDoc}
	 * @since 2023/06/04
	 */
	@Override
	public CSourceWriter array(float... __values)
		throws IOException
	{
		return this.writer().array(__values);
	}
	
	/**
	 * {@inheritDoc}
	 * @since 2023/06/04
	 */
	@Override
	public CSourceWriter array(double... __values)
		throws IOException
	{
		return this.writer().array(__values);
	}
	
	/**
	 * {@inheritDoc}
	 * @since 2023/06/04
	 */
	@Override
	public CSourceWriter array(List<?> __values)
		throws IOException
	{
		return this.writer().array(__values);
	}
	
	/**
	 * {@inheritDoc}
	 * @since 2023/06/04
	 */
	@Override
	public CSourceWriter character(char __c)
		throws IOException
	{
		return this.writer().character(__c);
	}
	
	/**
	 * {@inheritDoc}
	 * @since 2023/06/04
	 */
	@Override
	public CBlock curly()
		throws IOException
	{
		return this.writer().curly();
	}
	
	/**
	 * {@inheritDoc}
	 * @since 2023/06/04
	 */
	@Override
	public CSourceWriter freshLine()
		throws IOException
	{
		return this.writer().freshLine();
	}
	
	/**
	 * {@inheritDoc}
	 * @since 2023/06/04
	 */
	@Override
	public CSourceWriter function(CModifier __modifier, String __name,
		CType __returnVal, CFunctionArgument... __arguments)
		throws IOException, NullPointerException
	{
		return this.writer().function(__modifier, __name, __returnVal,
			__arguments);
	}
	
	/**
	 * {@inheritDoc}
	 * @since 2023/06/04
	 */
	@Override
	public CSourceWriter functionCall(String __function, Object... __args)
		throws IOException, NullPointerException
	{
		return this.writer().functionCall(__function, __args);
	}
	
	/**
	 * {@inheritDoc}
	 * @since 2023/06/04
	 */
	@Override
	public CFunctionBlock functionDefine(CModifier __modifier, String __name,
		CType __returnVal, CFunctionArgument... __arguments)
		throws IOException, NullPointerException
	{
		return this.writer().functionDefine(__modifier, __name, __returnVal,
			__arguments);
	}
	
	/**
	 * {@inheritDoc}
	 * @since 2023/06/04
	 */
	@Override
	public CSourceWriter functionPrototype(CModifier __modifier, String __name,
		CType __returnVal, CFunctionArgument... __arguments)
		throws IOException, NullPointerException
	{
		return this.writer().functionPrototype(__modifier, __name, __returnVal,
			__arguments);
	}
	
	/**
	 * {@inheritDoc}
	 * @since 2023/06/04
	 */
	@Override
	public CSourceWriter number(Number __number)
		throws IOException, NullPointerException
	{
		return this.writer().number(__number);
	}
	
	/**
	 * {@inheritDoc}
	 * @since 2023/06/04
	 */
	@Override
	public CSourceWriter number(CNumberType __type, Number __number)
		throws IOException, NullPointerException
	{
		return this.writer().number(__type, __number);
	}
	
	/**
	 * {@inheritDoc}
	 * @since 2023/06/04
	 */
	@Override
	public CSourceWriter preprocessorDefine(String __symbol,
		Object... __tokens)
		throws IOException, NullPointerException
	{
		return this.writer().preprocessorDefine(__symbol, __tokens);
	}
	
	/**
	 * {@inheritDoc}
	 * @since 2023/06/04
	 */
	@Override
	public CPPBlock preprocessorIf(Object... __condition)
		throws IOException, NullPointerException
	{
		return this.writer().preprocessorIf(__condition);
	}
	
	/**
	 * {@inheritDoc}
	 * @since 2023/06/04
	 */
	@Override
	public CSourceWriter preprocessorInclude(String __fileName)
		throws IOException, NullPointerException
	{
		return this.writer().preprocessorInclude(__fileName);
	}
	
	/**
	 * {@inheritDoc}
	 * @since 2023/06/04
	 */
	@Override
	public CSourceWriter preprocessorLine(String __directive,
		Object... __tokens)
		throws IOException, NullPointerException
	{
		return this.writer().preprocessorLine(__directive, __tokens);
	}
	
	/**
	 * {@inheritDoc}
	 * @since 2023/06/04
	 */
	@Override
	public CSourceWriter preprocessorUndefine(String __symbol)
		throws IOException, NullPointerException
	{
		return this.writer().preprocessorUndefine(__symbol);
	}
	
	/**
	 * {@inheritDoc}
	 * @since 2023/06/04
	 */
	@Override
	public CSourceWriter returnValue(Object... __tokens)
		throws IOException
	{
		return this.writer().returnValue(__tokens);
	}
	
	/**
	 * {@inheritDoc}
	 * @since 2023/06/04
	 */
	@Override
	public CStructVariableBlock structVariableSet(CModifier __modifiers,
		CBasicType __structType, String __structName)
		throws IOException, NullPointerException
	{
		return this.writer().structVariableSet(__modifiers, __structType,
			__structName);
	}
	
	/**
	 * {@inheritDoc}
	 * @since 2023/06/04
	 */
	@Override
	public CSourceWriter surround(String __prefix, Object... __tokens)
		throws IOException
	{
		return this.writer().surround(__prefix, __tokens);
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
		return this.writer().surroundDelimited(__prefix, __delim, __tokens);
	}
	
	/**
	 * {@inheritDoc}
	 * @since 2023/06/04
	 */
	@Override
	public CSourceWriter token(CharSequence __token)
		throws IllegalArgumentException, IOException, NullPointerException
	{
		return this.writer().token(__token);
	}
	
	/**
	 * {@inheritDoc}
	 * @since 2023/06/04
	 */
	@Override
	public CSourceWriter token(Object __token)
		throws IllegalArgumentException, IOException
	{
		return this.writer().token(__token);
	}
	
	/**
	 * {@inheritDoc}
	 * @since 2023/06/04
	 */
	@Override
	public CSourceWriter tokens(Object... __tokens)
		throws IOException
	{
		return this.writer().tokens(__tokens);
	}
	
	/**
	 * {@inheritDoc}
	 * @since 2023/06/04
	 */
	@Override
	public CSourceWriter variableAssign(String __target, Object... __value)
		throws IOException, NullPointerException
	{
		return this.writer().variableAssign(__target, __value);
	}
	
	/**
	 * {@inheritDoc}
	 * @since 2023/06/04
	 */
	@Override
	public CSourceWriter variableDeclare(CModifier __modifier, CType __type,
		String __name)
		throws IOException, NullPointerException
	{
		return this.writer().variableDeclare(__modifier, __type, __name);
	}
	
	/**
	 * {@inheritDoc}
	 * @since 2023/06/04
	 */
	@Override
	public CSourceWriter variableSet(CType __type, String __name,
		String... __valueTokens)
		throws IOException, NullPointerException
	{
		return this.writer().variableSet(__type, __name, __valueTokens);
	}
	
	/**
	 * {@inheritDoc}
	 * @since 2023/06/04
	 */
	@Override
	public CSourceWriter variableSet(CModifier __modifier, CType __type,
		String __name, String... __valueTokens)
		throws IOException, NullPointerException
	{
		return this.writer().variableSet(__modifier, __type, __name,
			__valueTokens);
	}
	
	/**
	 * {@inheritDoc}
	 * @since 2023/06/04
	 */
	@Override
	public CSourceWriter variable(CModifier __modifier, CType __type,
		String __name)
		throws IOException, NullPointerException
	{
		return this.writer().variable(__modifier, __type, __name);
	}
	
	/**
	 * Returns the used source writer, for inlining.
	 * 
	 * @return The source writer user.
	 * @throws IllegalStateException If the writer was garbage collected.
	 * @since 2023/05/29
	 */
	public final CFile writer()
		throws IllegalStateException
	{
		CFile rv = this._file.get();
		if (rv == null)
			throw new IllegalStateException("GCGC");
		return rv;
	}
}
