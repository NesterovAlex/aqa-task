package com.test.framework.util;

import com.test.framework.exceptions.FrameworkRuntimeException;
import org.springframework.core.io.ClassPathResource;
import org.springframework.core.io.Resource;
import org.springframework.core.io.support.PropertiesLoaderUtils;

import java.io.IOException;
import java.util.Properties;

import static com.test.framework.constants.ExceptionConstants.PROPERTY_NOT_FOUND;

public class ResourceUtil {

    public static String getProperty(String property) {
        Resource resource = new ClassPathResource("localhost.properties");
        Properties properties = null;
        try {
            properties = PropertiesLoaderUtils.loadProperties(resource);
        } catch (IOException e) {
            throw new FrameworkRuntimeException(PROPERTY_NOT_FOUND, e);
        }
        return properties.getProperty(property);
    }
}
