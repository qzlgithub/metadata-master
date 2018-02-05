package com.mingdong.core.exception;

public class MetadataHttpException extends MetadataException
{
    public MetadataHttpException()
    {
        super();
    }

    public MetadataHttpException(String message)
    {
        super(message);
    }

    public MetadataHttpException(String message, Throwable cause)
    {
        super(message, cause);
    }
}
