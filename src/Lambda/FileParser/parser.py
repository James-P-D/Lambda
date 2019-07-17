import sys
import fileinput

class stringparser(object):
    """description of class"""

    LET = "let"
    LAMBDA = "lambda"
    ASSIGNMENT = "="
    PERIOD = "."

    PLUS = "+"
    MINUS = "-"
    DIVIDE = "/"
    MULTIPLY = "*"

    EQUAL = "=="
    NOT_EQUAL = "!="
    AND = "and"
    OR = "or"
    NOT = "not"

    IF = "if"
    THEN = "then"
    ELSE = "else"

    SPACE = " "
    NEW_LINE = "\n"
    CARIIAGE_RETURN = "\r"
    TAB = "\t"

    def function(self):
        print("created parser")

    def parse(self, filename):
        try:
            f = open(filename, "r")
            allLines = f.readlines()
            tokens = []
            for line in allLines:
                parsedLine = line.replace(self.NEW_LINE, self.SPACE).\
                    replace(self.CARIIAGE_RETURN, self.SPACE).\
                    replace(self.TAB, self.SPACE).\
                    split(self.SPACE);
                for token in parsedLine:
                    if len(token) > 0:
                        # TODO: Need more parsing here! Currently "a+5" will parse as single token, not three separate ones
                        tokens.append(token);

            print(tokens)
        except IOError:
            print("Unable to read from file %s" % filename)
            sys.exit(1)

