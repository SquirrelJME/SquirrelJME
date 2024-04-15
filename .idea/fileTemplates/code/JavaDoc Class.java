 * Describe this.
 *
#foreach($param in $RECORD_COMPONENTS)
 * @param $param
#end
#foreach($param in $TYPE_PARAMS)
 * @param <$param>
#end
 * @since $YEAR/$MONTH/$DAY