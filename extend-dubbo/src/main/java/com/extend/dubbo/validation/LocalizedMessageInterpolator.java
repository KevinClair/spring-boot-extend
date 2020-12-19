package com.extend.dubbo.validation;

import org.hibernate.validator.messageinterpolation.ResourceBundleMessageInterpolator;
import org.hibernate.validator.spi.resourceloading.ResourceBundleLocator;

import java.util.Locale;

/**
 * LocalizedMessageInterpolator.
 *
 * @author KevinClair
 */
public class LocalizedMessageInterpolator extends ResourceBundleMessageInterpolator {

    private Locale locale;

    public LocalizedMessageInterpolator(ResourceBundleLocator userResourceBundleLocator, Locale locale) {
        super(userResourceBundleLocator);
        this.locale = locale;
    }

    @Override
    public String interpolate(String message, Context context) {
        return super.interpolate(message, context, locale);
    }
}
