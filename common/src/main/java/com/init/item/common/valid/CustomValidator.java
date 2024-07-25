package com.init.item.common.valid;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;
import org.springframework.validation.BeanPropertyBindingResult;
import org.springframework.validation.Errors;
import org.springframework.validation.Validator;
import org.springframework.web.server.ServerWebInputException;

/**
 * 自定义参数校验
 */
@Component
@RequiredArgsConstructor
public class CustomValidator {
    private final Validator validator;

    /**
     * 可以实现Validator，自定义校验规则，此处不做过多展示
     */
    public <T> void validate(T req) {
        Errors errors = new BeanPropertyBindingResult(req, "req");
        validator.validate(req, errors);
        if (errors.hasErrors()) {
            StringBuffer errorStr = new StringBuffer();
            errors.getAllErrors().forEach(it -> {
                errorStr.append(it.getDefaultMessage()).append(";");
            });
            throw new ServerWebInputException(errorStr.toString());
        }
    }
}
