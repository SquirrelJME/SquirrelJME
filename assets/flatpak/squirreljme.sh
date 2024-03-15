#!/bin/sh

# Figure out what the scaling should be so it is not ultra-tiny
if [ -n "$GDK_SCALE" ]
then
	__scale="$GDK_SCALE"
elif [ -n "$GDK_DPI_SCALE" ]
then
	__scale="$GDK_DPI_SCALE"
elif [ -n "$QT_SCALE_FACTOR" ]
then
	__scale="$QT_SCALE_FACTOR"
elif [ -n "$QT_FONT_DPI" ]
then
	__dpi="$QT_FONT_DPI"
elif [ -n "$ELM_SCALE" ]
then
	__scale="$ELM_SCALE"
elif [ -f "$HOME/.Xresources" ]
then
	__dpi="$(grep 'Xft.dpi' < "$HOME/.Xresources" | cut -d ':' -f 2 |
		tr -d ' ')"
else
	__scale=1
fi

# If a DPI used, map to a scale
if [ -n "$__dpi" ]
then
	__div="$(expr "$__dpi" / 96)"
	__mod="$(expr "$__dpi" % 96)"
	
	# Scale is on the higher end, so round up
	if [ "$__mod" -ge "48" ]
	then
		__scale="$(expr "$__div" + 1)"
	else
		__scale="$__div"
	fi
fi

# If scale is not valid, just default to 1
if [ -z "$__scale" ] || ! [ "$__scale" -ge "-666" ] 2> /dev/null ||
	[ "$__scale" -le "0" ] 2> /dev/null
then
	__scale=1
fi

# Note that only whole numbers are supported... so round up for the user!
if echo "$__scale" | grep '\.' > /dev/null
then
	__int="$(echo "$__scale" | cut -d '.' -f 1)"
	__dec="$(echo "$__scale" | cut -d '.' -f 2)"
	
	# Should this round up? Also consider 1.75 and such...
	if [ "$__dec" -lt "10" ] && [ "$__dec" -ge "5" ]
	then
		__scale="$(expr "$__int" + 1)"
	
	# Duplicate because we do not want to fork!
	elif [ "$__dec" -ge "10" ] && [ "$__dec" -ge "50" ]
	then
		__scale="$(expr "$__int" + 1)"
		
	# Round down
	else
		__scale="$__int"
	fi
fi

# Run SquirrelJME!
/app/jre/bin/java "-Dsun.java2d.uiScale=$__scale" \
	-jar /app/bin/squirreljme-standalone.jar "$@"
exit $?
