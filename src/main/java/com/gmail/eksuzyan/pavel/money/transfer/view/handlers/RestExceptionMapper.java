package com.gmail.eksuzyan.pavel.money.transfer.view.handlers;

import com.gmail.eksuzyan.pavel.money.transfer.ctrl.exceptions.BusinessException;
import com.gmail.eksuzyan.pavel.money.transfer.ctrl.exceptions.custom.AccountExistsException;
import com.gmail.eksuzyan.pavel.money.transfer.ctrl.exceptions.custom.NotEnoughMoneyException;
import com.gmail.eksuzyan.pavel.money.transfer.ctrl.exceptions.custom.NotFoundAccountException;

import javax.ws.rs.core.Response;
import javax.ws.rs.ext.ExceptionMapper;
import java.util.HashMap;
import java.util.Map;

import static java.util.Collections.unmodifiableMap;
import static javax.ws.rs.core.Response.Status.*;

/**
 * @author Pavel Eksuzian.
 *         Created: 10/30/2018.
 */
public class RestExceptionMapper implements ExceptionMapper<Throwable> {

    private static final Map<Class<? extends BusinessException>, Response.Status> STATUSES = unmodifiableMap(
            new HashMap<Class<? extends BusinessException>, Response.Status>() {{
                put(AccountExistsException.class, ACCEPTED);
                put(NotFoundAccountException.class, NO_CONTENT);
                put(NotEnoughMoneyException.class, NO_CONTENT);
            }});

    @Override
    public Response toResponse(Throwable e) {

        if (e instanceof IllegalArgumentException)
            return Response.status(BAD_REQUEST.getStatusCode(), e.getMessage()).build();

        if (e instanceof BusinessException) {
            Response.Status status = STATUSES.getOrDefault(e.getClass(), NOT_IMPLEMENTED);
            return Response.status(status.getStatusCode(), e.getMessage()).build();
        }

        e.printStackTrace();
        return Response.status(INTERNAL_SERVER_ERROR).build();
    }

}
