package org.subethamail.smtp.internal.command;

import org.subethamail.smtp.AuthenticationHandlerFactory;
import org.subethamail.smtp.DropConnectionException;
import org.subethamail.smtp.internal.server.BaseCommand;
import org.subethamail.smtp.internal.util.TextUtils;
import org.subethamail.smtp.server.Session;

import java.io.IOException;
import java.util.HashMap;
import java.util.List;
import java.util.Optional;

/**
 * @author VEP
 * http://www.postfix.org/XFORWARD_README.html
 */
public final class XforwardCommand extends BaseCommand
{
    public XforwardCommand()
    {
        super("XFORWARD", "The postfix xforward command.");
    }

    @Override
    public void execute(String commandString, Session sess) throws IOException, DropConnectionException {
        if (sess.isMailTransactionInProgress())
        {
            sess.sendResponse("503 Mail transaction in progress");
            return;
        }

        String[] args = getArgs(commandString);
        if (args.length < 2)
        {
            sess.sendResponse("501 Bad command parameter syntax");
            return;
        }

        if(sess.getXattributes() == null){
            sess.setXattributes(new HashMap<>());
        }

        for (int i = 1; i < args.length; i++) {
            String[] attribute = args[i].split("=");
            if(attribute.length != 2){
                sess.sendResponse("501 Bad command parameter syntax");
                return;
            }
            sess.getXattributes().put(attribute[0], attribute[1]);
        }

        sess.sendResponse("250 Ok");
    }
}
