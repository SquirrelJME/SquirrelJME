 * Describe this.
 *
#foreach($param in $PARAMS)
 * @param $param
#end
#if($RETURN_TYPE != "void")
 * @return
#end
#foreach($param in $TYPE_PARAMS)
 * @param <$param>
#end
#foreach($exception in $THROWS)
 * @throws $exception
#end
 * @since $YEAR/$MONTH/$DAY