package spring.errorhandler;

import org.springframework.context.MessageSource;
import org.springframework.context.NoSuchMessageException;
import org.springframework.context.i18n.LocaleContextHolder;
import org.springframework.validation.FieldError;

public class MessageHelper {
    public static String UNKNOWN_ERROR_MESSAGE_KEY = "error-handler.unknown.error.message";
    public static String INVALID_REQUEST_MESSAGE_KEY = "error-handler.invalid.request.message";
    public static String INVALID_FIELD_MESSAGE_KEY = "api.error.invalid.%s.message";

    private MessageSource messageSource;

    public MessageHelper(MessageSource messageSource) {
        this.messageSource = messageSource;
    }

    public String getMessage(FieldError error) {
        return getMessage(error.getDefaultMessage(), error.getArguments());
    }

    public String getMessage(String key) {
        return getMessage(key, null);
    }

    public String getMessage(String messageKey, Object[] arguments) {
        try {
            return messageSource.getMessage(messageKey, arguments, LocaleContextHolder.getLocale());
        } catch (NoSuchMessageException e) {
            return messageKey;
        }
    }
}
