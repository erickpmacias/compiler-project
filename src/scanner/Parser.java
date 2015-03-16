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

        currentToken = tokens.get(next++);

        return false;
    }

    private boolean prod()
    {
        return checkToken(Token.Type.VAR) && checkToken(Token.Type.DEF) && expr() && checkToken(Token.Type.END);
    }

    private boolean expr()
    {
        int save = next - 1;
        if (expr2() == true)
        {
            return true;
        }
        else
        {
            next = save;
            currentToken = tokens.get(next++);
            return expr1();
        }
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
        int save = next - 1;
        if (term2() == true)
        {
            return true;
        }
        else
        {
            next = save;
            currentToken = tokens.get(next++);
            return term1();
        }
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
        int save = next - 1;
        if (fact3() == true)
        {
            return true;
        }
        else
        {
            next = save;
            currentToken = tokens.get(next++);
            if (fact2() == true)
            {
                return true;
            }
            else
            {
                next = save;
                currentToken = tokens.get(next++);
                return fact1();
            }
        }
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
        int save = next - 1;
        if  (prim3() == true)
        {
            return true;
        }
        else
        {
            next = save;
            currentToken = tokens.get(next++);
            if (prim2() == true)
            {
                return true;
            }
            else
            {
                next = save;
                currentToken = tokens.get(next++);
                return prim1();
            }
        }
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
