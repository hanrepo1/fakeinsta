package com.hanre.fakeinsta.util;

import com.hanre.fakeinsta.handler.ResponseHandler;
import jakarta.servlet.http.HttpServletRequest;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

public class ResponseUtil {

    public static ResponseEntity<Object> dataFound(String message, Object object, HttpServletRequest request){
        return new ResponseHandler().generateResponse(
                message,
                HttpStatus.OK,
                object,
                null,
                request
        );
    }

    public static ResponseEntity<Object> dataNotFound(String message, HttpServletRequest request){
        return new ResponseHandler().generateResponse(
                message,
                HttpStatus.BAD_REQUEST,
                null,
                "X-01-002",
                request
        );
    }

    public static ResponseEntity<Object> validationFailed(String message, String errorCode, HttpServletRequest request){
        return new ResponseHandler().generateResponse(
                message,
                HttpStatus.BAD_REQUEST,
                null,
                errorCode,
                request
        );
    }

    public static ResponseEntity<Object> dataSaveSuccess(HttpServletRequest request){
        return new ResponseHandler().generateResponse(
                "Data Saved Successfully",
                HttpStatus.CREATED,
                null,
                null,
                request
        );
    }

    public static ResponseEntity<Object> dataUpdateSuccess(HttpServletRequest request){
        return new ResponseHandler().generateResponse(
                "Data Updated Successfully",
                HttpStatus.OK,
                null,
                null,
                request
        );
    }
}
