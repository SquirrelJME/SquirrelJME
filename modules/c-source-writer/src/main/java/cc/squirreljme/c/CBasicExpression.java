package cc.squirreljme.c;

import java.io.IOException;
import java.util.Arrays;
import java.util.List;
import net.multiphasicapps.collections.UnmodifiableList;

public class CBasicExpression
	implements CExpression
{
	/** The tokens used in the expression. */
	protected final List<String> tokens;
	
	/**
	 * Returns the tokens used for the expression.
	 * 
	 * @param __tokens The tokens used for the expression.
	 * @since 2023/06/24
	 */
	CBasicExpression(String... __tokens)
	{
		this.tokens = UnmodifiableList.of(Arrays.asList(__tokens));
	}
	
	/**
	 * {@inheritDoc}
	 * @since 2023/06/23
	 */
	@Override
	public List<String> tokens()
	{
		return this.tokens;
	}
	
	/**
	 * Creates a byte array expression.
	 *
	 * @param __values Input values.
	 * @return The resultant expression.
	 * @throws IOException On write errors.
	 * @throws NullPointerException On null arguments.
	 * @since 2024/06/08
	 */
	public static CExpression array(byte... __values)
		throws IOException, NullPointerException
	{
		if (__values == null)
			throw new NullPointerException("NARG");
		
		return CExpressionBuilder.builder()
			.array(__values)
			.build();
	}
	
	/**
	 * Creates a short array expression.
	 *
	 * @param __values Input values.
	 * @return The resultant expression.
	 * @throws IOException On write errors.
	 * @throws NullPointerException On null arguments.
	 * @since 2024/06/08
	 */
	public static CExpression array(short... __values)
		throws IOException, NullPointerException
	{
		if (__values == null)
			throw new NullPointerException("NARG");
		
		return CExpressionBuilder.builder()
			.array(__values)
			.build();
	}
	
	/**
	 * Creates an integer array expression.
	 *
	 * @param __values Input values.
	 * @return The resultant expression.
	 * @throws IOException On write errors.
	 * @throws NullPointerException On null arguments.
	 * @since 2024/06/08
	 */
	public static CExpression array(int... __values)
		throws IOException, NullPointerException
	{
		if (__values == null)
			throw new NullPointerException("NARG");
		
		return CExpressionBuilder.builder()
			.array(__values)
			.build();
	}
	
	/**
	 * Returns a number expression.
	 * 
	 * @param __value The value to use.
	 * @return The expression for the number.
	 * @throws IOException On write errors.
	 * @throws NullPointerException On null arguments.
	 * @since 2023/07/16
	 */
	@SuppressWarnings("squirreljme_cSourceWriter_simplify")
	public static CExpression number(Number __value)
		throws IOException, NullPointerException
	{
		if (__value == null)
			throw new NullPointerException("NARG");
	
		return CExpressionBuilder.builder()
			.number(__value)
			.build();
	}
	
	/**
	 * Returns a number expression.
	 * 
	 * @param __type The number type.
	 * @param __value The value to use.
	 * @return The expression for the number.
	 * @throws IOException On write errors.
	 * @throws NullPointerException On null arguments.
	 * @since 2023/08/13
	 */
	@SuppressWarnings("squirreljme_cSourceWriter_simplify")
	public static CExpression number(CNumberType __type, Number __value)
		throws IOException, NullPointerException
	{
		if (__value == null)
			throw new NullPointerException("NARG");
	
		return CExpressionBuilder.builder()
			.number(__type,  __value)
			.build();
	}
	
	/**
	 * Initializes a basic expression.
	 * 
	 * @param __tokens The tokens to use.
	 * @return The expression.
	 * @since 2023/07/04
	 */
	public static CExpression of(String... __tokens)
	{
		return new CBasicExpression(__tokens.clone());
	}
	
	/**
	 * References the given value.
	 * 
	 * @param __value The value to reference.
	 * @return The referenced value.
	 * @throws IOException On write errors.
	 * @throws NullPointerException On null arguments.
	 * @since 2023/07/15
	 */
	@SuppressWarnings("squirreljme_cSourceWriter_simplify")
	public static CExpression reference(CExpression __value)
		throws IOException, NullPointerException
	{
		if (__value == null)
			throw new NullPointerException("NARG");
		
		return CExpressionBuilder.builder()
			.reference(__value)
			.build();
	}
	
	/**
	 * Returns a string expression.
	 * 
	 * @param __value The value to use.
	 * @return The expression for the number.
	 * @throws IOException On write errors.
	 * @throws NullPointerException On null arguments.
	 * @since 2023/07/25
	 */
	@SuppressWarnings("squirreljme_cSourceWriter_simplify")
	public static CExpression string(String __value)
		throws IOException, NullPointerException
	{
		if (__value == null)
			throw new NullPointerException("NARG");
	
		return CExpressionBuilder.builder()
			.string(__value)
			.build();
	}
}
