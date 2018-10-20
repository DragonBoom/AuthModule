package indi.auth.restlet;

import java.io.IOException;
import java.io.Serializable;
import java.util.List;

import org.restlet.data.MediaType;
import org.restlet.engine.converter.ConverterHelper;
import org.restlet.engine.resource.VariantInfo;
import org.restlet.representation.Representation;
import org.restlet.representation.StringRepresentation;
import org.restlet.representation.Variant;
import org.restlet.resource.Resource;

import indi.core.util.ObjectMapperUtils;

public class JsonConverter extends ConverterHelper {

    @Override
    public List<Class<?>> getObjectClasses(Variant source) {
        List<Class<?>> result = null;
        result = addObjectClass(result, String.class);
        return result;
    }

    private static final VariantInfo JSON = new VariantInfo(MediaType.APPLICATION_JSON);

    @Override
    public List<VariantInfo> getVariants(Class<?> source) throws IOException {
        List<VariantInfo> result = null;
        if (source != null) {
            if (Serializable.class.isAssignableFrom(source)) {
                result = addVariant(result, JSON);
            }
        }
        return null;
    }

    @Override
    public float score(Object source, Variant target, Resource resource) {
        float result = -1.0F;
        if (source instanceof Serializable) {
            return 1F;
        }
        return result;
    }

    @Override
    public <T> float score(Representation source, Class<T> target, Resource resource) {
        float result = -1.0F;
        if (Serializable.class.isAssignableFrom(target)) {
            result = 1F;
        }
        return result;
    }

    @Override
    public <T> T toObject(Representation source, Class<T> target, Resource resource) throws IOException {
        // 必须手动抛出异常，否则异常会被吞掉
        try {
            return ObjectMapperUtils.getMapper().readValue(resource.getRequestEntity().getText(), target);
        } catch (Exception e) {
            e.printStackTrace();
            throw e;
        }
    }

    @Override
    public Representation toRepresentation(Object source, Variant target, Resource resource)
            throws IOException {
        // 手动抛出异常，否则异常会被吞掉
        try {
            return new StringRepresentation(ObjectMapperUtils.getMapper().writeValueAsString(source));
        } catch (Exception e) {
            throw e;
        }
    }
}
