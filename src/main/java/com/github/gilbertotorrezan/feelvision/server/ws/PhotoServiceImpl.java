package com.github.gilbertotorrezan.feelvision.server.ws;

import java.util.Arrays;
import java.util.LinkedList;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;

import javax.ws.rs.BadRequestException;
import javax.ws.rs.InternalServerErrorException;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;

import com.github.gilbertotorrezan.feelvision.shared.model.FaceAnalysis;
import com.github.gilbertotorrezan.feelvision.shared.model.ImagePoint;
import com.github.gilbertotorrezan.feelvision.shared.model.Likelihood;
import com.github.gilbertotorrezan.feelvision.shared.model.PhotoAnalysis;
import com.github.gilbertotorrezan.feelvision.shared.model.PhotoAnalysisRequest;
import com.github.gilbertotorrezan.feelvision.shared.model.TagAnalysis;
import com.google.api.client.googleapis.javanet.GoogleNetHttpTransport;
import com.google.api.client.http.HttpRequestInitializer;
import com.google.api.client.json.JsonFactory;
import com.google.api.client.json.jackson2.JacksonFactory;
import com.google.api.services.vision.v1.Vision;
import com.google.api.services.vision.v1.Vision.Images.Annotate;
import com.google.api.services.vision.v1.VisionScopes;
import com.google.api.services.vision.v1.model.AnnotateImageRequest;
import com.google.api.services.vision.v1.model.AnnotateImageResponse;
import com.google.api.services.vision.v1.model.BatchAnnotateImagesRequest;
import com.google.api.services.vision.v1.model.BatchAnnotateImagesResponse;
import com.google.api.services.vision.v1.model.EntityAnnotation;
import com.google.api.services.vision.v1.model.FaceAnnotation;
import com.google.api.services.vision.v1.model.Feature;
import com.google.api.services.vision.v1.model.Image;
import com.google.api.services.vision.v1.model.Vertex;

/**
 * @author Gilberto Torrezan Filho
 *
 * @since Mar 4, 2016 3:12:34 PM
 */
@Path("ws/photo")
public class PhotoServiceImpl {
	
	private static final Logger logger = Logger.getLogger(PhotoServiceImpl.class.getName());

	@POST
    @Produces(MediaType.APPLICATION_JSON)
    public PhotoAnalysis analyze(PhotoAnalysisRequest request) {
		long time = System.currentTimeMillis();
		logger.info("Creating Vision API request now...");
		try {
			Vision vision = getVisionService();
			AnnotateImageRequest req = new AnnotateImageRequest()
				.setImage(new Image().setContent(request.getDataUrl()))
				.setFeatures(Arrays.asList(
						new Feature().setType("LABEL_DETECTION"), 
						new Feature().setType("FACE_DETECTION")));
			
			Annotate annotate = vision.images().annotate(new BatchAnnotateImagesRequest().setRequests(Arrays.asList(req)));

			BatchAnnotateImagesResponse batchResponse = annotate.execute();
			if (batchResponse.getResponses().size() != 1){
				String msg = "Vision API service error: Invalid image. Wrong response size (" + batchResponse.getResponses().size() + ")";
				logger.log(Level.SEVERE, msg);
				throw new BadRequestException(msg);
			}
			
			AnnotateImageResponse response = batchResponse.getResponses().get(0);
			PhotoAnalysis analysis = new PhotoAnalysis();
			analysis.setFaces(new LinkedList<FaceAnalysis>());
			analysis.setTags(new LinkedList<TagAnalysis>());
			
			List<FaceAnnotation> faceAnnotations = response.getFaceAnnotations();
			if (faceAnnotations != null){
				//converting the objects from the response
				for (FaceAnnotation faceAnnotation : faceAnnotations) {
					FaceAnalysis face = new FaceAnalysis();
					face.setAngerLikelihood(convertLikelihood(faceAnnotation.getAngerLikelihood()));
					face.setBlurredLikelihood(convertLikelihood(faceAnnotation.getBlurredLikelihood()));
					face.setHeadwearLikelihood(convertLikelihood(faceAnnotation.getHeadwearLikelihood()));
					face.setJoyLikelihood(convertLikelihood(faceAnnotation.getJoyLikelihood()));
					face.setSorrowLikelihood(convertLikelihood(faceAnnotation.getSorrowLikelihood()));
					face.setSurpriseLikelihood(convertLikelihood(faceAnnotation.getSurpriseLikelihood()));
					face.setUnderExposedLikelihood(convertLikelihood(faceAnnotation.getUnderExposedLikelihood()));
					face.setDetectionConfidence(faceAnnotation.getDetectionConfidence());
					
					List<ImagePoint> points = new LinkedList<>();
					face.setBoundingPoly(points);
					
					if (faceAnnotation.getBoundingPoly() != null && faceAnnotation.getBoundingPoly().getVertices() != null){
						List<Vertex> vertices = faceAnnotation.getBoundingPoly().getVertices();
						for (Vertex vertex : vertices) {
							ImagePoint point = new ImagePoint();
							point.setX(vertex.getX() == null ? 0 : vertex.getX());
							point.setY(vertex.getY() == null ? 0 : vertex.getY());
							points.add(point);
						}
					}
					analysis.getFaces().add(face);
				}				
			}
			
			List<EntityAnnotation> labelAnnotations = response.getLabelAnnotations();
			if (labelAnnotations != null){
				//converting the objects from the response
				for (EntityAnnotation entityAnnotation : labelAnnotations) {
					TagAnalysis tag = new TagAnalysis();
					tag.setDescription(entityAnnotation.getDescription());
					tag.setScore(entityAnnotation.getScore());
					
					List<ImagePoint> points = new LinkedList<>();
					tag.setBoundingPoly(points);
					
					if (entityAnnotation.getBoundingPoly() != null && entityAnnotation.getBoundingPoly().getVertices() != null){
						List<Vertex> vertices = entityAnnotation.getBoundingPoly().getVertices();
						for (Vertex vertex : vertices) {
							ImagePoint point = new ImagePoint();
							point.setX(vertex.getX());
							point.setY(vertex.getY());
							points.add(point);
						}
					}
					analysis.getTags().add(tag);
				}				
			}
			logger.info("Vision request processed in " + (System.currentTimeMillis() - time) + " ms.");
			return analysis;
		}
		catch (Exception e) {
			logger.log(Level.SEVERE, "Error accessing the Vision API service: " + e, e);
			throw new InternalServerErrorException(e);
		}
	}
	
	private Likelihood convertLikelihood(String text){
		if (text == null){
			return Likelihood.UNKNOWN;
		}
		return Likelihood.valueOf(text);
	}
	
	private static Vision getVisionService() throws Exception {
		try {
			HttpRequestInitializer credentials = CredentialsProvider.getCredentials(VisionScopes.all().toArray(new String[0]));
			JsonFactory jsonFactory = JacksonFactory.getDefaultInstance();
			return new Vision.Builder(GoogleNetHttpTransport.newTrustedTransport(), jsonFactory, credentials).setApplicationName("feel-vision").build();			
		}
		catch (Exception e) {
			logger.log(Level.SEVERE, "Error creating the Vision API service: " + e, e);
			throw e;
		}
	}
	
}
