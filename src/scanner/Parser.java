package scanner;

import java.util.ArrayList;

/**
 *
 * @author Mario
 */
public class Parser
{
    private ArrayList<Token> tokens;

    private int next = 0;
    private Token currentToken;

    private String output = "";

    public String getOutput()
    {
        return output;
    }

    public void parse(ArrayList<Token> tokens)
    {
        this.tokens = tokens;
        currentToken = tokens.get(next++);
        prod();
    }

    private boolean checkToken(int tokenType)
    {
        if (currentToken.getType() == tokenType)
        {
            output = String.format("%s%s", output, currentToken.getData());
            currentToken = tokens.get(next++);
            return true;
        }

        return false;
    }

    private boolean prod()
    {
        return checkToken(Token.Type.VAR) && checkToken(Token.Type.DEF) && expr() && checkToken(Token.Type.END);
    }

    private boolean expr()
    {
        return expr1() || expr2();
    }

    private boolean expr1()
    {
        return expr() && checkToken(Token.Type.OR) && term();
    }

    private boolean expr2()
    {
        return term();
    }

    private boolean term()
    {
        return term1() || term2();
    }

    private boolean term1()
    {
        return term() && checkToken(Token.Type.AND) && fact();
    }

    private boolean term2()
    {
        return fact();
    }

    private boolean fact()
    {
        return fact1() || fact2() || fact3();
    }

    private boolean fact1()
    {
        return checkToken(Token.Type.LBC) && expr() && checkToken(Token.Type.RBC);
    }

    private boolean fact2()
    {
        return checkToken(Token.Type.LBK) && expr() && checkToken(Token.Type.RBK);
    }

    private boolean fact3()
    {
        return prim();
    }

    private boolean prim()
    {
        return prim1() || prim2() || prim3();
    }

    private boolean prim1()
    {
        return checkToken(Token.Type.LPS) && expr() && checkToken(Token.Type.RPS);
    }

    private boolean prim2()
    {
        return checkToken(Token.Type.VAR);
    }

    private boolean prim3()
    {
        return checkToken(Token.Type.TERML);
    }
}
