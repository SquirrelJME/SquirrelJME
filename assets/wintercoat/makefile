
__c := $(wildcard *.c)
__o := $(patsubst %.c, %.o, $(__c))
__h := $(wildcard *.h)

wintercoat:		$(__o)
				$(CC) -o $@ $< $(LDFLAGS)

%.o:			%.c $(__h)
				$(CC) -o $@ -c $^ $(CFLAGS)

clean:			
				rm -f $(__o) wintercoat

