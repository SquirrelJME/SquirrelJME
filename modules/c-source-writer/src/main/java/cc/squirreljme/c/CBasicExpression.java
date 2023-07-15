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
	public static CExpression reference(CExpression __value)
		throws IOException, NullPointerException
	{
		if (__value == null)
			throw new NullPointerException("NARG");
		
		return CExpressionBuilder.builder()
			.reference(__value)
			.build();
	}
}
