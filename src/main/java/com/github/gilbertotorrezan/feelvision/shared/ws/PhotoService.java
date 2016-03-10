package com.github.gilbertotorrezan.feelvision.shared.ws;

import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;

import org.fusesource.restygwt.client.MethodCallback;
import org.fusesource.restygwt.client.RestService;

import com.github.gilbertotorrezan.feelvision.shared.model.PhotoAnalysis;
import com.github.gilbertotorrezan.feelvision.shared.model.PhotoAnalysisRequest;

/**
 * @author Gilberto Torrezan Filho
 *
 * @since Mar 4, 2016 4:27:21 PM
 */
@Path("ws/photo")
public interface PhotoService extends RestService {
	
	@POST
    @Produces(MediaType.APPLICATION_JSON)
    void analyze(PhotoAnalysisRequest request, MethodCallback<PhotoAnalysis> callback);

}
