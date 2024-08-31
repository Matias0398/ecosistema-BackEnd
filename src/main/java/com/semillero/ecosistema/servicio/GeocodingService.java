package com.semillero.ecosistema.servicio;

import jakarta.annotation.PostConstruct;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import com.opencagedata.jopencage.JOpenCageGeocoder;
import com.opencagedata.jopencage.model.JOpenCageForwardRequest;
import com.opencagedata.jopencage.model.JOpenCageResponse;

@Service
public class GeocodingService {
  
	@Value("${geocoding.api.key}")
	private String apiKey;

	private JOpenCageGeocoder jOpenCageGeocoder;

	@PostConstruct
	public void init() {
		if (apiKey == null || apiKey.trim().isEmpty()) {
			throw new IllegalStateException("API key for geocoding service is not set.");
		}
		jOpenCageGeocoder = new JOpenCageGeocoder(apiKey);
	}

	public JOpenCageResponse doForwardRequest(String query) {
		if (query == null || query.trim().isEmpty()) {
			throw new IllegalArgumentException("Query cannot be null or empty.");
		}

		JOpenCageForwardRequest request = new JOpenCageForwardRequest(query);
		JOpenCageResponse response = jOpenCageGeocoder.forward(request);

		if (response == null) {
			// Handle the case where response is null
			throw new RuntimeException("Geocoding service response is null.");
		}

		return response;
	}
}
