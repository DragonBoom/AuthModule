package indi.auth.restlet.resource;

import java.io.IOException;

import org.restlet.data.Status;
import org.restlet.representation.Representation;
import org.restlet.representation.Variant;
import org.restlet.resource.ServerResource;

import indi.core.rest.result.RestResult;

public class MyResource extends ServerResource {

    @Override
    public Representation toRepresentation(Object source, Variant target) throws IOException {
        Representation result = null;

        if (source != null) {
            if (source instanceof RestResult) {
                RestResult<?> restResult = (RestResult<?>) source;
                // 设置status对应为RestResult的code
                Status status = Status.valueOf(restResult.getCode());
                super.getResponse().setStatus(status);

                // 若RestResult的信息为空，则取Status的描述作为信息
                if (restResult.getMsg().length() == 0) {
                    restResult.setMsg(status.getDescription());
                }
            }

            org.restlet.service.ConverterService cs = getConverterService();
            result = cs.toRepresentation(source, target, this);
        }

        return result;
    }
}
