SRC=./src
CLASS=./classes
SOURCE=$(shell find $(SRC) -name '*.java')
CLASSES=$(subst .java,.class,$(SOURCE))

test:
	javac -d $(CLASS) -sourcepath $(SRC) -cp $(CLASS) $(SRC)/Test/main.java

multithread:
	javac -d $(CLASS) -sourcepath $(SRC) -cp $(CLASS) $(SRC)/multithread/Test.java

all: $(CLASSES)

$(CLASSES): %.class: %.java
	javac -d $(CLASS) -sourcepath $(SRC) -cp $(CLASS) $<

clean:
	# rm -rf bin/*
	find $(CLASS) -name '*'.class -exec rm -f {} ';'

# end
