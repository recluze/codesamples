import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;

import org.antlr.runtime.*;
import org.antlr.runtime.tree.CommonTree;
import org.antlr.runtime.tree.CommonTreeNodeStream;

public class Test {
	public static void main(String args[]) throws Exception {

		File inputFile = new File(args[0]);
		FileInputStream fis = new FileInputStream(inputFile);
		// System.out.println(fis.read());

		ANTLRInputStream input = new ANTLRInputStream(fis);

		ExprLexer lexer = new ExprLexer(input);

		CommonTokenStream tokens = new CommonTokenStream(lexer);

		ExprParser parser = new ExprParser(tokens);

		ExprParser.prog_return r = parser.prog();

		CommonTree t = (CommonTree) r.getTree();

		CommonTreeNodeStream nodes = new CommonTreeNodeStream(t);
		Eval walker = new Eval(nodes);
		walker.prog();

	}
}
