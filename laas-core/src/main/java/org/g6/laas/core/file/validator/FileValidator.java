package org.g6.laas.core.file.validator;


import org.g6.laas.core.file.ILogFile;


public interface FileValidator {

    boolean validate(ILogFile file);

}
