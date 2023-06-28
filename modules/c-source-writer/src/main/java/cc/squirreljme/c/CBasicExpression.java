package cc.squirreljme.c;

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
}
