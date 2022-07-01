package com.Concordia.Core;

import java.io.IOException;

public class RepException extends IOException {
    public RepException(String errorMessage,Throwable err){
        super(errorMessage,err);
    }
}
