package scanner;

import java.util.ArrayList;

/*
 * #####################################
 * ### Elimination of Left-Recursion ###
 * #####################################
 *
 * # 3 = empty | nothing | true
 *
 * # ~~~
 * conj  ->  conj | prod
 *
 * conj  ->  prod conj'
 * conj' ->  conj conj' | 3
 *
 * # ~~~
 * expr  ->  expr ALT term | term
 *
 * expr  ->  term expr'
 * expr' ->  ALT term expr' | 3
 *
 * # ~~~
 * term  ->  term AND fact | fact
 *
 * term  ->  fact term'
 * term' ->  AND fact term' | 3
 */

/**
 * Analizador sintactico.
 *
 * Analiza y verifica que el grupo de tokens sea valido
 * en base a la siguiente semantica del lenguaje.
 *
 * <ul>
 *     <li>prog - conj .</li>
 *     <li>conj - conj | prod</li>
 *     <li>prod - var DEF expr;</li>
 *     <li>expr - expr ALT term | term</li>
 *     <li>term - term & fact | fact</li>
 *     <li>fact - {expr} | [expr] | prim</li>
 *     <li>prim - (expr) | var | term</li>
 * </ul>
 *
 * @author Mario
 * @version 1.0
 * @since 16/03/15
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

    public boolean parse(ArrayList<Token> tokens)
    {
        this.tokens = tokens;
        currentToken = tokens.get(next++);
        return prog();
    }

    private boolean checkToken(int tokenType)
    {
        if (currentToken.getType() == tokenType)
        {
            output = String.format("%s%s", output, currentToken.getData());
            if (next + 1 <= tokens.size())
            {
                currentToken = tokens.get(next++);
            }
            return true;
        }

        return false;
    }

    private boolean prog()
    {
        return conj() && checkToken(Token.Type.EOF);
    }

    private boolean conj()
    {
        return prod() && conjp();
    }

    private boolean conjp()
    {
        return (conj() && conjp()) || true;
    }

    private boolean prod()
    {
        return
            checkToken(Token.Type.VAR)
         && checkToken(Token.Type.DEF)
         && expr()
         && checkToken(Token.Type.END);
    }

    private boolean expr()
    {
        return term() && exprp();
    }

    private boolean exprp()
    {
        return (checkToken(Token.Type.OR) && term() && exprp()) || true;
    }

    private boolean term()
    {
        return fact() && termp();
    }

    private boolean termp()
    {
        return (checkToken(Token.Type.AND) && fact() && termp()) || true;
    }

    private boolean fact()
    {
        return fact1() || fact2() || fact3();
    }

    private boolean fact1()
    {
        return
            checkToken(Token.Type.LBC) && expr() && checkToken(Token.Type.RBC);
    }

    private boolean fact2()
    {
        return
            checkToken(Token.Type.LBK) && expr() && checkToken(Token.Type.RBK);
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
        return
            checkToken(Token.Type.LPS) && expr() && checkToken(Token.Type.RPS);
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
