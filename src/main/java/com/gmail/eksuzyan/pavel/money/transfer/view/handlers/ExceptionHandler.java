package com.gmail.eksuzyan.pavel.money.transfer.view.handlers;

import com.gmail.eksuzyan.pavel.money.transfer.ctrl.exceptions.BusinessException;

import javax.ws.rs.core.Response;
import javax.ws.rs.ext.ExceptionMapper;

import static javax.ws.rs.core.Response.Status.BAD_REQUEST;
import static javax.ws.rs.core.Response.Status.NO_CONTENT;
import static javax.ws.rs.core.Response.Status.INTERNAL_SERVER_ERROR;

/**
 * @author Pavel Eksuzian.
 * Created: 10/30/2018.
 */
public class ExceptionHandler implements ExceptionMapper<Throwable> {

    @Override
    public Response toResponse(Throwable e) {

        // returns status code 400
        if (e instanceof IllegalArgumentException)
            return Response.status(BAD_REQUEST.getStatusCode(), e.getMessage()).build();

        // returns status code 204
        if (e instanceof BusinessException)
            return Response.status(NO_CONTENT.getStatusCode(), e.getMessage()).build();

        e.printStackTrace();
        // returns status code 500
        return Response.status(INTERNAL_SERVER_ERROR).build();
    }

}
