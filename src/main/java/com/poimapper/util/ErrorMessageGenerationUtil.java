package com.poimapper.util;
import com.poimapper.constants.ErrorCodes;

import java.text.MessageFormat;
import java.util.Locale;
import java.util.ResourceBundle;
import java.util.logging.Logger;

public class ErrorMessageGenerationUtil {
    private static final ResourceBundle errorMessages = ResourceBundle.getBundle("error-message",Locale.US);
    private static final Logger logger = Logger.getLogger(ErrorMessageGenerationUtil.class.getName());
    public static String getErrorMessage(ErrorCodes key, Object... arguments) {
        String pattern = errorMessages.getString(String.valueOf(key));
        return "[" + key + "] : " + MessageFormat.format(pattern, arguments);
    }
}
